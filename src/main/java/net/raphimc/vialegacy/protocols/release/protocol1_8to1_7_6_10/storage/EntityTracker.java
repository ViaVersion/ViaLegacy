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
package net.raphimc.vialegacy.protocols.release.protocol1_8to1_7_6_10.storage;

import com.viaversion.viaversion.api.connection.StoredObject;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.entities.EntityTypes1_10;
import com.viaversion.viaversion.api.minecraft.entitydata.EntityData;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectMap;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectOpenHashMap;
import net.raphimc.vialegacy.api.model.Location;
import net.raphimc.vialegacy.protocols.release.protocol1_8to1_7_6_10.metadata.MetaIndex1_8to1_7_6;
import net.raphimc.vialegacy.protocols.release.protocol1_8to1_7_6_10.model.HologramPartEntity;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

public class EntityTracker extends StoredObject {

    private final Map<Integer, EntityTypes1_10.EntityType> entityMap = new ConcurrentHashMap<>();
    private final Map<Integer, Boolean> groundMap = new ConcurrentHashMap<>();
    private final Int2ObjectMap<HologramPartEntity> hologramParts = new Int2ObjectOpenHashMap<>();
    private final Int2ObjectMap<HologramPartEntity> virtualHolograms = new Int2ObjectOpenHashMap<>();

    private int playerID;

    public EntityTracker(UserConnection user) {
        super(user);
    }

    public int getPlayerID() {
        return this.playerID;
    }

    public void setPlayerID(final int playerID) {
        this.playerID = playerID;
    }

    public Map<Integer, EntityTypes1_10.EntityType> getTrackedEntities() {
        return this.entityMap;
    }

    public Map<Integer, Boolean> getGroundMap() {
        return this.groundMap;
    }

    public Int2ObjectMap<HologramPartEntity> getVirtualHolograms() {
        return this.virtualHolograms;
    }

    public void trackEntity(final int entityId, final EntityTypes1_10.EntityType entityType) {
        if (this.virtualHolograms.containsKey(entityId)) {
            final int newMappedEntityId = this.getNextMappedEntityId();
            final HologramPartEntity hologramPartEntity = this.virtualHolograms.remove(entityId);
            hologramPartEntity.relocate(newMappedEntityId);
            this.hologramParts.put(newMappedEntityId, hologramPartEntity);
        }
        if (this.entityMap.containsKey(entityId)) {
            this.removeEntity(entityId);
        }

        this.entityMap.put(entityId, entityType);

        if (entityType == EntityTypes1_10.EntityType.HORSE || entityType == EntityTypes1_10.EntityType.WITHER_SKULL) {
            this.hologramParts.put(entityId, new HologramPartEntity(this.getUser(), entityId, entityType));
        }
    }

    public void removeEntity(final int entityId) {
        this.entityMap.remove(entityId);
        this.groundMap.remove(entityId);
        final HologramPartEntity removedEntity = this.hologramParts.remove(entityId);
        if (removedEntity != null) {
            if (removedEntity.getRiderEntity() != null) {
                removedEntity.getRiderEntity().setVehicleEntity(null);
            }
            if (removedEntity.getVehicleEntity() != null) {
                removedEntity.setVehicleEntity(null);
            }
            removedEntity.onRemove();
        }
    }

    public void updateEntityLocation(final int entityId, final int x, final int y, final int z, final boolean relative) {
        final HologramPartEntity entity = this.hologramParts.get(entityId);
        if (entity != null) {
            final Location oldLoc = entity.getLocation();

            final double xPos = x / 32.0D;
            final double yPos = y / 32.0D;
            final double zPos = z / 32.0D;

            Location newLoc;
            if (relative) {
                newLoc = new Location(oldLoc.getX() + xPos, oldLoc.getY() + yPos, oldLoc.getZ() + zPos);
            } else {
                newLoc = new Location(xPos, yPos, zPos);
            }

            entity.setLocation(newLoc);
        }
    }

    public void updateEntityMetadata(final int entityId, final List<EntityData> metadataList) {
        final HologramPartEntity entity = this.hologramParts.get(entityId);
        if (entity != null) {
            for (EntityData metadata : metadataList) {
                final MetaIndex1_8to1_7_6 metaIndex = MetaIndex1_8to1_7_6.searchIndex(entity.getEntityType(), metadata.id());
                if (metaIndex != null) {
                    try {
                        metadata.setTypeAndValue(metaIndex.getOldType(), metadata.getValue());
                    } catch (Throwable ignored) {
                        continue;
                    }
                    entity.setMetadata(metaIndex, metadata.getValue());
                }
            }
            entity.onChange();
        }
    }

    public void updateEntityAttachState(final int ridingId, final int vehicleId) {
        final HologramPartEntity ridingEntity = this.hologramParts.get(ridingId);
        if (ridingEntity != null) {
            ridingEntity.setVehicleEntity(this.hologramParts.get(vehicleId));
        }
    }

    public void clear() {
        this.entityMap.clear();
        this.groundMap.clear();
        for (HologramPartEntity hologramPartEntity : this.hologramParts.values()) {
            hologramPartEntity.onRemove();
        }
        this.virtualHolograms.clear();
    }

    public int getNextMappedEntityId() {
        int id;
        do {
            id = ThreadLocalRandom.current().nextInt(1_000_000_000, Integer.MAX_VALUE);
        } while (this.entityMap.containsKey(id) || this.virtualHolograms.containsKey(id));
        return id;
    }

}
