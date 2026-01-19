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

package net.raphimc.vialegacy.protocol.release.r1_2_1_3tor1_2_4_5.rewriter;

import net.raphimc.vialegacy.api.remapper.LegacyItemRewriter;
import net.raphimc.vialegacy.protocol.release.r1_2_1_3tor1_2_4_5.Protocolr1_2_1_3Tor1_2_4_5;
import net.raphimc.vialegacy.protocol.release.r1_2_1_3tor1_2_4_5.packet.ClientboundPackets1_2_1;
import net.raphimc.vialegacy.protocol.release.r1_2_4_5tor1_3_1_2.packet.ServerboundPackets1_2_4;
import net.raphimc.vialegacy.protocol.release.r1_2_4_5tor1_3_1_2.types.Types1_2_4;

public class ItemRewriter extends LegacyItemRewriter<ClientboundPackets1_2_1, ServerboundPackets1_2_4, Protocolr1_2_1_3Tor1_2_4_5> {

    public ItemRewriter(final Protocolr1_2_1_3Tor1_2_4_5 protocol) {
        super(protocol, "1.2.3", Types1_2_4.NBT_ITEM, Types1_2_4.NBT_ITEM_ARRAY);

        this.addNonExistentItem(5, 1, 3);
        this.addNonExistentItem(24, 1, 2);
    }

    @Override
    protected void registerPackets() {
        this.registerCreativeInventoryAction(ServerboundPackets1_2_4.SET_CREATIVE_MODE_SLOT);
    }

}
