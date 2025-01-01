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
package net.raphimc.vialegacy.protocol.classic.c0_28_30toa1_0_15.types;

import com.viaversion.viaversion.api.type.Type;
import io.netty.buffer.ByteBuf;
import net.raphimc.vialegacy.protocol.classic.c0_28_30toa1_0_15.data.Cp437String;

import java.util.Arrays;

public class StringType extends Type<String> {

    public StringType() {
        super(String.class);
    }

    public String read(ByteBuf buffer) {
        final byte[] stringBytes = new byte[64];
        buffer.readBytes(stringBytes);
        return Cp437String.fromBytes(stringBytes).trim();
    }

    public void write(ByteBuf buffer, String s) {
        final byte[] bytes = new byte[64];
        final byte[] stringBytes = Cp437String.toBytes(s);

        Arrays.fill(bytes, (byte) 32);
        System.arraycopy(stringBytes, 0, bytes, 0, Math.min(bytes.length, stringBytes.length));

        buffer.writeBytes(bytes);
    }

}
