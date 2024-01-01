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
package net.raphimc.vialegacy.api.remapper;

import com.viaversion.viaversion.api.minecraft.BlockChangeRecord;
import com.viaversion.viaversion.api.minecraft.chunks.Chunk;
import com.viaversion.viaversion.api.minecraft.chunks.ChunkSection;
import com.viaversion.viaversion.api.minecraft.chunks.DataPalette;
import com.viaversion.viaversion.api.minecraft.chunks.PaletteType;
import com.viaversion.viaversion.libs.fastutil.ints.Int2IntMap;
import com.viaversion.viaversion.libs.fastutil.ints.Int2IntOpenHashMap;
import net.raphimc.vialegacy.api.model.IdAndData;

public abstract class AbstractBlockRemapper {

    private final Int2IntMap REPLACEMENTS = new Int2IntOpenHashMap();

    protected void registerReplacement(final int from, final int to) {
        this.REPLACEMENTS.put(from, to);
    }

    protected void registerReplacement(final IdAndData from, final IdAndData to) {
        this.REPLACEMENTS.put(from.toCompressedData(), to.toCompressedData());
    }

    public void remapChunk(final Chunk chunk) {
        for (int i = 0; i < chunk.getSections().length; i++) {
            final ChunkSection section = chunk.getSections()[i];
            if (section == null) continue;
            final DataPalette palette = section.palette(PaletteType.BLOCKS);

            for (Int2IntMap.Entry entry : this.REPLACEMENTS.int2IntEntrySet()) {
                palette.replaceId(entry.getIntKey(), entry.getIntValue());
            }
        }
    }

    public void remapBlockChangeRecords(final BlockChangeRecord[] blockChangeRecords) {
        for (final BlockChangeRecord record : blockChangeRecords) {
            final int id = record.getBlockId();
            if (this.REPLACEMENTS.containsKey(id)) {
                record.setBlockId(this.REPLACEMENTS.get(id));
            }
        }
    }

    public void remapBlock(final IdAndData block) {
        if (this.REPLACEMENTS.containsKey(block.toCompressedData())) {
            final int replacement = this.REPLACEMENTS.get(block.toCompressedData());
            block.id = replacement >> 4;
            block.data = replacement & 15;
        }
    }

    public int remapBlock(final int id) {
        return this.REPLACEMENTS.getOrDefault(id, id);
    }

}
