/*
 * This file is part of ViaLegacy - https://github.com/RaphiMC/ViaLegacy
 * Copyright (C) 2020-2024 RK_01/RaphiMC and contributors
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
package net.raphimc.vialegacy.protocols.beta.protocolb1_2_0_2tob1_1_2;

import com.google.common.collect.Lists;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.item.DataItem;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.util.IdAndData;
import net.raphimc.vialegacy.api.protocol.StatelessProtocol;
import net.raphimc.vialegacy.api.splitter.PreNettySplitter;
import net.raphimc.vialegacy.protocols.beta.protocolb1_2_0_2tob1_1_2.rewriter.BlockDataRewriter;
import net.raphimc.vialegacy.protocols.beta.protocolb1_2_0_2tob1_1_2.storage.EntityFlagStorage;
import net.raphimc.vialegacy.protocols.beta.protocolb1_2_0_2tob1_1_2.types.Typesb1_1;
import net.raphimc.vialegacy.protocols.beta.protocolb1_3_0_1tob1_2_0_2.ClientboundPacketsb1_2;
import net.raphimc.vialegacy.protocols.beta.protocolb1_3_0_1tob1_2_0_2.ServerboundPacketsb1_2;
import net.raphimc.vialegacy.protocols.beta.protocolb1_3_0_1tob1_2_0_2.types.MetaTypeb1_2;
import net.raphimc.vialegacy.protocols.beta.protocolb1_3_0_1tob1_2_0_2.types.Typesb1_2;
import net.raphimc.vialegacy.protocols.release.protocol1_2_1_3to1_1.types.Types1_1;
import net.raphimc.vialegacy.protocols.release.protocol1_4_2to1_3_1_2.types.Types1_3_1;
import net.raphimc.vialegacy.protocols.release.protocol1_4_4_5to1_4_2.types.Types1_4_2;
import net.raphimc.vialegacy.protocols.release.protocol1_8to1_7_6_10.types.Types1_7_6;

public class Protocolb1_2_0_2tob1_1_2 extends StatelessProtocol<ClientboundPacketsb1_1, ClientboundPacketsb1_2, ServerboundPacketsb1_1, ServerboundPacketsb1_2> {

    private final BlockDataRewriter BLOCK_DATA_REWRITER = new BlockDataRewriter();

    public Protocolb1_2_0_2tob1_1_2() {
        super(ClientboundPacketsb1_1.class, ClientboundPacketsb1_2.class, ServerboundPacketsb1_1.class, ServerboundPacketsb1_2.class);
    }

    @Override
    protected void registerPackets() {
        this.registerClientbound(ClientboundPacketsb1_1.ENTITY_EQUIPMENT, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.INT); // entity id
                map(Type.SHORT); // slot
                map(Type.SHORT); // item id
                create(Type.SHORT, (short) 0); // item damage
            }
        });
        this.registerClientbound(ClientboundPacketsb1_1.ENTITY_ANIMATION, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.INT); // entity id
                map(Type.BYTE); // animation id
                handler(wrapper -> {
                    final int entityId = wrapper.get(Type.INT, 0);
                    final byte animationId = wrapper.get(Type.BYTE, 0);
                    if (animationId <= 2) return; // 1 - Swing | 2 - Hurt

                    wrapper.cancel();
                    final EntityFlagStorage entityFlagStorage = wrapper.user().get(EntityFlagStorage.class);
                    final int oldMask = entityFlagStorage.getFlagMask(entityId);
                    switch (animationId) {
                        case 100: // start riding
                            entityFlagStorage.setFlag(entityId, 2, true);
                            break;
                        case 101: // stop riding
                            entityFlagStorage.setFlag(entityId, 2, false);
                            break;
                        case 102: // start burning
                            entityFlagStorage.setFlag(entityId, 0, true);
                            break;
                        case 103: // stop burning
                            entityFlagStorage.setFlag(entityId, 0, false);
                            break;
                        case 104: // start sneaking
                            entityFlagStorage.setFlag(entityId, 1, true);
                            break;
                        case 105: // stop sneaking
                            entityFlagStorage.setFlag(entityId, 1, false);
                            break;
                    }

                    if (oldMask != entityFlagStorage.getFlagMask(entityId)) {
                        final PacketWrapper metadata = PacketWrapper.create(ClientboundPacketsb1_2.ENTITY_METADATA, wrapper.user());
                        metadata.write(Type.INT, wrapper.get(Type.INT, 0)); // entity id
                        metadata.write(Typesb1_2.METADATA_LIST, Lists.newArrayList(new Metadata(0, MetaTypeb1_2.Byte, (byte) entityFlagStorage.getFlagMask(entityId)))); // metadata
                        metadata.send(Protocolb1_2_0_2tob1_1_2.class);
                    }
                });
            }
        });
        this.registerClientbound(ClientboundPacketsb1_1.SPAWN_ITEM, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.INT); // entity id
                handler(wrapper -> {
                    final short itemId = wrapper.read(Type.SHORT); // item id
                    final byte itemCount = wrapper.read(Type.BYTE); // item count
                    wrapper.write(Types1_3_1.NBTLESS_ITEM, new DataItem(itemId, itemCount, (short) 0, null)); // item
                });
                map(Type.INT); // x
                map(Type.INT); // y
                map(Type.INT); // z
                map(Type.BYTE); // velocity x
                map(Type.BYTE); // velocity y
                map(Type.BYTE); // velocity z
            }
        });
        this.registerClientbound(ClientboundPacketsb1_1.SPAWN_MOB, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.INT); // entity id
                map(Type.UNSIGNED_BYTE); // type id
                map(Type.INT); // x
                map(Type.INT); // y
                map(Type.INT); // z
                map(Type.BYTE); // yaw
                map(Type.BYTE); // pitch
                handler(wrapper -> wrapper.write(Typesb1_2.METADATA_LIST, Lists.newArrayList(new Metadata(0, MetaTypeb1_2.Byte, (byte) 0)))); // metadata
            }
        });
        this.registerClientbound(ClientboundPacketsb1_1.CHUNK_DATA, new PacketHandlers() {
            @Override
            public void register() {
                handler(wrapper -> BLOCK_DATA_REWRITER.remapChunk(wrapper.passthrough(Types1_1.CHUNK))); // chunk
            }
        });
        this.registerClientbound(ClientboundPacketsb1_1.MULTI_BLOCK_CHANGE, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.INT); // chunkX
                map(Type.INT); // chunkZ
                map(Types1_1.BLOCK_CHANGE_RECORD_ARRAY); // blockChangeRecords
                handler(wrapper -> BLOCK_DATA_REWRITER.remapBlockChangeRecords(wrapper.get(Types1_1.BLOCK_CHANGE_RECORD_ARRAY, 0)));
            }
        });
        this.registerClientbound(ClientboundPacketsb1_1.BLOCK_CHANGE, new PacketHandlers() {
            @Override
            public void register() {
                map(Types1_7_6.POSITION_UBYTE); // position
                map(Type.UNSIGNED_BYTE); // block id
                map(Type.UNSIGNED_BYTE); // block data
                handler(wrapper -> {
                    final IdAndData block = new IdAndData(wrapper.get(Type.UNSIGNED_BYTE, 0), wrapper.get(Type.UNSIGNED_BYTE, 1));
                    BLOCK_DATA_REWRITER.remapBlock(block);
                    wrapper.set(Type.UNSIGNED_BYTE, 0, (short) block.getId());
                    wrapper.set(Type.UNSIGNED_BYTE, 1, (short) block.getData());
                });
            }
        });
        this.registerClientbound(ClientboundPacketsb1_1.SET_SLOT, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.BYTE); // window id
                map(Type.SHORT); // slot
                map(Typesb1_1.NBTLESS_ITEM, Types1_4_2.NBTLESS_ITEM); // item
            }
        });

        this.registerServerbound(ServerboundPacketsb1_2.PLAYER_BLOCK_PLACEMENT, new PacketHandlers() {
            @Override
            public void register() {
                map(Types1_7_6.POSITION_UBYTE); // position
                map(Type.UNSIGNED_BYTE); // direction
                map(Types1_4_2.NBTLESS_ITEM, Typesb1_1.NBTLESS_ITEM); // item
            }
        });
        this.registerServerbound(ServerboundPacketsb1_2.ENTITY_ACTION, ServerboundPacketsb1_1.ANIMATION, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.INT); // entity id
                map(Type.BYTE, Type.UNSIGNED_BYTE, i -> (short) (i + 103)); // action id | start/stop sneaking (1/2) -> 104/105
            }
        });
        this.registerServerbound(ServerboundPacketsb1_2.CLICK_WINDOW, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.BYTE); // window id
                map(Type.SHORT); // slot
                map(Type.BYTE); // button
                map(Type.SHORT); // action
                map(Types1_4_2.NBTLESS_ITEM, Typesb1_1.NBTLESS_ITEM); // item
            }
        });
    }

    @Override
    public void init(UserConnection userConnection) {
        userConnection.put(new PreNettySplitter(Protocolb1_2_0_2tob1_1_2.class, ClientboundPacketsb1_1::getPacket));

        userConnection.put(new EntityFlagStorage());
    }

}
