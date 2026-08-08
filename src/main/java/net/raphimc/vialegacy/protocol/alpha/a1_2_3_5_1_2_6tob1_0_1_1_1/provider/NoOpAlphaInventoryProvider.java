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
package net.raphimc.vialegacy.protocol.alpha.a1_2_3_5_1_2_6tob1_0_1_1_1.provider;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.item.Item;
import net.raphimc.vialegacy.protocol.alpha.a1_2_3_5_1_2_6tob1_0_1_1_1.storage.InventoryStorage;

public class NoOpAlphaInventoryProvider extends AlphaInventoryProvider {

    @Override
    public boolean usesInventoryTracker() {
        return false;
    }

    @Override
    public Item[] getMainInventoryItems(final UserConnection user) {
        return new Item[36];
    }

    @Override
    public Item[] getCraftingInventoryItems(final UserConnection user) {
        return new Item[4];
    }

    @Override
    public Item[] getArmorInventoryItems(final UserConnection user) {
        return new Item[4];
    }

    @Override
    public Item[] getContainerItems(final UserConnection user) {
        final InventoryStorage inventoryStorage = user.get(InventoryStorage.class);
        return inventoryStorage.getContainers().get(inventoryStorage.getOpenContainerPos());
    }

    @Override
    public void addToInventory(final UserConnection user, final Item item) {
    }

}
