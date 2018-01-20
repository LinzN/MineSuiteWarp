/*
 * Copyright (C) 2018. MineGaming - All Rights Reserved
 * You may use, distribute and modify this code under the
 * terms of the LGPLv3 license, which unfortunately won't be
 * written for another century.
 *
 *  You should have received a copy of the LGPLv3 license with
 *  this file. If not, please write to: niklas.linz@enigmar.de
 *
 */

package de.linzn.mineSuite.warp.database;

import de.linzn.mineSuite.core.database.mysql.MySQLConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;


public class WarpSqlActions {


	public static HashMap<String, UUID> getWarps(int visible) {
		MySQLConnectionManager manager = MySQLConnectionManager.DEFAULT;
		try {

			Connection conn = manager.getConnection("MineSuiteWarp");
			PreparedStatement sel;
			if (visible == 0) {
				sel = conn.prepareStatement("SELECT * FROM warps;");
			} else {
				sel = conn.prepareStatement("SELECT * FROM warps WHERE visible = '" + visible + "';");
			}

			HashMap<String, UUID> list = new HashMap<String, UUID>();

			ResultSet result = sel.executeQuery();
			if (result != null) {
				while (result.next()) {
					list.put(result.getString("warp_name"), UUID.fromString(result.getString("player")));
				}
			}
			result.close();
			sel.close();
			manager.release("MineSuiteWarp", conn);

			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

}
