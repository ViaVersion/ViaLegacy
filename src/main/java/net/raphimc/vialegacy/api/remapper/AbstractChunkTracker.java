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
package net.raphimc.vialegacy.api.remapper;

import com.viaversion.viaversion.api.connection.StorableObject;
import com.viaversion.viaversion.api.minecraft.BlockPosition;
import com.viaversion.viaversion.api.minecraft.chunks.*;
import com.viaversion.viaversion.libs.fastutil.ints.Int2IntMap;
import com.viaversion.viaversion.libs.fastutil.ints.Int2IntOpenHashMap;
import com.viaversion.viaversion.libs.fastutil.ints.IntOpenHashSet;
import com.viaversion.viaversion.libs.fastutil.ints.IntSet;
import com.viaversion.viaversion.util.IdAndData;
import net.raphimc.vialegacy.api.model.ChunkCoord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractChunkTracker implements StorableObject {

    private final Map<ChunkCoord, Chunk> chunks = new HashMap<>();
    private final IntSet toTrack = new IntOpenHashSet();
    private final boolean trackAll;
    private final Int2IntMap replacements = new Int2IntOpenHashMap();

    public AbstractChunkTracker(final int... toTrack) {
        for (final int trackedBlock : toTrack) {
            this.toTrack.add(trackedBlock);
        }
        this.trackAll = this.toTrack.contains(0);
    }

    public void trackAndRemap(final Chunk chunk) {
        final ChunkCoord chunkCoord = new ChunkCoord(chunk.getX(), chunk.getZ());
        if (chunk.isFullChunk() && chunk.getBitmask() == 0) {
            this.chunks.remove(chunkCoord);
            return;
        }

        final Chunk copyChunk = new BaseChunk(chunk.getX(), chunk.getZ(), true, false, 0xFFFF, new ChunkSection[chunk.getSections().length], null, new ArrayList<>());
        if (!chunk.isFullChunk()) {
            if (this.chunks.containsKey(chunkCoord)) {
                copyChunk.setSections(this.chunks.get(chunkCoord).getSections());
            } else {
                return;
            }
        } else {
            this.chunks.put(chunkCoord, copyChunk);
        }

        // Track
        if (!this.toTrack.isEmpty()) {
            for (int i = 0; i < chunk.getSections().length; i++) {
                final ChunkSection section = chunk.getSections()[i];
                if (section == null) continue;
                copyChunk.getSections()[i] = null;

                final DataPalette palette = section.palette(PaletteType.BLOCKS);
                if (!this.hasRemappableBlocks(palette)) continue;

                final ChunkSection copySection = copyChunk.getSections()[i] = new ChunkSectionImpl(false);
                final DataPalette copyPalette = copySection.palette(PaletteType.BLOCKS);
                copyPalette.addId(0);

                for (int x = 0; x < 16; x++) {
                    for (int y = 0; y < 16; y++) {
                        for (int z = 0; z < 16; z++) {
                            final int flatBlock = palette.idAt(x, y, z);
                            if (this.trackAll || this.toTrack.contains(flatBlock >> 4)) {
                                copyPalette.setIdAt(x, y, z, flatBlock);
                            }
                        }
                    }
                }
            }
        }

        // Remap
        for (int i = 0; i < chunk.getSections().length; i++) {
            final ChunkSection section = chunk.getSections()[i];
            if (section == null) continue;
            final DataPalette palette = section.palette(PaletteType.BLOCKS);

            for (Int2IntMap.Entry entry : this.replacements.int2IntEntrySet()) {
                palette.replaceId(entry.getIntKey(), entry.getIntValue());
            }

            if (!this.hasRemappableBlocks(palette)) continue;

            for (int x = 0; x < 16; x++) {
                for (int y = 0; y < 16; y++) {
                    for (int z = 0; z < 16; z++) {
                        final int flatBlock = palette.idAt(x, y, z);
                        if (this.trackAll || this.toTrack.contains(flatBlock >> 4)) {
                            final IdAndData block = IdAndData.fromRawData(flatBlock);
                            this.remapBlock(block, x + (chunk.getX() << 4), y + (i * 16), z + (chunk.getZ() << 4));
                            final int newFlatBlock = block.toRawData();
                            if (newFlatBlock != flatBlock) {
                                palette.setIdAt(x, y, z, newFlatBlock);
                            }
                        }
                    }
                }
            }

            this.postRemap(palette);
        }
    }

    public void trackAndRemap(final BlockPosition position, final IdAndData block) {
        final int x = position.x();
        final int y = position.y();
        final int z = position.z();
        final Chunk chunk = this.chunks.get(new ChunkCoord(x >> 4, z >> 4));

        if (chunk != null && y >= 0 && y >> 4 < chunk.getSections().length) {
            ChunkSection section = chunk.getSections()[y >> 4];
            if (this.trackAll || this.toTrack.contains(block.getId())) {
                if (section == null) {
                    section = chunk.getSections()[y >> 4] = new ChunkSectionImpl(false);
                    section.palette(PaletteType.BLOCKS).addId(0);
                }
                section.palette(PaletteType.BLOCKS).setIdAt(x & 15, y & 15, z & 15, block.toRawData());
            } else if (section != null) {
                section.palette(PaletteType.BLOCKS).setIdAt(x & 15, y & 15, z & 15, 0);
            }
        }

        if (this.replacements.containsKey(block.toRawData())) {
            final int newFlatBlock = this.replacements.get(block.toRawData());
            block.setId(newFlatBlock >> 4);
            block.setData(newFlatBlock & 15);
        }
        if (this.trackAll || this.toTrack.contains(block.getId())) {
            this.remapBlock(block, x, y, z);
        }
    }

    public void remapBlockParticle(final IdAndData block) {
        if (this.replacements.containsKey(block.toRawData())) {
            final int newFlatBlock = this.replacements.get(block.toRawData());
            block.setId(newFlatBlock >> 4);
            block.setData(newFlatBlock & 15);
        }
        if (this.trackAll || this.toTrack.contains(block.getId())) {
            this.remapBlock(block, 0, -16, 0);
        }
    }

    public void clear() {
        this.chunks.clear();
    }

    public boolean isChunkLoaded(final int chunkX, final int chunkZ) {
        return this.chunks.containsKey(new ChunkCoord(chunkX, chunkZ));
    }

    public IdAndData getBlockNotNull(final BlockPosition position) {
        return this.getBlockNotNull(position.x(), position.y(), position.z());
    }

    public IdAndData getBlockNotNull(final int x, final int y, final int z) {
        IdAndData block = this.getBlock(x, y, z);
        if (block == null) block = new IdAndData(0, 0);
        return block;
    }

    public IdAndData getBlock(final BlockPosition position) {
        return this.getBlock(position.x(), position.y(), position.z());
    }

    public IdAndData getBlock(final int x, final int y, final int z) {
        final Chunk chunk = this.chunks.get(new ChunkCoord(x >> 4, z >> 4));
        if (chunk != null) {
            if (y < 0 || y >> 4 > chunk.getSections().length - 1) return null;
            final ChunkSection section = chunk.getSections()[y >> 4];
            if (section != null) {
                return IdAndData.fromRawData(section.palette(PaletteType.BLOCKS).idAt(x & 15, y & 15, z & 15));
            }
        }
        return null;
    }

    protected void registerReplacement(final IdAndData from, final IdAndData to) {
        this.replacements.put(from.toRawData(), to.toRawData());
    }

    protected void remapBlock(final IdAndData block, final int x, final int y, final int z) {
    }

    protected void postRemap(final DataPalette palette) {
    }

    private boolean hasRemappableBlocks(final DataPalette palette) {
        if (this.trackAll) return true;
        if (this.toTrack.isEmpty()) return false;

        boolean hasTrackableBlocks = false;
        for (int i = 0; i < palette.size(); i++) {
            if (this.toTrack.contains(palette.idByIndex(i) >> 4)) {
                hasTrackableBlocks = true;
            }
        }
        return hasTrackableBlocks;
    }

}
