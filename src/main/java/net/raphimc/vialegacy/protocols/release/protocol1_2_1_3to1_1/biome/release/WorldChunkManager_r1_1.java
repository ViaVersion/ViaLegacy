/*
 * This file is part of ViaLegacy - https://github.com/RaphiMC/ViaLegacy
 * Copyright (C) 2020-2024 RK_01/RaphiMC and contributors
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

import com.viaversion.viaversion.api.connection.UserConnection;
import net.raphimc.vialegacy.protocols.release.protocol1_2_1_3to1_1.biome.IWorldChunkManager;
import net.raphimc.vialegacy.protocols.release.protocol1_2_1_3to1_1.biome.release.genlayer.GenLayer;

public class WorldChunkManager_r1_1 implements IWorldChunkManager {

    private GenLayer biomeIndexLayer;
    private GenLayer temperatureLayer;
    private GenLayer rainfallLayer;
    private final BiomeCache biomeCache;

    protected WorldChunkManager_r1_1() {
        IntCache.resetEverything();
        biomeCache = new BiomeCache(this);
    }

    public WorldChunkManager_r1_1(final UserConnection user, final long seed) {
        this();
        GenLayer[] agenlayer = GenLayer.func_35497_a(user, seed);
        biomeIndexLayer = agenlayer[1];
        temperatureLayer = agenlayer[2];
        rainfallLayer = agenlayer[3];
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

    public NewBiomeGenBase getBiomeGenAt(int i, int j) {
        return biomeCache.getBiomeGenAt(i, j);
    }

    public NewBiomeGenBase[] getBiomeGenAt(NewBiomeGenBase[] abiomegenbase, int i, int j, int k, int l, boolean flag) {
        IntCache.resetIntCache();
        if (abiomegenbase == null || abiomegenbase.length < k * l) {
            abiomegenbase = new NewBiomeGenBase[k * l];
        }
        if (flag && k == 16 && l == 16 && (i & 0xf) == 0 && (j & 0xf) == 0) {
            NewBiomeGenBase[] abiomegenbase1 = biomeCache.getCachedBiomes(i, j);
            System.arraycopy(abiomegenbase1, 0, abiomegenbase, 0, k * l);
            return abiomegenbase;
        }
        int[] ai = biomeIndexLayer.getInts(i, j, k, l);
        for (int i1 = 0; i1 < k * l; i1++) {
            abiomegenbase[i1] = NewBiomeGenBase.BIOME_LIST[ai[i1]];
        }

        return abiomegenbase;
    }

    public float[] getRainfall(float[] af, int i, int j, int k, int l) {
        IntCache.resetIntCache();
        if (af == null || af.length < k * l) {
            af = new float[k * l];
        }
        int[] ai = rainfallLayer.getInts(i, j, k, l);
        for (int i1 = 0; i1 < k * l; i1++) {
            float f = (float) ai[i1] / 65536F;
            if (f > 1.0F) {
                f = 1.0F;
            }
            af[i1] = f;
        }

        return af;
    }

    public float[] getTemperatures(float[] af, int i, int j, int k, int l) {
        IntCache.resetIntCache();
        if (af == null || af.length < k * l) {
            af = new float[k * l];
        }
        int[] ai = temperatureLayer.getInts(i, j, k, l);
        for (int i1 = 0; i1 < k * l; i1++) {
            float f = (float) ai[i1] / 65536F;
            if (f > 1.0F) {
                f = 1.0F;
            }
            af[i1] = f;
        }

        return af;
    }

    public void cleanupCache() {
        biomeCache.cleanupCache();
    }

}
