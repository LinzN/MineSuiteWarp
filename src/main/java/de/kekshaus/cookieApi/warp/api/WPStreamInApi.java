package de.kekshaus.cookieApi.warp.api;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import de.kekshaus.cookieApi.bukkit.CookieApiBukkit;
import de.kekshaus.cookieApi.bukkit.GlobalMessageDB;
import de.kekshaus.cookieApi.bukkit.utils.LocationUtil;
import de.kekshaus.cookieApi.warp.database.WarpHASHDB;

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
			WarpHASHDB.pendingWarpLocations.put(player, t);
			Bukkit.getScheduler().runTaskLaterAsynchronously(CookieApiBukkit.getInstance(), new Runnable() {
				@Override
				public void run() {
					if (WarpHASHDB.pendingWarpLocations.containsKey(player)) {
						WarpHASHDB.pendingWarpLocations.remove(player);
					}

				}
			}, 100L);
		}

	}

}
