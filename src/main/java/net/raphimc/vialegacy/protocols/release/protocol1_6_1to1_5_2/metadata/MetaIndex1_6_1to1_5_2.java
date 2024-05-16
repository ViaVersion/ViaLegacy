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
package net.raphimc.vialegacy.protocols.release.protocol1_6_1to1_5_2.metadata;

import com.viaversion.viaversion.api.minecraft.entities.EntityTypes1_10;
import com.viaversion.viaversion.util.Pair;
import net.raphimc.vialegacy.protocols.release.protocol1_7_2_5to1_6_4.types.MetaType1_6_4;

import java.util.HashMap;
import java.util.Optional;

public enum MetaIndex1_6_1to1_5_2 {

    ENTITY_LIVING_POTION_EFFECT_COLOR(EntityTypes1_10.EntityType.LIVING_ENTITY_BASE, 8, MetaType1_6_4.Int, 7, MetaType1_6_4.Int),
    ENTITY_LIVING_IS_POTION_EFFECT_AMBIENT(EntityTypes1_10.EntityType.LIVING_ENTITY_BASE, 9, MetaType1_6_4.Byte, 8, MetaType1_6_4.Byte),
    ENTITY_LIVING_ARROWS(EntityTypes1_10.EntityType.LIVING_ENTITY_BASE, 10, MetaType1_6_4.Byte, 9, MetaType1_6_4.Byte),
    ENTITY_LIVING_NAME_TAG(EntityTypes1_10.EntityType.LIVING_ENTITY_BASE, 5, MetaType1_6_4.String, 10, MetaType1_6_4.String),
    ENTITY_LIVING_NAME_TAG_VISIBILITY(EntityTypes1_10.EntityType.LIVING_ENTITY_BASE, 6, MetaType1_6_4.Byte, 11, MetaType1_6_4.Byte),
    HUMAN_ABSORPTION_HEARTS(EntityTypes1_10.EntityType.PLAYER, 17, MetaType1_6_4.Byte, MetaType1_6_4.Float),
    BOAT_DAMAGE_TAKEN(EntityTypes1_10.EntityType.BOAT, 19, MetaType1_6_4.Int, MetaType1_6_4.Float),
    MINECART_DAMAGE_TAKEN(EntityTypes1_10.EntityType.ABSTRACT_MINECART, 19, MetaType1_6_4.Int, MetaType1_6_4.Float),
    WITHER_HEALTH(EntityTypes1_10.EntityType.WITHER, 16, MetaType1_6_4.Int, 6, MetaType1_6_4.Float),
    ENDER_DRAGON_HEALTH(EntityTypes1_10.EntityType.ENDER_DRAGON, 16, MetaType1_6_4.Int, 6, MetaType1_6_4.Float),
    WOLF_HEALTH(EntityTypes1_10.EntityType.WOLF, 18, MetaType1_6_4.Int, MetaType1_6_4.Float),
    ;

    private static final HashMap<Pair<EntityTypes1_10.EntityType, Integer>, MetaIndex1_6_1to1_5_2> metadataRewrites = new HashMap<>();

    static {
        for (MetaIndex1_6_1to1_5_2 index : MetaIndex1_6_1to1_5_2.values()) {
            metadataRewrites.put(new Pair<>(index.getEntityType(), index.getOldIndex()), index);
        }
    }

    private final EntityTypes1_10.EntityType entityType;
    private final int oldIndex;
    private final int newIndex;
    private final MetaType1_6_4 oldType;
    private final MetaType1_6_4 newType;

    MetaIndex1_6_1to1_5_2(EntityTypes1_10.EntityType entityType, int oldIndex, MetaType1_6_4 oldType, MetaType1_6_4 newType) {
        this.entityType = entityType;
        this.oldIndex = oldIndex;
        this.newIndex = oldIndex;
        this.oldType = oldType;
        this.newType = newType;
    }

    MetaIndex1_6_1to1_5_2(EntityTypes1_10.EntityType entityType, int oldIndex, MetaType1_6_4 oldType, int newIndex, MetaType1_6_4 newType) {
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

    public MetaType1_6_4 getOldType() {
        return this.oldType;
    }

    public MetaType1_6_4 getNewType() {
        return this.newType;
    }

    private static Optional<MetaIndex1_6_1to1_5_2> getIndex(EntityTypes1_10.EntityType type, int index) {
        final Pair<EntityTypes1_10.EntityType, Integer> pair = new Pair<>(type, index);
        return Optional.ofNullable(metadataRewrites.get(pair));
    }

    public static MetaIndex1_6_1to1_5_2 searchIndex(EntityTypes1_10.EntityType type, int index) {
        EntityTypes1_10.EntityType currentType = type;
        do {
            Optional<MetaIndex1_6_1to1_5_2> optMeta = getIndex(currentType, index);
            if (optMeta.isPresent()) {
                return optMeta.get();
            }

            currentType = currentType.getParent();
        } while (currentType != null);

        return null;
    }

}
