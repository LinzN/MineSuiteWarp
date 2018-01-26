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

import de.linzn.mineSuite.core.MineSuiteCorePlugin;
import de.linzn.mineSuite.core.database.hashDatabase.PendingTeleportsData;
import de.linzn.mineSuite.warp.WarpPlugin;
import de.linzn.mineSuite.warp.socket.JClientWarpOutput;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class WarpCommand implements CommandExecutor {
    public ThreadPoolExecutor executorServiceCommands = new ThreadPoolExecutor(1, 1, 250L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>());

    public WarpCommand(WarpPlugin instance) {

    }

    @Override
    public boolean onCommand(final CommandSender sender, Command cmd, String label, final String[] args) {
        Player player = (Player) sender;
        if (player.hasPermission("mineSuite.warp.warp")) {
            this.executorServiceCommands.submit(() -> {
                if (sender instanceof Player) {
                    if ((args.length >= 1)) {
                        final String warpName = args[0].toLowerCase();

                        if (!player.hasPermission("mineSuite.bypass")) {
                            PendingTeleportsData.checkMoveLocation.put(player.getUniqueId(), player.getLocation());
                            player.sendMessage(MineSuiteCorePlugin.getInstance().getMineConfigs().generalLanguage.TELEPORT_TIMER.replace("{TIME}",
                                    String.valueOf(MineSuiteCorePlugin.getInstance().getMineConfigs().generalConfig.TELEPORT_WARMUP)));
                            WarpPlugin.inst().getServer().getScheduler().runTaskLater(WarpPlugin.inst(),
                                    () -> {

                                        Location loc = PendingTeleportsData.checkMoveLocation.get(player.getUniqueId());
                                        PendingTeleportsData.checkMoveLocation.remove(player.getUniqueId());
                                        if ((loc != null) && (loc.getBlock()
                                                .equals(player.getLocation().getBlock()))) {
                                            JClientWarpOutput.sendTeleportToWarpOut(player.getUniqueId(), warpName);
                                            return;
                                        } else {
                                            player.sendMessage(MineSuiteCorePlugin.getInstance().getMineConfigs().generalLanguage.TELEPORT_MOVE_CANCEL);
                                        }
                                    }, 20L * MineSuiteCorePlugin.getInstance().getMineConfigs().generalConfig.TELEPORT_WARMUP);
                        } else {

                            JClientWarpOutput.sendTeleportToWarpOut(player.getUniqueId(), warpName);

                            return;
                        }

                    } else {
                        sender.sendMessage(ChatColor.RED + "Du musst einen Warp angeben. Beispiel: /w lobby");
                    }
                }
            });
        } else {
            player.sendMessage(MineSuiteCorePlugin.getInstance().getMineConfigs().generalLanguage.NO_PERMISSIONS);
        }
        return false;
    }
}
