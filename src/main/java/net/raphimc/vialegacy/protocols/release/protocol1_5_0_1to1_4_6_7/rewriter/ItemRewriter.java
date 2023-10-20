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
package net.raphimc.vialegacy.protocols.release.protocol1_5_0_1to1_4_6_7.rewriter;

import net.raphimc.vialegacy.api.remapper.LegacyItemRewriter;
import net.raphimc.vialegacy.protocols.release.protocol1_5_0_1to1_4_6_7.Protocol1_5_0_1to1_4_6_7;
import net.raphimc.vialegacy.protocols.release.protocol1_6_1to1_5_2.ServerboundPackets1_5_2;
import net.raphimc.vialegacy.protocols.release.protocol1_8to1_7_6_10.types.Types1_7_6;

public class ItemRewriter extends LegacyItemRewriter<Protocol1_5_0_1to1_4_6_7> {

    public ItemRewriter(final Protocol1_5_0_1to1_4_6_7 protocol) {
        super(protocol, "1.4.7");

        this.addNonExistentItem(43, 7);
        this.addNonExistentItem(44, 7);
        this.addNonExistentItemRange(146, 158);
        this.addNonExistentItems(178);
        this.addNonExistentItemRange(404, 408);
    }

    @Override
    protected void registerPackets() {
        this.registerCreativeInventoryAction(ServerboundPackets1_5_2.CREATIVE_INVENTORY_ACTION, Types1_7_6.ITEM);
    }

}
