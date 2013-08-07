/*
 * Copyright © 2013 Jarett Creason
 *
 * This file is part of BrewNinja.
 *
 * BrewNinja is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BrewNinja is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with BrewNinja in the file named COPYING in the root directory.
 * If not, see <http://www.gnu.org/licenses/>.
 */

package com.europabrewing;

import com.europabrewing.daos.BurnerDAOHibernate;
import com.europabrewing.daos.PumpDAOHibernate;
import com.europabrewing.models.Burner;
import com.europabrewing.models.PinController;
import com.europabrewing.models.Pump;
import com.europabrewing.util.HibernateUtil;
import com.europabrewing.util.NullOutputStream;
import com.europabrewing.util.SysInfoUtil;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import java.io.PrintStream;
import java.util.List;

/**
 * @author jcreason - jcreason@gmail.com
 * @date June 2013
 *
 * Please see the README and/or documentation associated
 */
public class BrewNinja extends Application {

	/**
	 * If The GPIO controller is null, means we're running in development mode
	 */
	public static final boolean DEV_MODE;

	private final static int INIT_WIDTH = 1280;

	private final static int INIT_HEIGHT = 800;

	private final static Logger logger = LogManager.getLogger(BrewNinja.class.getName());

	private static final GpioController gpioController;

	private List<Burner> burners;

	private List<Pump> pumps;

	static {
		/*
		 * Create SINGLE GPIOController instance on initialization
		 * Do some shenanigans here redirect STDERR to NULL to stop the Pi4J
		 * library from printing an error directly to STDERR.  Changing the logging levels
		 * was not catching a stack trace when running this on non-RaspberryPi hardware,
		 * and that was annoying, so I capture that the brute force way.
		 * If you find this, and know a better solution, then by all means, implement it.
		 */
		GpioController tmpGpioCon = null;
		PrintStream origErrOut = System.err;
		try {
			System.setErr(new PrintStream(new NullOutputStream()));
			tmpGpioCon = GpioFactory.getInstance();
		} catch (UnsatisfiedLinkError e) {
			logger.warn("Looks like you might not be running on a Raspberry Pi architecture.  Running in DEV mode.");
		} finally {
			System.setErr(origErrOut);
		}
		gpioController = tmpGpioCon;

		// if the GPIO Controller is null after the fact, it couldn't be initialized,
		// meaning we're not running on a RaspPi, so set us up to be in "DEV_MODE"
		DEV_MODE = null == gpioController;
	}

	/**
	 * Build the class the manages it all.
	 * On construction, all equipment is loaded in from the database
	 */
	public BrewNinja() {
		initializeEquipmunk();

		logger.trace("All burners configured:\n\t * " + Joiner.on("\n\t * ").join(burners));
		logger.trace("All pumps configured:\n\t * " + Joiner.on("\n\t * ").join(pumps));

		if (!DEV_MODE) {
			SysInfoUtil.logInfo();
		}
	}

	/**
	 * MAIN
	 *
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("BrewNinja");

		Button btn = new Button();
		btn.setText("Say 'Hello World'");
		btn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				System.out.println("Hello World!");
			}
		});


		final StackPane root = new StackPane();
		root.setPrefWidth(INIT_WIDTH);
		root.setPrefHeight(INIT_HEIGHT);
		root.getChildren().add(btn);

		Scale scale = new Scale(1, 1, 0, 0);
		scale.xProperty().bind(root.widthProperty().divide(INIT_WIDTH));
		scale.yProperty().bind(root.heightProperty().divide(INIT_HEIGHT));
		root.getTransforms().add(scale);

		final Scene scene = new Scene(root, INIT_WIDTH, INIT_HEIGHT);

		stage.setScene(scene);
		stage.setResizable(false);
		stage.setFullScreen(true);
		stage.show();
	}

	/**
	 * Test all of the PinControllers by turning them on, then back off.
	 * Prints to STDOUT
	 *
	 * @throws InterruptedException
	 */
	public void testPinControllers() throws InterruptedException {
		if (DEV_MODE) {
			System.out.println("Running in DEV mode, not going to test pins we don't have...");
			return;
		}

		System.out.println("Turning on all burners");
		for (Burner burner : burners) {
			burner.turnOn();
			Thread.sleep(3000);
		}
		Thread.sleep(10000);

		System.out.println("Turning on all pumps");
		for (Pump pump : pumps) {
			pump.turnOn();
			Thread.sleep(3000);
		}
		Thread.sleep(10000);

		System.out.println("Turning off all burners");
		for (Burner burner : burners) {
			burner.turnOff();
			Thread.sleep(3000);
		}
		Thread.sleep(10000);

		System.out.println("Turning off all pumps");
		for (Pump pump : pumps) {
			pump.turnOff();
			Thread.sleep(3000);
		}
	}

	/**
	 * Shut everything down
	 */
	public void shutdown() {
		if (null != gpioController) {
			gpioController.shutdown();
		}
	}

	/**
	 * Initialize all of the equipment including
	 * Burners & liquid Pumps
	 */
	private void initializeEquipmunk() {
		logger.trace("Retrieving all equipment from database");

		Session session = HibernateUtil.getSession();

		this.burners = new BurnerDAOHibernate(session).getEnabledBurners();
		this.pumps = new PumpDAOHibernate(session).getEnabledPumps();

		if (!DEV_MODE) {
			// both pumps and burners inherit from PinController, so combine and couple with
			// the pins outputs on the RaspberryPi board
			Iterable<PinController> pinControllers = Iterables.concat(burners, pumps);
			for (PinController pinController : pinControllers) {
				pinController.couplePin(gpioController);
			}
		}

		session.close();
	}
}
