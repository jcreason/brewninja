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

package com.europabrewing.models;


import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;

import javax.persistence.*;


/**
 * @author jcreason - jcreason@gmail.com
 * @date August 2013
 *
 * Please see the README and/or documentation associated
 */
@MappedSuperclass
public abstract class PinController {

	protected Gpio gpio;

	protected String name;

	@Transient
	protected GpioPinDigitalOutput pin;

	/**
	 * Couple this burner with the GPIO pin on the RaspPi board
	 *
	 * @param gpioController use this controller
	 * @return result of coupling
	 */
	public boolean couplePin(GpioController gpioController) {
		Pin piPin = gpio.convertToPiPin();
		if (null == piPin) {
			return false;
		}
		this.pin = gpioController.provisionDigitalOutputPin(piPin, name, PinState.LOW);
		return true;
	}

	/**
	 * Turn this Burner's valve on
	 *
	 * @return
	 */
	public void turnOn() {
		pin.high();
	}

	/**
	 * Turn this Burner's valve off
	 *
	 * @return
	 */
	public void turnOff() {
		pin.low();
	}

	/**
	 * Toggle this Burner's valve on or off (whatever it's not now)
	 *
	 * @return
	 */
	public void toggle() {
		pin.toggle();
	}

	/**
	 * Is this Burner's valve on?
	 *
	 * @return
	 */
	@Transient
	public boolean isOn() {
		return pin.isHigh();
	}

	/*
	 * HIBERNATE GETTERS & SETTERS
	 */

	@Basic
	@Column(name = "name", nullable = false, insertable = true, updatable = true, length = 200)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@OneToOne(optional = true)
	@JoinColumn(name = "gpio_id", referencedColumnName = "gpio_id")
	public Gpio getGpio() {
		return gpio;
	}

	public void setGpio(Gpio gpio) {
		this.gpio = gpio;
	}
}
