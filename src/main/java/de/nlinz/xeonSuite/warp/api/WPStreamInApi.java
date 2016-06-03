package de.nlinz.xeonSuite.warp.api;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import de.nlinz.xeonSuite.bukkit.XeonSuiteBukkit;
import de.nlinz.xeonSuite.bukkit.GlobalMessageDB;
import de.nlinz.xeonSuite.bukkit.utils.LocationUtil;
import de.nlinz.xeonSuite.bukkit.utils.tables.WarpDataTable;

public class WPStreamInApi {

	public static void teleportToWarp(final String player, String world, double x, double y, double z, float yaw,
			float pitch) {
		World w = Bukkit.getWorld(world);
		Location t;

		if (w != null) {
			t = new Location(w, x, y, z, yaw, pitch);
		} else {
			w = Bukkit.getWorlds().get(0);
			t = w.getSpawnLocation();
		}
		Player p = Bukkit.getPlayer(player);
		if (p != null) {
			// Check if Block is safe
			if (LocationUtil.isBlockUnsafe(t.getWorld(), t.getBlockX(), t.getBlockY(), t.getBlockZ())) {
				try {
					Location l = LocationUtil.getSafeDestination(p, t);
					if (l != null) {
						p.teleport(l);
						p.sendMessage(GlobalMessageDB.Teleport_Warp);
					} else {
						p.sendMessage(ChatColor.RED + "Unable to find a safe location for teleport.");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				p.teleport(t);
				p.sendMessage(GlobalMessageDB.Teleport_Warp);
			}
		} else {
			WarpDataTable.pendingWarpLocations.put(player, t);
			Bukkit.getScheduler().runTaskLaterAsynchronously(XeonSuiteBukkit.getInstance(), new Runnable() {
				@Override
				public void run() {
					if (WarpDataTable.pendingWarpLocations.containsKey(player)) {
						WarpDataTable.pendingWarpLocations.remove(player);
					}

				}
			}, 100L);
		}

	}

}
