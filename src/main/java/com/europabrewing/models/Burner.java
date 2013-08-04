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

import static javax.persistence.GenerationType.IDENTITY;

/**
 * @author jcreason - jcreason@gmail.com
 * @date July 2013
 *
 * Please see the README and/or documentation associated
 */
@Entity
@Table(name = "burner")
public class Burner {

	private Integer burnerId;

	private String name;

	private String description;

	private Boolean disabled;

	private TempMonitor tempMonitor;

	private Gpio gpio;

	@Transient
	private GpioPinDigitalOutput pin;

	public Burner() {
	}

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

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "burner_id", nullable = false, unique = true)
	public Integer getBurnerId() {
		return burnerId;
	}

	public void setBurnerId(Integer burnerId) {
		this.burnerId = burnerId;
	}

	@Column(name = "name", nullable = false, insertable = true, updatable = true, length = 200, unique = true)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "description", nullable = true, insertable = true, updatable = true, length = 500)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "disabled", nullable = false, insertable = true, updatable = true)
	public Boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}

	@OneToOne(optional = true)
	@JoinColumn(name = "gpio_id", referencedColumnName = "gpio_id")
	public Gpio getGpio() {
		return gpio;
	}

	public void setGpio(Gpio gpio) {
		this.gpio = gpio;
	}

	@OneToOne(optional = true)
	@JoinColumn(name = "monitor_id", referencedColumnName = "monitor_id")
	public TempMonitor getTempMonitor() {
		return tempMonitor;
	}

	public void setTempMonitor(TempMonitor tempMonitor) {
		this.tempMonitor = tempMonitor;
	}

	@Override
	public String toString() {
		return String.format("%s - %s (%s)", getName(), getDescription(), getDisabled() ? "disabled" : "enabled");
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Burner burner = (Burner) o;

		if (burnerId != null ? !burnerId.equals(burner.burnerId) : burner.burnerId != null) {
			return false;
		}
		if (description != null ? !description.equals(burner.description) : burner.description != null) {
			return false;
		}
		if (disabled != null ? !disabled.equals(burner.disabled) : burner.disabled != null) {
			return false;
		}
		if (name != null ? !name.equals(burner.name) : burner.name != null) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = burnerId != null ? burnerId.hashCode() : 0;
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (description != null ? description.hashCode() : 0);
		result = 31 * result + (disabled != null ? disabled.hashCode() : 0);
		return result;
	}
}
