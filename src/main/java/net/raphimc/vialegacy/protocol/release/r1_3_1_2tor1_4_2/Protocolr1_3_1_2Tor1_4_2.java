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

package net.raphimc.vialegacy.protocol.release.r1_3_1_2tor1_4_2;

import com.google.common.collect.Lists;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.ProtocolInfo;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.entities.EntityTypes1_8;
import com.viaversion.viaversion.api.minecraft.entitydata.EntityData;
import com.viaversion.viaversion.api.minecraft.item.DataItem;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.packet.State;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.api.type.Types;
import net.raphimc.vialegacy.ViaLegacy;
import net.raphimc.vialegacy.api.data.ItemList1_6;
import net.raphimc.vialegacy.api.protocol.StatelessProtocol;
import net.raphimc.vialegacy.api.splitter.PreNettySplitter;
import net.raphimc.vialegacy.api.util.PacketUtil;
import net.raphimc.vialegacy.protocol.release.r1_3_1_2tor1_4_2.packet.ClientboundPackets1_3_1;
import net.raphimc.vialegacy.protocol.release.r1_3_1_2tor1_4_2.packet.ServerboundPackets1_3_1;
import net.raphimc.vialegacy.protocol.release.r1_3_1_2tor1_4_2.rewriter.ItemRewriter;
import net.raphimc.vialegacy.protocol.release.r1_3_1_2tor1_4_2.rewriter.SoundRewriter;
import net.raphimc.vialegacy.protocol.release.r1_3_1_2tor1_4_2.types.Types1_3_1;
import net.raphimc.vialegacy.protocol.release.r1_4_2tor1_4_4_5.packet.ClientboundPackets1_4_2;
import net.raphimc.vialegacy.protocol.release.r1_4_2tor1_4_4_5.types.EntityDataTypes1_4_2;
import net.raphimc.vialegacy.protocol.release.r1_4_2tor1_4_4_5.types.Types1_4_2;
import net.raphimc.vialegacy.protocol.release.r1_5_2tor1_6_1.packet.ServerboundPackets1_5_2;
import net.raphimc.vialegacy.protocol.release.r1_5_2tor1_6_1.storage.EntityTracker;
import net.raphimc.vialegacy.protocol.release.r1_6_4tor1_7_2_5.types.Types1_6_4;
import net.raphimc.vialegacy.protocol.release.r1_7_6_10tor1_8.data.EntityDataIndex1_7_6;
import net.raphimc.vialegacy.protocol.release.r1_7_6_10tor1_8.types.Types1_7_6;

import java.util.List;
import java.util.logging.Level;

public class Protocolr1_3_1_2Tor1_4_2 extends StatelessProtocol<ClientboundPackets1_3_1, ClientboundPackets1_4_2, ServerboundPackets1_3_1, ServerboundPackets1_5_2> {

    private final ItemRewriter itemRewriter = new ItemRewriter(this);

    public Protocolr1_3_1_2Tor1_4_2() {
        super(ClientboundPackets1_3_1.class, ClientboundPackets1_4_2.class, ServerboundPackets1_3_1.class, ServerboundPackets1_5_2.class);
    }

    @Override
    protected void registerPackets() {
        super.registerPackets();

        this.registerClientbound(ClientboundPackets1_3_1.DISCONNECT, wrapper -> {
            final State currentState = wrapper.user().getProtocolInfo().getServerState();
            if (currentState == State.STATUS) {
                final String reason = wrapper.read(Types1_6_4.STRING); // reason
                try {
                    final ProtocolInfo info = wrapper.user().getProtocolInfo();
                    final String[] pingParts = reason.split("§");
                    final String out = "§1\0" + info.serverProtocolVersion().getVersion() + "\0" + info.serverProtocolVersion().getName() + "\0" + pingParts[0] + "\0" + pingParts[1] + "\0" + pingParts[2];
                    wrapper.write(Types1_6_4.STRING, out);
                } catch (Throwable e) {
                    ViaLegacy.getPlatform().getLogger().log(Level.WARNING, "Could not parse 1.3.1 ping: " + reason, e);
                    wrapper.cancel();
                }
            }
        });
        this.registerClientbound(ClientboundPackets1_3_1.LOGIN, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // entity id
                map(Types1_6_4.STRING); // level type
                map(Types.BYTE); // game mode
                map(Types.BYTE); // dimension id
                map(Types.BYTE); // difficulty
                map(Types.BYTE); // world height
                map(Types.BYTE); // max players
                handler(wrapper -> {
                    wrapper.send(Protocolr1_3_1_2Tor1_4_2.class);
                    wrapper.cancel();

                    final PacketWrapper setEntityData = PacketWrapper.create(ClientboundPackets1_4_2.SET_ENTITY_DATA, wrapper.user());
                    setEntityData.write(Types.INT, wrapper.get(Types.INT, 0)); // entity id
                    setEntityData.write(Types1_4_2.ENTITY_DATA_LIST, Lists.newArrayList(new EntityData(EntityDataIndex1_7_6.HUMAN_SKIN_FLAGS.getOldIndex(), EntityDataTypes1_4_2.BYTE, (byte) 0))); // entity data
                    setEntityData.send(Protocolr1_3_1_2Tor1_4_2.class);
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_3_1.SET_TIME, wrapper -> {
            final long time = wrapper.passthrough(Types.LONG); // time
            wrapper.write(Types.LONG, time % 24_000); // time of day
        });
        this.registerClientbound(ClientboundPackets1_3_1.RESPAWN, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // dimension id
                map(Types.BYTE); // difficulty
                map(Types.BYTE); // game mode
                map(Types.SHORT); // world height
                map(Types1_6_4.STRING); // worldType
                handler(wrapper -> {
                    final EntityTracker entityTracker = wrapper.user().get(EntityTracker.class);
                    final Integer[] entityIds = entityTracker.getTrackedEntities().keySet().stream().filter(i -> i != entityTracker.getPlayerID()).toArray(Integer[]::new);
                    final int[] primitiveInts = new int[entityIds.length];
                    for (int i = 0; i < entityIds.length; i++) primitiveInts[i] = entityIds[i];

                    final PacketWrapper destroyEntities = PacketWrapper.create(ClientboundPackets1_4_2.REMOVE_ENTITIES, wrapper.user());
                    destroyEntities.write(Types1_7_6.INT_ARRAY, primitiveInts);
                    destroyEntities.send(Protocolr1_3_1_2Tor1_4_2.class);
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_3_1.ADD_PLAYER, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // entity id
                map(Types1_6_4.STRING); // name
                map(Types.INT); // x
                map(Types.INT); // y
                map(Types.INT); // z
                map(Types.BYTE); // yaw
                map(Types.BYTE); // pitch
                map(Types.UNSIGNED_SHORT); // item
                map(Types1_3_1.ENTITY_DATA_LIST, Types1_4_2.ENTITY_DATA_LIST); // entity data
                handler(wrapper -> {
                    final List<EntityData> entityDataList = wrapper.get(Types1_4_2.ENTITY_DATA_LIST, 0);
                    rewriteEntityData(entityDataList);
                    entityDataList.removeIf(entityData -> entityData.dataType() == EntityDataTypes1_4_2.BYTE && entityData.id() == EntityDataIndex1_7_6.HUMAN_SKIN_FLAGS.getOldIndex());
                    entityDataList.add(new EntityData(EntityDataIndex1_7_6.HUMAN_SKIN_FLAGS.getOldIndex(), EntityDataTypes1_4_2.BYTE, (byte) 0));
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_3_1.SPAWN_ITEM, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // entity id
                map(Types1_3_1.NBTLESS_ITEM, Types1_7_6.ITEM);
                map(Types.INT); // x
                map(Types.INT); // y
                map(Types.INT); // z
                map(Types.BYTE); // velocity x
                map(Types.BYTE); // velocity y
                map(Types.BYTE); // velocity z
            }
        });
        this.registerClientbound(ClientboundPackets1_3_1.ADD_MOB, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // entity id
                map(Types.UNSIGNED_BYTE); // type id
                map(Types.INT); // x
                map(Types.INT); // y
                map(Types.INT); // z
                map(Types.BYTE); // yaw
                map(Types.BYTE); // pitch
                map(Types.BYTE); // head yaw
                map(Types.SHORT); // velocity x
                map(Types.SHORT); // velocity y
                map(Types.SHORT); // velocity z
                map(Types1_3_1.ENTITY_DATA_LIST, Types1_4_2.ENTITY_DATA_LIST); // entity data
                handler(wrapper -> {
                    rewriteEntityData(wrapper.get(Types1_4_2.ENTITY_DATA_LIST, 0));

                    final int entityId = wrapper.get(Types.INT, 0);
                    final short typeId = wrapper.get(Types.UNSIGNED_BYTE, 0);
                    if (typeId == EntityTypes1_8.EntityType.SKELETON.getId()) {
                        setMobHandItem(entityId, new DataItem(ItemList1_6.bow.itemId(), (byte) 1, (short) 0, null), wrapper);
                    } else if (typeId == EntityTypes1_8.EntityType.ZOMBIE_PIGMEN.getId()) {
                        setMobHandItem(entityId, new DataItem(ItemList1_6.swordGold.itemId(), (byte) 1, (short) 0, null), wrapper);
                    }
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_3_1.ADD_PAINTING, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // entity id
                map(Types1_6_4.STRING); // motive
                map(Types1_7_6.BLOCK_POSITION_INT); // position
                map(Types.INT); // rotation
                handler(wrapper -> {
                    int direction = wrapper.get(Types.INT, 1);
                    direction = switch (direction) {
                        case 0 -> 2;
                        case 2 -> 0;
                        default -> direction;
                    };
                    wrapper.set(Types.INT, 1, direction);
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_3_1.SET_ENTITY_DATA, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // entity id
                map(Types1_3_1.ENTITY_DATA_LIST, Types1_4_2.ENTITY_DATA_LIST); // entity data
                handler(wrapper -> rewriteEntityData(wrapper.get(Types1_4_2.ENTITY_DATA_LIST, 0)));
            }
        });
        this.registerClientbound(ClientboundPackets1_3_1.LEVEL_EVENT, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // effect id
                map(Types1_7_6.BLOCK_POSITION_UBYTE); // position
                map(Types.INT); // data
                create(Types.BOOLEAN, false); // server wide
            }
        });
        this.registerClientbound(ClientboundPackets1_3_1.CUSTOM_SOUND, new PacketHandlers() {
            @Override
            public void register() {
                handler(wrapper -> {
                    final String oldSound = wrapper.read(Types1_6_4.STRING); // sound
                    String newSound = SoundRewriter.map(oldSound);
                    if (oldSound.isEmpty()) newSound = "";
                    if (newSound == null) {
                        if (Via.getConfig().logOtherConversionWarnings()) {
                            ViaLegacy.getPlatform().getLogger().warning("Unable to map 1.3.2 sound '" + oldSound + "'");
                        }
                        newSound = "";
                    }
                    if (newSound.isEmpty()) {
                        wrapper.cancel();
                        return;
                    }
                    wrapper.write(Types1_6_4.STRING, newSound);
                });
                map(Types.INT); // x
                map(Types.INT); // y
                map(Types.INT); // z
                map(Types.FLOAT); // volume
                map(Types.UNSIGNED_BYTE); // pitch
            }
        });
        this.registerClientbound(ClientboundPackets1_3_1.MAP_ITEM_DATA, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.SHORT); // item id
                map(Types.SHORT); // map id
                map(Types1_4_2.UNSIGNED_BYTE_BYTE_ARRAY); // data
                handler(wrapper -> {
                    final byte[] data = wrapper.get(Types1_4_2.UNSIGNED_BYTE_BYTE_ARRAY, 0);
                    if (data[0] == 1) {
                        for (int i = 0; i < (data.length - 1) / 3; i++) {
                            final byte icon = (byte) (data[i * 3 + 1] % 16);
                            final byte centerX = data[i * 3 + 2];
                            final byte centerZ = data[i * 3 + 3];
                            final byte iconRotation = (byte) (data[i * 3 + 1] / 16);
                            data[i * 3 + 1] = (byte) (icon << 4 | iconRotation & 15);
                            data[i * 3 + 2] = centerX;
                            data[i * 3 + 3] = centerZ;
                        }
                    }
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_3_1.CUSTOM_PAYLOAD, new PacketHandlers() {
            @Override
            public void register() {
                handler(wrapper -> {
                    final String channel = wrapper.read(Types1_6_4.STRING); // channel
                    short length = wrapper.read(Types.SHORT); // length

                    try {
                        if (channel.equals("MC|TrList")) {
                            wrapper.passthrough(Types.INT); // window Id
                            final int count = wrapper.passthrough(Types.UNSIGNED_BYTE); // count
                            for (int i = 0; i < count; i++) {
                                wrapper.passthrough(Types1_7_6.ITEM); // item 1
                                wrapper.passthrough(Types1_7_6.ITEM); // item 3
                                if (wrapper.passthrough(Types.BOOLEAN)) { // has 3 items
                                    wrapper.passthrough(Types1_7_6.ITEM); // item 2
                                }
                                wrapper.write(Types.BOOLEAN, false); // unavailable
                            }
                            length = (short) PacketUtil.calculateLength(wrapper);
                        }
                    } catch (Exception e) {
                        if (Via.getConfig().logOtherConversionWarnings()) {
                            Via.getPlatform().getLogger().log(Level.WARNING, "Failed to handle packet", e);
                        }
                        wrapper.cancel();
                        return;
                    }

                    wrapper.resetReader();
                    wrapper.write(Types1_6_4.STRING, channel); // channel
                    wrapper.write(Types.SHORT, length); // length
                });
            }
        });

        this.registerServerbound(ServerboundPackets1_5_2.SERVER_PING, new PacketHandlers() {
            @Override
            public void register() {
                handler(PacketWrapper::clearPacket);
            }
        });
        this.registerServerbound(ServerboundPackets1_5_2.CLIENT_INFORMATION, new PacketHandlers() {
            @Override
            public void register() {
                map(Types1_6_4.STRING); // language
                map(Types.BYTE); // view distance
                map(Types.BYTE); // mask
                map(Types.BYTE); // difficulty
                read(Types.BOOLEAN); // show cape
            }
        });
    }

    private void rewriteEntityData(final List<EntityData> entityDataList) {
        for (EntityData entityData : entityDataList) {
            entityData.setDataType(EntityDataTypes1_4_2.byId(entityData.dataType().typeId()));
        }
    }

    private void setMobHandItem(final int entityId, final Item item, final PacketWrapper wrapper) {
        final PacketWrapper handItem = PacketWrapper.create(ClientboundPackets1_4_2.SET_EQUIPPED_ITEM, wrapper.user());
        handItem.write(Types.INT, entityId); // entity id
        handItem.write(Types.SHORT, (short) 0); // slot
        handItem.write(Types1_7_6.ITEM, item); // item

        wrapper.send(Protocolr1_3_1_2Tor1_4_2.class);
        handItem.send(Protocolr1_3_1_2Tor1_4_2.class);
        wrapper.cancel();
    }

    @Override
    public void init(UserConnection userConnection) {
        userConnection.put(new PreNettySplitter(Protocolr1_3_1_2Tor1_4_2.class, ClientboundPackets1_3_1::getPacket));
    }

    @Override
    public ItemRewriter getItemRewriter() {
        return this.itemRewriter;
    }

}
