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
package net.raphimc.vialegacy.protocols.release.protocol1_4_2to1_3_1_2.rewriter;

import com.viaversion.viaversion.api.minecraft.item.Item;
import net.raphimc.vialegacy.api.data.ItemList1_6;
import net.raphimc.vialegacy.api.remapper.LegacyItemRewriter;
import net.raphimc.vialegacy.protocols.release.protocol1_4_2to1_3_1_2.Protocol1_4_2to1_3_1_2;
import net.raphimc.vialegacy.protocols.release.protocol1_6_1to1_5_2.ServerboundPackets1_5_2;
import net.raphimc.vialegacy.protocols.release.protocol1_8to1_7_6_10.types.Types1_7_6;

public class ItemRewriter extends LegacyItemRewriter<Protocol1_4_2to1_3_1_2> {

    public ItemRewriter(final Protocol1_4_2to1_3_1_2 protocol) {
        super(protocol, "1.3.2", Types1_7_6.ITEM, Types1_7_6.ITEM_ARRAY);

        this.addNonExistentItemRange(137, 145);
        this.addNonExistentItemRange(389, 400);
        this.addNonExistentItem(383, 65);
        this.addNonExistentItem(383, 66);
        this.addNonExistentItems(422);
    }

    @Override
    protected void registerPackets() {
        this.registerCreativeInventoryAction(ServerboundPackets1_5_2.CREATIVE_INVENTORY_ACTION);
    }

    @Override
    public Item handleItemToServer(Item item) {
        if (item != null && item.identifier() == ItemList1_6.emptyMap.itemID) {
            item.setIdentifier(ItemList1_6.map.itemID);
        }

        return super.handleItemToServer(item);
    }

}
