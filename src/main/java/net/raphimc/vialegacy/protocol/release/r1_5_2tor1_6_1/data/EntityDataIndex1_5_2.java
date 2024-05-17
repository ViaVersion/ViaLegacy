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
package net.raphimc.vialegacy.protocol.release.r1_5_2tor1_6_1.data;

import com.viaversion.viaversion.api.minecraft.entities.EntityTypes1_8;
import com.viaversion.viaversion.util.Pair;
import net.raphimc.vialegacy.protocol.release.r1_6_4tor1_7_2_5.types.EntityDataTypes1_6_4;

import java.util.HashMap;
import java.util.Optional;

public enum EntityDataIndex1_5_2 {

    ENTITY_LIVING_POTION_EFFECT_COLOR(EntityTypes1_8.EntityType.LIVING_ENTITY_BASE, 8, EntityDataTypes1_6_4.INT, 7, EntityDataTypes1_6_4.INT),
    ENTITY_LIVING_IS_POTION_EFFECT_AMBIENT(EntityTypes1_8.EntityType.LIVING_ENTITY_BASE, 9, EntityDataTypes1_6_4.BYTE, 8, EntityDataTypes1_6_4.BYTE),
    ENTITY_LIVING_ARROWS(EntityTypes1_8.EntityType.LIVING_ENTITY_BASE, 10, EntityDataTypes1_6_4.BYTE, 9, EntityDataTypes1_6_4.BYTE),
    ENTITY_LIVING_NAME_TAG(EntityTypes1_8.EntityType.LIVING_ENTITY_BASE, 5, EntityDataTypes1_6_4.STRING, 10, EntityDataTypes1_6_4.STRING),
    ENTITY_LIVING_NAME_TAG_VISIBILITY(EntityTypes1_8.EntityType.LIVING_ENTITY_BASE, 6, EntityDataTypes1_6_4.BYTE, 11, EntityDataTypes1_6_4.BYTE),
    HUMAN_ABSORPTION_HEARTS(EntityTypes1_8.EntityType.PLAYER, 17, EntityDataTypes1_6_4.BYTE, EntityDataTypes1_6_4.FLOAT),
    BOAT_DAMAGE_TAKEN(EntityTypes1_8.EntityType.BOAT, 19, EntityDataTypes1_6_4.INT, EntityDataTypes1_6_4.FLOAT),
    MINECART_DAMAGE_TAKEN(EntityTypes1_8.EntityType.ABSTRACT_MINECART, 19, EntityDataTypes1_6_4.INT, EntityDataTypes1_6_4.FLOAT),
    WITHER_HEALTH(EntityTypes1_8.EntityType.WITHER, 16, EntityDataTypes1_6_4.INT, 6, EntityDataTypes1_6_4.FLOAT),
    ENDER_DRAGON_HEALTH(EntityTypes1_8.EntityType.ENDER_DRAGON, 16, EntityDataTypes1_6_4.INT, 6, EntityDataTypes1_6_4.FLOAT),
    WOLF_HEALTH(EntityTypes1_8.EntityType.WOLF, 18, EntityDataTypes1_6_4.INT, EntityDataTypes1_6_4.FLOAT),
    ;

    private static final HashMap<Pair<EntityTypes1_8.EntityType, Integer>, EntityDataIndex1_5_2> ENTITY_DATA_REWRITES = new HashMap<>();

    static {
        for (EntityDataIndex1_5_2 index : EntityDataIndex1_5_2.values()) {
            ENTITY_DATA_REWRITES.put(new Pair<>(index.getEntityType(), index.getOldIndex()), index);
        }
    }

    private final EntityTypes1_8.EntityType entityType;
    private final int oldIndex;
    private final int newIndex;
    private final EntityDataTypes1_6_4 oldType;
    private final EntityDataTypes1_6_4 newType;

    EntityDataIndex1_5_2(EntityTypes1_8.EntityType entityType, int oldIndex, EntityDataTypes1_6_4 oldType, EntityDataTypes1_6_4 newType) {
        this.entityType = entityType;
        this.oldIndex = oldIndex;
        this.newIndex = oldIndex;
        this.oldType = oldType;
        this.newType = newType;
    }

    EntityDataIndex1_5_2(EntityTypes1_8.EntityType entityType, int oldIndex, EntityDataTypes1_6_4 oldType, int newIndex, EntityDataTypes1_6_4 newType) {
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

    public EntityDataTypes1_6_4 getOldType() {
        return this.oldType;
    }

    public EntityDataTypes1_6_4 getNewType() {
        return this.newType;
    }

    private static Optional<EntityDataIndex1_5_2> getIndex(EntityTypes1_8.EntityType type, int index) {
        final Pair<EntityTypes1_8.EntityType, Integer> pair = new Pair<>(type, index);
        return Optional.ofNullable(ENTITY_DATA_REWRITES.get(pair));
    }

    public static EntityDataIndex1_5_2 searchIndex(EntityTypes1_8.EntityType type, int index) {
        EntityTypes1_8.EntityType currentType = type;
        do {
            Optional<EntityDataIndex1_5_2> optMeta = getIndex(currentType, index);
            if (optMeta.isPresent()) {
                return optMeta.get();
            }

            currentType = currentType.getParent();
        } while (currentType != null);

        return null;
    }

}
