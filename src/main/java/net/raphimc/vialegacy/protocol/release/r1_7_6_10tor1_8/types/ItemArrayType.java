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

import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.type.Type;
import io.netty.buffer.ByteBuf;

public class ItemArrayType<T extends Item> extends Type<Item[]> {

    private final Type<T> itemType;

    public ItemArrayType(final Type<T> itemType) {
        super(Item[].class);
        this.itemType = itemType;
    }

    @Override
    public Item[] read(ByteBuf buffer) {
        final int amount = buffer.readShort();
        final Item[] items = new Item[amount];

        for (int i = 0; i < amount; i++) {
            items[i] = this.itemType.read(buffer);
        }
        return items;
    }

    @Override
    public void write(ByteBuf buffer, Item[] items) {
        buffer.writeShort(items.length);
        for (Item item : items) {
            this.itemType.write(buffer, (T) item);
        }
    }

}
