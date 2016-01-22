package de.kekshaus.cookieApi.warp.commands;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.bukkit.entity.Player;

import de.kekshaus.cookieApi.bukkit.managerApi.WarpApi;
import de.kekshaus.cookieApi.warp.Warpplugin;
import de.kekshaus.cookieApi.warp.database.ConnectionInject;
import net.md_5.bungee.api.ChatColor;

public class WarpPortalApi {
	public static ThreadPoolExecutor executorServiceCommands = new ThreadPoolExecutor(1, 1, 250L, TimeUnit.MILLISECONDS,
			new LinkedBlockingQueue<Runnable>());

	public WarpPortalApi(Warpplugin instance) {

	}

	public static boolean warp(final Player player, final String warpName) {

		executorServiceCommands.submit(new Runnable() {
			public void run() {

				if (ConnectionInject.isWarp(warpName)) {

					List<String> list = ConnectionInject.getWarp(warpName);
					String world = list.get(1);
					String server = list.get(2);

					double x = Double.parseDouble(list.get(3));
					double y = Double.parseDouble(list.get(4));
					double z = Double.parseDouble(list.get(5));
					float yaw = Float.parseFloat(list.get(6));
					float pitch = Float.parseFloat(list.get(7));

					WarpApi.sendTeleportToWarpOut(player.getName(), server, world, x, y, z, yaw, pitch);

					return;
				} else {
					player.sendMessage(ChatColor.RED + "Warp ist nicht verfügbar!");
				}
			}

		});

		return false;
	}
}
