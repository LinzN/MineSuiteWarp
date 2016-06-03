package de.nlinz.xeonSuite.warp.api;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.nlinz.javaSocket.client.api.XeonSocketClientManager;
import de.nlinz.xeonSuite.warp.Listener.XeonWarp;

public class WPStreamOutApi {

	public static void sendTeleportToWarpOut(String player, String server, String world, Double x, Double y, Double z,
			Float yaw, Float pitch) {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		DataOutputStream out = XeonSocketClientManager.createChannel(bytes, XeonWarp.channelName);
		try {
			out.writeUTF("TeleportToWarp");
			out.writeUTF(player);
			out.writeUTF(server);
			out.writeUTF(world);
			out.writeDouble(x);
			out.writeDouble(y);
			out.writeDouble(z);
			out.writeFloat(yaw);
			out.writeFloat(pitch);
		} catch (IOException e) {
			e.printStackTrace();
		}

		XeonSocketClientManager.sendData(bytes);

	}

}
