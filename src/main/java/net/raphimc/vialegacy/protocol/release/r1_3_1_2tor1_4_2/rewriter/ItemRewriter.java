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
package net.raphimc.vialegacy.protocol.release.r1_3_1_2tor1_4_2.rewriter;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.item.Item;
import net.raphimc.vialegacy.api.data.ItemList1_6;
import net.raphimc.vialegacy.api.remapper.LegacyItemRewriter;
import net.raphimc.vialegacy.protocol.release.r1_3_1_2tor1_4_2.Protocolr1_3_1_2Tor1_4_2;
import net.raphimc.vialegacy.protocol.release.r1_3_1_2tor1_4_2.packet.ClientboundPackets1_3_1;
import net.raphimc.vialegacy.protocol.release.r1_5_2tor1_6_1.packet.ServerboundPackets1_5_2;
import net.raphimc.vialegacy.protocol.release.r1_7_6_10tor1_8.types.Types1_7_6;

public class ItemRewriter extends LegacyItemRewriter<ClientboundPackets1_3_1, ServerboundPackets1_5_2, Protocolr1_3_1_2Tor1_4_2> {

    public ItemRewriter(final Protocolr1_3_1_2Tor1_4_2 protocol) {
        super(protocol, "1.3.2", Types1_7_6.ITEM, Types1_7_6.ITEM_ARRAY);

        this.addNonExistentItemRange(137, 145);
        this.addNonExistentItemRange(389, 400);
        this.addNonExistentItem(383, 65);
        this.addNonExistentItem(383, 66);
        this.addNonExistentItems(422);
    }

    @Override
    protected void registerPackets() {
        this.registerCreativeInventoryAction(ServerboundPackets1_5_2.SET_CREATIVE_MODE_SLOT);
    }

    @Override
    public Item handleItemToServer(final UserConnection user, final Item item) {
        if (item != null && item.identifier() == ItemList1_6.emptyMap.itemId()) {
            item.setIdentifier(ItemList1_6.map.itemId());
        }

        return super.handleItemToServer(user, item);
    }

}
