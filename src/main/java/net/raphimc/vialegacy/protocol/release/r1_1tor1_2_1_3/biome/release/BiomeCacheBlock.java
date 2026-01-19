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

public class BiomeCacheBlock {

    public NewBiomeGenBase[] biomes;
    public int xPosition;
    public int zPosition;
    public long lastAccessTime;
    final BiomeCache biomeCache;

    public BiomeCacheBlock(BiomeCache biomecache, int chunkX, int chunkZ) {
        biomeCache = biomecache;

        biomes = new NewBiomeGenBase[256];
        xPosition = chunkX;
        zPosition = chunkZ;
        BiomeCache.getWorldChunkManager(biomecache).getBiomeGenAt(biomes, chunkX << 4, chunkZ << 4, 16, 16, false);
    }

    public NewBiomeGenBase getBiomeGenAt(int i, int j) {
        return biomes[i & 0xf | (j & 0xf) << 4];
    }

}
