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

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

import javax.persistence.*;
import java.lang.reflect.Field;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * @author jcreason - jcreason@gmail.com
 * @date July 2013
 *
 * Please see the README and/or documentation associated
 */
@Entity
@Table(name = "gpio")
public class Gpio {

	private Integer gpioId;

	private String raspPiName;

	private Integer pin;

	private Pump pump;

	private Burner burner;

	public Pin convertToPiPin() {
		try {
			Field raspPiPin = RaspiPin.class.getField(raspPiName.toUpperCase());
			return (Pin) raspPiPin.get(null);
		} catch (NoSuchFieldException e) {
			return null;
		} catch (IllegalAccessException e) {
			return null;
		}
	}

	/*
	 * HIBERNATE GETTERS & SETTERS
	 */

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "gpio_id", nullable = false, unique = true)
	public Integer getGpioId() {
		return gpioId;
	}

	public void setGpioId(Integer gpioId) {
		this.gpioId = gpioId;
	}

	@Column(name = "rasp_pi_name", nullable = false, insertable = true, updatable = true, length = 20, unique = true)
	public String getRaspPiName() {
		return raspPiName;
	}

	public void setRaspPiName(String raspPiName) {
		this.raspPiName = raspPiName;
	}

	@Column(name = "pin", nullable = false, insertable = true, updatable = true, precision = 0, unique = true)
	public Integer getPin() {
		return pin;
	}

	public void setPin(Integer pin) {
		this.pin = pin;
	}

	@OneToOne(mappedBy = "gpio", optional = true)
	public Pump getPump() {
		return pump;
	}

	public void setPump(Pump pump) {
		this.pump = pump;
	}

	@OneToOne(mappedBy = "gpio", optional = true)
	public Burner getBurner() {
		return burner;
	}

	public void setBurner(Burner burner) {
		this.burner = burner;
	}

	@Override
	public String toString() {
		return String.format("Gpio %d (%s)", pin, raspPiName);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Gpio gpio = (Gpio) o;

		if (gpioId != null ? !gpioId.equals(gpio.gpioId) : gpio.gpioId != null) {
			return false;
		}
		if (pin != null ? !pin.equals(gpio.pin) : gpio.pin != null) {
			return false;
		}
		if (raspPiName != null ? !raspPiName.equals(gpio.raspPiName) : gpio.raspPiName != null) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = gpioId != null ? gpioId.hashCode() : 0;
		result = 31 * result + (raspPiName != null ? raspPiName.hashCode() : 0);
		result = 31 * result + (pin != null ? pin.hashCode() : 0);
		return result;
	}
}
