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
package net.raphimc.vialegacy.util;

import com.viaversion.viaversion.libs.opennbt.tag.builtin.*;
import net.lenni0451.mcstructs.nbt.INbtTag;

import java.util.Map;

public class NbtConverter {

    public static Tag mcStructsToVia(final INbtTag nbtTag) {
        if (nbtTag == null) return null;

        if (nbtTag instanceof net.lenni0451.mcstructs.nbt.tags.ByteTag) {
            return new ByteTag(((net.lenni0451.mcstructs.nbt.tags.ByteTag) nbtTag).getValue());
        } else if (nbtTag instanceof net.lenni0451.mcstructs.nbt.tags.ShortTag) {
            return new ShortTag(((net.lenni0451.mcstructs.nbt.tags.ShortTag) nbtTag).getValue());
        } else if (nbtTag instanceof net.lenni0451.mcstructs.nbt.tags.IntTag) {
            return new IntTag(((net.lenni0451.mcstructs.nbt.tags.IntTag) nbtTag).getValue());
        } else if (nbtTag instanceof net.lenni0451.mcstructs.nbt.tags.LongTag) {
            return new LongTag(((net.lenni0451.mcstructs.nbt.tags.LongTag) nbtTag).getValue());
        } else if (nbtTag instanceof net.lenni0451.mcstructs.nbt.tags.FloatTag) {
            return new FloatTag(((net.lenni0451.mcstructs.nbt.tags.FloatTag) nbtTag).getValue());
        } else if (nbtTag instanceof net.lenni0451.mcstructs.nbt.tags.DoubleTag) {
            return new DoubleTag(((net.lenni0451.mcstructs.nbt.tags.DoubleTag) nbtTag).getValue());
        } else if (nbtTag instanceof net.lenni0451.mcstructs.nbt.tags.ByteArrayTag) {
            return new ByteArrayTag(((net.lenni0451.mcstructs.nbt.tags.ByteArrayTag) nbtTag).getValue());
        } else if (nbtTag instanceof net.lenni0451.mcstructs.nbt.tags.StringTag) {
            return new StringTag(((net.lenni0451.mcstructs.nbt.tags.StringTag) nbtTag).getValue());
        } else if (nbtTag instanceof net.lenni0451.mcstructs.nbt.tags.ListTag<?>) {
            final ListTag list = new ListTag();
            for (INbtTag t : ((net.lenni0451.mcstructs.nbt.tags.ListTag<?>) nbtTag).getValue()) {
                list.add(mcStructsToVia(t));
            }
            return list;
        } else if (nbtTag instanceof net.lenni0451.mcstructs.nbt.tags.CompoundTag) {
            final Map<String, INbtTag> values = ((net.lenni0451.mcstructs.nbt.tags.CompoundTag) nbtTag).getValue();
            final CompoundTag compound = new CompoundTag();
            for (Map.Entry<String, INbtTag> entry : values.entrySet()) {
                compound.put(entry.getKey(), mcStructsToVia(entry.getValue()));
            }
            return compound;
        } else if (nbtTag instanceof net.lenni0451.mcstructs.nbt.tags.IntArrayTag) {
            return new IntArrayTag(((net.lenni0451.mcstructs.nbt.tags.IntArrayTag) nbtTag).getValue());
        } else if (nbtTag instanceof net.lenni0451.mcstructs.nbt.tags.LongArrayTag) {
            return new LongArrayTag(((net.lenni0451.mcstructs.nbt.tags.LongArrayTag) nbtTag).getValue());
        } else {
            throw new IllegalArgumentException("Unsupported tag type: " + nbtTag.getClass().getName());
        }
    }

    public static INbtTag viaToMcStructs(final Tag tag) {
        if (tag == null) return null;

        if (tag instanceof ByteTag) {
            return new net.lenni0451.mcstructs.nbt.tags.ByteTag(((ByteTag) tag).asByte());
        } else if (tag instanceof ShortTag) {
            return new net.lenni0451.mcstructs.nbt.tags.ShortTag(((ShortTag) tag).asShort());
        } else if (tag instanceof IntTag) {
            return new net.lenni0451.mcstructs.nbt.tags.IntTag(((IntTag) tag).asInt());
        } else if (tag instanceof LongTag) {
            return new net.lenni0451.mcstructs.nbt.tags.LongTag(((LongTag) tag).asLong());
        } else if (tag instanceof FloatTag) {
            return new net.lenni0451.mcstructs.nbt.tags.FloatTag(((FloatTag) tag).asFloat());
        } else if (tag instanceof DoubleTag) {
            return new net.lenni0451.mcstructs.nbt.tags.DoubleTag(((DoubleTag) tag).asDouble());
        } else if (tag instanceof ByteArrayTag) {
            return new net.lenni0451.mcstructs.nbt.tags.ByteArrayTag(((ByteArrayTag) tag).getValue());
        } else if (tag instanceof StringTag) {
            return new net.lenni0451.mcstructs.nbt.tags.StringTag(((StringTag) tag).getValue());
        } else if (tag instanceof ListTag) {
            final net.lenni0451.mcstructs.nbt.tags.ListTag<INbtTag> list = new net.lenni0451.mcstructs.nbt.tags.ListTag<>();
            for (Tag t : ((ListTag) tag).getValue()) {
                list.add(viaToMcStructs(t));
            }
            return list;
        } else if (tag instanceof CompoundTag) {
            final Map<String, Tag> values = ((CompoundTag) tag).getValue();
            final net.lenni0451.mcstructs.nbt.tags.CompoundTag compound = new net.lenni0451.mcstructs.nbt.tags.CompoundTag();
            for (Map.Entry<String, Tag> entry : values.entrySet()) {
                compound.add(entry.getKey(), viaToMcStructs(entry.getValue()));
            }
            return compound;
        } else if (tag instanceof IntArrayTag) {
            return new net.lenni0451.mcstructs.nbt.tags.IntArrayTag(((IntArrayTag) tag).getValue());
        } else if (tag instanceof LongArrayTag) {
            return new net.lenni0451.mcstructs.nbt.tags.LongArrayTag(((LongArrayTag) tag).getValue());
        } else {
            throw new IllegalArgumentException("Unsupported tag type: " + tag.getClass().getName());
        }
    }

}
