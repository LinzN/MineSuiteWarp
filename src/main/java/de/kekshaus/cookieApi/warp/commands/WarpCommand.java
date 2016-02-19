package de.kekshaus.cookieApi.warp.commands;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.kekshaus.cookieApi.bukkit.CookieApiBukkit;
import de.kekshaus.cookieApi.bukkit.MessageDB;
import de.kekshaus.cookieApi.warp.Warpplugin;
import de.kekshaus.cookieApi.warp.api.WPStreamOutApi;
import de.kekshaus.cookieApi.warp.database.ConnectionInject;
import de.kekshaus.cookieApi.warp.database.WarpHASHDB;
import net.md_5.bungee.api.ChatColor;

public class WarpCommand implements CommandExecutor {
	public ThreadPoolExecutor executorServiceCommands = new ThreadPoolExecutor(1, 1, 250L, TimeUnit.MILLISECONDS,
			new LinkedBlockingQueue<Runnable>());

	public WarpCommand(Warpplugin instance) {

	}

	public boolean onCommand(final CommandSender sender, Command cmd, String label, final String[] args) {
		final Player player = (Player) sender;
		if (player.hasPermission("cookieApi.warp.warp")) {
			this.executorServiceCommands.submit(new Runnable() {
				public void run() {
					if (sender instanceof Player) {
						final Player player = (Player) sender;
						if ((args.length >= 1)) {
							final String warpName = args[0].toLowerCase();

							if (ConnectionInject.isWarp(warpName)) {
								if (!player.hasPermission("cookieApi.bypass")) {
									WarpHASHDB.lastWarpLocation.put(player, player.getLocation());
									player.sendMessage(MessageDB.TELEPORT_TIMER.replace("{TIME}",
											String.valueOf(CookieApiBukkit.getWarmUpTime())));
									Warpplugin.inst().getServer().getScheduler().runTaskLater(Warpplugin.inst(),
											new Runnable() {
										@Override
										public void run() {

											Location loc = WarpHASHDB.lastWarpLocation.get(player);
											WarpHASHDB.lastWarpLocation.remove(player);
											if ((loc != null)
													&& (loc.getBlock().equals(player.getLocation().getBlock()))) {
												List<String> list = ConnectionInject.getWarp(warpName);
												String world = list.get(1);
												String server = list.get(2);
												double x = Double.parseDouble(list.get(3));
												double y = Double.parseDouble(list.get(4));
												double z = Double.parseDouble(list.get(5));
												float yaw = Float.parseFloat(list.get(6));
												float pitch = Float.parseFloat(list.get(7));
												WPStreamOutApi.sendTeleportToWarpOut(player.getName(), server, world, x,
														y, z, yaw, pitch);

												return;
											} else {
												player.sendMessage(MessageDB.TELEPORT_MOVE_CANCEL);
											}
										}
									}, 20L * CookieApiBukkit.getWarmUpTime());
								} else {
									List<String> list = ConnectionInject.getWarp(warpName);
									String world = list.get(1);
									String server = list.get(2);

									double x = Double.parseDouble(list.get(3));
									double y = Double.parseDouble(list.get(4));
									double z = Double.parseDouble(list.get(5));
									float yaw = Float.parseFloat(list.get(6));
									float pitch = Float.parseFloat(list.get(7));

									WPStreamOutApi.sendTeleportToWarpOut(player.getName(), server, world, x, y, z, yaw,
											pitch);

									return;
								}
							} else {
								player.sendMessage(ChatColor.GOLD + "Dieser Warp existiert nicht!");
							}
						} else {
							sender.sendMessage(ChatColor.RED + "Du musst einen Warp angeben. Beispiel: /w lobby");
						}
					}
				}
			});
		} else {
			player.sendMessage(MessageDB.NO_PERMISSIONS);
		}
		return false;
	}
}
