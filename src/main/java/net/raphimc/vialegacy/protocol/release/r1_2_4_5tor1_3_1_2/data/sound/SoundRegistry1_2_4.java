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

package net.raphimc.vialegacy.protocol.release.r1_2_4_5tor1_3_1_2.data.sound;

import com.viaversion.viaversion.api.minecraft.entities.EntityTypes1_8;
import net.raphimc.vialegacy.protocol.release.r1_2_4_5tor1_3_1_2.model.ConfiguredSound;

public class SoundRegistry1_2_4 {

    private final static Sound[][] ENTITY_SOUNDS = new Sound[256][];
    private final static float[] VOL_ADJUST = new float[256];
    private final static int[] INTERVAL_ADJUST = new int[256];

    static {
        ENTITY_SOUNDS[48/*HUMAN*/] = new Sound[]{
                Sound.NO_SOUND,
                Sound.MOB_HUMAN_HURT,
                Sound.MOB_HUMAN_HURT
        };
        ENTITY_SOUNDS[EntityTypes1_8.EntityType.CREEPER.getId()] = new Sound[]{
                Sound.NO_SOUND,
                Sound.MOB_CREEPER,
                Sound.MOB_CREEPER_DEATH
        };
        ENTITY_SOUNDS[EntityTypes1_8.EntityType.SKELETON.getId()] = new Sound[]{
                Sound.MOB_SKELETON,
                Sound.MOB_SKELETON_HURT,
                Sound.MOB_SKELETON_DEATH
        };
        ENTITY_SOUNDS[EntityTypes1_8.EntityType.SPIDER.getId()] = new Sound[]{
                Sound.MOB_SPIDER,
                Sound.MOB_SPIDER,
                Sound.MOB_SPIDER_DEATH
        };
        ENTITY_SOUNDS[EntityTypes1_8.EntityType.GIANT.getId()] = new Sound[]{
                Sound.NO_SOUND,
                Sound.MOB_HUMAN_HURT,
                Sound.MOB_HUMAN_HURT
        };
        ENTITY_SOUNDS[EntityTypes1_8.EntityType.ZOMBIE.getId()] = new Sound[]{
                Sound.MOB_ZOMBIE,
                Sound.MOB_ZOMBIE_HURT,
                Sound.MOB_ZOMBIE_DEATH
        };
        ENTITY_SOUNDS[EntityTypes1_8.EntityType.SLIME.getId()] = new Sound[]{
                Sound.MOB_SLIME,
                Sound.MOB_SLIME,
                Sound.MOB_SLIME
        };
        ENTITY_SOUNDS[EntityTypes1_8.EntityType.GHAST.getId()] = new Sound[]{
                Sound.MOB_GHAST,
                Sound.MOB_GHAST_HURT,
                Sound.MOB_GHAST_DEATH
        };
        ENTITY_SOUNDS[EntityTypes1_8.EntityType.ZOMBIE_PIGMEN.getId()] = new Sound[]{
                Sound.MOB_PIG_ZOMBIE,
                Sound.MOB_PIG_ZOMBIE_HURT,
                Sound.MOB_PIG_ZOMBIE_DEATH
        };
        ENTITY_SOUNDS[EntityTypes1_8.EntityType.ENDERMAN.getId()] = new Sound[]{
                Sound.MOB_ENDERMEN,
                Sound.MOB_ENDERMEN_HURT,
                Sound.MOB_ENDERMEN_DEATH
        };
        ENTITY_SOUNDS[EntityTypes1_8.EntityType.CAVE_SPIDER.getId()] = new Sound[]{
                Sound.MOB_SPIDER,
                Sound.MOB_SPIDER,
                Sound.MOB_SPIDER_DEATH
        };
        ENTITY_SOUNDS[EntityTypes1_8.EntityType.SILVERFISH.getId()] = new Sound[]{
                Sound.MOB_SILVERFISH,
                Sound.MOB_SILVERFISH_HURT,
                Sound.MOB_SILVERFISH_DEATH
        };
        ENTITY_SOUNDS[EntityTypes1_8.EntityType.BLAZE.getId()] = new Sound[]{
                Sound.MOB_BLAZE,
                Sound.MOB_BLAZE_HURT,
                Sound.MOB_BLAZE_DEATH
        };
        ENTITY_SOUNDS[EntityTypes1_8.EntityType.MAGMA_CUBE.getId()] = new Sound[]{
                Sound.MOB_MAGMACUBE_SMALL,
                Sound.MOB_SLIME,
                Sound.MOB_SLIME
        };
        ENTITY_SOUNDS[EntityTypes1_8.EntityType.ENDER_DRAGON.getId()] = new Sound[]{
                Sound.NO_SOUND,
                Sound.MOB_HUMAN_HURT,
                Sound.MOB_HUMAN_HURT
        };
        ENTITY_SOUNDS[EntityTypes1_8.EntityType.PIG.getId()] = new Sound[]{
                Sound.MOB_PIG,
                Sound.MOB_PIG,
                Sound.MOB_PIG_DEATH
        };
        ENTITY_SOUNDS[EntityTypes1_8.EntityType.SHEEP.getId()] = new Sound[]{
                Sound.MOB_SHEEP,
                Sound.MOB_SHEEP,
                Sound.MOB_SHEEP
        };
        ENTITY_SOUNDS[EntityTypes1_8.EntityType.COW.getId()] = new Sound[]{
                Sound.MOB_COW,
                Sound.MOB_COW_HURT,
                Sound.MOB_COW_HURT
        };
        ENTITY_SOUNDS[EntityTypes1_8.EntityType.CHICKEN.getId()] = new Sound[]{
                Sound.MOB_CHICKEN,
                Sound.MOB_CHICKEN_HURT,
                Sound.MOB_CHICKEN_HURT
        };
        ENTITY_SOUNDS[EntityTypes1_8.EntityType.SQUID.getId()] = new Sound[]{
                Sound.NO_SOUND,
                Sound.NO_SOUND,
                Sound.NO_SOUND
        };
        ENTITY_SOUNDS[EntityTypes1_8.EntityType.WOLF.getId()] = new Sound[]{
                Sound.MOB_WOLF,
                Sound.MOB_WOLF_HURT,
                Sound.MOB_WOLF_DEATH
        };
        ENTITY_SOUNDS[EntityTypes1_8.EntityType.MOOSHROOM.getId()] = new Sound[]{
                Sound.MOB_COW,
                Sound.MOB_COW_HURT,
                Sound.MOB_COW_HURT
        };
        ENTITY_SOUNDS[EntityTypes1_8.EntityType.SNOW_GOLEM.getId()] = new Sound[]{
                Sound.NO_SOUND,
                Sound.NO_SOUND,
                Sound.NO_SOUND
        };
        ENTITY_SOUNDS[EntityTypes1_8.EntityType.OCELOT.getId()] = new Sound[]{
                Sound.NO_SOUND,
                Sound.MOB_CAT_HURT,
                Sound.MOB_CAT_HURT
        };
        ENTITY_SOUNDS[EntityTypes1_8.EntityType.IRON_GOLEM.getId()] = new Sound[]{
                Sound.NO_SOUND,
                Sound.MOB_IRON_GOLEM_HURT,
                Sound.MOB_IRON_GOLEM_DEATH
        };
        ENTITY_SOUNDS[EntityTypes1_8.EntityType.VILLAGER.getId()] = new Sound[]{
                Sound.MOB_VILLAGER,
                Sound.MOB_VILLAGER_HURT,
                Sound.MOB_VILLAGER_DEATH
        };

        VOL_ADJUST[EntityTypes1_8.EntityType.SLIME.getId()] = 1.6F;
        VOL_ADJUST[EntityTypes1_8.EntityType.MAGMA_CUBE.getId()] = 1.6F;
        VOL_ADJUST[EntityTypes1_8.EntityType.GHAST.getId()] = 10.0F;
        VOL_ADJUST[EntityTypes1_8.EntityType.COW.getId()] = 0.4F;
        VOL_ADJUST[EntityTypes1_8.EntityType.WOLF.getId()] = 0.4F;
        VOL_ADJUST[EntityTypes1_8.EntityType.SQUID.getId()] = 0.4F;
        VOL_ADJUST[EntityTypes1_8.EntityType.MOOSHROOM.getId()] = 0.4F;
        VOL_ADJUST[EntityTypes1_8.EntityType.OCELOT.getId()] = 0.4F;

        INTERVAL_ADJUST[EntityTypes1_8.EntityType.PIG.getId()] = 120;
        INTERVAL_ADJUST[EntityTypes1_8.EntityType.SHEEP.getId()] = 120;
        INTERVAL_ADJUST[EntityTypes1_8.EntityType.WOLF.getId()] = 120;
        INTERVAL_ADJUST[EntityTypes1_8.EntityType.SNOW_GOLEM.getId()] = 120;
        INTERVAL_ADJUST[EntityTypes1_8.EntityType.IRON_GOLEM.getId()] = 120;
        INTERVAL_ADJUST[EntityTypes1_8.EntityType.MOOSHROOM.getId()] = 120;
        INTERVAL_ADJUST[EntityTypes1_8.EntityType.COW.getId()] = 120;
        INTERVAL_ADJUST[EntityTypes1_8.EntityType.CHICKEN.getId()] = 120;
        INTERVAL_ADJUST[EntityTypes1_8.EntityType.SQUID.getId()] = 120;
        INTERVAL_ADJUST[EntityTypes1_8.EntityType.OCELOT.getId()] = 120;
    }

    public static ConfiguredSound getEntitySound(EntityTypes1_8.EntityType entityType, SoundType soundType) {
        ConfiguredSound sound = new ConfiguredSound(Sound.NO_SOUND, 0.0F, 1.0F);
        int entityTypeID = entityType.getId();
        if (entityType.isOrHasParent(EntityTypes1_8.EntityType.PLAYER)) entityTypeID = 48;

        final Sound[] entitySounds = ENTITY_SOUNDS[entityTypeID];
        if (entitySounds == null) {
            return sound;
        }

        sound = switch (soundType) {
            case IDLE -> new ConfiguredSound(entitySounds[0], 1.0F, 1.0F);
            case HURT -> new ConfiguredSound(entitySounds[1], 1.0F, 1.0F);
            case DEATH -> new ConfiguredSound(entitySounds[2], 1.0F, 1.0F);
        };

        final float correctedVolume = VOL_ADJUST[entityTypeID];
        if (correctedVolume != 0F) {
            sound.setVolume(correctedVolume);
        }

        return sound;
    }

    public static int getSoundDelayTime(EntityTypes1_8.EntityType entityType) {
        int entityTypeID = entityType.getId();
        if (entityType.isOrHasParent(EntityTypes1_8.EntityType.PLAYER)) entityTypeID = 48;

        int soundTime = -80;
        final int ajustedSoundTime = SoundRegistry1_2_4.INTERVAL_ADJUST[entityTypeID];
        if (ajustedSoundTime != 0) soundTime = -ajustedSoundTime;

        return soundTime;
    }

}
