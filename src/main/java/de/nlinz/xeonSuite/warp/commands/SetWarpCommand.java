package de.nlinz.xeonSuite.warp.commands;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.nlinz.xeonSuite.bukkit.XeonSuiteBukkit;
import de.nlinz.xeonSuite.bukkit.GlobalMessageDB;
import de.nlinz.xeonSuite.warp.Warpplugin;
import de.nlinz.xeonSuite.warp.database.ConnectionInject;

public class SetWarpCommand implements CommandExecutor {
	public ThreadPoolExecutor executorServiceCommands = new ThreadPoolExecutor(1, 1, 250L, TimeUnit.MILLISECONDS,
			new LinkedBlockingQueue<Runnable>());

	public SetWarpCommand(Warpplugin instance) {
	}

	public boolean onCommand(final CommandSender sender, Command cmnd, String label, final String[] args) {
		final Player player = (Player) sender;
		if (player.hasPermission("cookieApi.warp.setwarp")) {
			this.executorServiceCommands.submit(new Runnable() {
				public void run() {
					if (sender instanceof Player) {
						Player player = (Player) sender;

						Location coords = player.getLocation();
						String server = XeonSuiteBukkit.getServerName();
						String world = coords.getWorld().getName();
						Double x = coords.getX();
						Double y = coords.getY();
						Double z = coords.getZ();
						Float yaw = coords.getYaw();
						Float pitch = coords.getPitch();

						if (args.length >= 1) {
							String warpName = args[0].toLowerCase();

							if (ConnectionInject.isWarp(warpName)) {
								sender.sendMessage("Dieser Warp ist bereits registriert.");
								return;
							}
							int visible = 1;
							if (args.length >= 2) {
								if (args[1].equalsIgnoreCase("false")) {
									visible = 0;
								}
							}

							ConnectionInject.setWarp(player.getUniqueId(), warpName, server, world, x, y, z, yaw, pitch,
									visible);
							sender.sendMessage(ChatColor.GREEN + "Du hast den Warp " + ChatColor.YELLOW + warpName
									+ ChatColor.GREEN + " registriert!");

							return;
						} else {
							sender.sendMessage("Du musst einen Warpnamen angeben!");
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
