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
package net.raphimc.vialegacy.protocol.release.r1_7_6_10tor1_8;

import com.google.common.base.Joiner;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.ProtocolInfo;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.*;
import com.viaversion.viaversion.api.minecraft.chunks.Chunk;
import com.viaversion.viaversion.api.minecraft.entities.EntityTypes1_8;
import com.viaversion.viaversion.api.minecraft.entitydata.EntityData;
import com.viaversion.viaversion.api.minecraft.entitydata.types.EntityDataTypes1_8;
import com.viaversion.viaversion.api.minecraft.item.DataItem;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.platform.providers.ViaProviders;
import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.packet.State;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.api.protocol.remapper.ValueTransformer;
import com.viaversion.viaversion.api.type.Types;
import com.viaversion.viaversion.api.type.types.chunk.BulkChunkType1_8;
import com.viaversion.viaversion.api.type.types.chunk.ChunkType1_8;
import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viaversion.libs.gson.JsonObject;
import com.viaversion.viaversion.libs.mcstructs.text.serializer.TextComponentSerializer;
import com.viaversion.viaversion.protocols.base.ClientboundLoginPackets;
import com.viaversion.viaversion.protocols.base.ServerboundLoginPackets;
import com.viaversion.viaversion.protocols.v1_8to1_9.packet.ClientboundPackets1_8;
import com.viaversion.viaversion.protocols.v1_8to1_9.packet.ServerboundPackets1_8;
import com.viaversion.viaversion.util.IdAndData;
import net.raphimc.vialegacy.ViaLegacy;
import net.raphimc.vialegacy.api.data.ItemList1_6;
import net.raphimc.vialegacy.api.util.GameProfileUtil;
import net.raphimc.vialegacy.api.util.PacketUtil;
import net.raphimc.vialegacy.protocol.release.r1_7_2_5tor1_7_6_10.packet.ClientboundPackets1_7_2;
import net.raphimc.vialegacy.protocol.release.r1_7_2_5tor1_7_6_10.packet.ServerboundPackets1_7_2;
import net.raphimc.vialegacy.protocol.release.r1_7_6_10tor1_8.data.EntityDataIndex1_7_6;
import net.raphimc.vialegacy.protocol.release.r1_7_6_10tor1_8.data.Particle1_7_6;
import net.raphimc.vialegacy.protocol.release.r1_7_6_10tor1_8.model.MapData;
import net.raphimc.vialegacy.protocol.release.r1_7_6_10tor1_8.model.MapIcon;
import net.raphimc.vialegacy.protocol.release.r1_7_6_10tor1_8.model.TabListEntry;
import net.raphimc.vialegacy.protocol.release.r1_7_6_10tor1_8.provider.GameProfileFetcher;
import net.raphimc.vialegacy.protocol.release.r1_7_6_10tor1_8.rewriter.EntityDataRewriter;
import net.raphimc.vialegacy.protocol.release.r1_7_6_10tor1_8.rewriter.ItemRewriter;
import net.raphimc.vialegacy.protocol.release.r1_7_6_10tor1_8.rewriter.TextRewriter;
import net.raphimc.vialegacy.protocol.release.r1_7_6_10tor1_8.storage.*;
import net.raphimc.vialegacy.protocol.release.r1_7_6_10tor1_8.types.Types1_7_6;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class Protocolr1_7_6_10Tor1_8 extends AbstractProtocol<ClientboundPackets1_7_2, ClientboundPackets1_8, ServerboundPackets1_7_2, ServerboundPackets1_8> {

    private final ItemRewriter itemRewriter = new ItemRewriter(this);
    private final TextRewriter chatComponentRewriter = new TextRewriter(this);
    private final EntityDataRewriter entityDataRewriter = new EntityDataRewriter(this);

    public Protocolr1_7_6_10Tor1_8() {
        super(ClientboundPackets1_7_2.class, ClientboundPackets1_8.class, ServerboundPackets1_7_2.class, ServerboundPackets1_8.class);
    }

    public static final ValueTransformer<String, String> LEGACY_TO_JSON = new ValueTransformer<>(Types.STRING, Types.STRING) {
        @Override
        public String transform(PacketWrapper packetWrapper, String message) {
            final JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("text", message);
            return jsonObject.toString();
        }
    };

    public static final ValueTransformer<String, String> LEGACY_TO_JSON_TRANSLATE = new ValueTransformer<>(Types.STRING, Types.STRING) {
        @Override
        public String transform(PacketWrapper packetWrapper, String message) {
            final JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("translate", message);
            return jsonObject.toString();
        }
    };

    @Override
    protected void registerPackets() {
        super.registerPackets();

        this.registerClientbound(State.LOGIN, ClientboundLoginPackets.HELLO, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.STRING); // server hash
                map(Types.SHORT_BYTE_ARRAY, Types.BYTE_ARRAY_PRIMITIVE); // public key
                map(Types.SHORT_BYTE_ARRAY, Types.BYTE_ARRAY_PRIMITIVE); // verify token
            }
        });
        this.registerClientbound(State.LOGIN, ClientboundLoginPackets.LOGIN_FINISHED, new PacketHandlers() {
            @Override
            public void register() {
                read(Types.STRING); // uuid
                read(Types.STRING); // name
                handler(wrapper -> { // 1.7.10 ignores the data from the server
                    final ProtocolInfo protocolInfo = wrapper.user().getProtocolInfo();
                    wrapper.write(Types.STRING, protocolInfo.getUuid().toString()); // uuid
                    wrapper.write(Types.STRING, protocolInfo.getUsername()); // name
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_7_2.KEEP_ALIVE, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT, Types.VAR_INT); // key
            }
        });
        this.registerClientbound(ClientboundPackets1_7_2.LOGIN, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // entity id
                map(Types.UNSIGNED_BYTE); // gamemode
                map(Types.BYTE); // dimension id
                map(Types.UNSIGNED_BYTE); // difficulty
                map(Types.UNSIGNED_BYTE); // max players
                map(Types.STRING); // level type
                create(Types.BOOLEAN, false); // reduced debug info
                handler(wrapper -> {
                    final ProtocolInfo protocolInfo = wrapper.user().getProtocolInfo();
                    final TablistStorage tablistStorage = wrapper.user().get(TablistStorage.class);
                    tablistStorage.sendTempEntry(new TabListEntry(protocolInfo.getUsername(), protocolInfo.getUuid())); // load own skin

                    final int entityId = wrapper.get(Types.INT, 0);
                    final byte dimensionId = wrapper.get(Types.BYTE, 0);
                    final EntityTracker tracker = wrapper.user().get(EntityTracker.class);
                    tracker.trackEntity(entityId, EntityTypes1_8.EntityType.PLAYER);
                    tracker.setPlayerID(entityId);
                    wrapper.user().getClientWorld(Protocolr1_7_6_10Tor1_8.class).setEnvironment(dimensionId);

                    wrapper.send(Protocolr1_7_6_10Tor1_8.class);
                    wrapper.cancel();

                    final PacketWrapper setBorder = PacketWrapper.create(ClientboundPackets1_8.SET_BORDER, wrapper.user());
                    setBorder.write(Types.VAR_INT, 3); // action (INITIALIZE)
                    setBorder.write(Types.DOUBLE, 0D); // center x
                    setBorder.write(Types.DOUBLE, 0D); // center z
                    setBorder.write(Types.DOUBLE, 0D); // old size
                    setBorder.write(Types.DOUBLE, 60_000_000D); // new size
                    setBorder.write(Types.VAR_LONG, 0L); // lerp time
                    setBorder.write(Types.VAR_INT, 60_000_000); // new absolute max size
                    setBorder.write(Types.VAR_INT, 0); // warning blocks
                    setBorder.write(Types.VAR_INT, 0); // warning time
                    setBorder.send(Protocolr1_7_6_10Tor1_8.class);
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_7_2.CHAT, new PacketHandlers() {
            @Override
            public void register() {
                handler(wrapper -> wrapper.write(Types.STRING, chatComponentRewriter.toClient(wrapper.user(), wrapper.read(Types.STRING)))); // message
                create(Types.BYTE, (byte) 0); // position
            }
        });
        this.registerClientbound(ClientboundPackets1_7_2.SET_EQUIPPED_ITEM, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT, Types.VAR_INT); // entity id
                map(Types.SHORT); // slot
                map(Types1_7_6.ITEM, Types.ITEM1_8); // item
                handler(wrapper -> itemRewriter.handleItemToClient(wrapper.user(), wrapper.get(Types.ITEM1_8, 0)));
            }
        });
        this.registerClientbound(ClientboundPackets1_7_2.SET_DEFAULT_SPAWN_POSITION, new PacketHandlers() {
            @Override
            public void register() {
                map(Types1_7_6.BLOCK_POSITION_INT, Types.BLOCK_POSITION1_8); // position
            }
        });
        this.registerClientbound(ClientboundPackets1_7_2.SET_HEALTH, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.FLOAT); // health
                map(Types.SHORT, Types.VAR_INT); // food
                map(Types.FLOAT); // saturation
            }
        });
        this.registerClientbound(ClientboundPackets1_7_2.RESPAWN, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // dimension id
                map(Types.UNSIGNED_BYTE); // difficulty
                map(Types.UNSIGNED_BYTE); // gamemode
                map(Types.STRING); // worldType
                handler(wrapper -> {
                    final EntityTracker entityTracker = wrapper.user().get(EntityTracker.class);
                    if (wrapper.user().getClientWorld(Protocolr1_7_6_10Tor1_8.class).setEnvironment(wrapper.get(Types.INT, 0))) {
                        wrapper.user().get(ChunkTracker.class).clear();
                        entityTracker.clear();
                        entityTracker.trackEntity(entityTracker.getPlayerID(), EntityTypes1_8.EntityType.PLAYER);
                    }

                    final ProtocolInfo protocolInfo = wrapper.user().getProtocolInfo();
                    final TablistStorage tablistStorage = wrapper.user().get(TablistStorage.class);
                    tablistStorage.sendTempEntry(new TabListEntry(protocolInfo.getUsername(), protocolInfo.getUuid())); // load own skin

                    wrapper.send(Protocolr1_7_6_10Tor1_8.class);
                    wrapper.cancel();

                    final PacketWrapper setBorder = PacketWrapper.create(ClientboundPackets1_8.SET_BORDER, wrapper.user());
                    setBorder.write(Types.VAR_INT, 3); // action (INITIALIZE)
                    setBorder.write(Types.DOUBLE, 0D); // center x
                    setBorder.write(Types.DOUBLE, 0D); // center z
                    setBorder.write(Types.DOUBLE, 0D); // old size
                    setBorder.write(Types.DOUBLE, 60_000_000D); // new size
                    setBorder.write(Types.VAR_LONG, 0L); // lerp time
                    setBorder.write(Types.VAR_INT, 60_000_000); // new absolute max size
                    setBorder.write(Types.VAR_INT, 0); // warning blocks
                    setBorder.write(Types.VAR_INT, 0); // warning time
                    setBorder.send(Protocolr1_7_6_10Tor1_8.class);

                    // 1.7 doesn't keep entity data after respawn, but 1.8 does
                    final List<EntityData> defaultEntityData = new ArrayList<>();
                    defaultEntityData.add(new EntityData(EntityDataIndex1_7_6.ENTITY_FLAGS.getNewIndex(), EntityDataTypes1_8.BYTE, (byte) 0));
                    defaultEntityData.add(new EntityData(EntityDataIndex1_7_6.ENTITY_AIR.getNewIndex(), EntityDataTypes1_8.SHORT, (short) 300));
                    defaultEntityData.add(new EntityData(EntityDataIndex1_7_6.ENTITY_LIVING_POTION_EFFECT_COLOR.getNewIndex(), EntityDataTypes1_8.INT, 0));
                    defaultEntityData.add(new EntityData(EntityDataIndex1_7_6.ENTITY_LIVING_IS_POTION_EFFECT_AMBIENT.getNewIndex(), EntityDataTypes1_8.BYTE, (byte) 0));
                    defaultEntityData.add(new EntityData(EntityDataIndex1_7_6.ENTITY_LIVING_ARROWS.getNewIndex(), EntityDataTypes1_8.BYTE, (byte) 0));
                    defaultEntityData.add(new EntityData(EntityDataIndex1_7_6.HUMAN_SKIN_FLAGS.getNewIndex(), EntityDataTypes1_8.BYTE, (byte) 0));
                    defaultEntityData.add(new EntityData(EntityDataIndex1_7_6.HUMAN_ABSORPTION_HEARTS.getNewIndex(), EntityDataTypes1_8.FLOAT, 0F));
                    defaultEntityData.add(new EntityData(EntityDataIndex1_7_6.HUMAN_SCORE.getNewIndex(), EntityDataTypes1_8.INT, 0));
                    final PacketWrapper setEntityData = PacketWrapper.create(ClientboundPackets1_8.SET_ENTITY_DATA, wrapper.user());
                    setEntityData.write(Types.VAR_INT, entityTracker.getPlayerID()); // entity id
                    setEntityData.write(Types.ENTITY_DATA_LIST1_8, defaultEntityData); // entity data
                    setEntityData.send(Protocolr1_7_6_10Tor1_8.class);
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_7_2.PLAYER_POSITION, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.DOUBLE); // x
                map(Types.DOUBLE, Types.DOUBLE, stance -> stance - 1.62F); // y
                map(Types.DOUBLE); // z
                map(Types.FLOAT); // yaw
                map(Types.FLOAT); // pitch
                read(Types.BOOLEAN); // onGround
                create(Types.BYTE, (byte) 0); // flags
            }
        });
        this.registerClientbound(ClientboundPackets1_7_2.PLAYER_SLEEP, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT, Types.VAR_INT); // entity id
                map(Types1_7_6.BLOCK_POSITION_BYTE, Types.BLOCK_POSITION1_8); // position
            }
        });
        this.registerClientbound(ClientboundPackets1_7_2.ADD_PLAYER, wrapper -> {
            final int entityID = wrapper.passthrough(Types.VAR_INT); // entity id
            final UUID uuid = UUID.fromString(wrapper.read(Types.STRING)); // uuid
            wrapper.write(Types.UUID, uuid);
            final String name = wrapper.read(Types.STRING); // name
            final GameProfile.Property[] properties = new GameProfile.Property[wrapper.read(Types.VAR_INT)]; // properties count
            for (int i = 0; i < properties.length; i++) {
                final String key = wrapper.read(Types.STRING); // name
                final String value = wrapper.read(Types.STRING); // value
                final String signature = wrapper.read(Types.STRING); // signature
                properties[i] = new GameProfile.Property(key, value, signature);
            }

            wrapper.passthrough(Types.INT); // x
            wrapper.passthrough(Types.INT); // y
            wrapper.passthrough(Types.INT); // z
            wrapper.passthrough(Types.BYTE); // yaw
            wrapper.passthrough(Types.BYTE); // pitch

            final short itemId = wrapper.read(Types.SHORT); // item in hand
            final Item currentItem = new DataItem(itemId, (byte) 1, (short) 0, null);
            itemRewriter.handleItemToClient(wrapper.user(), currentItem);
            wrapper.write(Types.SHORT, (short) currentItem.identifier());

            final List<EntityData> entityDataList = wrapper.read(Types1_7_6.ENTITY_DATA_LIST); // entity data
            entityDataRewriter.transform(wrapper.user(), EntityTypes1_8.EntityType.PLAYER, entityDataList);
            wrapper.write(Types.ENTITY_DATA_LIST1_8, entityDataList);

            wrapper.user().get(TablistStorage.class).sendTempEntry(new TabListEntry(new GameProfile(name, uuid, properties)));
            wrapper.user().get(EntityTracker.class).trackEntity(entityID, EntityTypes1_8.EntityType.PLAYER);
        });
        this.registerClientbound(ClientboundPackets1_7_2.TAKE_ITEM_ENTITY, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT, Types.VAR_INT); // collected entity id
                map(Types.INT, Types.VAR_INT); // collector entity id
                handler(wrapper -> wrapper.user().get(EntityTracker.class).removeEntity(wrapper.get(Types.VAR_INT, 0)));
            }
        });
        this.registerClientbound(ClientboundPackets1_7_2.ADD_ENTITY, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.VAR_INT); // entity id
                map(Types.BYTE); // type id
                map(Types.INT); // x
                map(Types.INT); // y
                map(Types.INT); // z
                map(Types.BYTE); // pitch
                map(Types.BYTE); // yaw
                map(Types.INT); // data
                handler(wrapper -> {
                    final EntityTracker tracker = wrapper.user().get(EntityTracker.class);
                    final int entityID = wrapper.get(Types.VAR_INT, 0);
                    final int typeID = wrapper.get(Types.BYTE, 0);
                    int x = wrapper.get(Types.INT, 0);
                    int y = wrapper.get(Types.INT, 1);
                    int z = wrapper.get(Types.INT, 2);
                    byte yaw = wrapper.get(Types.BYTE, 2);
                    int data = wrapper.get(Types.INT, 3);
                    final EntityTypes1_8.EntityType type = EntityTypes1_8.ObjectType.getEntityType(typeID, data);
                    if (type == null) {
                        return;
                    }
                    tracker.trackEntity(entityID, type);
                    tracker.updateEntityLocation(entityID, x, y, z, false);

                    if (type == EntityTypes1_8.ObjectType.ITEM_FRAME.getType()) {
                        yaw = switch (data) {
                            case 0 -> {
                                z += 32;
                                yield 0;
                            }
                            case 1 -> {
                                x -= 32;
                                yield 64;
                            }
                            case 2 -> {
                                z -= 32;
                                yield -128;
                            }
                            case 3 -> {
                                x += 32;
                                yield -64;
                            }
                            default -> yaw;
                        };
                    } else if (type == EntityTypes1_8.ObjectType.FALLING_BLOCK.getType()) {
                        final int id = data & 0xffff;
                        final int metadata = data >> 16;
                        final IdAndData block = new IdAndData(id, metadata);
                        wrapper.user().get(ChunkTracker.class).remapBlockParticle(block);
                        data = block.getId() | block.getData() << 12;
                    }

                    y = realignEntityY(type, y);

                    wrapper.set(Types.INT, 0, x);
                    wrapper.set(Types.INT, 1, y);
                    wrapper.set(Types.INT, 2, z);
                    wrapper.set(Types.BYTE, 2, yaw);
                    wrapper.set(Types.INT, 3, data);
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_7_2.ADD_MOB, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.VAR_INT); // entity id
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
                map(Types1_7_6.ENTITY_DATA_LIST, Types.ENTITY_DATA_LIST1_8); // entity data
                handler(wrapper -> {
                    final EntityTracker tracker = wrapper.user().get(EntityTracker.class);
                    final int entityID = wrapper.get(Types.VAR_INT, 0);
                    final int typeID = wrapper.get(Types.UNSIGNED_BYTE, 0);
                    final int x = wrapper.get(Types.INT, 0);
                    final int y = wrapper.get(Types.INT, 1);
                    final int z = wrapper.get(Types.INT, 2);
                    final List<EntityData> entityDataList = wrapper.get(Types.ENTITY_DATA_LIST1_8, 0);
                    final EntityTypes1_8.EntityType entityType = EntityTypes1_8.EntityType.findById(typeID);
                    if (entityType == null) {
                        wrapper.cancel();
                        return;
                    }
                    tracker.trackEntity(entityID, entityType);
                    tracker.updateEntityLocation(entityID, x, y, z, false);
                    tracker.updateEntityData(entityID, entityDataList);

                    entityDataRewriter.transform(wrapper.user(), entityType, entityDataList);
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_7_2.ADD_PAINTING, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.VAR_INT); // entity id
                map(Types.STRING); // motive
                map(Types1_7_6.BLOCK_POSITION_INT, Types.BLOCK_POSITION1_8); // position
                map(Types.INT, Types.BYTE); // rotation
                handler(wrapper -> {
                    final short rotation = wrapper.get(Types.BYTE, 0);
                    final BlockPosition pos = wrapper.get(Types.BLOCK_POSITION1_8, 0);
                    int modX = 0;
                    int modZ = 0;
                    switch (rotation) {
                        case 0 -> modZ = 1;
                        case 1 -> modX = -1;
                        case 2 -> modZ = -1;
                        case 3 -> modX = 1;
                    }
                    wrapper.set(Types.BLOCK_POSITION1_8, 0, new BlockPosition(pos.x() + modX, pos.y(), pos.z() + modZ));

                    final int entityID = wrapper.get(Types.VAR_INT, 0);
                    wrapper.user().get(EntityTracker.class).trackEntity(entityID, EntityTypes1_8.EntityType.PAINTING);
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_7_2.ADD_EXPERIENCE_ORB, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.VAR_INT); // entity id
                map(Types.INT); // x
                map(Types.INT); // y
                map(Types.INT); // z
                map(Types.SHORT); // count
                handler(wrapper -> {
                    final int entityID = wrapper.get(Types.VAR_INT, 0);
                    wrapper.user().get(EntityTracker.class).trackEntity(entityID, EntityTypes1_8.EntityType.EXPERIENCE_ORB);

                    wrapper.set(Types.INT, 1, realignEntityY(EntityTypes1_8.EntityType.EXPERIENCE_ORB, wrapper.get(Types.INT, 1)));
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_7_2.SET_ENTITY_MOTION, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT, Types.VAR_INT); // entity id
                map(Types.SHORT); // velocity x
                map(Types.SHORT); // velocity y
                map(Types.SHORT); // velocity z
            }
        });
        this.registerClientbound(ClientboundPackets1_7_2.REMOVE_ENTITIES, new PacketHandlers() {
            @Override
            public void register() {
                map(Types1_7_6.INT_ARRAY, Types.VAR_INT_ARRAY_PRIMITIVE); // entity ids
                handler(wrapper -> {
                    final EntityTracker tracker = wrapper.user().get(EntityTracker.class);
                    for (int entityId : wrapper.get(Types.VAR_INT_ARRAY_PRIMITIVE, 0)) {
                        tracker.removeEntity(entityId);
                    }
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_7_2.MOVE_ENTITY, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT, Types.VAR_INT); // entity id
            }
        });
        this.registerClientbound(ClientboundPackets1_7_2.MOVE_ENTITY_POS, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT, Types.VAR_INT); // entity id
                map(Types.BYTE); // x
                map(Types.BYTE); // y
                map(Types.BYTE); // z
                handler(wrapper -> {
                    final EntityTracker entityTracker = wrapper.user().get(EntityTracker.class);
                    final int entityId = wrapper.get(Types.VAR_INT, 0);
                    final byte x = wrapper.get(Types.BYTE, 0);
                    final byte y = wrapper.get(Types.BYTE, 1);
                    final byte z = wrapper.get(Types.BYTE, 2);
                    entityTracker.updateEntityLocation(entityId, x, y, z, true);

                    if (ViaLegacy.getConfig().isDynamicOnground()) {
                        final boolean onGround = wrapper.get(Types.BYTE, 1) > -8/*0.25D*/;
                        entityTracker.getGroundMap().put(wrapper.get(Types.VAR_INT, 0), onGround);
                        wrapper.write(Types.BOOLEAN, onGround); // onGround
                    } else {
                        wrapper.write(Types.BOOLEAN, true); // onGround
                    }
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_7_2.MOVE_ENTITY_ROT, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT, Types.VAR_INT); // entity id
                map(Types.BYTE); // yaw
                map(Types.BYTE); // pitch
                handler(wrapper -> {
                    if (ViaLegacy.getConfig().isDynamicOnground()) {
                        final EntityTracker entityTracker = wrapper.user().get(EntityTracker.class);
                        wrapper.write(Types.BOOLEAN, entityTracker.getGroundMap().getOrDefault(wrapper.get(Types.VAR_INT, 0), true)); // onGround
                    } else {
                        wrapper.write(Types.BOOLEAN, true); // onGround
                    }
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_7_2.MOVE_ENTITY_POS_ROT, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT, Types.VAR_INT); // entity id
                map(Types.BYTE); // x
                map(Types.BYTE); // y
                map(Types.BYTE); // z
                map(Types.BYTE); // yaw
                map(Types.BYTE); // pitch
                handler(wrapper -> {
                    final EntityTracker entityTracker = wrapper.user().get(EntityTracker.class);
                    final int entityId = wrapper.get(Types.VAR_INT, 0);
                    final byte x = wrapper.get(Types.BYTE, 0);
                    final byte y = wrapper.get(Types.BYTE, 1);
                    final byte z = wrapper.get(Types.BYTE, 2);
                    entityTracker.updateEntityLocation(entityId, x, y, z, true);

                    if (ViaLegacy.getConfig().isDynamicOnground()) {
                        final boolean onGround = wrapper.get(Types.BYTE, 1) > -8/*0.25D*/;
                        entityTracker.getGroundMap().put(wrapper.get(Types.VAR_INT, 0), onGround);
                        wrapper.write(Types.BOOLEAN, onGround); // onGround
                    } else {
                        wrapper.write(Types.BOOLEAN, true); // onGround
                    }
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_7_2.TELEPORT_ENTITY, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT, Types.VAR_INT); // entity id
                map(Types.INT); // x
                map(Types.INT); // y
                map(Types.INT); // z
                map(Types.BYTE); // yaw
                map(Types.BYTE); // pitch
                create(Types.BOOLEAN, true); // onGround
                handler(wrapper -> {
                    final EntityTracker entityTracker = wrapper.user().get(EntityTracker.class);
                    final int entityId = wrapper.get(Types.VAR_INT, 0);
                    final int x = wrapper.get(Types.INT, 0);
                    final int y = wrapper.get(Types.INT, 1);
                    final int z = wrapper.get(Types.INT, 2);
                    entityTracker.updateEntityLocation(entityId, x, y, z, false);

                    final EntityTypes1_8.EntityType type = entityTracker.getTrackedEntities().get(entityId);
                    if (type != null) {
                        wrapper.set(Types.INT, 1, realignEntityY(type, y));
                    }
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_7_2.ROTATE_HEAD, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT, Types.VAR_INT); // entity id
                map(Types.BYTE); // head yaw
            }
        });
        this.registerClientbound(ClientboundPackets1_7_2.SET_ENTITY_LINK, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // riding entity id
                map(Types.INT); // vehicle entity id
                map(Types.UNSIGNED_BYTE); // leash state
                handler(wrapper -> {
                    final short leashState = wrapper.get(Types.UNSIGNED_BYTE, 0);
                    if (leashState == 0) {
                        final EntityTracker tracker = wrapper.user().get(EntityTracker.class);
                        final int ridingId = wrapper.get(Types.INT, 0);
                        final int vehicleId = wrapper.get(Types.INT, 1);
                        tracker.updateEntityAttachState(ridingId, vehicleId);
                    }
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_7_2.SET_ENTITY_DATA, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT, Types.VAR_INT); // entity id
                map(Types1_7_6.ENTITY_DATA_LIST, Types.ENTITY_DATA_LIST1_8); // entity data
                handler(wrapper -> {
                    final EntityTracker tracker = wrapper.user().get(EntityTracker.class);
                    final List<EntityData> entityDataList = wrapper.get(Types.ENTITY_DATA_LIST1_8, 0);
                    final int entityID = wrapper.get(Types.VAR_INT, 0);
                    if (tracker.getTrackedEntities().containsKey(entityID)) {
                        tracker.updateEntityData(entityID, entityDataList);
                        entityDataRewriter.transform(wrapper.user(), tracker.getTrackedEntities().get(entityID), entityDataList);
                        if (entityDataList.isEmpty()) wrapper.cancel();
                    } else {
                        wrapper.cancel();
                    }
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_7_2.UPDATE_MOB_EFFECT, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT, Types.VAR_INT); // entity id
                map(Types.BYTE); // effect id
                map(Types.BYTE); // amplifier
                map(Types.SHORT, Types.VAR_INT); // duration
                create(Types.BOOLEAN, false); // hide particles
            }
        });
        this.registerClientbound(ClientboundPackets1_7_2.REMOVE_MOB_EFFECT, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT, Types.VAR_INT); // entity id
                map(Types.BYTE); // effect id
            }
        });
        this.registerClientbound(ClientboundPackets1_7_2.SET_EXPERIENCE, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.FLOAT); // experience bar
                map(Types.SHORT, Types.VAR_INT); // level
                map(Types.SHORT, Types.VAR_INT); // total experience
            }
        });
        this.registerClientbound(ClientboundPackets1_7_2.UPDATE_ATTRIBUTES, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT, Types.VAR_INT); // entity id
                handler(wrapper -> {
                    final int amount = wrapper.passthrough(Types.INT); // count
                    for (int i = 0; i < amount; i++) {
                        wrapper.passthrough(Types.STRING); // id
                        wrapper.passthrough(Types.DOUBLE); // baseValue
                        final int modifierlength = wrapper.read(Types.SHORT); // modifier count
                        wrapper.write(Types.VAR_INT, modifierlength);
                        for (int j = 0; j < modifierlength; j++) {
                            wrapper.passthrough(Types.UUID); // modifier uuid
                            wrapper.passthrough(Types.DOUBLE); // modifier amount
                            wrapper.passthrough(Types.BYTE); // modifier operation
                        }
                    }
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_7_2.LEVEL_CHUNK, wrapper -> {
            final Environment dimension = wrapper.user().getClientWorld(Protocolr1_7_6_10Tor1_8.class).getEnvironment();

            final Chunk chunk = wrapper.read(Types1_7_6.getChunk(dimension));
            wrapper.user().get(ChunkTracker.class).trackAndRemap(chunk);
            wrapper.write(ChunkType1_8.forEnvironment(dimension), chunk);
        });
        this.registerClientbound(ClientboundPackets1_7_2.CHUNK_BLOCKS_UPDATE, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // chunkX
                map(Types.INT); // chunkZ
                map(Types1_7_6.BLOCK_CHANGE_RECORD_ARRAY, Types.BLOCK_CHANGE_ARRAY); // blockChangeRecords
                handler(wrapper -> {
                    final int chunkX = wrapper.get(Types.INT, 0);
                    final int chunkZ = wrapper.get(Types.INT, 1);
                    final BlockChangeRecord[] blockChangeRecords = wrapper.get(Types.BLOCK_CHANGE_ARRAY, 0);
                    for (BlockChangeRecord record : blockChangeRecords) {
                        final int targetX = record.getSectionX() + (chunkX << 4);
                        final int targetY = record.getY(-1);
                        final int targetZ = record.getSectionZ() + (chunkZ << 4);
                        final IdAndData block = IdAndData.fromRawData(record.getBlockId());
                        final BlockPosition pos = new BlockPosition(targetX, targetY, targetZ);
                        wrapper.user().get(ChunkTracker.class).trackAndRemap(pos, block);
                        record.setBlockId(block.toRawData());
                    }
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_7_2.BLOCK_UPDATE, new PacketHandlers() {
            @Override
            public void register() {
                map(Types1_7_6.BLOCK_POSITION_UBYTE, Types.BLOCK_POSITION1_8); // position
                handler(wrapper -> {
                    final int blockId = wrapper.read(Types.VAR_INT); // block id
                    final int data = wrapper.read(Types.UNSIGNED_BYTE); // block data
                    final BlockPosition pos = wrapper.get(Types.BLOCK_POSITION1_8, 0); // position
                    final IdAndData block = new IdAndData(blockId, data);
                    wrapper.user().get(ChunkTracker.class).trackAndRemap(pos, block);
                    wrapper.write(Types.VAR_INT, block.toRawData());
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_7_2.BLOCK_EVENT, new PacketHandlers() {
            @Override
            public void register() {
                map(Types1_7_6.BLOCK_POSITION_SHORT, Types.BLOCK_POSITION1_8); // position
                map(Types.UNSIGNED_BYTE); // type
                map(Types.UNSIGNED_BYTE); // data
                map(Types.VAR_INT); // block id
            }
        });
        this.registerClientbound(ClientboundPackets1_7_2.BLOCK_DESTRUCTION, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.VAR_INT); // entity id
                map(Types1_7_6.BLOCK_POSITION_INT, Types.BLOCK_POSITION1_8); // position
                map(Types.BYTE); // progress
            }
        });
        this.registerClientbound(ClientboundPackets1_7_2.MAP_BULK_CHUNK, wrapper -> {
            final Chunk[] chunks = wrapper.read(Types1_7_6.CHUNK_BULK);
            for (Chunk chunk : chunks) {
                wrapper.user().get(ChunkTracker.class).trackAndRemap(chunk);
            }
            wrapper.write(BulkChunkType1_8.TYPE, chunks);
        });
        this.registerClientbound(ClientboundPackets1_7_2.EXPLODE, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.FLOAT); // x
                map(Types.FLOAT); // y
                map(Types.FLOAT); // z
                map(Types.FLOAT); // radius
                map(Types.INT); // record count
                handler(wrapper -> {
                    final int x = wrapper.get(Types.FLOAT, 0).intValue();
                    final int y = wrapper.get(Types.FLOAT, 1).intValue();
                    final int z = wrapper.get(Types.FLOAT, 2).intValue();
                    final int recordCount = wrapper.get(Types.INT, 0);
                    final ChunkTracker chunkTracker = wrapper.user().get(ChunkTracker.class);
                    for (int i = 0; i < recordCount; i++) {
                        final BlockPosition pos = new BlockPosition(x + wrapper.passthrough(Types.BYTE), y + wrapper.passthrough(Types.BYTE), z + wrapper.passthrough(Types.BYTE));
                        chunkTracker.trackAndRemap(pos, new IdAndData(0, 0));
                    }
                });
                map(Types.FLOAT); // velocity x
                map(Types.FLOAT); // velocity y
                map(Types.FLOAT); // velocity z
            }
        });
        this.registerClientbound(ClientboundPackets1_7_2.LEVEL_EVENT, wrapper -> {
            int effectId = wrapper.read(Types.INT); // effect id
            final BlockPosition pos = wrapper.read(Types1_7_6.BLOCK_POSITION_UBYTE); // position
            int data = wrapper.read(Types.INT); // data
            final boolean disableRelativeVolume = wrapper.read(Types.BOOLEAN); // server wide

            if (!disableRelativeVolume && effectId == 2006) { // block dust effect
                wrapper.setPacketType(ClientboundPackets1_8.LEVEL_PARTICLES);
                final Random rnd = new Random();
                final ChunkTracker chunkTracker = wrapper.user().get(ChunkTracker.class);
                final IdAndData block = chunkTracker.getBlockNotNull(pos);
                if (block.getId() != 0) {
                    double var21 = Math.min(0.2F + (float) data / 15.0F, 10.0F);
                    if (var21 > 2.5D) var21 = 2.5D;
                    final float var25 = randomFloatClamp(rnd, 0.0F, ((float) Math.PI * 2F));
                    final double var26 = randomFloatClamp(rnd, 0.75F, 1.0F);

                    final float offsetY = (float) (0.20000000298023224D + var21 / 100.0D);
                    final float offsetX = (float) (Math.cos(var25) * 0.2F * var26 * var26 * (var21 + 0.2D));
                    final float offsetZ = (float) (Math.sin(var25) * 0.2F * var26 * var26 * (var21 + 0.2D));
                    final int amount = (int) (150.0D * var21);

                    wrapper.write(Types.INT, Particle1_7_6.BLOCK_DUST.ordinal());
                    wrapper.write(Types.BOOLEAN, false); // longDistance
                    wrapper.write(Types.FLOAT, pos.x() + 0.5F);
                    wrapper.write(Types.FLOAT, pos.y() + 1.0F);
                    wrapper.write(Types.FLOAT, pos.z() + 0.5F);
                    wrapper.write(Types.FLOAT, offsetX);
                    wrapper.write(Types.FLOAT, offsetY);
                    wrapper.write(Types.FLOAT, offsetZ);
                    wrapper.write(Types.FLOAT, 0.15000000596046448F); // particleSpeed
                    wrapper.write(Types.INT, amount);
                    wrapper.write(Types.VAR_INT, block.getId() | (block.getData() << 12));
                } else {
                    wrapper.cancel();
                }
            } else {
                if (!disableRelativeVolume && effectId == 1003) { // door_open
                    if (Math.random() > 0.5) {
                        effectId = 1006; // door_close
                    }
                } else if (!disableRelativeVolume && effectId == 2001) { // block break effect
                    final ChunkTracker chunkTracker = wrapper.user().get(ChunkTracker.class);
                    final int blockID = data & 4095;
                    final int blockData = data >> 12 & 255;
                    final IdAndData block = new IdAndData(blockID, blockData);
                    chunkTracker.remapBlockParticle(block);
                    data = block.getId() | (block.getData() << 12);
                }

                wrapper.write(Types.INT, effectId);
                wrapper.write(Types.BLOCK_POSITION1_8, pos);
                wrapper.write(Types.INT, data);
                wrapper.write(Types.BOOLEAN, disableRelativeVolume);
            }
        });
        this.registerClientbound(ClientboundPackets1_7_2.LEVEL_PARTICLES, wrapper -> {
            final String[] parts = wrapper.read(Types.STRING).split("_", 3);
            Particle1_7_6 particle = Particle1_7_6.find(parts[0]);
            if (particle == null) {
                particle = Particle1_7_6.BARRIER;
                if (Via.getConfig().logOtherConversionWarnings()) {
                    ViaLegacy.getPlatform().getLogger().warning("Could not find 1.8 particle for " + Arrays.toString(parts));
                }
            }
            wrapper.write(Types.INT, particle.ordinal()); // particle id
            wrapper.write(Types.BOOLEAN, false); // long distance
            wrapper.passthrough(Types.FLOAT); // x
            wrapper.passthrough(Types.FLOAT); // y
            wrapper.passthrough(Types.FLOAT); // z
            wrapper.passthrough(Types.FLOAT); // offset x
            wrapper.passthrough(Types.FLOAT); // offset y
            wrapper.passthrough(Types.FLOAT); // offset z
            wrapper.passthrough(Types.FLOAT); // speed
            wrapper.passthrough(Types.INT); // amount

            if (particle == Particle1_7_6.ICON_CRACK) {
                final int id = Integer.parseInt(parts[1]);
                int damage = 0;
                if (parts.length > 2) damage = Integer.parseInt(parts[2]);
                final DataItem item = new DataItem(id, (byte) 1, (short) damage, null);
                itemRewriter.handleItemToClient(wrapper.user(), item);
                wrapper.write(Types.VAR_INT, item.identifier()); // particle data
                if (item.data() != 0)
                    wrapper.write(Types.VAR_INT, (int) item.data()); // particle data
            } else if (particle == Particle1_7_6.BLOCK_CRACK || particle == Particle1_7_6.BLOCK_DUST) {
                final int id = Integer.parseInt(parts[1]);
                final int metadata = Integer.parseInt(parts[2]);
                final IdAndData block = new IdAndData(id, metadata);
                wrapper.user().get(ChunkTracker.class).remapBlockParticle(block);
                wrapper.write(Types.VAR_INT, block.getId() | block.getData() << 12); // particle data
            } else if (particle.extra > 0)
                throw new IllegalStateException("Tried to write particle which requires extra data, but no handler was found");
        });
        this.registerClientbound(ClientboundPackets1_7_2.GAME_EVENT, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.UNSIGNED_BYTE); // reason
                map(Types.FLOAT); // value
                handler(wrapper -> {
                    if (wrapper.get(Types.UNSIGNED_BYTE, 0) == 3) {
                        final PacketWrapper chatMessage = PacketWrapper.create(ClientboundPackets1_8.CHAT, wrapper.user());
                        chatMessage.write(Types.STRING, LEGACY_TO_JSON.transform(chatMessage, "Your game mode has been updated")); // message
                        chatMessage.write(Types.BYTE, (byte) 0); // position
                        chatMessage.send(Protocolr1_7_6_10Tor1_8.class);
                    }
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_7_2.OPEN_SCREEN, wrapper -> {
            final short windowId = wrapper.passthrough(Types.UNSIGNED_BYTE); // window id
            final short windowType = wrapper.read(Types.UNSIGNED_BYTE); // window type
            String title = wrapper.read(Types.STRING); // title
            short slots = wrapper.read(Types.UNSIGNED_BYTE); // slots
            boolean useProvidedWindowTitle = wrapper.read(Types.BOOLEAN); // use provided title

            wrapper.user().get(WindowTracker.class).types.put(windowId, windowType);

            final String inventoryName;
            switch (windowType) {
                case 0 -> inventoryName = "minecraft:chest";
                case 1 -> {
                    inventoryName = "minecraft:crafting_table";
                    title = "container.crafting";
                    useProvidedWindowTitle = false;
                }
                case 2 -> {
                    inventoryName = "minecraft:furnace";
                    if (!useProvidedWindowTitle) {
                        title = "container.furnace";
                    }
                }
                case 3 -> {
                    inventoryName = "minecraft:dispenser";
                    if (!useProvidedWindowTitle) {
                        title = "container.dispenser";
                    }
                }
                case 4 -> {
                    inventoryName = "minecraft:enchanting_table";
                    if (!useProvidedWindowTitle) {
                        title = "container.enchant";
                    }
                }
                case 5 -> {
                    inventoryName = "minecraft:brewing_stand";
                    if (!useProvidedWindowTitle) {
                        title = "container.brewing";
                    }
                }
                case 6 -> {
                    inventoryName = "minecraft:villager";
                    if (!useProvidedWindowTitle || title.isEmpty()) {
                        title = "entity.Villager.name";
                        useProvidedWindowTitle = false;
                    }
                }
                case 7 -> {
                    inventoryName = "minecraft:beacon";
                    if (!useProvidedWindowTitle) {
                        title = "container.beacon";
                    }
                }
                case 8 -> {
                    inventoryName = "minecraft:anvil";
                    title = "container.repair";
                    useProvidedWindowTitle = false;
                }
                case 9 -> {
                    inventoryName = "minecraft:hopper";
                    if (!useProvidedWindowTitle) {
                        title = "container.hopper";
                    }
                }
                case 10 -> {
                    inventoryName = "minecraft:dropper";
                    if (!useProvidedWindowTitle) {
                        title = "container.dropper";
                    }
                }
                case 11 -> inventoryName = "EntityHorse";
                default -> throw new IllegalArgumentException("Unknown window type: " + windowType);
            }

            if (windowType == 1/*crafting_table*/ || windowType == 4/*enchanting_table*/ || windowType == 8/*anvil*/) {
                slots = 0;
            }

            if (useProvidedWindowTitle) {
                title = LEGACY_TO_JSON.transform(wrapper, title);
            } else {
                title = LEGACY_TO_JSON_TRANSLATE.transform(wrapper, title);
            }

            wrapper.write(Types.STRING, inventoryName);
            wrapper.write(Types.STRING, title);
            wrapper.write(Types.UNSIGNED_BYTE, slots);
            if (windowType == 11) wrapper.passthrough(Types.INT); // entity id
        });
        this.registerClientbound(ClientboundPackets1_7_2.CONTAINER_SET_SLOT, new PacketHandlers() {
            @Override
            public void register() {
                handler(wrapper -> {
                    final byte windowId = wrapper.passthrough(Types.BYTE); // window id
                    short slot = wrapper.read(Types.SHORT); // slot
                    final short windowType = wrapper.user().get(WindowTracker.class).get(windowId);
                    if (windowType == 4/*enchanting_table*/ && slot >= 1) slot += 1;
                    wrapper.write(Types.SHORT, slot);
                });
                map(Types1_7_6.ITEM, Types.ITEM1_8); // item
                handler(wrapper -> itemRewriter.handleItemToClient(wrapper.user(), wrapper.get(Types.ITEM1_8, 0)));
            }
        });
        this.registerClientbound(ClientboundPackets1_7_2.CONTAINER_SET_CONTENT, new PacketHandlers() {
            @Override
            public void register() {
                handler(wrapper -> {
                    final short windowId = wrapper.passthrough(Types.UNSIGNED_BYTE); // window id
                    final short windowType = wrapper.user().get(WindowTracker.class).get(windowId);
                    Item[] items = wrapper.read(Types1_7_6.ITEM_ARRAY); // items
                    if (windowType == 4/*enchanting_table*/) {
                        final Item[] old = items;
                        items = new Item[old.length + 1];
                        items[0] = old[0];
                        System.arraycopy(old, 1, items, 2, old.length - 1);
                        items[1] = new DataItem(351/*lapis_lazuli*/, (byte) 3, (short) 4, null);
                    }
                    for (Item item : items) {
                        itemRewriter.handleItemToClient(wrapper.user(), item);
                    }
                    wrapper.write(Types.ITEM1_8_SHORT_ARRAY, items);
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_7_2.CONTAINER_SET_DATA, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.UNSIGNED_BYTE); // window id
                map(Types.SHORT); // progress bar id
                map(Types.SHORT); // progress bar value
                handler(wrapper -> {
                    final short windowId = wrapper.get(Types.UNSIGNED_BYTE, 0);
                    short progressBar = wrapper.get(Types.SHORT, 0);
                    final short windowType = wrapper.user().get(WindowTracker.class).get(windowId);
                    if (windowType == 2) { // furnace
                        switch (progressBar) {
                            case 0 /* cookTime */ -> {
                                progressBar = 2;
                                final PacketWrapper windowProperty = PacketWrapper.create(ClientboundPackets1_8.CONTAINER_SET_DATA, wrapper.user());
                                windowProperty.write(Types.UNSIGNED_BYTE, windowId);
                                windowProperty.write(Types.SHORT, (short) 3);
                                windowProperty.write(Types.SHORT, (short) 200);
                                windowProperty.send(Protocolr1_7_6_10Tor1_8.class);
                            }
                            case 1 /* furnaceBurnTime */ -> progressBar = 0;
                            case 2 /* currentItemBurnTime */ -> progressBar = 1;
                        }
                        wrapper.set(Types.SHORT, 0, progressBar);
                    }
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_7_2.UPDATE_SIGN, new PacketHandlers() {
            @Override
            public void register() {
                map(Types1_7_6.BLOCK_POSITION_SHORT, Types.BLOCK_POSITION1_8); // position
                map(LEGACY_TO_JSON); // line 1
                map(LEGACY_TO_JSON); // line 2
                map(LEGACY_TO_JSON); // line 3
                map(LEGACY_TO_JSON); // line 4
            }
        });
        this.registerClientbound(ClientboundPackets1_7_2.MAP_ITEM_DATA, wrapper -> {
            final int id = wrapper.passthrough(Types.VAR_INT); // map id
            final byte[] data = wrapper.read(Types.SHORT_BYTE_ARRAY); // data

            final MapStorage mapStorage = wrapper.user().get(MapStorage.class);
            MapData mapData = mapStorage.getMapData(id);
            if (mapData == null) mapStorage.putMapData(id, mapData = new MapData());

            if (data[0] == 1) {
                final int count = (data.length - 1) / 3;
                mapData.mapIcons = new MapIcon[count];

                for (int i = 0; i < count; i++) {
                    mapData.mapIcons[i] = new MapIcon((byte) (data[i * 3 + 1] >> 4), (byte) (data[i * 3 + 1] & 0xF), data[i * 3 + 2], data[i * 3 + 3]);
                }
            } else if (data[0] == 2) {
                mapData.scale = data[1];
            }

            wrapper.write(Types.BYTE, mapData.scale);
            wrapper.write(Types.VAR_INT, mapData.mapIcons.length);
            for (MapIcon mapIcon : mapData.mapIcons) {
                wrapper.write(Types.BYTE, (byte) (mapIcon.direction << 4 | mapIcon.type & 0xF));
                wrapper.write(Types.BYTE, mapIcon.x);
                wrapper.write(Types.BYTE, mapIcon.z);
            }

            if (data[0] == 0) {
                final byte x = data[1];
                final byte z = data[2];
                final int rows = data.length - 3;
                final byte[] newData = new byte[rows];
                System.arraycopy(data, 3, newData, 0, rows);

                wrapper.write(Types.BYTE, (byte) 1);
                wrapper.write(Types.BYTE, (byte) rows);
                wrapper.write(Types.BYTE, x);
                wrapper.write(Types.BYTE, z);
                wrapper.write(Types.BYTE_ARRAY_PRIMITIVE, newData);
            } else {
                wrapper.write(Types.BYTE, (byte) 0);
            }
        });
        this.registerClientbound(ClientboundPackets1_7_2.BLOCK_ENTITY_DATA, new PacketHandlers() {
            @Override
            public void register() {
                map(Types1_7_6.BLOCK_POSITION_SHORT, Types.BLOCK_POSITION1_8); // position
                map(Types.UNSIGNED_BYTE); // type
                map(Types1_7_6.NBT, Types.NAMED_COMPOUND_TAG); // data
            }
        });
        this.registerClientbound(ClientboundPackets1_7_2.OPEN_SIGN_EDITOR, new PacketHandlers() {
            @Override
            public void register() {
                map(Types1_7_6.BLOCK_POSITION_INT, Types.BLOCK_POSITION1_8); // position
            }
        });
        this.registerClientbound(ClientboundPackets1_7_2.PLAYER_INFO, wrapper -> {
            final String name = wrapper.read(Types.STRING); // name
            final boolean online = wrapper.read(Types.BOOLEAN); // online
            final short ping = wrapper.read(Types.SHORT); // ping

            final TablistStorage tablistStorage = wrapper.user().get(TablistStorage.class);
            TabListEntry entry = tablistStorage.tablist.get(name);

            if (entry == null && online) { // add entry
                tablistStorage.tablist.put(name, entry = new TabListEntry(name, ping));
                wrapper.write(Types.VAR_INT, 0); // action
                wrapper.write(Types.VAR_INT, 1); // count
                wrapper.write(Types.UUID, entry.gameProfile.id()); // uuid
                wrapper.write(Types.STRING, entry.gameProfile.name()); // name
                wrapper.write(Types.PROFILE_PROPERTY_ARRAY, entry.gameProfile.properties()); // properties
                wrapper.write(Types.VAR_INT, 0); // gamemode
                wrapper.write(Types.VAR_INT, entry.ping); // ping
                wrapper.write(Types.OPTIONAL_STRING, null); // display name
            } else if (entry != null && !online) { // remove entry
                tablistStorage.tablist.remove(name);
                wrapper.write(Types.VAR_INT, 4); // action
                wrapper.write(Types.VAR_INT, 1); // count
                wrapper.write(Types.UUID, entry.gameProfile.id()); // uuid
            } else if (entry != null) { // update ping
                entry.ping = ping;
                wrapper.write(Types.VAR_INT, 2); // action
                wrapper.write(Types.VAR_INT, 1); // count
                wrapper.write(Types.UUID, entry.gameProfile.id()); // uuid
                wrapper.write(Types.VAR_INT, entry.ping); // ping
            } else {
                wrapper.cancel();
            }
        });
        this.registerClientbound(ClientboundPackets1_7_2.SET_OBJECTIVE, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.STRING); // name
                handler(wrapper -> {
                    final String value = wrapper.read(Types.STRING); // value
                    final byte mode = wrapper.passthrough(Types.BYTE); // mode

                    if (mode == 0/*CREATE*/ || mode == 2/*UPDATE*/) {
                        wrapper.write(Types.STRING, value);
                        wrapper.write(Types.STRING, "integer");
                    }
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_7_2.SET_SCORE, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.STRING); // name
                map(Types.BYTE, Types.VAR_INT); // mode
                handler(wrapper -> {
                    final int mode = wrapper.get(Types.VAR_INT, 0);
                    if (mode == 0/*UPDATE*/) {
                        wrapper.passthrough(Types.STRING); // objective
                        wrapper.write(Types.VAR_INT, wrapper.read(Types.INT)); // score
                    } else {
                        wrapper.write(Types.STRING, "");
                    }
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_7_2.SET_PLAYER_TEAM, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.STRING); // name
                handler(wrapper -> {
                    final byte mode = wrapper.passthrough(Types.BYTE); // mode
                    if (mode == 0 || mode == 2) {
                        wrapper.passthrough(Types.STRING); // display name
                        wrapper.passthrough(Types.STRING); // prefix
                        wrapper.passthrough(Types.STRING); // suffix
                        wrapper.passthrough(Types.BYTE); // flags
                        wrapper.write(Types.STRING, "always"); // nametag visibility
                        wrapper.write(Types.BYTE, (byte) 0); // color
                    }
                    if (mode == 0 || mode == 3 || mode == 4) {
                        final int count = wrapper.read(Types.SHORT); // count
                        final String[] playerNames = new String[count];
                        for (int i = 0; i < count; i++) {
                            playerNames[i] = wrapper.read(Types.STRING); // player name
                        }
                        wrapper.write(Types.STRING_ARRAY, playerNames);
                    }
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_7_2.CUSTOM_PAYLOAD, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.STRING); // channel
                read(Types.UNSIGNED_SHORT); // length
                handlerSoftFail(wrapper -> {
                    final String channel = wrapper.get(Types.STRING, 0);
                    switch (channel) {
                        case "MC|Brand" -> wrapper.write(Types.STRING, new String(wrapper.read(Types.REMAINING_BYTES), StandardCharsets.UTF_8)); // brand
                        case "MC|TrList" -> {
                            wrapper.passthrough(Types.INT); // window id
                            final int count = wrapper.passthrough(Types.UNSIGNED_BYTE); // count
                            for (int i = 0; i < count; i++) {
                                Item item = wrapper.read(Types1_7_6.ITEM);
                                itemRewriter.handleItemToClient(wrapper.user(), item);
                                wrapper.write(Types.ITEM1_8, item); // item 1

                                item = wrapper.read(Types1_7_6.ITEM);
                                itemRewriter.handleItemToClient(wrapper.user(), item);
                                wrapper.write(Types.ITEM1_8, item); // item 3

                                final boolean has3Items = wrapper.passthrough(Types.BOOLEAN); // has 3 items
                                if (has3Items) {
                                    item = wrapper.read(Types1_7_6.ITEM);
                                    itemRewriter.handleItemToClient(wrapper.user(), item);
                                    wrapper.write(Types.ITEM1_8, item); // item 2
                                }

                                wrapper.passthrough(Types.BOOLEAN); // unavailable
                                wrapper.write(Types.INT, 0); // uses
                                wrapper.write(Types.INT, Integer.MAX_VALUE); // max uses
                            }
                        }
                        case "MC|RPack" -> {
                            final String url = new String(wrapper.read(Types.REMAINING_BYTES), StandardCharsets.UTF_8); // url
                            wrapper.clearPacket();
                            wrapper.setPacketType(ClientboundPackets1_8.RESOURCE_PACK);
                            wrapper.write(Types.STRING, url); // url
                            wrapper.write(Types.STRING, "legacy"); // hash
                        }
                    }
                });
            }
        });

        this.registerServerbound(State.LOGIN, ServerboundLoginPackets.HELLO, new PacketHandlers() {
            @Override
            public void register() {
                handler(wrapper -> {
                    final String name = wrapper.passthrough(Types.STRING); // name
                    final ProtocolInfo info = wrapper.user().getProtocolInfo();
                    // Set the information early
                    if (info.getUsername() == null) {
                        info.setUsername(name);
                    }
                    if (info.getUuid() == null) {
                        info.setUuid(ViaLegacy.getConfig().isLegacySkinLoading() ? Via.getManager().getProviders().get(GameProfileFetcher.class).getMojangUuid(name) : GameProfileUtil.getOfflinePlayerUuid(name));
                    }
                });
            }
        });
        this.registerServerbound(State.LOGIN, ServerboundLoginPackets.ENCRYPTION_KEY, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.BYTE_ARRAY_PRIMITIVE, Types.SHORT_BYTE_ARRAY); // shared secret
                map(Types.BYTE_ARRAY_PRIMITIVE, Types.SHORT_BYTE_ARRAY); // verify token
            }
        });
        this.registerServerbound(ServerboundPackets1_8.KEEP_ALIVE, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.VAR_INT, Types.INT); // key
            }
        });
        this.registerServerbound(ServerboundPackets1_8.INTERACT, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.VAR_INT, Types.INT); // entity id
                handler(wrapper -> {
                    final int mode = wrapper.read(Types.VAR_INT); // mode
                    if (mode == 2) { // interactAt
                        wrapper.write(Types.BYTE, (byte) 0); // mode
                        wrapper.read(Types.FLOAT); // offsetX
                        wrapper.read(Types.FLOAT); // offsetY
                        wrapper.read(Types.FLOAT); // offsetZ
                        final EntityTracker entityTracker = wrapper.user().get(EntityTracker.class);
                        final EntityTypes1_8.EntityType entityType = entityTracker.getTrackedEntities().get(wrapper.get(Types.INT, 0));
                        if (entityType == null || !entityType.isOrHasParent(EntityTypes1_8.EntityType.ARMOR_STAND)) {
                            wrapper.cancel();
                        }
                    } else {
                        wrapper.write(Types.BYTE, (byte) mode); // mode
                    }
                });
            }
        });
        this.registerServerbound(ServerboundPackets1_8.MOVE_PLAYER_POS, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.DOUBLE); // x
                map(Types.DOUBLE); // y
                handler(wrapper -> wrapper.write(Types.DOUBLE, wrapper.get(Types.DOUBLE, 1) + 1.62)); // stance
                map(Types.DOUBLE); // z
                map(Types.BOOLEAN); // onGround
            }
        });
        this.registerServerbound(ServerboundPackets1_8.MOVE_PLAYER_POS_ROT, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.DOUBLE); // x
                map(Types.DOUBLE); // y
                handler(wrapper -> wrapper.write(Types.DOUBLE, wrapper.get(Types.DOUBLE, 1) + 1.62)); // stance
                map(Types.DOUBLE); // z
                map(Types.FLOAT); // yaw
                map(Types.FLOAT); // pitch
                map(Types.BOOLEAN); // onGround
            }
        });
        this.registerServerbound(ServerboundPackets1_8.PLAYER_ACTION, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.VAR_INT, Types.UNSIGNED_BYTE); // status
                map(Types.BLOCK_POSITION1_8, Types1_7_6.BLOCK_POSITION_UBYTE); // position
                map(Types.UNSIGNED_BYTE); // direction
                handler(wrapper -> {
                    final short status = wrapper.get(Types.UNSIGNED_BYTE, 0);
                    if (status == 5) { // RELEASE_USE_ITEM
                        wrapper.set(Types.UNSIGNED_BYTE, 1, (short) 255);
                    }
                });
            }
        });
        this.registerServerbound(ServerboundPackets1_8.USE_ITEM_ON, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.BLOCK_POSITION1_8, Types1_7_6.BLOCK_POSITION_UBYTE); // position
                map(Types.UNSIGNED_BYTE); // direction
                map(Types.ITEM1_8, Types1_7_6.ITEM); // item
                map(Types.UNSIGNED_BYTE); // offset x
                map(Types.UNSIGNED_BYTE); // offset y
                map(Types.UNSIGNED_BYTE); // offset z
                handler(wrapper -> {
                    final short direction = wrapper.get(Types.UNSIGNED_BYTE, 0);
                    final Item item = wrapper.get(Types1_7_6.ITEM, 0);
                    itemRewriter.handleItemToServer(wrapper.user(), item);

                    if (item != null && item.identifier() == ItemList1_6.writtenBook.itemId() && direction == 255) { // If placed item is a book then cancel it and send a MC|BOpen to the client
                        final PacketWrapper openBook = PacketWrapper.create(ClientboundPackets1_8.CUSTOM_PAYLOAD, wrapper.user());
                        openBook.write(Types.STRING, "MC|BOpen"); // channel
                        openBook.send(Protocolr1_7_6_10Tor1_8.class);
                        wrapper.cancel();
                    }
                });
            }
        });
        this.registerServerbound(ServerboundPackets1_8.SWING, wrapper -> {
            final EntityTracker entityTracker = wrapper.user().get(EntityTracker.class);
            wrapper.write(Types.INT, entityTracker.getPlayerID()); // entity id
            wrapper.write(Types.BYTE, (byte) 1); // animation
        });
        this.registerServerbound(ServerboundPackets1_8.PLAYER_COMMAND, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.VAR_INT, Types.INT); // entity id
                map(Types.VAR_INT, Types.BYTE, action -> (byte) (action + 1)); // action id
                map(Types.VAR_INT, Types.INT); // action parameter
            }
        });
        this.registerServerbound(ServerboundPackets1_8.PLAYER_INPUT, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.FLOAT); // sideways
                map(Types.FLOAT); // forwards
                handler(wrapper -> {
                    final byte flags = wrapper.read(Types.BYTE); // flags
                    wrapper.write(Types.BOOLEAN, (flags & 1) > 0); // jumping
                    wrapper.write(Types.BOOLEAN, (flags & 2) > 0); // sneaking
                });
            }
        });
        this.registerServerbound(ServerboundPackets1_8.CONTAINER_CLICK, new PacketHandlers() {
            @Override
            public void register() {
                handler(wrapper -> {
                    final byte windowId = wrapper.passthrough(Types.BYTE); // window id
                    final short slot = wrapper.passthrough(Types.SHORT); // slot

                    final short windowType = wrapper.user().get(WindowTracker.class).get(windowId);
                    if (windowType == 4/*enchanting_table*/) {
                        if (slot == 1) {
                            final PacketWrapper resetHandItem = PacketWrapper.create(ClientboundPackets1_8.CONTAINER_SET_SLOT, wrapper.user());
                            resetHandItem.write(Types.BYTE, (byte) -1); // window id
                            resetHandItem.write(Types.SHORT, (short) 0); // slot
                            resetHandItem.write(Types.ITEM1_8, null); // item
                            resetHandItem.send(Protocolr1_7_6_10Tor1_8.class);
                            final PacketWrapper setLapisSlot = PacketWrapper.create(ClientboundPackets1_8.CONTAINER_SET_SLOT, wrapper.user());
                            setLapisSlot.write(Types.BYTE, windowId); // window id
                            setLapisSlot.write(Types.SHORT, slot); // slot
                            setLapisSlot.write(Types.ITEM1_8, new DataItem(351/*lapis_lazuli*/, (byte) 3, (short) 4, null)); // item
                            setLapisSlot.send(Protocolr1_7_6_10Tor1_8.class);
                            wrapper.cancel();
                        } else if (slot > 1) {
                            wrapper.set(Types.SHORT, 0, (short) (slot - 1));
                        }
                    }
                });
                map(Types.BYTE); // button
                map(Types.SHORT); // transaction id
                map(Types.BYTE); // action
                map(Types.ITEM1_8, Types1_7_6.ITEM); // item
                handler(wrapper -> itemRewriter.handleItemToServer(wrapper.user(), wrapper.get(Types1_7_6.ITEM, 0)));
            }
        });
        this.registerServerbound(ServerboundPackets1_8.SET_CREATIVE_MODE_SLOT, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.SHORT); // slot
                map(Types.ITEM1_8, Types1_7_6.ITEM); // item
                handler(wrapper -> itemRewriter.handleItemToServer(wrapper.user(), wrapper.get(Types1_7_6.ITEM, 0)));
            }
        });
        this.registerServerbound(ServerboundPackets1_8.SIGN_UPDATE, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.BLOCK_POSITION1_8, Types1_7_6.BLOCK_POSITION_SHORT); // position
                handler(wrapper -> {
                    for (int i = 0; i < 4; i++) {
                        final JsonElement component = wrapper.read(Types.COMPONENT); // line
                        String text = TextComponentSerializer.V1_8.deserialize(component).asUnformattedString();
                        if (text.length() > 15) text = text.substring(0, 15);
                        wrapper.write(Types.STRING, text);
                    }
                });
            }
        });
        this.registerServerbound(ServerboundPackets1_8.COMMAND_SUGGESTION, wrapper -> {
            final String text = wrapper.read(Types.STRING); // text
            wrapper.clearPacket(); // remove optional blockpos
            wrapper.write(Types.STRING, text);
        });
        this.registerServerbound(ServerboundPackets1_8.CLIENT_INFORMATION, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.STRING); // language
                map(Types.BYTE); // view distance
                map(Types.BYTE); // chat visibility
                map(Types.BOOLEAN); // enable colors
                create(Types.BYTE, (byte) 2); // difficulty (unused)
                map(Types.UNSIGNED_BYTE, Types.BOOLEAN, flags -> (flags & 1) == 1); // skin flags -> show cape
            }
        });
        this.registerServerbound(ServerboundPackets1_8.CUSTOM_PAYLOAD, new PacketHandlers() {
            @Override
            public void register() {
                handler(wrapper -> {
                    final String channel = wrapper.read(Types.STRING); // channel

                    if (ViaLegacy.getConfig().isIgnoreLong1_8ChannelNames() && channel.length() > 16) {
                        if (Via.getConfig().logOtherConversionWarnings()) {
                            ViaLegacy.getPlatform().getLogger().warning("Ignoring serverbound plugin channel, as it is longer than 16 characters: '" + channel + "'");
                        }
                        wrapper.cancel();
                        return;
                    }

                    switch (channel) {
                        case "MC|BEdit", "MC|BSign" -> {
                            final Item item = wrapper.read(Types.ITEM1_8); // book
                            itemRewriter.handleItemToServer(wrapper.user(), item);
                            wrapper.write(Types1_7_6.ITEM, item); // book
                        }
                        case "MC|Brand", "MC|ItemName" -> {
                            final String content = wrapper.read(Types.STRING); // client brand or item name
                            wrapper.write(Types.SERVERBOUND_CUSTOM_PAYLOAD_DATA, content.getBytes(StandardCharsets.UTF_8)); // client brand or item name
                        }
                        case "MC|AdvCdm" -> {
                            final byte type = wrapper.passthrough(Types.BYTE); // command block type (0 = Block, 1 = Minecart)
                            if (type == 0) {
                                wrapper.passthrough(Types.INT); // x
                                wrapper.passthrough(Types.INT); // y
                                wrapper.passthrough(Types.INT); // z
                            } else if (type == 1) {
                                wrapper.passthrough(Types.INT); // entity id
                            } else {
                                if (Via.getConfig().logOtherConversionWarnings()) {
                                    ViaLegacy.getPlatform().getLogger().warning("Unknown 1.8 command block type: " + type);
                                }
                                wrapper.cancel();
                                return;
                            }
                            wrapper.passthrough(Types.STRING); // command
                            wrapper.read(Types.BOOLEAN); // track output
                        }
                        case "REGISTER", "UNREGISTER" -> {
                            byte[] channels = wrapper.read(Types.SERVERBOUND_CUSTOM_PAYLOAD_DATA);

                            if (ViaLegacy.getConfig().isIgnoreLong1_8ChannelNames()) {
                                final String[] registeredChannels = new String(channels, StandardCharsets.UTF_8).split("\0");
                                final List<String> validChannels = new ArrayList<>(registeredChannels.length);
                                for (String registeredChannel : registeredChannels) {
                                    if (registeredChannel.length() > 16) {
                                        if (Via.getConfig().logOtherConversionWarnings()) {
                                            ViaLegacy.getPlatform().getLogger().warning("Ignoring serverbound plugin channel register of '" + registeredChannel + "', as it is longer than 16 characters");
                                        }
                                        continue;
                                    }
                                    validChannels.add(registeredChannel);
                                }
                                if (validChannels.isEmpty()) {
                                    wrapper.cancel();
                                    return;
                                }
                                channels = Joiner.on('\0').join(validChannels).getBytes(StandardCharsets.UTF_8);
                            }

                            wrapper.write(Types.SERVERBOUND_CUSTOM_PAYLOAD_DATA, channels); // data
                        }
                    }

                    final short length = (short) PacketUtil.calculateLength(wrapper);
                    wrapper.resetReader();
                    wrapper.write(Types.STRING, channel); // channel
                    wrapper.write(Types.SHORT, length); // length
                });
            }
        });
        this.cancelServerbound(ServerboundPackets1_8.TELEPORT_TO_ENTITY);
        this.cancelServerbound(ServerboundPackets1_8.RESOURCE_PACK);
    }

    private float randomFloatClamp(Random rnd, float min, float max) {
        return min >= max ? min : rnd.nextFloat() * (max - min) + min;
    }

    private int realignEntityY(final EntityTypes1_8.EntityType type, final int y) {
        float yPos = y / 32F;
        float yOffset = 0F;
        if (type.isOrHasParent(EntityTypes1_8.ObjectType.FALLING_BLOCK.getType()))
            yOffset = 0.98F / 2F;
        if (type.isOrHasParent(EntityTypes1_8.ObjectType.TNT_PRIMED.getType()))
            yOffset = 0.98F / 2F;
        if (type.isOrHasParent(EntityTypes1_8.ObjectType.ENDER_CRYSTAL.getType()))
            yOffset = 1F;
        else if (type.isOrHasParent(EntityTypes1_8.ObjectType.MINECART.getType()))
            yOffset = 0.7F / 2F;
        else if (type.isOrHasParent(EntityTypes1_8.ObjectType.BOAT.getType()))
            yOffset = 0.6F / 2F;
        else if (type.isOrHasParent(EntityTypes1_8.ObjectType.ITEM.getType()))
            yOffset = 0.24F / 2F; // Should be 0.25F but that causes items to fall through the ground on modern client versions
        else if (type.isOrHasParent(EntityTypes1_8.ObjectType.LEASH.getType()))
            yOffset = 0.5F;
        else if (type.isOrHasParent(EntityTypes1_8.EntityType.EXPERIENCE_ORB))
            yOffset = 0.5F / 2F;
        return (int) Math.floor((yPos - yOffset) * 32F);
    }

    @Override
    public void register(ViaProviders providers) {
        providers.require(GameProfileFetcher.class);
    }

    @Override
    public void init(UserConnection userConnection) {
        userConnection.addClientWorld(Protocolr1_7_6_10Tor1_8.class, new ClientWorld());
        userConnection.put(new TablistStorage(userConnection));
        userConnection.put(new WindowTracker());
        userConnection.put(new EntityTracker(userConnection));
        userConnection.put(new MapStorage());
        userConnection.put(new ChunkTracker());
    }

    @Override
    public ItemRewriter getItemRewriter() {
        return this.itemRewriter;
    }

    public EntityDataRewriter getEntityDataRewriter() {
        return this.entityDataRewriter;
    }

}
