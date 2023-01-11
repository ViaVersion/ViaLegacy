/*
 * This file is part of ViaProtocolHack - https://github.com/RaphiMC/ViaProtocolHack
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
package net.raphimc.vialegacy.protocols.alpha.protocolb1_0_1_1_1toa1_2_3_5_1_2_6.storage;

import com.viaversion.viaversion.api.connection.StoredObject;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.Position;
import com.viaversion.viaversion.api.minecraft.item.Item;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class InventoryStorage extends StoredObject {

    public static final byte WORKBENCH_WID = 33;
    public static final byte FURNACE_WID = 44;
    public static final byte CHEST_WID = 55;

    public Item handItem = null;
    public Item[] mainInventory = null;
    public Item[] craftingInventory = null;
    public Item[] armorInventory = null;

    public final Map<Position, Item[]> containers = new HashMap<>();

    public Position openContainerPos = null;
    public short selectedHotbarSlot = 0;

    public InventoryStorage(UserConnection user) {
        super(user);
        this.resetPlayerInventory();
    }

    public void unload(final int chunkX, final int chunkZ) {
        final Iterator<Position> it = this.containers.keySet().iterator();
        while (it.hasNext()) {
            final Position entry = it.next();
            final int x = entry.x() >> 4;
            final int z = entry.z() >> 4;

            if (chunkX == x && chunkZ == z) {
                it.remove();
            }
        }
    }

    public void resetPlayerInventory() {
        // alpha keeps handItem value after respawn
        this.mainInventory = new Item[37];
        this.craftingInventory = new Item[4];
        this.armorInventory = new Item[4];
        this.openContainerPos = null;
    }

}
