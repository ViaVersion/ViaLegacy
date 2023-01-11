/*
 * This file is part of ViaProtocolHack - https://github.com/RaphiMC/ViaProtocolHack
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
package net.raphimc.vialegacy.protocols.release.protocol1_3_1_2to1_2_4_5.model;

import com.viaversion.viaversion.api.minecraft.entities.Entity1_10Types;
import net.raphimc.vialegacy.api.model.Location;

public abstract class AbstractTrackedEntity {

    private int entityId;
    private Location location;
    private Entity1_10Types.EntityType entityType;

    private boolean isRiding;

    public AbstractTrackedEntity(final int entityId, final Location location, final Entity1_10Types.EntityType entityType) {
        this.entityId = entityId;
        this.location = location;
        this.entityType = entityType;
    }

    public int getEntityId() {
        return this.entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Entity1_10Types.EntityType getEntityType() {
        return this.entityType;
    }

    public void setEntityType(Entity1_10Types.EntityType entityType) {
        this.entityType = entityType;
    }

    public boolean isRiding() {
        return this.isRiding;
    }

    public void setRiding(boolean riding) {
        this.isRiding = riding;
    }

}
