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
package net.raphimc.vialegacy.protocols.release.protocol1_2_4_5to1_2_1_3.rewriter;

import net.raphimc.vialegacy.api.remapper.LegacyItemRewriter;
import net.raphimc.vialegacy.protocols.release.protocol1_2_4_5to1_2_1_3.Protocol1_2_4_5to1_2_1_3;
import net.raphimc.vialegacy.protocols.release.protocol1_3_1_2to1_2_4_5.ServerboundPackets1_2_4;
import net.raphimc.vialegacy.protocols.release.protocol1_3_1_2to1_2_4_5.types.Types1_2_4;

public class ItemRewriter extends LegacyItemRewriter<Protocol1_2_4_5to1_2_1_3> {

    public ItemRewriter(final Protocol1_2_4_5to1_2_1_3 protocol) {
        super(protocol, "1.2.3");

        this.addNonExistentItem(5, 1, 3);
        this.addNonExistentItem(24, 1, 2);
    }

    @Override
    protected void registerPackets() {
        this.registerCreativeInventoryAction(ServerboundPackets1_2_4.CREATIVE_INVENTORY_ACTION, Types1_2_4.NBT_ITEM);
    }

}
