package de.nlinz.xeonSuite.warp.Listener;

import java.io.DataInputStream;
import java.io.IOException;

import de.nlinz.javaSocket.client.api.XeonSocketClientManager;
import de.nlinz.javaSocket.client.events.SocketDataEvent;
import de.nlinz.javaSocket.client.interfaces.IDataListener;
import de.nlinz.xeonSuite.bukkit.XeonSuiteBukkit;
import de.nlinz.xeonSuite.warp.api.WPStreamInApi;

public class XeonWarp implements IDataListener {

	@Override
	public String getChannel() {
		// TODO Auto-generated method stub
		return channelName;
	}

	public static String channelName = "xeonWarp";

	@Override
	public void onDataRecieve(SocketDataEvent event) {
		// TODO Auto-generated method stub
		DataInputStream in = XeonSocketClientManager.readDataInput(event.getStreamBytes());
		String task = null;
		String servername = null;
		try {
			servername = in.readUTF();

			if (!servername.equalsIgnoreCase(XeonSuiteBukkit.getServerName())) {
				return;
			}

			task = in.readUTF();

			if (task.equals("TeleportToWarp")) {
				WPStreamInApi.teleportToWarp(in.readUTF(), in.readUTF(), in.readDouble(), in.readDouble(),
						in.readDouble(), in.readFloat(), in.readFloat());
			}

		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}
