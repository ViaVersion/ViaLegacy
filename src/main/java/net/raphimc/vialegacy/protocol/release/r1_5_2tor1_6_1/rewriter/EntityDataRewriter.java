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
package net.raphimc.vialegacy.protocol.release.r1_5_2tor1_6_1.rewriter;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.minecraft.entities.EntityTypes1_8;
import com.viaversion.viaversion.api.minecraft.entitydata.EntityData;
import net.raphimc.vialegacy.ViaLegacy;
import net.raphimc.vialegacy.protocol.release.r1_5_2tor1_6_1.data.EntityDataIndex1_5_2;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class EntityDataRewriter {

    public static void transform(final EntityTypes1_8.EntityType type, final List<EntityData> list) {
        for (EntityData entry : new ArrayList<>(list)) {
            final EntityDataIndex1_5_2 entityDataIndex = EntityDataIndex1_5_2.searchIndex(type, entry.id());
            try {
                if (entityDataIndex == null) continue;

                final Object value = entry.getValue();
                entry.setTypeAndValue(entityDataIndex.getOldType(), value); // check if entity data type is the expected type from entity data index entry
                entry.setDataTypeUnsafe(entityDataIndex.getNewType());
                entry.setId(entityDataIndex.getNewIndex());

                switch (entityDataIndex.getNewType()) {
                    case BYTE:
                        entry.setValue(((Number) value).byteValue());
                        break;
                    case SHORT:
                        entry.setValue(((Number) value).shortValue());
                        break;
                    case INT:
                        entry.setValue(((Number) value).intValue());
                        break;
                    case FLOAT:
                        entry.setValue(((Number) value).floatValue());
                        break;
                    case ITEM:
                    case STRING:
                    case BLOCK_POSITION:
                        break;
                    default:
                        if (!Via.getConfig().isSuppressConversionWarnings() || Via.getManager().isDebug()) {
                            ViaLegacy.getPlatform().getLogger().warning("1.5.2 EntityDataRewriter: Unhandled Type: " + entityDataIndex.getNewType() + " " + entry);
                        }
                        list.remove(entry);
                        break;
                }
            } catch (Throwable e) {
                if (!Via.getConfig().isSuppressConversionWarnings() || Via.getManager().isDebug()) {
                    ViaLegacy.getPlatform().getLogger().log(Level.WARNING, "Error rewriting entity data entry for " + type.name() + ": " + entry, e);
                }
                list.remove(entry);
            }
        }
    }

}
