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

package net.raphimc.vialegacy.api.model;

import java.util.Objects;

public record ChunkCoord(int x, int z) {

	public ChunkCoord add(final ChunkCoord other) {
		return new ChunkCoord(this.x + other.x, this.z + other.z);
	}

	public ChunkCoord subtract(final ChunkCoord other) {
		return new ChunkCoord(this.x - other.x, this.z - other.z);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		} else if (o == null || getClass() != o.getClass()) {
			return false;
		} else {
			final ChunkCoord that = (ChunkCoord) o;
			return this.x == that.x && this.z == that.z;
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.x, this.z);
	}

}
