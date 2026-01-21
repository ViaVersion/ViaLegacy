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

public class PlainsBiomeSource extends BiomeSource {

    public PlainsBiomeSource() {
        super(Biomes.PLAINS.getVersion(), 0L);
    }

    @Override
    public Dimension getDimension() {
        return Biomes.PLAINS.getDimension();
    }

    @Override
    public Biome getBiome(final BPos bpos) {
        return Biomes.PLAINS;
    }

    @Override
    public Biome getBiome(final int x, final int y, final int z) {
        return Biomes.PLAINS;
    }

    @Override
    public Biome getBiomeForNoiseGen(final int x, final int y, final int z) {
        return Biomes.PLAINS;
    }

}
