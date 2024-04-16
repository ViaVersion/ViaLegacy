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
package net.raphimc.vialegacy.protocols.release.protocol1_2_1_3to1_1.model;

import com.viaversion.viaversion.api.minecraft.Position;
import com.viaversion.viaversion.util.IdAndData;

public class PendingBlockEntry {

    private final Position position;
    private final IdAndData block;
    private int countdown = 80;

    public PendingBlockEntry(final Position position, final IdAndData block) {
        this.position = position;
        this.block = block;
    }

    public Position getPosition() {
        return this.position;
    }

    public IdAndData getBlock() {
        return this.block;
    }

    public boolean decrementAndCheckIsExpired() {
        return --this.countdown <= 0;
    }

}
