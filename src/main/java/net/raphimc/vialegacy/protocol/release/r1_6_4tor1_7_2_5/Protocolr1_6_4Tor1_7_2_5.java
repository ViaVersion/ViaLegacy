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
package net.raphimc.vialegacy.protocol.release.r1_6_4tor1_7_2_5;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.ProtocolInfo;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.BlockChangeRecord;
import com.viaversion.viaversion.api.minecraft.BlockPosition;
import com.viaversion.viaversion.api.minecraft.ClientWorld;
import com.viaversion.viaversion.api.minecraft.chunks.Chunk;
import com.viaversion.viaversion.api.minecraft.entities.EntityTypes1_8;
import com.viaversion.viaversion.api.minecraft.entitydata.EntityData;
import com.viaversion.viaversion.api.minecraft.item.DataItem;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.platform.providers.ViaProviders;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.packet.State;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.api.type.Types;
import com.viaversion.viaversion.libs.fastutil.ints.Int2IntMap;
import com.viaversion.viaversion.libs.fastutil.objects.Object2IntMap;
import com.viaversion.viaversion.libs.fastutil.objects.Object2IntOpenHashMap;
import com.viaversion.viaversion.libs.gson.JsonObject;
import com.viaversion.viaversion.protocols.base.*;
import com.viaversion.viaversion.protocols.base.v1_7.ClientboundBaseProtocol1_7;
import com.viaversion.viaversion.protocols.v1_8to1_9.packet.ClientboundPackets1_8;
import com.viaversion.viaversion.util.IdAndData;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import net.raphimc.vialegacy.ViaLegacy;
import net.raphimc.vialegacy.api.protocol.StatelessTransitionProtocol;
import net.raphimc.vialegacy.api.splitter.PreNettySplitter;
import net.raphimc.vialegacy.api.util.PacketUtil;
import net.raphimc.vialegacy.protocol.release.r1_6_4tor1_7_2_5.packet.ClientboundPackets1_6_4;
import net.raphimc.vialegacy.protocol.release.r1_6_4tor1_7_2_5.packet.ServerboundPackets1_6_4;
import net.raphimc.vialegacy.protocol.release.r1_6_4tor1_7_2_5.provider.EncryptionProvider;
import net.raphimc.vialegacy.protocol.release.r1_6_4tor1_7_2_5.rewriter.ItemRewriter;
import net.raphimc.vialegacy.protocol.release.r1_6_4tor1_7_2_5.rewriter.SoundRewriter;
import net.raphimc.vialegacy.protocol.release.r1_6_4tor1_7_2_5.rewriter.StatisticRewriter;
import net.raphimc.vialegacy.protocol.release.r1_6_4tor1_7_2_5.rewriter.TextRewriter;
import net.raphimc.vialegacy.protocol.release.r1_6_4tor1_7_2_5.storage.*;
import net.raphimc.vialegacy.protocol.release.r1_6_4tor1_7_2_5.types.EntityDataTypes1_6_4;
import net.raphimc.vialegacy.protocol.release.r1_6_4tor1_7_2_5.types.Types1_6_4;
import net.raphimc.vialegacy.protocol.release.r1_7_2_5tor1_7_6_10.packet.ClientboundPackets1_7_2;
import net.raphimc.vialegacy.protocol.release.r1_7_2_5tor1_7_6_10.packet.ServerboundPackets1_7_2;
import net.raphimc.vialegacy.protocol.release.r1_7_6_10tor1_8.Protocolr1_7_6_10Tor1_8;
import net.raphimc.vialegacy.protocol.release.r1_7_6_10tor1_8.model.GameProfile;
import net.raphimc.vialegacy.protocol.release.r1_7_6_10tor1_8.provider.GameProfileFetcher;
import net.raphimc.vialegacy.protocol.release.r1_7_6_10tor1_8.types.EntityDataTypes1_7_6;
import net.raphimc.vialegacy.protocol.release.r1_7_6_10tor1_8.types.Types1_7_6;

import java.util.List;
import java.util.logging.Level;

public class Protocolr1_6_4Tor1_7_2_5 extends StatelessTransitionProtocol<ClientboundPackets1_6_4, ClientboundPackets1_7_2, ServerboundPackets1_6_4, ServerboundPackets1_7_2> {

    private final ItemRewriter itemRewriter = new ItemRewriter(this);

    public Protocolr1_6_4Tor1_7_2_5() {
        super(ClientboundPackets1_6_4.class, ClientboundPackets1_7_2.class, ServerboundPackets1_6_4.class, ServerboundPackets1_7_2.class);
    }

    @Override
    protected void registerPackets() {
        super.registerPackets();

        this.registerClientboundTransition(ClientboundPackets1_6_4.LOGIN,
                ClientboundPackets1_7_2.LOGIN, new PacketHandlers() {
                    @Override
                    public void register() {
                        map(Types.INT); // entity id
                        handler(wrapper -> {
                            wrapper.user().get(PlayerInfoStorage.class).entityId = wrapper.get(Types.INT, 0);
                            final String terrainType = wrapper.read(Types1_6_4.STRING); // level type
                            final short gameType = wrapper.read(Types.BYTE); // game mode
                            final byte dimension = wrapper.read(Types.BYTE); // dimension id
                            final short difficulty = wrapper.read(Types.BYTE); // difficulty
                            wrapper.read(Types.BYTE); // world height
                            final short maxPlayers = wrapper.read(Types.BYTE); // max players

                            wrapper.write(Types.UNSIGNED_BYTE, gameType);
                            wrapper.write(Types.BYTE, dimension);
                            wrapper.write(Types.UNSIGNED_BYTE, difficulty);
                            wrapper.write(Types.UNSIGNED_BYTE, maxPlayers);
                            wrapper.write(Types.STRING, terrainType);
                        });
                        handler(wrapper -> {
                            final byte dimensionId = wrapper.get(Types.BYTE, 0);
                            wrapper.user().getClientWorld(Protocolr1_6_4Tor1_7_2_5.class).setEnvironment(dimensionId);

                            wrapper.user().put(new ChunkTracker(wrapper.user()));
                        });
                    }
                }, State.LOGIN, (PacketHandler) wrapper -> {
                    ViaLegacy.getPlatform().getLogger().warning("Server skipped LOGIN state");
                    final PacketWrapper sharedKey = PacketWrapper.create(ClientboundPackets1_6_4.SHARED_KEY, wrapper.user());
                    sharedKey.write(Types.SHORT_BYTE_ARRAY, new byte[0]);
                    sharedKey.write(Types.SHORT_BYTE_ARRAY, new byte[0]);
                    wrapper.user().get(ProtocolMetadataStorage.class).skipEncryption = true;
                    sharedKey.send(Protocolr1_6_4Tor1_7_2_5.class, false); // switch to play state
                    wrapper.user().get(ProtocolMetadataStorage.class).skipEncryption = false;

                    wrapper.setPacketType(ClientboundPackets1_6_4.LOGIN);
                    wrapper.send(Protocolr1_6_4Tor1_7_2_5.class, false);
                    wrapper.cancel();
                }
        );
        this.registerClientbound(ClientboundPackets1_6_4.CHAT, new PacketHandlers() {
            @Override
            public void register() {
                map(Types1_6_4.STRING, Types.STRING, TextRewriter::toClient); // message
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.SET_EQUIPPED_ITEM, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // entity id
                map(Types.SHORT); // slot
                map(Types1_7_6.ITEM); // item
                handler(wrapper -> itemRewriter.handleItemToClient(wrapper.user(), wrapper.get(Types1_7_6.ITEM, 0)));
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.RESPAWN, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // dimension id
                map(Types.BYTE, Types.UNSIGNED_BYTE); // difficulty
                map(Types.BYTE, Types.UNSIGNED_BYTE); // gamemode
                read(Types.SHORT); // world height
                map(Types1_6_4.STRING, Types.STRING); // worldType
                handler(wrapper -> {
                    if (wrapper.user().getClientWorld(Protocolr1_6_4Tor1_7_2_5.class).setEnvironment(wrapper.get(Types.INT, 0))) {
                        wrapper.user().get(ChunkTracker.class).clear();
                    }
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.MOVE_PLAYER_STATUS_ONLY, ClientboundPackets1_7_2.PLAYER_POSITION, wrapper -> {
            final PlayerInfoStorage playerInfoStorage = wrapper.user().get(PlayerInfoStorage.class);
            final boolean supportsFlags = wrapper.user().getProtocolInfo().protocolVersion().newerThanOrEqualTo(ProtocolVersion.v1_8);

            wrapper.write(Types.DOUBLE, supportsFlags ? 0D : playerInfoStorage.posX); // x
            wrapper.write(Types.DOUBLE, supportsFlags ? 0D : playerInfoStorage.posY + 1.62F); // y
            wrapper.write(Types.DOUBLE, supportsFlags ? 0D : playerInfoStorage.posZ); // z
            wrapper.write(Types.FLOAT, supportsFlags ? 0F : playerInfoStorage.yaw); // yaw
            wrapper.write(Types.FLOAT, supportsFlags ? 0F : playerInfoStorage.pitch); // pitch
            if (supportsFlags) {
                wrapper.read(Types.BOOLEAN); // onGround
                wrapper.write(Types.BYTE, (byte) 0b11111); // flags

                wrapper.setPacketType(ClientboundPackets1_8.PLAYER_POSITION);
                wrapper.send(Protocolr1_7_6_10Tor1_8.class);
                wrapper.cancel();
            } else {
                wrapper.passthrough(Types.BOOLEAN); // onGround
            }

            final PacketWrapper setVelocityToZero = PacketWrapper.create(ClientboundPackets1_7_2.SET_ENTITY_MOTION, wrapper.user());
            setVelocityToZero.write(Types.INT, playerInfoStorage.entityId); // entity id
            setVelocityToZero.write(Types.SHORT, (short) 0); // velocity x
            setVelocityToZero.write(Types.SHORT, (short) 0); // velocity y
            setVelocityToZero.write(Types.SHORT, (short) 0); // velocity z

            if (!wrapper.isCancelled()) wrapper.send(Protocolr1_6_4Tor1_7_2_5.class);
            setVelocityToZero.send(Protocolr1_6_4Tor1_7_2_5.class);
            wrapper.cancel();
        });
        this.registerClientbound(ClientboundPackets1_6_4.MOVE_PLAYER_POS, ClientboundPackets1_7_2.PLAYER_POSITION, wrapper -> {
            final PlayerInfoStorage playerInfoStorage = wrapper.user().get(PlayerInfoStorage.class);
            final boolean supportsFlags = wrapper.user().getProtocolInfo().protocolVersion().newerThanOrEqualTo(ProtocolVersion.v1_8);

            wrapper.passthrough(Types.DOUBLE); // x
            wrapper.passthrough(Types.DOUBLE); // stance
            wrapper.read(Types.DOUBLE); // y
            wrapper.passthrough(Types.DOUBLE); // z
            wrapper.write(Types.FLOAT, supportsFlags ? 0F : playerInfoStorage.yaw); // yaw
            wrapper.write(Types.FLOAT, supportsFlags ? 0F : playerInfoStorage.pitch); // pitch
            if (supportsFlags) {
                wrapper.read(Types.BOOLEAN); // onGround
                wrapper.write(Types.BYTE, (byte) 0b11000); // flags

                wrapper.setPacketType(ClientboundPackets1_8.PLAYER_POSITION);
                wrapper.send(Protocolr1_7_6_10Tor1_8.class);
                wrapper.cancel();
            } else {
                wrapper.passthrough(Types.BOOLEAN); // onGround
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.MOVE_PLAYER_ROT, ClientboundPackets1_7_2.PLAYER_POSITION, wrapper -> {
            final PlayerInfoStorage playerInfoStorage = wrapper.user().get(PlayerInfoStorage.class);
            final boolean supportsFlags = wrapper.user().getProtocolInfo().protocolVersion().newerThanOrEqualTo(ProtocolVersion.v1_8);

            wrapper.write(Types.DOUBLE, supportsFlags ? 0D : playerInfoStorage.posX); // x
            wrapper.write(Types.DOUBLE, supportsFlags ? 0D : playerInfoStorage.posY + 1.62F); // y
            wrapper.write(Types.DOUBLE, supportsFlags ? 0D : playerInfoStorage.posZ); // z
            wrapper.passthrough(Types.FLOAT); // yaw
            wrapper.passthrough(Types.FLOAT); // pitch
            if (supportsFlags) {
                wrapper.read(Types.BOOLEAN); // onGround
                wrapper.write(Types.BYTE, (byte) 0b111); // flags

                wrapper.setPacketType(ClientboundPackets1_8.PLAYER_POSITION);
                wrapper.send(Protocolr1_7_6_10Tor1_8.class);
                wrapper.cancel();
            } else {
                wrapper.passthrough(Types.BOOLEAN); // onGround
            }

            final PacketWrapper setVelocityToZero = PacketWrapper.create(ClientboundPackets1_7_2.SET_ENTITY_MOTION, wrapper.user());
            setVelocityToZero.write(Types.INT, playerInfoStorage.entityId); // entity id
            setVelocityToZero.write(Types.SHORT, (short) 0); // velocity x
            setVelocityToZero.write(Types.SHORT, (short) 0); // velocity y
            setVelocityToZero.write(Types.SHORT, (short) 0); // velocity z

            if (!wrapper.isCancelled()) wrapper.send(Protocolr1_6_4Tor1_7_2_5.class);
            setVelocityToZero.send(Protocolr1_6_4Tor1_7_2_5.class);
            wrapper.cancel();
        });
        this.registerClientbound(ClientboundPackets1_6_4.PLAYER_POSITION, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.DOUBLE); // x
                map(Types.DOUBLE); // stance
                read(Types.DOUBLE); // y
                map(Types.DOUBLE); // z
                map(Types.FLOAT); // yaw
                map(Types.FLOAT); // pitch
                map(Types.BOOLEAN); // onGround
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.SET_CARRIED_ITEM, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.SHORT, Types.BYTE); // slot
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.PLAYER_SLEEP, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // entity id
                handler(wrapper -> {
                    if (wrapper.read(Types.BYTE) != 0) wrapper.cancel();
                });
                map(Types1_7_6.BLOCK_POSITION_BYTE); // position
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.ANIMATE, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT, Types.VAR_INT); // entity id
                handler(wrapper -> {
                    short animate = wrapper.read(Types.BYTE); // animation
                    if (animate == 0 || animate == 4) wrapper.cancel();
                    if (animate >= 1 && animate <= 3) {
                        animate--;
                    } else {
                        animate -= 2;
                    }
                    wrapper.write(Types.UNSIGNED_BYTE, animate);
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.ADD_PLAYER, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT, Types.VAR_INT); // entity id
                handler(wrapper -> {
                    final String name = wrapper.read(Types1_6_4.STRING); // name
                    wrapper.write(Types.STRING, (ViaLegacy.getConfig().isLegacySkinLoading() ? Via.getManager().getProviders().get(GameProfileFetcher.class).getMojangUUID(name) : new GameProfile(name).uuid).toString().replace("-", "")); // uuid
                    wrapper.write(Types.STRING, name);
                });
                map(Types.INT); // x
                map(Types.INT); // y
                map(Types.INT); // z
                map(Types.BYTE); // yaw
                map(Types.BYTE); // pitch
                handler(wrapper -> {
                    final Item currentItem = new DataItem(wrapper.read(Types.UNSIGNED_SHORT), (byte) 1, (short) 0, null); // item
                    itemRewriter.handleItemToClient(wrapper.user(), currentItem);
                    wrapper.write(Types.SHORT, (short) currentItem.identifier());
                });
                map(Types1_6_4.ENTITY_DATA_LIST, Types1_7_6.ENTITY_DATA_LIST); // entity data
                handler(wrapper -> rewriteEntityData(wrapper.user(), wrapper.get(Types1_7_6.ENTITY_DATA_LIST, 0)));
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.ADD_ENTITY, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT, Types.VAR_INT); // entity id
                map(Types.BYTE); // type id
                map(Types.INT); // x
                map(Types.INT); // y
                map(Types.INT); // z
                map(Types.BYTE); // pitch
                map(Types.BYTE); // yaw
                map(Types.INT); // data
                handler(wrapper -> {
                    int data = wrapper.get(Types.INT, 3);
                    if (EntityTypes1_8.getTypeFromId(wrapper.get(Types.BYTE, 0), true) == EntityTypes1_8.ObjectType.FALLING_BLOCK.getType()) {
                        final int id = data & 0xFFFF;
                        final int metadata = data >> 16;
                        final IdAndData block = new IdAndData(id, metadata);
                        wrapper.user().get(ChunkTracker.class).remapBlockParticle(block);
                        data = (block.getId() & 0xFFFF) | block.getData() << 16;
                    }
                    wrapper.set(Types.INT, 3, data);
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.ADD_MOB, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT, Types.VAR_INT); // entity id
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
                map(Types1_6_4.ENTITY_DATA_LIST, Types1_7_6.ENTITY_DATA_LIST); // entity data
                handler(wrapper -> rewriteEntityData(wrapper.user(), wrapper.get(Types1_7_6.ENTITY_DATA_LIST, 0)));
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.ADD_PAINTING, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT, Types.VAR_INT); // entity id
                map(Types1_6_4.STRING, Types.STRING); // motive
                map(Types1_7_6.BLOCK_POSITION_INT); // position
                map(Types.INT); // rotation
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.ADD_EXPERIENCE_ORB, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT, Types.VAR_INT); // entity id
                map(Types.INT); // x
                map(Types.INT); // y
                map(Types.INT); // z
                map(Types.SHORT); // count
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.SET_ENTITY_DATA, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // entity id
                map(Types1_6_4.ENTITY_DATA_LIST, Types1_7_6.ENTITY_DATA_LIST); // entity data
                handler(wrapper -> rewriteEntityData(wrapper.user(), wrapper.get(Types1_7_6.ENTITY_DATA_LIST, 0)));
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.UPDATE_ATTRIBUTES, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // entity id
                handler(wrapper -> {
                    final int amount = wrapper.passthrough(Types.INT); // count
                    for (int i = 0; i < amount; i++) {
                        wrapper.write(Types.STRING, wrapper.read(Types1_6_4.STRING)); // id
                        wrapper.passthrough(Types.DOUBLE); // baseValue
                        final int modifierCount = wrapper.passthrough(Types.SHORT); // modifier count
                        for (int x = 0; x < modifierCount; x++) {
                            wrapper.passthrough(Types.UUID); // modifier uuid
                            wrapper.passthrough(Types.DOUBLE); // modifier amount
                            wrapper.passthrough(Types.BYTE); // modifier operation
                        }
                    }
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.LEVEL_CHUNK, wrapper -> {
            final Chunk chunk = wrapper.passthrough(Types1_7_6.getChunk(wrapper.user().getClientWorld(Protocolr1_6_4Tor1_7_2_5.class).getEnvironment()));
            wrapper.user().get(ChunkTracker.class).trackAndRemap(chunk);
        });
        this.registerClientbound(ClientboundPackets1_6_4.CHUNK_BLOCKS_UPDATE, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // chunkX
                map(Types.INT); // chunkZ
                map(Types1_7_6.BLOCK_CHANGE_RECORD_ARRAY); // blockChangeRecords
                handler(wrapper -> {
                    final int chunkX = wrapper.get(Types.INT, 0);
                    final int chunkZ = wrapper.get(Types.INT, 1);
                    final BlockChangeRecord[] blockChangeRecords = wrapper.get(Types1_7_6.BLOCK_CHANGE_RECORD_ARRAY, 0);
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
        this.registerClientbound(ClientboundPackets1_6_4.BLOCK_UPDATE, new PacketHandlers() {
            @Override
            public void register() {
                map(Types1_7_6.BLOCK_POSITION_UBYTE); // position
                map(Types.UNSIGNED_SHORT, Types.VAR_INT); // block id
                map(Types.UNSIGNED_BYTE); // block data
                handler(wrapper -> {
                    final BlockPosition pos = wrapper.get(Types1_7_6.BLOCK_POSITION_UBYTE, 0); // position
                    final int blockId = wrapper.get(Types.VAR_INT, 0); // block id
                    final int data = wrapper.get(Types.UNSIGNED_BYTE, 0); // block data
                    final IdAndData block = new IdAndData(blockId, data);
                    wrapper.user().get(ChunkTracker.class).trackAndRemap(pos, block);
                    wrapper.set(Types.VAR_INT, 0, block.getId()); // block id
                    wrapper.set(Types.UNSIGNED_BYTE, 0, (short) block.getData()); // block data
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.BLOCK_EVENT, new PacketHandlers() {
            @Override
            public void register() {
                map(Types1_7_6.BLOCK_POSITION_SHORT); // position
                map(Types.BYTE, Types.UNSIGNED_BYTE); // type
                map(Types.BYTE, Types.UNSIGNED_BYTE); // data
                map(Types.SHORT, Types.VAR_INT); // block id
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.BLOCK_DESTRUCTION, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT, Types.VAR_INT); // entity id
                map(Types1_7_6.BLOCK_POSITION_INT); // position
                map(Types.BYTE); // progress
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.MAP_BULK_CHUNK, wrapper -> {
            final Chunk[] chunks = wrapper.passthrough(Types1_7_6.CHUNK_BULK);
            for (Chunk chunk : chunks) {
                wrapper.user().get(ChunkTracker.class).trackAndRemap(chunk);
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.EXPLODE, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.DOUBLE, Types.FLOAT); // x
                map(Types.DOUBLE, Types.FLOAT); // y
                map(Types.DOUBLE, Types.FLOAT); // z
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
        this.registerClientbound(ClientboundPackets1_6_4.CUSTOM_SOUND, new PacketHandlers() {
            @Override
            public void register() {
                handler(wrapper -> {
                    final String oldSound = wrapper.read(Types1_6_4.STRING); // sound
                    String newSound = SoundRewriter.map(oldSound);
                    if (oldSound.isEmpty()) newSound = "";
                    if (newSound == null) {
                        if (!Via.getConfig().isSuppressConversionWarnings()) {
                            ViaLegacy.getPlatform().getLogger().warning("Unable to map 1.6.4 sound '" + oldSound + "'");
                        }
                        newSound = "";
                    }
                    if (newSound.isEmpty()) {
                        wrapper.cancel();
                        return;
                    }
                    wrapper.write(Types.STRING, newSound);
                });
                map(Types.INT); // x
                map(Types.INT); // y
                map(Types.INT); // z
                map(Types.FLOAT); // volume
                map(Types.UNSIGNED_BYTE); // pitch
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.LEVEL_EVENT, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // effect id
                map(Types1_7_6.BLOCK_POSITION_UBYTE); // position
                map(Types.INT); // data
                map(Types.BOOLEAN); // server wide
                handler(wrapper -> {
                    final int effectId = wrapper.get(Types.INT, 0); // effect id
                    int data = wrapper.get(Types.INT, 1); // data
                    final boolean disableRelativeVolume = wrapper.get(Types.BOOLEAN, 0); // server wide

                    if (!disableRelativeVolume && effectId == 2001) { // block break effect
                        final ChunkTracker chunkTracker = wrapper.user().get(ChunkTracker.class);
                        final int blockID = data & 4095;
                        final int blockData = data >> 12 & 255;
                        final IdAndData block = new IdAndData(blockID, blockData);
                        chunkTracker.remapBlockParticle(block);
                        data = (block.getId() & 4095) | block.getData() << 12;

                        wrapper.set(Types.INT, 1, data);
                    }
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.LEVEL_PARTICLES, new PacketHandlers() {
            @Override
            public void register() {
                map(Types1_6_4.STRING, Types.STRING); // particle
                map(Types.FLOAT); // x
                map(Types.FLOAT); // y
                map(Types.FLOAT); // z
                map(Types.FLOAT); // offset x
                map(Types.FLOAT); // offset y
                map(Types.FLOAT); // offset z
                map(Types.FLOAT); // speed
                map(Types.INT); // amount
                handler(wrapper -> {
                    final String[] parts = wrapper.get(Types.STRING, 0).split("_", 3);
                    if (parts[0].equals("tilecrack")) {
                        parts[0] = "blockcrack";
                    }
                    if (parts[0].equals("blockcrack") || parts[0].equals("blockdust")) {
                        final int id = Integer.parseInt(parts[1]);
                        final int metadata = Integer.parseInt(parts[2]);
                        final IdAndData block = new IdAndData(id, metadata);
                        wrapper.user().get(ChunkTracker.class).remapBlockParticle(block);
                        parts[1] = String.valueOf(block.getId());
                        parts[2] = String.valueOf(block.getData());
                    }

                    wrapper.set(Types.STRING, 0, String.join("_", parts));
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.GAME_EVENT, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.BYTE, Types.UNSIGNED_BYTE); // reason
                map(Types.BYTE, Types.FLOAT); // value
                handler(wrapper -> {
                    final short gameState = wrapper.get(Types.UNSIGNED_BYTE, 0);
                    if (gameState == 1) {
                        final PacketWrapper startRain = PacketWrapper.create(ClientboundPackets1_7_2.GAME_EVENT, wrapper.user());
                        startRain.write(Types.UNSIGNED_BYTE, (short) 7);
                        startRain.write(Types.FLOAT, 1.0f);

                        wrapper.send(Protocolr1_6_4Tor1_7_2_5.class);
                        startRain.send(Protocolr1_6_4Tor1_7_2_5.class);
                        wrapper.cancel();
                    } else if (gameState == 2) {
                        final PacketWrapper stopRain = PacketWrapper.create(ClientboundPackets1_7_2.GAME_EVENT, wrapper.user());
                        stopRain.write(Types.UNSIGNED_BYTE, (short) 7);
                        stopRain.write(Types.FLOAT, 0.0f);

                        wrapper.send(Protocolr1_6_4Tor1_7_2_5.class);
                        stopRain.send(Protocolr1_6_4Tor1_7_2_5.class);
                        wrapper.cancel();
                    }
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.ADD_GLOBAL_ENTITY, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT, Types.VAR_INT); // entity id
                map(Types.BYTE); // type id
                map(Types.INT); // x
                map(Types.INT); // y
                map(Types.INT); // z
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.OPEN_SCREEN, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.UNSIGNED_BYTE); // window id
                map(Types.UNSIGNED_BYTE); // window type
                map(Types1_6_4.STRING, Types.STRING); // title
                map(Types.UNSIGNED_BYTE); // slots
                map(Types.BOOLEAN); // use provided title
                // more conditional data
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.CONTAINER_CLOSE, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.BYTE, Types.UNSIGNED_BYTE); // window id
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.CONTAINER_SET_SLOT, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.BYTE); // window id
                map(Types.SHORT); // slot
                map(Types1_7_6.ITEM); // item
                handler(wrapper -> itemRewriter.handleItemToClient(wrapper.user(), wrapper.get(Types1_7_6.ITEM, 0)));
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.CONTAINER_SET_CONTENT, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.BYTE, Types.UNSIGNED_BYTE); // window id
                handler(wrapper -> {
                    final Item[] items = wrapper.passthrough(Types1_7_6.ITEM_ARRAY); // items
                    for (Item item : items) {
                        itemRewriter.handleItemToClient(wrapper.user(), item);
                    }
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.UPDATE_SIGN, new PacketHandlers() {
            @Override
            public void register() {
                map(Types1_7_6.BLOCK_POSITION_SHORT); // position
                map(Types1_6_4.STRING, Types.STRING); // line 1
                map(Types1_6_4.STRING, Types.STRING); // line 2
                map(Types1_6_4.STRING, Types.STRING); // line 3
                map(Types1_6_4.STRING, Types.STRING); // line 4
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.MAP_ITEM_DATA, new PacketHandlers() {
            @Override
            public void register() {
                read(Types.SHORT); // item id
                map(Types.SHORT, Types.VAR_INT); // map id
                map(Types.SHORT_BYTE_ARRAY); // data
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.BLOCK_ENTITY_DATA, new PacketHandlers() {
            @Override
            public void register() {
                map(Types1_7_6.BLOCK_POSITION_SHORT); // position
                map(Types.BYTE, Types.UNSIGNED_BYTE); // type
                map(Types1_7_6.NBT); // data
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.OPEN_SIGN_EDITOR, new PacketHandlers() {
            @Override
            public void register() {
                read(Types.BYTE); // always 0
                map(Types1_7_6.BLOCK_POSITION_INT); // position
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.AWARD_STATS, wrapper -> {
            wrapper.cancel();
            final StatisticsStorage statisticsStorage = wrapper.user().get(StatisticsStorage.class);
            final int statId = wrapper.read(Types.INT); // statistic id
            final int increment = wrapper.read(Types.INT); // increment
            statisticsStorage.values.put(statId, statisticsStorage.values.get(statId) + increment);
        });
        this.registerClientbound(ClientboundPackets1_6_4.PLAYER_INFO, new PacketHandlers() {
            @Override
            public void register() {
                map(Types1_6_4.STRING, Types.STRING); // name
                map(Types.BOOLEAN); // online
                map(Types.SHORT); // ping
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.COMMAND_SUGGESTIONS, wrapper -> {
            final String completions = wrapper.read(Types1_6_4.STRING); // completions
            final String[] completionsArray = completions.split("\0");
            wrapper.write(Types.VAR_INT, completionsArray.length); // completions count
            for (String s : completionsArray) {
                wrapper.write(Types.STRING, s); // completion
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.SET_OBJECTIVE, new PacketHandlers() {
            @Override
            public void register() {
                map(Types1_6_4.STRING, Types.STRING); // name
                map(Types1_6_4.STRING, Types.STRING); // value
                map(Types.BYTE); // mode
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.SET_SCORE, wrapper -> {
            wrapper.write(Types.STRING, wrapper.read(Types1_6_4.STRING)); // name
            final byte mode = wrapper.passthrough(Types.BYTE); // mode
            if (mode == 0) {
                wrapper.write(Types.STRING, wrapper.read(Types1_6_4.STRING)); // objective
                wrapper.passthrough(Types.INT); // score
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.SET_DISPLAY_OBJECTIVE, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.BYTE); // position
                map(Types1_6_4.STRING, Types.STRING); // name
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.SET_PLAYER_TEAM, new PacketHandlers() {
            @Override
            public void register() {
                map(Types1_6_4.STRING, Types.STRING); // name
                handler(wrapper -> {
                    final byte mode = wrapper.passthrough(Types.BYTE); // mode
                    if (mode == 0 || mode == 2) {
                        wrapper.write(Types.STRING, wrapper.read(Types1_6_4.STRING)); // display name
                        wrapper.write(Types.STRING, wrapper.read(Types1_6_4.STRING)); // prefix
                        wrapper.write(Types.STRING, wrapper.read(Types1_6_4.STRING)); // suffix
                        wrapper.passthrough(Types.BYTE); // flags
                    }
                    if (mode == 0 || mode == 3 || mode == 4) {
                        final int count = wrapper.passthrough(Types.SHORT); // count
                        for (int i = 0; i < count; i++) {
                            wrapper.write(Types.STRING, wrapper.read(Types1_6_4.STRING)); // player name
                        }
                    }
                });
            }
        });
        this.registerClientboundTransition(ClientboundPackets1_6_4.CUSTOM_PAYLOAD,
                ClientboundPackets1_7_2.CUSTOM_PAYLOAD, new PacketHandlers() {
                    @Override
                    public void register() {
                        handler(wrapper -> {
                            final String channel = wrapper.read(Types1_6_4.STRING); // channel
                            int length = wrapper.read(Types.SHORT); // length

                            if (length < 0) {
                                wrapper.write(Types.STRING, channel); // channel
                                wrapper.write(Types.UNSIGNED_SHORT, 0); // length
                                return;
                            }

                            try {
                                if (channel.equals("MC|TrList")) {
                                    wrapper.passthrough(Types.INT); // window id
                                    final int count = wrapper.passthrough(Types.UNSIGNED_BYTE); // count
                                    for (int i = 0; i < count; i++) {
                                        itemRewriter.handleItemToClient(wrapper.user(), wrapper.passthrough(Types1_7_6.ITEM)); // item 1
                                        itemRewriter.handleItemToClient(wrapper.user(), wrapper.passthrough(Types1_7_6.ITEM)); // item 3
                                        if (wrapper.passthrough(Types.BOOLEAN)) { // has 3 items
                                            itemRewriter.handleItemToClient(wrapper.user(), wrapper.passthrough(Types1_7_6.ITEM)); // item 2
                                        }
                                        wrapper.passthrough(Types.BOOLEAN); // unavailable
                                    }
                                    length = PacketUtil.calculateLength(wrapper);
                                }
                            } catch (Exception e) {
                                if (!Via.getConfig().isSuppressConversionWarnings()) {
                                    Via.getPlatform().getLogger().log(Level.WARNING, "Failed to handle packet", e);
                                }
                                wrapper.cancel();
                                return;
                            }

                            wrapper.resetReader();
                            wrapper.write(Types.STRING, channel); // channel
                            wrapper.write(Types.UNSIGNED_SHORT, length); // length
                        });
                    }
                }, State.LOGIN, (PacketHandler) PacketWrapper::cancel
        );
        this.registerClientboundTransition(ClientboundPackets1_6_4.SHARED_KEY, ClientboundLoginPackets.LOGIN_FINISHED, (PacketHandler) wrapper -> {
            final ProtocolInfo info = wrapper.user().getProtocolInfo();
            final ProtocolMetadataStorage protocolMetadata = wrapper.user().get(ProtocolMetadataStorage.class);
            wrapper.read(Types.SHORT_BYTE_ARRAY); // shared secret
            wrapper.read(Types.SHORT_BYTE_ARRAY); // verify token
            wrapper.write(Types.STRING, info.getUuid().toString().replace("-", "")); // uuid
            wrapper.write(Types.STRING, info.getUsername()); // user name

            if (!protocolMetadata.skipEncryption) {
                Via.getManager().getProviders().get(EncryptionProvider.class).enableDecryption(wrapper.user());
            }
            ClientboundBaseProtocol1_7.onLoginSuccess(wrapper.user());

            final PacketWrapper respawn = PacketWrapper.create(ServerboundPackets1_6_4.CLIENT_COMMAND, wrapper.user());
            respawn.write(Types.BYTE, (byte) 0); // force respawn
            respawn.sendToServer(Protocolr1_6_4Tor1_7_2_5.class);
        });
        this.registerClientboundTransition(ClientboundPackets1_6_4.SERVER_AUTH_DATA, ClientboundLoginPackets.HELLO, new PacketHandlers() {
            @Override
            public void register() {
                map(Types1_6_4.STRING, Types.STRING); // server hash
                map(Types.SHORT_BYTE_ARRAY); // public key
                map(Types.SHORT_BYTE_ARRAY); // verify token
                handler(wrapper -> {
                    final ProtocolMetadataStorage protocolMetadata = wrapper.user().get(ProtocolMetadataStorage.class);
                    final String serverHash = wrapper.get(Types.STRING, 0);
                    protocolMetadata.authenticate = !serverHash.equals("-");
                });
            }
        });
        this.registerClientboundTransition(ClientboundPackets1_6_4.DISCONNECT,
                ClientboundStatusPackets.STATUS_RESPONSE, (PacketHandler) wrapper -> {
                    final String reason = wrapper.read(Types1_6_4.STRING); // reason
                    try {
                        final String[] motdParts = reason.split("\0");
                        final JsonObject rootObject = new JsonObject();
                        final JsonObject descriptionObject = new JsonObject();
                        final JsonObject playersObject = new JsonObject();
                        final JsonObject versionObject = new JsonObject();

                        descriptionObject.addProperty("text", motdParts[3]);
                        playersObject.addProperty("max", Integer.parseInt(motdParts[5]));
                        playersObject.addProperty("online", Integer.parseInt(motdParts[4]));
                        versionObject.addProperty("name", motdParts[2]);
                        versionObject.addProperty("protocol", Integer.parseInt(motdParts[1]));
                        rootObject.add("description", descriptionObject);
                        rootObject.add("players", playersObject);
                        rootObject.add("version", versionObject);

                        wrapper.write(Types.STRING, rootObject.toString());
                    } catch (Throwable e) {
                        ViaLegacy.getPlatform().getLogger().log(Level.WARNING, "Could not parse 1.6.4 ping: " + reason, e);
                        wrapper.cancel();
                    }
                }, ClientboundLoginPackets.LOGIN_DISCONNECT, new PacketHandlers() {
                    @Override
                    protected void register() {
                        map(Types1_6_4.STRING, Types.STRING, TextRewriter::toClientDisconnect); // reason
                    }
                }, ClientboundPackets1_7_2.DISCONNECT, new PacketHandlers() {
                    @Override
                    public void register() {
                        map(Types1_6_4.STRING, Types.STRING, TextRewriter::toClientDisconnect); // reason
                    }
                }
        );
        this.cancelClientbound(ClientboundPackets1_6_4.SET_CREATIVE_MODE_SLOT);

        this.registerServerboundTransition(ServerboundHandshakePackets.CLIENT_INTENTION, null, wrapper -> {
            wrapper.cancel();
            wrapper.read(Types.VAR_INT); // protocol version
            final String hostname = wrapper.read(Types.STRING); // hostname
            final int port = wrapper.read(Types.UNSIGNED_SHORT); // port
            wrapper.user().put(new HandshakeStorage(hostname, port));
        });
        this.registerServerboundTransition(ServerboundStatusPackets.STATUS_REQUEST, ServerboundPackets1_6_4.SERVER_PING, wrapper -> {
            final HandshakeStorage handshakeStorage = wrapper.user().get(HandshakeStorage.class);
            final String ip = handshakeStorage.getHostname();
            final int port = handshakeStorage.getPort();
            wrapper.write(Types.UNSIGNED_BYTE, (short) 1); // always 1
            wrapper.write(Types.UNSIGNED_BYTE, (short) ServerboundPackets1_6_4.CUSTOM_PAYLOAD.getId()); // packet id
            wrapper.write(Types1_6_4.STRING, "MC|PingHost"); // channel
            wrapper.write(Types.SHORT, (short) (3 + 2 * ip.length() + 4)); // length
            wrapper.write(Types.UNSIGNED_BYTE, (short) wrapper.user().getProtocolInfo().serverProtocolVersion().getVersion()); // protocol Id
            wrapper.write(Types1_6_4.STRING, ip); // hostname
            wrapper.write(Types.INT, port); // port
        });
        this.registerServerboundTransition(ServerboundStatusPackets.PING_REQUEST, null, wrapper -> {
            wrapper.cancel();
            final PacketWrapper pong = PacketWrapper.create(ClientboundStatusPackets.PONG_RESPONSE, wrapper.user());
            pong.write(Types.LONG, wrapper.read(Types.LONG)); // start time
            pong.send(Protocolr1_6_4Tor1_7_2_5.class);
        });
        this.registerServerboundTransition(ServerboundLoginPackets.HELLO, ServerboundPackets1_6_4.CLIENT_PROTOCOL, wrapper -> {
            final HandshakeStorage handshakeStorage = wrapper.user().get(HandshakeStorage.class);

            final String name = wrapper.read(Types.STRING); // user name

            wrapper.write(Types.UNSIGNED_BYTE, (short) wrapper.user().getProtocolInfo().serverProtocolVersion().getVersion()); // protocol id
            wrapper.write(Types1_6_4.STRING, name); // user name
            wrapper.write(Types1_6_4.STRING, handshakeStorage.getHostname()); // hostname
            wrapper.write(Types.INT, handshakeStorage.getPort()); // port

            final ProtocolInfo info = wrapper.user().getProtocolInfo();
            // Set the information early
            if (info.getUsername() == null) {
                info.setUsername(name);
            }
            if (info.getUuid() == null) {
                info.setUuid(ViaLegacy.getConfig().isLegacySkinLoading() ? Via.getManager().getProviders().get(GameProfileFetcher.class).getMojangUUID(name) : new GameProfile(name).uuid);
            }
        });
        this.registerServerboundTransition(ServerboundLoginPackets.ENCRYPTION_KEY, ServerboundPackets1_6_4.SHARED_KEY, null);
        this.registerServerbound(ServerboundPackets1_7_2.CHAT, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.STRING, Types1_6_4.STRING); // message
            }
        });
        this.registerServerbound(ServerboundPackets1_7_2.INTERACT, new PacketHandlers() {
            @Override
            public void register() {
                handler(wrapper -> wrapper.write(Types.INT, wrapper.user().get(PlayerInfoStorage.class).entityId)); // player id
                map(Types.INT); // entity id
                map(Types.BYTE); // mode
            }
        });
        this.registerServerbound(ServerboundPackets1_7_2.MOVE_PLAYER_STATUS_ONLY, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.BOOLEAN); // onGround
                handler(wrapper -> wrapper.user().get(PlayerInfoStorage.class).onGround = wrapper.get(Types.BOOLEAN, 0));
            }
        });
        this.registerServerbound(ServerboundPackets1_7_2.MOVE_PLAYER_POS, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.DOUBLE); // x
                map(Types.DOUBLE); // y
                map(Types.DOUBLE); // stance
                map(Types.DOUBLE); // z
                map(Types.BOOLEAN); // onGround
                handler(wrapper -> {
                    final PlayerInfoStorage playerInfoStorage = wrapper.user().get(PlayerInfoStorage.class);
                    playerInfoStorage.posX = wrapper.get(Types.DOUBLE, 0);
                    playerInfoStorage.posY = wrapper.get(Types.DOUBLE, 1);
                    playerInfoStorage.posZ = wrapper.get(Types.DOUBLE, 3);
                    playerInfoStorage.onGround = wrapper.get(Types.BOOLEAN, 0);
                });
            }
        });
        this.registerServerbound(ServerboundPackets1_7_2.MOVE_PLAYER_ROT, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.FLOAT); // yaw
                map(Types.FLOAT); // pitch
                map(Types.BOOLEAN); // onGround
                handler(wrapper -> {
                    final PlayerInfoStorage playerInfoStorage = wrapper.user().get(PlayerInfoStorage.class);
                    playerInfoStorage.yaw = wrapper.get(Types.FLOAT, 0);
                    playerInfoStorage.pitch = wrapper.get(Types.FLOAT, 1);
                    playerInfoStorage.onGround = wrapper.get(Types.BOOLEAN, 0);
                });
            }
        });
        this.registerServerbound(ServerboundPackets1_7_2.MOVE_PLAYER_POS_ROT, new PacketHandlers() {
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
                    final PlayerInfoStorage playerInfoStorage = wrapper.user().get(PlayerInfoStorage.class);
                    playerInfoStorage.posX = wrapper.get(Types.DOUBLE, 0);
                    playerInfoStorage.posY = wrapper.get(Types.DOUBLE, 1);
                    playerInfoStorage.posZ = wrapper.get(Types.DOUBLE, 3);
                    playerInfoStorage.yaw = wrapper.get(Types.FLOAT, 0);
                    playerInfoStorage.pitch = wrapper.get(Types.FLOAT, 1);
                    playerInfoStorage.onGround = wrapper.get(Types.BOOLEAN, 0);
                });
            }
        });
        this.registerServerbound(ServerboundPackets1_7_2.USE_ITEM_ON, new PacketHandlers() {
            @Override
            public void register() {
                map(Types1_7_6.BLOCK_POSITION_UBYTE); // position
                map(Types.UNSIGNED_BYTE); // direction
                map(Types1_7_6.ITEM); // item
                handler(wrapper -> itemRewriter.handleItemToServer(wrapper.user(), wrapper.get(Types1_7_6.ITEM, 0)));
                map(Types.UNSIGNED_BYTE); // offset x
                map(Types.UNSIGNED_BYTE); // offset y
                map(Types.UNSIGNED_BYTE); // offset z
            }
        });
        this.registerServerbound(ServerboundPackets1_7_2.CONTAINER_CLICK, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.BYTE); // windowId
                map(Types.SHORT); // slot
                map(Types.BYTE); // button
                map(Types.SHORT); // action
                map(Types.BYTE); // mode
                map(Types1_7_6.ITEM); // item
                handler(wrapper -> itemRewriter.handleItemToServer(wrapper.user(), wrapper.get(Types1_7_6.ITEM, 0)));
            }
        });
        this.registerServerbound(ServerboundPackets1_7_2.SIGN_UPDATE, new PacketHandlers() {
            @Override
            public void register() {
                map(Types1_7_6.BLOCK_POSITION_SHORT); // position
                map(Types.STRING, Types1_6_4.STRING); // line 1
                map(Types.STRING, Types1_6_4.STRING); // line 2
                map(Types.STRING, Types1_6_4.STRING); // line 3
                map(Types.STRING, Types1_6_4.STRING); // line 4
            }
        });
        this.registerServerbound(ServerboundPackets1_7_2.COMMAND_SUGGESTION, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.STRING, Types1_6_4.STRING); // text
            }
        });
        this.registerServerbound(ServerboundPackets1_7_2.CLIENT_INFORMATION, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.STRING, Types1_6_4.STRING); // language
                handler(wrapper -> {
                    byte renderDistance = wrapper.read(Types.BYTE); // render distance
                    if (renderDistance <= 2) {
                        renderDistance = 3; // TINY
                    } else if (renderDistance <= 4) {
                        renderDistance = 2; // SHORT
                    } else if (renderDistance <= 8) {
                        renderDistance = 1; // NORMAL
                    } else { // >= 16
                        renderDistance = 0; // FAR
                    }
                    wrapper.write(Types.BYTE, renderDistance);

                    final byte chatVisibility = wrapper.read(Types.BYTE); // chat visibility
                    final boolean enableColors = wrapper.read(Types.BOOLEAN); // enable colors
                    final byte mask = (byte) (chatVisibility | (enableColors ? 1 : 0) << 3);
                    wrapper.write(Types.BYTE, mask); // mask
                });
                map(Types.BYTE); // difficulty
                map(Types.BOOLEAN); // show cape
            }
        });
        this.registerServerbound(ServerboundPackets1_7_2.CLIENT_COMMAND, wrapper -> {
            final int action = wrapper.read(Types.VAR_INT); // action

            if (action == 1) { // Request Statistics
                final Object2IntMap<String> loadedStatistics = new Object2IntOpenHashMap<>();
                for (Int2IntMap.Entry entry : wrapper.user().get(StatisticsStorage.class).values.int2IntEntrySet()) {
                    final String key = StatisticRewriter.map(entry.getIntKey());
                    if (key == null) continue;
                    loadedStatistics.put(key, entry.getIntValue());
                }

                final PacketWrapper statistics = PacketWrapper.create(ClientboundPackets1_8.AWARD_STATS, wrapper.user());
                statistics.write(Types.VAR_INT, loadedStatistics.size()); // count
                for (Object2IntMap.Entry<String> entry : loadedStatistics.object2IntEntrySet()) {
                    statistics.write(Types.STRING, entry.getKey()); // statistic name
                    statistics.write(Types.VAR_INT, entry.getIntValue()); // statistic value
                }
                statistics.send(Protocolr1_6_4Tor1_7_2_5.class);
            }
            if (action != 0) {
                wrapper.cancel();
                return;
            }
            wrapper.write(Types.BYTE, (byte) 1); // force respawn
        });
        this.registerServerbound(ServerboundPackets1_7_2.CUSTOM_PAYLOAD, new PacketHandlers() {
            @Override
            public void register() {
                handler(wrapper -> {
                    final String channel = wrapper.read(Types.STRING); // channel
                    short length = wrapper.read(Types.SHORT); // length

                    switch (channel) {
                        case "MC|BEdit", "MC|BSign" -> {
                            itemRewriter.handleItemToServer(wrapper.user(), wrapper.passthrough(Types1_7_6.ITEM));
                            length = (short) PacketUtil.calculateLength(wrapper);
                        }
                        case "MC|AdvCdm" -> {
                            final byte type = wrapper.read(Types.BYTE); // command block type
                            if (type == 0) {
                                wrapper.passthrough(Types.INT); // x
                                wrapper.passthrough(Types.INT); // y
                                wrapper.passthrough(Types.INT); // z
                                wrapper.passthrough(Types.STRING); // command
                            } else {
                                wrapper.cancel();
                                return;
                            }
                            length = (short) PacketUtil.calculateLength(wrapper);
                        }
                    }

                    wrapper.resetReader();
                    wrapper.write(Types1_6_4.STRING, channel); // channel
                    wrapper.write(Types.SHORT, length); // length
                });
            }
        });
    }

    private void rewriteEntityData(final UserConnection user, final List<EntityData> entityDataList) {
        for (EntityData entityData : entityDataList) {
            if (entityData.dataType().equals(EntityDataTypes1_6_4.ITEM)) {
                itemRewriter.handleItemToClient(user, entityData.value());
            }
            entityData.setDataType(EntityDataTypes1_7_6.byId(entityData.dataType().typeId()));
        }
    }

    @Override
    public void register(ViaProviders providers) {
        providers.require(EncryptionProvider.class);
    }

    @Override
    public void init(UserConnection userConnection) {
        userConnection.put(new PreNettySplitter(Protocolr1_6_4Tor1_7_2_5.class, ClientboundPackets1_6_4::getPacket));
        userConnection.addClientWorld(Protocolr1_6_4Tor1_7_2_5.class, new ClientWorld());

        userConnection.put(new ProtocolMetadataStorage());
        userConnection.put(new PlayerInfoStorage());
        userConnection.put(new StatisticsStorage());
        userConnection.put(new ChunkTracker(userConnection)); // Set again in JOIN_GAME handler for version comparisons to work

        if (userConnection.getChannel() != null) {
            userConnection.getChannel().pipeline().addFirst(new ChannelOutboundHandlerAdapter() {
                @Override
                public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
                    if (ctx.channel().isWritable() && userConnection.getProtocolInfo().getClientState().equals(State.PLAY) && userConnection.get(PlayerInfoStorage.class).entityId != -1) {
                        final PacketWrapper disconnect = PacketWrapper.create(ServerboundPackets1_6_4.DISCONNECT, userConnection);
                        disconnect.write(Types1_6_4.STRING, "Quitting"); // reason
                        disconnect.sendToServer(Protocolr1_6_4Tor1_7_2_5.class);
                    }

                    super.close(ctx, promise);
                }
            });
        }
    }

    @Override
    public ItemRewriter getItemRewriter() {
        return this.itemRewriter;
    }

}
