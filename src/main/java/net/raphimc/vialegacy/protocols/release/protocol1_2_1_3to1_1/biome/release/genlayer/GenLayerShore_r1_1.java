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
package net.raphimc.vialegacy.protocols.release.protocol1_2_1_3to1_1.biome.release.genlayer;

import net.raphimc.vialegacy.protocols.release.protocol1_2_1_3to1_1.biome.release.IntCache;
import net.raphimc.vialegacy.protocols.release.protocol1_2_1_3to1_1.biome.release.NewBiomeGenBase;

public class GenLayerShore_r1_1 extends GenLayer {
    public GenLayerShore_r1_1(long l, GenLayer genlayer) {
        super(l);
        parent = genlayer;
    }

    public int[] getInts(int i, int j, int k, int l) {
        int[] ai = parent.getInts(i - 1, j - 1, k + 2, l + 2);
        int[] ai1 = IntCache.getIntCache(k * l);
        for (int i1 = 0; i1 < l; i1++) {
            for (int j1 = 0; j1 < k; j1++) {
                initChunkSeed(j1 + i, i1 + j);
                int k1 = ai[j1 + 1 + (i1 + 1) * (k + 2)];
                if (k1 == NewBiomeGenBase.mushroomIsland.biomeID) {
                    int l1 = ai[j1 + 1 + ((i1 + 1) - 1) * (k + 2)];
                    int k2 = ai[j1 + 1 + 1 + (i1 + 1) * (k + 2)];
                    int j3 = ai[((j1 + 1) - 1) + (i1 + 1) * (k + 2)];
                    int i4 = ai[j1 + 1 + (i1 + 1 + 1) * (k + 2)];
                    if (l1 == NewBiomeGenBase.ocean.biomeID || k2 == NewBiomeGenBase.ocean.biomeID || j3 == NewBiomeGenBase.ocean.biomeID || i4 == NewBiomeGenBase.ocean.biomeID) {
                        ai1[j1 + i1 * k] = NewBiomeGenBase.mushroomIslandShore.biomeID;
                    } else {
                        ai1[j1 + i1 * k] = k1;
                    }
                    continue;
                }
                if (k1 != NewBiomeGenBase.ocean.biomeID && k1 != NewBiomeGenBase.river.biomeID && k1 != NewBiomeGenBase.swampland.biomeID && k1 != NewBiomeGenBase.extremeHills.biomeID) {
                    int i2 = ai[j1 + 1 + ((i1 + 1) - 1) * (k + 2)];
                    int l2 = ai[j1 + 1 + 1 + (i1 + 1) * (k + 2)];
                    int k3 = ai[((j1 + 1) - 1) + (i1 + 1) * (k + 2)];
                    int j4 = ai[j1 + 1 + (i1 + 1 + 1) * (k + 2)];
                    if (i2 == NewBiomeGenBase.ocean.biomeID || l2 == NewBiomeGenBase.ocean.biomeID || k3 == NewBiomeGenBase.ocean.biomeID || j4 == NewBiomeGenBase.ocean.biomeID) {
                        ai1[j1 + i1 * k] = NewBiomeGenBase.beach.biomeID;
                    } else {
                        ai1[j1 + i1 * k] = k1;
                    }
                    continue;
                }
                if (k1 == NewBiomeGenBase.extremeHills.biomeID) {
                    int j2 = ai[j1 + 1 + ((i1 + 1) - 1) * (k + 2)];
                    int i3 = ai[j1 + 1 + 1 + (i1 + 1) * (k + 2)];
                    int l3 = ai[((j1 + 1) - 1) + (i1 + 1) * (k + 2)];
                    int k4 = ai[j1 + 1 + (i1 + 1 + 1) * (k + 2)];
                    if (j2 != NewBiomeGenBase.extremeHills.biomeID || i3 != NewBiomeGenBase.extremeHills.biomeID || l3 != NewBiomeGenBase.extremeHills.biomeID || k4 != NewBiomeGenBase.extremeHills.biomeID) {
                        ai1[j1 + i1 * k] = NewBiomeGenBase.extremeHillsEdge.biomeID;
                    } else {
                        ai1[j1 + i1 * k] = k1;
                    }
                } else {
                    ai1[j1 + i1 * k] = k1;
                }
            }
        }

        return ai1;
    }
}
