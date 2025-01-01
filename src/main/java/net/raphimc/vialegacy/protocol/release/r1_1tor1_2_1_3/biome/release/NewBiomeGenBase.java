/*
 * This file is part of ViaLegacy - https://github.com/RaphiMC/ViaLegacy
 * Copyright (C) 2020-2025 RK_01/RaphiMC and contributors
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

public class NewBiomeGenBase {

    public static final NewBiomeGenBase[] BIOME_LIST = new NewBiomeGenBase[256];

    public static final NewBiomeGenBase ocean = new NewBiomeGenBase(0);
    public static final NewBiomeGenBase plains = new NewBiomeGenBase(1).setTemperatureRainfall(0.8F, 0.4F);
    public static final NewBiomeGenBase desert = new NewBiomeGenBase(2).setTemperatureRainfall(2.0F, 0.0F);
    public static final NewBiomeGenBase extremeHills = new NewBiomeGenBase(3).setTemperatureRainfall(0.2F, 0.3F);
    public static final NewBiomeGenBase forest = new NewBiomeGenBase(4).setTemperatureRainfall(0.7F, 0.8F);
    public static final NewBiomeGenBase taiga = new NewBiomeGenBase(5).setTemperatureRainfall(0.05F, 0.8F);
    public static final NewBiomeGenBase swampland = new NewBiomeGenBase(6).setTemperatureRainfall(0.8F, 0.9F);
    public static final NewBiomeGenBase river = new NewBiomeGenBase(7);
    public static final NewBiomeGenBase hell = new NewBiomeGenBase(8).setTemperatureRainfall(2.0F, 0.0F);
    public static final NewBiomeGenBase sky = new NewBiomeGenBase(9);
    public static final NewBiomeGenBase frozenOcean = new NewBiomeGenBase(10).setTemperatureRainfall(0.0F, 0.5F);
    public static final NewBiomeGenBase frozenRiver = new NewBiomeGenBase(11).setTemperatureRainfall(0.0F, 0.5F);
    public static final NewBiomeGenBase icePlains = new NewBiomeGenBase(12);
    public static final NewBiomeGenBase iceMountains = new NewBiomeGenBase(13).setTemperatureRainfall(0.0F, 0.5F);
    public static final NewBiomeGenBase mushroomIsland = new NewBiomeGenBase(14).setTemperatureRainfall(0.9F, 1.0F);
    public static final NewBiomeGenBase mushroomIslandShore = new NewBiomeGenBase(15).setTemperatureRainfall(0.9F, 1.0F);
    public static final NewBiomeGenBase beach = new NewBiomeGenBase(16).setTemperatureRainfall(0.8F, 0.4F);
    public static final NewBiomeGenBase desertHills = new NewBiomeGenBase(17).setTemperatureRainfall(2.0F, 0.0F);
    public static final NewBiomeGenBase forestHills = new NewBiomeGenBase(18).setTemperatureRainfall(0.7F, 0.8F);
    public static final NewBiomeGenBase taigaHills = new NewBiomeGenBase(19).setTemperatureRainfall(0.05F, 0.8F);
    public static final NewBiomeGenBase extremeHillsEdge = new NewBiomeGenBase(20).setTemperatureRainfall(0.2F, 0.3F);

    public static final NewBiomeGenBase jungle = new NewBiomeGenBase(21); //This is here for the OldWorldChunkManager
    public static final NewBiomeGenBase cold_taiga = new NewBiomeGenBase(30); //This is here for the OldWorldChunkManager
    public static final NewBiomeGenBase savanna = new NewBiomeGenBase(35); //This is here for the OldWorldChunkManager
    public static final NewBiomeGenBase mutatedJungleEdge = new NewBiomeGenBase(151); //This is here for the OldWorldChunkManager

    public final int biomeID;
    public float temperature;
    public float rainfall;

    protected NewBiomeGenBase(int i) {
        biomeID = i;
        if (i <= 20) {
            BIOME_LIST[i] = this;
        }
    }

    private NewBiomeGenBase setTemperatureRainfall(float f, float f1) {
        if (f > 0.1F && f < 0.2F) {
            throw new IllegalArgumentException("Please avoid temperatures in the range 0.1 - 0.2 because of snow");
        } else {
            temperature = f;
            rainfall = f1;
            return this;
        }
    }

    public final int getIntRainfall() {
        return (int) (rainfall * 65536F);
    }

    public final int getIntTemperature() {
        return (int) (temperature * 65536F);
    }

}
