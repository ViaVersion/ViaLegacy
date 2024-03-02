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
package net.raphimc.vialegacy.protocols.release.protocol1_1to1_0_0_1.rewriter;

import net.raphimc.vialegacy.api.remapper.LegacyItemRewriter;
import net.raphimc.vialegacy.protocols.release.protocol1_1to1_0_0_1.Protocol1_1to1_0_0_1;
import net.raphimc.vialegacy.protocols.release.protocol1_2_1_3to1_1.ServerboundPackets1_1;
import net.raphimc.vialegacy.protocols.release.protocol1_3_1_2to1_2_4_5.types.Types1_2_4;

public class ItemRewriter extends LegacyItemRewriter<Protocol1_1to1_0_0_1> {

    public ItemRewriter(final Protocol1_1to1_0_0_1 protocol) {
        super(protocol, "1.0", Types1_2_4.NBT_ITEM, Types1_2_4.NBT_ITEM_ARRAY);

        this.addNonExistentItems(383);
    }

    @Override
    protected void registerPackets() {
        this.registerCreativeInventoryAction(ServerboundPackets1_1.CREATIVE_INVENTORY_ACTION);
    }

}
