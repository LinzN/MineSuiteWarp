package de.kekshaus.cookieApi.warp;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import de.kekshaus.cookieApi.bukkit.CookieApiBukkit;
import de.kekshaus.cookieApi.warp.commands.DeleteWarpCommand;
import de.kekshaus.cookieApi.warp.commands.SetWarpCommand;
import de.kekshaus.cookieApi.warp.commands.WarpCommand;
import de.kekshaus.cookieApi.warp.commands.WarpListCommand;
import de.kekshaus.cookieApi.warp.database.MineWarpDB;

public class Warpplugin extends JavaPlugin {
	private static Warpplugin inst;

	public void onEnable() {
		inst = this;

		if (MineWarpDB.create()) {
			// someting
		}
		loadCommands();
	}

	public void onDisable() {
	}

	public static Warpplugin inst() {
		return inst;
	}

	public void loadCommands() {
		getCommand("warp").setExecutor(new WarpCommand(this));
		getCommand("setwarp").setExecutor(new SetWarpCommand(this));
		getCommand("warps").setExecutor(new WarpListCommand(this));
		getCommand("delwarp").setExecutor(new DeleteWarpCommand(this));
	}

}
