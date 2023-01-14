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
package net.raphimc.vialegacy.protocols.release.protocol1_2_1_3to1_1.biome.release.genlayer;

import net.raphimc.vialegacy.protocols.release.protocol1_2_1_3to1_1.biome.release.IntCache;
import net.raphimc.vialegacy.protocols.release.protocol1_2_1_3to1_1.biome.release.NewBiomeGenBase;

public class GenLayerSnow extends GenLayer {
    public GenLayerSnow(long l, GenLayer genlayer) {
        super(l);
        parent = genlayer;
    }

    public int[] getInts(int i, int j, int k, int l) {
        int i1 = i - 1;
        int j1 = j - 1;
        int k1 = k + 2;
        int l1 = l + 2;
        int[] ai = parent.getInts(i1, j1, k1, l1);
        int[] ai1 = IntCache.getIntCache(k * l);
        for (int i2 = 0; i2 < l; i2++) {
            for (int j2 = 0; j2 < k; j2++) {
                int k2 = ai[j2 + 1 + (i2 + 1) * k1];
                initChunkSeed(j2 + i, i2 + j);
                if (k2 == 0) {
                    ai1[j2 + i2 * k] = 0;
                    continue;
                }
                int l2 = nextInt(5);
                if (l2 == 0) {
                    l2 = NewBiomeGenBase.icePlains.biomeID;
                } else {
                    l2 = 1;
                }
                ai1[j2 + i2 * k] = l2;
            }
        }

        return ai1;
    }
}
