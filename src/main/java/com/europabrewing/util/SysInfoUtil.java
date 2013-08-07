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

package com.europabrewing.util;

import com.pi4j.system.NetworkInfo;
import com.pi4j.system.SystemInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.text.ParseException;

/**
 * @author jcreason - jcreason@gmail.com
 * @date August 2013
 *
 * Please see the README and/or documentation associated
 */
public class SysInfoUtil {

	private final static Logger logger = LogManager.getLogger(SysInfoUtil.class.getName());

	/**
	 * Log at the INFO level a bunch of data about this Raspberry Pi.
	 *
	 * Code pulled from example at: http://pi4j.com/example/system-info.html
	 */
	public static void logInfo() {

		try {
			// display a few of the available system information properties
			logger.info("----------------------------------------------------");
			logger.info("HARDWARE INFO");
			logger.info("----------------------------------------------------");
			logger.info("Serial Number     :  " + SystemInfo.getSerial());
			logger.info("CPU Revision      :  " + SystemInfo.getCpuRevision());
			logger.info("CPU Architecture  :  " + SystemInfo.getCpuArchitecture());
			logger.info("CPU Part          :  " + SystemInfo.getCpuPart());
			logger.info("CPU Temperature   :  " + SystemInfo.getCpuTemperature());
			logger.info("CPU Core Voltage  :  " + SystemInfo.getCpuVoltage());
			logger.info("MIPS              :  " + SystemInfo.getBogoMIPS());
			logger.info("Processor         :  " + SystemInfo.getProcessor());
			logger.info("Hardware Revision :  " + SystemInfo.getRevision());
			logger.info("Is Hard Float ABI :  " + SystemInfo.isHardFloatAbi());
			logger.info("Board Type        :  " + SystemInfo.getBoardType().name());

			logger.info("----------------------------------------------------");
			logger.info("MEMORY INFO");
			logger.info("----------------------------------------------------");
			logger.info("Total Memory      :  " + SystemInfo.getMemoryTotal());
			logger.info("Used Memory       :  " + SystemInfo.getMemoryUsed());
			logger.info("Free Memory       :  " + SystemInfo.getMemoryFree());
			logger.info("Shared Memory     :  " + SystemInfo.getMemoryShared());
			logger.info("Memory Buffers    :  " + SystemInfo.getMemoryBuffers());
			logger.info("Cached Memory     :  " + SystemInfo.getMemoryCached());
			logger.info("SDRAM_C Voltage   :  " + SystemInfo.getMemoryVoltageSDRam_C());
			logger.info("SDRAM_I Voltage   :  " + SystemInfo.getMemoryVoltageSDRam_I());
			logger.info("SDRAM_P Voltage   :  " + SystemInfo.getMemoryVoltageSDRam_P());

			logger.info("----------------------------------------------------");
			logger.info("OPERATING SYSTEM INFO");
			logger.info("----------------------------------------------------");
			logger.info("OS Name           :  " + SystemInfo.getOsName());
			logger.info("OS Version        :  " + SystemInfo.getOsVersion());
			logger.info("OS Architecture   :  " + SystemInfo.getOsArch());
			logger.info("OS Firmware Build :  " + SystemInfo.getOsFirmwareBuild());
			logger.info("OS Firmware Date  :  " + SystemInfo.getOsFirmwareDate());

			logger.info("----------------------------------------------------");
			logger.info("JAVA ENVIRONMENT INFO");
			logger.info("----------------------------------------------------");
			logger.info("Java Vendor       :  " + SystemInfo.getJavaVendor());
			logger.info("Java Vendor URL   :  " + SystemInfo.getJavaVendorUrl());
			logger.info("Java Version      :  " + SystemInfo.getJavaVersion());
			logger.info("Java VM           :  " + SystemInfo.getJavaVirtualMachine());
			logger.info("Java Runtime      :  " + SystemInfo.getJavaRuntime());

			logger.info("----------------------------------------------------");
			logger.info("NETWORK INFO");
			logger.info("----------------------------------------------------");

			// display some of the network information
			logger.info("Hostname          :  " + NetworkInfo.getHostname());
			for (String ipAddress : NetworkInfo.getIPAddresses()) {
				logger.info("IP Addresses      :  " + ipAddress);
			}
			for (String fqdn : NetworkInfo.getFQDNs()) {
				logger.info("FQDN              :  " + fqdn);
			}
			for (String nameserver : NetworkInfo.getNameservers()) {
				logger.info("Nameserver        :  " + nameserver);
			}

			logger.info("----------------------------------------------------");
			logger.info("CODEC INFO");
			logger.info("----------------------------------------------------");
			logger.info("H264 Codec Enabled:  " + SystemInfo.getCodecH264Enabled());
			logger.info("MPG2 Codec Enabled:  " + SystemInfo.getCodecMPG2Enabled());
			logger.info("WVC1 Codec Enabled:  " + SystemInfo.getCodecWVC1Enabled());

			logger.info("----------------------------------------------------");
			logger.info("CLOCK INFO");
			logger.info("----------------------------------------------------");
			logger.info("ARM Frequency     :  " + SystemInfo.getClockFrequencyArm());
			logger.info("CORE Frequency    :  " + SystemInfo.getClockFrequencyCore());
			logger.info("H264 Frequency    :  " + SystemInfo.getClockFrequencyH264());
			logger.info("ISP Frequency     :  " + SystemInfo.getClockFrequencyISP());
			logger.info("V3D Frequency     :  " + SystemInfo.getClockFrequencyV3D());
			logger.info("UART Frequency    :  " + SystemInfo.getClockFrequencyUART());
			logger.info("PWM Frequency     :  " + SystemInfo.getClockFrequencyPWM());
			logger.info("EMMC Frequency    :  " + SystemInfo.getClockFrequencyEMMC());
			logger.info("Pixel Frequency   :  " + SystemInfo.getClockFrequencyPixel());
			logger.info("VEC Frequency     :  " + SystemInfo.getClockFrequencyVEC());
			logger.info("HDMI Frequency    :  " + SystemInfo.getClockFrequencyHDMI());
			logger.info("DPI Frequency     :  " + SystemInfo.getClockFrequencyDPI());

		} catch (InterruptedException | ParseException | IOException e) {
			logger.error(e);
		}
	}
}
