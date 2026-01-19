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
package net.raphimc.vialegacy.protocol.beta.b1_2_0_2tob1_3_0_1;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.BlockPosition;
import com.viaversion.viaversion.api.minecraft.entitydata.EntityData;
import com.viaversion.viaversion.api.platform.providers.ViaProviders;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.api.type.Types;
import com.viaversion.viaversion.util.IdAndData;
import net.raphimc.vialegacy.api.protocol.StatelessProtocol;
import net.raphimc.vialegacy.api.splitter.PreNettySplitter;
import net.raphimc.vialegacy.protocol.beta.b1_2_0_2tob1_3_0_1.data.BlockHardnessList;
import net.raphimc.vialegacy.protocol.beta.b1_2_0_2tob1_3_0_1.packet.ClientboundPacketsb1_2;
import net.raphimc.vialegacy.protocol.beta.b1_2_0_2tob1_3_0_1.packet.ServerboundPacketsb1_2;
import net.raphimc.vialegacy.protocol.beta.b1_2_0_2tob1_3_0_1.storage.BlockDigStorage;
import net.raphimc.vialegacy.protocol.beta.b1_2_0_2tob1_3_0_1.task.BlockDigTickTask;
import net.raphimc.vialegacy.protocol.beta.b1_2_0_2tob1_3_0_1.types.Typesb1_2;
import net.raphimc.vialegacy.protocol.beta.b1_3_0_1tob1_4_0_1.packet.ClientboundPacketsb1_3;
import net.raphimc.vialegacy.protocol.beta.b1_4_0_1tob1_5_0_2.packet.ServerboundPacketsb1_4;
import net.raphimc.vialegacy.protocol.beta.b1_4_0_1tob1_5_0_2.types.EntityDataTypesb1_4;
import net.raphimc.vialegacy.protocol.beta.b1_4_0_1tob1_5_0_2.types.Typesb1_4;
import net.raphimc.vialegacy.protocol.release.r1_6_4tor1_7_2_5.storage.ChunkTracker;
import net.raphimc.vialegacy.protocol.release.r1_7_6_10tor1_8.types.Types1_7_6;

import java.util.List;

public class Protocolb1_2_0_2Tob1_3_0_1 extends StatelessProtocol<ClientboundPacketsb1_2, ClientboundPacketsb1_3, ServerboundPacketsb1_2, ServerboundPacketsb1_4> {

    public Protocolb1_2_0_2Tob1_3_0_1() {
        super(ClientboundPacketsb1_2.class, ClientboundPacketsb1_3.class, ServerboundPacketsb1_2.class, ServerboundPacketsb1_4.class);
    }

    @Override
    protected void registerPackets() {
        this.registerClientbound(ClientboundPacketsb1_2.ADD_MOB, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // entity id
                map(Types.UNSIGNED_BYTE); // type id
                map(Types.INT); // x
                map(Types.INT); // y
                map(Types.INT); // z
                map(Types.BYTE); // yaw
                map(Types.BYTE); // pitch
                map(Typesb1_2.ENTITY_DATA_LIST, Typesb1_4.ENTITY_DATA_LIST); // entity data
                handler(wrapper -> rewriteEntityData(wrapper.get(Typesb1_4.ENTITY_DATA_LIST, 0)));
            }
        });
        this.registerClientbound(ClientboundPacketsb1_2.SET_ENTITY_DATA, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // entity id
                map(Typesb1_2.ENTITY_DATA_LIST, Typesb1_4.ENTITY_DATA_LIST); // entity data
                handler(wrapper -> rewriteEntityData(wrapper.get(Typesb1_4.ENTITY_DATA_LIST, 0)));
            }
        });

        this.registerServerbound(ServerboundPacketsb1_4.PLAYER_ACTION, wrapper -> {
            wrapper.cancel();
            final short status = wrapper.read(Types.UNSIGNED_BYTE); // status
            final BlockPosition pos = wrapper.read(Types1_7_6.BLOCK_POSITION_UBYTE); // position
            final short facing = wrapper.read(Types.UNSIGNED_BYTE); // direction

            if (status != 4) {
                wrapper.user().getStoredObjects().remove(BlockDigStorage.class);
            }

            switch (status) {
                case 0 -> {
                    final IdAndData blockBeingBroken = wrapper.user().get(ChunkTracker.class).getBlockNotNull(pos);
                    if (BlockHardnessList.canBeBrokenInstantly(blockBeingBroken)) {
                        sendBlockDigPacket(wrapper.user(), (byte) 0, pos, facing);
                        sendBlockDigPacket(wrapper.user(), (byte) 3, pos, facing);
                        sendBlockDigPacket(wrapper.user(), (byte) 1, pos, facing);
                        sendBlockDigPacket(wrapper.user(), (byte) 2, pos, facing);
                        return;
                    }
                    wrapper.user().put(new BlockDigStorage(wrapper.user(), pos, facing));
                    sendBlockDigPacket(wrapper.user(), (byte) 0, pos, facing);
                    sendBlockDigPacket(wrapper.user(), (byte) 1, pos, facing);
                }
                case 1 -> sendBlockDigPacket(wrapper.user(), (byte) 2, pos, facing);
                case 2 -> {
                    sendBlockDigPacket(wrapper.user(), (byte) 1, pos, facing);
                    sendBlockDigPacket(wrapper.user(), (byte) 3, pos, facing);
                    sendBlockDigPacket(wrapper.user(), (byte) 2, pos, facing);
                }
                case 4 -> sendBlockDigPacket(wrapper.user(), (byte) 4, pos, facing);
            }
        });
        this.registerServerbound(ServerboundPacketsb1_4.PLAYER_COMMAND, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // entity id
                map(Types.BYTE); // action id
                handler(wrapper -> {
                    if (wrapper.get(Types.BYTE, 0) > 2) wrapper.cancel();
                });
            }
        });
        this.cancelServerbound(ServerboundPacketsb1_4.POSITION);
    }

    private void rewriteEntityData(final List<EntityData> entityDataList) {
        for (EntityData entityData : entityDataList) {
            entityData.setDataType(EntityDataTypesb1_4.byId(entityData.dataType().typeId()));
        }
    }

    @Override
    public void register(ViaProviders providers) {
        Via.getPlatform().runRepeatingSync(new BlockDigTickTask(), 1L);
    }

    @Override
    public void init(UserConnection userConnection) {
        userConnection.put(new PreNettySplitter(Protocolb1_2_0_2Tob1_3_0_1.class, ClientboundPacketsb1_2::getPacket));
    }


    public static void sendBlockDigPacket(final UserConnection userConnection, final short status, final BlockPosition position, final short facing) {
        final PacketWrapper blockDig = PacketWrapper.create(ServerboundPacketsb1_2.PLAYER_ACTION, userConnection);
        blockDig.write(Types.UNSIGNED_BYTE, status); // status
        blockDig.write(Types1_7_6.BLOCK_POSITION_UBYTE, position); // position
        blockDig.write(Types.UNSIGNED_BYTE, facing); // direction
        blockDig.sendToServer(Protocolb1_2_0_2Tob1_3_0_1.class);
    }

}
