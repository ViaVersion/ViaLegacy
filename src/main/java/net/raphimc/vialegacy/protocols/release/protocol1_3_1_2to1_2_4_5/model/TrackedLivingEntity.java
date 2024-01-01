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
package net.raphimc.vialegacy.protocols.release.protocol1_3_1_2to1_2_4_5.model;

import com.viaversion.viaversion.api.minecraft.entities.EntityTypes1_10;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import net.raphimc.vialegacy.api.model.Location;
import net.raphimc.vialegacy.protocols.release.protocol1_3_1_2to1_2_4_5.sound.SoundEmulation;
import net.raphimc.vialegacy.protocols.release.protocol1_3_1_2to1_2_4_5.sound.SoundType;
import net.raphimc.vialegacy.protocols.release.protocol1_3_1_2to1_2_4_5.storage.EntityTracker;
import net.raphimc.vialegacy.protocols.release.protocol1_6_1to1_5_2.metadata.MetaIndex1_6_1to1_5_2;
import net.raphimc.vialegacy.protocols.release.protocol1_8to1_7_6_10.metadata.MetaIndex1_8to1_7_6;

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

    public TrackedLivingEntity(int entityId, Location location, EntityTypes1_10.EntityType entityType) {
        super(entityId, location, entityType);
    }

    public void tick(EntityTracker tracker) {
        if (tracker.RND.nextInt(1000) < this.soundTime++) {
            this.soundTime = SoundEmulation.getSoundDelayTime(this.getEntityType());

            tracker.playSound(this.getEntityId(), SoundType.IDLE);
        }

        if (this.getEntityType().isOrHasParent(EntityTypes1_10.EntityType.ENTITY_AGEABLE)) {
            if (this.growingAge < 0) {
                this.growingAge++;
            } else if (this.growingAge > 0) {
                this.growingAge--;
            }
        }
    }

    public void updateMetadata(List<Metadata> metadataList) {
        for (Metadata metadata : metadataList) {
            final MetaIndex1_6_1to1_5_2 index = MetaIndex1_6_1to1_5_2.searchIndex(this.getEntityType(), metadata.id());
            final MetaIndex1_8to1_7_6 index2 = MetaIndex1_8to1_7_6.searchIndex(this.getEntityType(), metadata.id());

            if (index == MetaIndex1_6_1to1_5_2.WOLF_HEALTH) {
                this.wolfHealth = metadata.<Integer>value();
            } else if (index != null) {
                continue;
            }
            if (index2 == MetaIndex1_8to1_7_6.ENTITY_AGEABLE_AGE) {
                this.growingAge = metadata.value();
            } else if (index2 == MetaIndex1_8to1_7_6.TAMEABLE_FLAGS) {
                this.isTamed = (metadata.<Byte>value() & 4) != 0;
                this.wolfIsAngry = (metadata.<Byte>value() & 2) != 0;
            }
        }
    }

    public void applyPitch(EntityTracker tracker, ConfiguredSound sound) {
        float pitch;

        if (this.getEntityType().isOrHasParent(EntityTypes1_10.EntityType.ENTITY_AGEABLE) && this.growingAge < 0) {
            pitch = (tracker.RND.nextFloat() - tracker.RND.nextFloat()) * 0.2F + 1.5F;
        } else {
            pitch = (tracker.RND.nextFloat() - tracker.RND.nextFloat()) * 0.2F + 1.0F;
        }

        sound.setPitch(pitch);
    }

}
