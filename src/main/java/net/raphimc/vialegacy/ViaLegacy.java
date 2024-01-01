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
package net.raphimc.vialegacy;

import net.raphimc.vialegacy.platform.ViaLegacyConfig;
import net.raphimc.vialegacy.platform.ViaLegacyPlatform;

public class ViaLegacy {

    public static final String VERSION = "${version}";
    public static final String IMPL_VERSION = "${impl_version}";

    private static ViaLegacyPlatform platform;
    private static ViaLegacyConfig config;

    private ViaLegacy() {
    }

    public static void init(final ViaLegacyPlatform platform, final ViaLegacyConfig config) {
        if (ViaLegacy.platform != null) throw new IllegalStateException("ViaLegacy is already initialized");

        ViaLegacy.platform = platform;
        ViaLegacy.config = config;
    }

    public static ViaLegacyPlatform getPlatform() {
        return ViaLegacy.platform;
    }

    public static ViaLegacyConfig getConfig() {
        return ViaLegacy.config;
    }

}
