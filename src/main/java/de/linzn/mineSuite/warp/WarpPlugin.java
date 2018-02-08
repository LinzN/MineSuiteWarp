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


import de.linzn.mineSuite.warp.commands.SetWarpCommand;
import de.linzn.mineSuite.warp.commands.UnsetWarpCommand;
import de.linzn.mineSuite.warp.commands.WarpCommand;
import de.linzn.mineSuite.warp.commands.WarpListCommand;
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
    }

    @Override
    public void onDisable() {
    }

    private void loadCommands() {
        getCommand("warp").setExecutor(new WarpCommand());
        getCommand("setwarp").setExecutor(new SetWarpCommand());
        getCommand("warps").setExecutor(new WarpListCommand());
        getCommand("unsetwarp").setExecutor(new UnsetWarpCommand());
    }

}
