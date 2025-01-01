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
package net.raphimc.vialegacy.protocol.classic.c0_28_30toa1_0_15.model;

import com.viaversion.viaversion.api.minecraft.BlockPosition;
import net.raphimc.vialegacy.protocol.classic.c0_28_30toa1_0_15.data.ClassicBlocks;

public class ClassicLevel {

    private final int sizeX;
    private final int sizeY;
    private final int sizeZ;

    private final byte[] blocks;
    private final int[] lightBlocking;

    public ClassicLevel(final int sizeX, final int sizeY, final int sizeZ) {
        this(sizeX, sizeY, sizeZ, new byte[sizeX * sizeY * sizeZ], new int[sizeX * sizeZ]);
    }

    public ClassicLevel(final int sizeX, final int sizeY, final int sizeZ, final byte[] blocks) {
        this(sizeX, sizeY, sizeZ, blocks, new int[sizeX * sizeZ]);
    }

    public ClassicLevel(final int sizeX, final int sizeY, final int sizeZ, final byte[] blocks, final int[] lightBlocking) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
        this.blocks = blocks;
        this.lightBlocking = lightBlocking;
    }

    public void calculateLight(int startX, int startZ, int sizeX, int sizeZ) {
        for (int x = startX; x < startX + sizeX; x++) {
            for (int z = startZ; z < startZ + sizeZ; z++) {
                if (!this.isInBounds(x, 0, z)) continue;
                int y = this.sizeY - 1;
                while (y > 0 && !this.isLightBlocking(x, y, z)) {
                    y--;
                }
                this.lightBlocking[x + z * this.sizeX] = y;
            }
        }
    }

    public int getSizeX() {
        return this.sizeX;
    }

    public int getSizeY() {
        return this.sizeY;
    }

    public int getSizeZ() {
        return this.sizeZ;
    }

    public int getBlock(BlockPosition position) {
        return this.getBlock(position.x(), position.y(), position.z());
    }

    public int getBlock(int x, int y, int z) {
        return this.isInBounds(x, y, z) ? this.blocks[(y * this.sizeZ + z) * this.sizeX + x] & 255 : 0;
    }

    public void setBlock(BlockPosition position, int block) {
        this.setBlock(position.x(), position.y(), position.z(), block);
    }

    public void setBlock(int x, int y, int z, int block) {
        if (this.isInBounds(x, y, z)) {
            this.blocks[(y * this.sizeZ + z) * this.sizeX + x] = (byte) block;
            this.calculateLight(x, z, 1, 1);
        }
    }

    public boolean isLightBlocking(BlockPosition position) {
        return this.isLightBlocking(position.x(), position.y(), position.z());
    }

    public boolean isLightBlocking(int x, int y, int z) {
        return switch (this.getBlock(x, y, z)) {
            case ClassicBlocks.AIR, ClassicBlocks.WATER, ClassicBlocks.STATIONARY_WATER, ClassicBlocks.LAVA, ClassicBlocks.STATIONARY_LAVA, ClassicBlocks.LEAVES,
                 ClassicBlocks.DANDELION, ClassicBlocks.ROSE, ClassicBlocks.BROWN_MUSHROOM, ClassicBlocks.RED_MUSHROOM, ClassicBlocks.GLASS -> false;
            default -> true;
        };
    }

    public boolean isLit(BlockPosition position) {
        return this.isLit(position.x(), position.y(), position.z());
    }

    public boolean isLit(int x, int y, int z) {
        return !this.isInBounds(x, y, z) || y >= this.lightBlocking[x + z * this.sizeX];
    }

    public boolean isInBounds(int x, int y, int z) {
        return x >= 0 && y >= 0 && z >= 0 && x < this.sizeX && y < this.sizeY && z < this.sizeZ;
    }

}
