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
package net.raphimc.vialegacy.protocol.release.r1_1tor1_2_1_3.biome;

import com.seedfinding.mcbiome.biome.Biome;
import com.seedfinding.mcbiome.biome.Biomes;
import com.seedfinding.mcbiome.source.BiomeSource;
import com.seedfinding.mccore.state.Dimension;
import com.seedfinding.mccore.util.pos.BPos;
import com.seedfinding.mccore.version.MCVersion;
import com.seedfinding.mcmath.util.Mth;
import com.seedfinding.mcseed.rand.JRand;

public class BetaOverworldBiomeSource extends BiomeSource {

    private final BetaOctaveSimplexNoiseSampler temperatureNoise;
    private final BetaOctaveSimplexNoiseSampler humidityNoise;
    private final BetaOctaveSimplexNoiseSampler biomeNoise;

    public BetaOverworldBiomeSource(final long worldSeed) {
        super(MCVersion.vb1_7_3, worldSeed);

        this.temperatureNoise = new BetaOctaveSimplexNoiseSampler(new JRand(worldSeed * 9871L), 4, 0.025F, 0.025F, 0.25D);
        this.humidityNoise = new BetaOctaveSimplexNoiseSampler(new JRand(worldSeed * 39811L), 4, 0.05F, 0.05F, 0.3333333333333333D);
        this.biomeNoise = new BetaOctaveSimplexNoiseSampler(new JRand(worldSeed * 543321L), 2, 0.25D, 0.25D, 0.5882352941176471D);
    }

    @Override
    public Dimension getDimension() {
        return Dimension.OVERWORLD;
    }

    @Override
    public Biome getBiome(final BPos bpos) {
        return this.getBiome(bpos.getX(), bpos.getY(), bpos.getZ());
    }

    @Override
    public Biome getBiome(final int x, final int y, final int z) {
        return switch (this.getBetaBiome(x, z)) {
            case RAINFOREST -> Biomes.JUNGLE;
            case SWAMPLAND -> Biomes.SWAMPLAND;
            case SEASONAL_FOREST, FOREST -> Biomes.FOREST;
            case SAVANNA, SHRUBLAND -> Biomes.SAVANNA;
            case TAIGA -> Biomes.TAIGA;
            case DESERT -> Biomes.DESERT;
            case PLAINS -> Biomes.PLAINS;
            case ICE_DESERT, TUNDRA -> Biomes.ICE_PLAINS;
            case HELL -> Biomes.HELL;
            case SKY -> Biomes.SKY;
        };
    }

    @Override
    public Biome getBiomeForNoiseGen(final int x, final int y, final int z) {
        throw new UnsupportedOperationException();
    }

    public BetaBiome getBetaBiome(final int x, final int z) {
        final double temperature = this.getTemperature(x, z);
        final double humidity = this.getHumidity(x, z);
        final float quantizedTemperature = ((int) (temperature * 63D)) / 63F;
        final float quantizedHumidity = ((int) (humidity * 63D)) / 63F;
        return BetaBiome.getBiome(quantizedTemperature, quantizedHumidity);
    }

    public double getTemperature(final int x, final int z) {
        final double temperatureValue = this.temperatureNoise.sample(x, z) * 0.15D + 0.7D;
        final double biomeValue = this.biomeNoise.sample(x, z) * 1.1D + 0.5D;
        final double mixedTemperature = temperatureValue * 0.99D + biomeValue * 0.01D;
        return Mth.clamp(1D - (1D - mixedTemperature) * (1D - mixedTemperature), 0D, 1D);
    }

    public double getHumidity(final int x, final int z) {
        final double humidityValue = this.humidityNoise.sample(x, z) * 0.15D + 0.5D;
        final double biomeValue = this.biomeNoise.sample(x, z) * 1.1D + 0.5D;
        final double mixedHumidity = humidityValue * 0.998D + biomeValue * 0.002D;
        return Mth.clamp(mixedHumidity, 0D, 1D);
    }

}
