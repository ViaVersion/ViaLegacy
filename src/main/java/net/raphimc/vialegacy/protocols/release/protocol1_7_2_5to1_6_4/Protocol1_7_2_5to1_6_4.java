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
package net.raphimc.vialegacy.protocols.release.protocol1_7_2_5to1_6_4;

import com.google.common.base.Joiner;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.ProtocolInfo;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.BlockChangeRecord;
import com.viaversion.viaversion.api.minecraft.Position;
import com.viaversion.viaversion.api.minecraft.chunks.Chunk;
import com.viaversion.viaversion.api.minecraft.entities.EntityTypes1_10;
import com.viaversion.viaversion.api.minecraft.item.DataItem;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import com.viaversion.viaversion.api.platform.providers.ViaProviders;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.packet.State;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.libs.fastutil.ints.Int2IntMap;
import com.viaversion.viaversion.libs.fastutil.objects.Object2IntMap;
import com.viaversion.viaversion.libs.fastutil.objects.Object2IntOpenHashMap;
import com.viaversion.viaversion.libs.gson.JsonObject;
import com.viaversion.viaversion.protocols.base.ClientboundLoginPackets;
import com.viaversion.viaversion.protocols.base.ClientboundStatusPackets;
import com.viaversion.viaversion.protocols.base.ServerboundLoginPackets;
import com.viaversion.viaversion.protocols.base.ServerboundStatusPackets;
import com.viaversion.viaversion.protocols.protocol1_8.ClientboundPackets1_8;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import net.raphimc.vialegacy.ViaLegacy;
import net.raphimc.vialegacy.api.LegacyProtocolVersion;
import net.raphimc.vialegacy.api.model.IdAndData;
import net.raphimc.vialegacy.api.protocol.StatelessTransitionProtocol;
import net.raphimc.vialegacy.api.remapper.LegacyItemRewriter;
import net.raphimc.vialegacy.api.splitter.PreNettySplitter;
import net.raphimc.vialegacy.protocols.release.protocol1_7_2_5to1_6_4.providers.EncryptionProvider;
import net.raphimc.vialegacy.protocols.release.protocol1_7_2_5to1_6_4.rewriter.*;
import net.raphimc.vialegacy.protocols.release.protocol1_7_2_5to1_6_4.storage.*;
import net.raphimc.vialegacy.protocols.release.protocol1_7_2_5to1_6_4.types.MetaType1_6_4;
import net.raphimc.vialegacy.protocols.release.protocol1_7_2_5to1_6_4.types.Types1_6_4;
import net.raphimc.vialegacy.protocols.release.protocol1_7_6_10to1_7_2_5.ClientboundPackets1_7_2;
import net.raphimc.vialegacy.protocols.release.protocol1_7_6_10to1_7_2_5.ServerboundPackets1_7_2;
import net.raphimc.vialegacy.protocols.release.protocol1_8to1_7_6_10.Protocol1_8to1_7_6_10;
import net.raphimc.vialegacy.protocols.release.protocol1_8to1_7_6_10.model.GameProfile;
import net.raphimc.vialegacy.protocols.release.protocol1_8to1_7_6_10.providers.GameProfileFetcher;
import net.raphimc.vialegacy.protocols.release.protocol1_8to1_7_6_10.types.MetaType1_7_6;
import net.raphimc.vialegacy.protocols.release.protocol1_8to1_7_6_10.types.Types1_7_6;

import java.util.List;
import java.util.logging.Level;

public class Protocol1_7_2_5to1_6_4 extends StatelessTransitionProtocol<ClientboundPackets1_6_4, ClientboundPackets1_7_2, ServerboundPackets1_6_4, ServerboundPackets1_7_2> {

    private final LegacyItemRewriter<Protocol1_7_2_5to1_6_4> itemRewriter = new ItemRewriter(this);

    public Protocol1_7_2_5to1_6_4() {
        super(ClientboundPackets1_6_4.class, ClientboundPackets1_7_2.class, ServerboundPackets1_6_4.class, ServerboundPackets1_7_2.class);
    }

    @Override
    protected void registerPackets() {
        this.itemRewriter.register();

        this.registerClientboundTransition(ClientboundPackets1_6_4.JOIN_GAME,
                ClientboundPackets1_7_2.JOIN_GAME, new PacketHandlers() {
                    @Override
                    public void register() {
                        map(Type.INT); // entity id
                        handler(wrapper -> {
                            wrapper.user().get(PlayerInfoStorage.class).entityId = wrapper.get(Type.INT, 0);
                            final String terrainType = wrapper.read(Types1_6_4.STRING); // level type
                            final short gameType = wrapper.read(Type.BYTE); // game mode
                            final byte dimension = wrapper.read(Type.BYTE); // dimension id
                            final short difficulty = wrapper.read(Type.BYTE); // difficulty
                            wrapper.read(Type.BYTE); // world height
                            final short maxPlayers = wrapper.read(Type.BYTE); // max players

                            wrapper.write(Type.UNSIGNED_BYTE, gameType);
                            wrapper.write(Type.BYTE, dimension);
                            wrapper.write(Type.UNSIGNED_BYTE, difficulty);
                            wrapper.write(Type.UNSIGNED_BYTE, maxPlayers);
                            wrapper.write(Type.STRING, terrainType);
                        });
                        handler(wrapper -> {
                            final byte dimensionId = wrapper.get(Type.BYTE, 0);
                            wrapper.user().get(DimensionTracker.class).changeDimension(dimensionId);

                            wrapper.user().put(new ChunkTracker(wrapper.user()));
                        });
                    }
                }, State.LOGIN, new PacketHandlers() {
                    @Override
                    protected void register() {
                        handler(wrapper -> {
                            ViaLegacy.getPlatform().getLogger().warning("Server skipped LOGIN state");
                            final PacketWrapper sharedKey = PacketWrapper.create(ClientboundPackets1_6_4.SHARED_KEY, wrapper.user());
                            sharedKey.write(Type.SHORT_BYTE_ARRAY, new byte[0]);
                            sharedKey.write(Type.SHORT_BYTE_ARRAY, new byte[0]);
                            wrapper.user().get(ProtocolMetadataStorage.class).skipEncryption = true;
                            sharedKey.send(Protocol1_7_2_5to1_6_4.class, false); // switch to play state
                            wrapper.user().get(ProtocolMetadataStorage.class).skipEncryption = false;

                            wrapper.setPacketType(ClientboundPackets1_6_4.JOIN_GAME);
                            wrapper.send(Protocol1_7_2_5to1_6_4.class, false);
                            wrapper.cancel();
                        });
                    }
                }
        );
        this.registerClientbound(ClientboundPackets1_6_4.CHAT_MESSAGE, new PacketHandlers() {
            @Override
            public void register() {
                map(Types1_6_4.STRING, Type.STRING, msg -> TranslationRewriter.toClient(ChatComponentRewriter.toClient(msg))); // message
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.ENTITY_EQUIPMENT, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.INT); // entity id
                map(Type.SHORT); // slot
                map(Types1_7_6.ITEM); // item
                handler(wrapper -> itemRewriter.handleItemToClient(wrapper.get(Types1_7_6.ITEM, 0)));
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.RESPAWN, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.INT); // dimension id
                map(Type.BYTE, Type.UNSIGNED_BYTE); // difficulty
                map(Type.BYTE, Type.UNSIGNED_BYTE); // gamemode
                read(Type.SHORT); // world height
                map(Types1_6_4.STRING, Type.STRING); // worldType
                handler(wrapper -> {
                    if (wrapper.user().get(DimensionTracker.class).changeDimension(wrapper.get(Type.INT, 0))) {
                        wrapper.user().get(ChunkTracker.class).clear();
                    }
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.PLAYER_POSITION_ONLY_ONGROUND, ClientboundPackets1_7_2.PLAYER_POSITION, new PacketHandlers() {
            @Override
            public void register() {
                handler(wrapper -> {
                    final PlayerInfoStorage playerInfoStorage = wrapper.user().get(PlayerInfoStorage.class);
                    final boolean supportsFlags = wrapper.user().getProtocolInfo().getPipeline().contains(Protocol1_8to1_7_6_10.class);

                    wrapper.write(Type.DOUBLE, supportsFlags ? 0D : playerInfoStorage.posX); // x
                    wrapper.write(Type.DOUBLE, supportsFlags ? 0D : playerInfoStorage.posY + 1.62F); // y
                    wrapper.write(Type.DOUBLE, supportsFlags ? 0D : playerInfoStorage.posZ); // z
                    wrapper.write(Type.FLOAT, supportsFlags ? 0F : playerInfoStorage.yaw); // yaw
                    wrapper.write(Type.FLOAT, supportsFlags ? 0F : playerInfoStorage.pitch); // pitch
                    if (supportsFlags) {
                        wrapper.read(Type.BOOLEAN); // onGround
                        wrapper.write(Type.BYTE, (byte) 0b11111); // flags

                        wrapper.setPacketType(ClientboundPackets1_8.PLAYER_POSITION);
                        wrapper.send(Protocol1_8to1_7_6_10.class);
                        wrapper.cancel();
                    } else {
                        wrapper.passthrough(Type.BOOLEAN); // onGround
                    }

                    final PacketWrapper setVelocityToZero = PacketWrapper.create(ClientboundPackets1_7_2.ENTITY_VELOCITY, wrapper.user());
                    setVelocityToZero.write(Type.INT, playerInfoStorage.entityId); // entity id
                    setVelocityToZero.write(Type.SHORT, (short) 0); // velocity x
                    setVelocityToZero.write(Type.SHORT, (short) 0); // velocity y
                    setVelocityToZero.write(Type.SHORT, (short) 0); // velocity z

                    if (!wrapper.isCancelled()) wrapper.send(Protocol1_7_2_5to1_6_4.class);
                    setVelocityToZero.send(Protocol1_7_2_5to1_6_4.class);
                    wrapper.cancel();
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.PLAYER_POSITION_ONLY_POSITION, ClientboundPackets1_7_2.PLAYER_POSITION, new PacketHandlers() {
            @Override
            public void register() {
                handler(wrapper -> {
                    final PlayerInfoStorage playerInfoStorage = wrapper.user().get(PlayerInfoStorage.class);
                    final boolean supportsFlags = wrapper.user().getProtocolInfo().getPipeline().contains(Protocol1_8to1_7_6_10.class);

                    wrapper.passthrough(Type.DOUBLE); // x
                    wrapper.passthrough(Type.DOUBLE); // stance
                    wrapper.read(Type.DOUBLE); // y
                    wrapper.passthrough(Type.DOUBLE); // z
                    wrapper.write(Type.FLOAT, supportsFlags ? 0F : playerInfoStorage.yaw); // yaw
                    wrapper.write(Type.FLOAT, supportsFlags ? 0F : playerInfoStorage.pitch); // pitch
                    if (supportsFlags) {
                        wrapper.read(Type.BOOLEAN); // onGround
                        wrapper.write(Type.BYTE, (byte) 0b11000); // flags

                        wrapper.setPacketType(ClientboundPackets1_8.PLAYER_POSITION);
                        wrapper.send(Protocol1_8to1_7_6_10.class);
                        wrapper.cancel();
                    } else {
                        wrapper.passthrough(Type.BOOLEAN); // onGround
                    }
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.PLAYER_POSITION_ONLY_LOOK, ClientboundPackets1_7_2.PLAYER_POSITION, new PacketHandlers() {
            @Override
            public void register() {
                handler(wrapper -> {
                    final PlayerInfoStorage playerInfoStorage = wrapper.user().get(PlayerInfoStorage.class);
                    final boolean supportsFlags = wrapper.user().getProtocolInfo().getPipeline().contains(Protocol1_8to1_7_6_10.class);

                    wrapper.write(Type.DOUBLE, supportsFlags ? 0D : playerInfoStorage.posX); // x
                    wrapper.write(Type.DOUBLE, supportsFlags ? 0D : playerInfoStorage.posY + 1.62F); // y
                    wrapper.write(Type.DOUBLE, supportsFlags ? 0D : playerInfoStorage.posZ); // z
                    wrapper.passthrough(Type.FLOAT); // yaw
                    wrapper.passthrough(Type.FLOAT); // pitch
                    if (supportsFlags) {
                        wrapper.read(Type.BOOLEAN); // onGround
                        wrapper.write(Type.BYTE, (byte) 0b111); // flags

                        wrapper.setPacketType(ClientboundPackets1_8.PLAYER_POSITION);
                        wrapper.send(Protocol1_8to1_7_6_10.class);
                        wrapper.cancel();
                    } else {
                        wrapper.passthrough(Type.BOOLEAN); // onGround
                    }

                    final PacketWrapper setVelocityToZero = PacketWrapper.create(ClientboundPackets1_7_2.ENTITY_VELOCITY, wrapper.user());
                    setVelocityToZero.write(Type.INT, playerInfoStorage.entityId); // entity id
                    setVelocityToZero.write(Type.SHORT, (short) 0); // velocity x
                    setVelocityToZero.write(Type.SHORT, (short) 0); // velocity y
                    setVelocityToZero.write(Type.SHORT, (short) 0); // velocity z

                    if (!wrapper.isCancelled()) wrapper.send(Protocol1_7_2_5to1_6_4.class);
                    setVelocityToZero.send(Protocol1_7_2_5to1_6_4.class);
                    wrapper.cancel();
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.PLAYER_POSITION, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.DOUBLE); // x
                map(Type.DOUBLE); // stance
                read(Type.DOUBLE); // y
                map(Type.DOUBLE); // z
                map(Type.FLOAT); // yaw
                map(Type.FLOAT); // pitch
                map(Type.BOOLEAN); // onGround
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.HELD_ITEM_CHANGE, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.SHORT, Type.BYTE); // slot
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.USE_BED, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.INT); // entity id
                handler(wrapper -> {
                    if (wrapper.read(Type.BYTE) != 0) wrapper.cancel();
                });
                map(Types1_7_6.POSITION_BYTE); // position
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.ENTITY_ANIMATION, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.INT, Type.VAR_INT); // entity id
                handler(wrapper -> {
                    short animate = wrapper.read(Type.BYTE); // animation
                    if (animate == 0 || animate == 4) wrapper.cancel();
                    if (animate >= 1 && animate <= 3) {
                        animate--;
                    } else {
                        animate -= 2;
                    }
                    wrapper.write(Type.UNSIGNED_BYTE, animate);
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.SPAWN_PLAYER, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.INT, Type.VAR_INT); // entity id
                handler(wrapper -> {
                    final String name = wrapper.read(Types1_6_4.STRING); // name
                    wrapper.write(Type.STRING, (ViaLegacy.getConfig().isLegacySkinLoading() ? Via.getManager().getProviders().get(GameProfileFetcher.class).getMojangUUID(name) : new GameProfile(name).uuid).toString().replace("-", "")); // uuid
                    wrapper.write(Type.STRING, name);
                });
                map(Type.INT); // x
                map(Type.INT); // y
                map(Type.INT); // z
                map(Type.BYTE); // yaw
                map(Type.BYTE); // pitch
                handler(wrapper -> {
                    final Item currentItem = new DataItem(wrapper.read(Type.UNSIGNED_SHORT), (byte) 1, (short) 0, null); // item
                    itemRewriter.handleItemToClient(currentItem);
                    wrapper.write(Type.SHORT, (short) currentItem.identifier());
                });
                map(Types1_6_4.METADATA_LIST, Types1_7_6.METADATA_LIST); // metadata
                handler(wrapper -> rewriteMetadata(wrapper.get(Types1_7_6.METADATA_LIST, 0)));
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.SPAWN_ENTITY, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.INT, Type.VAR_INT); // entity id
                map(Type.BYTE); // type id
                map(Type.INT); // x
                map(Type.INT); // y
                map(Type.INT); // z
                map(Type.BYTE); // pitch
                map(Type.BYTE); // yaw
                map(Type.INT); // data
                handler(wrapper -> {
                    int data = wrapper.get(Type.INT, 3);
                    if (EntityTypes1_10.getTypeFromId(wrapper.get(Type.BYTE, 0), true) == EntityTypes1_10.ObjectType.FALLING_BLOCK.getType()) {
                        final int id = data & 0xFFFF;
                        final int metadata = data >> 16;
                        final IdAndData block = new IdAndData(id, metadata);
                        wrapper.user().get(ChunkTracker.class).remapBlockParticle(block);
                        data = (block.id & 0xFFFF) | block.data << 16;
                    }
                    wrapper.set(Type.INT, 3, data);
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.SPAWN_MOB, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.INT, Type.VAR_INT); // entity id
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
                map(Types1_6_4.METADATA_LIST, Types1_7_6.METADATA_LIST); // metadata
                handler(wrapper -> rewriteMetadata(wrapper.get(Types1_7_6.METADATA_LIST, 0)));
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.SPAWN_PAINTING, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.INT, Type.VAR_INT); // entity id
                map(Types1_6_4.STRING, Type.STRING); // motive
                map(Types1_7_6.POSITION_INT); // position
                map(Type.INT); // rotation
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.SPAWN_EXPERIENCE_ORB, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.INT, Type.VAR_INT); // entity id
                map(Type.INT); // x
                map(Type.INT); // y
                map(Type.INT); // z
                map(Type.SHORT); // count
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.ENTITY_METADATA, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.INT); // entity id
                map(Types1_6_4.METADATA_LIST, Types1_7_6.METADATA_LIST); // metadata
                handler(wrapper -> rewriteMetadata(wrapper.get(Types1_7_6.METADATA_LIST, 0)));
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.ENTITY_PROPERTIES, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.INT); // entity id
                handler(wrapper -> {
                    final int amount = wrapper.passthrough(Type.INT); // count
                    for (int i = 0; i < amount; i++) {
                        wrapper.write(Type.STRING, wrapper.read(Types1_6_4.STRING)); // id
                        wrapper.passthrough(Type.DOUBLE); // baseValue
                        final int modifierCount = wrapper.passthrough(Type.SHORT); // modifier count
                        for (int x = 0; x < modifierCount; x++) {
                            wrapper.passthrough(Type.UUID); // modifier uuid
                            wrapper.passthrough(Type.DOUBLE); // modifier amount
                            wrapper.passthrough(Type.BYTE); // modifier operation
                        }
                    }
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.CHUNK_DATA, new PacketHandlers() {
            @Override
            public void register() {
                handler(wrapper -> {
                    final Chunk chunk = wrapper.passthrough(Types1_7_6.getChunk(wrapper.user().get(DimensionTracker.class).getDimension()));
                    wrapper.user().get(ChunkTracker.class).trackAndRemap(chunk);
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.MULTI_BLOCK_CHANGE, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.INT); // chunkX
                map(Type.INT); // chunkZ
                map(Types1_7_6.BLOCK_CHANGE_RECORD_ARRAY); // blockChangeRecords
                handler(wrapper -> {
                    final int chunkX = wrapper.get(Type.INT, 0);
                    final int chunkZ = wrapper.get(Type.INT, 1);
                    final BlockChangeRecord[] blockChangeRecords = wrapper.get(Types1_7_6.BLOCK_CHANGE_RECORD_ARRAY, 0);
                    for (BlockChangeRecord record : blockChangeRecords) {
                        final int targetX = record.getSectionX() + (chunkX << 4);
                        final int targetY = record.getY(-1);
                        final int targetZ = record.getSectionZ() + (chunkZ << 4);
                        final IdAndData block = IdAndData.fromCompressedData(record.getBlockId());
                        final Position pos = new Position(targetX, targetY, targetZ);
                        wrapper.user().get(ChunkTracker.class).trackAndRemap(pos, block);
                        record.setBlockId(block.toCompressedData());
                    }
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.BLOCK_CHANGE, new PacketHandlers() {
            @Override
            public void register() {
                map(Types1_7_6.POSITION_UBYTE); // position
                map(Type.UNSIGNED_SHORT, Type.VAR_INT); // block id
                map(Type.UNSIGNED_BYTE); // block data
                handler(wrapper -> {
                    final Position pos = wrapper.get(Types1_7_6.POSITION_UBYTE, 0); // position
                    final int blockId = wrapper.get(Type.VAR_INT, 0); // block id
                    final int data = wrapper.get(Type.UNSIGNED_BYTE, 0); // block data
                    final IdAndData block = new IdAndData(blockId, data);
                    wrapper.user().get(ChunkTracker.class).trackAndRemap(pos, block);
                    wrapper.set(Type.VAR_INT, 0, block.id); // block id
                    wrapper.set(Type.UNSIGNED_BYTE, 0, (short) block.data); // block data
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.BLOCK_ACTION, new PacketHandlers() {
            @Override
            public void register() {
                map(Types1_7_6.POSITION_SHORT); // position
                map(Type.BYTE, Type.UNSIGNED_BYTE); // type
                map(Type.BYTE, Type.UNSIGNED_BYTE); // data
                map(Type.SHORT, Type.VAR_INT); // block id
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.BLOCK_BREAK_ANIMATION, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.INT, Type.VAR_INT); // entity id
                map(Types1_7_6.POSITION_INT); // position
                map(Type.BYTE); // progress
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.MAP_BULK_CHUNK, new PacketHandlers() {
            @Override
            public void register() {
                handler(wrapper -> {
                    final Chunk[] chunks = wrapper.passthrough(Types1_7_6.CHUNK_BULK);
                    for (Chunk chunk : chunks) {
                        wrapper.user().get(ChunkTracker.class).trackAndRemap(chunk);
                    }
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.EXPLOSION, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.DOUBLE, Type.FLOAT); // x
                map(Type.DOUBLE, Type.FLOAT); // y
                map(Type.DOUBLE, Type.FLOAT); // z
                map(Type.FLOAT); // radius
                map(Type.INT); // record count
                handler(wrapper -> {
                    final int x = wrapper.get(Type.FLOAT, 0).intValue();
                    final int y = wrapper.get(Type.FLOAT, 1).intValue();
                    final int z = wrapper.get(Type.FLOAT, 2).intValue();
                    final int recordCount = wrapper.get(Type.INT, 0);
                    final ChunkTracker chunkTracker = wrapper.user().get(ChunkTracker.class);
                    for (int i = 0; i < recordCount; i++) {
                        final Position pos = new Position(x + wrapper.passthrough(Type.BYTE), y + wrapper.passthrough(Type.BYTE), z + wrapper.passthrough(Type.BYTE));
                        chunkTracker.trackAndRemap(pos, new IdAndData(0, 0));
                    }
                });
                map(Type.FLOAT); // velocity x
                map(Type.FLOAT); // velocity y
                map(Type.FLOAT); // velocity z
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.NAMED_SOUND, new PacketHandlers() {
            @Override
            public void register() {
                handler(wrapper -> {
                    final String oldSound = wrapper.read(Types1_6_4.STRING); // sound
                    String newSound = SoundRewriter.map(oldSound);
                    if (oldSound.isEmpty()) newSound = "";
                    if (newSound == null) {
                        ViaLegacy.getPlatform().getLogger().warning("Unable to map 1.6.4 sound '" + oldSound + "'");
                        newSound = "";
                    }
                    if (newSound.isEmpty()) {
                        wrapper.cancel();
                        return;
                    }
                    wrapper.write(Type.STRING, newSound);
                });
                map(Type.INT); // x
                map(Type.INT); // y
                map(Type.INT); // z
                map(Type.FLOAT); // volume
                map(Type.UNSIGNED_BYTE); // pitch
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.EFFECT, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.INT); // effect id
                map(Types1_7_6.POSITION_UBYTE); // position
                map(Type.INT); // data
                map(Type.BOOLEAN); // server wide
                handler(wrapper -> {
                    final int effectId = wrapper.get(Type.INT, 0); // effect id
                    int data = wrapper.get(Type.INT, 1); // data
                    final boolean disableRelativeVolume = wrapper.get(Type.BOOLEAN, 0); // server wide

                    if (!disableRelativeVolume && effectId == 2001) { // block break effect
                        final ChunkTracker chunkTracker = wrapper.user().get(ChunkTracker.class);
                        final int blockID = data & 4095;
                        final int blockData = data >> 12 & 255;
                        final IdAndData block = new IdAndData(blockID, blockData);
                        chunkTracker.remapBlockParticle(block);
                        data = (block.id & 4095) | block.data << 12;

                        wrapper.set(Type.INT, 1, data);
                    }
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.SPAWN_PARTICLE, new PacketHandlers() {
            @Override
            public void register() {
                map(Types1_6_4.STRING, Type.STRING); // particle
                map(Type.FLOAT); // x
                map(Type.FLOAT); // y
                map(Type.FLOAT); // z
                map(Type.FLOAT); // offset x
                map(Type.FLOAT); // offset y
                map(Type.FLOAT); // offset z
                map(Type.FLOAT); // speed
                map(Type.INT); // amount
                handler(wrapper -> {
                    final String[] parts = wrapper.get(Type.STRING, 0).split("_", 3);
                    if (parts[0].equals("tilecrack")) {
                        parts[0] = "blockcrack";
                    }
                    if (parts[0].equals("blockcrack") || parts[0].equals("blockdust")) {
                        final int id = Integer.parseInt(parts[1]);
                        final int metadata = Integer.parseInt(parts[2]);
                        final IdAndData block = new IdAndData(id, metadata);
                        wrapper.user().get(ChunkTracker.class).remapBlockParticle(block);
                        parts[1] = String.valueOf(block.id);
                        parts[2] = String.valueOf(block.data);
                    }

                    wrapper.set(Type.STRING, 0, String.join("_", parts));
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.GAME_EVENT, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.BYTE, Type.UNSIGNED_BYTE); // reason
                map(Type.BYTE, Type.FLOAT); // value
                handler(wrapper -> {
                    final short gameState = wrapper.get(Type.UNSIGNED_BYTE, 0);
                    if (gameState == 1) {
                        final PacketWrapper startRain = PacketWrapper.create(ClientboundPackets1_7_2.GAME_EVENT, wrapper.user());
                        startRain.write(Type.UNSIGNED_BYTE, (short) 7);
                        startRain.write(Type.FLOAT, 1.0f);

                        wrapper.send(Protocol1_7_2_5to1_6_4.class);
                        startRain.send(Protocol1_7_2_5to1_6_4.class);
                        wrapper.cancel();
                    } else if (gameState == 2) {
                        final PacketWrapper stopRain = PacketWrapper.create(ClientboundPackets1_7_2.GAME_EVENT, wrapper.user());
                        stopRain.write(Type.UNSIGNED_BYTE, (short) 7);
                        stopRain.write(Type.FLOAT, 0.0f);

                        wrapper.send(Protocol1_7_2_5to1_6_4.class);
                        stopRain.send(Protocol1_7_2_5to1_6_4.class);
                        wrapper.cancel();
                    }
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.SPAWN_GLOBAL_ENTITY, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.INT, Type.VAR_INT); // entity id
                map(Type.BYTE); // type id
                map(Type.INT); // x
                map(Type.INT); // y
                map(Type.INT); // z
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.OPEN_WINDOW, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.UNSIGNED_BYTE); // window id
                map(Type.UNSIGNED_BYTE); // window type
                map(Types1_6_4.STRING, Type.STRING); // title
                map(Type.UNSIGNED_BYTE); // slots
                map(Type.BOOLEAN); // use provided title
                // more conditional data
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.CLOSE_WINDOW, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.BYTE, Type.UNSIGNED_BYTE); // window id
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.SET_SLOT, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.BYTE); // window id
                map(Type.SHORT); // slot
                map(Types1_7_6.ITEM); // item
                handler(wrapper -> itemRewriter.handleItemToClient(wrapper.get(Types1_7_6.ITEM, 0)));
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.WINDOW_ITEMS, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.BYTE, Type.UNSIGNED_BYTE); // window id
                handler(wrapper -> {
                    final Item[] items = wrapper.passthrough(Types1_7_6.ITEM_ARRAY); // items
                    for (Item item : items) {
                        itemRewriter.handleItemToClient(item);
                    }
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.UPDATE_SIGN, new PacketHandlers() {
            @Override
            public void register() {
                map(Types1_7_6.POSITION_SHORT); // position
                map(Types1_6_4.STRING, Type.STRING); // line 1
                map(Types1_6_4.STRING, Type.STRING); // line 2
                map(Types1_6_4.STRING, Type.STRING); // line 3
                map(Types1_6_4.STRING, Type.STRING); // line 4
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.MAP_DATA, new PacketHandlers() {
            @Override
            public void register() {
                read(Type.SHORT); // item id
                map(Type.SHORT, Type.VAR_INT); // map id
                map(Type.SHORT_BYTE_ARRAY); // data
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.BLOCK_ENTITY_DATA, new PacketHandlers() {
            @Override
            public void register() {
                map(Types1_7_6.POSITION_SHORT); // position
                map(Type.BYTE, Type.UNSIGNED_BYTE); // type
                map(Types1_7_6.NBT); // data
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.OPEN_SIGN_EDITOR, new PacketHandlers() {
            @Override
            public void register() {
                read(Type.BYTE); // always 0
                map(Types1_7_6.POSITION_INT); // position
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.STATISTICS, new PacketHandlers() {
            @Override
            public void register() {
                handler(wrapper -> {
                    wrapper.cancel();
                    final StatisticsStorage statisticsStorage = wrapper.user().get(StatisticsStorage.class);
                    final int statId = wrapper.read(Type.INT); // statistic id
                    final int increment = wrapper.read(Type.INT); // increment
                    statisticsStorage.values.put(statId, statisticsStorage.values.get(statId) + increment);
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.PLAYER_INFO, new PacketHandlers() {
            @Override
            public void register() {
                map(Types1_6_4.STRING, Type.STRING); // name
                map(Type.BOOLEAN); // online
                map(Type.SHORT); // ping
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.TAB_COMPLETE, new PacketHandlers() {
            @Override
            public void register() {
                handler(wrapper -> {
                    final String completions = wrapper.read(Types1_6_4.STRING); // completions
                    final String[] completionsArray = completions.split("\0");
                    wrapper.write(Type.VAR_INT, completionsArray.length); // completions count
                    for (String s : completionsArray) {
                        wrapper.write(Type.STRING, s); // completion
                    }
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.SCOREBOARD_OBJECTIVE, new PacketHandlers() {
            @Override
            public void register() {
                map(Types1_6_4.STRING, Type.STRING); // name
                map(Types1_6_4.STRING, Type.STRING); // value
                map(Type.BYTE); // mode
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.UPDATE_SCORE, new PacketHandlers() {
            @Override
            public void register() {
                handler(wrapper -> {
                    wrapper.write(Type.STRING, wrapper.read(Types1_6_4.STRING)); // name
                    final byte mode = wrapper.passthrough(Type.BYTE); // mode
                    if (mode == 0) {
                        wrapper.write(Type.STRING, wrapper.read(Types1_6_4.STRING)); // objective
                        wrapper.passthrough(Type.INT); // score
                    }
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.DISPLAY_SCOREBOARD, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.BYTE); // position
                map(Types1_6_4.STRING, Type.STRING); // name
            }
        });
        this.registerClientbound(ClientboundPackets1_6_4.TEAMS, new PacketHandlers() {
            @Override
            public void register() {
                map(Types1_6_4.STRING, Type.STRING); // name
                handler(wrapper -> {
                    final byte mode = wrapper.passthrough(Type.BYTE); // mode
                    if (mode == 0 || mode == 2) {
                        wrapper.write(Type.STRING, wrapper.read(Types1_6_4.STRING)); // display name
                        wrapper.write(Type.STRING, wrapper.read(Types1_6_4.STRING)); // prefix
                        wrapper.write(Type.STRING, wrapper.read(Types1_6_4.STRING)); // suffix
                        wrapper.passthrough(Type.BYTE); // flags
                    }
                    if (mode == 0 || mode == 3 || mode == 4) {
                        final int count = wrapper.passthrough(Type.SHORT); // count
                        for (int i = 0; i < count; i++) {
                            wrapper.write(Type.STRING, wrapper.read(Types1_6_4.STRING)); // player name
                        }
                    }
                });
            }
        });
        this.registerClientboundTransition(ClientboundPackets1_6_4.PLUGIN_MESSAGE,
                ClientboundPackets1_7_2.PLUGIN_MESSAGE, new PacketHandlers() {
                    @Override
                    public void register() {
                        map(Types1_6_4.STRING, Type.STRING); // channel
                        handler(wrapper -> {
                            final String channel = wrapper.get(Type.STRING, 0);
                            wrapper.passthrough(Type.SHORT); // length
                            if (channel.equals("MC|TrList")) {
                                wrapper.passthrough(Type.INT); // window id
                                final int count = wrapper.passthrough(Type.UNSIGNED_BYTE); // count
                                for (int i = 0; i < count; i++) {
                                    itemRewriter.handleItemToClient(wrapper.passthrough(Types1_7_6.ITEM)); // item 1
                                    itemRewriter.handleItemToClient(wrapper.passthrough(Types1_7_6.ITEM)); // item 3
                                    if (wrapper.passthrough(Type.BOOLEAN)) { // has 3 items
                                        itemRewriter.handleItemToClient(wrapper.passthrough(Types1_7_6.ITEM)); // item 2
                                    }
                                    wrapper.passthrough(Type.BOOLEAN); // unavailable
                                }
                            }
                        });
                    }
                }, State.LOGIN, (PacketHandler) PacketWrapper::cancel
        );
        this.registerClientboundTransition(ClientboundPackets1_6_4.SHARED_KEY, ClientboundLoginPackets.GAME_PROFILE, new PacketHandlers() {
            @Override
            public void register() {
                handler(wrapper -> {
                    final ProtocolInfo info = wrapper.user().getProtocolInfo();
                    final ProtocolMetadataStorage protocolMetadata = wrapper.user().get(ProtocolMetadataStorage.class);
                    wrapper.read(Type.SHORT_BYTE_ARRAY); // shared secret
                    wrapper.read(Type.SHORT_BYTE_ARRAY); // verify token
                    wrapper.write(Type.STRING, info.getUuid().toString().replace("-", "")); // uuid
                    wrapper.write(Type.STRING, info.getUsername()); // user name

                    if (!protocolMetadata.skipEncryption) {
                        Via.getManager().getProviders().get(EncryptionProvider.class).enableDecryption(wrapper.user());
                    }

                    // Parts of BaseProtocol1_7 GAME_PROFILE handler
                    if (info.getProtocolVersion() < ProtocolVersion.v1_20_2.getVersion()) {
                        info.setState(State.PLAY);
                    }
                    Via.getManager().getConnectionManager().onLoginSuccess(wrapper.user());
                    if (!info.getPipeline().hasNonBaseProtocols()) {
                        wrapper.user().setActive(false);
                    }
                    if (Via.getManager().isDebug()) {
                        ViaLegacy.getPlatform().getLogger().log(Level.INFO, "{0} logged in with protocol {1}, Route: {2}", new Object[]{info.getUsername(), info.getProtocolVersion(), Joiner.on(", ").join(info.getPipeline().pipes(), ", ")});
                    }

                    final PacketWrapper respawn = PacketWrapper.create(ServerboundPackets1_6_4.CLIENT_STATUS, wrapper.user());
                    respawn.write(Type.BYTE, (byte) 0); // force respawn
                    respawn.sendToServer(Protocol1_7_2_5to1_6_4.class);
                });
            }
        });
        this.registerClientboundTransition(ClientboundPackets1_6_4.SERVER_AUTH_DATA, ClientboundLoginPackets.HELLO, new PacketHandlers() {
            @Override
            public void register() {
                map(Types1_6_4.STRING, Type.STRING); // server hash
                map(Type.SHORT_BYTE_ARRAY); // public key
                map(Type.SHORT_BYTE_ARRAY); // verify token
                handler(wrapper -> {
                    final ProtocolMetadataStorage protocolMetadata = wrapper.user().get(ProtocolMetadataStorage.class);
                    final String serverHash = wrapper.get(Type.STRING, 0);
                    protocolMetadata.authenticate = !serverHash.equals("-");
                });
            }
        });
        this.registerClientboundTransition(ClientboundPackets1_6_4.DISCONNECT,
                ClientboundStatusPackets.STATUS_RESPONSE, new PacketHandlers() {
                    @Override
                    protected void register() {
                        handler(wrapper -> {
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

                                wrapper.write(Type.STRING, rootObject.toString());
                            } catch (Throwable e) {
                                ViaLegacy.getPlatform().getLogger().log(Level.WARNING, "Could not parse 1.6.4 ping: " + reason, e);
                                wrapper.cancel();
                            }
                        });
                    }
                }, ClientboundLoginPackets.LOGIN_DISCONNECT, new PacketHandlers() {
                    @Override
                    protected void register() {
                        map(Types1_6_4.STRING, Type.STRING, ChatComponentRewriter::toClientDisconnect); // reason
                    }
                }, ClientboundPackets1_7_2.DISCONNECT, new PacketHandlers() {
                    @Override
                    public void register() {
                        map(Types1_6_4.STRING, Type.STRING, ChatComponentRewriter::toClientDisconnect); // reason
                    }
                }
        );
        this.cancelClientbound(ClientboundPackets1_6_4.CREATIVE_INVENTORY_ACTION);

        this.registerServerboundTransition(ServerboundStatusPackets.STATUS_REQUEST, ServerboundPackets1_6_4.SERVER_PING, new PacketHandlers() {
            @Override
            public void register() {
                handler(wrapper -> {
                    final HandshakeStorage handshakeStorage = wrapper.user().get(HandshakeStorage.class);
                    final String ip = handshakeStorage.getHostname();
                    final int port = handshakeStorage.getPort();
                    wrapper.write(Type.UNSIGNED_BYTE, (short) 1); // always 1
                    wrapper.write(Type.UNSIGNED_BYTE, (short) ServerboundPackets1_6_4.PLUGIN_MESSAGE.getId()); // packet id
                    wrapper.write(Types1_6_4.STRING, "MC|PingHost"); // channel
                    wrapper.write(Type.UNSIGNED_SHORT, 3 + 2 * ip.length() + 4); // length
                    wrapper.write(Type.UNSIGNED_BYTE, (short) LegacyProtocolVersion.getRealProtocolVersion(wrapper.user().getProtocolInfo().getServerProtocolVersion())); // protocol Id
                    wrapper.write(Types1_6_4.STRING, ip); // hostname
                    wrapper.write(Type.INT, port); // port
                });
            }
        });
        this.registerServerboundTransition(ServerboundStatusPackets.PING_REQUEST, null, new PacketHandlers() {
            @Override
            public void register() {
                handler(wrapper -> {
                    wrapper.cancel();
                    final PacketWrapper pong = PacketWrapper.create(ClientboundStatusPackets.PONG_RESPONSE, wrapper.user());
                    pong.write(Type.LONG, wrapper.read(Type.LONG)); // start time
                    pong.send(Protocol1_7_2_5to1_6_4.class);
                });
            }
        });
        this.registerServerboundTransition(ServerboundLoginPackets.HELLO, ServerboundPackets1_6_4.CLIENT_PROTOCOL, new PacketHandlers() {
            @Override
            public void register() {
                handler(wrapper -> {
                    final String name = wrapper.read(Type.STRING); // user name
                    final ProtocolInfo info = wrapper.user().getProtocolInfo();
                    final HandshakeStorage handshakeStorage = wrapper.user().get(HandshakeStorage.class);
                    info.setUsername(name);
                    info.setUuid(ViaLegacy.getConfig().isLegacySkinLoading() ? Via.getManager().getProviders().get(GameProfileFetcher.class).getMojangUUID(name) : new GameProfile(name).uuid);

                    wrapper.write(Type.UNSIGNED_BYTE, (short) LegacyProtocolVersion.getRealProtocolVersion(info.getServerProtocolVersion())); // protocol id
                    wrapper.write(Types1_6_4.STRING, name); // user name
                    wrapper.write(Types1_6_4.STRING, handshakeStorage.getHostname()); // hostname
                    wrapper.write(Type.INT, handshakeStorage.getPort()); // port
                });
            }
        });
        this.registerServerboundTransition(ServerboundLoginPackets.ENCRYPTION_KEY, ServerboundPackets1_6_4.SHARED_KEY, null);
        this.registerServerbound(ServerboundPackets1_7_2.CHAT_MESSAGE, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.STRING, Types1_6_4.STRING); // message
            }
        });
        this.registerServerbound(ServerboundPackets1_7_2.INTERACT_ENTITY, new PacketHandlers() {
            @Override
            public void register() {
                handler(wrapper -> wrapper.write(Type.INT, wrapper.user().get(PlayerInfoStorage.class).entityId)); // player id
                map(Type.INT); // entity id
                map(Type.BYTE); // mode
            }
        });
        this.registerServerbound(ServerboundPackets1_7_2.PLAYER_MOVEMENT, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.BOOLEAN); // onGround
                handler(wrapper -> wrapper.user().get(PlayerInfoStorage.class).onGround = wrapper.get(Type.BOOLEAN, 0));
            }
        });
        this.registerServerbound(ServerboundPackets1_7_2.PLAYER_POSITION, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.DOUBLE); // x
                map(Type.DOUBLE); // y
                map(Type.DOUBLE); // stance
                map(Type.DOUBLE); // z
                map(Type.BOOLEAN); // onGround
                handler(wrapper -> {
                    final PlayerInfoStorage playerInfoStorage = wrapper.user().get(PlayerInfoStorage.class);
                    playerInfoStorage.posX = wrapper.get(Type.DOUBLE, 0);
                    playerInfoStorage.posY = wrapper.get(Type.DOUBLE, 1);
                    playerInfoStorage.posZ = wrapper.get(Type.DOUBLE, 3);
                    playerInfoStorage.onGround = wrapper.get(Type.BOOLEAN, 0);
                });
            }
        });
        this.registerServerbound(ServerboundPackets1_7_2.PLAYER_ROTATION, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.FLOAT); // yaw
                map(Type.FLOAT); // pitch
                map(Type.BOOLEAN); // onGround
                handler(wrapper -> {
                    final PlayerInfoStorage playerInfoStorage = wrapper.user().get(PlayerInfoStorage.class);
                    playerInfoStorage.yaw = wrapper.get(Type.FLOAT, 0);
                    playerInfoStorage.pitch = wrapper.get(Type.FLOAT, 1);
                    playerInfoStorage.onGround = wrapper.get(Type.BOOLEAN, 0);
                });
            }
        });
        this.registerServerbound(ServerboundPackets1_7_2.PLAYER_POSITION_AND_ROTATION, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.DOUBLE); // x
                map(Type.DOUBLE); // y
                map(Type.DOUBLE); // stance
                map(Type.DOUBLE); // z
                map(Type.FLOAT); // yaw
                map(Type.FLOAT); // pitch
                map(Type.BOOLEAN); // onGround
                handler(wrapper -> {
                    final PlayerInfoStorage playerInfoStorage = wrapper.user().get(PlayerInfoStorage.class);
                    playerInfoStorage.posX = wrapper.get(Type.DOUBLE, 0);
                    playerInfoStorage.posY = wrapper.get(Type.DOUBLE, 1);
                    playerInfoStorage.posZ = wrapper.get(Type.DOUBLE, 3);
                    playerInfoStorage.yaw = wrapper.get(Type.FLOAT, 0);
                    playerInfoStorage.pitch = wrapper.get(Type.FLOAT, 1);
                    playerInfoStorage.onGround = wrapper.get(Type.BOOLEAN, 0);
                });
            }
        });
        this.registerServerbound(ServerboundPackets1_7_2.PLAYER_BLOCK_PLACEMENT, new PacketHandlers() {
            @Override
            public void register() {
                map(Types1_7_6.POSITION_UBYTE); // position
                map(Type.UNSIGNED_BYTE); // direction
                map(Types1_7_6.ITEM); // item
                handler(wrapper -> itemRewriter.handleItemToServer(wrapper.get(Types1_7_6.ITEM, 0)));
                map(Type.UNSIGNED_BYTE); // offset x
                map(Type.UNSIGNED_BYTE); // offset y
                map(Type.UNSIGNED_BYTE); // offset z
            }
        });
        this.registerServerbound(ServerboundPackets1_7_2.CLICK_WINDOW, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.BYTE); // windowId
                map(Type.SHORT); // slot
                map(Type.BYTE); // button
                map(Type.SHORT); // action
                map(Type.BYTE); // mode
                map(Types1_7_6.ITEM); // item
                handler(wrapper -> itemRewriter.handleItemToServer(wrapper.get(Types1_7_6.ITEM, 0)));
            }
        });
        this.registerServerbound(ServerboundPackets1_7_2.UPDATE_SIGN, new PacketHandlers() {
            @Override
            public void register() {
                map(Types1_7_6.POSITION_SHORT); // position
                map(Type.STRING, Types1_6_4.STRING); // line 1
                map(Type.STRING, Types1_6_4.STRING); // line 2
                map(Type.STRING, Types1_6_4.STRING); // line 3
                map(Type.STRING, Types1_6_4.STRING); // line 4
            }
        });
        this.registerServerbound(ServerboundPackets1_7_2.TAB_COMPLETE, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.STRING, Types1_6_4.STRING); // text
            }
        });
        this.registerServerbound(ServerboundPackets1_7_2.CLIENT_SETTINGS, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.STRING, Types1_6_4.STRING); // language
                handler(wrapper -> {
                    byte renderDistance = wrapper.read(Type.BYTE); // render distance

                    if (renderDistance <= 2) {
                        renderDistance = 3; // TINY
                    } else if (renderDistance <= 4) {
                        renderDistance = 2; // SHORT
                    } else if (renderDistance <= 8) {
                        renderDistance = 1; // NORMAL
                    } else { // >= 16
                        renderDistance = 0; // FAR
                    }

                    wrapper.write(Type.BYTE, renderDistance);
                });
                handler(wrapper -> {
                    final byte chatVisibility = wrapper.read(Type.BYTE); // chat visibility
                    final boolean enableColors = wrapper.read(Type.BOOLEAN); // enable colors
                    final byte mask = (byte) (chatVisibility | (enableColors ? 1 : 0) << 3);
                    wrapper.write(Type.BYTE, mask); // mask
                });
                map(Type.BYTE); // difficulty
                map(Type.BOOLEAN); // show cape
            }
        });
        this.registerServerbound(ServerboundPackets1_7_2.CLIENT_STATUS, new PacketHandlers() {
            @Override
            public void register() {
                handler(wrapper -> {
                    final int action = wrapper.read(Type.VAR_INT); // action

                    if (action == 1) { // Request Statistics
                        final Object2IntMap<String> loadedStatistics = new Object2IntOpenHashMap<>();
                        for (Int2IntMap.Entry entry : wrapper.user().get(StatisticsStorage.class).values.int2IntEntrySet()) {
                            final String key = StatisticRewriter.map(entry.getIntKey());
                            if (key == null) continue;
                            loadedStatistics.put(key, entry.getIntValue());
                        }

                        final PacketWrapper statistics = PacketWrapper.create(ClientboundPackets1_8.STATISTICS, wrapper.user());
                        statistics.write(Type.VAR_INT, loadedStatistics.size()); // count
                        for (Object2IntMap.Entry<String> entry : loadedStatistics.object2IntEntrySet()) {
                            statistics.write(Type.STRING, entry.getKey()); // statistic name
                            statistics.write(Type.VAR_INT, entry.getIntValue()); // statistic value
                        }
                        statistics.send(Protocol1_7_2_5to1_6_4.class);
                    }
                    if (action != 0) {
                        wrapper.cancel();
                        return;
                    }
                    wrapper.write(Type.BYTE, (byte) 1); // force respawn
                });
            }
        });
        this.registerServerbound(ServerboundPackets1_7_2.PLUGIN_MESSAGE, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.STRING, Types1_6_4.STRING); // channel
                map(Type.SHORT); // length
                handler(wrapper -> {
                    final String channel = wrapper.get(Types1_6_4.STRING, 0);
                    final PacketWrapper lengthPacketWrapper = PacketWrapper.create(null, wrapper.user());
                    final ByteBuf lengthBuffer = Unpooled.buffer();

                    switch (channel) {
                        case "MC|BEdit":
                        case "MC|BSign":
                            final Item item = wrapper.read(Types1_7_6.ITEM); // book
                            itemRewriter.handleItemToServer(item);

                            lengthPacketWrapper.write(Types1_7_6.ITEM, item);
                            lengthPacketWrapper.writeToBuffer(lengthBuffer);

                            wrapper.set(Type.SHORT, 0, (short) lengthBuffer.readableBytes()); // length
                            wrapper.write(Types1_7_6.ITEM, item); // book
                            break;
                        case "MC|AdvCdm":
                            final byte type = wrapper.read(Type.BYTE); // command block type
                            if (type == 0) {
                                final int posX = wrapper.read(Type.INT); // x
                                final int posY = wrapper.read(Type.INT); // y
                                final int posZ = wrapper.read(Type.INT); // z
                                final String command = wrapper.read(Type.STRING); // command

                                lengthPacketWrapper.write(Type.INT, posX);
                                lengthPacketWrapper.write(Type.INT, posY);
                                lengthPacketWrapper.write(Type.INT, posZ);
                                lengthPacketWrapper.write(Types1_6_4.STRING, command);
                                lengthPacketWrapper.writeToBuffer(lengthBuffer);

                                wrapper.set(Type.SHORT, 0, (short) lengthBuffer.readableBytes()); // length
                                wrapper.write(Type.INT, posX); // x
                                wrapper.write(Type.INT, posY); // y
                                wrapper.write(Type.INT, posZ); // z
                                wrapper.write(Types1_6_4.STRING, command); // command
                            } else {
                                wrapper.cancel();
                            }
                            break;
                    }
                    lengthBuffer.release();
                });
            }
        });
    }

    private void rewriteMetadata(final List<Metadata> metadataList) {
        for (Metadata metadata : metadataList) {
            if (metadata.metaType().equals(MetaType1_6_4.Slot)) {
                itemRewriter.handleItemToClient(metadata.value());
            }
            metadata.setMetaType(MetaType1_7_6.byId(metadata.metaType().typeId()));
        }
    }

    @Override
    public void register(ViaProviders providers) {
        providers.require(EncryptionProvider.class);
    }

    @Override
    public void init(UserConnection userConnection) {
        userConnection.put(new PreNettySplitter(Protocol1_7_2_5to1_6_4.class, ClientboundPackets1_6_4::getPacket));

        userConnection.put(new PlayerInfoStorage());
        userConnection.put(new StatisticsStorage());
        userConnection.put(new DimensionTracker());
        userConnection.put(new ChunkTracker(userConnection)); // Set again in JOIN_GAME handler for version comparisons to work

        if (userConnection.getChannel() != null) {
            userConnection.getChannel().pipeline().addFirst(new ChannelOutboundHandlerAdapter() {
                @Override
                public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
                    if (ctx.channel().isWritable() && userConnection.getProtocolInfo().getClientState().equals(State.PLAY) && userConnection.get(PlayerInfoStorage.class).entityId != -1) {
                        final PacketWrapper disconnect = PacketWrapper.create(ServerboundPackets1_6_4.DISCONNECT, userConnection);
                        disconnect.write(Types1_6_4.STRING, "Quitting"); // reason
                        disconnect.sendToServer(Protocol1_7_2_5to1_6_4.class);
                    }

                    super.close(ctx, promise);
                }
            });
        }
    }

    @Override
    public LegacyItemRewriter<Protocol1_7_2_5to1_6_4> getItemRewriter() {
        return this.itemRewriter;
    }

}
