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
package net.raphimc.vialegacy.protocols.beta.protocolb1_3_0_1tob1_2_0_2;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.Position;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import com.viaversion.viaversion.api.platform.providers.ViaProviders;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.api.type.Type;
import net.raphimc.vialegacy.api.model.IdAndData;
import net.raphimc.vialegacy.api.protocol.StatelessProtocol;
import net.raphimc.vialegacy.api.splitter.PreNettySplitter;
import net.raphimc.vialegacy.protocols.beta.protocolb1_3_0_1tob1_2_0_2.data.BlockHardnessList;
import net.raphimc.vialegacy.protocols.beta.protocolb1_3_0_1tob1_2_0_2.storage.BlockDigStorage;
import net.raphimc.vialegacy.protocols.beta.protocolb1_3_0_1tob1_2_0_2.task.BlockDigTickTask;
import net.raphimc.vialegacy.protocols.beta.protocolb1_3_0_1tob1_2_0_2.types.Typesb1_2;
import net.raphimc.vialegacy.protocols.beta.protocolb1_4_0_1tob1_3_0_1.ClientboundPacketsb1_3;
import net.raphimc.vialegacy.protocols.beta.protocolb1_5_0_2tob1_4_0_1.ServerboundPacketsb1_4;
import net.raphimc.vialegacy.protocols.beta.protocolb1_5_0_2tob1_4_0_1.types.MetaTypeb1_4;
import net.raphimc.vialegacy.protocols.beta.protocolb1_5_0_2tob1_4_0_1.types.Typesb1_4;
import net.raphimc.vialegacy.protocols.release.protocol1_7_2_5to1_6_4.storage.ChunkTracker;
import net.raphimc.vialegacy.protocols.release.protocol1_8to1_7_6_10.types.Types1_7_6;

import java.util.List;

public class Protocolb1_3_0_1tob1_2_0_2 extends StatelessProtocol<ClientboundPacketsb1_2, ClientboundPacketsb1_3, ServerboundPacketsb1_2, ServerboundPacketsb1_4> {

    public Protocolb1_3_0_1tob1_2_0_2() {
        super(ClientboundPacketsb1_2.class, ClientboundPacketsb1_3.class, ServerboundPacketsb1_2.class, ServerboundPacketsb1_4.class);
    }

    @Override
    protected void registerPackets() {
        this.registerClientbound(ClientboundPacketsb1_2.SPAWN_MOB, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.INT); // entity id
                map(Type.UNSIGNED_BYTE); // type id
                map(Type.INT); // x
                map(Type.INT); // y
                map(Type.INT); // z
                map(Type.BYTE); // yaw
                map(Type.BYTE); // pitch
                map(Typesb1_2.METADATA_LIST, Typesb1_4.METADATA_LIST); // metadata
                handler(wrapper -> rewriteMetadata(wrapper.get(Typesb1_4.METADATA_LIST, 0)));
            }
        });
        this.registerClientbound(ClientboundPacketsb1_2.ENTITY_METADATA, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.INT); // entity id
                map(Typesb1_2.METADATA_LIST, Typesb1_4.METADATA_LIST); // metadata
                handler(wrapper -> rewriteMetadata(wrapper.get(Typesb1_4.METADATA_LIST, 0)));
            }
        });

        this.registerServerbound(ServerboundPacketsb1_4.PLAYER_DIGGING, new PacketHandlers() {
            @Override
            public void register() {
                handler(wrapper -> {
                    wrapper.cancel();
                    final short status = wrapper.read(Type.UNSIGNED_BYTE); // status
                    final Position pos = wrapper.read(Types1_7_6.POSITION_UBYTE); // position
                    final short facing = wrapper.read(Type.UNSIGNED_BYTE); // direction

                    if (status != 4) {
                        wrapper.user().getStoredObjects().remove(BlockDigStorage.class);
                    }

                    switch (status) {
                        case 0:
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
                            break;
                        case 1:
                            sendBlockDigPacket(wrapper.user(), (byte) 2, pos, facing);
                            break;
                        case 2:
                            sendBlockDigPacket(wrapper.user(), (byte) 1, pos, facing);
                            sendBlockDigPacket(wrapper.user(), (byte) 3, pos, facing);
                            sendBlockDigPacket(wrapper.user(), (byte) 2, pos, facing);
                            break;
                        case 4:
                            sendBlockDigPacket(wrapper.user(), (byte) 4, pos, facing);
                            break;
                    }
                });
            }
        });
        this.registerServerbound(ServerboundPacketsb1_4.ENTITY_ACTION, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.INT); // entity id
                map(Type.BYTE); // action id
                handler(wrapper -> {
                    if (wrapper.get(Type.BYTE, 0) > 2) wrapper.cancel();
                });
            }
        });
        this.cancelServerbound(ServerboundPacketsb1_4.POSITION);
    }

    private void rewriteMetadata(final List<Metadata> metadataList) {
        for (Metadata metadata : metadataList) {
            metadata.setMetaType(MetaTypeb1_4.byId(metadata.metaType().typeId()));
        }
    }

    @Override
    public void register(ViaProviders providers) {
        Via.getPlatform().runRepeatingSync(new BlockDigTickTask(), 1L);
    }

    @Override
    public void init(UserConnection userConnection) {
        userConnection.put(new PreNettySplitter(userConnection, Protocolb1_3_0_1tob1_2_0_2.class, ClientboundPacketsb1_2::getPacket));
    }


    public static void sendBlockDigPacket(final UserConnection userConnection, final short status, final Position position, final short facing) throws Exception {
        final PacketWrapper blockDig = PacketWrapper.create(ServerboundPacketsb1_2.PLAYER_DIGGING, userConnection);
        blockDig.write(Type.UNSIGNED_BYTE, status); // status
        blockDig.write(Types1_7_6.POSITION_UBYTE, position); // position
        blockDig.write(Type.UNSIGNED_BYTE, facing); // direction
        blockDig.sendToServer(Protocolb1_3_0_1tob1_2_0_2.class);
    }

}
