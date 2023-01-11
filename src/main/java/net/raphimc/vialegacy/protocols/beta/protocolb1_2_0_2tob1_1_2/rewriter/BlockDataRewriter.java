/*
 * This file is part of ViaProtocolHack - https://github.com/RaphiMC/ViaProtocolHack
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
package net.raphimc.vialegacy.protocols.beta.protocolb1_2_0_2tob1_1_2.rewriter;

import net.raphimc.vialegacy.api.data.BlockList1_6;
import net.raphimc.vialegacy.api.model.IdAndData;
import net.raphimc.vialegacy.api.remapper.AbstractBlockRemapper;

public class BlockDataRewriter extends AbstractBlockRemapper {

    public BlockDataRewriter() {
        for (int i = 1; i < 16; i++) { // cobblestone
            this.registerReplacement(new IdAndData(BlockList1_6.cobblestone.blockID, i), new IdAndData(BlockList1_6.cobblestone.blockID, 0));
        }
        for (int i = 1; i < 16; i++) { // sand
            this.registerReplacement(new IdAndData(BlockList1_6.sand.blockID, i), new IdAndData(BlockList1_6.sand.blockID, 0));
        }
        for (int i = 1; i < 16; i++) { // gravel
            this.registerReplacement(new IdAndData(BlockList1_6.gravel.blockID, i), new IdAndData(BlockList1_6.gravel.blockID, 0));
        }
        for (int i = 1; i < 16; i++) { // obsidian
            this.registerReplacement(new IdAndData(BlockList1_6.obsidian.blockID, i), new IdAndData(BlockList1_6.obsidian.blockID, 0));
        }
    }

}
