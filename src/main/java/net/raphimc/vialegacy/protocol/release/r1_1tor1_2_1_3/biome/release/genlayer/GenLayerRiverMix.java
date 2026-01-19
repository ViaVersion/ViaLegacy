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
import net.raphimc.vialegacy.protocol.release.r1_1tor1_2_1_3.biome.release.NewBiomeGenBase;

public class GenLayerRiverMix extends GenLayer {
    private final GenLayer field_35512_b;
    private final GenLayer field_35513_c;

    public GenLayerRiverMix(long l, GenLayer genlayer, GenLayer genlayer1) {
        super(l);
        field_35512_b = genlayer;
        field_35513_c = genlayer1;
    }

    public void initWorldGenSeed(long l) {
        field_35512_b.initWorldGenSeed(l);
        field_35513_c.initWorldGenSeed(l);
        super.initWorldGenSeed(l);
    }

    public int[] getInts(int i, int j, int k, int l) {
        int[] ai = field_35512_b.getInts(i, j, k, l);
        int[] ai1 = field_35513_c.getInts(i, j, k, l);
        int[] ai2 = IntCache.getIntCache(k * l);
        for (int i1 = 0; i1 < k * l; i1++) {
            if (ai[i1] == NewBiomeGenBase.ocean.biomeID) {
                ai2[i1] = ai[i1];
                continue;
            }
            if (ai1[i1] >= 0) {
                if (ai[i1] == NewBiomeGenBase.icePlains.biomeID) {
                    ai2[i1] = NewBiomeGenBase.frozenRiver.biomeID;
                    continue;
                }
                if (ai[i1] == NewBiomeGenBase.mushroomIsland.biomeID || ai[i1] == NewBiomeGenBase.mushroomIslandShore.biomeID) {
                    ai2[i1] = NewBiomeGenBase.mushroomIslandShore.biomeID;
                } else {
                    ai2[i1] = ai1[i1];
                }
            } else {
                ai2[i1] = ai[i1];
            }
        }

        return ai2;
    }
}
