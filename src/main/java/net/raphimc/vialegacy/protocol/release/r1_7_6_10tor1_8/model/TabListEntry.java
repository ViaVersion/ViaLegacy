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
package net.raphimc.vialegacy.protocol.release.r1_7_6_10tor1_8.model;

import com.google.common.base.Charsets;

import java.util.UUID;

public class TabListEntry {

    public GameProfile gameProfile;
    public int ping;
    public int gameMode;

    public boolean resolved;

    public TabListEntry(final String name, final UUID uuid) {
        this.gameProfile = new GameProfile(name, uuid);
        this.resolved = true;
    }

    public TabListEntry(final String name, final short ping) {
        this.gameProfile = new GameProfile(name, UUID.nameUUIDFromBytes(("LegacyPlayer:" + name).getBytes(Charsets.UTF_8)));
        this.ping = ping;
    }

}
