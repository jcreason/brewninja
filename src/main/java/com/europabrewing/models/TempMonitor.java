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
@Table(name = "temp_monitor")
public class TempMonitor {
	private Integer monitorId;

	private String name;

	private String serial;

	private String directory;

	private Boolean disabled;

	private Burner burner;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "monitor_id", nullable = false, unique = true)
	public Integer getMonitorId() {
		return monitorId;
	}

	public void setMonitorId(Integer monitorId) {
		this.monitorId = monitorId;
	}

	@Column(name = "name", nullable = false, insertable = true, updatable = true, length = 200, unique = true)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "serial", nullable = true, insertable = true, updatable = true, length = 100, unique = true)
	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	@Column(name = "directory", nullable = true, insertable = true, updatable = true, length = 200)
	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

	@Column(name = "disabled", nullable = false, insertable = true, updatable = true)
	public Boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}

	@ManyToOne
	@JoinColumn(name = "burner_id", referencedColumnName = "burner_id")
	public Burner getBurner() {
		return burner;
	}

	public void setBurner(Burner burner) {
		this.burner = burner;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		TempMonitor that = (TempMonitor) o;

		if (directory != null ? !directory.equals(that.directory) : that.directory != null) {
			return false;
		}
		if (disabled != null ? !disabled.equals(that.disabled) : that.disabled != null) {
			return false;
		}
		if (monitorId != null ? !monitorId.equals(that.monitorId) : that.monitorId != null) {
			return false;
		}
		if (name != null ? !name.equals(that.name) : that.name != null) {
			return false;
		}
		if (serial != null ? !serial.equals(that.serial) : that.serial != null) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = monitorId != null ? monitorId.hashCode() : 0;
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (serial != null ? serial.hashCode() : 0);
		result = 31 * result + (directory != null ? directory.hashCode() : 0);
		result = 31 * result + (disabled != null ? disabled.hashCode() : 0);
		return result;
	}
}
