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
package net.raphimc.vialegacy.api.util;

import com.viaversion.viaversion.api.minecraft.GameProfile;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class GameProfileUtil {

    public static UUID getOfflinePlayerUuid(final String name) {
        return UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes(StandardCharsets.UTF_8));
    }

    public static boolean isOfflineGameProfile(final GameProfile gameProfile) {
        return gameProfile.id() != null && gameProfile.name() != null && gameProfile.id().equals(getOfflinePlayerUuid(gameProfile.name()));
    }

}
