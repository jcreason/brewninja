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

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * @author jcreason - jcreason@gmail.com
 * @date July 2013
 *
 * Please see the README and/or documentation associated
 */
@Entity
public class Valve {
	private Integer valveId;

	private String name;

	private Boolean disabled;

	private Burner burner;

	private Gpio gpio;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "valve_id", nullable = false, unique = true)
	public Integer getValveId() {
		return valveId;
	}

	public void setValveId(Integer valveId) {
		this.valveId = valveId;
	}

	@Column(name = "name", nullable = false, insertable = true, updatable = true, length = 200)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "disabled", nullable = false, insertable = true, updatable = true)
	public Boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}

	@ManyToOne
	@JoinColumn(name = "burner_id", referencedColumnName = "burner_id", nullable = false)
	public Burner getBurner() {
		return burner;
	}

	public void setBurner(Burner burnerByBurnerId) {
		this.burner = burnerByBurnerId;
	}

	@ManyToOne
	@JoinColumn(name = "gpio_id", referencedColumnName = "gpio_id")
	public Gpio getGpio() {
		return gpio;
	}

	public void setGpio(Gpio gpioByGpioId) {
		this.gpio = gpioByGpioId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Valve valve = (Valve) o;

		if (disabled != null ? !disabled.equals(valve.disabled) : valve.disabled != null) {
			return false;
		}
		if (name != null ? !name.equals(valve.name) : valve.name != null) {
			return false;
		}
		if (valveId != null ? !valveId.equals(valve.valveId) : valve.valveId != null) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = valveId != null ? valveId.hashCode() : 0;
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (disabled != null ? disabled.hashCode() : 0);
		return result;
	}
}
