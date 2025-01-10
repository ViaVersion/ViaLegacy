/*
 * This file is part of ViaLegacy - https://github.com/RaphiMC/ViaLegacy
 * Copyright (C) 2020-2025 RK_01/RaphiMC and contributors
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
package net.raphimc.vialegacy.protocol.release.r1_2_4_5tor1_3_1_2;

import com.google.common.collect.Lists;
import com.viaversion.nbt.tag.CompoundTag;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.ProtocolInfo;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.BlockPosition;
import com.viaversion.viaversion.api.minecraft.ClientWorld;
import com.viaversion.viaversion.api.minecraft.Environment;
import com.viaversion.viaversion.api.minecraft.chunks.BaseChunk;
import com.viaversion.viaversion.api.minecraft.chunks.Chunk;
import com.viaversion.viaversion.api.minecraft.chunks.ChunkSection;
import com.viaversion.viaversion.api.minecraft.entities.EntityTypes1_8;
import com.viaversion.viaversion.api.minecraft.entitydata.EntityData;
import com.viaversion.viaversion.api.minecraft.item.DataItem;
import com.viaversion.viaversion.api.platform.providers.ViaProviders;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.packet.State;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.api.type.Types;
import com.viaversion.viaversion.util.ChunkUtil;
import com.viaversion.viaversion.util.IdAndData;
import net.raphimc.vialegacy.ViaLegacy;
import net.raphimc.vialegacy.api.data.BlockList1_6;
import net.raphimc.vialegacy.api.model.Location;
import net.raphimc.vialegacy.api.protocol.StatelessProtocol;
import net.raphimc.vialegacy.api.splitter.PreNettySplitter;
import net.raphimc.vialegacy.protocol.release.r1_2_4_5tor1_3_1_2.data.EntityList1_2_4;
import net.raphimc.vialegacy.protocol.release.r1_2_4_5tor1_3_1_2.data.sound.Sound;
import net.raphimc.vialegacy.protocol.release.r1_2_4_5tor1_3_1_2.data.sound.SoundType;
import net.raphimc.vialegacy.protocol.release.r1_2_4_5tor1_3_1_2.model.AbstractTrackedEntity;
import net.raphimc.vialegacy.protocol.release.r1_2_4_5tor1_3_1_2.model.TrackedEntity;
import net.raphimc.vialegacy.protocol.release.r1_2_4_5tor1_3_1_2.model.TrackedLivingEntity;
import net.raphimc.vialegacy.protocol.release.r1_2_4_5tor1_3_1_2.packet.ClientboundPackets1_2_4;
import net.raphimc.vialegacy.protocol.release.r1_2_4_5tor1_3_1_2.packet.ServerboundPackets1_2_4;
import net.raphimc.vialegacy.protocol.release.r1_2_4_5tor1_3_1_2.provider.OldAuthProvider;
import net.raphimc.vialegacy.protocol.release.r1_2_4_5tor1_3_1_2.rewriter.ItemRewriter;
import net.raphimc.vialegacy.protocol.release.r1_2_4_5tor1_3_1_2.storage.ChestStateTracker;
import net.raphimc.vialegacy.protocol.release.r1_2_4_5tor1_3_1_2.storage.EntityTracker;
import net.raphimc.vialegacy.protocol.release.r1_2_4_5tor1_3_1_2.task.EntityTrackerTickTask;
import net.raphimc.vialegacy.protocol.release.r1_2_4_5tor1_3_1_2.types.Types1_2_4;
import net.raphimc.vialegacy.protocol.release.r1_3_1_2tor1_4_2.packet.ClientboundPackets1_3_1;
import net.raphimc.vialegacy.protocol.release.r1_3_1_2tor1_4_2.packet.ServerboundPackets1_3_1;
import net.raphimc.vialegacy.protocol.release.r1_3_1_2tor1_4_2.types.EntityDataTypes1_3_1;
import net.raphimc.vialegacy.protocol.release.r1_3_1_2tor1_4_2.types.Types1_3_1;
import net.raphimc.vialegacy.protocol.release.r1_5_2tor1_6_1.data.EntityDataIndex1_5_2;
import net.raphimc.vialegacy.protocol.release.r1_6_4tor1_7_2_5.storage.ChunkTracker;
import net.raphimc.vialegacy.protocol.release.r1_6_4tor1_7_2_5.storage.ProtocolMetadataStorage;
import net.raphimc.vialegacy.protocol.release.r1_6_4tor1_7_2_5.types.Types1_6_4;
import net.raphimc.vialegacy.protocol.release.r1_7_6_10tor1_8.data.EntityDataIndex1_7_6;
import net.raphimc.vialegacy.protocol.release.r1_7_6_10tor1_8.types.Types1_7_6;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;

public class Protocolr1_2_4_5Tor1_3_1_2 extends StatelessProtocol<ClientboundPackets1_2_4, ClientboundPackets1_3_1, ServerboundPackets1_2_4, ServerboundPackets1_3_1> {

    private final ItemRewriter itemRewriter = new ItemRewriter(this);

    public Protocolr1_2_4_5Tor1_3_1_2() {
        super(ClientboundPackets1_2_4.class, ClientboundPackets1_3_1.class, ServerboundPackets1_2_4.class, ServerboundPackets1_3_1.class);
    }

    @Override
    protected void registerPackets() {
        super.registerPackets();

        this.registerClientbound(ClientboundPackets1_2_4.HANDSHAKE, ClientboundPackets1_3_1.SHARED_KEY, wrapper -> {
            final String serverHash = wrapper.read(Types1_6_4.STRING); // server hash
            if (!serverHash.trim().isEmpty() && !serverHash.equalsIgnoreCase("-")) {
                try {
                    Via.getManager().getProviders().get(OldAuthProvider.class).sendAuthRequest(wrapper.user(), serverHash);
                } catch (Throwable e) {
                    ViaLegacy.getPlatform().getLogger().log(Level.WARNING, "Could not authenticate with mojang for joinserver request!", e);
                    wrapper.cancel();
                    final PacketWrapper kick = PacketWrapper.create(ClientboundPackets1_3_1.DISCONNECT, wrapper.user());
                    kick.write(Types1_6_4.STRING, "Failed to log in: Invalid session (Try restarting your game and the launcher)"); // reason
                    kick.send(Protocolr1_2_4_5Tor1_3_1_2.class);
                    return;
                }
            }

            final ProtocolInfo info = wrapper.user().getProtocolInfo();
            final PacketWrapper login = PacketWrapper.create(ServerboundPackets1_2_4.LOGIN, wrapper.user());
            login.write(Types.INT, info.serverProtocolVersion().getVersion()); // protocol id
            login.write(Types1_6_4.STRING, info.getUsername()); // username
            login.write(Types1_6_4.STRING, ""); // level type
            login.write(Types.INT, 0); // game mode
            login.write(Types.INT, 0); // dimension id
            login.write(Types.BYTE, (byte) 0); // difficulty
            login.write(Types.BYTE, (byte) 0); // world height
            login.write(Types.BYTE, (byte) 0); // max players
            login.sendToServer(Protocolr1_2_4_5Tor1_3_1_2.class);

            final State currentState = wrapper.user().getProtocolInfo().getServerState();
            if (currentState != State.LOGIN) { // Very hacky but some servers expect the client to send back a Packet1Login
                wrapper.cancel();
            } else {
                wrapper.write(Types.SHORT_BYTE_ARRAY, new byte[0]);
                wrapper.write(Types.SHORT_BYTE_ARRAY, new byte[0]);
                wrapper.user().get(ProtocolMetadataStorage.class).skipEncryption = true;
            }
        });
        this.registerClientbound(ClientboundPackets1_2_4.LOGIN, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // entity id
                read(Types1_6_4.STRING); // username
                map(Types1_6_4.STRING); // level type
                map(Types.INT, Types.BYTE); // game mode
                map(Types.INT, Types.BYTE); // dimension id
                map(Types.BYTE); // difficulty
                map(Types.BYTE); // world height
                map(Types.BYTE); // max players
                handler(wrapper -> {
                    wrapper.user().getClientWorld(Protocolr1_2_4_5Tor1_3_1_2.class).setEnvironment(wrapper.get(Types.BYTE, 1));
                    final EntityTracker entityTracker = wrapper.user().get(EntityTracker.class);
                    entityTracker.setPlayerID(wrapper.get(Types.INT, 0));
                    entityTracker.getTrackedEntities().put(entityTracker.getPlayerID(), new TrackedLivingEntity(entityTracker.getPlayerID(), new Location(8, 64, 8), EntityTypes1_8.EntityType.PLAYER));
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_2_4.SET_EQUIPPED_ITEM, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // entity id
                map(Types.SHORT); // slot
                handler(wrapper -> {
                    final int itemId = wrapper.read(Types.SHORT); // item id
                    final short itemDamage = wrapper.read(Types.SHORT); // item damage
                    wrapper.write(Types1_7_6.ITEM, itemId < 0 ? null : new DataItem(itemId, (byte) 1, itemDamage, null));
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_2_4.RESPAWN, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // dimension id
                map(Types.BYTE); // difficulty
                map(Types.BYTE); // game mode
                map(Types.SHORT); // world height
                map(Types1_6_4.STRING); // level type
                handler(wrapper -> {
                    if (wrapper.user().getClientWorld(Protocolr1_2_4_5Tor1_3_1_2.class).setEnvironment(wrapper.get(Types.INT, 0))) {
                        wrapper.user().get(ChestStateTracker.class).clear();
                        final EntityTracker entityTracker = wrapper.user().get(EntityTracker.class);
                        entityTracker.getTrackedEntities().clear();
                        entityTracker.getTrackedEntities().put(entityTracker.getPlayerID(), new TrackedLivingEntity(entityTracker.getPlayerID(), new Location(8, 64, 8), EntityTypes1_8.EntityType.PLAYER));
                    }
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_2_4.ADD_PLAYER, new PacketHandlers() {
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
                handler(wrapper -> wrapper.write(Types1_3_1.ENTITY_DATA_LIST, Lists.newArrayList(new EntityData(0, EntityDataTypes1_3_1.BYTE, (byte) 0)))); // entity data
                handler(wrapper -> {
                    final int entityId = wrapper.get(Types.INT, 0);
                    final double x = wrapper.get(Types.INT, 1) / 32.0D;
                    final double y = wrapper.get(Types.INT, 2) / 32.0D;
                    final double z = wrapper.get(Types.INT, 3) / 32.0D;
                    final EntityTracker tracker = wrapper.user().get(EntityTracker.class);
                    tracker.getTrackedEntities().put(entityId, new TrackedLivingEntity(entityId, new Location(x, y, z), EntityTypes1_8.EntityType.PLAYER));
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_2_4.SPAWN_ITEM, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // entity id
                map(Types1_3_1.NBTLESS_ITEM); // item
                map(Types.INT); // x
                map(Types.INT); // y
                map(Types.INT); // z
                map(Types.BYTE); // velocity x
                map(Types.BYTE); // velocity y
                map(Types.BYTE); // velocity z
                handler(wrapper -> {
                    final EntityTracker tracker = wrapper.user().get(EntityTracker.class);
                    final int entityId = wrapper.get(Types.INT, 0);
                    final double x = wrapper.get(Types.INT, 1) / 32.0D;
                    final double y = wrapper.get(Types.INT, 2) / 32.0D;
                    final double z = wrapper.get(Types.INT, 3) / 32.0D;
                    tracker.getTrackedEntities().put(entityId, new TrackedEntity(entityId, new Location(x, y, z), EntityTypes1_8.ObjectType.ITEM.getType()));
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_2_4.TAKE_ITEM_ENTITY, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // collected entity id
                map(Types.INT); // collector entity id
                handler(wrapper -> wrapper.user().get(EntityTracker.class).getTrackedEntities().remove(wrapper.get(Types.INT, 0)));
            }
        });
        this.registerClientbound(ClientboundPackets1_2_4.ADD_ENTITY, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // entity id
                map(Types.BYTE); // type id
                map(Types.INT); // x
                map(Types.INT); // y
                map(Types.INT); // z
                map(Types.INT); // data
                handler(wrapper -> {
                    final EntityTracker entityTracker = wrapper.user().get(EntityTracker.class);
                    final int entityId = wrapper.get(Types.INT, 0);
                    final byte typeId = wrapper.get(Types.BYTE, 0);
                    final int data = wrapper.get(Types.INT, 1);
                    final EntityTypes1_8.EntityType type;
                    if (typeId == 70 || typeId == 71 || typeId == 74) {
                        type = EntityTypes1_8.ObjectType.FALLING_BLOCK.getType();
                        wrapper.set(Types.BYTE, 0, (byte) EntityTypes1_8.ObjectType.FALLING_BLOCK.getId());
                    } else if (typeId == 10 || typeId == 11 || typeId == 12) {
                        type = EntityTypes1_8.ObjectType.MINECART.getType();
                    } else {
                        type = EntityTypes1_8.ObjectType.getEntityType(typeId, data);
                    }
                    final double x = wrapper.get(Types.INT, 1) / 32.0D;
                    final double y = wrapper.get(Types.INT, 2) / 32.0D;
                    final double z = wrapper.get(Types.INT, 3) / 32.0D;
                    final Location location = new Location(x, y, z);
                    int throwerEntityId = wrapper.get(Types.INT, 4);
                    short speedX = 0;
                    short speedY = 0;
                    short speedZ = 0;
                    if (throwerEntityId > 0) {
                        speedX = wrapper.read(Types.SHORT); // velocity x
                        speedY = wrapper.read(Types.SHORT); // velocity y
                        speedZ = wrapper.read(Types.SHORT); // velocity z
                    }
                    if (typeId == 70) throwerEntityId = 12; // sand
                    if (typeId == 71) throwerEntityId = 13; // gravel
                    if (typeId == 74) throwerEntityId = 122; // dragon egg
                    if (typeId == EntityTypes1_8.ObjectType.FISHIHNG_HOOK.getId()) {
                        final Optional<AbstractTrackedEntity> nearestEntity = entityTracker.getNearestEntity(location, 2.0D, e -> e.getEntityType().isOrHasParent(EntityTypes1_8.EntityType.PLAYER));
                        throwerEntityId = nearestEntity.map(AbstractTrackedEntity::getEntityId).orElseGet(entityTracker::getPlayerID);
                    }
                    wrapper.set(Types.INT, 4, throwerEntityId);
                    if (throwerEntityId > 0) {
                        wrapper.write(Types.SHORT, speedX);
                        wrapper.write(Types.SHORT, speedY);
                        wrapper.write(Types.SHORT, speedZ);
                    }

                    if (type == null) {
                        return;
                    }

                    entityTracker.getTrackedEntities().put(entityId, new TrackedEntity(entityId, location, type));

                    float pitch;
                    switch (type) {
                        case TNT -> entityTracker.playSoundAt(location, Sound.RANDOM_FUSE, 1.0F, 1.0F);
                        case ARROW -> {
                            pitch = 1.0F / (entityTracker.RND.nextFloat() * 0.4F + 1.2F) + 0.5F;
                            entityTracker.playSoundAt(location, Sound.RANDOM_BOW, 1.0F, pitch);
                        }
                        case SNOWBALL, EGG, ENDER_PEARL, EYE_OF_ENDER, POTION, EXPERIENCE_BOTTLE, FISHING_HOOK -> {
                            pitch = 0.4F / (entityTracker.RND.nextFloat() * 0.4F + 0.8F);
                            entityTracker.playSoundAt(location, Sound.RANDOM_BOW, 0.5F, pitch);
                        }
                    }
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_2_4.ADD_MOB, new PacketHandlers() {
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
                create(Types.SHORT, (short) 0); // velocity x
                create(Types.SHORT, (short) 0); // velocity y
                create(Types.SHORT, (short) 0); // velocity z
                map(Types1_3_1.ENTITY_DATA_LIST); // entity data
                handler(wrapper -> {
                    final int entityId = wrapper.get(Types.INT, 0);
                    final short type = wrapper.get(Types.UNSIGNED_BYTE, 0);
                    final double x = wrapper.get(Types.INT, 1) / 32.0D;
                    final double y = wrapper.get(Types.INT, 2) / 32.0D;
                    final double z = wrapper.get(Types.INT, 3) / 32.0D;
                    final List<EntityData> entityDataList = wrapper.get(Types1_3_1.ENTITY_DATA_LIST, 0);
                    final EntityTypes1_8.EntityType entityType = EntityTypes1_8.EntityType.findById(type);
                    if (entityType == null) {
                        wrapper.cancel();
                        return;
                    }
                    final EntityTracker tracker = wrapper.user().get(EntityTracker.class);
                    tracker.getTrackedEntities().put(entityId, new TrackedLivingEntity(entityId, new Location(x, y, z), entityType));
                    tracker.updateEntityDataList(entityId, entityDataList);
                    handleEntityDataList(entityId, entityDataList, wrapper);
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_2_4.REMOVE_ENTITIES, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT, Types1_7_6.INT_ARRAY, i -> new int[]{i});
                handler(wrapper -> {
                    final EntityTracker tracker = wrapper.user().get(EntityTracker.class);
                    for (int entityId : wrapper.get(Types1_7_6.INT_ARRAY, 0)) {
                        tracker.getTrackedEntities().remove(entityId);
                    }
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_2_4.MOVE_ENTITY_POS, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // entity id
                map(Types.BYTE); // x
                map(Types.BYTE); // y
                map(Types.BYTE); // z
                handler(wrapper -> {
                    final EntityTracker tracker = wrapper.user().get(EntityTracker.class);
                    final int entityId = wrapper.get(Types.INT, 0);
                    final byte x = wrapper.get(Types.BYTE, 0);
                    final byte y = wrapper.get(Types.BYTE, 1);
                    final byte z = wrapper.get(Types.BYTE, 2);
                    tracker.updateEntityLocation(entityId, x, y, z, true);
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_2_4.MOVE_ENTITY_POS_ROT, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // entity id
                map(Types.BYTE); // x
                map(Types.BYTE); // y
                map(Types.BYTE); // z
                map(Types.BYTE); // yaw
                map(Types.BYTE); // pitch
                handler(wrapper -> {
                    final EntityTracker tracker = wrapper.user().get(EntityTracker.class);
                    final int entityId = wrapper.get(Types.INT, 0);
                    final byte x = wrapper.get(Types.BYTE, 0);
                    final byte y = wrapper.get(Types.BYTE, 1);
                    final byte z = wrapper.get(Types.BYTE, 2);
                    tracker.updateEntityLocation(entityId, x, y, z, true);
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_2_4.TELEPORT_ENTITY, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // entity id
                map(Types.INT); // x
                map(Types.INT); // y
                map(Types.INT); // z
                map(Types.BYTE); // yaw
                map(Types.BYTE); // pitch
                handler(wrapper -> {
                    final EntityTracker tracker = wrapper.user().get(EntityTracker.class);
                    final int entityId = wrapper.get(Types.INT, 0);
                    final int x = wrapper.get(Types.INT, 1);
                    final int y = wrapper.get(Types.INT, 2);
                    final int z = wrapper.get(Types.INT, 3);
                    tracker.updateEntityLocation(entityId, x, y, z, false);
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_2_4.ENTITY_EVENT, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // entity id
                map(Types.BYTE); // status
                handler(wrapper -> {
                    final EntityTracker entityTracker = wrapper.user().get(EntityTracker.class);
                    final int entityId = wrapper.get(Types.INT, 0);
                    final byte status = wrapper.get(Types.BYTE, 0);

                    if (status == 2) { // hurt
                        entityTracker.playSound(entityId, SoundType.HURT);
                    } else if (status == 3) { // death
                        entityTracker.playSound(entityId, SoundType.DEATH);
                    }
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_2_4.SET_ENTITY_DATA, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // entity id
                map(Types1_3_1.ENTITY_DATA_LIST); // entity data
                handler(wrapper -> {
                    final int entityId = wrapper.get(Types.INT, 0);
                    final List<EntityData> entityDataList = wrapper.get(Types1_3_1.ENTITY_DATA_LIST, 0);

                    final EntityTracker entityTracker = wrapper.user().get(EntityTracker.class);
                    if (entityTracker.getTrackedEntities().containsKey(entityId)) {
                        entityTracker.updateEntityDataList(entityId, entityDataList);
                        handleEntityDataList(entityId, entityDataList, wrapper);
                    } else {
                        wrapper.cancel();
                    }
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_2_4.PRE_CHUNK, ClientboundPackets1_3_1.LEVEL_CHUNK, wrapper -> {
            final int chunkX = wrapper.read(Types.INT); // x
            final int chunkZ = wrapper.read(Types.INT); // z
            final boolean load = wrapper.read(Types.BOOLEAN); // mode

            wrapper.user().get(ChestStateTracker.class).unload(chunkX, chunkZ);

            final Chunk chunk;
            if (load) {
                chunk = ChunkUtil.createEmptyChunk(chunkX, chunkZ);
            } else {
                chunk = new BaseChunk(chunkX, chunkZ, true, false, 0, new ChunkSection[16], null, new ArrayList<>());
            }
            wrapper.write(Types1_7_6.getChunk(wrapper.user().getClientWorld(Protocolr1_2_4_5Tor1_3_1_2.class).getEnvironment()), chunk);
        });
        this.registerClientbound(ClientboundPackets1_2_4.LEVEL_CHUNK, wrapper -> {
            final Environment dimension = wrapper.user().getClientWorld(Protocolr1_2_4_5Tor1_3_1_2.class).getEnvironment();
            Chunk chunk = wrapper.read(Types1_2_4.CHUNK);

            wrapper.user().get(ChestStateTracker.class).unload(chunk.getX(), chunk.getZ());

            if (chunk.isFullChunk() && chunk.getBitmask() == 0) { // Remap to empty chunk
                if (!Via.getConfig().isSuppressConversionWarnings()) {
                    ViaLegacy.getPlatform().getLogger().warning("Received empty 1.2.5 chunk packet");
                }
                chunk = ChunkUtil.createEmptyChunk(chunk.getX(), chunk.getZ());
                if (dimension == Environment.NORMAL) {
                    ChunkUtil.setDummySkylight(chunk, true);
                }
            }

            if (dimension != Environment.NORMAL) {
                for (ChunkSection section : chunk.getSections()) {
                    if (section != null) {
                        section.getLight().setSkyLight(null);
                    }
                }
            }
            wrapper.write(Types1_7_6.getChunk(dimension), chunk);
        });
        this.registerClientbound(ClientboundPackets1_2_4.BLOCK_UPDATE, new PacketHandlers() {
            @Override
            public void register() {
                map(Types1_7_6.BLOCK_POSITION_UBYTE); // position
                map(Types.UNSIGNED_BYTE, Types.UNSIGNED_SHORT); // block id
                map(Types.UNSIGNED_BYTE); // block data
            }
        });
        this.registerClientbound(ClientboundPackets1_2_4.BLOCK_EVENT, new PacketHandlers() {
            @Override
            public void register() {
                map(Types1_7_6.BLOCK_POSITION_SHORT); // position
                map(Types.BYTE); // type
                map(Types.BYTE); // data
                handler(wrapper -> {
                    final IdAndData block = wrapper.user().get(ChunkTracker.class).getBlockNotNull(wrapper.get(Types1_7_6.BLOCK_POSITION_SHORT, 0));
                    wrapper.write(Types.SHORT, (short) block.getId()); // block id

                    final EntityTracker entityTracker = wrapper.user().get(EntityTracker.class);
                    final BlockPosition pos = wrapper.get(Types1_7_6.BLOCK_POSITION_SHORT, 0);
                    final byte type = wrapper.get(Types.BYTE, 0);
                    final short data = wrapper.get(Types.BYTE, 1);
                    final short blockId = wrapper.get(Types.SHORT, 0);
                    if (blockId <= 0) return;

                    float volume = 1F;
                    float pitch = 1F;
                    Sound sound = null;

                    if (block.getId() == BlockList1_6.music.blockId()) {
                        sound = switch (type) {
                            default -> Sound.NOTE_HARP;
                            case 1 -> Sound.NOTE_CLICK;
                            case 2 -> Sound.NOTE_SNARE;
                            case 3 -> Sound.NOTE_HAT;
                            case 4 -> Sound.NOTE_BASS_ATTACK;
                        };
                        volume = 3F;
                        pitch = (float) Math.pow(2D, (double) (data - 12) / 12D);
                    } else if (block.getId() == BlockList1_6.chest.blockId()) {
                        if (type == 1) {
                            final ChestStateTracker chestStateTracker = wrapper.user().get(ChestStateTracker.class);
                            if (chestStateTracker.isChestOpen(pos) && data <= 0) {
                                sound = Sound.CHEST_CLOSE;
                                chestStateTracker.closeChest(pos);
                            } else if (!chestStateTracker.isChestOpen(pos) && data > 0) {
                                sound = Sound.CHEST_OPEN;
                                chestStateTracker.openChest(pos);
                            }
                            volume = 0.5F;
                            pitch = entityTracker.RND.nextFloat() * 0.1F + 0.9F;
                        }
                    } else if (block.getId() == BlockList1_6.pistonBase.blockId() || block.getId() == BlockList1_6.pistonStickyBase.blockId()) {
                        if (type == 0) {
                            sound = Sound.PISTON_OUT;
                            volume = 0.5F;
                            pitch = entityTracker.RND.nextFloat() * 0.25F + 0.6F;
                        } else if (type == 1) {
                            sound = Sound.PISTON_IN;
                            volume = 0.5F;
                            pitch = entityTracker.RND.nextFloat() * 0.15F + 0.6F;
                        }
                    }

                    if (sound != null) {
                        entityTracker.playSoundAt(new Location(pos), sound, volume, pitch);
                    }
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_2_4.EXPLODE, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.DOUBLE); // x
                map(Types.DOUBLE); // y
                map(Types.DOUBLE); // z
                map(Types.FLOAT); // radius
                map(Types.INT); // record count
                handler(wrapper -> {
                    final int count = wrapper.get(Types.INT, 0);
                    for (int i = 0; i < count * 3; i++) wrapper.passthrough(Types.BYTE);
                });
                create(Types.FLOAT, 0F); // velocity x
                create(Types.FLOAT, 0F); // velocity y
                create(Types.FLOAT, 0F); // velocity z
                handler(wrapper -> {
                    final EntityTracker entityTracker = wrapper.user().get(EntityTracker.class);
                    final Location loc = new Location(wrapper.get(Types.DOUBLE, 0), wrapper.get(Types.DOUBLE, 1), wrapper.get(Types.DOUBLE, 2));
                    entityTracker.playSoundAt(loc, Sound.RANDOM_EXPLODE, 4F, (1.0F + (entityTracker.RND.nextFloat() - entityTracker.RND.nextFloat()) * 0.2F) * 0.7F);
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_2_4.CONTAINER_SET_SLOT, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.BYTE); // window id
                map(Types.SHORT); // slot
                map(Types1_2_4.NBT_ITEM, Types1_7_6.ITEM); // item
            }
        });
        this.registerClientbound(ClientboundPackets1_2_4.CONTAINER_SET_CONTENT, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.BYTE); // window id
                map(Types1_2_4.NBT_ITEM_ARRAY, Types1_7_6.ITEM_ARRAY); // items
            }
        });
        this.registerClientbound(ClientboundPackets1_2_4.BLOCK_ENTITY_DATA, new PacketHandlers() {
            @Override
            public void register() {
                map(Types1_7_6.BLOCK_POSITION_SHORT); // position
                map(Types.BYTE); // type
                handler(wrapper -> {
                    final int entityId = wrapper.read(Types.INT); // entity id
                    wrapper.read(Types.INT); // unused
                    wrapper.read(Types.INT); // unused
                    if (wrapper.get(Types.BYTE, 0) != 1) { // spawner
                        wrapper.cancel();
                        return;
                    }
                    final BlockPosition pos = wrapper.get(Types1_7_6.BLOCK_POSITION_SHORT, 0);

                    final CompoundTag tag = new CompoundTag();
                    tag.putString("EntityId", EntityList1_2_4.getEntityName(entityId));
                    tag.putShort("Delay", (short) 20);
                    tag.putInt("x", pos.x());
                    tag.putInt("y", pos.y());
                    tag.putInt("z", pos.z());
                    wrapper.write(Types1_7_6.NBT, tag);
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_2_4.PLAYER_ABILITIES, wrapper -> {
            final boolean disableDamage = wrapper.read(Types.BOOLEAN); // invulnerable
            final boolean flying = wrapper.read(Types.BOOLEAN); // flying
            final boolean allowFlying = wrapper.read(Types.BOOLEAN); // allow flying
            final boolean creativeMode = wrapper.read(Types.BOOLEAN); // creative mode

            byte mask = 0;
            if (disableDamage) mask |= 1;
            if (flying) mask |= 2;
            if (allowFlying) mask |= 4;
            if (creativeMode) mask |= 8;

            wrapper.write(Types.BYTE, mask); // flags
            wrapper.write(Types.BYTE, (byte) (0.05f * 255)); // fly speed
            wrapper.write(Types.BYTE, (byte) (0.1f * 255)); // walk speed
        });

        this.registerServerbound(ServerboundPackets1_3_1.CLIENT_PROTOCOL, ServerboundPackets1_2_4.HANDSHAKE, wrapper -> {
            wrapper.read(Types.UNSIGNED_BYTE); // protocol id
            final String userName = wrapper.read(Types1_6_4.STRING); // user name
            final String hostname = wrapper.read(Types1_6_4.STRING); // hostname
            final int port = wrapper.read(Types.INT); // port
            wrapper.write(Types1_6_4.STRING, userName + ";" + hostname + ":" + port); // info
        });
        this.cancelServerbound(ServerboundPackets1_3_1.SHARED_KEY);
        this.registerServerbound(ServerboundPackets1_3_1.MOVE_PLAYER_POS, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.DOUBLE); // x
                map(Types.DOUBLE); // y
                map(Types.DOUBLE); // stance
                map(Types.DOUBLE); // z
                map(Types.BOOLEAN); // onGround
                handler(wrapper -> {
                    final EntityTracker entityTracker = wrapper.user().get(EntityTracker.class);
                    final AbstractTrackedEntity player = entityTracker.getTrackedEntities().get(entityTracker.getPlayerID());
                    if (wrapper.get(Types.DOUBLE, 1) == -999D && wrapper.get(Types.DOUBLE, 2) == -999D) {
                        player.setRiding(true);
                    } else {
                        player.setRiding(false);
                        player.getLocation().setX(wrapper.get(Types.DOUBLE, 0));
                        player.getLocation().setY(wrapper.get(Types.DOUBLE, 1));
                        player.getLocation().setZ(wrapper.get(Types.DOUBLE, 3));
                    }
                });
            }
        });
        this.registerServerbound(ServerboundPackets1_3_1.MOVE_PLAYER_POS_ROT, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.DOUBLE); // x
                map(Types.DOUBLE); // y
                map(Types.DOUBLE); // stance
                map(Types.DOUBLE); // z
                map(Types.FLOAT); // yaw
                map(Types.FLOAT); // pitch
                map(Types.BOOLEAN); // onGround
                handler(wrapper -> {
                    final EntityTracker entityTracker = wrapper.user().get(EntityTracker.class);
                    final AbstractTrackedEntity player = entityTracker.getTrackedEntities().get(entityTracker.getPlayerID());
                    if (wrapper.get(Types.DOUBLE, 1) == -999D && wrapper.get(Types.DOUBLE, 2) == -999D) {
                        player.setRiding(true);
                    } else {
                        player.setRiding(false);
                        player.getLocation().setX(wrapper.get(Types.DOUBLE, 0));
                        player.getLocation().setY(wrapper.get(Types.DOUBLE, 1));
                        player.getLocation().setZ(wrapper.get(Types.DOUBLE, 3));
                    }
                });
            }
        });
        this.registerServerbound(ServerboundPackets1_3_1.USE_ITEM_ON, new PacketHandlers() {
            @Override
            public void register() {
                map(Types1_7_6.BLOCK_POSITION_UBYTE); // position
                map(Types.UNSIGNED_BYTE); // direction
                map(Types1_7_6.ITEM, Types1_2_4.NBT_ITEM); // item
                read(Types.UNSIGNED_BYTE); // offset x
                read(Types.UNSIGNED_BYTE); // offset y
                read(Types.UNSIGNED_BYTE); // offset z
            }
        });
        this.registerServerbound(ServerboundPackets1_3_1.CONTAINER_CLICK, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.BYTE); // window id
                map(Types.SHORT); // slot
                map(Types.BYTE); // button
                map(Types.SHORT); // action
                map(Types.BYTE); // mode
                map(Types1_7_6.ITEM, Types1_2_4.NBT_ITEM); // item
            }
        });
        this.registerServerbound(ServerboundPackets1_3_1.PLAYER_ABILITIES, wrapper -> {
            final byte mask = wrapper.read(Types.BYTE); // flags
            wrapper.read(Types.BYTE); // fly speed
            wrapper.read(Types.BYTE); // walk speed

            final boolean disableDamage = ((mask & 1) > 0);
            final boolean flying = ((mask & 2) > 0);
            final boolean allowFlying = ((mask & 4) > 0);
            final boolean creativeMode = ((mask & 8) > 0);

            wrapper.write(Types.BOOLEAN, disableDamage); // invulnerable
            wrapper.write(Types.BOOLEAN, flying); // flying
            wrapper.write(Types.BOOLEAN, allowFlying); // allow flying
            wrapper.write(Types.BOOLEAN, creativeMode); // creative mode
        });
        this.registerServerbound(ServerboundPackets1_3_1.CLIENT_COMMAND, ServerboundPackets1_2_4.RESPAWN, wrapper -> {
            final byte action = wrapper.read(Types.BYTE); // force respawn
            if (action != 1) {
                wrapper.cancel();
            }
            wrapper.write(Types.INT, 0); // dimension id
            wrapper.write(Types.BYTE, (byte) 0); // difficulty
            wrapper.write(Types.BYTE, (byte) 0); // game mode
            wrapper.write(Types.SHORT, (short) 0); // world height
            wrapper.write(Types1_6_4.STRING, ""); // level type
        });
        this.cancelServerbound(ServerboundPackets1_3_1.COMMAND_SUGGESTION);
        this.cancelServerbound(ServerboundPackets1_3_1.CLIENT_INFORMATION);
    }

    private void handleEntityDataList(final int entityId, final List<EntityData> entityDataList, final PacketWrapper wrapper) {
        final EntityTracker tracker = wrapper.user().get(EntityTracker.class);
        if (entityId == tracker.getPlayerID()) return;

        final AbstractTrackedEntity entity = tracker.getTrackedEntities().get(entityId);
        for (EntityData entityData : entityDataList) {
            if (EntityDataIndex1_5_2.searchIndex(entity.getEntityType(), entityData.id()) != null) continue;
            final EntityDataIndex1_7_6 index = EntityDataIndex1_7_6.searchIndex(entity.getEntityType(), entityData.id());

            if (index == EntityDataIndex1_7_6.ENTITY_FLAGS) {
                if ((entityData.<Byte>value() & 4) != 0) { // entity mount
                    final Optional<AbstractTrackedEntity> oNearbyEntity = tracker.getNearestEntity(entity.getLocation(), 1.0D, e -> e.getEntityType().isOrHasParent(EntityTypes1_8.EntityType.MINECART) || e.getEntityType().isOrHasParent(EntityTypes1_8.EntityType.PIG) || e.getEntityType().isOrHasParent(EntityTypes1_8.EntityType.BOAT));

                    if (oNearbyEntity.isPresent()) {
                        entity.setRiding(true);
                        final AbstractTrackedEntity nearbyEntity = oNearbyEntity.get();

                        final PacketWrapper attachEntity = PacketWrapper.create(ClientboundPackets1_3_1.SET_ENTITY_LINK, wrapper.user());
                        attachEntity.write(Types.INT, entityId); // riding entity id
                        attachEntity.write(Types.INT, nearbyEntity.getEntityId()); // vehicle entity id

                        wrapper.send(Protocolr1_2_4_5Tor1_3_1_2.class);
                        attachEntity.send(Protocolr1_2_4_5Tor1_3_1_2.class);
                        wrapper.cancel();
                    }
                } else if ((entityData.<Byte>value() & 4) == 0 && entity.isRiding()) { // entity unmount
                    entity.setRiding(false);

                    final PacketWrapper detachEntity = PacketWrapper.create(ClientboundPackets1_3_1.SET_ENTITY_LINK, wrapper.user());
                    detachEntity.write(Types.INT, entityId); // riding entity id
                    detachEntity.write(Types.INT, -1); // vehicle entity id

                    detachEntity.send(Protocolr1_2_4_5Tor1_3_1_2.class);
                    wrapper.send(Protocolr1_2_4_5Tor1_3_1_2.class);
                    wrapper.cancel();
                }
                break;
            } else if (index == EntityDataIndex1_7_6.CREEPER_STATE) {
                if (entityData.<Byte>value() > 0) {
                    tracker.playSoundAt(entity.getLocation(), Sound.RANDOM_FUSE, 1.0F, 0.5F);
                }
            }
        }
    }

    @Override
    public void register(ViaProviders providers) {
        providers.register(OldAuthProvider.class, new OldAuthProvider());

        if (ViaLegacy.getConfig().isSoundEmulation()) Via.getPlatform().runRepeatingSync(new EntityTrackerTickTask(), 1L);
    }

    @Override
    public void init(UserConnection userConnection) {
        userConnection.put(new PreNettySplitter(Protocolr1_2_4_5Tor1_3_1_2.class, ClientboundPackets1_2_4::getPacket));
        userConnection.addClientWorld(Protocolr1_2_4_5Tor1_3_1_2.class, new ClientWorld());

        userConnection.put(new ChestStateTracker());
        userConnection.put(new EntityTracker(userConnection));
    }

    @Override
    public ItemRewriter getItemRewriter() {
        return this.itemRewriter;
    }

}
