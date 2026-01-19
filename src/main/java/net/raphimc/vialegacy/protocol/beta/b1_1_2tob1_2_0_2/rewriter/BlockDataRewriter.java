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
package net.raphimc.vialegacy.protocol.beta.b1_1_2tob1_2_0_2.rewriter;

import com.viaversion.viaversion.util.IdAndData;
import net.raphimc.vialegacy.api.data.BlockList1_6;
import net.raphimc.vialegacy.api.remapper.AbstractBlockRemapper;

public class BlockDataRewriter extends AbstractBlockRemapper {

    public BlockDataRewriter() {
        for (int i = 1; i < 16; i++) { // cobblestone
            this.registerReplacement(new IdAndData(BlockList1_6.cobblestone.blockId(), i), new IdAndData(BlockList1_6.cobblestone.blockId(), 0));
        }
        for (int i = 1; i < 16; i++) { // sand
            this.registerReplacement(new IdAndData(BlockList1_6.sand.blockId(), i), new IdAndData(BlockList1_6.sand.blockId(), 0));
        }
        for (int i = 1; i < 16; i++) { // gravel
            this.registerReplacement(new IdAndData(BlockList1_6.gravel.blockId(), i), new IdAndData(BlockList1_6.gravel.blockId(), 0));
        }
        for (int i = 1; i < 16; i++) { // obsidian
            this.registerReplacement(new IdAndData(BlockList1_6.obsidian.blockId(), i), new IdAndData(BlockList1_6.obsidian.blockId(), 0));
        }
    }

}
