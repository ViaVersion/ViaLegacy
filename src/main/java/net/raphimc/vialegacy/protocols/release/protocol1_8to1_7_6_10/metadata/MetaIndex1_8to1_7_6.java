/*
 * This file is part of ViaLegacy - https://github.com/RaphiMC/ViaLegacy
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
package net.raphimc.vialegacy.protocols.release.protocol1_8to1_7_6_10.metadata;

import com.viaversion.viaversion.api.minecraft.entities.EntityTypes1_10;
import com.viaversion.viaversion.api.minecraft.metadata.types.MetaType1_8;
import com.viaversion.viaversion.util.Pair;
import net.raphimc.vialegacy.protocols.release.protocol1_8to1_7_6_10.types.MetaType1_7_6;

import java.util.HashMap;
import java.util.Optional;

public enum MetaIndex1_8to1_7_6 {

    ENTITY_FLAGS(EntityTypes1_10.EntityType.ENTITY, 0, MetaType1_7_6.Byte, MetaType1_8.Byte),
    ENTITY_AIR(EntityTypes1_10.EntityType.ENTITY, 1, MetaType1_7_6.Short, MetaType1_8.Short),
    ENTITY_NAME_TAG(EntityTypes1_10.EntityType.ENTITY, -1, null, 2, MetaType1_8.String),
    ENTITY_NAME_TAG_VISIBILITY(EntityTypes1_10.EntityType.ENTITY, -1, null, 3, MetaType1_8.Byte),
    ENTITY_SILENT(EntityTypes1_10.EntityType.ENTITY, -1, null, 4, MetaType1_8.Byte),
    ENTITY_LIVING_HEALTH(EntityTypes1_10.EntityType.ENTITY_LIVING, 6, MetaType1_7_6.Float, MetaType1_8.Float),
    ENTITY_LIVING_POTION_EFFECT_COLOR(EntityTypes1_10.EntityType.ENTITY_LIVING, 7, MetaType1_7_6.Int, MetaType1_8.Int),
    ENTITY_LIVING_IS_POTION_EFFECT_AMBIENT(EntityTypes1_10.EntityType.ENTITY_LIVING, 8, MetaType1_7_6.Byte, MetaType1_8.Byte),
    ENTITY_LIVING_ARROWS(EntityTypes1_10.EntityType.ENTITY_LIVING, 9, MetaType1_7_6.Byte, MetaType1_8.Byte),
    ENTITY_LIVING_NAME_TAG(EntityTypes1_10.EntityType.ENTITY_LIVING, 10, MetaType1_7_6.String, 2, MetaType1_8.String),
    ENTITY_LIVING_NAME_TAG_VISIBILITY(EntityTypes1_10.EntityType.ENTITY_LIVING, 11, MetaType1_7_6.Byte, 3, MetaType1_8.Byte),
    ENTITY_LIVING_AI(EntityTypes1_10.EntityType.ENTITY_LIVING, -1, null, 15, MetaType1_8.Byte),
    ENTITY_AGEABLE_AGE(EntityTypes1_10.EntityType.ENTITY_AGEABLE, 12, MetaType1_7_6.Int, MetaType1_8.Byte),
    ARMOR_STAND_FLAGS(EntityTypes1_10.EntityType.ARMOR_STAND, -1, null, 10, MetaType1_8.Byte),
    ARMOR_STAND_HEAD_POSITION(EntityTypes1_10.EntityType.ARMOR_STAND, -1, null, 11, MetaType1_8.Rotation),
    ARMOR_STAND_BODY_POSITION(EntityTypes1_10.EntityType.ARMOR_STAND, -1, null, 12, MetaType1_8.Rotation),
    ARMOR_STAND_LEFT_ARM_POSITION(EntityTypes1_10.EntityType.ARMOR_STAND, -1, null, 13, MetaType1_8.Rotation),
    ARMOR_STAND_RIGHT_ARM_POSITION(EntityTypes1_10.EntityType.ARMOR_STAND, -1, null, 14, MetaType1_8.Rotation),
    ARMOR_STAND_LEFT_LEG_POSITION(EntityTypes1_10.EntityType.ARMOR_STAND, -1, null, 15, MetaType1_8.Rotation),
    ARMOR_STAND_RIGHT_LEG_POSITION(EntityTypes1_10.EntityType.ARMOR_STAND, -1, null, 16, MetaType1_8.Rotation),
    HUMAN_SKIN_FLAGS(EntityTypes1_10.EntityType.ENTITY_HUMAN, 16, MetaType1_7_6.Byte, 10, MetaType1_8.Byte),
    HUMAN_UNUSED(EntityTypes1_10.EntityType.ENTITY_HUMAN, -1, null, 16, MetaType1_8.Byte),
    HUMAN_ABSORPTION_HEATS(EntityTypes1_10.EntityType.ENTITY_HUMAN, 17, MetaType1_7_6.Float, MetaType1_8.Float),
    HUMAN_SCORE(EntityTypes1_10.EntityType.ENTITY_HUMAN, 18, MetaType1_7_6.Int, MetaType1_8.Int),
    HORSE_FLAGS(EntityTypes1_10.EntityType.HORSE, 16, MetaType1_7_6.Int, MetaType1_8.Int),
    HORSE_TYPE(EntityTypes1_10.EntityType.HORSE, 19, MetaType1_7_6.Byte, MetaType1_8.Byte),
    HORSE_COLOR(EntityTypes1_10.EntityType.HORSE, 20, MetaType1_7_6.Int, MetaType1_8.Int),
    HORSE_OWNER(EntityTypes1_10.EntityType.HORSE, 21, MetaType1_7_6.String, MetaType1_8.String),
    HORSE_ARMOR(EntityTypes1_10.EntityType.HORSE, 22, MetaType1_7_6.Int, MetaType1_8.Int),
    BAT_HANGING(EntityTypes1_10.EntityType.BAT, 16, MetaType1_7_6.Byte, MetaType1_8.Byte),
    TAMEABLE_FLAGS(EntityTypes1_10.EntityType.ENTITY_TAMEABLE_ANIMAL, 16, MetaType1_7_6.Byte, MetaType1_8.Byte),
    TAMEABLE_OWNER(EntityTypes1_10.EntityType.ENTITY_TAMEABLE_ANIMAL, 17, MetaType1_7_6.String, MetaType1_8.String),
    OCELOT_TYPE(EntityTypes1_10.EntityType.OCELOT, 18, MetaType1_7_6.Byte, MetaType1_8.Byte),
    WOLF_HEALTH(EntityTypes1_10.EntityType.WOLF, 18, MetaType1_7_6.Float, MetaType1_8.Float),
    WOLF_BEGGING(EntityTypes1_10.EntityType.WOLF, 19, MetaType1_7_6.Byte, MetaType1_8.Byte),
    WOLF_COLLAR_COLOR(EntityTypes1_10.EntityType.WOLF, 20, MetaType1_7_6.Byte, MetaType1_8.Byte),
    PIG_SADDLE(EntityTypes1_10.EntityType.PIG, 16, MetaType1_7_6.Byte, MetaType1_8.Byte),
    SHEEP_COLOR_OR_SHEARED(EntityTypes1_10.EntityType.SHEEP, 16, MetaType1_7_6.Byte, MetaType1_8.Byte),
    VILLAGER_TYPE(EntityTypes1_10.EntityType.VILLAGER, 16, MetaType1_7_6.Int, MetaType1_8.Int),
    ENDERMAN_CARRIED_BLOCK(EntityTypes1_10.EntityType.ENDERMAN, 16, MetaType1_7_6.Byte, MetaType1_8.Short), // merged with ENDERMAN_CARRIED_BLOCK_DATA
    ENDERMAN_CARRIED_BLOCK_DATA(EntityTypes1_10.EntityType.ENDERMAN, 17, MetaType1_7_6.Byte, MetaType1_8.Byte), // merged to ENDERMAN_CARRIED_BLOCK
    ENDERMAN_IS_SCREAMING(EntityTypes1_10.EntityType.ENDERMAN, 18, MetaType1_7_6.Byte, MetaType1_8.Byte),
    ZOMBIE_CHILD(EntityTypes1_10.EntityType.ZOMBIE, 12, MetaType1_7_6.Byte, MetaType1_8.Byte),
    ZOMBIE_VILLAGER(EntityTypes1_10.EntityType.ZOMBIE, 13, MetaType1_7_6.Byte, MetaType1_8.Byte),
    ZOMBIE_CONVERTING(EntityTypes1_10.EntityType.ZOMBIE, 14, MetaType1_7_6.Byte, MetaType1_8.Byte),
    BLAZE_ON_FIRE(EntityTypes1_10.EntityType.BLAZE, 16, MetaType1_7_6.Byte, MetaType1_8.Byte),
    SPIDER_CLIMBING(EntityTypes1_10.EntityType.SPIDER, 16, MetaType1_7_6.Byte, MetaType1_8.Byte),
    CREEPER_STATE(EntityTypes1_10.EntityType.CREEPER, 16, MetaType1_7_6.Byte, MetaType1_8.Byte),
    CREEPER_POWERED(EntityTypes1_10.EntityType.CREEPER, 17, MetaType1_7_6.Byte, MetaType1_8.Byte),
    CREEPER_ISIGNITED(EntityTypes1_10.EntityType.CREEPER, 18, MetaType1_7_6.Byte, MetaType1_8.Byte),
    GHAST_STATE(EntityTypes1_10.EntityType.GHAST, 16, MetaType1_7_6.Byte, MetaType1_8.Byte),
    GHAST_IS_POWERED(EntityTypes1_10.EntityType.GHAST, 17, null, MetaType1_8.Byte),
    SLIME_SIZE(EntityTypes1_10.EntityType.SLIME, 16, MetaType1_7_6.Byte, MetaType1_8.Byte),
    SKELETON_TYPE(EntityTypes1_10.EntityType.SKELETON, 13, MetaType1_7_6.Byte, MetaType1_8.Byte),
    WITCH_AGRESSIVE(EntityTypes1_10.EntityType.WITCH, 21, MetaType1_7_6.Byte, MetaType1_8.Byte),
    IRON_GOLEM_IS_PLAYER_CREATED(EntityTypes1_10.EntityType.IRON_GOLEM, 16, MetaType1_7_6.Byte, MetaType1_8.Byte),
    WITHER_WATCHED_TAGRET_1(EntityTypes1_10.EntityType.WITHER, 17, MetaType1_7_6.Int, MetaType1_8.Int),
    WITHER_WATCHED_TAGRET_2(EntityTypes1_10.EntityType.WITHER, 18, MetaType1_7_6.Int, MetaType1_8.Int),
    WITHER_WATCHED_TAGRET_3(EntityTypes1_10.EntityType.WITHER, 19, MetaType1_7_6.Int, MetaType1_8.Int),
    WITHER_INVULNERABLE_TIME(EntityTypes1_10.EntityType.WITHER, 20, MetaType1_7_6.Int, MetaType1_8.Int),
    WITHER_SKULL_ISINVULNERABLE(EntityTypes1_10.EntityType.WITHER_SKULL, 10, MetaType1_7_6.Byte, MetaType1_8.Byte),
    GUARDIAN_FLAGS(EntityTypes1_10.EntityType.GUARDIAN, 16, null, MetaType1_8.Byte),
    GUARDIAN_TARGET(EntityTypes1_10.EntityType.GUARDIAN, 17, null, MetaType1_8.Int),
    BOAT_TIME_SINCE_HIT(EntityTypes1_10.EntityType.BOAT, 17, MetaType1_7_6.Int, MetaType1_8.Int),
    BOAT_FORWARD_DIRECTION(EntityTypes1_10.EntityType.BOAT, 18, MetaType1_7_6.Int, MetaType1_8.Int),
    BOAT_DAMAGE_TAKEN(EntityTypes1_10.EntityType.BOAT, 19, MetaType1_7_6.Float, MetaType1_8.Float),
    MINECART_SHAKING_POWER(EntityTypes1_10.EntityType.MINECART_ABSTRACT, 17, MetaType1_7_6.Int, MetaType1_8.Int),
    MINECART_SHAKING_DIRECTION(EntityTypes1_10.EntityType.MINECART_ABSTRACT, 18, MetaType1_7_6.Int, MetaType1_8.Int),
    MINECART_DAMAGE_TAKEN(EntityTypes1_10.EntityType.MINECART_ABSTRACT, 19, MetaType1_7_6.Float, MetaType1_8.Float),
    MINECART_BLOCK_INSIDE(EntityTypes1_10.EntityType.MINECART_ABSTRACT, 20, MetaType1_7_6.Int, MetaType1_8.Int),
    MINECART_BLOCK_Y(EntityTypes1_10.EntityType.MINECART_ABSTRACT, 21, MetaType1_7_6.Int, MetaType1_8.Int),
    MINECART_SHOW_BLOCK(EntityTypes1_10.EntityType.MINECART_ABSTRACT, 22, MetaType1_7_6.Byte, MetaType1_8.Byte),
    FURNACE_MINECART_IS_POWERED(EntityTypes1_10.EntityType.MINECART_ABSTRACT, 16, MetaType1_7_6.Byte, MetaType1_8.Byte),
    ITEM_ITEM(EntityTypes1_10.EntityType.DROPPED_ITEM, 10, MetaType1_7_6.Slot, MetaType1_8.Slot),
    ARROW_IS_CRITICAL(EntityTypes1_10.EntityType.ARROW, 16, MetaType1_7_6.Byte, MetaType1_8.Byte),
    FIREWORK_INFO(EntityTypes1_10.EntityType.FIREWORK, 8, MetaType1_7_6.Slot, MetaType1_8.Slot),
    ITEM_FRAME_ITEM(EntityTypes1_10.EntityType.ITEM_FRAME, 2, MetaType1_7_6.Slot, 8, MetaType1_8.Slot),
    ITEM_FRAME_ROTATION(EntityTypes1_10.EntityType.ITEM_FRAME, 3, MetaType1_7_6.Byte, 9, MetaType1_8.Byte),
    ENDER_CRYSTAL_HEALTH(EntityTypes1_10.EntityType.ENDER_CRYSTAL, 8, MetaType1_7_6.Int, 8, MetaType1_8.Int),
    RABBIT_TYPE(EntityTypes1_10.EntityType.RABBIT, -1, null, 18, MetaType1_8.Byte),
    ;

    private static final HashMap<Pair<EntityTypes1_10.EntityType, Integer>, MetaIndex1_8to1_7_6> metadataRewrites = new HashMap<>();

    static {
        for (MetaIndex1_8to1_7_6 index : MetaIndex1_8to1_7_6.values()) {
            metadataRewrites.put(new Pair<>(index.getEntityType(), index.getOldIndex()), index);
        }
    }

    private final EntityTypes1_10.EntityType entityType;
    private final int oldIndex;
    private final int newIndex;
    private final MetaType1_7_6 oldType;
    private final MetaType1_8 newType;

    MetaIndex1_8to1_7_6(EntityTypes1_10.EntityType entityType, int oldIndex, MetaType1_7_6 oldType, MetaType1_8 newType) {
        this.entityType = entityType;
        this.oldIndex = oldIndex;
        this.newIndex = oldIndex;
        this.oldType = oldType;
        this.newType = newType;
    }

    MetaIndex1_8to1_7_6(EntityTypes1_10.EntityType entityType, int oldIndex, MetaType1_7_6 oldType, int newIndex, MetaType1_8 newType) {
        this.entityType = entityType;
        this.oldIndex = oldIndex;
        this.oldType = oldType;
        this.newIndex = newIndex;
        this.newType = newType;
    }

    public EntityTypes1_10.EntityType getEntityType() {
        return this.entityType;
    }

    public int getOldIndex() {
        return this.oldIndex;
    }

    public int getNewIndex() {
        return this.newIndex;
    }

    public MetaType1_7_6 getOldType() {
        return this.oldType;
    }

    public MetaType1_8 getNewType() {
        return this.newType;
    }

    private static Optional<MetaIndex1_8to1_7_6> getIndex(EntityTypes1_10.EntityType type, int index) {
        final Pair<EntityTypes1_10.EntityType, Integer> pair = new Pair<>(type, index);
        return Optional.ofNullable(metadataRewrites.get(pair));
    }

    public static MetaIndex1_8to1_7_6 searchIndex(EntityTypes1_10.EntityType type, int index) {
        EntityTypes1_10.EntityType currentType = type;
        do {
            Optional<MetaIndex1_8to1_7_6> optMeta = getIndex(currentType, index);
            if (optMeta.isPresent()) {
                return optMeta.get();
            }

            currentType = currentType.getParent();
        } while (currentType != null);

        return null;
    }

}
