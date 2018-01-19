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

package de.linzn.mineSuite.warp.listener;

import de.linzn.mineSuite.core.MineSuiteCorePlugin;
import de.linzn.mineSuite.core.database.hashDatabase.WarpDataTable;
import de.linzn.mineSuite.warp.WarpPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;


public class WarpListener implements Listener {

    @EventHandler
    public void playerConnect(PlayerSpawnLocationEvent e) {
        if (WarpDataTable.pendingWarp.containsKey(e.getPlayer().getName())) {
            Player t = WarpDataTable.pendingWarp.get(e.getPlayer().getName());
            WarpDataTable.pendingWarp.remove(e.getPlayer().getName());
            if ((t == null) || (!t.isOnline())) {
                e.getPlayer().sendMessage("Player is no longer online");
                return;
            }
            WarpDataTable.ignoreWarp.add(e.getPlayer());
            e.setSpawnLocation(t.getLocation());
            sendWarpMSG(e.getPlayer());
        } else if (WarpDataTable.pendingWarpLocations.containsKey(e.getPlayer().getName())) {
            Location l = WarpDataTable.pendingWarpLocations.get(e.getPlayer().getName());
            WarpDataTable.ignoreWarp.add(e.getPlayer());
            e.setSpawnLocation(l);
            sendWarpMSG(e.getPlayer());
        }
    }

    public void sendWarpMSG(final Player p) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(WarpPlugin.inst(), () -> {
            WarpDataTable.ignoreWarp.remove(p);
            p.sendMessage(MineSuiteCorePlugin.getInstance().getMineConfigs().generalLanguage.Teleport_Warp);
        }, 20);

    }
}
