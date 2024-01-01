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
package net.raphimc.vialegacy.protocols.release.protocol1_2_1_3to1_1.biome;

import net.raphimc.vialegacy.protocols.release.protocol1_2_1_3to1_1.biome.release.NewBiomeGenBase;

import java.util.Arrays;

public class NetherBiomeGenerator implements IWorldChunkManager {

    private static final byte[] NETHER_BIOME_DATA;

    static {
        NETHER_BIOME_DATA = new byte[256];
        Arrays.fill(NETHER_BIOME_DATA, (byte) NewBiomeGenBase.hell.biomeID);
    }

    @Override
    public byte[] getBiomeDataAt(int chunkX, int chunkZ) {
        return NETHER_BIOME_DATA;
    }

}
