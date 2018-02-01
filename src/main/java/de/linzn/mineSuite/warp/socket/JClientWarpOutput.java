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

package de.linzn.mineSuite.warp.socket;

import de.linzn.mineSuite.core.MineSuiteCorePlugin;
import org.bukkit.Location;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;


public class JClientWarpOutput {

    public static void sendTeleportToWarpOut(UUID playerUUID, String warpName) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

        try {
            dataOutputStream.writeUTF("client_warp_teleport-warp");
            dataOutputStream.writeUTF(warpName);
            dataOutputStream.writeUTF(playerUUID.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        MineSuiteCorePlugin.getInstance().getMineJSocketClient().jClientConnection1.writeOutput("mineSuiteWarp", byteArrayOutputStream.toByteArray());

    }

    public static void sendWarpCreate(UUID playerUUID, String warpName, Location location, int visible) {
        String server = MineSuiteCorePlugin.getInstance().getMineConfigs().generalConfig.BUNGEE_SERVER_NAME;
        String world = location.getWorld().getName();
        Double x = location.getX();
        Double y = location.getY();
        Double z = location.getZ();
        Float yaw = location.getYaw();
        Float pitch = location.getPitch();


        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

        try {
            dataOutputStream.writeUTF("client_warp_create-warp");
            dataOutputStream.writeUTF(warpName);
            dataOutputStream.writeUTF(playerUUID.toString());
            dataOutputStream.writeUTF(server);
            dataOutputStream.writeUTF(world);
            dataOutputStream.writeDouble(x);
            dataOutputStream.writeDouble(y);
            dataOutputStream.writeDouble(z);
            dataOutputStream.writeFloat(yaw);
            dataOutputStream.writeFloat(pitch);
            dataOutputStream.writeInt(visible);
        } catch (IOException e) {
            e.printStackTrace();
        }
        MineSuiteCorePlugin.getInstance().getMineJSocketClient().jClientConnection1.writeOutput("mineSuiteWarp", byteArrayOutputStream.toByteArray());

    }

    public static void sendWarpRemove(UUID playerUUID, String warpName) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

        try {
            dataOutputStream.writeUTF("client_warp_remove-warp");
            dataOutputStream.writeUTF(warpName);
            dataOutputStream.writeUTF(playerUUID.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        MineSuiteCorePlugin.getInstance().getMineJSocketClient().jClientConnection1.writeOutput("mineSuiteWarp", byteArrayOutputStream.toByteArray());

    }

    public static void sendGetWarpsList(UUID playerUUID, int pageid, int showAll) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

        try {
            dataOutputStream.writeUTF("client_warp_get-warps");
            dataOutputStream.writeUTF(playerUUID.toString());
            dataOutputStream.writeInt(pageid);
            dataOutputStream.writeInt(showAll);
        } catch (IOException e) {
            e.printStackTrace();
        }
        MineSuiteCorePlugin.getInstance().getMineJSocketClient().jClientConnection1.writeOutput("mineSuiteWarp", byteArrayOutputStream.toByteArray());

    }



}
