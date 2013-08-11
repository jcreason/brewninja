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

package com.europabrewing.lib;

/**
 * @author jcreason
 * @date August 2013
 *
 * Please see the README and/or documentation associated
 */

/**
 * Represents a temperature
 */
public class Temp {

	public static enum UNIT {
		C, F
	}

	private final UNIT unit;

	private double temp;

	/**
	 * Create a new Temp object of a specific type
	 */
	public Temp(UNIT unit) {
		this.unit = unit;
	}

	/**
	 * Given a temp in celsius, convert it to fahrenheit
	 *
	 * @param c
	 * @return
	 */
	public static double convertToFahrenheit(double c) {
		return ((c * 9 / 5) + 32);
	}

	/**
	 * Given a temp in fahrenheit, convert it to celsius
	 *
	 * @param f
	 * @return
	 */
	public static double convertToCelsius(double f) {
		return ((f - 32) * 5 / 9);
	}

	/**
	 * Get the unit of this Temp object
	 *
	 * @return
	 */
	public UNIT getUnit() {
		return unit;
	}

	/**
	 * Get the temp
	 *
	 * @return
	 */
	public double getTemp() {
		return temp;
	}

	/**
	 * Set the temp
	 *
	 * @param temp
	 */
	public void setTemp(double temp) {
		this.temp = temp;
	}

	@Override
	public String toString() {
		return String.format("%.2f %s", getTemp(), getUnit());
	}
}
