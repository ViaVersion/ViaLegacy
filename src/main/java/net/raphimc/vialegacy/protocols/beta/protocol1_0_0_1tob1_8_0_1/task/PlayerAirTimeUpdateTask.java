/*
 * This file is part of ViaLegacy - https://github.com/RaphiMC/ViaLegacy
 * Copyright (C) 2023 RK_01/RaphiMC and contributors
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
package net.raphimc.vialegacy.protocols.beta.protocol1_0_0_1tob1_8_0_1.task;

import com.google.common.collect.Lists;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.type.Type;
import net.raphimc.vialegacy.ViaLegacy;
import net.raphimc.vialegacy.api.data.BlockList1_6;
import net.raphimc.vialegacy.api.model.IdAndData;
import net.raphimc.vialegacy.protocols.beta.protocol1_0_0_1tob1_8_0_1.Protocol1_0_0_1tob1_8_0_1;
import net.raphimc.vialegacy.protocols.beta.protocol1_0_0_1tob1_8_0_1.storage.PlayerAirTimeStorage;
import net.raphimc.vialegacy.protocols.release.protocol1_1to1_0_0_1.ClientboundPackets1_0;
import net.raphimc.vialegacy.protocols.release.protocol1_4_2to1_3_1_2.types.MetaType1_3_1;
import net.raphimc.vialegacy.protocols.release.protocol1_4_2to1_3_1_2.types.Types1_3_1;
import net.raphimc.vialegacy.protocols.release.protocol1_7_2_5to1_6_4.storage.ChunkTracker;
import net.raphimc.vialegacy.protocols.release.protocol1_7_2_5to1_6_4.storage.PlayerInfoStorage;

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
                    try {
                        final IdAndData headBlock = info.get(ChunkTracker.class).getBlockNotNull(floor(playerInfoStorage.posX), floor(playerInfoStorage.posY + 1.62F), floor(playerInfoStorage.posZ));
                        if (headBlock.id == BlockList1_6.waterMoving.blockID || headBlock.id == BlockList1_6.waterStill.blockID) {
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

    private void sendAirTime(final PlayerInfoStorage playerInfoStorage, final PlayerAirTimeStorage playerAirTimeStorage, final UserConnection userConnection) throws Exception {
        final PacketWrapper updateAirTime = PacketWrapper.create(ClientboundPackets1_0.ENTITY_METADATA, userConnection);
        updateAirTime.write(Type.INT, playerInfoStorage.entityId); // entity id
        updateAirTime.write(Types1_3_1.METADATA_LIST, Lists.newArrayList(new Metadata(1, MetaType1_3_1.Short, Integer.valueOf(playerAirTimeStorage.air).shortValue()))); // metadata
        updateAirTime.send(Protocol1_0_0_1tob1_8_0_1.class);
    }

    private static int floor(double f) {
        int i = (int) f;
        return f < (double) i ? i - 1 : i;
    }

}
