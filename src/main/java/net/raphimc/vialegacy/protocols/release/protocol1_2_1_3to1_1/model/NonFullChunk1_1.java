/*
 * This file is part of ViaProtocolHack - https://github.com/RaphiMC/ViaProtocolHack
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
package net.raphimc.vialegacy.protocols.release.protocol1_2_1_3to1_1.model;

import com.viaversion.viaversion.api.minecraft.BlockChangeRecord;
import com.viaversion.viaversion.api.minecraft.BlockChangeRecord1_8;
import com.viaversion.viaversion.api.minecraft.Position;
import com.viaversion.viaversion.api.minecraft.chunks.BaseChunk;
import com.viaversion.viaversion.api.minecraft.chunks.ChunkSection;
import com.viaversion.viaversion.api.minecraft.chunks.PaletteType;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;

import java.util.ArrayList;
import java.util.List;

public class NonFullChunk1_1 extends BaseChunk {

    private final Position startPos;
    private final Position endPos;

    public NonFullChunk1_1(int x, int z, int bitmask, ChunkSection[] sections, Position startPos, Position endPos) {
        super(x, z, false, false, bitmask, sections, null, new CompoundTag(), new ArrayList<>());

        this.startPos = startPos;
        this.endPos = endPos;
    }

    public Position getStartPos() {
        return this.startPos;
    }

    public Position getEndPos() {
        return this.endPos;
    }

    public List<BlockChangeRecord> asBlockChangeRecords() {
        final List<BlockChangeRecord> blockChangeRecords = new ArrayList<>();

        for (int y = this.startPos.y(); y < this.endPos.y(); y++) {
            final ChunkSection section = this.getSections()[y >> 4];
            for (int x = this.startPos.x(); x < this.endPos.x(); x++) {
                for (int z = this.startPos.z(); z < this.endPos.z(); z++) {
                    blockChangeRecords.add(new BlockChangeRecord1_8(x, y, z, section.palette(PaletteType.BLOCKS).idAt(x, y & 15, z)));
                }
            }
        }

        return blockChangeRecords;
    }

}
