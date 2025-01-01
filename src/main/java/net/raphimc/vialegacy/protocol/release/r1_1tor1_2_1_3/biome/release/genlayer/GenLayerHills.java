/*
 * This file is part of ViaLegacy - https://github.com/RaphiMC/ViaLegacy
 * Copyright (C) 2020-2025 RK_01/RaphiMC and contributors
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
import net.raphimc.vialegacy.protocol.release.r1_1tor1_2_1_3.biome.release.NewBiomeGenBase;

public class GenLayerHills extends GenLayer {
    public GenLayerHills(long l, GenLayer genlayer) {
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
                if (nextInt(3) == 0) {
                    int l1 = k1;
                    if (k1 == NewBiomeGenBase.desert.biomeID) {
                        l1 = NewBiomeGenBase.desertHills.biomeID;
                    } else if (k1 == NewBiomeGenBase.forest.biomeID) {
                        l1 = NewBiomeGenBase.forestHills.biomeID;
                    } else if (k1 == NewBiomeGenBase.taiga.biomeID) {
                        l1 = NewBiomeGenBase.taigaHills.biomeID;
                    } else if (k1 == NewBiomeGenBase.plains.biomeID) {
                        l1 = NewBiomeGenBase.forest.biomeID;
                    } else if (k1 == NewBiomeGenBase.icePlains.biomeID) {
                        l1 = NewBiomeGenBase.iceMountains.biomeID;
                    }
                    if (l1 != k1) {
                        int i2 = ai[j1 + 1 + ((i1 + 1) - 1) * (k + 2)];
                        int j2 = ai[j1 + 1 + 1 + (i1 + 1) * (k + 2)];
                        int k2 = ai[((j1 + 1) - 1) + (i1 + 1) * (k + 2)];
                        int l2 = ai[j1 + 1 + (i1 + 1 + 1) * (k + 2)];
                        if (i2 == k1 && j2 == k1 && k2 == k1 && l2 == k1) {
                            ai1[j1 + i1 * k] = l1;
                        } else {
                            ai1[j1 + i1 * k] = k1;
                        }
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
