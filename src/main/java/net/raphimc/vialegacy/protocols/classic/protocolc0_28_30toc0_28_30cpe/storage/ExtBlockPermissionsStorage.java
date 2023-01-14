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
package net.raphimc.vialegacy.protocols.classic.protocolc0_28_30toc0_28_30cpe.storage;

import com.viaversion.viaversion.api.connection.StoredObject;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.libs.fastutil.ints.IntOpenHashSet;
import com.viaversion.viaversion.libs.fastutil.ints.IntSet;

public class ExtBlockPermissionsStorage extends StoredObject {

    private final IntSet placingAllowed = new IntOpenHashSet();
    private final IntSet breakingAllowed = new IntOpenHashSet();

    public ExtBlockPermissionsStorage(final UserConnection user) {
        super(user);
    }

    public void addPlaceable(final int block) {
        this.placingAllowed.add(block);
    }

    public void addBreakable(final int block) {
        this.breakingAllowed.add(block);
    }

    public void removePlaceable(final int block) {
        this.placingAllowed.remove(block);
    }

    public void removeBreakable(final int block) {
        this.breakingAllowed.remove(block);
    }

    public boolean isPlacingAllowed(final int block) {
        return this.placingAllowed.contains(block);
    }

    public boolean isBreakingAllowed(final int block) {
        return this.breakingAllowed.contains(block);
    }

}
