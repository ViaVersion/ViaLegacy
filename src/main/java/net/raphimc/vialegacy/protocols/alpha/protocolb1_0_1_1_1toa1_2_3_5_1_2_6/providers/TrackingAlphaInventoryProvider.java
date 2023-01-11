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
package net.raphimc.vialegacy.protocols.alpha.protocolb1_0_1_1_1toa1_2_3_5_1_2_6.providers;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.item.Item;
import net.raphimc.vialegacy.protocols.alpha.protocolb1_0_1_1_1toa1_2_3_5_1_2_6.storage.AlphaInventoryTracker;

import static net.raphimc.vialegacy.protocols.alpha.protocolb1_0_1_1_1toa1_2_3_5_1_2_6.Protocolb1_0_1_1_1toa1_2_3_5_1_2_6.copyItems;

public class TrackingAlphaInventoryProvider extends AlphaInventoryProvider {

    @Override
    public boolean usesInventoryTracker() {
        return true;
    }

    @Override
    public Item[] getMainInventoryItems(UserConnection user) {
        return copyItems(user.get(AlphaInventoryTracker.class).getMainInventory());
    }

    @Override
    public Item[] getCraftingInventoryItems(UserConnection user) {
        return copyItems(user.get(AlphaInventoryTracker.class).getCraftingInventory());
    }

    @Override
    public Item[] getArmorInventoryItems(UserConnection user) {
        return copyItems(user.get(AlphaInventoryTracker.class).getArmorInventory());
    }

    @Override
    public Item[] getContainerItems(UserConnection user) {
        return copyItems(user.get(AlphaInventoryTracker.class).getOpenContainerItems());
    }

    @Override
    public void addToInventory(UserConnection user, Item item) {
        user.get(AlphaInventoryTracker.class).addItem(item);
    }

}
