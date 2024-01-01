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

import com.viaversion.viaversion.api.minecraft.BlockChangeRecord;
import com.viaversion.viaversion.api.minecraft.BlockChangeRecord1_8;
import com.viaversion.viaversion.api.type.Type;
import io.netty.buffer.ByteBuf;
import net.raphimc.vialegacy.ViaLegacy;

import java.io.*;
import java.util.logging.Level;

public class BlockChangeRecordArrayType extends Type<BlockChangeRecord[]> {

    public BlockChangeRecordArrayType() {
        super(BlockChangeRecord[].class);
    }

    @Override
    public BlockChangeRecord[] read(ByteBuf buffer) throws Exception {
        final int length = buffer.readUnsignedShort();
        final int dataLength = buffer.readInt();
        final byte[] data = new byte[dataLength];
        buffer.readBytes(data);
        final DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(data));
        final BlockChangeRecord[] blockChangeRecords = new BlockChangeRecord[length];
        try {
            for (int i = 0; i < length; i++) {
                final short position = dataInputStream.readShort();
                final short blockId = dataInputStream.readShort();
                blockChangeRecords[i] = new BlockChangeRecord1_8(position >> 12 & 15, position & 255, position >> 8 & 15, blockId);
            }
        } catch (IOException e) {
            ViaLegacy.getPlatform().getLogger().log(Level.WARNING, "MultiBlockChange Record Array length mismatch: Expected " + dataLength + " bytes", e);
        }
        return blockChangeRecords;
    }

    @Override
    public void write(ByteBuf buffer, BlockChangeRecord[] records) throws Exception {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        for (BlockChangeRecord record : records) {
            dataOutputStream.writeShort((short) (record.getSectionX() << 12 | record.getSectionZ() << 8 | record.getY(-1)));
            dataOutputStream.writeShort((short) record.getBlockId());
        }
        final byte[] data = byteArrayOutputStream.toByteArray();
        buffer.writeShort(records.length);
        buffer.writeInt(data.length);
        buffer.writeBytes(data);
    }

}
