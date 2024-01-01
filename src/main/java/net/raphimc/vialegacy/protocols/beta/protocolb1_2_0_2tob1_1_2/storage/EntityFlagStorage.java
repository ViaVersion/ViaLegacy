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
package net.raphimc.vialegacy.protocols.beta.protocolb1_2_0_2tob1_1_2.storage;

import com.viaversion.viaversion.api.connection.StorableObject;
import com.viaversion.viaversion.libs.fastutil.ints.Int2IntMap;
import com.viaversion.viaversion.libs.fastutil.ints.Int2IntOpenHashMap;

public class EntityFlagStorage implements StorableObject {

    private final Int2IntMap animationFlags = new Int2IntOpenHashMap();

    public boolean getFlag(final int entityId, final int index) {
        return (this.getFlagMask(entityId) & 1 << index) != 0;
    }

    public int getFlagMask(final int entityId) {
        return this.animationFlags.get(entityId);
    }

    public void setFlag(final int entityId, final int index, final boolean flag) {
        final int mask = this.animationFlags.get(entityId);
        if (flag) {
            this.animationFlags.put(entityId, mask | 1 << index);
        } else {
            this.animationFlags.put(entityId, mask & ~(1 << index));
        }
    }

}
