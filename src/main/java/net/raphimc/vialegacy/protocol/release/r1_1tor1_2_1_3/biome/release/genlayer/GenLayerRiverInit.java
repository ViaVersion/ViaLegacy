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
package net.raphimc.vialegacy.protocol.release.r1_1tor1_2_1_3.biome.release.genlayer;

import net.raphimc.vialegacy.protocol.release.r1_1tor1_2_1_3.biome.release.IntCache;

public class GenLayerRiverInit extends GenLayer {
    public GenLayerRiverInit(long l, GenLayer genlayer) {
        super(l);
        parent = genlayer;
    }

    public int[] getInts(int i, int j, int k, int l) {
        int[] ai = parent.getInts(i, j, k, l);
        int[] ai1 = IntCache.getIntCache(k * l);
        for (int i1 = 0; i1 < l; i1++) {
            for (int j1 = 0; j1 < k; j1++) {
                initChunkSeed(j1 + i, i1 + j);
                ai1[j1 + i1 * k] = ai[j1 + i1 * k] <= 0 ? 0 : nextInt(2) + 2;
            }
        }

        return ai1;
    }
}
