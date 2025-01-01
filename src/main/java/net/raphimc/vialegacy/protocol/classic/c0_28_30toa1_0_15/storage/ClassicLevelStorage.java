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
package net.raphimc.vialegacy.protocol.classic.c0_28_30toa1_0_15.storage;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.StoredObject;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.BlockPosition;
import com.viaversion.viaversion.api.minecraft.chunks.*;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.util.ChunkUtil;
import net.raphimc.vialegacy.ViaLegacy;
import net.raphimc.vialegacy.api.model.ChunkCoord;
import net.raphimc.vialegacy.api.util.ChunkCoordSpiral;
import net.raphimc.vialegacy.protocol.alpha.a1_0_15toa1_0_16_2.packet.ClientboundPacketsa1_0_15;
import net.raphimc.vialegacy.protocol.classic.c0_28_30toa1_0_15.Protocolc0_28_30Toa1_0_15;
import net.raphimc.vialegacy.protocol.classic.c0_28_30toa1_0_15.model.ClassicLevel;
import net.raphimc.vialegacy.protocol.classic.c0_28_30toa1_0_15.provider.ClassicWorldHeightProvider;
import net.raphimc.vialegacy.protocol.release.r1_1tor1_2_1_3.model.LegacyNibbleArray;
import net.raphimc.vialegacy.protocol.release.r1_1tor1_2_1_3.types.Types1_1;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.GZIPInputStream;

public class ClassicLevelStorage extends StoredObject {

    private ByteArrayOutputStream netBuffer = new ByteArrayOutputStream(64 * 64 * 64);

    private ClassicLevel classicLevel;

    private int chunkXCount;
    private int sectionYCount;
    private int chunkZCount;

    private int subChunkXLength;
    private int subChunkYLength;
    private int subChunkZLength;

    private int sectionBitmask;

    private final Set<ChunkCoord> loadedChunks = new HashSet<>();
    private long eventLoopPing = 0;

    public ClassicLevelStorage(final UserConnection user) {
        super(user);
    }

    public void addDataPart(final byte[] part, final int partSize) {
        if (this.netBuffer == null) throw new IllegalStateException("Level is already fully loaded");
        this.netBuffer.write(part, 0, partSize);
    }

    public void finish(final int sizeX, final int sizeY, final int sizeZ) {
        try {
            final DataInputStream dis = new DataInputStream(new GZIPInputStream(new ByteArrayInputStream(this.netBuffer.toByteArray()), 64 * 1024));
            final byte[] blocks = new byte[dis.readInt()];
            dis.readFully(blocks);
            dis.close();
            this.netBuffer = null;
            this.classicLevel = new ClassicLevel(sizeX, sizeY, sizeZ, blocks);
        } catch (Throwable e) {
            throw new IllegalStateException("Failed to load level", e);
        }

        final short maxChunkSectionCount = Via.getManager().getProviders().get(ClassicWorldHeightProvider.class).getMaxChunkSectionCount(this.user());

        this.chunkXCount = sizeX >> 4;
        if (sizeX % 16 != 0) this.chunkXCount++;
        this.sectionYCount = sizeY >> 4;
        if (sizeY % 16 != 0) this.sectionYCount++;
        if (this.sectionYCount > maxChunkSectionCount) this.sectionYCount = maxChunkSectionCount;
        this.chunkZCount = sizeZ >> 4;
        if (sizeZ % 16 != 0) this.chunkZCount++;
        this.subChunkXLength = Math.min(16, sizeX);
        this.subChunkYLength = Math.min(16, sizeY);
        this.subChunkZLength = Math.min(16, sizeZ);
        this.sectionBitmask = 0;
        for (int i = 0; i < this.sectionYCount; i++) this.sectionBitmask = (this.sectionBitmask << 1) | 1;

        { // Sodium fix (Sodium requires a ring of empty chunks around the loaded chunks)
            for (int chunkX = -1; chunkX <= this.chunkXCount; chunkX++) {
                for (int chunkZ = -1; chunkZ <= this.chunkZCount; chunkZ++) {
                    if (chunkX < 0 || chunkX >= this.chunkXCount || chunkZ < 0 || chunkZ >= this.chunkZCount) {
                        final Chunk chunk = ChunkUtil.createEmptyChunk(chunkX, chunkZ, Math.max(8, this.sectionYCount), this.sectionBitmask);
                        ChunkUtil.setDummySkylight(chunk, true);
                        final PacketWrapper chunkData = PacketWrapper.create(ClientboundPacketsa1_0_15.LEVEL_CHUNK, this.user());
                        chunkData.write(Types1_1.CHUNK, chunk);
                        chunkData.send(Protocolc0_28_30Toa1_0_15.class);
                    }
                }
            }
        }
    }

    public void tick() {
        final ClassicPositionTracker positionTracker = this.user().get(ClassicPositionTracker.class);
        if (!positionTracker.spawned) return;

        final long start = System.currentTimeMillis();
        this.user().getChannel().eventLoop().submit(() -> {
            ClassicLevelStorage.this.eventLoopPing = System.currentTimeMillis() - start;
        });

        int limit = 0;
        if (this.eventLoopPing < 50) {
            limit = 12;
        } else if (this.eventLoopPing < 100) {
            limit = 6;
        } else if (this.eventLoopPing < 250) {
            limit = 3;
        } else if (this.eventLoopPing < 400) {
            limit = 1;
        }
        if (limit != 0) {
            this.sendChunks(positionTracker.getChunkPosition(), ViaLegacy.getConfig().getClassicChunkRange(), limit);
        }
    }

    public void sendChunks(final ChunkCoord center, final int radius) {
        this.sendChunks(center, radius, Integer.MAX_VALUE);
    }

    public void sendChunks(final ChunkCoord center, final int radius, int limit) {
        final ChunkCoordSpiral spiral = new ChunkCoordSpiral(center, new ChunkCoord(radius, radius));
        for (ChunkCoord coord : spiral) {
            if (!this.shouldSend(coord)) continue;
            if (limit-- <= 0) return;
            this.sendChunk(coord);
        }
    }

    public void sendChunk(final ChunkCoord coord) {
        if (!this.shouldSend(coord)) return;
        final ClassicBlockRemapper remapper = this.user().get(ClassicBlockRemapper.class);

        this.classicLevel.calculateLight(coord.chunkX * 16, coord.chunkZ * 16, this.subChunkXLength, this.subChunkZLength);

        final ChunkSection[] modernSections = new ChunkSection[Math.max(8, this.sectionYCount)];
        for (int sectionY = 0; sectionY < this.sectionYCount; sectionY++) {
            final ChunkSection section = modernSections[sectionY] = new ChunkSectionImpl(true);
            section.palette(PaletteType.BLOCKS).addId(0);
            final LegacyNibbleArray skyLight = new LegacyNibbleArray(16 * 16 * 16, 4);

            for (int y = 0; y < this.subChunkYLength; y++) {
                final int totalY = y + (sectionY * 16);
                for (int x = 0; x < this.subChunkXLength; x++) {
                    final int totalX = x + (coord.chunkX * 16);
                    for (int z = 0; z < this.subChunkZLength; z++) {
                        final int totalZ = z + (coord.chunkZ * 16);
                        section.palette(PaletteType.BLOCKS).setIdAt(x, y, z, remapper.mapper().get(this.classicLevel.getBlock(totalX, totalY, totalZ)).toRawData());
                        skyLight.set(x, y, z, this.classicLevel.isLit(totalX, totalY, totalZ) ? 15 : 9);
                    }
                }
            }

            section.getLight().setSkyLight(skyLight.getHandle());
        }

        this.loadedChunks.add(coord);

        final Chunk viaChunk = new BaseChunk(coord.chunkX, coord.chunkZ, true, false, this.sectionBitmask, modernSections, new int[256], new ArrayList<>());
        final PacketWrapper chunkData = PacketWrapper.create(ClientboundPacketsa1_0_15.LEVEL_CHUNK, this.user());
        chunkData.write(Types1_1.CHUNK, viaChunk);
        chunkData.send(Protocolc0_28_30Toa1_0_15.class);
    }

    private boolean shouldSend(final ChunkCoord coord) {
        if (!this.hasReceivedLevel()) return false;
        boolean isInBounds = (coord.chunkX >= 0 && coord.chunkX < chunkXCount) && coord.chunkZ >= 0 && coord.chunkZ < chunkZCount;
        return isInBounds && !this.isChunkLoaded(coord);
    }

    public boolean isChunkLoaded(final ChunkCoord coord) {
        return this.loadedChunks.contains(coord);
    }

    public boolean isChunkLoaded(final BlockPosition position) {
        return this.isChunkLoaded(new ChunkCoord(position.x() >> 4, position.z() >> 4));
    }

    public boolean hasReceivedLevel() {
        return this.classicLevel != null;
    }

    public ClassicLevel getClassicLevel() {
        return this.classicLevel;
    }

}
