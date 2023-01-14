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
package net.raphimc.vialegacy.api.model;

import java.util.Objects;

public class ChunkCoord {

    public int chunkX;
    public int chunkZ;

    public ChunkCoord(final int chunkX, final int chunkZ) {
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
    }

    public ChunkCoord(final long pos) {
        this.chunkX = (int) pos;
        this.chunkZ = (int) (pos >> 32);
    }

    public long toLong() {
        return toLong(this.chunkX, this.chunkZ);
    }

    public static long toLong(final int chunkX, final int chunkZ) {
        return (long) chunkX & 4294967295L | ((long) chunkZ & 4294967295L) << 32;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChunkCoord that = (ChunkCoord) o;
        return chunkX == that.chunkX &&
                chunkZ == that.chunkZ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(chunkX, chunkZ);
    }

    @Override
    public String toString() {
        return "ChunkCoord{" +
                "chunkX=" + chunkX +
                ", chunkZ=" + chunkZ +
                '}';
    }

}
