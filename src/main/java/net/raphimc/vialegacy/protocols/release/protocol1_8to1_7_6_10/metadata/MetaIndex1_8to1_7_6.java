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
package net.raphimc.vialegacy.protocols.release.protocol1_8to1_7_6_10.metadata;

import com.viaversion.viaversion.api.minecraft.entities.EntityTypes1_10;
import com.viaversion.viaversion.api.minecraft.entitydata.types.EntityDataTypes1_8;
import com.viaversion.viaversion.util.Pair;
import net.raphimc.vialegacy.protocols.release.protocol1_8to1_7_6_10.types.MetaType1_7_6;

import java.util.HashMap;
import java.util.Optional;

public enum MetaIndex1_8to1_7_6 {

    ENTITY_FLAGS(EntityTypes1_10.EntityType.ENTITY, 0, MetaType1_7_6.Byte, EntityDataTypes1_8.BYTE),
    ENTITY_AIR(EntityTypes1_10.EntityType.ENTITY, 1, MetaType1_7_6.Short, EntityDataTypes1_8.SHORT),
    ENTITY_NAME_TAG(EntityTypes1_10.EntityType.ENTITY, -1, null, 2, EntityDataTypes1_8.STRING),
    ENTITY_NAME_TAG_VISIBILITY(EntityTypes1_10.EntityType.ENTITY, -1, null, 3, EntityDataTypes1_8.BYTE),
    ENTITY_SILENT(EntityTypes1_10.EntityType.ENTITY, -1, null, 4, EntityDataTypes1_8.BYTE),
    ENTITY_LIVING_HEALTH(EntityTypes1_10.EntityType.LIVING_ENTITY_BASE, 6, MetaType1_7_6.Float, EntityDataTypes1_8.FLOAT),
    ENTITY_LIVING_POTION_EFFECT_COLOR(EntityTypes1_10.EntityType.LIVING_ENTITY_BASE, 7, MetaType1_7_6.Int, EntityDataTypes1_8.INT),
    ENTITY_LIVING_IS_POTION_EFFECT_AMBIENT(EntityTypes1_10.EntityType.LIVING_ENTITY_BASE, 8, MetaType1_7_6.Byte, EntityDataTypes1_8.BYTE),
    ENTITY_LIVING_ARROWS(EntityTypes1_10.EntityType.LIVING_ENTITY_BASE, 9, MetaType1_7_6.Byte, EntityDataTypes1_8.BYTE),
    ENTITY_LIVING_NAME_TAG(EntityTypes1_10.EntityType.LIVING_ENTITY_BASE, 10, MetaType1_7_6.String, 2, EntityDataTypes1_8.STRING),
    ENTITY_LIVING_NAME_TAG_VISIBILITY(EntityTypes1_10.EntityType.LIVING_ENTITY_BASE, 11, MetaType1_7_6.Byte, 3, EntityDataTypes1_8.BYTE),
    ENTITY_LIVING_AI(EntityTypes1_10.EntityType.LIVING_ENTITY, -1, null, 15, EntityDataTypes1_8.BYTE),
    ENTITY_AGEABLE_AGE(EntityTypes1_10.EntityType.ABSTRACT_AGEABLE, 12, MetaType1_7_6.Int, EntityDataTypes1_8.BYTE),
    ARMOR_STAND_FLAGS(EntityTypes1_10.EntityType.ARMOR_STAND, -1, null, 10, EntityDataTypes1_8.BYTE),
    ARMOR_STAND_HEAD_POSITION(EntityTypes1_10.EntityType.ARMOR_STAND, -1, null, 11, EntityDataTypes1_8.ROTATIONS),
    ARMOR_STAND_BODY_POSITION(EntityTypes1_10.EntityType.ARMOR_STAND, -1, null, 12, EntityDataTypes1_8.ROTATIONS),
    ARMOR_STAND_LEFT_ARM_POSITION(EntityTypes1_10.EntityType.ARMOR_STAND, -1, null, 13, EntityDataTypes1_8.ROTATIONS),
    ARMOR_STAND_RIGHT_ARM_POSITION(EntityTypes1_10.EntityType.ARMOR_STAND, -1, null, 14, EntityDataTypes1_8.ROTATIONS),
    ARMOR_STAND_LEFT_LEG_POSITION(EntityTypes1_10.EntityType.ARMOR_STAND, -1, null, 15, EntityDataTypes1_8.ROTATIONS),
    ARMOR_STAND_RIGHT_LEG_POSITION(EntityTypes1_10.EntityType.ARMOR_STAND, -1, null, 16, EntityDataTypes1_8.ROTATIONS),
    HUMAN_SKIN_FLAGS(EntityTypes1_10.EntityType.PLAYER, 16, MetaType1_7_6.Byte, 10, EntityDataTypes1_8.BYTE),
    HUMAN_UNUSED(EntityTypes1_10.EntityType.PLAYER, -1, null, 16, EntityDataTypes1_8.BYTE),
    HUMAN_ABSORPTION_HEATS(EntityTypes1_10.EntityType.PLAYER, 17, MetaType1_7_6.Float, EntityDataTypes1_8.FLOAT),
    HUMAN_SCORE(EntityTypes1_10.EntityType.PLAYER, 18, MetaType1_7_6.Int, EntityDataTypes1_8.INT),
    HORSE_FLAGS(EntityTypes1_10.EntityType.HORSE, 16, MetaType1_7_6.Int, EntityDataTypes1_8.INT),
    HORSE_TYPE(EntityTypes1_10.EntityType.HORSE, 19, MetaType1_7_6.Byte, EntityDataTypes1_8.BYTE),
    HORSE_COLOR(EntityTypes1_10.EntityType.HORSE, 20, MetaType1_7_6.Int, EntityDataTypes1_8.INT),
    HORSE_OWNER(EntityTypes1_10.EntityType.HORSE, 21, MetaType1_7_6.String, EntityDataTypes1_8.STRING),
    HORSE_ARMOR(EntityTypes1_10.EntityType.HORSE, 22, MetaType1_7_6.Int, EntityDataTypes1_8.INT),
    BAT_HANGING(EntityTypes1_10.EntityType.BAT, 16, MetaType1_7_6.Byte, EntityDataTypes1_8.BYTE),
    TAMEABLE_FLAGS(EntityTypes1_10.EntityType.TAMABLE_ANIMAL, 16, MetaType1_7_6.Byte, EntityDataTypes1_8.BYTE),
    TAMEABLE_OWNER(EntityTypes1_10.EntityType.TAMABLE_ANIMAL, 17, MetaType1_7_6.String, EntityDataTypes1_8.STRING),
    OCELOT_TYPE(EntityTypes1_10.EntityType.OCELOT, 18, MetaType1_7_6.Byte, EntityDataTypes1_8.BYTE),
    WOLF_HEALTH(EntityTypes1_10.EntityType.WOLF, 18, MetaType1_7_6.Float, EntityDataTypes1_8.FLOAT),
    WOLF_BEGGING(EntityTypes1_10.EntityType.WOLF, 19, MetaType1_7_6.Byte, EntityDataTypes1_8.BYTE),
    WOLF_COLLAR_COLOR(EntityTypes1_10.EntityType.WOLF, 20, MetaType1_7_6.Byte, EntityDataTypes1_8.BYTE),
    PIG_SADDLE(EntityTypes1_10.EntityType.PIG, 16, MetaType1_7_6.Byte, EntityDataTypes1_8.BYTE),
    SHEEP_COLOR_OR_SHEARED(EntityTypes1_10.EntityType.SHEEP, 16, MetaType1_7_6.Byte, EntityDataTypes1_8.BYTE),
    VILLAGER_TYPE(EntityTypes1_10.EntityType.VILLAGER, 16, MetaType1_7_6.Int, EntityDataTypes1_8.INT),
    ENDERMAN_CARRIED_BLOCK(EntityTypes1_10.EntityType.ENDERMAN, 16, MetaType1_7_6.Byte, EntityDataTypes1_8.SHORT), // merged with ENDERMAN_CARRIED_BLOCK_DATA
    ENDERMAN_CARRIED_BLOCK_DATA(EntityTypes1_10.EntityType.ENDERMAN, 17, MetaType1_7_6.Byte, EntityDataTypes1_8.BYTE), // merged to ENDERMAN_CARRIED_BLOCK
    ENDERMAN_IS_SCREAMING(EntityTypes1_10.EntityType.ENDERMAN, 18, MetaType1_7_6.Byte, EntityDataTypes1_8.BYTE),
    ZOMBIE_CHILD(EntityTypes1_10.EntityType.ZOMBIE, 12, MetaType1_7_6.Byte, EntityDataTypes1_8.BYTE),
    ZOMBIE_VILLAGER(EntityTypes1_10.EntityType.ZOMBIE, 13, MetaType1_7_6.Byte, EntityDataTypes1_8.BYTE),
    ZOMBIE_CONVERTING(EntityTypes1_10.EntityType.ZOMBIE, 14, MetaType1_7_6.Byte, EntityDataTypes1_8.BYTE),
    BLAZE_ON_FIRE(EntityTypes1_10.EntityType.BLAZE, 16, MetaType1_7_6.Byte, EntityDataTypes1_8.BYTE),
    SPIDER_CLIMBING(EntityTypes1_10.EntityType.SPIDER, 16, MetaType1_7_6.Byte, EntityDataTypes1_8.BYTE),
    CREEPER_STATE(EntityTypes1_10.EntityType.CREEPER, 16, MetaType1_7_6.Byte, EntityDataTypes1_8.BYTE),
    CREEPER_POWERED(EntityTypes1_10.EntityType.CREEPER, 17, MetaType1_7_6.Byte, EntityDataTypes1_8.BYTE),
    CREEPER_ISIGNITED(EntityTypes1_10.EntityType.CREEPER, 18, MetaType1_7_6.Byte, EntityDataTypes1_8.BYTE),
    GHAST_STATE(EntityTypes1_10.EntityType.GHAST, 16, MetaType1_7_6.Byte, EntityDataTypes1_8.BYTE),
    GHAST_IS_POWERED(EntityTypes1_10.EntityType.GHAST, 17, null, EntityDataTypes1_8.BYTE),
    SLIME_SIZE(EntityTypes1_10.EntityType.SLIME, 16, MetaType1_7_6.Byte, EntityDataTypes1_8.BYTE),
    SKELETON_TYPE(EntityTypes1_10.EntityType.SKELETON, 13, MetaType1_7_6.Byte, EntityDataTypes1_8.BYTE),
    WITCH_AGRESSIVE(EntityTypes1_10.EntityType.WITCH, 21, MetaType1_7_6.Byte, EntityDataTypes1_8.BYTE),
    IRON_GOLEM_IS_PLAYER_CREATED(EntityTypes1_10.EntityType.IRON_GOLEM, 16, MetaType1_7_6.Byte, EntityDataTypes1_8.BYTE),
    WITHER_WATCHED_TAGRET_1(EntityTypes1_10.EntityType.WITHER, 17, MetaType1_7_6.Int, EntityDataTypes1_8.INT),
    WITHER_WATCHED_TAGRET_2(EntityTypes1_10.EntityType.WITHER, 18, MetaType1_7_6.Int, EntityDataTypes1_8.INT),
    WITHER_WATCHED_TAGRET_3(EntityTypes1_10.EntityType.WITHER, 19, MetaType1_7_6.Int, EntityDataTypes1_8.INT),
    WITHER_INVULNERABLE_TIME(EntityTypes1_10.EntityType.WITHER, 20, MetaType1_7_6.Int, EntityDataTypes1_8.INT),
    WITHER_SKULL_ISINVULNERABLE(EntityTypes1_10.EntityType.WITHER_SKULL, 10, MetaType1_7_6.Byte, EntityDataTypes1_8.BYTE),
    GUARDIAN_FLAGS(EntityTypes1_10.EntityType.GUARDIAN, 16, null, EntityDataTypes1_8.BYTE),
    GUARDIAN_TARGET(EntityTypes1_10.EntityType.GUARDIAN, 17, null, EntityDataTypes1_8.INT),
    BOAT_TIME_SINCE_HIT(EntityTypes1_10.EntityType.BOAT, 17, MetaType1_7_6.Int, EntityDataTypes1_8.INT),
    BOAT_FORWARD_DIRECTION(EntityTypes1_10.EntityType.BOAT, 18, MetaType1_7_6.Int, EntityDataTypes1_8.INT),
    BOAT_DAMAGE_TAKEN(EntityTypes1_10.EntityType.BOAT, 19, MetaType1_7_6.Float, EntityDataTypes1_8.FLOAT),
    MINECART_SHAKING_POWER(EntityTypes1_10.EntityType.ABSTRACT_MINECART, 17, MetaType1_7_6.Int, EntityDataTypes1_8.INT),
    MINECART_SHAKING_DIRECTION(EntityTypes1_10.EntityType.ABSTRACT_MINECART, 18, MetaType1_7_6.Int, EntityDataTypes1_8.INT),
    MINECART_DAMAGE_TAKEN(EntityTypes1_10.EntityType.ABSTRACT_MINECART, 19, MetaType1_7_6.Float, EntityDataTypes1_8.FLOAT),
    MINECART_BLOCK_INSIDE(EntityTypes1_10.EntityType.ABSTRACT_MINECART, 20, MetaType1_7_6.Int, EntityDataTypes1_8.INT),
    MINECART_BLOCK_Y(EntityTypes1_10.EntityType.ABSTRACT_MINECART, 21, MetaType1_7_6.Int, EntityDataTypes1_8.INT),
    MINECART_SHOW_BLOCK(EntityTypes1_10.EntityType.ABSTRACT_MINECART, 22, MetaType1_7_6.Byte, EntityDataTypes1_8.BYTE),
    FURNACE_MINECART_IS_POWERED(EntityTypes1_10.EntityType.FURNACE_MINECART, 16, MetaType1_7_6.Byte, EntityDataTypes1_8.BYTE),
    ITEM_ITEM(EntityTypes1_10.EntityType.ITEM, 10, MetaType1_7_6.Slot, EntityDataTypes1_8.ITEM),
    ARROW_IS_CRITICAL(EntityTypes1_10.EntityType.ARROW, 16, MetaType1_7_6.Byte, EntityDataTypes1_8.BYTE),
    FIREWORK_INFO(EntityTypes1_10.EntityType.FIREWORK_ROCKET, 8, MetaType1_7_6.Slot, EntityDataTypes1_8.ITEM),
    ITEM_FRAME_ITEM(EntityTypes1_10.EntityType.ITEM_FRAME, 2, MetaType1_7_6.Slot, 8, EntityDataTypes1_8.ITEM),
    ITEM_FRAME_ROTATION(EntityTypes1_10.EntityType.ITEM_FRAME, 3, MetaType1_7_6.Byte, 9, EntityDataTypes1_8.BYTE),
    ENDER_CRYSTAL_HEALTH(EntityTypes1_10.EntityType.END_CRYSTAL, 8, MetaType1_7_6.Int, 8, EntityDataTypes1_8.INT),
    RABBIT_TYPE(EntityTypes1_10.EntityType.RABBIT, -1, null, 18, EntityDataTypes1_8.BYTE),
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
    private final EntityDataTypes1_8 newType;

    MetaIndex1_8to1_7_6(EntityTypes1_10.EntityType entityType, int oldIndex, MetaType1_7_6 oldType, EntityDataTypes1_8 newType) {
        this.entityType = entityType;
        this.oldIndex = oldIndex;
        this.newIndex = oldIndex;
        this.oldType = oldType;
        this.newType = newType;
    }

    MetaIndex1_8to1_7_6(EntityTypes1_10.EntityType entityType, int oldIndex, MetaType1_7_6 oldType, int newIndex, EntityDataTypes1_8 newType) {
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

    public EntityDataTypes1_8 getNewType() {
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
