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

import de.linzn.jSocket.core.IncomingDataListener;
import de.linzn.mineSuite.core.MineSuiteCorePlugin;
import de.linzn.mineSuite.warp.api.WarpManager;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;

public class JClientWarpListener implements IncomingDataListener {

    @Override
    public void onEvent(String channel, UUID clientUUID, byte[] dataInBytes) {
        // TODO Auto-generated method stub
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(dataInBytes));
        String subChannel;
        try {
            String serverName = in.readUTF();

            if (!serverName.equalsIgnoreCase(MineSuiteCorePlugin.getInstance().getMineConfigs().generalConfig.BUNGEE_SERVER_NAME)) {
                return;
            }
            subChannel = in.readUTF();
            if (subChannel.equals("server_warp_teleport-warp")) {
                WarpManager.teleportToWarp(in.readUTF(), in.readUTF(), in.readDouble(), in.readDouble(),
                        in.readDouble(), in.readFloat(), in.readFloat());
            }

        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

}
