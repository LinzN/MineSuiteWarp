package de.nlinz.xeonSuite.warp;

import org.bstats.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

import de.nlinz.javaSocket.client.api.XeonSocketClientManager;
import de.nlinz.xeonSuite.warp.Listener.WarpListener;
import de.nlinz.xeonSuite.warp.Listener.XeonWarp;
import de.nlinz.xeonSuite.warp.commands.DeleteWarpCommand;
import de.nlinz.xeonSuite.warp.commands.SetWarpCommand;
import de.nlinz.xeonSuite.warp.commands.WarpCommand;
import de.nlinz.xeonSuite.warp.commands.WarpListCommand;

public class Warpplugin extends JavaPlugin {
	private static Warpplugin inst;

	@Override
	public void onEnable() {
		inst = this;
		loadCommands();
		XeonSocketClientManager.registerDataListener(new XeonWarp());
		getServer().getPluginManager().registerEvents(new WarpListener(), this);
		new Metrics(this);
	}

	@Override
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
