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
package net.raphimc.vialegacy.protocol.release.r1_7_6_10tor1_8.rewriter;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.entities.EntityTypes1_8;
import com.viaversion.viaversion.api.minecraft.entitydata.EntityData;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.util.IdAndData;
import net.raphimc.vialegacy.ViaLegacy;
import net.raphimc.vialegacy.protocol.release.r1_7_6_10tor1_8.Protocolr1_7_6_10Tor1_8;
import net.raphimc.vialegacy.protocol.release.r1_7_6_10tor1_8.data.EntityDataIndex1_7_6;
import net.raphimc.vialegacy.protocol.release.r1_7_6_10tor1_8.storage.ChunkTracker;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class EntityDataRewriter {

    private final Protocolr1_7_6_10Tor1_8 protocol;

    public EntityDataRewriter(final Protocolr1_7_6_10Tor1_8 protocol) {
        this.protocol = protocol;
    }

    public void transform(final UserConnection user, final EntityTypes1_8.EntityType type, final List<EntityData> list) {
        for (EntityData entry : new ArrayList<>(list)) {
            final EntityDataIndex1_7_6 entityDataIndex = EntityDataIndex1_7_6.searchIndex(type, entry.id());
            try {
                if (entityDataIndex == null) {
                    if (Via.getConfig().logEntityDataErrors()) {
                        ViaLegacy.getPlatform().getLogger().warning("Could not find valid entity data index entry for " + type.name() + ": " + entry);
                    }
                    list.remove(entry);
                    continue;
                }

                final Object value = entry.getValue();
                entry.setTypeAndValue(entityDataIndex.getOldType(), value); // check if entity data type is the expected type from entity data index entry
                entry.setDataTypeUnsafe(entityDataIndex.getNewType());
                entry.setId(entityDataIndex.getNewIndex());

                if (entityDataIndex == EntityDataIndex1_7_6.ENTITY_AGEABLE_AGE) {
                    entry.setValue(((int) value) < 0 ? (byte) -1 : (byte) 0);
                    continue;
                } else if (entityDataIndex == EntityDataIndex1_7_6.ITEM_FRAME_ROTATION) {
                    entry.setValue(Integer.valueOf((Byte) value * 2).byteValue());
                    continue;
                } else if (entityDataIndex == EntityDataIndex1_7_6.ENDERMAN_CARRIED_BLOCK) {
                    final byte id = (byte) value;
                    EntityData blockDataMeta = null;
                    for (EntityData entityData : list) {
                        if (entityData.id() == EntityDataIndex1_7_6.ENDERMAN_CARRIED_BLOCK_DATA.getOldIndex()) {
                            blockDataMeta = entityData;
                            list.remove(blockDataMeta);
                            break;
                        }
                    }
                    final byte data = blockDataMeta != null ? (Byte) blockDataMeta.getValue() : 0;
                    final IdAndData block = new IdAndData(id, data);
                    user.get(ChunkTracker.class).remapBlockParticle(block);
                    entry.setValue((short) (block.getId() | (block.getData() << 12)));
                    continue;
                } else if (entityDataIndex == EntityDataIndex1_7_6.HUMAN_SKIN_FLAGS) {
                    byte flags = (byte) value;
                    boolean cape = (flags & 2) == 0;
                    flags = (byte) (cape ? 127 : 126);
                    entry.setValue(flags);
                    continue;
                }

                switch (entityDataIndex.getNewType()) {
                    case BYTE -> entry.setValue(((Number) value).byteValue());
                    case SHORT -> entry.setValue(((Number) value).shortValue());
                    case INT -> entry.setValue(((Number) value).intValue());
                    case FLOAT -> entry.setValue(((Number) value).floatValue());
                    case ITEM -> this.protocol.getItemRewriter().handleItemToClient(user, (Item) value);
                    case STRING, BLOCK_POSITION, ROTATIONS -> {
                    }
                    default -> {
                        if (Via.getConfig().logEntityDataErrors()) {
                            ViaLegacy.getPlatform().getLogger().warning("1.7.10 EntityDataRewriter: Unhandled Type: " + entityDataIndex.getNewType() + " " + entry);
                        }
                        list.remove(entry);
                    }
                }
            } catch (Throwable e) {
                if (Via.getConfig().logEntityDataErrors()) {
                    ViaLegacy.getPlatform().getLogger().log(Level.WARNING, "Error rewriting entity data entry for " + type.name() + ": " + entry, e);
                }
                list.remove(entry);
            }
        }
    }

}
