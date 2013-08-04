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
import com.europabrewing.util.SysInfoUtil;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * @author jcreason - jcreason@gmail.com
 * @date June 2013
 *
 * Please see the README and/or documentation associated
 */
public class BrewNinja {

	private final static Logger logger = LogManager.getLogger(BrewNinja.class.getName());

	/** The GPIO controller.  If it's null, means we're running in development mode */
	private static final GpioController gpioController;

	private static final boolean DEV_MODE;

	private List<Burner> burners;

	private List<Pump> pumps;

	static {
		// create gpio controller instance
		GpioController tmpGpioCon = null;
		try {
			try {
				tmpGpioCon = GpioFactory.getInstance();
			} catch (Exception e) {
				logger.error("another exception");
			}
		} catch (UnsatisfiedLinkError e) {
			logger.error("Looks like you might not be running on a Raspberry Pi architecture.  Running in DEV mode.");
		}
		gpioController = tmpGpioCon;

		DEV_MODE = null == gpioController;
	}


	/**
	 * Build the class the manages it all.
	 * On construction, all equipment is loaded in from the database
	 */
	public BrewNinja() {
		initializeEquipmunk();

		logger.trace("All burners configured:\n\t * " + Joiner.on("\n\t * ").join(burners));

		if (!DEV_MODE) {
			try {
				SysInfoUtil.logInfo();
			} catch (IOException | InterruptedException | ParseException e) {
				logger.warn("Exception thrown while trying to log system info.\n", e);
			}
		}

	}

	/**
	 * MAIN
	 *
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		BrewNinja brewNinja = null;

		try {
			brewNinja = new BrewNinja();

			brewNinja.testPinControllers();

		} catch (Exception t) {
			if (brewNinja != null) {
				brewNinja.shutdown();
			}
		}
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
			Thread.sleep(100);
		}
		Thread.sleep(500);

		System.out.println("Turning on all pumps");
		for (Pump pump : pumps) {
			pump.turnOn();
			Thread.sleep(100);
		}
		Thread.sleep(500);

		System.out.println("Turning off all burners");
		for (Burner burner : burners) {
			burner.turnOff();
			Thread.sleep(100);
		}
		Thread.sleep(500);

		System.out.println("Turning off all pumps");
		for (Pump pump : pumps) {
			pump.turnOff();
			Thread.sleep(100);
		}
	}

	/** Shut everything down */
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
