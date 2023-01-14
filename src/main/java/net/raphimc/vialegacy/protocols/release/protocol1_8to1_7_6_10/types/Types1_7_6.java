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
package net.raphimc.vialegacy.protocols.release.protocol1_8to1_7_6_10.types;

import com.viaversion.viaversion.api.minecraft.BlockChangeRecord;
import com.viaversion.viaversion.api.minecraft.Position;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.type.types.minecraft.MetaListType;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;

import java.util.List;

public class Types1_7_6 {

    public static final Type<int[]> INT_ARRAY = new IntArrayType();

    public static final Type<CompoundTag> NBT = new NBTType(false);
    public static final Type<CompoundTag> COMPRESSED_NBT = new NBTType(true);

    public static final Type<Item> ITEM = new ItemType(false);
    public static final Type<Item> COMPRESSED_ITEM = new ItemType(true);

    public static final Type<Item[]> ITEM_ARRAY = new ItemArrayType<>(ITEM);
    public static final Type<Item[]> COMPRESSED_ITEM_ARRAY = new ItemArrayType<>(COMPRESSED_ITEM);

    public static final Type<Metadata> METADATA = new MetadataType();
    public static final Type<List<Metadata>> METADATA_LIST = new MetaListType(METADATA);

    public static final Type<BlockChangeRecord[]> BLOCK_CHANGE_RECORD_ARRAY = new BlockChangeRecordArrayType();

    public static final Type<Position> POSITION_BYTE = new PositionVarYType<>(Type.BYTE, i -> (byte) i);
    public static final Type<Position> POSITION_UBYTE = new PositionVarYType<>(Type.UNSIGNED_BYTE, i -> (short) i);
    public static final Type<Position> POSITION_SHORT = new PositionVarYType<>(Type.SHORT, i -> (short) i);
    public static final Type<Position> POSITION_INT = new PositionVarYType<>(Type.INT, i -> i);

}
