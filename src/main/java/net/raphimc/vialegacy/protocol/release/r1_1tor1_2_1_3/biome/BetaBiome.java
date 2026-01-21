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

public enum BetaBiome {

    RAINFOREST,
    SWAMPLAND,
    SEASONAL_FOREST,
    FOREST,
    SAVANNA,
    SHRUBLAND,
    TAIGA,
    DESERT,
    PLAINS,
    ICE_DESERT,
    TUNDRA,
    HELL,
    SKY,
    ;

    public static BetaBiome getBiome(final double temperature, double humidity) {
        humidity *= temperature;
        if (temperature < 0.1D) {
            return TUNDRA;
        } else if (humidity < 0.2D) {
            if (temperature < 0.5D) {
                return TUNDRA;
            } else if (temperature < 0.95D) {
                return SAVANNA;
            } else {
                return DESERT;
            }
        } else if (humidity > 0.5D && temperature < 0.7D) {
            return SWAMPLAND;
        } else if (temperature < 0.5D) {
            return TAIGA;
        } else if (temperature < 0.97D) {
            if (humidity < 0.35D) {
                return SHRUBLAND;
            } else {
                return FOREST;
            }
        } else if (humidity < 0.45D) {
            return PLAINS;
        } else if (humidity < 0.9D) {
            return SEASONAL_FOREST;
        } else {
            return RAINFOREST;
        }
    }

}
