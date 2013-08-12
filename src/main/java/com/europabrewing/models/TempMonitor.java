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
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;

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

		private final static Logger logger = LogManager.getLogger(TempUpdater.class.getName());

		private final TempMonitor tempMonitor;

		private final File file;

		private StringBuilder contents;

		public TempUpdater(TempMonitor tempMonitor) {
			logger.trace("Created new TempUpdater class to monitor " + tempMonitor);

			this.tempMonitor = tempMonitor;

			String fileName = String.format(
					"%s%s%s%s%s",
					tempMonitor.getDirectory(),
					File.pathSeparator,
					tempMonitor.getSerial(),
					File.pathSeparator,
					FILENAME);

			this.file = new File(fileName);
		}

		@Override
		public void run() {
			FileInputStream fin = null;

			try {
				final FileChannel channel;
				final MappedByteBuffer buffer;

				fin = new FileInputStream(file);
				channel = fin.getChannel();
				buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, file.length());

				while (true) {
					try {
						contents = new StringBuilder();
						for (int i = 0; i < buffer.limit(); i++) {
							buffer.get();
						}

						Temp temp = parse(contents.toString());
						tempMonitor.setTemp(temp);

						logger.trace(String.format("Gathered temperature for %s which was %s", tempMonitor, temp));

						Thread.sleep(SLEEP_TIME);
					} catch (Exception e) {
						logger.error("Error reading temperature", e);
					}
				}

			} catch (Exception e) {
				logger.error("Error setting up files to read temperature, abandoning thread!", e);
			} finally {
				if (fin != null) {
					try {
						fin.close();
					} catch (IOException ignored) {
					}
				}
			}
		}

		/**
		 * Take the contents of the temp file and parse it.  If the reading
		 * failed, null will be returned
		 *
		 * @param contents
		 * @return
		 */
		private Temp parse(String contents) {
			try {
				List<String> lines = Lists.newArrayList(Splitter.on("\n").split(contents));

				// match a line that looks like: "94 01 4b 46 7f ff 0c 10 26 : crc=26 YES"
				if (lines.get(0).endsWith("YES")) {

					// match a line that looks like: "94 01 4b 46 7f ff 0c 10 26 t=25250"
					Double cTemp = Double.valueOf(lines.get(1).substring(lines.get(1).indexOf("t=") + 2)) / 1000;
					Temp temp = new Temp(Temp.UNIT.F);
					temp.setTemp(Temp.convertToFahrenheit(cTemp));

					return temp;
				}

			} catch (Exception e) {
				logger.error("Error trying to parse temp from file", e);
			}

			return null;
		}
	}
}
