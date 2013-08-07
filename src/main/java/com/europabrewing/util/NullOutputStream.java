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

import java.io.OutputStream;

/**
 * @author jcreason - jcreason@gmail.com
 * @date August 2013
 *
 * Please see the README and/or documentation associated
 */
public class NullOutputStream extends OutputStream {

	public NullOutputStream() {
	}

	@Override
	public void write(int b) {
	}

	@Override
	public void write(byte[] b) {
	}

	@Override
	public void write(byte[] b, int off, int len) {
	}
}
