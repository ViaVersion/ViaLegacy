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
package net.raphimc.vialegacy.protocols.release.protocol1_3_1_2to1_2_4_5.rewriter;

import net.raphimc.vialegacy.api.remapper.LegacyItemRewriter;
import net.raphimc.vialegacy.protocols.release.protocol1_3_1_2to1_2_4_5.Protocol1_3_1_2to1_2_4_5;
import net.raphimc.vialegacy.protocols.release.protocol1_3_1_2to1_2_4_5.types.Types1_2_4;
import net.raphimc.vialegacy.protocols.release.protocol1_4_2to1_3_1_2.ServerboundPackets1_3_1;
import net.raphimc.vialegacy.protocols.release.protocol1_8to1_7_6_10.types.Types1_7_6;

public class ItemRewriter extends LegacyItemRewriter<Protocol1_3_1_2to1_2_4_5> {

    public ItemRewriter(final Protocol1_3_1_2to1_2_4_5 protocol) {
        super(protocol, "1.2.5", Types1_2_4.NBT_ITEM, Types1_2_4.NBT_ITEM_ARRAY, Types1_7_6.ITEM, Types1_7_6.ITEM_ARRAY);

        this.addNonExistentItemRange(126, 136);
        this.addNonExistentItem(322, 1);
        this.addNonExistentItemRange(386, 388);
    }

    @Override
    protected void registerPackets() {
        this.registerCreativeInventoryAction(ServerboundPackets1_3_1.CREATIVE_INVENTORY_ACTION);
    }

}
