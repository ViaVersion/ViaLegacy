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

public class GenLayerIsland_r1_0 extends GenLayer {
    public GenLayerIsland_r1_0(long l, GenLayer genlayer) {
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
                int k2 = ai[j2 + 0 + (i2 + 0) * k1];
                int l2 = ai[j2 + 2 + (i2 + 0) * k1];
                int i3 = ai[j2 + 0 + (i2 + 2) * k1];
                int j3 = ai[j2 + 2 + (i2 + 2) * k1];
                int k3 = ai[j2 + 1 + (i2 + 1) * k1];
                initChunkSeed(j2 + i, i2 + j);
                if (k3 == 0 && (k2 != 0 || l2 != 0 || i3 != 0 || j3 != 0)) {
                    int l3 = 1;
                    int i4 = 1;
                    if (k2 != 0 && nextInt(l3++) == 0) {
                        i4 = k2;
                    }
                    if (l2 != 0 && nextInt(l3++) == 0) {
                        i4 = l2;
                    }
                    if (i3 != 0 && nextInt(l3++) == 0) {
                        i4 = i3;
                    }
                    if (j3 != 0 && nextInt(l3++) == 0) {
                        i4 = j3;
                    }
                    if (nextInt(3) == 0) {
                        ai1[j2 + i2 * k] = i4;
                        continue;
                    }
                    if (i4 == NewBiomeGenBase.icePlains.biomeID) {
                        ai1[j2 + i2 * k] = NewBiomeGenBase.frozenOcean.biomeID;
                    } else {
                        ai1[j2 + i2 * k] = 0;
                    }
                    continue;
                }
                if (k3 > 0 && (k2 == 0 || l2 == 0 || i3 == 0 || j3 == 0)) {
                    if (nextInt(5) == 0) {
                        if (k3 == NewBiomeGenBase.icePlains.biomeID) {
                            ai1[j2 + i2 * k] = NewBiomeGenBase.frozenOcean.biomeID;
                        } else {
                            ai1[j2 + i2 * k] = 0;
                        }
                    } else {
                        ai1[j2 + i2 * k] = k3;
                    }
                } else {
                    ai1[j2 + i2 * k] = k3;
                }
            }
        }

        return ai1;
    }
}
