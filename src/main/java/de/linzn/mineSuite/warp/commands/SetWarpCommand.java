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

import de.linzn.mineSuite.core.configurations.YamlFiles.GeneralLanguage;
import de.linzn.mineSuite.warp.WarpPlugin;
import de.linzn.mineSuite.warp.socket.JClientWarpOutput;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SetWarpCommand implements CommandExecutor {
    private ThreadPoolExecutor executorServiceCommands = new ThreadPoolExecutor(1, 1, 250L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>());

    public SetWarpCommand(WarpPlugin instance) {
    }

    @Override
    public boolean onCommand(final CommandSender sender, Command cmnd, String label, final String[] args) {
        final Player player = (Player) sender;
        if (player.hasPermission("mineSuite.warp.setwarp")) {
            this.executorServiceCommands.submit(() -> {
                Location coords = player.getLocation();
                if (args.length >= 1) {
                    String warpName = args[0].toLowerCase();
                    int visible = 1;
                    if (args.length >= 2) {
                        if (args[1].equalsIgnoreCase("false")) {
                            visible = 0;
                        }
                    }
                    JClientWarpOutput.sendWarpCreate(player.getUniqueId(), warpName, coords, visible);
                } else {
                    sender.sendMessage(GeneralLanguage.warp_NO_WARP_ARGUMENT);
                }
            });
        } else {
            player.sendMessage(GeneralLanguage.global_NO_PERMISSIONS);
        }
        return false;
    }
}
