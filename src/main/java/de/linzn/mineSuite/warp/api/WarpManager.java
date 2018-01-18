package de.linzn.mineSuite.warp.api;

import de.linzn.mineSuite.core.MineSuiteCorePlugin;
import de.linzn.mineSuite.core.database.hashDatabase.WarpDataTable;
import de.linzn.mineSuite.core.utils.LocationUtil;
import de.linzn.mineSuite.warp.WarpPlugin;
import de.linzn.mineSuite.warp.database.WarpSqlActions;
import de.linzn.mineSuite.warp.socket.JClientWarpOutput;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;


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

    public static boolean portalWarp(final Player player, final String warpName) {
        if (WarpSqlActions.isWarp(warpName)) {
            List<String> list = WarpSqlActions.getWarp(warpName);
            String world = list.get(1);
            String server = list.get(2);
            double x = Double.parseDouble(list.get(3));
            double y = Double.parseDouble(list.get(4));
            double z = Double.parseDouble(list.get(5));
            float yaw = Float.parseFloat(list.get(6));
            float pitch = Float.parseFloat(list.get(7));
            JClientWarpOutput.sendTeleportToWarpOut(player.getName(), server, world, x, y, z, yaw, pitch);
            return true;
        } else {
            player.sendMessage(net.md_5.bungee.api.ChatColor.RED + "Warp ist nicht verfügbar!");
        }


        return false;
    }

}
