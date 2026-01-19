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
package net.raphimc.vialegacy.protocol.release.r1_1tor1_2_1_3.model;

import com.viaversion.viaversion.api.minecraft.chunks.NibbleArray;

public class LegacyNibbleArray extends NibbleArray {

    private final int depthBits;
    private final int depthBitsPlusFour;

    public LegacyNibbleArray(final int length) {
        this(length, 7);
    }

    public LegacyNibbleArray(final int length, final int depthBits) {
        super(length);
        this.depthBits = depthBits;
        this.depthBitsPlusFour = depthBits + 4;
    }

    public LegacyNibbleArray(final byte[] handle, final int depthBits) {
        super(handle);
        this.depthBits = depthBits;
        this.depthBitsPlusFour = depthBits + 4;
    }

    @Override
    public byte get(int x, int y, int z) {
        return this.get(this.index(x, y, z));
    }

    @Override
    public void set(int x, int y, int z, int value) {
        this.set(this.index(x, y, z), value);
    }

    public int index(final int x, final int y, final int z) {
        return x << this.depthBitsPlusFour | z << this.depthBits | y;
    }

}
