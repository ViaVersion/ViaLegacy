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

package net.raphimc.vialegacy.protocol.release.r1_1tor1_2_1_3.rewriter;

import net.raphimc.vialegacy.api.remapper.LegacyItemRewriter;
import net.raphimc.vialegacy.protocol.release.r1_1tor1_2_1_3.Protocolr1_1Tor1_2_1_3;
import net.raphimc.vialegacy.protocol.release.r1_1tor1_2_1_3.packet.ClientboundPackets1_1;
import net.raphimc.vialegacy.protocol.release.r1_2_1_3tor1_2_4_5.packet.ServerboundPackets1_2_1;
import net.raphimc.vialegacy.protocol.release.r1_2_4_5tor1_3_1_2.types.Types1_2_4;

public class ItemRewriter extends LegacyItemRewriter<ClientboundPackets1_1, ServerboundPackets1_2_1, Protocolr1_1Tor1_2_1_3> {

    public ItemRewriter(final Protocolr1_1Tor1_2_1_3 protocol) {
        super(protocol, "1.1", Types1_2_4.NBT_ITEM, Types1_2_4.NBT_ITEM_ARRAY);

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
        this.registerCreativeInventoryAction(ServerboundPackets1_2_1.SET_CREATIVE_MODE_SLOT);
    }

}
