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
package net.raphimc.vialegacy.protocol.release.r1_2_4_5tor1_3_1_2.storage;

import com.viaversion.viaversion.api.connection.StorableObject;
import com.viaversion.viaversion.api.minecraft.Position;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ChestStateTracker implements StorableObject {

    private final Set<Position> openChests = new HashSet<>();

    public void openChest(final Position position) {
        this.openChests.add(position);
    }

    public void closeChest(final Position position) {
        this.openChests.remove(position);
    }

    public boolean isChestOpen(final Position position) {
        return this.openChests.contains(position);
    }

    public void clear() {
        this.openChests.clear();
    }

    public void unload(final int chunkX, final int chunkZ) {
        final Iterator<Position> it = this.openChests.iterator();
        while (it.hasNext()) {
            final Position entry = it.next();
            final int x = entry.x() >> 4;
            final int z = entry.z() >> 4;

            if (chunkX == x && chunkZ == z) {
                it.remove();
            }
        }
    }

}
