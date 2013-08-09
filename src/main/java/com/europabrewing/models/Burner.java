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
@Table(name = "burner")
public class Burner extends PinController {

	private Integer burnerId;

	private String description;

	private TempMonitor tempMonitor;

	private Pump pump;

	/**
	 * Fire off a new thread to automate this burner's temperature
	 *
	 * @param b
	 */
	public void automateTemp(boolean b) {
		// TODO
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

	@Column(name = "description", nullable = true, insertable = true, updatable = true, length = 500)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@OneToOne(optional = true)
	@JoinColumn(name = "monitor_id", referencedColumnName = "monitor_id")
	public TempMonitor getTempMonitor() {
		return tempMonitor;
	}

	public void setTempMonitor(TempMonitor tempMonitor) {
		this.tempMonitor = tempMonitor;
	}

	@OneToOne(optional = true)
	@JoinColumn(name = "pump_id", referencedColumnName = "pump_id")
	public Pump getPump() {
		return pump;
	}

	public void setPump(Pump pump) {
		this.pump = pump;
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
