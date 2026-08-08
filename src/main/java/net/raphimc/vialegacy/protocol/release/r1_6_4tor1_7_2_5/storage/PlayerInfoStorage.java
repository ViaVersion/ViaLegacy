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
package net.raphimc.vialegacy.protocol.release.r1_6_4tor1_7_2_5.storage;

import com.viaversion.viaversion.api.connection.StorableObject;

public class PlayerInfoStorage implements StorableObject {

    private int entityId = -1;
    private boolean onGround = false;
    private double posX = 8;
    private double posY = 64;
    private double posZ = 8;
    private float yaw = -180;
    private float pitch = 0;

    public int getEntityId() {
        return this.entityId;
    }

    public void setEntityId(final int entityId) {
        this.entityId = entityId;
    }

    public void setOnGround(final boolean onGround) {
        this.onGround = onGround;
    }

    public double getPosX() {
        return this.posX;
    }

    public void setPosX(final double posX) {
        this.posX = posX;
    }

    public double getPosY() {
        return this.posY;
    }

    public void setPosY(final double posY) {
        this.posY = posY;
    }

    public double getPosZ() {
        return this.posZ;
    }

    public void setPosZ(final double posZ) {
        this.posZ = posZ;
    }

    public float getYaw() {
        return this.yaw;
    }

    public void setYaw(final float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public void setPitch(final float pitch) {
        this.pitch = pitch;
    }

}
