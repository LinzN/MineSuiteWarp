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

package de.linzn.mineSuite.warp.api;

import de.linzn.mineSuite.core.MineSuiteCorePlugin;
import de.linzn.mineSuite.core.database.hashDatabase.WarpDataTable;
import de.linzn.mineSuite.core.utils.LocationUtil;
import de.linzn.mineSuite.warp.WarpPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;


public class WarpManager {

    public static void teleportToWarp(final String player, String world, double x, double y, double z, float yaw,
                                      float pitch) {
        World w = Bukkit.getWorld(world);
        Location t;

        if (w != null) {
            t = new Location(w, x, y, z, yaw, pitch);
        } else {
            w = Bukkit.getWorlds().get(0);
            t = w.getSpawnLocation();
        }
        Player p = Bukkit.getPlayer(player);
        if (p != null) {
            Bukkit.getScheduler().runTask(WarpPlugin.inst(), () -> {

                // Check if Block is safe
                if (LocationUtil.isBlockUnsafe(t.getWorld(), t.getBlockX(), t.getBlockY(), t.getBlockZ())) {
                    try {
                        Location l = LocationUtil.getSafeDestination(p, t);
                        if (l != null) {
                            p.teleport(l);
                            p.sendMessage(MineSuiteCorePlugin.getInstance().getMineConfigs().generalLanguage.Teleport_Warp);
                        } else {
                            p.sendMessage(ChatColor.RED + "Unable to find a safe location for teleport.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    p.teleport(t);
                    p.sendMessage(MineSuiteCorePlugin.getInstance().getMineConfigs().generalLanguage.Teleport_Warp);
                }
            });
        } else {
            WarpDataTable.pendingWarpLocations.put(player, t);
            Bukkit.getScheduler().runTaskLaterAsynchronously(WarpPlugin.inst(), () -> {
                if (WarpDataTable.pendingWarpLocations.containsKey(player)) {
                    WarpDataTable.pendingWarpLocations.remove(player);
                }

            }, 100L);
        }

    }

}
