package de.kekshaus.cookieApi.warp.Listener;

import java.io.DataInputStream;
import java.io.IOException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import de.keks.socket.bukkit.events.plugin.BukkitSockWarpEvent;
import de.keks.socket.core.ByteStreamConverter;
import de.kekshaus.cookieApi.bukkit.CookieApiBukkit;
import de.kekshaus.cookieApi.warp.api.WPStreamInApi;

public class BukkitSockWarpListener implements Listener {

	@EventHandler
	public void onBukkitSockBanEvent(final BukkitSockWarpEvent e) {

		DataInputStream in = ByteStreamConverter.toDataInputStream(e.readBytes());
		String task = null;
		String servername = null;
		try {
			servername = in.readUTF();

			if (!servername.equalsIgnoreCase(CookieApiBukkit.getServerName())) {
				return;
			}

			task = in.readUTF();

			if (task.equals("TeleportToWarp")) {
				WPStreamInApi.teleportToWarp(in.readUTF(), in.readUTF(), in.readDouble(), in.readDouble(), in.readDouble(),
						in.readFloat(), in.readFloat());
			}

		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}
