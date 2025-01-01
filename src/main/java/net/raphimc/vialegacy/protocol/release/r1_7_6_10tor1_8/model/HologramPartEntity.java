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
package net.raphimc.vialegacy.protocol.release.r1_7_6_10tor1_8.model;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.entities.EntityTypes1_8;
import com.viaversion.viaversion.api.minecraft.entitydata.EntityData;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.type.Types;
import com.viaversion.viaversion.api.type.types.version.Types1_8;
import com.viaversion.viaversion.protocols.v1_8to1_9.data.EntityDataIndex1_9;
import com.viaversion.viaversion.protocols.v1_8to1_9.packet.ClientboundPackets1_8;
import net.raphimc.vialegacy.api.model.Location;
import net.raphimc.vialegacy.protocol.release.r1_7_6_10tor1_8.Protocolr1_7_6_10Tor1_8;
import net.raphimc.vialegacy.protocol.release.r1_7_6_10tor1_8.data.EntityDataIndex1_7_6;
import net.raphimc.vialegacy.protocol.release.r1_7_6_10tor1_8.storage.EntityTracker;

import java.util.*;

public class HologramPartEntity {

    private static final float HORSE_HEIGHT = 1.6F;
    private static final float WITHER_SKULL_HEIGHT = 0.3125F;

    private final UserConnection user;
    private final EntityTracker entityTracker;

    private final int entityId;
    private final EntityTypes1_8.EntityType entityType;
    private HologramPartEntity riderEntity;
    private HologramPartEntity vehicleEntity;
    private Location location;
    private final Map<EntityDataIndex1_7_6, Object> entityData = new HashMap<>();

    private Integer mappedEntityId;

    public HologramPartEntity(final UserConnection user, final int entityId, final EntityTypes1_8.EntityType entityType) {
        this.user = user;
        this.entityTracker = this.user.get(EntityTracker.class);

        this.entityId = entityId;
        this.entityType = entityType;
        this.location = new Location(Float.NaN, Float.NaN, Float.NaN);

        if (entityType == EntityTypes1_8.EntityType.HORSE) {
            this.entityData.put(EntityDataIndex1_7_6.ENTITY_FLAGS, (byte) 0);
            this.entityData.put(EntityDataIndex1_7_6.ENTITY_LIVING_NAME_TAG_VISIBILITY, (byte) 0);
            this.entityData.put(EntityDataIndex1_7_6.ENTITY_LIVING_NAME_TAG, "");
            this.entityData.put(EntityDataIndex1_7_6.ENTITY_AGEABLE_AGE, 0);
        }
    }

    public void onChange() {
        if (this.vehicleEntity == null && this.riderEntity != null) {
            this.riderEntity.setPositionFromVehicle();
        }

        if (this.isHologram()) {
            if (this.wouldBeInvisible()) {
                this.destroyHologramPartEntities();
                this.destroyArmorStand();
                return;
            }

            if (this.mappedEntityId == null) {
                this.mappedEntityId = this.entityTracker.getNextMappedEntityId();
                this.entityTracker.getVirtualHolograms().put(this.mappedEntityId.intValue(), this);
                this.destroyHologramPartEntities();
                this.spawnArmorStand();
            }

            this.updateArmorStand();
        } else if (this.mappedEntityId != null) {
            this.onRemove();
        }
    }

    public void onRemove() {
        if (this.mappedEntityId != null) {
            this.entityTracker.getVirtualHolograms().remove(this.mappedEntityId.intValue());
            this.destroyArmorStand();
            this.spawnHologramPartEntities();
            this.mappedEntityId = null;
        }
    }

    public void relocate(final int newMappedEntityId) {
        this.destroyArmorStand();
        this.mappedEntityId = newMappedEntityId;
        this.spawnArmorStand();
        this.updateArmorStand();
    }

    private void spawnHologramPartEntities() {
        {
            final PacketWrapper spawnMob = PacketWrapper.create(ClientboundPackets1_8.ADD_MOB, this.user);
            spawnMob.write(Types.VAR_INT, this.entityId); // entity id
            spawnMob.write(Types.UNSIGNED_BYTE, (short) this.entityType.getId()); // type id
            spawnMob.write(Types.INT, (int) (this.location.getX() * 32F)); // x
            spawnMob.write(Types.INT, (int) (this.location.getY() * 32F)); // y
            spawnMob.write(Types.INT, (int) (this.location.getZ() * 32F)); // z
            spawnMob.write(Types.BYTE, (byte) 0); // yaw
            spawnMob.write(Types.BYTE, (byte) 0); // pitch
            spawnMob.write(Types.BYTE, (byte) 0); // head yaw
            spawnMob.write(Types.SHORT, (short) 0); // velocity x
            spawnMob.write(Types.SHORT, (short) 0); // velocity y
            spawnMob.write(Types.SHORT, (short) 0); // velocity z
            spawnMob.write(Types1_8.ENTITY_DATA_LIST, this.get1_8EntityData()); // entity data
            spawnMob.send(Protocolr1_7_6_10Tor1_8.class);
        }
        if (this.vehicleEntity != null) {
            final int objectId = Arrays.stream(EntityTypes1_8.ObjectType.values()).filter(o -> o.getType() == this.vehicleEntity.entityType).map(EntityTypes1_8.ObjectType::getId).findFirst().orElse(-1);
            if (objectId == -1) {
                throw new IllegalStateException("Could not find object id for entity type " + this.vehicleEntity.entityType);
            }

            final PacketWrapper spawnEntity = PacketWrapper.create(ClientboundPackets1_8.ADD_ENTITY, this.user);
            spawnEntity.write(Types.VAR_INT, this.vehicleEntity.entityId); // entity id
            spawnEntity.write(Types.BYTE, (byte) objectId); // type id
            spawnEntity.write(Types.INT, (int) (this.vehicleEntity.location.getX() * 32F)); // x
            spawnEntity.write(Types.INT, (int) (this.vehicleEntity.location.getY() * 32F)); // y
            spawnEntity.write(Types.INT, (int) (this.vehicleEntity.location.getZ() * 32F)); // z
            spawnEntity.write(Types.BYTE, (byte) 0); // yaw
            spawnEntity.write(Types.BYTE, (byte) 0); // pitch
            spawnEntity.write(Types.INT, 0); // data
            spawnEntity.send(Protocolr1_7_6_10Tor1_8.class);

            final PacketWrapper setEntityData = PacketWrapper.create(ClientboundPackets1_8.SET_ENTITY_DATA, this.user);
            setEntityData.write(Types.VAR_INT, this.vehicleEntity.entityId); // entity id
            setEntityData.write(Types1_8.ENTITY_DATA_LIST, this.vehicleEntity.get1_8EntityData()); // entity data
            setEntityData.send(Protocolr1_7_6_10Tor1_8.class);

            final PacketWrapper attachEntity = PacketWrapper.create(ClientboundPackets1_8.SET_ENTITY_LINK, this.user);
            attachEntity.write(Types.INT, this.entityId); // entity id
            attachEntity.write(Types.INT, this.vehicleEntity.entityId); // vehicle id
            attachEntity.write(Types.UNSIGNED_BYTE, (short) 0); // leash state
            attachEntity.send(Protocolr1_7_6_10Tor1_8.class);
        }
    }

    private void destroyHologramPartEntities() {
        final PacketWrapper destroyEntities = PacketWrapper.create(ClientboundPackets1_8.REMOVE_ENTITIES, this.user);
        destroyEntities.write(Types.VAR_INT_ARRAY_PRIMITIVE, new int[]{this.entityId, this.vehicleEntity.entityId}); // entity ids
        destroyEntities.scheduleSend(Protocolr1_7_6_10Tor1_8.class);
    }

    private void spawnArmorStand() {
        final PacketWrapper spawnMob = PacketWrapper.create(ClientboundPackets1_8.ADD_MOB, this.user);
        spawnMob.write(Types.VAR_INT, this.mappedEntityId); // entity id
        spawnMob.write(Types.UNSIGNED_BYTE, (short) EntityTypes1_8.EntityType.ARMOR_STAND.getId()); // type id
        spawnMob.write(Types.INT, (int) (this.location.getX() * 32F)); // x
        spawnMob.write(Types.INT, (int) ((this.location.getY() + this.getHeight()) * 32F)); // y
        spawnMob.write(Types.INT, (int) (this.location.getZ() * 32F)); // z
        spawnMob.write(Types.BYTE, (byte) 0); // yaw
        spawnMob.write(Types.BYTE, (byte) 0); // pitch
        spawnMob.write(Types.BYTE, (byte) 0); // head yaw
        spawnMob.write(Types.SHORT, (short) 0); // velocity x
        spawnMob.write(Types.SHORT, (short) 0); // velocity y
        spawnMob.write(Types.SHORT, (short) 0); // velocity z
        spawnMob.write(Types1_8.ENTITY_DATA_LIST, this.getArmorStandEntityData()); // entity data
        spawnMob.send(Protocolr1_7_6_10Tor1_8.class);
    }

    private void destroyArmorStand() {
        if (this.mappedEntityId == null) return;

        final PacketWrapper destroyEntities = PacketWrapper.create(ClientboundPackets1_8.REMOVE_ENTITIES, this.user);
        destroyEntities.write(Types.VAR_INT_ARRAY_PRIMITIVE, new int[]{this.mappedEntityId}); // entity ids
        destroyEntities.send(Protocolr1_7_6_10Tor1_8.class);
    }

    private void updateArmorStand() {
        if (this.mappedEntityId == null) return;

        final PacketWrapper setEntityData = PacketWrapper.create(ClientboundPackets1_8.SET_ENTITY_DATA, this.user);
        setEntityData.write(Types.VAR_INT, this.mappedEntityId); // entity id
        setEntityData.write(Types1_8.ENTITY_DATA_LIST, this.getArmorStandEntityData()); // entity data
        setEntityData.send(Protocolr1_7_6_10Tor1_8.class);

        final PacketWrapper entityTeleport = PacketWrapper.create(ClientboundPackets1_8.TELEPORT_ENTITY, this.user);
        entityTeleport.write(Types.VAR_INT, this.mappedEntityId); // entity id
        entityTeleport.write(Types.INT, (int) (this.location.getX() * 32F)); // x
        entityTeleport.write(Types.INT, (int) ((this.location.getY() + this.getHeight()) * 32F)); // y
        entityTeleport.write(Types.INT, (int) (this.location.getZ() * 32F)); // z
        entityTeleport.write(Types.BYTE, (byte) 0); // yaw
        entityTeleport.write(Types.BYTE, (byte) 0); // pitch
        entityTeleport.write(Types.BOOLEAN, false); // onGround
        entityTeleport.send(Protocolr1_7_6_10Tor1_8.class);
    }

    private boolean isHologram() {
        if (this.entityType != EntityTypes1_8.EntityType.HORSE) return false;
        if (this.vehicleEntity == null) return false;
        if (this.riderEntity != null) return false;
        if (this.vehicleEntity.riderEntity != this) return false;
        if (this.vehicleEntity.vehicleEntity != null) return false;

        return ((int) this.getEntityData(EntityDataIndex1_7_6.ENTITY_AGEABLE_AGE)) <= -44_000;
    }

    private boolean wouldBeInvisible() {
        if (this.entityType != EntityTypes1_8.EntityType.HORSE) return false;

        final int age = (int) this.getEntityData(EntityDataIndex1_7_6.ENTITY_AGEABLE_AGE);
        return age >= -50_000;
    }

    private float getHeight() {
        if (this.entityType == EntityTypes1_8.EntityType.HORSE) {
            final int age = (int) this.getEntityData(EntityDataIndex1_7_6.ENTITY_AGEABLE_AGE);
            final float size = age >= 0 ? 1F : (0.5F + (-24_000F - age) / -24_000F * 0.5F);
            return HORSE_HEIGHT * size;
        } else {
            return WITHER_SKULL_HEIGHT;
        }
    }

    private void setPositionFromVehicle() {
        if (this.vehicleEntity != null) {
            this.location = new Location(this.vehicleEntity.location.getX(), this.vehicleEntity.location.getY() + this.vehicleEntity.getHeight() * 0.75F, this.vehicleEntity.location.getZ());
        }
        if (this.riderEntity != null) {
            this.riderEntity.setPositionFromVehicle();
        }
    }

    public int getEntityId() {
        return this.entityId;
    }

    public EntityTypes1_8.EntityType getEntityType() {
        return this.entityType;
    }

    public void setVehicleEntity(final HologramPartEntity vehicleEntity) {
        if (vehicleEntity == null) {
            if (this.vehicleEntity != null) {
                this.location = this.vehicleEntity.location;
                this.location = new Location(this.location.getX(), this.location.getY() + (this.vehicleEntity.entityType == EntityTypes1_8.EntityType.HORSE ? HORSE_HEIGHT : WITHER_SKULL_HEIGHT), this.location.getZ());
                this.vehicleEntity.riderEntity = null;
                this.vehicleEntity.onChange();
            }

            this.vehicleEntity = null;
        } else {
            if (this.vehicleEntity != null) {
                this.vehicleEntity.riderEntity = null;
                this.vehicleEntity.onChange();
            }

            for (HologramPartEntity entity = vehicleEntity.vehicleEntity; entity != null; entity = entity.riderEntity) {
                if (entity == this) return;
            }

            this.vehicleEntity = vehicleEntity;
            vehicleEntity.riderEntity = this;
            vehicleEntity.onChange();
        }

        this.onChange();
    }

    public HologramPartEntity getVehicleEntity() {
        return this.vehicleEntity;
    }

    public HologramPartEntity getRiderEntity() {
        return this.riderEntity;
    }

    public void setLocation(final Location location) {
        this.location = location;
        this.onChange();
    }

    public Location getLocation() {
        return this.location;
    }

    public void setEntityData(final EntityDataIndex1_7_6 index, final Object value) {
        this.entityData.put(index, value);
        // The onChange() method is called from the EntityTracker
    }

    public Object getEntityData(final EntityDataIndex1_7_6 index) {
        return this.entityData.get(index);
    }

    private List<EntityData> get1_8EntityData() {
        final List<EntityData> entityDataList = new ArrayList<>();
        for (final Map.Entry<EntityDataIndex1_7_6, Object> entry : this.entityData.entrySet()) {
            entityDataList.add(new EntityData(entry.getKey().getOldIndex(), entry.getKey().getOldType(), entry.getValue()));
        }
        Via.getManager().getProtocolManager().getProtocol(Protocolr1_7_6_10Tor1_8.class).getEntityDataRewriter().transform(this.user, this.entityType, entityDataList);
        return entityDataList;
    }

    private List<EntityData> getArmorStandEntityData() {
        final List<EntityData> entityDataList = new ArrayList<>();
        if (this.entityType == EntityTypes1_8.EntityType.HORSE) {
            entityDataList.add(new EntityData(EntityDataIndex1_7_6.ENTITY_LIVING_NAME_TAG_VISIBILITY.getNewIndex(), EntityDataIndex1_7_6.ENTITY_LIVING_NAME_TAG_VISIBILITY.getNewType(), this.getEntityData(EntityDataIndex1_7_6.ENTITY_LIVING_NAME_TAG_VISIBILITY)));
            entityDataList.add(new EntityData(EntityDataIndex1_7_6.ENTITY_LIVING_NAME_TAG.getNewIndex(), EntityDataIndex1_7_6.ENTITY_LIVING_NAME_TAG.getNewType(), this.getEntityData(EntityDataIndex1_7_6.ENTITY_LIVING_NAME_TAG)));
            entityDataList.add(new EntityData(EntityDataIndex1_7_6.ENTITY_FLAGS.getNewIndex(), EntityDataIndex1_7_6.ENTITY_FLAGS.getNewType(), (byte) (1 << 5)));
            entityDataList.add(new EntityData(EntityDataIndex1_9.ARMOR_STAND_INFO.getIndex(), EntityDataIndex1_9.ARMOR_STAND_INFO.getOldType(), (byte) (1 << 4)));
        }
        return entityDataList;
    }

}
