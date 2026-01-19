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

package net.raphimc.vialegacy.protocol.release.r1_2_4_5tor1_3_1_2.model;

import com.viaversion.viaversion.api.minecraft.entities.EntityTypes1_8;
import com.viaversion.viaversion.api.minecraft.entitydata.EntityData;
import net.raphimc.vialegacy.api.model.Location;
import net.raphimc.vialegacy.protocol.release.r1_2_4_5tor1_3_1_2.data.sound.SoundRegistry1_2_4;
import net.raphimc.vialegacy.protocol.release.r1_2_4_5tor1_3_1_2.data.sound.SoundType;
import net.raphimc.vialegacy.protocol.release.r1_2_4_5tor1_3_1_2.storage.EntityTracker;
import net.raphimc.vialegacy.protocol.release.r1_5_2tor1_6_1.data.EntityDataIndex1_5_2;
import net.raphimc.vialegacy.protocol.release.r1_7_6_10tor1_8.data.EntityDataIndex1_7_6;

import java.util.List;

public class TrackedLivingEntity extends AbstractTrackedEntity {

    private int soundTime;

    // ENTITY_AGEABLE
    public int growingAge;

    // ENTITY_TAMEABLE_ANIMAL
    public boolean isTamed;

    // WOLF
    public int wolfHealth;
    public boolean wolfIsAngry;

    public TrackedLivingEntity(int entityId, Location location, EntityTypes1_8.EntityType entityType) {
        super(entityId, location, entityType);
    }

    public void tick(EntityTracker tracker) {
        if (tracker.RND.nextInt(1000) < this.soundTime++) {
            this.soundTime = SoundRegistry1_2_4.getSoundDelayTime(this.getEntityType());

            tracker.playSound(this.getEntityId(), SoundType.IDLE);
        }

        if (this.getEntityType().isOrHasParent(EntityTypes1_8.EntityType.ABSTRACT_AGEABLE)) {
            if (this.growingAge < 0) {
                this.growingAge++;
            } else if (this.growingAge > 0) {
                this.growingAge--;
            }
        }
    }

    public void updateEntityData(List<EntityData> entityDataList) {
        for (EntityData entityData : entityDataList) {
            final EntityDataIndex1_5_2 index = EntityDataIndex1_5_2.searchIndex(this.getEntityType(), entityData.id());
            final EntityDataIndex1_7_6 index2 = EntityDataIndex1_7_6.searchIndex(this.getEntityType(), entityData.id());

            if (index == EntityDataIndex1_5_2.WOLF_HEALTH) {
                this.wolfHealth = entityData.<Integer>value();
            } else if (index != null) {
                continue;
            }
            if (index2 == EntityDataIndex1_7_6.ENTITY_AGEABLE_AGE) {
                this.growingAge = entityData.value();
            } else if (index2 == EntityDataIndex1_7_6.TAMEABLE_FLAGS) {
                this.isTamed = (entityData.<Byte>value() & 4) != 0;
                this.wolfIsAngry = (entityData.<Byte>value() & 2) != 0;
            }
        }
    }

    public void applyPitch(EntityTracker tracker, ConfiguredSound sound) {
        float pitch;

        if (this.getEntityType().isOrHasParent(EntityTypes1_8.EntityType.ABSTRACT_AGEABLE) && this.growingAge < 0) {
            pitch = (tracker.RND.nextFloat() - tracker.RND.nextFloat()) * 0.2F + 1.5F;
        } else {
            pitch = (tracker.RND.nextFloat() - tracker.RND.nextFloat()) * 0.2F + 1.0F;
        }

        sound.setPitch(pitch);
    }

}
