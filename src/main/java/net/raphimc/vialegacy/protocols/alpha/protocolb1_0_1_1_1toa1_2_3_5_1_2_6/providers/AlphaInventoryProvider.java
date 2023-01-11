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
import com.viaversion.viaversion.api.platform.providers.Provider;
import net.raphimc.vialegacy.protocols.alpha.protocolb1_0_1_1_1toa1_2_3_5_1_2_6.storage.InventoryStorage;

public abstract class AlphaInventoryProvider implements Provider {

    public abstract boolean usesInventoryTracker();

    public Item getHandItem(final UserConnection user) {
        final InventoryStorage inventoryStorage = user.get(InventoryStorage.class);
        final Item[] inventory = this.getMainInventoryItems(user);
        return inventory[inventoryStorage.selectedHotbarSlot];
    }

    public abstract Item[] getMainInventoryItems(final UserConnection user);

    public abstract Item[] getCraftingInventoryItems(final UserConnection user);

    public abstract Item[] getArmorInventoryItems(final UserConnection user);

    public abstract Item[] getContainerItems(final UserConnection user);

    public abstract void addToInventory(final UserConnection user, final Item item);

}
