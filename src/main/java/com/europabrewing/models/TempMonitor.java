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

import com.europabrewing.lib.Temp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

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

	public static final String FILENAME = "w1_slave";

	private Integer monitorId;

	private String name;

	private String serial;

	private String directory;

	private Boolean disabled;

	private Burner burner;

	private Temp temp;

	private Long updatedTime;

	/**
	 * Initialize the collection of temperature readings from
	 * the file on disk
	 */
	public void initTempCollection() {
		TempUpdater tempUpdater = new TempUpdater(this);
		tempUpdater.start();
	}

	/**
	 * Get the last time this Temp was updated
	 *
	 * @return
	 */
	@Transient
	public Long getUpdatedTime() {
		return updatedTime;
	}

	/**
	 * Return the current temperature of this TempMonitor
	 *
	 * @return the current Temp
	 */
	@Transient
	public Temp getTemp() {
		return temp;
	}

	/**
	 * Set the temp
	 *
	 * @param temp
	 */
	public void setTemp(Temp temp) {
		this.updatedTime = System.currentTimeMillis();
		this.temp = temp;
	}

	/*
	 * HIBERNATE GETTERS & SETTERS
	 */

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

	@OneToOne(mappedBy = "tempMonitor")
	public Burner getBurner() {
		return burner;
	}

	public void setBurner(Burner burner) {
		this.burner = burner;
	}

	@Override
	public String toString() {
		return String.format("%s - %d(%s)", getName(), getMonitorId(), getDisabled() ? "disabled" : "enabled");
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

	/**
	 * A threaded class to update the temperature of a TempMonitor
	 */
	public static class TempUpdater extends Thread {

		public static final int SLEEP_TIME = 5000;

		private static final Logger logger = LogManager.getLogger(TempUpdater.class.getName());

		private static final String SEPARATOR = "/";

		private static final int MAX_TRIES = 2;

		private final TempMonitor tempMonitor;

		private final String filePath;

		public TempUpdater(TempMonitor tempMonitor) {
			this.tempMonitor = tempMonitor;
			this.filePath = String.format(
					"%s%s%s%s%s", tempMonitor.getDirectory(), SEPARATOR,
					tempMonitor.getSerial(), SEPARATOR, FILENAME);

			logger.trace(String.format("Created new TempUpdater class to monitor %s at file %s", tempMonitor, filePath));
		}

		/**
		 * Take the contents of the temp file and parse it.  If the reading
		 * failed, null will be returned
		 *
		 * @param line1
		 * @param line2
		 * @return
		 */
		private static Temp parse(String line1, String line2) {
			try {

				// match a line that looks like: "94 01 4b 46 7f ff 0c 10 26 : crc=26 YES"
				if (line1.endsWith("YES")) {

					// match a line that looks like: "94 01 4b 46 7f ff 0c 10 26 t=25250"
					Double cTemp = Double.valueOf(line2.substring(line2.indexOf("t=") + 2)) / 1000;
					Temp temp = new Temp(Temp.UNIT.F);
					temp.setTemp(Temp.convertToFahrenheit(cTemp));

					return temp;
				}

			} catch (Exception e) {
				logger.error("Error trying to parse temp from file", e);
			}

			return null;
		}

		@Override
		public void run() {
			String line1, line2;
			BufferedReader br = null;

			// never stop running
			while (true) {
				try {
					int tries = 0;
					Temp temp = null;

					// sometimes reading the temp fails, so we'll try a couple times
					while (null == temp && tries < MAX_TRIES) {
						tries++;
						br = new BufferedReader(new FileReader(filePath));
						line1 = br.readLine();
						line2 = br.readLine();
						br.close();

						temp = parse(line1, line2);

						if (null == temp)
							logger.debug(String.format("Try number %d failed to read temp from %s", tries, tempMonitor));
					}

					if (null != temp)
						tempMonitor.setTemp(temp);

					logger.trace(String.format("Gathered temperature for %s which was %s", tempMonitor, temp));

					Thread.sleep(SLEEP_TIME);

				} catch (Exception e) {
					logger.error("Error reading temperature", e);
				} finally {
					if (br != null) {
						try {
							br.close();
						} catch (IOException ignored) {
						}
					}
				}
			}
		}
	}
}
