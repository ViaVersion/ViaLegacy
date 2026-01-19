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

package net.raphimc.vialegacy.protocol.release.r1_1tor1_2_1_3.biome.release;

import java.util.ArrayList;
import java.util.List;

public class IntCacheInstance {

    private int intCacheSize = 256;
    private List<int[]> freeSmallArrays = new ArrayList<>();
    private List<int[]> inUseSmallArrays = new ArrayList<>();
    private List<int[]> freeLargeArrays = new ArrayList<>();
    private List<int[]> inUseLargeArrays = new ArrayList<>();

    public int[] getIntCache(int i) {
        if (i <= 256) {
            if (freeSmallArrays.isEmpty()) {
                int[] ai = new int[256];
                inUseSmallArrays.add(ai);
                return ai;
            } else {
                int[] ai1 = freeSmallArrays.remove(freeSmallArrays.size() - 1);
                inUseSmallArrays.add(ai1);
                return ai1;
            }
        }

        if (i > intCacheSize) {
            intCacheSize = i;
            freeLargeArrays.clear();
            inUseLargeArrays.clear();
            int[] ai2 = new int[intCacheSize];
            inUseLargeArrays.add(ai2);
            return ai2;
        }

        if (freeLargeArrays.isEmpty()) {
            int[] ai3 = new int[intCacheSize];
            inUseLargeArrays.add(ai3);
            return ai3;
        } else {
            int[] ai4 = freeLargeArrays.remove(freeLargeArrays.size() - 1);
            inUseLargeArrays.add(ai4);
            return ai4;
        }
    }

    public void resetIntCache() {
        if (!freeLargeArrays.isEmpty()) {
            freeLargeArrays.remove(freeLargeArrays.size() - 1);
        }

        if (!freeSmallArrays.isEmpty()) {
            freeSmallArrays.remove(freeSmallArrays.size() - 1);
        }

        freeLargeArrays.addAll(inUseLargeArrays);
        freeSmallArrays.addAll(inUseSmallArrays);
        inUseLargeArrays.clear();
        inUseSmallArrays.clear();
    }

    public void resetEverything() {
        intCacheSize = 256;
        freeSmallArrays = new ArrayList<>();
        inUseSmallArrays = new ArrayList<>();
        freeLargeArrays = new ArrayList<>();
        inUseLargeArrays = new ArrayList<>();
    }

}
