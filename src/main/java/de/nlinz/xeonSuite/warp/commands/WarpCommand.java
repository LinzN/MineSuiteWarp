package de.nlinz.xeonSuite.warp.commands;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.nlinz.xeonSuite.bukkit.XeonSuiteBukkit;
import de.nlinz.xeonSuite.bukkit.utils.tables.WarpDataTable;
import de.nlinz.xeonSuite.bukkit.GlobalMessageDB;
import de.nlinz.xeonSuite.warp.Warpplugin;
import de.nlinz.xeonSuite.warp.api.WPStreamOutApi;
import de.nlinz.xeonSuite.warp.database.WarpSqlActions;
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

							if (WarpSqlActions.isWarp(warpName)) {
								if (!player.hasPermission("cookieApi.bypass")) {
									WarpDataTable.lastWarpLocation.put(player, player.getLocation());
									player.sendMessage(GlobalMessageDB.TELEPORT_TIMER.replace("{TIME}",
											String.valueOf(XeonSuiteBukkit.getWarmUpTime())));
									Warpplugin.inst().getServer().getScheduler().runTaskLater(Warpplugin.inst(),
											new Runnable() {
										@Override
										public void run() {

											Location loc = WarpDataTable.lastWarpLocation.get(player);
											WarpDataTable.lastWarpLocation.remove(player);
											if ((loc != null)
													&& (loc.getBlock().equals(player.getLocation().getBlock()))) {
												List<String> list = WarpSqlActions.getWarp(warpName);
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
												player.sendMessage(GlobalMessageDB.TELEPORT_MOVE_CANCEL);
											}
										}
									}, 20L * XeonSuiteBukkit.getWarmUpTime());
								} else {
									List<String> list = WarpSqlActions.getWarp(warpName);
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
			player.sendMessage(GlobalMessageDB.NO_PERMISSIONS);
		}
		return false;
	}
}
