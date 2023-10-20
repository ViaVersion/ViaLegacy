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
package net.raphimc.vialegacy.protocols.release.protocol1_2_1_3to1_1.rewriter;

import net.raphimc.vialegacy.api.remapper.LegacyItemRewriter;
import net.raphimc.vialegacy.protocols.release.protocol1_2_1_3to1_1.Protocol1_2_1_3to1_1;
import net.raphimc.vialegacy.protocols.release.protocol1_2_4_5to1_2_1_3.ServerboundPackets1_2_1;
import net.raphimc.vialegacy.protocols.release.protocol1_3_1_2to1_2_4_5.types.Types1_2_4;

public class ItemRewriter extends LegacyItemRewriter<Protocol1_2_1_3to1_1> {

    public ItemRewriter(final Protocol1_2_1_3to1_1 protocol) {
        super(protocol, "1.1");

        this.addNonExistentItem(6, 3);
        this.addNonExistentItem(17, 3);
        this.addNonExistentItem(18, 3);
        this.addNonExistentItem(98, 3);
        this.addNonExistentItemRange(123, 124);
        this.addNonExistentItemRange(384, 385);
        this.addNonExistentItem(383, 98);
    }

    @Override
    protected void registerPackets() {
        this.registerCreativeInventoryAction(ServerboundPackets1_2_1.CREATIVE_INVENTORY_ACTION, Types1_2_4.NBT_ITEM);
    }

}
