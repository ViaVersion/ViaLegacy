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

import net.raphimc.vialegacy.protocol.release.r1_1tor1_2_1_3.biome.IWorldChunkManager;
import net.raphimc.vialegacy.protocol.release.r1_1tor1_2_1_3.biome.release.NewBiomeGenBase;

import java.util.Random;

public class WorldChunkManager_b1_7 implements IWorldChunkManager {

    private final NoiseGeneratorOctaves2 temperatureNoise;
    private final NoiseGeneratorOctaves2 humidityNoise;
    private final NoiseGeneratorOctaves2 field_4192_g;
    public double[] temperature;
    public double[] humidity;
    public double[] field_4196_c;
    public OldBiomeGenBase[] biomes;

    public WorldChunkManager_b1_7(final long seed) {
        temperatureNoise = new NoiseGeneratorOctaves2(new Random(seed * 9871L), 4);
        humidityNoise = new NoiseGeneratorOctaves2(new Random(seed * 39811L), 4);
        field_4192_g = new NoiseGeneratorOctaves2(new Random(seed * 0x84a59L), 2);
    }

    @Override
    public byte[] getBiomeDataAt(int chunkX, int chunkZ) {
        final byte[] biomeData = new byte[256];
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                biomeData[z << 4 | x] = (byte) this.getBiomeGenAt((chunkX * 16) + x, (chunkZ * 16) + z).biomeID;
            }
        }

        return biomeData;
    }

    public NewBiomeGenBase getBiomeGenAt(int x, int z) {
        final OldBiomeGenBase oldBiomeGenBase = getBiomes(x, z, 1, 1)[0];
        if (oldBiomeGenBase.equals(OldBiomeGenBase.rainforest)) {
            return NewBiomeGenBase.jungle;
        } else if (oldBiomeGenBase.equals(OldBiomeGenBase.swampland)) {
            return NewBiomeGenBase.swampland;
        } else if (oldBiomeGenBase.equals(OldBiomeGenBase.seasonalForest)) {
            return NewBiomeGenBase.forest;
        } else if (oldBiomeGenBase.equals(OldBiomeGenBase.forest)) {
            return NewBiomeGenBase.forest;
        } else if (oldBiomeGenBase.equals(OldBiomeGenBase.savanna)) {
            return NewBiomeGenBase.savanna;
        } else if (oldBiomeGenBase.equals(OldBiomeGenBase.shrubland)) {
            return NewBiomeGenBase.savanna;
        } else if (oldBiomeGenBase.equals(OldBiomeGenBase.taiga)) {
            return NewBiomeGenBase.taiga;
        } else if (oldBiomeGenBase.equals(OldBiomeGenBase.desert)) {
            return NewBiomeGenBase.desert;
        } else if (oldBiomeGenBase.equals(OldBiomeGenBase.plains)) {
            return NewBiomeGenBase.plains;
        } else if (oldBiomeGenBase.equals(OldBiomeGenBase.iceDesert)) {
            return NewBiomeGenBase.icePlains;
        } else if (oldBiomeGenBase.equals(OldBiomeGenBase.tundra)) {
            return NewBiomeGenBase.icePlains;
        } else if (oldBiomeGenBase.equals(OldBiomeGenBase.hell)) {
            return NewBiomeGenBase.hell;
        } else if (oldBiomeGenBase.equals(OldBiomeGenBase.sky)) {
            return NewBiomeGenBase.sky;
        } else {
            return NewBiomeGenBase.plains;
        }
    }

    public OldBiomeGenBase[] getBiomes(int blockX, int blockZ, int k, int l) {
        biomes = loadBlockGeneratorData(biomes, blockX, blockZ, k, l);
        return biomes;
    }

    public OldBiomeGenBase[] loadBlockGeneratorData(OldBiomeGenBase[] biomes, int blockX, int blockZ, int k, int l) {
        if (biomes == null || biomes.length < k * l) {
            biomes = new OldBiomeGenBase[k * l];
        }

        temperature = temperatureNoise.getValues(temperature, blockX, blockZ, k, k, 0.02500000037252903D, 0.02500000037252903D, 0.25D);
        humidity = humidityNoise.getValues(humidity, blockX, blockZ, k, k, 0.05000000074505806D, 0.05000000074505806D, 0.33333333333333331D);
        field_4196_c = field_4192_g.getValues(field_4196_c, blockX, blockZ, k, k, 0.25D, 0.25D, 0.58823529411764708D);
        int i1 = 0;
        for (int j1 = 0; j1 < k; j1++) {
            for (int k1 = 0; k1 < l; k1++) {
                double d = field_4196_c[i1] * 1.1000000000000001D + 0.5D;
                double d1 = 0.01D;
                double d2 = 1.0D - d1;

                double temperature = (this.temperature[i1] * 0.14999999999999999D + 0.69999999999999996D) * d2 + d * d1;
                d1 = 0.002D;
                d2 = 1.0D - d1;

                final double rainfall = Math.max(0.0, Math.min(1.0, (humidity[i1] * 0.14999999999999999D + 0.5D) * d2 + d * d1));
                this.temperature[i1] = Math.max(0.0, Math.min(1.0, 1.0D - (1.0D - temperature) * (1.0D - temperature)));
                this.humidity[i1] = rainfall;

                biomes[i1++] = OldBiomeGenBase.getBiomeFromLookup(temperature, rainfall);
            }
        }

        return biomes;
    }

}
