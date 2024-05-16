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
package net.raphimc.vialegacy.protocols.release.protocol1_2_1_3to1_1;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.BlockChangeRecord;
import com.viaversion.viaversion.api.minecraft.Position;
import com.viaversion.viaversion.api.minecraft.chunks.Chunk;
import com.viaversion.viaversion.api.minecraft.chunks.ChunkSection;
import com.viaversion.viaversion.api.minecraft.chunks.NibbleArray;
import com.viaversion.viaversion.api.platform.providers.ViaProviders;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.api.type.Types;
import com.viaversion.viaversion.util.IdAndData;
import net.raphimc.vialegacy.ViaLegacy;
import net.raphimc.vialegacy.api.LegacyProtocolVersion;
import net.raphimc.vialegacy.api.protocol.StatelessProtocol;
import net.raphimc.vialegacy.api.splitter.PreNettySplitter;
import net.raphimc.vialegacy.protocols.release.protocol1_2_1_3to1_1.biome.EndBiomeGenerator;
import net.raphimc.vialegacy.protocols.release.protocol1_2_1_3to1_1.biome.NetherBiomeGenerator;
import net.raphimc.vialegacy.protocols.release.protocol1_2_1_3to1_1.biome.PlainsBiomeGenerator;
import net.raphimc.vialegacy.protocols.release.protocol1_2_1_3to1_1.biome.beta.WorldChunkManager_b1_7;
import net.raphimc.vialegacy.protocols.release.protocol1_2_1_3to1_1.biome.release.WorldChunkManager_r1_1;
import net.raphimc.vialegacy.protocols.release.protocol1_2_1_3to1_1.chunks.NibbleArray1_1;
import net.raphimc.vialegacy.protocols.release.protocol1_2_1_3to1_1.model.NonFullChunk1_1;
import net.raphimc.vialegacy.protocols.release.protocol1_2_1_3to1_1.rewriter.ItemRewriter;
import net.raphimc.vialegacy.protocols.release.protocol1_2_1_3to1_1.storage.DimensionTracker;
import net.raphimc.vialegacy.protocols.release.protocol1_2_1_3to1_1.storage.PendingBlocksTracker;
import net.raphimc.vialegacy.protocols.release.protocol1_2_1_3to1_1.storage.SeedStorage;
import net.raphimc.vialegacy.protocols.release.protocol1_2_1_3to1_1.tasks.BlockReceiveInvalidatorTask;
import net.raphimc.vialegacy.protocols.release.protocol1_2_1_3to1_1.types.Types1_1;
import net.raphimc.vialegacy.protocols.release.protocol1_2_4_5to1_2_1_3.ClientboundPackets1_2_1;
import net.raphimc.vialegacy.protocols.release.protocol1_2_4_5to1_2_1_3.ServerboundPackets1_2_1;
import net.raphimc.vialegacy.protocols.release.protocol1_3_1_2to1_2_4_5.types.Types1_2_4;
import net.raphimc.vialegacy.protocols.release.protocol1_4_2to1_3_1_2.types.Types1_3_1;
import net.raphimc.vialegacy.protocols.release.protocol1_7_2_5to1_6_4.storage.ChunkTracker;
import net.raphimc.vialegacy.protocols.release.protocol1_7_2_5to1_6_4.types.Types1_6_4;
import net.raphimc.vialegacy.protocols.release.protocol1_8to1_7_6_10.types.Types1_7_6;

import java.util.Arrays;

public class Protocol1_2_1_3to1_1 extends StatelessProtocol<ClientboundPackets1_1, ClientboundPackets1_2_1, ServerboundPackets1_1, ServerboundPackets1_2_1> {

    private final ItemRewriter itemRewriter = new ItemRewriter(this);

    public Protocol1_2_1_3to1_1() {
        super(ClientboundPackets1_1.class, ClientboundPackets1_2_1.class, ServerboundPackets1_1.class, ServerboundPackets1_2_1.class);
    }

    @Override
    protected void registerPackets() {
        this.itemRewriter.register();

        this.registerClientbound(ClientboundPackets1_1.LOGIN, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // entity id
                map(Types1_6_4.STRING); // username
                handler(wrapper -> wrapper.user().get(SeedStorage.class).seed = wrapper.read(Types.LONG)); // seed
                map(Types1_6_4.STRING); // level type
                map(Types.INT); // game mode
                map(Types.BYTE, Types.INT); // dimension id
                map(Types.BYTE); // difficulty
                map(Types.BYTE); // world height
                map(Types.BYTE); // max players
                handler(wrapper -> handleRespawn(wrapper.get(Types.INT, 2), wrapper.user()));
            }
        });
        this.registerClientbound(ClientboundPackets1_1.RESPAWN, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.BYTE, Types.INT); // dimension id
                map(Types.BYTE); // difficulty
                map(Types.BYTE); // game mode
                map(Types.SHORT); // world height
                handler(wrapper -> wrapper.user().get(SeedStorage.class).seed = wrapper.read(Types.LONG)); // seed
                map(Types1_6_4.STRING); // level type
                handler(wrapper -> handleRespawn(wrapper.get(Types.INT, 0), wrapper.user()));
            }
        });
        this.registerClientbound(ClientboundPackets1_1.ADD_MOB, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // entity id
                map(Types.UNSIGNED_BYTE); // type id
                map(Types.INT); // x
                map(Types.INT); // y
                map(Types.INT); // z
                map(Types.BYTE); // yaw
                map(Types.BYTE); // pitch
                handler(wrapper -> wrapper.write(Types.BYTE, wrapper.get(Types.BYTE, 0))); // head yaw
                map(Types1_3_1.METADATA_LIST); // metadata
            }
        });
        this.registerClientbound(ClientboundPackets1_1.MOVE_ENTITY_ROT, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // entity id
                map(Types.BYTE); // yaw
                map(Types.BYTE); // pitch
                handler(wrapper -> sendEntityHeadLook(wrapper.get(Types.INT, 0), wrapper.get(Types.BYTE, 0), wrapper));
            }
        });
        this.registerClientbound(ClientboundPackets1_1.MOVE_ENTITY_POS_ROT, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // entity id
                map(Types.BYTE); // x
                map(Types.BYTE); // y
                map(Types.BYTE); // z
                map(Types.BYTE); // yaw
                map(Types.BYTE); // pitch
                handler(wrapper -> sendEntityHeadLook(wrapper.get(Types.INT, 0), wrapper.get(Types.BYTE, 3), wrapper));
            }
        });
        this.registerClientbound(ClientboundPackets1_1.TELEPORT_ENTITY, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // entity id
                map(Types.INT); // x
                map(Types.INT); // y
                map(Types.INT); // z
                map(Types.BYTE); // yaw
                map(Types.BYTE); // pitch
                handler(wrapper -> sendEntityHeadLook(wrapper.get(Types.INT, 0), wrapper.get(Types.BYTE, 0), wrapper));
            }
        });
        this.registerClientbound(ClientboundPackets1_1.LEVEL_CHUNK, wrapper -> {
            final ChunkTracker chunkTracker = wrapper.user().get(ChunkTracker.class);
            final SeedStorage seedStorage = wrapper.user().get(SeedStorage.class);
            final PendingBlocksTracker pendingBlocksTracker = wrapper.user().get(PendingBlocksTracker.class);
            final Chunk chunk = wrapper.read(Types1_1.CHUNK);

            if (chunk instanceof NonFullChunk1_1) {
                if (!chunkTracker.isChunkLoaded(chunk.getX(), chunk.getZ())) { // Cancel because update in unloaded area is ignored by mc
                    wrapper.cancel();
                    return;
                }

                final NonFullChunk1_1 nfc = (NonFullChunk1_1) chunk;
                wrapper.setPacketType(ClientboundPackets1_2_1.CHUNK_BLOCKS_UPDATE);
                wrapper.write(Types.INT, nfc.getX());
                wrapper.write(Types.INT, nfc.getZ());
                wrapper.write(Types1_7_6.BLOCK_CHANGE_RECORD_ARRAY, nfc.asBlockChangeRecords().toArray(new BlockChangeRecord[0]));

                pendingBlocksTracker.markReceived(new Position((nfc.getX() << 4) + nfc.getStartPos().x(), nfc.getStartPos().y(), (nfc.getZ() << 4) + nfc.getStartPos().z()), new Position((nfc.getX() << 4) + nfc.getEndPos().x() - 1, nfc.getEndPos().y() - 1, (nfc.getZ() << 4) + nfc.getEndPos().z() - 1));
                return;
            }
            pendingBlocksTracker.markReceived(new Position(chunk.getX() << 4, 0, chunk.getZ() << 4), new Position((chunk.getX() << 4) + 15, chunk.getSections().length * 16, (chunk.getZ() << 4) + 15));

            int[] newBiomeData;
            if (seedStorage.worldChunkManager != null) {
                final byte[] oldBiomeData = seedStorage.worldChunkManager.getBiomeDataAt(chunk.getX(), chunk.getZ());
                newBiomeData = new int[oldBiomeData.length];
                for (int i = 0; i < oldBiomeData.length; i++) {
                    newBiomeData[i] = oldBiomeData[i] & 255;
                }
            } else {
                newBiomeData = new int[256];
                Arrays.fill(newBiomeData, 1); // plains
            }
            chunk.setBiomeData(newBiomeData);

            for (ChunkSection section : chunk.getSections()) {
                if (section == null) continue;
                final NibbleArray1_1 oldBlockLight = new NibbleArray1_1(section.getLight().getBlockLight(), 4);
                final NibbleArray newBlockLight = new NibbleArray(oldBlockLight.size());
                final NibbleArray1_1 oldSkyLight = new NibbleArray1_1(section.getLight().getSkyLight(), 4);
                final NibbleArray newSkyLight = new NibbleArray(oldSkyLight.size());

                for (int x = 0; x < 16; x++) {
                    for (int y = 0; y < 16; y++) {
                        for (int z = 0; z < 16; z++) {
                            newBlockLight.set(x, y, z, oldBlockLight.get(x, y, z));
                            newSkyLight.set(x, y, z, oldSkyLight.get(x, y, z));
                        }
                    }
                }
                section.getLight().setBlockLight(newBlockLight.getHandle());
                section.getLight().setSkyLight(newSkyLight.getHandle());
            }

            if (chunk.getSections().length < 16) { // Increase available sections to match new world height
                final ChunkSection[] newArray = new ChunkSection[16];
                System.arraycopy(chunk.getSections(), 0, newArray, 0, chunk.getSections().length);
                chunk.setSections(newArray);
            }

            wrapper.write(Types1_2_4.CHUNK, chunk);
        });
        this.registerClientbound(ClientboundPackets1_1.CHUNK_BLOCKS_UPDATE, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // chunkX
                map(Types.INT); // chunkZ
                map(Types1_1.BLOCK_CHANGE_RECORD_ARRAY, Types1_7_6.BLOCK_CHANGE_RECORD_ARRAY); // blockChangeRecords
                handler(wrapper -> {
                    final PendingBlocksTracker pendingBlocksTracker = wrapper.user().get(PendingBlocksTracker.class);
                    final int chunkX = wrapper.get(Types.INT, 0);
                    final int chunkZ = wrapper.get(Types.INT, 1);
                    final BlockChangeRecord[] blockChangeRecords = wrapper.get(Types1_7_6.BLOCK_CHANGE_RECORD_ARRAY, 0);
                    for (BlockChangeRecord record : blockChangeRecords) {
                        final int targetX = record.getSectionX() + (chunkX << 4);
                        final int targetY = record.getY(-1);
                        final int targetZ = record.getSectionZ() + (chunkZ << 4);
                        pendingBlocksTracker.markReceived(new Position(targetX, targetY, targetZ));
                    }
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_1.BLOCK_UPDATE, new PacketHandlers() {
            @Override
            public void register() {
                map(Types1_7_6.POSITION_UBYTE); // position
                map(Types.UNSIGNED_BYTE); // block id
                map(Types.UNSIGNED_BYTE); // block data
                handler(wrapper -> wrapper.user().get(PendingBlocksTracker.class).markReceived(wrapper.get(Types1_7_6.POSITION_UBYTE, 0)));
            }
        });
        this.registerClientbound(ClientboundPackets1_1.EXPLODE, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.DOUBLE); // x
                map(Types.DOUBLE); // y
                map(Types.DOUBLE); // z
                map(Types.FLOAT); // radius
                map(Types.INT); // record count
                handler(wrapper -> {
                    final PendingBlocksTracker pendingBlocksTracker = wrapper.user().get(PendingBlocksTracker.class);
                    final ChunkTracker chunkTracker = wrapper.user().get(ChunkTracker.class);
                    final int x = wrapper.get(Types.DOUBLE, 0).intValue();
                    final int y = wrapper.get(Types.DOUBLE, 1).intValue();
                    final int z = wrapper.get(Types.DOUBLE, 2).intValue();
                    final int recordCount = wrapper.get(Types.INT, 0);
                    for (int i = 0; i < recordCount; i++) {
                        final Position pos = new Position(x + wrapper.passthrough(Types.BYTE), y + wrapper.passthrough(Types.BYTE), z + wrapper.passthrough(Types.BYTE));
                        final IdAndData block = chunkTracker.getBlockNotNull(pos);
                        if (block.getId() != 0) {
                            pendingBlocksTracker.addPending(pos, block);
                        }
                    }
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_1.LEVEL_EVENT, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // effect id
                map(Types1_7_6.POSITION_UBYTE); // position
                map(Types.INT); // data
                handler(wrapper -> {
                    final int sfxId = wrapper.get(Types.INT, 0);
                    final int sfxData = wrapper.get(Types.INT, 1);
                    if (sfxId == 2001) { // Block Break effect
                        final int blockID = sfxData & 255;
                        final int blockData = sfxData >> 8 & 255;
                        wrapper.set(Types.INT, 1, blockID + (blockData << 12));
                    } else if (sfxId == 1009) { // Ghast fireball effect (volume 1) (sound packet would be a better replacement but changing the id is easier and the difference is minimal)
                        wrapper.set(Types.INT, 0, 1008); // Ghast fireball effect (volume 10)
                    }
                });
            }
        });

        this.registerServerbound(ServerboundPackets1_2_1.HANDSHAKE, new PacketHandlers() {
            @Override
            public void register() {
                map(Types1_6_4.STRING, Types1_6_4.STRING, s -> s.split(";")[0]); // info
            }
        });
        this.registerServerbound(ServerboundPackets1_2_1.LOGIN, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // protocol id
                map(Types1_6_4.STRING); // username
                create(Types.LONG, 0L); // seed
                map(Types1_6_4.STRING); // level type
                map(Types.INT); // game mode
                map(Types.INT, Types.BYTE); // dimension id
                map(Types.BYTE); // difficulty
                map(Types.BYTE); // world height
                map(Types.BYTE); // max players
            }
        });
        this.registerServerbound(ServerboundPackets1_2_1.RESPAWN, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT, Types.BYTE); // dimension id
                map(Types.BYTE); // difficulty
                map(Types.BYTE); // game mode
                map(Types.SHORT); // world height
                create(Types.LONG, 0L); // seed
                map(Types1_6_4.STRING); // level type
            }
        });
    }

    private void handleRespawn(final int dimensionId, final UserConnection user) {
        if (user.get(DimensionTracker.class).changeDimension(dimensionId)) {
            user.get(PendingBlocksTracker.class).clear();
        }

        if (ViaLegacy.getConfig().isOldBiomes()) {
            final SeedStorage seedStorage = user.get(SeedStorage.class);
            if (dimensionId == -1) { // Nether
                seedStorage.worldChunkManager = new NetherBiomeGenerator();
            } else if (dimensionId == 1) { // End
                seedStorage.worldChunkManager = new EndBiomeGenerator();
            } else if (dimensionId == 0) { // Overworld

                if (user.getProtocolInfo().serverProtocolVersion().newerThanOrEqualTo(LegacyProtocolVersion.b1_8tob1_8_1)) {
                    seedStorage.worldChunkManager = new WorldChunkManager_r1_1(user, seedStorage.seed);
                } else if (user.getProtocolInfo().serverProtocolVersion().newerThanOrEqualTo(LegacyProtocolVersion.a1_0_15)) {
                    seedStorage.worldChunkManager = new WorldChunkManager_b1_7(seedStorage.seed);
                } else {
                    seedStorage.worldChunkManager = new PlainsBiomeGenerator();
                }
            } else {
                seedStorage.worldChunkManager = null;
            }
        }
    }

    private void sendEntityHeadLook(final int entityId, final byte headYaw, final PacketWrapper wrapper) {
        final PacketWrapper entityHeadLook = PacketWrapper.create(ClientboundPackets1_2_1.ROTATE_HEAD, wrapper.user());
        entityHeadLook.write(Types.INT, entityId); // entity id
        entityHeadLook.write(Types.BYTE, headYaw); // head yaw

        wrapper.send(Protocol1_2_1_3to1_1.class);
        entityHeadLook.send(Protocol1_2_1_3to1_1.class);
        wrapper.cancel();
    }

    @Override
    public void register(ViaProviders providers) {
        Via.getPlatform().runRepeatingSync(new BlockReceiveInvalidatorTask(), 1L);
    }

    @Override
    public void init(UserConnection userConnection) {
        userConnection.put(new PreNettySplitter(Protocol1_2_1_3to1_1.class, ClientboundPackets1_1::getPacket));

        userConnection.put(new SeedStorage());
        userConnection.put(new PendingBlocksTracker(userConnection));
        userConnection.put(new DimensionTracker());
    }

    @Override
    public ItemRewriter getItemRewriter() {
        return this.itemRewriter;
    }

}
