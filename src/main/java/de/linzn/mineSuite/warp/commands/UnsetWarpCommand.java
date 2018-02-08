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
import de.linzn.mineSuite.warp.socket.JClientWarpOutput;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class UnsetWarpCommand implements CommandExecutor {
    private ThreadPoolExecutor executorServiceCommands = new ThreadPoolExecutor(1, 1, 250L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>());

    @Override
    public boolean onCommand(final CommandSender sender, Command cmd, String label, final String[] args) {
        final Player player = (Player) sender;
        if (player.hasPermission("mineSuite.warp.unsetwarp")) {
            this.executorServiceCommands.submit(() -> {
                if (args.length >= 1) {
                    String warpName = args[0].toLowerCase();
                    JClientWarpOutput.sendWarpRemove(player.getUniqueId(), warpName);
                } else {
                    sender.sendMessage(GeneralLanguage.warp_UNSETWARP_USAGE);
                }
            });
        } else {
            player.sendMessage(GeneralLanguage.global_NO_PERMISSIONS);

        }
        return false;
    }
}
