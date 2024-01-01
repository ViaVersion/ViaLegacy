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
package net.raphimc.vialegacy.protocols.classic.protocola1_0_15toc0_28_30.types;

import com.viaversion.viaversion.api.type.Type;
import io.netty.buffer.ByteBuf;

public class ByteArrayType extends Type<byte[]> {

    public ByteArrayType() {
        super(byte[].class);
    }

    public void write(ByteBuf buffer, byte[] array) throws Exception {
        if (array.length != 1024) throw new IllegalStateException("Byte array needs to be exactly 1024 bytes long");

        buffer.writeBytes(array);
    }

    public byte[] read(ByteBuf buffer) throws Exception {
        final byte[] array = new byte[1024];
        buffer.readBytes(array);
        return array;
    }

}
