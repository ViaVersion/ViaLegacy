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
package net.raphimc.vialegacy.api.util;

import net.raphimc.vialegacy.api.model.ChunkCoord;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;

public class ChunkCoordSpiral implements Iterable<ChunkCoord> {

    private final ChunkCoord center;
    private final ChunkCoord lowerBound;
    private final ChunkCoord upperBound;
    private final int step;

    private boolean returnCenter = true;

    public ChunkCoordSpiral(final ChunkCoord center, final ChunkCoord lowerBound, final ChunkCoord upperBound, final int step) {
        this.center = center;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.step = step;
    }

    public ChunkCoordSpiral(final ChunkCoord center, final ChunkCoord radius, final int step) {
        this(center, new ChunkCoord(center.getChunkX() - radius.getChunkX(), center.getChunkZ() - radius.getChunkZ()), new ChunkCoord(center.getChunkX() + radius.getChunkX(), center.getChunkZ() + radius.getChunkZ()), step);
    }

    public ChunkCoordSpiral(final ChunkCoord center, final ChunkCoord radius) {
        this(center, radius, 1);
    }

    @Override
    public Iterator<ChunkCoord> iterator() {
        return new Iterator<>() {
            int x = ChunkCoordSpiral.this.center.getChunkX();
            int z = ChunkCoordSpiral.this.center.getChunkZ();

            float n = 1;
            int floorN = 1;
            int i = 0;
            int j = 0;

            @Override
            public boolean hasNext() {
                return ChunkCoordSpiral.this.returnCenter || this.x >= ChunkCoordSpiral.this.lowerBound.getChunkX() && this.x <= ChunkCoordSpiral.this.upperBound.getChunkX() && this.z >= ChunkCoordSpiral.this.lowerBound.getChunkZ() && this.z <= ChunkCoordSpiral.this.upperBound.getChunkZ();
            }

            @Override
            public ChunkCoord next() {
                if (ChunkCoordSpiral.this.returnCenter) {
                    ChunkCoordSpiral.this.returnCenter = false;
                    return new ChunkCoord(this.x, this.z);
                }

                this.floorN = (int) Math.floor(this.n);
                if (this.j < this.floorN) {
                    switch (this.i % 4) {
                        case 0 -> this.z += ChunkCoordSpiral.this.step;
                        case 1 -> this.x += ChunkCoordSpiral.this.step;
                        case 2 -> this.z -= ChunkCoordSpiral.this.step;
                        case 3 -> this.x -= ChunkCoordSpiral.this.step;
                    }
                    this.j++;
                    return new ChunkCoord(this.x, this.z);
                }
                this.j = 0;
                this.n += 0.5F;
                this.i++;
                return this.next();
            }
        };
    }

    @Override
    public Spliterator<ChunkCoord> spliterator() {
        return Spliterators.spliteratorUnknownSize(this.iterator(), Spliterator.ORDERED);
    }

}
