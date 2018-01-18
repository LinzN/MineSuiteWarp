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
import de.linzn.mineSuite.warp.WarpPlugin;
import de.linzn.mineSuite.warp.database.WarpSqlActions;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SetWarpCommand implements CommandExecutor {
	public ThreadPoolExecutor executorServiceCommands = new ThreadPoolExecutor(1, 1, 250L, TimeUnit.MILLISECONDS,
			new LinkedBlockingQueue<Runnable>());

	public SetWarpCommand(WarpPlugin instance) {
	}

	@Override
	public boolean onCommand(final CommandSender sender, Command cmnd, String label, final String[] args) {
		final Player player = (Player) sender;
		if (player.hasPermission("mineSuite.warp.setwarp")) {
			this.executorServiceCommands.submit(() -> {
				if (sender instanceof Player) {
					Player player1 = (Player) sender;

					Location coords = player1.getLocation();
					String server = MineSuiteCorePlugin.getInstance().getMineConfigs().generalConfig.BUNGEE_SERVER_NAME;
					String world = coords.getWorld().getName();
					Double x = coords.getX();
					Double y = coords.getY();
					Double z = coords.getZ();
					Float yaw = coords.getYaw();
					Float pitch = coords.getPitch();

					if (args.length >= 1) {
						String warpName = args[0].toLowerCase();

						if (WarpSqlActions.isWarp(warpName)) {
							sender.sendMessage("Dieser Warp ist bereits registriert.");
							return;
						}
						int visible = 1;
						if (args.length >= 2) {
							if (args[1].equalsIgnoreCase("false")) {
								visible = 0;
							}
						}

						WarpSqlActions.setWarp(player1.getUniqueId(), warpName, server, world, x, y, z, yaw, pitch,
								visible);
						sender.sendMessage(ChatColor.GREEN + "Du hast den Warp " + ChatColor.YELLOW + warpName
								+ ChatColor.GREEN + " registriert!");

						return;
					} else {
						sender.sendMessage("Du musst einen Warpnamen angeben!");
					}
				}
			});
		} else {
			player.sendMessage(MineSuiteCorePlugin.getInstance().getMineConfigs().generalLanguage.NO_PERMISSIONS);
		}
		return false;
	}
}
