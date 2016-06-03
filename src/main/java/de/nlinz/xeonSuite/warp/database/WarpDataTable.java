package de.nlinz.xeonSuite.warp.database;

import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class WarpDataTable {
	public static HashMap<String, Player> pendingWarp = new HashMap<String, Player>();
	public static HashMap<String, Location> pendingWarpLocations = new HashMap<String, Location>();
	public static HashSet<Player> ignoreWarp = new HashSet<Player>();
	public static HashMap<Player, Location> lastWarpLocation = new HashMap<Player, Location>();

	public static void removeWarpPlayer(Player player) {
		pendingWarp.remove(player.getName());
		pendingWarpLocations.remove(player.getName());
		ignoreWarp.remove(player);
		lastWarpLocation.remove(player);
	}

}