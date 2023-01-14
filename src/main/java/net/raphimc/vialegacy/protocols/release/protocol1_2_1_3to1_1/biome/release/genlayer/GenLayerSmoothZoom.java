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

public class GenLayerSmoothZoom extends GenLayer {
    public GenLayerSmoothZoom(long l, GenLayer genlayer) {
        super(l);
        parent = genlayer;
    }

    public int[] getInts(int i, int j, int k, int l) {
        int i1 = i >> 1;
        int j1 = j >> 1;
        int k1 = (k >> 1) + 3;
        int l1 = (l >> 1) + 3;
        int[] ai = parent.getInts(i1, j1, k1, l1);
        int[] ai1 = IntCache.getIntCache(k1 * 2 * (l1 * 2));
        int i2 = k1 << 1;
        for (int j2 = 0; j2 < l1 - 1; j2++) {
            int k2 = j2 << 1;
            int i3 = k2 * i2;
            int j3 = ai[0 + (j2 + 0) * k1];
            int k3 = ai[0 + (j2 + 1) * k1];
            for (int l3 = 0; l3 < k1 - 1; l3++) {
                initChunkSeed(l3 + i1 << 1, j2 + j1 << 1);
                int i4 = ai[l3 + 1 + (j2 + 0) * k1];
                int j4 = ai[l3 + 1 + (j2 + 1) * k1];
                ai1[i3] = j3;
                ai1[i3++ + i2] = j3 + ((k3 - j3) * nextInt(256)) / 256;
                ai1[i3] = j3 + ((i4 - j3) * nextInt(256)) / 256;
                int k4 = j3 + ((i4 - j3) * nextInt(256)) / 256;
                int l4 = k3 + ((j4 - k3) * nextInt(256)) / 256;
                ai1[i3++ + i2] = k4 + ((l4 - k4) * nextInt(256)) / 256;
                j3 = i4;
                k3 = j4;
            }
        }

        int[] ai2 = IntCache.getIntCache(k * l);
        for (int l2 = 0; l2 < l; l2++) {
            System.arraycopy(ai1, (l2 + (j & 1)) * (k1 << 1) + (i & 1), ai2, l2 * k, k);
        }

        return ai2;
    }

    public static GenLayer func_35517_a(long l, GenLayer genlayer, int i) {
        Object obj = genlayer;
        for (int j = 0; j < i; j++) {
            obj = new GenLayerSmoothZoom(l + (long) j, ((GenLayer) (obj)));
        }

        return ((GenLayer) (obj));
    }
}
