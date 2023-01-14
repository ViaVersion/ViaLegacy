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

import net.raphimc.vialegacy.api.remapper.AbstractItemRewriter;

public class ItemRewriter extends AbstractItemRewriter {

    public ItemRewriter() {
        super("1.6.4", false);
        registerRemappedItem(26, 355, "Bed Block");
        registerRemappedItem(34, 33, "Piston Head");
        registerRemappedItem(36, 33, "Piston Moving");
        registerRemappedItem(55, 331, "Redstone Wire");
        registerRemappedItem(59, 295, "Wheat Crops");
        registerRemappedItem(63, 323, "Standing Sign");
        registerRemappedItem(64, 324, "Oak Door Block");
        registerRemappedItem(68, 323, "Wall Sign");
        registerRemappedItem(71, 330, "Iron Door Block");
        registerRemappedItem(74, 73, "Lit Redstone Ore");
        registerRemappedItem(75, 76, "Unlit Redstone Torch");
        registerRemappedItem(83, 338, "Sugar Cane Block");
        registerRemappedItem(92, 354, "Cake Block");
        registerRemappedItem(93, 356, "Unlit Redstone Repeater");
        registerRemappedItem(94, 356, "Lit Redstone Repeater");
        registerRemappedItem(95, 146, "Locked Chest");
        registerRemappedItem(104, 361, "Pumpkin Stem");
        registerRemappedItem(105, 362, "Melon Stem");
        registerRemappedItem(115, 372, "Nether Wart Block");
        registerRemappedItem(117, 379, "Brewing Stand Block");
        registerRemappedItem(118, 380, "Cauldron Block");
        registerRemappedItem(124, 123, "Lit Redstone Lamp");
        registerRemappedItem(132, 287, "Tripwire");
        registerRemappedItem(140, 390, "Flower Pot");
        registerRemappedItem(144, 397, "Undefined Mob Head");
        registerRemappedItem(149, 404, "Unlit Redstone Comparator");
        registerRemappedItem(150, 404, "Lit Redstone Comparator");
    }

}
