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
package net.raphimc.vialegacy.protocol.release.r1_7_6_10tor1_8.storage;

import com.viaversion.viaversion.api.minecraft.chunks.DataPalette;
import com.viaversion.viaversion.util.IdAndData;
import net.raphimc.vialegacy.api.data.BlockList1_6;
import net.raphimc.vialegacy.api.remapper.AbstractChunkTracker;

public class ChunkTracker extends AbstractChunkTracker {

    public ChunkTracker() {
        super(BlockList1_6.obsidian.blockId(), BlockList1_6.portal.blockId());

        for (int i = 9; i < 16; i++) { // double plant
            this.registerReplacement(new IdAndData(175, i), new IdAndData(175, 0));
        }
        for (int i = 1; i < 16; i++) { // air
            this.registerReplacement(new IdAndData(0, i), new IdAndData(0, 0));
        }
        for (int i = 1; i < 7; i++) { // stone variants
            this.registerReplacement(new IdAndData(BlockList1_6.stone.blockId(), i), new IdAndData(BlockList1_6.stone.blockId(), 0));
        }
        this.registerInvalidDirectionReplacements(BlockList1_6.ladder.blockId(), new IdAndData(BlockList1_6.planks.blockId(), 0)); // Ladders with broken data values use the last ladder collision box the player has looked at. We just set them to a full block to avoid glitches.
        this.registerInvalidDirectionReplacements(BlockList1_6.furnaceBurning.blockId(), new IdAndData(BlockList1_6.furnaceBurning.blockId(), 2));
        this.registerReplacement(new IdAndData(BlockList1_6.chest.blockId(), 0), new IdAndData(BlockList1_6.chest.blockId(), 3));
    }

    @Override
    protected void remapBlock(IdAndData block, int x, int y, int z) {
        if (block.getId() == BlockList1_6.portal.blockId() && block.getData() == 0) {
            if (this.getBlockNotNull(x - 1, y, z).getId() == BlockList1_6.obsidian.blockId() || this.getBlockNotNull(x + 1, y, z).getId() == BlockList1_6.obsidian.blockId()) {
                block.setData(1);
            } else {
                block.setData(2);
            }
        }
    }

    @Override
    protected void postRemap(DataPalette palette) {
        palette.replaceId(BlockList1_6.portal.blockId() << 4, 0);
    }

    private void registerInvalidDirectionReplacements(final int blockId, final IdAndData replacement) {
        for (int i = 0; i < 2; i++) {
            this.registerReplacement(new IdAndData(blockId, i), replacement);
        }
        for (int i = 6; i < 16; i++) {
            this.registerReplacement(new IdAndData(blockId, i), replacement);
        }
    }

}
