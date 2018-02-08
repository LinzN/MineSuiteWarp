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
import de.linzn.mineSuite.core.database.hashDatabase.PendingTeleportsData;
import de.linzn.mineSuite.warp.WarpPlugin;
import de.linzn.mineSuite.warp.socket.JClientWarpOutput;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class WarpCommand implements CommandExecutor {
    private ThreadPoolExecutor executorServiceCommands = new ThreadPoolExecutor(1, 1, 250L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>());

    @Override
    public boolean onCommand(final CommandSender sender, Command cmd, String label, final String[] args) {
        Player player = (Player) sender;
        if (player.hasPermission("mineSuite.warp.warp")) {
            if (!PendingTeleportsData.playerCommand.contains(player.getUniqueId())) {
                PendingTeleportsData.addCommandSpam(player.getUniqueId());
                this.executorServiceCommands.submit(() -> {
                    if ((args.length >= 1)) {
                        final String warpName = args[0].toLowerCase();
                        WarpPlugin.inst().getServer().getScheduler().runTaskAsynchronously(WarpPlugin.inst(),
                                () -> JClientWarpOutput.sendTeleportToWarpOut(player.getUniqueId(), warpName));
                    } else {
                        sender.sendMessage(GeneralLanguage.warp_NO_WARP_ARGUMENT);
                    }
                });
            } else {
                player.sendMessage(GeneralLanguage.global_COMMAND_PENDING);
            }
        } else {
            player.sendMessage(GeneralLanguage.global_NO_PERMISSIONS);
        }
        return false;
    }
}
