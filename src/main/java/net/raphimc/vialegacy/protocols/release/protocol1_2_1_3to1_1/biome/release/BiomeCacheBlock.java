/*
 * This file is part of ViaProtocolHack - https://github.com/RaphiMC/ViaProtocolHack
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
package net.raphimc.vialegacy.protocols.release.protocol1_2_1_3to1_1.biome.release;

public class BiomeCacheBlock {

    public NewBiomeGenBase[] biomes;
    public int xPosition;
    public int zPosition;
    public long lastAccessTime;
    final BiomeCache biomeCache;

    public BiomeCacheBlock(BiomeCache biomecache, int i, int j) {
        biomeCache = biomecache;

        biomes = new NewBiomeGenBase[256];
        xPosition = i;
        zPosition = j;
        BiomeCache.getWorldChunkManager(biomecache).getBiomeGenAt(biomes, i << 4, j << 4, 16, 16, false);
    }

    public NewBiomeGenBase getBiomeGenAt(int i, int j) {
        return biomes[i & 0xf | (j & 0xf) << 4];
    }

}
