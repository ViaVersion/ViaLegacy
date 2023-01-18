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
package net.raphimc.vialegacy.protocols.release.protocol1_7_2_5to1_6_4.rewriter;

import net.raphimc.vialegacy.api.remapper.LegacyItemRewriter;
import net.raphimc.vialegacy.protocols.release.protocol1_7_2_5to1_6_4.Protocol1_7_2_5to1_6_4;
import net.raphimc.vialegacy.protocols.release.protocol1_7_6_10to1_7_2_5.ServerboundPackets1_7_2;
import net.raphimc.vialegacy.protocols.release.protocol1_8to1_7_6_10.types.Types1_7_6;

public class ItemRewriter extends LegacyItemRewriter<Protocol1_7_2_5to1_6_4> {

    public ItemRewriter(final Protocol1_7_2_5to1_6_4 protocol) {
        super(protocol, "1.6.4");
        addRemappedItem(26, 355, "Bed Block");
        addRemappedItem(34, 33, "Piston Head");
        addRemappedItem(36, 33, "Piston Moving");
        addRemappedItem(55, 331, "Redstone Wire");
        addRemappedItem(59, 295, "Wheat Crops");
        addRemappedItem(63, 323, "Standing Sign");
        addRemappedItem(64, 324, "Oak Door Block");
        addRemappedItem(68, 323, "Wall Sign");
        addRemappedItem(71, 330, "Iron Door Block");
        addRemappedItem(74, 73, "Lit Redstone Ore");
        addRemappedItem(75, 76, "Unlit Redstone Torch");
        addRemappedItem(83, 338, "Sugar Cane Block");
        addRemappedItem(92, 354, "Cake Block");
        addRemappedItem(93, 356, "Unlit Redstone Repeater");
        addRemappedItem(94, 356, "Lit Redstone Repeater");
        addRemappedItem(95, 146, "Locked Chest");
        addRemappedItem(104, 361, "Pumpkin Stem");
        addRemappedItem(105, 362, "Melon Stem");
        addRemappedItem(115, 372, "Nether Wart Block");
        addRemappedItem(117, 379, "Brewing Stand Block");
        addRemappedItem(118, 380, "Cauldron Block");
        addRemappedItem(124, 123, "Lit Redstone Lamp");
        addRemappedItem(132, 287, "Tripwire");
        addRemappedItem(140, 390, "Flower Pot");
        addRemappedItem(144, 397, "Undefined Mob Head");
        addRemappedItem(149, 404, "Unlit Redstone Comparator");
        addRemappedItem(150, 404, "Lit Redstone Comparator");
    }

    @Override
    protected void registerPackets() {
        this.registerCreativeInventoryAction(ServerboundPackets1_7_2.CREATIVE_INVENTORY_ACTION, Types1_7_6.COMPRESSED_ITEM);
    }

}
