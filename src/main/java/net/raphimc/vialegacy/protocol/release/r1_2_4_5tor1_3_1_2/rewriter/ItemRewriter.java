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
package net.raphimc.vialegacy.protocol.release.r1_2_4_5tor1_3_1_2.rewriter;

import net.raphimc.vialegacy.api.remapper.LegacyItemRewriter;
import net.raphimc.vialegacy.protocol.release.r1_2_4_5tor1_3_1_2.Protocolr1_2_4_5Tor1_3_1_2;
import net.raphimc.vialegacy.protocol.release.r1_2_4_5tor1_3_1_2.packet.ClientboundPackets1_2_4;
import net.raphimc.vialegacy.protocol.release.r1_2_4_5tor1_3_1_2.types.Types1_2_4;
import net.raphimc.vialegacy.protocol.release.r1_3_1_2tor1_4_2.packet.ServerboundPackets1_3_1;
import net.raphimc.vialegacy.protocol.release.r1_7_6_10tor1_8.types.Types1_7_6;

public class ItemRewriter extends LegacyItemRewriter<ClientboundPackets1_2_4, ServerboundPackets1_3_1, Protocolr1_2_4_5Tor1_3_1_2> {

    public ItemRewriter(final Protocolr1_2_4_5Tor1_3_1_2 protocol) {
        super(protocol, "1.2.5", Types1_2_4.NBT_ITEM, Types1_2_4.NBT_ITEM_ARRAY, Types1_7_6.ITEM, Types1_7_6.ITEM_ARRAY);

        this.addNonExistentItemRange(126, 136);
        this.addNonExistentItem(322, 1);
        this.addNonExistentItemRange(386, 388);
    }

    @Override
    protected void registerPackets() {
        this.registerCreativeInventoryAction(ServerboundPackets1_3_1.SET_CREATIVE_MODE_SLOT);
    }

}
