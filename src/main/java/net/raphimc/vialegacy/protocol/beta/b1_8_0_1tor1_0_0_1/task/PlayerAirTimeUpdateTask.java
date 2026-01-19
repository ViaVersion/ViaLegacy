/*
 * This file is part of ViaLegacy - https://github.com/RaphiMC/ViaLegacy
 * Copyright (C) 2020-2026 RK_01/RaphiMC and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.raphimc.vialegacy.protocol.beta.b1_8_0_1tor1_0_0_1.task;

import com.google.common.collect.Lists;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.entitydata.EntityData;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.type.Types;
import com.viaversion.viaversion.util.IdAndData;
import net.raphimc.vialegacy.ViaLegacy;
import net.raphimc.vialegacy.api.data.BlockList1_6;
import net.raphimc.vialegacy.protocol.beta.b1_8_0_1tor1_0_0_1.Protocolb1_8_0_1tor1_0_0_1;
import net.raphimc.vialegacy.protocol.beta.b1_8_0_1tor1_0_0_1.storage.PlayerAirTimeStorage;
import net.raphimc.vialegacy.protocol.release.r1_0_0_1tor1_1.packet.ClientboundPackets1_0_0;
import net.raphimc.vialegacy.protocol.release.r1_3_1_2tor1_4_2.types.EntityDataTypes1_3_1;
import net.raphimc.vialegacy.protocol.release.r1_3_1_2tor1_4_2.types.Types1_3_1;
import net.raphimc.vialegacy.protocol.release.r1_6_4tor1_7_2_5.storage.ChunkTracker;
import net.raphimc.vialegacy.protocol.release.r1_6_4tor1_7_2_5.storage.PlayerInfoStorage;

import java.util.logging.Level;

public class PlayerAirTimeUpdateTask implements Runnable {

    @Override
    public void run() {
        for (UserConnection info : Via.getManager().getConnectionManager().getConnections()) {
            final PlayerAirTimeStorage playerAirTimeStorage = info.get(PlayerAirTimeStorage.class);
            if (playerAirTimeStorage != null) {
                final PlayerInfoStorage playerInfoStorage = info.get(PlayerInfoStorage.class);
                if (playerInfoStorage == null) continue;
                info.getChannel().eventLoop().submit(() -> {
                    if (!info.getChannel().isActive()) return;

                    try {
                        final IdAndData headBlock = info.get(ChunkTracker.class).getBlockNotNull(floor(playerInfoStorage.posX), floor(playerInfoStorage.posY + 1.62F), floor(playerInfoStorage.posZ));
                        if (headBlock.getId() == BlockList1_6.waterMoving.blockId() || headBlock.getId() == BlockList1_6.waterStill.blockId()) {
                            playerAirTimeStorage.sentPacket = false;
                            playerAirTimeStorage.air--;
                            if (playerAirTimeStorage.air < 0) playerAirTimeStorage.air = 0;
                            this.sendAirTime(playerInfoStorage, playerAirTimeStorage, info);
                        } else if (!playerAirTimeStorage.sentPacket) {
                            playerAirTimeStorage.sentPacket = true;
                            playerAirTimeStorage.air = playerAirTimeStorage.MAX_AIR;
                            this.sendAirTime(playerInfoStorage, playerAirTimeStorage, info);
                        }
                    } catch (Throwable e) {
                        ViaLegacy.getPlatform().getLogger().log(Level.WARNING, "Error updating air time", e);
                    }
                });
            }
        }
    }

    private void sendAirTime(final PlayerInfoStorage playerInfoStorage, final PlayerAirTimeStorage playerAirTimeStorage, final UserConnection userConnection) {
        final PacketWrapper updateAirTime = PacketWrapper.create(ClientboundPackets1_0_0.SET_ENTITY_DATA, userConnection);
        updateAirTime.write(Types.INT, playerInfoStorage.entityId); // entity id
        updateAirTime.write(Types1_3_1.ENTITY_DATA_LIST, Lists.newArrayList(new EntityData(1, EntityDataTypes1_3_1.SHORT, Integer.valueOf(playerAirTimeStorage.air).shortValue()))); // entity data
        updateAirTime.send(Protocolb1_8_0_1tor1_0_0_1.class);
    }

    private static int floor(double f) {
        int i = (int) f;
        return f < (double) i ? i - 1 : i;
    }

}
