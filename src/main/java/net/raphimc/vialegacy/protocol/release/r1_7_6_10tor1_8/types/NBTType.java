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

package net.raphimc.vialegacy.protocol.release.r1_7_6_10tor1_8.types;

import com.viaversion.nbt.io.NBTIO;
import com.viaversion.nbt.limiter.TagLimiter;
import com.viaversion.nbt.tag.CompoundTag;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.type.types.misc.NamedCompoundTagType;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class NBTType extends Type<CompoundTag> {

    public NBTType() {
        super(CompoundTag.class);
    }

    @Override
    public CompoundTag read(ByteBuf buffer) {
        final short length = buffer.readShort();
        if (length < 0) {
            return null;
        }

        final ByteBuf data = buffer.readSlice(length);
        try (InputStream in = new GZIPInputStream(new ByteBufInputStream(data))) {
            return NBTIO.readTag(new DataInputStream(in), TagLimiter.create(NamedCompoundTagType.MAX_NBT_BYTES, NamedCompoundTagType.MAX_NESTING_LEVEL), true, CompoundTag.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void write(ByteBuf buffer, CompoundTag nbt) {
        if (nbt == null) {
            buffer.writeShort(-1);
            return;
        }

        final ByteBuf data = buffer.alloc().buffer();
        try {
            try (OutputStream out = new GZIPOutputStream(new ByteBufOutputStream(data))) {
                NBTIO.writeTag(new DataOutputStream(out), nbt, true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            buffer.writeShort(data.readableBytes());
            buffer.writeBytes(data);
        } finally {
            data.release();
        }
    }

}
