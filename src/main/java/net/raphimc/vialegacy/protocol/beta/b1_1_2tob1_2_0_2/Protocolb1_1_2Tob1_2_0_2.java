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
package net.raphimc.vialegacy.protocol.beta.b1_1_2tob1_2_0_2;

import com.google.common.collect.Lists;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.entitydata.EntityData;
import com.viaversion.viaversion.api.minecraft.item.DataItem;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.api.type.Types;
import com.viaversion.viaversion.util.IdAndData;
import net.raphimc.vialegacy.api.protocol.StatelessProtocol;
import net.raphimc.vialegacy.api.splitter.PreNettySplitter;
import net.raphimc.vialegacy.protocol.beta.b1_1_2tob1_2_0_2.packet.ClientboundPacketsb1_1;
import net.raphimc.vialegacy.protocol.beta.b1_1_2tob1_2_0_2.packet.ServerboundPacketsb1_1;
import net.raphimc.vialegacy.protocol.beta.b1_1_2tob1_2_0_2.rewriter.BlockDataRewriter;
import net.raphimc.vialegacy.protocol.beta.b1_1_2tob1_2_0_2.storage.EntityFlagStorage;
import net.raphimc.vialegacy.protocol.beta.b1_1_2tob1_2_0_2.types.Typesb1_1;
import net.raphimc.vialegacy.protocol.beta.b1_2_0_2tob1_3_0_1.packet.ClientboundPacketsb1_2;
import net.raphimc.vialegacy.protocol.beta.b1_2_0_2tob1_3_0_1.packet.ServerboundPacketsb1_2;
import net.raphimc.vialegacy.protocol.beta.b1_2_0_2tob1_3_0_1.types.EntityDataTypesb1_2;
import net.raphimc.vialegacy.protocol.beta.b1_2_0_2tob1_3_0_1.types.Typesb1_2;
import net.raphimc.vialegacy.protocol.release.r1_1tor1_2_1_3.types.Types1_1;
import net.raphimc.vialegacy.protocol.release.r1_3_1_2tor1_4_2.types.Types1_3_1;
import net.raphimc.vialegacy.protocol.release.r1_4_2tor1_4_4_5.types.Types1_4_2;
import net.raphimc.vialegacy.protocol.release.r1_7_6_10tor1_8.types.Types1_7_6;

public class Protocolb1_1_2Tob1_2_0_2 extends StatelessProtocol<ClientboundPacketsb1_1, ClientboundPacketsb1_2, ServerboundPacketsb1_1, ServerboundPacketsb1_2> {

    private final BlockDataRewriter BLOCK_DATA_REWRITER = new BlockDataRewriter();

    public Protocolb1_1_2Tob1_2_0_2() {
        super(ClientboundPacketsb1_1.class, ClientboundPacketsb1_2.class, ServerboundPacketsb1_1.class, ServerboundPacketsb1_2.class);
    }

    @Override
    protected void registerPackets() {
        this.registerClientbound(ClientboundPacketsb1_1.SET_EQUIPPED_ITEM, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // entity id
                map(Types.SHORT); // slot
                map(Types.SHORT); // item id
                create(Types.SHORT, (short) 0); // item damage
            }
        });
        this.registerClientbound(ClientboundPacketsb1_1.ANIMATE, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // entity id
                map(Types.BYTE); // animation id
                handler(wrapper -> {
                    final int entityId = wrapper.get(Types.INT, 0);
                    final byte animationId = wrapper.get(Types.BYTE, 0);
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
                        final PacketWrapper setEntityData = PacketWrapper.create(ClientboundPacketsb1_2.SET_ENTITY_DATA, wrapper.user());
                        setEntityData.write(Types.INT, wrapper.get(Types.INT, 0)); // entity id
                        setEntityData.write(Typesb1_2.ENTITY_DATA_LIST, Lists.newArrayList(new EntityData(0, EntityDataTypesb1_2.BYTE, (byte) entityFlagStorage.getFlagMask(entityId)))); // entity data
                        setEntityData.send(Protocolb1_1_2Tob1_2_0_2.class);
                    }
                });
            }
        });
        this.registerClientbound(ClientboundPacketsb1_1.SPAWN_ITEM, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // entity id
                handler(wrapper -> {
                    final short itemId = wrapper.read(Types.SHORT); // item id
                    final byte itemCount = wrapper.read(Types.BYTE); // item count
                    wrapper.write(Types1_3_1.NBTLESS_ITEM, new DataItem(itemId, itemCount, (short) 0, null)); // item
                });
                map(Types.INT); // x
                map(Types.INT); // y
                map(Types.INT); // z
                map(Types.BYTE); // velocity x
                map(Types.BYTE); // velocity y
                map(Types.BYTE); // velocity z
            }
        });
        this.registerClientbound(ClientboundPacketsb1_1.ADD_MOB, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // entity id
                map(Types.UNSIGNED_BYTE); // type id
                map(Types.INT); // x
                map(Types.INT); // y
                map(Types.INT); // z
                map(Types.BYTE); // yaw
                map(Types.BYTE); // pitch
                handler(wrapper -> wrapper.write(Typesb1_2.ENTITY_DATA_LIST, Lists.newArrayList(new EntityData(0, EntityDataTypesb1_2.BYTE, (byte) 0)))); // entity data
            }
        });
        this.registerClientbound(ClientboundPacketsb1_1.LEVEL_CHUNK, new PacketHandlers() {
            @Override
            public void register() {
                handler(wrapper -> BLOCK_DATA_REWRITER.remapChunk(wrapper.passthrough(Types1_1.CHUNK))); // chunk
            }
        });
        this.registerClientbound(ClientboundPacketsb1_1.CHUNK_BLOCKS_UPDATE, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // chunkX
                map(Types.INT); // chunkZ
                map(Types1_1.BLOCK_CHANGE_RECORD_ARRAY); // blockChangeRecords
                handler(wrapper -> BLOCK_DATA_REWRITER.remapBlockChangeRecords(wrapper.get(Types1_1.BLOCK_CHANGE_RECORD_ARRAY, 0)));
            }
        });
        this.registerClientbound(ClientboundPacketsb1_1.BLOCK_UPDATE, new PacketHandlers() {
            @Override
            public void register() {
                map(Types1_7_6.POSITION_UBYTE); // position
                map(Types.UNSIGNED_BYTE); // block id
                map(Types.UNSIGNED_BYTE); // block data
                handler(wrapper -> {
                    final IdAndData block = new IdAndData(wrapper.get(Types.UNSIGNED_BYTE, 0), wrapper.get(Types.UNSIGNED_BYTE, 1));
                    BLOCK_DATA_REWRITER.remapBlock(block);
                    wrapper.set(Types.UNSIGNED_BYTE, 0, (short) block.getId());
                    wrapper.set(Types.UNSIGNED_BYTE, 1, (short) block.getData());
                });
            }
        });
        this.registerClientbound(ClientboundPacketsb1_1.CONTAINER_SET_SLOT, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.BYTE); // window id
                map(Types.SHORT); // slot
                map(Typesb1_1.NBTLESS_ITEM, Types1_4_2.NBTLESS_ITEM); // item
            }
        });

        this.registerServerbound(ServerboundPacketsb1_2.USE_ITEM_ON, new PacketHandlers() {
            @Override
            public void register() {
                map(Types1_7_6.POSITION_UBYTE); // position
                map(Types.UNSIGNED_BYTE); // direction
                map(Types1_4_2.NBTLESS_ITEM, Typesb1_1.NBTLESS_ITEM); // item
            }
        });
        this.registerServerbound(ServerboundPacketsb1_2.PLAYER_COMMAND, ServerboundPacketsb1_1.SWING, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // entity id
                map(Types.BYTE, Types.UNSIGNED_BYTE, i -> (short) (i + 103)); // action id | start/stop sneaking (1/2) -> 104/105
            }
        });
        this.registerServerbound(ServerboundPacketsb1_2.CONTAINER_CLICK, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.BYTE); // window id
                map(Types.SHORT); // slot
                map(Types.BYTE); // button
                map(Types.SHORT); // action
                map(Types1_4_2.NBTLESS_ITEM, Typesb1_1.NBTLESS_ITEM); // item
            }
        });
    }

    @Override
    public void init(UserConnection userConnection) {
        userConnection.put(new PreNettySplitter(Protocolb1_1_2Tob1_2_0_2.class, ClientboundPacketsb1_1::getPacket));

        userConnection.put(new EntityFlagStorage());
    }

}
