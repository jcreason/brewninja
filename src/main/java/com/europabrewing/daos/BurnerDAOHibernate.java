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

package com.europabrewing.daos;

import com.europabrewing.models.Burner;
import com.europabrewing.util.HibernateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import java.util.List;

/**
 * @author jcreason - jcreason@gmail.com
 * @date July 2013
 *
 * Please see the README and/or documentation associated
 */
public class BurnerDAOHibernate implements BurnerDAO {


	private final static Logger logger = LogManager.getLogger(BurnerDAOHibernate.class.getName());

	private final Session session;

	public BurnerDAOHibernate(Session session) {
		this.session = session;
	}

	@Override
	public List<Burner> getEnabledBurners() {
		return HibernateUtil.listAndCast(session.createQuery("from Burner where disabled = false "));
	}
}
