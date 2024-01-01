/*
 * This file is part of ViaLegacy - https://github.com/RaphiMC/ViaLegacy
 * Copyright (C) 2020-2024 RK_01/RaphiMC and contributors
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
package net.raphimc.vialegacy.protocols.release.protocol1_8to1_7_6_10.storage;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.chunks.DataPalette;
import net.raphimc.vialegacy.api.data.BlockList1_6;
import net.raphimc.vialegacy.api.model.IdAndData;
import net.raphimc.vialegacy.api.remapper.AbstractChunkTracker;

public class ChunkTracker extends AbstractChunkTracker {

    public ChunkTracker(final UserConnection user) {
        super(user, BlockList1_6.obsidian.blockID, BlockList1_6.portal.blockID);

        for (int i = 9; i < 16; i++) { // double plant
            this.registerReplacement(new IdAndData(175, i), new IdAndData(175, 0));
        }
        for (int i = 1; i < 16; i++) { // air
            this.registerReplacement(new IdAndData(0, i), new IdAndData(0, 0));
        }
        this.registerReplacement(new IdAndData(BlockList1_6.chest.blockID, 0), new IdAndData(BlockList1_6.chest.blockID, 3));
    }

    @Override
    protected void remapBlock(IdAndData block, int x, int y, int z) {
        if (block.id == BlockList1_6.portal.blockID && block.data == 0) {
            if (this.getBlockNotNull(x - 1, y, z).id == BlockList1_6.obsidian.blockID || this.getBlockNotNull(x + 1, y, z).id == BlockList1_6.obsidian.blockID) {
                block.data = 1;
            } else {
                block.data = 2;
            }
        }
    }

    @Override
    protected void postRemap(DataPalette palette) {
        palette.replaceId(BlockList1_6.portal.blockID << 4, 0);
    }

}
