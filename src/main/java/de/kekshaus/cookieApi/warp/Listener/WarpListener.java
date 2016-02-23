package de.kekshaus.cookieApi.warp.Listener;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import de.kekshaus.cookieApi.bukkit.CookieApiBukkit;
import de.kekshaus.cookieApi.bukkit.GlobalMessageDB;
import de.kekshaus.cookieApi.warp.database.WarpHASHDB;

public class WarpListener implements Listener {

	@EventHandler
	public void playerConnect(PlayerSpawnLocationEvent e) {
		if (WarpHASHDB.pendingWarp.containsKey(e.getPlayer().getName())) {
			Player t = WarpHASHDB.pendingWarp.get(e.getPlayer().getName());
			WarpHASHDB.pendingWarp.remove(e.getPlayer().getName());
			if ((t == null) || (!t.isOnline())) {
				e.getPlayer().sendMessage("Player is no longer online");
				return;
			}
			WarpHASHDB.ignoreWarp.add(e.getPlayer());
			e.setSpawnLocation(t.getLocation());
			sendWarpMSG(e.getPlayer());
		} else if (WarpHASHDB.pendingWarpLocations.containsKey(e.getPlayer().getName())) {
			Location l = WarpHASHDB.pendingWarpLocations.get(e.getPlayer().getName());
			WarpHASHDB.ignoreWarp.add(e.getPlayer());
			e.setSpawnLocation(l);
			sendWarpMSG(e.getPlayer());
		}
	}

	public void sendWarpMSG(final Player p) {
		Bukkit.getScheduler().runTaskLaterAsynchronously(CookieApiBukkit.getInstance(), new Runnable() {
			@Override
			public void run() {
				WarpHASHDB.ignoreWarp.remove(p);
				p.sendMessage(GlobalMessageDB.Teleport_Warp);
			}
		}, 20);

	}
}
