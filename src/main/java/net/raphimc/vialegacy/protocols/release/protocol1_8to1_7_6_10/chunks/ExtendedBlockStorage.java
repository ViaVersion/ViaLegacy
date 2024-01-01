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
package net.raphimc.vialegacy.protocols.release.protocol1_8to1_7_6_10.chunks;

import com.viaversion.viaversion.api.minecraft.chunks.ChunkSection;
import com.viaversion.viaversion.api.minecraft.chunks.NibbleArray;

public class ExtendedBlockStorage {

    private final byte[] blockLSBArray;
    private NibbleArray blockMSBArray;
    private final NibbleArray blockMetadataArray;
    private final NibbleArray blockLightArray;
    private NibbleArray skyLightArray;

    public ExtendedBlockStorage(final boolean skylight) {
        this.blockLSBArray = new byte[4096];
        this.blockMetadataArray = new NibbleArray(this.blockLSBArray.length);
        this.blockLightArray = new NibbleArray(this.blockLSBArray.length);

        if (skylight) {
            this.skyLightArray = new NibbleArray(this.blockLSBArray.length);
        }
    }

    public int getBlockId(final int x, final int y, final int z) {
        int value = this.blockLSBArray[ChunkSection.index(x, y, z)] & 255;
        if (this.blockMSBArray != null) {
            value |= this.blockMSBArray.get(x, y, z) << 8;
        }
        return value;
    }

    public void setBlockId(final int x, final int y, final int z, final int value) {
        this.blockLSBArray[ChunkSection.index(x, y, z)] = (byte) (value & 255);
        if (value > 255) {
            this.getOrCreateBlockMSBArray().set(x, y, z, (value & 0xF00) >> 8);
        } else if (this.blockMSBArray != null) {
            this.blockMSBArray.set(x, y, z, 0);
        }
    }

    public int getBlockMetadata(final int x, final int y, final int z) {
        return this.blockMetadataArray.get(x, y, z);
    }

    public void setBlockMetadata(final int x, final int y, final int z, final int value) {
        this.blockMetadataArray.set(x, y, z, value);
    }

    public int getBlockLight(final int x, final int y, final int z) {
        return this.blockLightArray.get(x, y, z);
    }

    public void setBlockLight(final int x, final int y, int z, final int value) {
        this.blockLightArray.set(x, y, z, value);
    }

    public int getSkyLight(final int x, final int y, final int z) {
        return this.skyLightArray.get(x, y, z);
    }

    public void setSkyLight(final int x, final int y, final int z, final int value) {
        this.skyLightArray.set(x, y, z, value);
    }

    public boolean hasBlockMSBArray() {
        return this.blockMSBArray != null;
    }

    public byte[] getBlockLSBArray() {
        return this.blockLSBArray;
    }

    public NibbleArray getOrCreateBlockMSBArray() {
        if (this.blockMSBArray == null) {
            return this.blockMSBArray = new NibbleArray(this.blockLSBArray.length);
        }
        return this.blockMSBArray;
    }

    public NibbleArray getBlockMetadataArray() {
        return this.blockMetadataArray;
    }

    public NibbleArray getBlockLightArray() {
        return this.blockLightArray;
    }

    public NibbleArray getSkyLightArray() {
        return this.skyLightArray;
    }

}
