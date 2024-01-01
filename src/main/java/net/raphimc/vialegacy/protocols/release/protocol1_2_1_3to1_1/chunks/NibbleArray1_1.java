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
package net.raphimc.vialegacy.protocols.release.protocol1_2_1_3to1_1.chunks;

import com.viaversion.viaversion.api.minecraft.chunks.NibbleArray;

public class NibbleArray1_1 extends NibbleArray {

    private final int depthBits;
    private final int depthBitsPlusFour;

    public NibbleArray1_1(final int length) {
        this(length, 7);
    }

    public NibbleArray1_1(final byte[] handle) {
        this(handle, 7);
    }

    public NibbleArray1_1(final int length, final int depthBits) {
        super(length);
        this.depthBits = depthBits;
        this.depthBitsPlusFour = depthBits + 4;
    }

    public NibbleArray1_1(final byte[] handle, final int depthBits) {
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
