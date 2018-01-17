package de.linzn.mineSuite.warp;


import de.linzn.mineSuite.core.MineSuiteCorePlugin;
import de.linzn.mineSuite.warp.Listener.WarpListener;
import de.linzn.mineSuite.warp.commands.DeleteWarpCommand;
import de.linzn.mineSuite.warp.commands.SetWarpCommand;
import de.linzn.mineSuite.warp.commands.WarpCommand;
import de.linzn.mineSuite.warp.commands.WarpListCommand;
import de.linzn.mineSuite.warp.socket.JClientWarpListener;
import org.bukkit.plugin.java.JavaPlugin;

public class Warpplugin extends JavaPlugin {
	private static Warpplugin inst;

	public static Warpplugin inst() {
		return inst;
	}

	@Override
	public void onEnable() {
		inst = this;
		loadCommands();
		MineSuiteCorePlugin.getInstance().getMineJSocketClient().jClientConnection1.registerIncomingDataListener("mineSuiteWarp", new JClientWarpListener());
		getServer().getPluginManager().registerEvents(new WarpListener(), this);
	}

	@Override
	public void onDisable() {
	}

	public void loadCommands() {
		getCommand("warp").setExecutor(new WarpCommand(this));
		getCommand("setwarp").setExecutor(new SetWarpCommand(this));
		getCommand("warps").setExecutor(new WarpListCommand(this));
		getCommand("delwarp").setExecutor(new DeleteWarpCommand(this));
	}

}
