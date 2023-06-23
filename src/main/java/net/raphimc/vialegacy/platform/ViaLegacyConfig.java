/*
 * This file is part of ViaLegacy - https://github.com/RaphiMC/ViaLegacy
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
package net.raphimc.vialegacy.platform;

public interface ViaLegacyConfig {

    boolean isDynamicOnground();

    boolean isIgnoreLong1_8ChannelNames();

    boolean isLegacySkullLoading();

    boolean isLegacySkinLoading();

    boolean isSoundEmulation();

    boolean isOldBiomes();

    boolean isRemapBasedOnColor();

    int getClassicChunkRange();

    int getChunksPerTick();

    boolean enableClassicFly();

}
