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

package net.raphimc.vialegacy.protocol.release.r1_5_2tor1_6_1;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.entities.EntityTypes1_8;
import com.viaversion.viaversion.api.minecraft.entitydata.EntityData;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.api.type.Types;
import com.viaversion.viaversion.libs.mcstructs.text.components.StringComponent;
import com.viaversion.viaversion.libs.mcstructs.text.serializer.TextComponentSerializer;
import net.raphimc.vialegacy.ViaLegacy;
import net.raphimc.vialegacy.api.protocol.StatelessProtocol;
import net.raphimc.vialegacy.api.splitter.PreNettySplitter;
import net.raphimc.vialegacy.api.util.PacketUtil;
import net.raphimc.vialegacy.protocol.release.r1_5_2tor1_6_1.packet.ClientboundPackets1_5_2;
import net.raphimc.vialegacy.protocol.release.r1_5_2tor1_6_1.packet.ServerboundPackets1_5_2;
import net.raphimc.vialegacy.protocol.release.r1_5_2tor1_6_1.rewriter.EntityDataRewriter;
import net.raphimc.vialegacy.protocol.release.r1_5_2tor1_6_1.rewriter.ItemRewriter;
import net.raphimc.vialegacy.protocol.release.r1_5_2tor1_6_1.rewriter.SoundRewriter;
import net.raphimc.vialegacy.protocol.release.r1_5_2tor1_6_1.storage.AttachTracker;
import net.raphimc.vialegacy.protocol.release.r1_5_2tor1_6_1.storage.EntityTracker;
import net.raphimc.vialegacy.protocol.release.r1_6_1tor1_6_2.packet.ClientboundPackets1_6_1;
import net.raphimc.vialegacy.protocol.release.r1_6_4tor1_7_2_5.packet.ServerboundPackets1_6_4;
import net.raphimc.vialegacy.protocol.release.r1_6_4tor1_7_2_5.types.Types1_6_4;
import net.raphimc.vialegacy.protocol.release.r1_7_6_10tor1_8.data.EntityDataIndex1_7_6;
import net.raphimc.vialegacy.protocol.release.r1_7_6_10tor1_8.types.Types1_7_6;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.logging.Level;

public class Protocolr1_5_2Tor1_6_1 extends StatelessProtocol<ClientboundPackets1_5_2, ClientboundPackets1_6_1, ServerboundPackets1_5_2, ServerboundPackets1_6_4> {

    private final ItemRewriter itemRewriter = new ItemRewriter(this);

    public Protocolr1_5_2Tor1_6_1() {
        super(ClientboundPackets1_5_2.class, ClientboundPackets1_6_1.class, ServerboundPackets1_5_2.class, ServerboundPackets1_6_4.class);
    }

    @Override
    protected void registerPackets() {
        super.registerPackets();

        this.registerClientbound(ClientboundPackets1_5_2.LOGIN, new PacketHandlers() {
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
                    final int entityId = wrapper.get(Types.INT, 0);
                    final EntityTracker tracker = wrapper.user().get(EntityTracker.class);
                    tracker.getTrackedEntities().put(entityId, EntityTypes1_8.EntityType.PLAYER);
                    tracker.setPlayerID(entityId);
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_5_2.CHAT, new PacketHandlers() {
            @Override
            public void register() {
                map(Types1_6_4.STRING, Types1_6_4.STRING, msg -> TextComponentSerializer.V1_6.serialize(new StringComponent(msg))); // message
            }
        });
        this.registerClientbound(ClientboundPackets1_5_2.SET_HEALTH, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.SHORT, Types.FLOAT); // health
                map(Types.SHORT); // food
                map(Types.FLOAT); // saturation
            }
        });
        this.registerClientbound(ClientboundPackets1_5_2.ADD_PLAYER, new PacketHandlers() {
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
                map(Types1_6_4.ENTITY_DATA_LIST); // entity data
                handler(wrapper -> EntityDataRewriter.transform(EntityTypes1_8.EntityType.PLAYER, wrapper.get(Types1_6_4.ENTITY_DATA_LIST, 0)));
                handler(wrapper -> {
                    final int entityId = wrapper.get(Types.INT, 0);
                    wrapper.user().get(EntityTracker.class).getTrackedEntities().put(entityId, EntityTypes1_8.EntityType.PLAYER);
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_5_2.TAKE_ITEM_ENTITY, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // collected entity id
                map(Types.INT); // collector entity id
                handler(wrapper -> wrapper.user().get(EntityTracker.class).getTrackedEntities().remove(wrapper.get(Types.INT, 0)));
            }
        });
        this.registerClientbound(ClientboundPackets1_5_2.ADD_ENTITY, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // entity id
                map(Types.BYTE); // type id
                map(Types.INT); // x
                map(Types.INT); // y
                map(Types.INT); // z
                map(Types.BYTE); // pitch
                map(Types.BYTE); // yaw
                map(Types.INT); // data
                // more conditional data
                handler(wrapper -> {
                    final int entityID = wrapper.get(Types.INT, 0);
                    final int typeID = wrapper.get(Types.BYTE, 0);
                    final int data = wrapper.get(Types.INT, 4);
                    final EntityTypes1_8.EntityType entityType = EntityTypes1_8.ObjectType.getEntityType(typeID, data);
                    if (entityType != null) {
                        wrapper.user().get(EntityTracker.class).getTrackedEntities().put(entityID, entityType);
                    }
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_5_2.ADD_MOB, new PacketHandlers() {
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
                map(Types1_6_4.ENTITY_DATA_LIST); // entity data
                handler(wrapper -> {
                    final int entityID = wrapper.get(Types.INT, 0);
                    final int typeID = wrapper.get(Types.UNSIGNED_BYTE, 0);
                    final EntityTypes1_8.EntityType entityType = EntityTypes1_8.EntityType.findById(typeID);
                    if (entityType == null) {
                        wrapper.cancel();
                        return;
                    }
                    final List<EntityData> entityDataList = wrapper.get(Types1_6_4.ENTITY_DATA_LIST, 0);
                    wrapper.user().get(EntityTracker.class).getTrackedEntities().put(entityID, entityType);
                    EntityDataRewriter.transform(entityType, entityDataList);

                    if (entityType.isOrHasParent(EntityTypes1_8.EntityType.WOLF)) {
                        handleWolfEntityData(entityID, entityDataList, wrapper);
                    }
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_5_2.ADD_PAINTING, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // entity id
                map(Types1_6_4.STRING); // motive
                map(Types1_7_6.BLOCK_POSITION_INT); // position
                map(Types.INT); // rotation
                handler(wrapper -> {
                    final int entityID = wrapper.get(Types.INT, 0);
                    wrapper.user().get(EntityTracker.class).getTrackedEntities().put(entityID, EntityTypes1_8.EntityType.PAINTING);
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_5_2.ADD_EXPERIENCE_ORB, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // entity id
                map(Types.INT); // x
                map(Types.INT); // y
                map(Types.INT); // z
                map(Types.SHORT); // count
                handler(wrapper -> {
                    final int entityID = wrapper.get(Types.INT, 0);
                    wrapper.user().get(EntityTracker.class).getTrackedEntities().put(entityID, EntityTypes1_8.EntityType.EXPERIENCE_ORB);
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_5_2.REMOVE_ENTITIES, new PacketHandlers() {
            @Override
            public void register() {
                map(Types1_7_6.INT_ARRAY); // entity ids
                handler(wrapper -> {
                    final EntityTracker tracker = wrapper.user().get(EntityTracker.class);
                    for (int entityId : wrapper.get(Types1_7_6.INT_ARRAY, 0)) {
                        tracker.getTrackedEntities().remove(entityId);
                    }
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_5_2.SET_ENTITY_LINK, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // riding entity id
                map(Types.INT); // vehicle entity id
                handler(wrapper -> {
                    final AttachTracker attachTracker = wrapper.user().get(AttachTracker.class);
                    final EntityTracker entityTracker = wrapper.user().get(EntityTracker.class);
                    final int ridingId = wrapper.get(Types.INT, 0);
                    final int vehicleId = wrapper.get(Types.INT, 1);
                    if (entityTracker.getPlayerID() == ridingId) {
                        attachTracker.vehicleEntityId = vehicleId;
                    }
                });
                create(Types.UNSIGNED_BYTE, (short) 0); // leash state
            }
        });
        this.registerClientbound(ClientboundPackets1_5_2.SET_ENTITY_DATA, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // entity id
                map(Types1_6_4.ENTITY_DATA_LIST); // entity data
                handler(wrapper -> {
                    final EntityTracker tracker = wrapper.user().get(EntityTracker.class);
                    final List<EntityData> entityDataList = wrapper.get(Types1_6_4.ENTITY_DATA_LIST, 0);
                    final int entityID = wrapper.get(Types.INT, 0);
                    final EntityTypes1_8.EntityType entityType = tracker.getTrackedEntities().get(entityID);
                    if (tracker.getTrackedEntities().containsKey(entityID)) {
                        EntityDataRewriter.transform(entityType, entityDataList);
                        if (entityDataList.isEmpty()) wrapper.cancel();

                        if (entityType.isOrHasParent(EntityTypes1_8.EntityType.WOLF)) {
                            handleWolfEntityData(entityID, entityDataList, wrapper);
                        }
                    } else {
                        wrapper.cancel();
                    }
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_5_2.CUSTOM_SOUND, new PacketHandlers() {
            @Override
            public void register() {
                handler(wrapper -> {
                    final String oldSound = wrapper.read(Types1_6_4.STRING); // sound
                    String newSound = SoundRewriter.map(oldSound);
                    if (oldSound.isEmpty()) newSound = "";
                    if (newSound == null) {
                        if (Via.getConfig().logOtherConversionWarnings()) {
                            ViaLegacy.getPlatform().getLogger().warning("Unable to map 1.5.2 sound '" + oldSound + "'");
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
        this.registerClientbound(ClientboundPackets1_5_2.AWARD_STATS, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // statistic id
                map(Types.BYTE, Types.INT); // increment
            }
        });
        this.registerClientbound(ClientboundPackets1_5_2.PLAYER_ABILITIES, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.BYTE); // flags
                handler(wrapper -> {
                    final float flySpeed = wrapper.read(Types.BYTE) / 255F; // fly speed
                    final float walkSpeed = wrapper.read(Types.BYTE) / 255F; // walk speed
                    wrapper.write(Types.FLOAT, flySpeed);
                    wrapper.write(Types.FLOAT, walkSpeed);

                    final PacketWrapper entityProperties = PacketWrapper.create(ClientboundPackets1_6_1.UPDATE_ATTRIBUTES, wrapper.user());
                    entityProperties.write(Types.INT, wrapper.user().get(EntityTracker.class).getPlayerID()); // entity id
                    entityProperties.write(Types.INT, 1); // count
                    entityProperties.write(Types1_6_4.STRING, "generic.movementSpeed"); // id
                    entityProperties.write(Types.DOUBLE, (double) walkSpeed); // value

                    wrapper.send(Protocolr1_5_2Tor1_6_1.class);
                    entityProperties.send(Protocolr1_5_2Tor1_6_1.class);
                    wrapper.cancel();
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_5_2.CUSTOM_PAYLOAD, new PacketHandlers() {
            @Override
            public void register() {
                handler(wrapper -> {
                    String channel = wrapper.read(Types1_6_4.STRING); // channel
                    short length = wrapper.read(Types.SHORT); // length

                    try {
                        if (channel.equals("MC|TPack")) {
                            channel = "MC|RPack";
                            final String[] data = new String(wrapper.read(Types.REMAINING_BYTES), StandardCharsets.UTF_8).split("\0"); // data
                            final String url = data[0];
                            final String resolution = data[1];
                            if (!resolution.equals("16")) {
                                wrapper.cancel();
                                return;
                            }

                            wrapper.write(Types.REMAINING_BYTES, url.getBytes(StandardCharsets.UTF_8));
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

        this.registerServerbound(ServerboundPackets1_6_4.SERVER_PING, wrapper -> {
            wrapper.clearPacket();
            wrapper.write(Types.BYTE, (byte) 1); // readSuccessfully
        });
        this.registerServerbound(ServerboundPackets1_6_4.PLAYER_COMMAND, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // entity id
                map(Types.BYTE); // action id
                read(Types.INT); // action parameter
                handler(wrapper -> {
                    if (wrapper.get(Types.BYTE, 0) > 5) wrapper.cancel();
                });
            }
        });
        this.registerServerbound(ServerboundPackets1_6_4.PLAYER_INPUT, ServerboundPackets1_5_2.INTERACT, wrapper -> {
            final AttachTracker attachTracker = wrapper.user().get(AttachTracker.class);
            final EntityTracker entityTracker = wrapper.user().get(EntityTracker.class);
            wrapper.read(Types.FLOAT); // sideways
            wrapper.read(Types.FLOAT); // forwards
            wrapper.read(Types.BOOLEAN); // jumping
            final boolean sneaking = wrapper.read(Types.BOOLEAN); // sneaking

            if (attachTracker.lastSneakState != sneaking) {
                attachTracker.lastSneakState = sneaking;
                if (sneaking) {
                    wrapper.write(Types.INT, entityTracker.getPlayerID()); // player id
                    wrapper.write(Types.INT, attachTracker.vehicleEntityId); // entity id
                    wrapper.write(Types.BYTE, (byte) 0); // mode
                    return;
                }
            }
            wrapper.cancel();
        });
        this.registerServerbound(ServerboundPackets1_6_4.PLAYER_ABILITIES, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.BYTE); // flags
                map(Types.FLOAT, Types.BYTE, f -> (byte) (f * 255F)); // fly speed
                map(Types.FLOAT, Types.BYTE, f -> (byte) (f * 255F)); // walk speed
            }
        });
    }

    private void handleWolfEntityData(final int entityId, final List<EntityData> entityDataList, final PacketWrapper wrapper) {
        for (EntityData entityData : entityDataList) {
            final EntityDataIndex1_7_6 index = EntityDataIndex1_7_6.searchIndex(EntityTypes1_8.EntityType.WOLF, entityData.id());

            if (index == EntityDataIndex1_7_6.TAMEABLE_FLAGS) {
                if ((entityData.<Byte>value() & 4) != 0) { // is tamed
                    final PacketWrapper attributes = PacketWrapper.create(ClientboundPackets1_6_1.UPDATE_ATTRIBUTES, wrapper.user());
                    attributes.write(Types.INT, entityId); // entity id
                    attributes.write(Types.INT, 1); // count
                    attributes.write(Types1_6_4.STRING, "generic.maxHealth"); // id
                    attributes.write(Types.DOUBLE, 20.0D); // value

                    wrapper.send(Protocolr1_5_2Tor1_6_1.class);
                    attributes.send(Protocolr1_5_2Tor1_6_1.class);
                    wrapper.cancel();
                }
                break;
            }
        }
    }

    @Override
    public void init(UserConnection userConnection) {
        userConnection.put(new PreNettySplitter(Protocolr1_5_2Tor1_6_1.class, ClientboundPackets1_5_2::getPacket));

        userConnection.put(new EntityTracker());
        userConnection.put(new AttachTracker());
    }

    @Override
    public ItemRewriter getItemRewriter() {
        return this.itemRewriter;
    }

}
