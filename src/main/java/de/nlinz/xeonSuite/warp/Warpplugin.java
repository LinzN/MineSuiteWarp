package de.nlinz.xeonSuite.warp;

import org.bukkit.plugin.java.JavaPlugin;

import de.nlinz.xeonSuite.warp.Listener.BukkitSockWarpListener;
import de.nlinz.xeonSuite.warp.Listener.WarpListener;
import de.nlinz.xeonSuite.warp.commands.DeleteWarpCommand;
import de.nlinz.xeonSuite.warp.commands.SetWarpCommand;
import de.nlinz.xeonSuite.warp.commands.WarpCommand;
import de.nlinz.xeonSuite.warp.commands.WarpListCommand;
import de.nlinz.xeonSuite.warp.database.MineWarpDB;

public class Warpplugin extends JavaPlugin {
	private static Warpplugin inst;

	public void onEnable() {
		inst = this;

		if (MineWarpDB.create()) {
			// someting
		}
		loadCommands();
		getServer().getPluginManager().registerEvents(new BukkitSockWarpListener(), this);
		getServer().getPluginManager().registerEvents(new WarpListener(), this);
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
