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
package net.raphimc.vialegacy.protocol.classic.c0_30cpetoc0_28_30.storage;

import com.viaversion.viaversion.api.connection.StorableObject;
import com.viaversion.viaversion.libs.fastutil.ints.IntOpenHashSet;
import com.viaversion.viaversion.libs.fastutil.ints.IntSet;

public class ExtBlockPermissionsStorage implements StorableObject {

    private final IntSet placingDenied = new IntOpenHashSet();
    private final IntSet breakingDenied = new IntOpenHashSet();

    public void addPlaceable(final int block) {
        this.placingDenied.remove(block);
    }

    public void addBreakable(final int block) {
        this.breakingDenied.remove(block);
    }

    public void removePlaceable(final int block) {
        this.placingDenied.add(block);
    }

    public void removeBreakable(final int block) {
        this.breakingDenied.add(block);
    }

    public boolean isPlacingDenied(final int block) {
        return this.placingDenied.contains(block);
    }

    public boolean isBreakingDenied(final int block) {
        return this.breakingDenied.contains(block);
    }

}
