package de.nlinz.xeonSuite.warp.Listener;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import de.nlinz.xeonSuite.bukkit.XeonSuiteBukkit;
import de.nlinz.xeonSuite.bukkit.utils.languages.WarpLanguage;
import de.nlinz.xeonSuite.bukkit.utils.tables.WarpDataTable;

public class WarpListener implements Listener {

	@EventHandler
	public void playerConnect(PlayerSpawnLocationEvent e) {
		if (WarpDataTable.pendingWarp.containsKey(e.getPlayer().getName())) {
			Player t = WarpDataTable.pendingWarp.get(e.getPlayer().getName());
			WarpDataTable.pendingWarp.remove(e.getPlayer().getName());
			if ((t == null) || (!t.isOnline())) {
				e.getPlayer().sendMessage("Player is no longer online");
				return;
			}
			WarpDataTable.ignoreWarp.add(e.getPlayer());
			e.setSpawnLocation(t.getLocation());
			sendWarpMSG(e.getPlayer());
		} else if (WarpDataTable.pendingWarpLocations.containsKey(e.getPlayer().getName())) {
			Location l = WarpDataTable.pendingWarpLocations.get(e.getPlayer().getName());
			WarpDataTable.ignoreWarp.add(e.getPlayer());
			e.setSpawnLocation(l);
			sendWarpMSG(e.getPlayer());
		}
	}

	public void sendWarpMSG(final Player p) {
		Bukkit.getScheduler().runTaskLaterAsynchronously(XeonSuiteBukkit.getInstance(), new Runnable() {
			@Override
			public void run() {
				WarpDataTable.ignoreWarp.remove(p);
				p.sendMessage(WarpLanguage.Teleport_Warp);
			}
		}, 20);

	}
}
