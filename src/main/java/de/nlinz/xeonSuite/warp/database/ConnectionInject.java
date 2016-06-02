package de.nlinz.xeonSuite.warp.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ConnectionInject {

	public static void setWarp(UUID uuid, String warp, String server, String world, double x, double y, double z,
			float yaw, float pitch, int visible) {
		ConnectionManager manager = ConnectionManager.DEFAULT;
		try {
			Connection conn = manager.getConnection("minewarp");
			PreparedStatement sql = conn
					.prepareStatement("SELECT warp_name FROM warps WHERE warp_name = '" + warp + "';");
			ResultSet result = sql.executeQuery();
			if (result.next()) {
				PreparedStatement update = conn.prepareStatement("UPDATE warps SET server = '" + server + "', world = '"
						+ world + "', x = '" + x + "', y = '" + y + "', z = '" + z + "', yaw = '" + yaw + "', pitch = '"
						+ pitch + "', visible = " + visible + " WHERE warp_name = '" + warp + "';");
				update.executeUpdate();
				update.close();
			} else {
				PreparedStatement insert = conn.prepareStatement(
						"INSERT INTO warps (player, warp_name, server, world, x, y, z, yaw, pitch, visible) VALUES ('"
								+ uuid.toString() + "', '" + warp + "', '" + server + "', '" + world + "', '" + x
								+ "', '" + y + "', '" + z + "', '" + yaw + "', '" + pitch + "', '" + visible + "');");
				insert.executeUpdate();
				insert.close();
			}
			result.close();
			sql.close();
			manager.release("minewarp", conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void delWarp(String warp) {
		ConnectionManager manager = ConnectionManager.DEFAULT;
		try {
			Connection conn = manager.getConnection("minewarp");
			PreparedStatement sql = conn
					.prepareStatement("SELECT warp_name FROM warps WHERE warp_name = '" + warp + "';");
			ResultSet result = sql.executeQuery();
			if (result.next()) {
				PreparedStatement update = conn.prepareStatement("DELETE FROM warps WHERE warp_name = '" + warp + "';");
				update.executeUpdate();
				update.close();
			}
			result.close();
			sql.close();
			manager.release("minewarp", conn);

		}

		catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static List<String> getWarp(String warp) {
		final List<String> rlist = new ArrayList<String>();

		ConnectionManager manager = ConnectionManager.DEFAULT;
		try {
			Connection conn = manager.getConnection("minewarp");
			PreparedStatement sql = conn.prepareStatement(
					"SELECT world, server, x, y, z, yaw, pitch FROM warps WHERE warp_name = '" + warp + "';");
			final ResultSet result = sql.executeQuery();
			if (result.next()) {
				rlist.add(0, "empty");
				rlist.add(1, result.getString(1));
				rlist.add(2, result.getString(2));
				rlist.add(3, result.getString(3));
				rlist.add(4, result.getString(4));
				rlist.add(5, result.getString(5));
				rlist.add(6, result.getString(6));
				rlist.add(7, result.getString(7));
				result.close();
				sql.close();
				manager.release("minewarp", conn);
			} else {
				result.close();
				sql.close();
				manager.release("minewarp", conn);
				return null;
			}
			return rlist;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean isWarp(String warp) {
		boolean isWarp = false;
		ConnectionManager manager = ConnectionManager.DEFAULT;
		try {
			Connection conn = manager.getConnection("minewarp");
			PreparedStatement sql = conn
					.prepareStatement("SELECT warp_name FROM warps WHERE warp_name = '" + warp + "';");
			ResultSet result = sql.executeQuery();
			if (result.next()) {
				isWarp = true;
			} else {
				isWarp = false;
			}
			result.close();
			sql.close();
			manager.release("minewarp", conn);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return isWarp;
	}

	public static HashMap<String, UUID> getWarps(int visible) {
		ConnectionManager manager = ConnectionManager.DEFAULT;
		try {

			Connection conn = manager.getConnection("minewarp");
			PreparedStatement sel;
			if (visible == 0) {
				sel = conn.prepareStatement("SELECT * FROM warps;");
			} else {
				sel = conn.prepareStatement("SELECT * FROM warps WHERE visible = '" + visible + "';");
			}

			HashMap<String, UUID> list = new HashMap<String, UUID>();
			try {
				ResultSet result = sel.executeQuery();
				if (result != null) {
					while (result.next()) {
						list.put(result.getString("warp_name"), UUID.fromString(result.getString("player")));
					}
				}
				result.close();
				sel.close();
				manager.release("minewarp", conn);
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

}
