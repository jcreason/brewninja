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

import com.europabrewing.util.HibernateUtil;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

/**
 * @author jcreason - jcreason@gmail.com
 * @date June 2013
 *
 * Please see the README and/or documentation associated
 */
public class BrewNinja {

	private final static Logger logger = LogManager.getLogger(BrewNinja.class.getName());

	public final GpioController gpioController;

	public BrewNinja() {
		// create gpio controller instance
		gpioController = GpioFactory.getInstance();
	}

	/**
	 * MAIN
	 *
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		System.out.println("hello world");

		logger.error("Error message");
		logger.warn("Warn message");
		logger.info("Info message");
		logger.debug("Debug message");
		logger.trace("Trace message");

		logger.debug("Going to get the hibernate session");
		Session session = HibernateUtil.getSession();

		logger.debug("running query...");

//		session.createQuery("SELECT * FROM burner ").list();
		session.close();

		logger.debug("closed session");
	}

}
