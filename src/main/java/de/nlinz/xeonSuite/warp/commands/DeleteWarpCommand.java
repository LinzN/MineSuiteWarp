package de.nlinz.xeonSuite.warp.commands;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.nlinz.xeonSuite.bukkit.utils.languages.GlobalLanguage;
import de.nlinz.xeonSuite.warp.Warpplugin;
import de.nlinz.xeonSuite.warp.database.WarpSqlActions;

public class DeleteWarpCommand implements CommandExecutor {
	public ThreadPoolExecutor executorServiceCommands = new ThreadPoolExecutor(1, 1, 250L, TimeUnit.MILLISECONDS,
			new LinkedBlockingQueue<Runnable>());

	public DeleteWarpCommand(Warpplugin instance) {
	}

	@Override
	public boolean onCommand(final CommandSender sender, Command cmd, String label, final String[] args) {
		final Player player = (Player) sender;
		if (player.hasPermission("cookieApi.warp.delwarp")) {
			this.executorServiceCommands.submit(new Runnable() {
				@Override
				public void run() {
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
				}
			});
		} else {
			player.sendMessage(GlobalLanguage.NO_PERMISSIONS);

		}
		return false;
	}
}
