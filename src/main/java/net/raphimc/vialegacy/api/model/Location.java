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

import com.viaversion.viaversion.api.minecraft.BlockPosition;

import java.util.Objects;

public class Location {

	private double x;
	private double y;
	private double z;

	public Location(final BlockPosition position) {
		this(position.x(), position.y(), position.z());
	}

	public Location(final double x, final double y, final double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void setX(final double x) {
		this.x = x;
	}

	public double getX() {
		return this.x;
	}

	public void setY(final double y) {
		this.y = y;
	}

	public double getY() {
		return this.y;
	}

	public void setZ(final double z) {
		this.z = z;
	}

	public double getZ() {
		return this.z;
	}

	public double distanceTo(final Location p2) {
		return Math.sqrt(Math.pow(p2.getX() - this.x, 2) +
				Math.pow(p2.getY() - this.y, 2) +
				Math.pow(p2.getZ() - this.z, 2)
		);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		} else if (o == null || getClass() != o.getClass()) {
			return false;
		} else {
			final Location location = (Location) o;
			return Double.compare(location.x, x) == 0 && Double.compare(location.y, y) == 0 && Double.compare(location.z, z) == 0;
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y, z);
	}

	@Override
	public String toString() {
		return String.format("Location{x=%f, y=%f, z=%f}", x, y, z);
	}

}
