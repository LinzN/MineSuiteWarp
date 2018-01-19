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

package de.linzn.mineSuite.warp;


import de.linzn.mineSuite.core.MineSuiteCorePlugin;
import de.linzn.mineSuite.warp.commands.DeleteWarpCommand;
import de.linzn.mineSuite.warp.commands.SetWarpCommand;
import de.linzn.mineSuite.warp.commands.WarpCommand;
import de.linzn.mineSuite.warp.commands.WarpListCommand;
import de.linzn.mineSuite.warp.listener.WarpListener;
import de.linzn.mineSuite.warp.socket.JClientWarpListener;
import org.bukkit.plugin.java.JavaPlugin;

public class WarpPlugin extends JavaPlugin {
    private static WarpPlugin inst;

    public static WarpPlugin inst() {
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
