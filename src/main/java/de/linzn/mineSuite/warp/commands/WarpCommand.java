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

package de.linzn.mineSuite.warp.commands;

import de.linzn.mineSuite.core.MineSuiteCorePlugin;
import de.linzn.mineSuite.core.database.hashDatabase.WarpDataTable;
import de.linzn.mineSuite.warp.WarpPlugin;
import de.linzn.mineSuite.warp.database.WarpSqlActions;
import de.linzn.mineSuite.warp.socket.JClientWarpOutput;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class WarpCommand implements CommandExecutor {
	public ThreadPoolExecutor executorServiceCommands = new ThreadPoolExecutor(1, 1, 250L, TimeUnit.MILLISECONDS,
			new LinkedBlockingQueue<Runnable>());

	public WarpCommand(WarpPlugin instance) {

	}

	@Override
	public boolean onCommand(final CommandSender sender, Command cmd, String label, final String[] args) {
		final Player player = (Player) sender;
		if (player.hasPermission("mineSuite.warp.warp")) {
			this.executorServiceCommands.submit(() -> {
				if (sender instanceof Player) {
					final Player player1 = (Player) sender;
					if ((args.length >= 1)) {
						final String warpName = args[0].toLowerCase();

						if (WarpSqlActions.isWarp(warpName)) {
							if (!player1.hasPermission("xeonSuite.bypass")) {
								WarpDataTable.lastWarpLocation.put(player1, player1.getLocation());
								player1.sendMessage(MineSuiteCorePlugin.getInstance().getMineConfigs().generalLanguage.TELEPORT_TIMER.replace("{TIME}",
										String.valueOf(MineSuiteCorePlugin.getInstance().getMineConfigs().generalConfig.TELEPORT_WARMUP)));
								WarpPlugin.inst().getServer().getScheduler().runTaskLater(WarpPlugin.inst(),
										new Runnable() {
											@Override
											public void run() {

												Location loc = WarpDataTable.lastWarpLocation.get(player1);
												WarpDataTable.lastWarpLocation.remove(player1);
												if ((loc != null) && (loc.getBlock()
														.equals(player1.getLocation().getBlock()))) {
													List<String> list = WarpSqlActions.getWarp(warpName);
													String world = list.get(1);
													String server = list.get(2);
													double x = Double.parseDouble(list.get(3));
													double y = Double.parseDouble(list.get(4));
													double z = Double.parseDouble(list.get(5));
													float yaw = Float.parseFloat(list.get(6));
													float pitch = Float.parseFloat(list.get(7));
													JClientWarpOutput.sendTeleportToWarpOut(player1.getName(), server,
															world, x, y, z, yaw, pitch);

													return;
												} else {
													player1.sendMessage(MineSuiteCorePlugin.getInstance().getMineConfigs().generalLanguage.TELEPORT_MOVE_CANCEL);
												}
											}
										}, 20L * MineSuiteCorePlugin.getInstance().getMineConfigs().generalConfig.TELEPORT_WARMUP);
							} else {
								List<String> list = WarpSqlActions.getWarp(warpName);
								String world = list.get(1);
								String server = list.get(2);

								double x = Double.parseDouble(list.get(3));
								double y = Double.parseDouble(list.get(4));
								double z = Double.parseDouble(list.get(5));
								float yaw = Float.parseFloat(list.get(6));
								float pitch = Float.parseFloat(list.get(7));

								JClientWarpOutput.sendTeleportToWarpOut(player1.getName(), server, world, x, y, z, yaw,
										pitch);

								return;
							}
						} else {
							player1.sendMessage(ChatColor.GOLD + "Dieser Warp existiert nicht!");
						}
					} else {
						sender.sendMessage(ChatColor.RED + "Du musst einen Warp angeben. Beispiel: /w lobby");
					}
				}
			});
		} else {
			player.sendMessage(MineSuiteCorePlugin.getInstance().getMineConfigs().generalLanguage.NO_PERMISSIONS);
		}
		return false;
	}
}
