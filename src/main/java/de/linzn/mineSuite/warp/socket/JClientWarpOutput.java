package de.linzn.mineSuite.warp.socket;

import de.linzn.mineSuite.core.MineSuiteCorePlugin;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;


public class JClientWarpOutput {

    public static void sendTeleportToWarpOut(String player, String server, String world, Double x, Double y, Double z,
                                             Float yaw, Float pitch) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

        try {
            dataOutputStream.writeUTF("client_warp_teleport-warp");
            dataOutputStream.writeUTF(player);
            dataOutputStream.writeUTF(server);
            dataOutputStream.writeUTF(world);
            dataOutputStream.writeDouble(x);
            dataOutputStream.writeDouble(y);
            dataOutputStream.writeDouble(z);
            dataOutputStream.writeFloat(yaw);
            dataOutputStream.writeFloat(pitch);
        } catch (IOException e) {
            e.printStackTrace();
        }
        MineSuiteCorePlugin.getInstance().getMineJSocketClient().jClientConnection1.writeOutput("mineSuiteWarp", byteArrayOutputStream.toByteArray());

    }

}