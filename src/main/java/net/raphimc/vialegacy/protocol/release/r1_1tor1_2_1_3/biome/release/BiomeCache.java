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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BiomeCache {

    private final WorldChunkManager_r1_1 chunkmanager;
    private final Map<Long, BiomeCacheBlock> cacheMap;
    private final List<BiomeCacheBlock> cache;

    public BiomeCache(WorldChunkManager_r1_1 worldchunkmanager) {
        cacheMap = new HashMap<>();
        cache = new ArrayList<>();
        chunkmanager = worldchunkmanager;
    }

    public BiomeCacheBlock getBiomeCacheBlock(int blockX, int blockZ) {
        final int chunkX = blockX >> 4;
        final int chunkZ = blockZ >> 4;

        final long l = (long) chunkX & 0xffffffffL | ((long) chunkZ & 0xffffffffL) << 32;
        BiomeCacheBlock biomecacheblock = cacheMap.get(l);
        if (biomecacheblock == null) {
            biomecacheblock = new BiomeCacheBlock(this, chunkX, chunkZ);
            cacheMap.put(l, biomecacheblock);
            cache.add(biomecacheblock);
        }

        biomecacheblock.lastAccessTime = System.currentTimeMillis();
        return biomecacheblock;
    }

    public NewBiomeGenBase getBiomeGenAt(int i, int j) {
        return getBiomeCacheBlock(i, j).getBiomeGenAt(i, j);
    }

    public NewBiomeGenBase[] getCachedBiomes(int i, int j) {
        return getBiomeCacheBlock(i, j).biomes;
    }

    static WorldChunkManager_r1_1 getWorldChunkManager(BiomeCache biomecache) {
        return biomecache.chunkmanager;
    }

}
