package de.linzn.mineSuite.warp.commands;

import de.linzn.mineSuite.core.MineSuiteCorePlugin;
import de.linzn.mineSuite.warp.WarpPlugin;
import de.linzn.mineSuite.warp.database.WarpSqlActions;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DeleteWarpCommand implements CommandExecutor {
	public ThreadPoolExecutor executorServiceCommands = new ThreadPoolExecutor(1, 1, 250L, TimeUnit.MILLISECONDS,
			new LinkedBlockingQueue<>());

	public DeleteWarpCommand(WarpPlugin instance) {
	}

	@Override
	public boolean onCommand(final CommandSender sender, Command cmd, String label, final String[] args) {
		final Player player = (Player) sender;
		if (player.hasPermission("mineSuite.warp.delwarp")) {
			this.executorServiceCommands.submit(() -> {
				if (sender instanceof Player) {
					if (args.length >= 1) {
						String warpName = args[0].toLowerCase();

						WarpSqlActions.delWarp(warpName);
						sender.sendMessage(ChatColor.GREEN + "Der Warp " + ChatColor.YELLOW + warpName
								+ ChatColor.GREEN + " wurde entfernt!");
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
