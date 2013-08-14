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

import com.europabrewing.BrewNinja;
import com.europabrewing.models.TempMonitor;
import javafx.application.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author jcreason
 * @date August 2013
 *
 * A threaded class to update the temperature of a TempMonitor
 */
public class TempUpdater extends Thread {

	public static final int SLEEP_TIME = 5000;

	private static final Logger logger = LogManager.getLogger(TempUpdater.class.getName());

	private static final String SEPARATOR = "/";

	private static final int MAX_TRIES = 2;

	private final TempMonitor tempMonitor;

	private final String filePath;

	private final BrewNinja brewNinja;

	public TempUpdater(TempMonitor tempMonitor, BrewNinja brewNinja) {
		this.tempMonitor = tempMonitor;
		this.brewNinja = brewNinja;

		this.filePath = String.format("%s%s%s%s%s", tempMonitor.getDirectory(), SEPARATOR,
									  tempMonitor.getSerial(), SEPARATOR, TempMonitor.FILENAME);

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

				if (null != temp) {
					tempMonitor.setTemp(temp);

					// TODO: we should not have to do this, the GUI should be bound to the model - figure that out
					// update the GUI here, when we update the tempMonitor model
					if (brewNinja.getTempLabels().containsKey(tempMonitor)) {
						final Temp finalTemp = temp;
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								brewNinja.getTempLabels().get(tempMonitor).setText(finalTemp.toString());
							}
						});
					}
				}

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
