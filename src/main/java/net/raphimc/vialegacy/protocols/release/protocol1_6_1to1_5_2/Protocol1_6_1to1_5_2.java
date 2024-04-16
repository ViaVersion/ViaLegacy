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
package net.raphimc.vialegacy.protocols.release.protocol1_6_1to1_5_2;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.entities.EntityTypes1_10;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.libs.mcstructs.text.components.StringComponent;
import com.viaversion.viaversion.libs.mcstructs.text.serializer.TextComponentSerializer;
import net.raphimc.vialegacy.ViaLegacy;
import net.raphimc.vialegacy.api.protocol.StatelessProtocol;
import net.raphimc.vialegacy.api.remapper.LegacyItemRewriter;
import net.raphimc.vialegacy.api.splitter.PreNettySplitter;
import net.raphimc.vialegacy.api.util.PacketUtil;
import net.raphimc.vialegacy.protocols.release.protocol1_6_1to1_5_2.metadata.MetadataRewriter;
import net.raphimc.vialegacy.protocols.release.protocol1_6_1to1_5_2.rewriter.ItemRewriter;
import net.raphimc.vialegacy.protocols.release.protocol1_6_1to1_5_2.rewriter.SoundRewriter;
import net.raphimc.vialegacy.protocols.release.protocol1_6_1to1_5_2.storage.AttachTracker;
import net.raphimc.vialegacy.protocols.release.protocol1_6_1to1_5_2.storage.EntityTracker;
import net.raphimc.vialegacy.protocols.release.protocol1_6_2to1_6_1.ClientboundPackets1_6_1;
import net.raphimc.vialegacy.protocols.release.protocol1_7_2_5to1_6_4.ServerboundPackets1_6_4;
import net.raphimc.vialegacy.protocols.release.protocol1_7_2_5to1_6_4.types.Types1_6_4;
import net.raphimc.vialegacy.protocols.release.protocol1_8to1_7_6_10.metadata.MetaIndex1_8to1_7_6;
import net.raphimc.vialegacy.protocols.release.protocol1_8to1_7_6_10.types.Types1_7_6;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.logging.Level;

public class Protocol1_6_1to1_5_2 extends StatelessProtocol<ClientboundPackets1_5_2, ClientboundPackets1_6_1, ServerboundPackets1_5_2, ServerboundPackets1_6_4> {

    private final LegacyItemRewriter<Protocol1_6_1to1_5_2> itemRewriter = new ItemRewriter(this);

    public Protocol1_6_1to1_5_2() {
        super(ClientboundPackets1_5_2.class, ClientboundPackets1_6_1.class, ServerboundPackets1_5_2.class, ServerboundPackets1_6_4.class);
    }

    @Override
    protected void registerPackets() {
        this.itemRewriter.register();

        this.registerClientbound(ClientboundPackets1_5_2.JOIN_GAME, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.INT); // entity id
                map(Types1_6_4.STRING); // level type
                map(Type.BYTE); // game mode
                map(Type.BYTE); // dimension id
                map(Type.BYTE); // difficulty
                map(Type.BYTE); // world height
                map(Type.BYTE); // max players
                handler(wrapper -> {
                    final int entityId = wrapper.get(Type.INT, 0);
                    final EntityTracker tracker = wrapper.user().get(EntityTracker.class);
                    tracker.getTrackedEntities().put(entityId, EntityTypes1_10.EntityType.PLAYER);
                    tracker.setPlayerID(entityId);
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_5_2.CHAT_MESSAGE, new PacketHandlers() {
            @Override
            public void register() {
                map(Types1_6_4.STRING, Types1_6_4.STRING, msg -> TextComponentSerializer.V1_6.serialize(new StringComponent(msg))); // message
            }
        });
        this.registerClientbound(ClientboundPackets1_5_2.UPDATE_HEALTH, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.SHORT, Type.FLOAT); // health
                map(Type.SHORT); // food
                map(Type.FLOAT); // saturation
            }
        });
        this.registerClientbound(ClientboundPackets1_5_2.SPAWN_PLAYER, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.INT); // entity id
                map(Types1_6_4.STRING); // name
                map(Type.INT); // x
                map(Type.INT); // y
                map(Type.INT); // z
                map(Type.BYTE); // yaw
                map(Type.BYTE); // pitch
                map(Type.UNSIGNED_SHORT); // item
                map(Types1_6_4.METADATA_LIST); // metadata
                handler(wrapper -> MetadataRewriter.transform(EntityTypes1_10.EntityType.PLAYER, wrapper.get(Types1_6_4.METADATA_LIST, 0)));
                handler(wrapper -> {
                    final int entityId = wrapper.get(Type.INT, 0);
                    wrapper.user().get(EntityTracker.class).getTrackedEntities().put(entityId, EntityTypes1_10.EntityType.PLAYER);
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_5_2.COLLECT_ITEM, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.INT); // collected entity id
                map(Type.INT); // collector entity id
                handler(wrapper -> wrapper.user().get(EntityTracker.class).getTrackedEntities().remove(wrapper.get(Type.INT, 0)));
            }
        });
        this.registerClientbound(ClientboundPackets1_5_2.SPAWN_ENTITY, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.INT); // entity id
                map(Type.BYTE); // type id
                map(Type.INT); // x
                map(Type.INT); // y
                map(Type.INT); // z
                map(Type.BYTE); // pitch
                map(Type.BYTE); // yaw
                map(Type.INT); // data
                // more conditional data
                handler(wrapper -> {
                    final int entityID = wrapper.get(Type.INT, 0);
                    final int typeID = wrapper.get(Type.BYTE, 0);
                    wrapper.user().get(EntityTracker.class).getTrackedEntities().put(entityID, EntityTypes1_10.getTypeFromId(typeID, true));
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_5_2.SPAWN_MOB, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.INT); // entity id
                map(Type.UNSIGNED_BYTE); // type id
                map(Type.INT); // x
                map(Type.INT); // y
                map(Type.INT); // z
                map(Type.BYTE); // yaw
                map(Type.BYTE); // pitch
                map(Type.BYTE); // head yaw
                map(Type.SHORT); // velocity x
                map(Type.SHORT); // velocity y
                map(Type.SHORT); // velocity z
                map(Types1_6_4.METADATA_LIST); // metadata
                handler(wrapper -> {
                    final int entityID = wrapper.get(Type.INT, 0);
                    final int typeID = wrapper.get(Type.UNSIGNED_BYTE, 0);
                    final EntityTypes1_10.EntityType entityType = EntityTypes1_10.getTypeFromId(typeID, false);
                    final List<Metadata> metadataList = wrapper.get(Types1_6_4.METADATA_LIST, 0);
                    wrapper.user().get(EntityTracker.class).getTrackedEntities().put(entityID, entityType);
                    MetadataRewriter.transform(entityType, metadataList);

                    if (entityType.isOrHasParent(EntityTypes1_10.EntityType.WOLF)) {
                        handleWolfMetadata(entityID, metadataList, wrapper);
                    }
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_5_2.SPAWN_PAINTING, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.INT); // entity id
                map(Types1_6_4.STRING); // motive
                map(Types1_7_6.POSITION_INT); // position
                map(Type.INT); // rotation
                handler(wrapper -> {
                    final int entityID = wrapper.get(Type.INT, 0);
                    wrapper.user().get(EntityTracker.class).getTrackedEntities().put(entityID, EntityTypes1_10.EntityType.PAINTING);
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_5_2.SPAWN_EXPERIENCE_ORB, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.INT); // entity id
                map(Type.INT); // x
                map(Type.INT); // y
                map(Type.INT); // z
                map(Type.SHORT); // count
                handler(wrapper -> {
                    final int entityID = wrapper.get(Type.INT, 0);
                    wrapper.user().get(EntityTracker.class).getTrackedEntities().put(entityID, EntityTypes1_10.EntityType.EXPERIENCE_ORB);
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_5_2.DESTROY_ENTITIES, new PacketHandlers() {
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
        this.registerClientbound(ClientboundPackets1_5_2.ATTACH_ENTITY, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.INT); // riding entity id
                map(Type.INT); // vehicle entity id
                handler(wrapper -> {
                    final AttachTracker attachTracker = wrapper.user().get(AttachTracker.class);
                    final EntityTracker entityTracker = wrapper.user().get(EntityTracker.class);
                    final int ridingId = wrapper.get(Type.INT, 0);
                    final int vehicleId = wrapper.get(Type.INT, 1);
                    if (entityTracker.getPlayerID() == ridingId) {
                        attachTracker.vehicleEntityId = vehicleId;
                    }
                });
                create(Type.UNSIGNED_BYTE, (short) 0); // leash state
            }
        });
        this.registerClientbound(ClientboundPackets1_5_2.ENTITY_METADATA, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.INT); // entity id
                map(Types1_6_4.METADATA_LIST); // metadata
                handler(wrapper -> {
                    final EntityTracker tracker = wrapper.user().get(EntityTracker.class);
                    final List<Metadata> metadataList = wrapper.get(Types1_6_4.METADATA_LIST, 0);
                    final int entityID = wrapper.get(Type.INT, 0);
                    final EntityTypes1_10.EntityType entityType = tracker.getTrackedEntities().get(entityID);
                    if (tracker.getTrackedEntities().containsKey(entityID)) {
                        MetadataRewriter.transform(entityType, metadataList);
                        if (metadataList.isEmpty()) wrapper.cancel();

                        if (entityType.isOrHasParent(EntityTypes1_10.EntityType.WOLF)) {
                            handleWolfMetadata(entityID, metadataList, wrapper);
                        }
                    } else {
                        wrapper.cancel();
                    }
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_5_2.NAMED_SOUND, new PacketHandlers() {
            @Override
            public void register() {
                handler(wrapper -> {
                    final String oldSound = wrapper.read(Types1_6_4.STRING); // sound
                    String newSound = SoundRewriter.map(oldSound);
                    if (oldSound.isEmpty()) newSound = "";
                    if (newSound == null) {
                        if (!Via.getConfig().isSuppressConversionWarnings() || Via.getManager().isDebug()) {
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
                map(Type.INT); // x
                map(Type.INT); // y
                map(Type.INT); // z
                map(Type.FLOAT); // volume
                map(Type.UNSIGNED_BYTE); // pitch
            }
        });
        this.registerClientbound(ClientboundPackets1_5_2.STATISTICS, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.INT); // statistic id
                map(Type.BYTE, Type.INT); // increment
            }
        });
        this.registerClientbound(ClientboundPackets1_5_2.PLAYER_ABILITIES, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.BYTE); // flags
                handler(wrapper -> {
                    final float flySpeed = wrapper.read(Type.BYTE) / 255F; // fly speed
                    final float walkSpeed = wrapper.read(Type.BYTE) / 255F; // walk speed
                    wrapper.write(Type.FLOAT, flySpeed);
                    wrapper.write(Type.FLOAT, walkSpeed);

                    final PacketWrapper entityProperties = PacketWrapper.create(ClientboundPackets1_6_1.ENTITY_PROPERTIES, wrapper.user());
                    entityProperties.write(Type.INT, wrapper.user().get(EntityTracker.class).getPlayerID()); // entity id
                    entityProperties.write(Type.INT, 1); // count
                    entityProperties.write(Types1_6_4.STRING, "generic.movementSpeed"); // id
                    entityProperties.write(Type.DOUBLE, (double) walkSpeed); // value

                    wrapper.send(Protocol1_6_1to1_5_2.class);
                    entityProperties.send(Protocol1_6_1to1_5_2.class);
                    wrapper.cancel();
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_5_2.PLUGIN_MESSAGE, new PacketHandlers() {
            @Override
            public void register() {
                handler(wrapper -> {
                    String channel = wrapper.read(Types1_6_4.STRING); // channel
                    short length = wrapper.read(Type.SHORT); // length

                    try {
                        if (channel.equals("MC|TPack")) {
                            channel = "MC|RPack";
                            final String[] data = new String(wrapper.read(Type.REMAINING_BYTES), StandardCharsets.UTF_8).split("\0"); // data
                            final String url = data[0];
                            final String resolution = data[1];
                            if (!resolution.equals("16")) {
                                wrapper.cancel();
                                return;
                            }

                            wrapper.write(Type.REMAINING_BYTES, url.getBytes(StandardCharsets.UTF_8));
                            length = (short) PacketUtil.calculateLength(wrapper);
                        }
                    } catch (Exception e) {
                        if (!Via.getConfig().isSuppressConversionWarnings() || Via.getManager().isDebug()) {
                            Via.getPlatform().getLogger().log(Level.WARNING, "Failed to handle packet", e);
                        }
                        wrapper.cancel();
                        return;
                    }

                    wrapper.resetReader();
                    wrapper.write(Types1_6_4.STRING, channel); // channel
                    wrapper.write(Type.SHORT, length); // length
                });
            }
        });

        this.registerServerbound(ServerboundPackets1_6_4.SERVER_PING, wrapper -> {
            wrapper.clearPacket();
            wrapper.write(Type.BYTE, (byte) 1); // readSuccessfully
        });
        this.registerServerbound(ServerboundPackets1_6_4.ENTITY_ACTION, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.INT); // entity id
                map(Type.BYTE); // action id
                read(Type.INT); // action parameter
                handler(wrapper -> {
                    if (wrapper.get(Type.BYTE, 0) > 5) wrapper.cancel();
                });
            }
        });
        this.registerServerbound(ServerboundPackets1_6_4.STEER_VEHICLE, ServerboundPackets1_5_2.INTERACT_ENTITY, wrapper -> {
            final AttachTracker attachTracker = wrapper.user().get(AttachTracker.class);
            final EntityTracker entityTracker = wrapper.user().get(EntityTracker.class);
            wrapper.read(Type.FLOAT); // sideways
            wrapper.read(Type.FLOAT); // forwards
            wrapper.read(Type.BOOLEAN); // jumping
            final boolean sneaking = wrapper.read(Type.BOOLEAN); // sneaking

            if (attachTracker.lastSneakState != sneaking) {
                attachTracker.lastSneakState = sneaking;
                if (sneaking) {
                    wrapper.write(Type.INT, entityTracker.getPlayerID()); // player id
                    wrapper.write(Type.INT, attachTracker.vehicleEntityId); // entity id
                    wrapper.write(Type.BYTE, (byte) 0); // mode
                    return;
                }
            }
            wrapper.cancel();
        });
        this.registerServerbound(ServerboundPackets1_6_4.PLAYER_ABILITIES, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.BYTE); // flags
                map(Type.FLOAT, Type.BYTE, f -> (byte) (f * 255F)); // fly speed
                map(Type.FLOAT, Type.BYTE, f -> (byte) (f * 255F)); // walk speed
            }
        });
    }

    private void handleWolfMetadata(final int entityId, final List<Metadata> metadataList, final PacketWrapper wrapper) throws Exception {
        for (Metadata metadata : metadataList) {
            final MetaIndex1_8to1_7_6 index = MetaIndex1_8to1_7_6.searchIndex(EntityTypes1_10.EntityType.WOLF, metadata.id());

            if (index == MetaIndex1_8to1_7_6.TAMEABLE_FLAGS) {
                if ((metadata.<Byte>value() & 4) != 0) { // is tamed
                    final PacketWrapper attributes = PacketWrapper.create(ClientboundPackets1_6_1.ENTITY_PROPERTIES, wrapper.user());
                    attributes.write(Type.INT, entityId); // entity id
                    attributes.write(Type.INT, 1); // count
                    attributes.write(Types1_6_4.STRING, "generic.maxHealth"); // id
                    attributes.write(Type.DOUBLE, 20.0D); // value

                    wrapper.send(Protocol1_6_1to1_5_2.class);
                    attributes.send(Protocol1_6_1to1_5_2.class);
                    wrapper.cancel();
                }
                break;
            }
        }
    }

    @Override
    public void init(UserConnection userConnection) {
        userConnection.put(new PreNettySplitter(Protocol1_6_1to1_5_2.class, ClientboundPackets1_5_2::getPacket));

        userConnection.put(new EntityTracker());
        userConnection.put(new AttachTracker());
    }

    @Override
    public LegacyItemRewriter<Protocol1_6_1to1_5_2> getItemRewriter() {
        return this.itemRewriter;
    }

}
