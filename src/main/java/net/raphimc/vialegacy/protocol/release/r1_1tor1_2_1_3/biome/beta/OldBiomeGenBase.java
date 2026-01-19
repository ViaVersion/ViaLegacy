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

package net.raphimc.vialegacy.protocol.release.r1_1tor1_2_1_3.biome.beta;

public class OldBiomeGenBase {

    private static final OldBiomeGenBase[] biomeLookupTable = new OldBiomeGenBase[4096];

    public static final OldBiomeGenBase rainforest = new OldBiomeGenBase();
    public static final OldBiomeGenBase swampland = new OldBiomeGenBase();
    public static final OldBiomeGenBase seasonalForest = new OldBiomeGenBase();
    public static final OldBiomeGenBase forest = new OldBiomeGenBase();
    public static final OldBiomeGenBase savanna = new OldBiomeGenBase();
    public static final OldBiomeGenBase shrubland = new OldBiomeGenBase();
    public static final OldBiomeGenBase taiga = new OldBiomeGenBase();
    public static final OldBiomeGenBase desert = new OldBiomeGenBase();
    public static final OldBiomeGenBase plains = new OldBiomeGenBase();
    public static final OldBiomeGenBase iceDesert = new OldBiomeGenBase();
    public static final OldBiomeGenBase tundra = new OldBiomeGenBase();
    public static final OldBiomeGenBase hell = new OldBiomeGenBase();
    public static final OldBiomeGenBase sky = new OldBiomeGenBase();

    static {
        generateBiomeLookup();
    }

    protected OldBiomeGenBase() {
    }

    public static void generateBiomeLookup() {
        for (int i = 0; i < 64; i++) {
            for (int j = 0; j < 64; j++) {
                biomeLookupTable[i + j * 64] = getBiome((float) i / 63F, (float) j / 63F);
            }

        }
    }

    public static OldBiomeGenBase getBiomeFromLookup(double d, double d1) {
        int i = (int) (d * 63D);
        int j = (int) (d1 * 63D);
        return biomeLookupTable[i + j * 64];
    }

    public static OldBiomeGenBase getBiome(float f, float f1) {
        f1 *= f;
        if (f < 0.1F) {
            return tundra;
        } else if (f1 < 0.2F) {
            if (f < 0.5F) {
                return tundra;
            } else if (f < 0.95F) {
                return savanna;
            } else {
                return desert;
            }
        } else if (f1 > 0.5F && f < 0.7F) {
            return swampland;
        } else if (f < 0.5F) {
            return taiga;
        } else if (f < 0.97F) {
            if (f1 < 0.35F) {
                return shrubland;
            } else {
                return forest;
            }
        } else if (f1 < 0.45F) {
            return plains;
        } else if (f1 < 0.9F) {
            return seasonalForest;
        } else {
            return rainforest;
        }
    }

}
