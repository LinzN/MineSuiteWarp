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
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class WarpListCommand implements CommandExecutor {
    public ThreadPoolExecutor executorServiceCommands = new ThreadPoolExecutor(1, 1, 250L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>());

    public WarpListCommand(WarpPlugin instance) {
    }

    @Override
    public boolean onCommand(final CommandSender sender, Command cmd, String label, final String[] args) {
        final Player player = (Player) sender;
        if (player.hasPermission("mineSuite.warp.warps")) {
            this.executorServiceCommands.submit(() -> {
                int visible = 1;
                if (player.hasPermission("mineSuite.bypass")) {
                    visible = 0;
                }
                int pageNumb;
                try {
                    if (args.length == 1) {
                        int number = Integer.valueOf(args[0]);
                        if (number < 1) {
                            pageNumb = 0;
                        } else {
                            pageNumb = Integer.valueOf(args[0]) - 1;
                        }

                    } else {
                        pageNumb = 0;
                    }
                } catch (Exception e) {
                    player.sendMessage("No number");
                    return;
                }
                JClientWarpOutput.sendGetWarpsList(player.getUniqueId(), pageNumb, visible);

            });
        } else {
            player.sendMessage(GeneralLanguage.global_NO_PERMISSIONS);
        }
        return false;
    }
}
