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
package net.raphimc.vialegacy.protocol.release.r1_7_6_10tor1_8.data;

import com.viaversion.viaversion.api.minecraft.entities.EntityTypes1_8;
import com.viaversion.viaversion.api.minecraft.entitydata.types.EntityDataTypes1_8;
import com.viaversion.viaversion.util.Pair;
import net.raphimc.vialegacy.protocol.release.r1_7_6_10tor1_8.types.EntityDataTypes1_7_6;

import java.util.HashMap;
import java.util.Optional;

public enum EntityDataIndex1_7_6 {

    ENTITY_FLAGS(EntityTypes1_8.EntityType.ENTITY, 0, EntityDataTypes1_7_6.BYTE, EntityDataTypes1_8.BYTE),
    ENTITY_AIR(EntityTypes1_8.EntityType.ENTITY, 1, EntityDataTypes1_7_6.SHORT, EntityDataTypes1_8.SHORT),
    ENTITY_LIVING_HEALTH(EntityTypes1_8.EntityType.LIVING_ENTITY_BASE, 6, EntityDataTypes1_7_6.FLOAT, EntityDataTypes1_8.FLOAT),
    ENTITY_LIVING_POTION_EFFECT_COLOR(EntityTypes1_8.EntityType.LIVING_ENTITY_BASE, 7, EntityDataTypes1_7_6.INT, EntityDataTypes1_8.INT),
    ENTITY_LIVING_IS_POTION_EFFECT_AMBIENT(EntityTypes1_8.EntityType.LIVING_ENTITY_BASE, 8, EntityDataTypes1_7_6.BYTE, EntityDataTypes1_8.BYTE),
    ENTITY_LIVING_ARROWS(EntityTypes1_8.EntityType.LIVING_ENTITY_BASE, 9, EntityDataTypes1_7_6.BYTE, EntityDataTypes1_8.BYTE),
    ENTITY_LIVING_NAME_TAG(EntityTypes1_8.EntityType.LIVING_ENTITY_BASE, 10, EntityDataTypes1_7_6.STRING, 2, EntityDataTypes1_8.STRING),
    ENTITY_LIVING_NAME_TAG_VISIBILITY(EntityTypes1_8.EntityType.LIVING_ENTITY_BASE, 11, EntityDataTypes1_7_6.BYTE, 3, EntityDataTypes1_8.BYTE),
    ENTITY_AGEABLE_AGE(EntityTypes1_8.EntityType.ABSTRACT_AGEABLE, 12, EntityDataTypes1_7_6.INT, EntityDataTypes1_8.BYTE),
    HUMAN_SKIN_FLAGS(EntityTypes1_8.EntityType.PLAYER, 16, EntityDataTypes1_7_6.BYTE, 10, EntityDataTypes1_8.BYTE),
    HUMAN_ABSORPTION_HEARTS(EntityTypes1_8.EntityType.PLAYER, 17, EntityDataTypes1_7_6.FLOAT, EntityDataTypes1_8.FLOAT),
    HUMAN_SCORE(EntityTypes1_8.EntityType.PLAYER, 18, EntityDataTypes1_7_6.INT, EntityDataTypes1_8.INT),
    HORSE_FLAGS(EntityTypes1_8.EntityType.HORSE, 16, EntityDataTypes1_7_6.INT, EntityDataTypes1_8.INT),
    HORSE_TYPE(EntityTypes1_8.EntityType.HORSE, 19, EntityDataTypes1_7_6.BYTE, EntityDataTypes1_8.BYTE),
    HORSE_COLOR(EntityTypes1_8.EntityType.HORSE, 20, EntityDataTypes1_7_6.INT, EntityDataTypes1_8.INT),
    HORSE_OWNER(EntityTypes1_8.EntityType.HORSE, 21, EntityDataTypes1_7_6.STRING, EntityDataTypes1_8.STRING),
    HORSE_ARMOR(EntityTypes1_8.EntityType.HORSE, 22, EntityDataTypes1_7_6.INT, EntityDataTypes1_8.INT),
    BAT_HANGING(EntityTypes1_8.EntityType.BAT, 16, EntityDataTypes1_7_6.BYTE, EntityDataTypes1_8.BYTE),
    TAMEABLE_FLAGS(EntityTypes1_8.EntityType.TAMABLE_ANIMAL, 16, EntityDataTypes1_7_6.BYTE, EntityDataTypes1_8.BYTE),
    TAMEABLE_OWNER(EntityTypes1_8.EntityType.TAMABLE_ANIMAL, 17, EntityDataTypes1_7_6.STRING, EntityDataTypes1_8.STRING),
    OCELOT_TYPE(EntityTypes1_8.EntityType.OCELOT, 18, EntityDataTypes1_7_6.BYTE, EntityDataTypes1_8.BYTE),
    WOLF_HEALTH(EntityTypes1_8.EntityType.WOLF, 18, EntityDataTypes1_7_6.FLOAT, EntityDataTypes1_8.FLOAT),
    WOLF_BEGGING(EntityTypes1_8.EntityType.WOLF, 19, EntityDataTypes1_7_6.BYTE, EntityDataTypes1_8.BYTE),
    WOLF_COLLAR_COLOR(EntityTypes1_8.EntityType.WOLF, 20, EntityDataTypes1_7_6.BYTE, EntityDataTypes1_8.BYTE),
    PIG_SADDLE(EntityTypes1_8.EntityType.PIG, 16, EntityDataTypes1_7_6.BYTE, EntityDataTypes1_8.BYTE),
    SHEEP_COLOR_OR_SHEARED(EntityTypes1_8.EntityType.SHEEP, 16, EntityDataTypes1_7_6.BYTE, EntityDataTypes1_8.BYTE),
    VILLAGER_TYPE(EntityTypes1_8.EntityType.VILLAGER, 16, EntityDataTypes1_7_6.INT, EntityDataTypes1_8.INT),
    ENDERMAN_CARRIED_BLOCK(EntityTypes1_8.EntityType.ENDERMAN, 16, EntityDataTypes1_7_6.BYTE, EntityDataTypes1_8.SHORT), // merged with ENDERMAN_CARRIED_BLOCK_DATA
    ENDERMAN_CARRIED_BLOCK_DATA(EntityTypes1_8.EntityType.ENDERMAN, 17, EntityDataTypes1_7_6.BYTE, EntityDataTypes1_8.BYTE), // merged to ENDERMAN_CARRIED_BLOCK
    ENDERMAN_IS_SCREAMING(EntityTypes1_8.EntityType.ENDERMAN, 18, EntityDataTypes1_7_6.BYTE, EntityDataTypes1_8.BYTE),
    ZOMBIE_CHILD(EntityTypes1_8.EntityType.ZOMBIE, 12, EntityDataTypes1_7_6.BYTE, EntityDataTypes1_8.BYTE),
    ZOMBIE_VILLAGER(EntityTypes1_8.EntityType.ZOMBIE, 13, EntityDataTypes1_7_6.BYTE, EntityDataTypes1_8.BYTE),
    ZOMBIE_CONVERTING(EntityTypes1_8.EntityType.ZOMBIE, 14, EntityDataTypes1_7_6.BYTE, EntityDataTypes1_8.BYTE),
    BLAZE_ON_FIRE(EntityTypes1_8.EntityType.BLAZE, 16, EntityDataTypes1_7_6.BYTE, EntityDataTypes1_8.BYTE),
    SPIDER_CLIMBING(EntityTypes1_8.EntityType.SPIDER, 16, EntityDataTypes1_7_6.BYTE, EntityDataTypes1_8.BYTE),
    CREEPER_STATE(EntityTypes1_8.EntityType.CREEPER, 16, EntityDataTypes1_7_6.BYTE, EntityDataTypes1_8.BYTE),
    CREEPER_POWERED(EntityTypes1_8.EntityType.CREEPER, 17, EntityDataTypes1_7_6.BYTE, EntityDataTypes1_8.BYTE),
    CREEPER_ISIGNITED(EntityTypes1_8.EntityType.CREEPER, 18, EntityDataTypes1_7_6.BYTE, EntityDataTypes1_8.BYTE),
    GHAST_STATE(EntityTypes1_8.EntityType.GHAST, 16, EntityDataTypes1_7_6.BYTE, EntityDataTypes1_8.BYTE),
    GHAST_IS_POWERED(EntityTypes1_8.EntityType.GHAST, 17, null, EntityDataTypes1_8.BYTE),
    SLIME_SIZE(EntityTypes1_8.EntityType.SLIME, 16, EntityDataTypes1_7_6.BYTE, EntityDataTypes1_8.BYTE),
    SKELETON_TYPE(EntityTypes1_8.EntityType.SKELETON, 13, EntityDataTypes1_7_6.BYTE, EntityDataTypes1_8.BYTE),
    WITCH_AGRESSIVE(EntityTypes1_8.EntityType.WITCH, 21, EntityDataTypes1_7_6.BYTE, EntityDataTypes1_8.BYTE),
    IRON_GOLEM_IS_PLAYER_CREATED(EntityTypes1_8.EntityType.IRON_GOLEM, 16, EntityDataTypes1_7_6.BYTE, EntityDataTypes1_8.BYTE),
    WITHER_WATCHED_TAGRET_1(EntityTypes1_8.EntityType.WITHER, 17, EntityDataTypes1_7_6.INT, EntityDataTypes1_8.INT),
    WITHER_WATCHED_TAGRET_2(EntityTypes1_8.EntityType.WITHER, 18, EntityDataTypes1_7_6.INT, EntityDataTypes1_8.INT),
    WITHER_WATCHED_TAGRET_3(EntityTypes1_8.EntityType.WITHER, 19, EntityDataTypes1_7_6.INT, EntityDataTypes1_8.INT),
    WITHER_INVULNERABLE_TIME(EntityTypes1_8.EntityType.WITHER, 20, EntityDataTypes1_7_6.INT, EntityDataTypes1_8.INT),
    WITHER_SKULL_ISINVULNERABLE(EntityTypes1_8.EntityType.WITHER_SKULL, 10, EntityDataTypes1_7_6.BYTE, EntityDataTypes1_8.BYTE),
    GUARDIAN_FLAGS(EntityTypes1_8.EntityType.GUARDIAN, 16, null, EntityDataTypes1_8.BYTE),
    GUARDIAN_TARGET(EntityTypes1_8.EntityType.GUARDIAN, 17, null, EntityDataTypes1_8.INT),
    BOAT_TIME_SINCE_HIT(EntityTypes1_8.EntityType.BOAT, 17, EntityDataTypes1_7_6.INT, EntityDataTypes1_8.INT),
    BOAT_FORWARD_DIRECTION(EntityTypes1_8.EntityType.BOAT, 18, EntityDataTypes1_7_6.INT, EntityDataTypes1_8.INT),
    BOAT_DAMAGE_TAKEN(EntityTypes1_8.EntityType.BOAT, 19, EntityDataTypes1_7_6.FLOAT, EntityDataTypes1_8.FLOAT),
    MINECART_SHAKING_POWER(EntityTypes1_8.EntityType.ABSTRACT_MINECART, 17, EntityDataTypes1_7_6.INT, EntityDataTypes1_8.INT),
    MINECART_SHAKING_DIRECTION(EntityTypes1_8.EntityType.ABSTRACT_MINECART, 18, EntityDataTypes1_7_6.INT, EntityDataTypes1_8.INT),
    MINECART_DAMAGE_TAKEN(EntityTypes1_8.EntityType.ABSTRACT_MINECART, 19, EntityDataTypes1_7_6.FLOAT, EntityDataTypes1_8.FLOAT),
    MINECART_BLOCK_INSIDE(EntityTypes1_8.EntityType.ABSTRACT_MINECART, 20, EntityDataTypes1_7_6.INT, EntityDataTypes1_8.INT),
    MINECART_BLOCK_Y(EntityTypes1_8.EntityType.ABSTRACT_MINECART, 21, EntityDataTypes1_7_6.INT, EntityDataTypes1_8.INT),
    MINECART_SHOW_BLOCK(EntityTypes1_8.EntityType.ABSTRACT_MINECART, 22, EntityDataTypes1_7_6.BYTE, EntityDataTypes1_8.BYTE),
    FURNACE_MINECART_IS_POWERED(EntityTypes1_8.EntityType.FURNACE_MINECART, 16, EntityDataTypes1_7_6.BYTE, EntityDataTypes1_8.BYTE),
    ITEM_ITEM(EntityTypes1_8.EntityType.ITEM, 10, EntityDataTypes1_7_6.ITEM, EntityDataTypes1_8.ITEM),
    ARROW_IS_CRITICAL(EntityTypes1_8.EntityType.ARROW, 16, EntityDataTypes1_7_6.BYTE, EntityDataTypes1_8.BYTE),
    FIREWORK_INFO(EntityTypes1_8.EntityType.FIREWORK_ROCKET, 8, EntityDataTypes1_7_6.ITEM, EntityDataTypes1_8.ITEM),
    ITEM_FRAME_ITEM(EntityTypes1_8.EntityType.ITEM_FRAME, 2, EntityDataTypes1_7_6.ITEM, 8, EntityDataTypes1_8.ITEM),
    ITEM_FRAME_ROTATION(EntityTypes1_8.EntityType.ITEM_FRAME, 3, EntityDataTypes1_7_6.BYTE, 9, EntityDataTypes1_8.BYTE),
    ENDER_CRYSTAL_HEALTH(EntityTypes1_8.EntityType.END_CRYSTAL, 8, EntityDataTypes1_7_6.INT, 8, EntityDataTypes1_8.INT),
    ;

    private static final HashMap<Pair<EntityTypes1_8.EntityType, Integer>, EntityDataIndex1_7_6> ENTITY_DATA_REWRITES = new HashMap<>();

    static {
        for (EntityDataIndex1_7_6 index : EntityDataIndex1_7_6.values()) {
            ENTITY_DATA_REWRITES.put(new Pair<>(index.getEntityType(), index.getOldIndex()), index);
        }
    }

    private final EntityTypes1_8.EntityType entityType;
    private final int oldIndex;
    private final int newIndex;
    private final EntityDataTypes1_7_6 oldType;
    private final EntityDataTypes1_8 newType;

    EntityDataIndex1_7_6(EntityTypes1_8.EntityType entityType, int oldIndex, EntityDataTypes1_7_6 oldType, EntityDataTypes1_8 newType) {
        this.entityType = entityType;
        this.oldIndex = oldIndex;
        this.newIndex = oldIndex;
        this.oldType = oldType;
        this.newType = newType;
    }

    EntityDataIndex1_7_6(EntityTypes1_8.EntityType entityType, int oldIndex, EntityDataTypes1_7_6 oldType, int newIndex, EntityDataTypes1_8 newType) {
        this.entityType = entityType;
        this.oldIndex = oldIndex;
        this.oldType = oldType;
        this.newIndex = newIndex;
        this.newType = newType;
    }

    public EntityTypes1_8.EntityType getEntityType() {
        return this.entityType;
    }

    public int getOldIndex() {
        return this.oldIndex;
    }

    public int getNewIndex() {
        return this.newIndex;
    }

    public EntityDataTypes1_7_6 getOldType() {
        return this.oldType;
    }

    public EntityDataTypes1_8 getNewType() {
        return this.newType;
    }

    private static Optional<EntityDataIndex1_7_6> getIndex(EntityTypes1_8.EntityType type, int index) {
        final Pair<EntityTypes1_8.EntityType, Integer> pair = new Pair<>(type, index);
        return Optional.ofNullable(ENTITY_DATA_REWRITES.get(pair));
    }

    public static EntityDataIndex1_7_6 searchIndex(EntityTypes1_8.EntityType type, int index) {
        EntityTypes1_8.EntityType currentType = type;
        do {
            Optional<EntityDataIndex1_7_6> optMeta = getIndex(currentType, index);
            if (optMeta.isPresent()) {
                return optMeta.get();
            }

            currentType = currentType.getParent();
        } while (currentType != null);

        return null;
    }

}
