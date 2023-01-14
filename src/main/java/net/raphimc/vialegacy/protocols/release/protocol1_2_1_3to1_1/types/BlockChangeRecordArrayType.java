/*
 * This file is part of ViaLegacy - https://github.com/RaphiMC/ViaLegacy
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
package net.raphimc.vialegacy.protocols.release.protocol1_2_1_3to1_1.types;

import com.viaversion.viaversion.api.minecraft.BlockChangeRecord;
import com.viaversion.viaversion.api.minecraft.BlockChangeRecord1_8;
import com.viaversion.viaversion.api.type.Type;
import io.netty.buffer.ByteBuf;
import net.raphimc.vialegacy.api.model.IdAndData;

public class BlockChangeRecordArrayType extends Type<BlockChangeRecord[]> {

    public BlockChangeRecordArrayType() {
        super(BlockChangeRecord[].class);
    }

    @Override
    public BlockChangeRecord[] read(ByteBuf buffer) throws Exception {
        final int length = buffer.readUnsignedShort();
        final short[] positions = new short[length];
        final short[] blocks = new short[length];
        final byte[] metas = new byte[length];
        for (int i = 0; i < length; i++) {
            positions[i] = buffer.readShort();
        }
        for (int i = 0; i < length; i++) {
            blocks[i] = buffer.readUnsignedByte();
        }
        for (int i = 0; i < length; i++) {
            metas[i] = buffer.readByte();
        }

        final BlockChangeRecord[] blockChangeRecords = new BlockChangeRecord[length];
        for (int i = 0; i < length; i++) {
            blockChangeRecords[i] = new BlockChangeRecord1_8(positions[i] >> 12 & 15, positions[i] & 255, positions[i] >> 8 & 15, IdAndData.toCompressedData(blocks[i], metas[i]));
        }
        return blockChangeRecords;
    }

    @Override
    public void write(ByteBuf buffer, BlockChangeRecord[] records) throws Exception {
        buffer.writeShort(records.length);
        for (BlockChangeRecord record : records) {
            buffer.writeShort(record.getSectionX() << 12 | record.getSectionZ() << 8 | record.getY(-1));
        }
        for (BlockChangeRecord record : records) {
            buffer.writeByte(record.getBlockId() >> 4);
        }
        for (BlockChangeRecord record : records) {
            buffer.writeByte(record.getBlockId() & 15);
        }
    }

}
