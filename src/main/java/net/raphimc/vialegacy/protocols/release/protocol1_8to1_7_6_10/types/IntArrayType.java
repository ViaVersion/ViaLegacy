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
package net.raphimc.vialegacy.protocols.release.protocol1_8to1_7_6_10.types;

import com.viaversion.viaversion.api.type.Type;
import io.netty.buffer.ByteBuf;

public class IntArrayType extends Type<int[]> {

    public IntArrayType() {
        super(int[].class);
    }

    @Override
    public int[] read(ByteBuf buffer) {
        final byte length = buffer.readByte();
        final int[] array = new int[length];

        for (byte i = 0; i < length; i++) {
            array[i] = buffer.readInt();
        }
        return array;
    }

    @Override
    public void write(ByteBuf buffer, int[] array) {
        buffer.writeByte(array.length);
        for (int i : array) buffer.writeInt(i);
    }

}
