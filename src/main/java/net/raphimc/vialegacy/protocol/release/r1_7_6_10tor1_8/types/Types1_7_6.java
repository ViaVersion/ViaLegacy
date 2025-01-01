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
package net.raphimc.vialegacy.protocol.release.r1_7_6_10tor1_8.types;

import com.viaversion.nbt.tag.CompoundTag;
import com.viaversion.viaversion.api.minecraft.BlockChangeRecord;
import com.viaversion.viaversion.api.minecraft.BlockPosition;
import com.viaversion.viaversion.api.minecraft.Environment;
import com.viaversion.viaversion.api.minecraft.chunks.Chunk;
import com.viaversion.viaversion.api.minecraft.entitydata.EntityData;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.type.Types;
import com.viaversion.viaversion.api.type.types.entitydata.EntityDataListType;

import java.util.List;

public class Types1_7_6 {

    public static final Type<int[]> INT_ARRAY = new IntArrayType();

    public static final Type<CompoundTag> NBT = new NBTType();

    public static final Type<Item> ITEM = new ItemType();
    public static final Type<Item[]> ITEM_ARRAY = new ItemArrayType<>(ITEM);

    public static final Type<EntityData> ENTITY_DATA = new EntityDataType();
    public static final Type<List<EntityData>> ENTITY_DATA_LIST = new EntityDataListType(ENTITY_DATA);

    public static final Type<BlockChangeRecord[]> BLOCK_CHANGE_RECORD_ARRAY = new BlockChangeRecordArrayType();

    public static final Type<BlockPosition> BLOCK_POSITION_BYTE = new BlockPositionVarYType<>(Types.BYTE, i -> (byte) i);
    public static final Type<BlockPosition> BLOCK_POSITION_UBYTE = new BlockPositionVarYType<>(Types.UNSIGNED_BYTE, i -> (short) i);
    public static final Type<BlockPosition> BLOCK_POSITION_SHORT = new BlockPositionVarYType<>(Types.SHORT, i -> (short) i);
    public static final Type<BlockPosition> BLOCK_POSITION_INT = new BlockPositionVarYType<>(Types.INT, i -> i);

    public static final Type<Chunk> CHUNK_WITH_SKYLIGHT = new ChunkType(true);
    public static final Type<Chunk> CHUNK_WITHOUT_SKYLIGHT = new ChunkType(false);
    public static final Type<Chunk[]> CHUNK_BULK = new BulkChunkType();

    public static Type<Chunk> getChunk(final Environment dimension) {
        return dimension == Environment.NORMAL ? CHUNK_WITH_SKYLIGHT : CHUNK_WITHOUT_SKYLIGHT;
    }

}
