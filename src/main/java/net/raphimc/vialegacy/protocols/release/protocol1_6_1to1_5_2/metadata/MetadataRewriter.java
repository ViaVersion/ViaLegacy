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

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.minecraft.entities.EntityTypes1_10;
import com.viaversion.viaversion.api.minecraft.entitydata.EntityData;
import net.raphimc.vialegacy.ViaLegacy;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class MetadataRewriter {

    public static void transform(final EntityTypes1_10.EntityType type, final List<EntityData> list) {
        for (EntityData entry : new ArrayList<>(list)) {
            final MetaIndex1_6_1to1_5_2 metaIndex = MetaIndex1_6_1to1_5_2.searchIndex(type, entry.id());
            try {
                if (metaIndex == null) continue;

                final Object value = entry.getValue();
                entry.setTypeAndValue(metaIndex.getOldType(), value); // check if metadata type is the expected type from metaindex entry
                entry.setDataTypeUnsafe(metaIndex.getNewType());
                entry.setId(metaIndex.getNewIndex());

                switch (metaIndex.getNewType()) {
                    case Byte:
                        entry.setValue(((Number) value).byteValue());
                        break;
                    case Short:
                        entry.setValue(((Number) value).shortValue());
                        break;
                    case Int:
                        entry.setValue(((Number) value).intValue());
                        break;
                    case Float:
                        entry.setValue(((Number) value).floatValue());
                        break;
                    case Slot:
                    case String:
                    case Position:
                        break;
                    default:
                        if (!Via.getConfig().isSuppressConversionWarnings() || Via.getManager().isDebug()) {
                            ViaLegacy.getPlatform().getLogger().warning("1.5.2 MetaDataType: Unhandled Type: " + metaIndex.getNewType() + " " + entry);
                        }
                        list.remove(entry);
                        break;
                }
            } catch (Throwable e) {
                if (!Via.getConfig().isSuppressConversionWarnings() || Via.getManager().isDebug()) {
                    ViaLegacy.getPlatform().getLogger().log(Level.WARNING, "Error rewriting metadata entry for " + type.name() + ": " + entry, e);
                }
                list.remove(entry);
            }
        }
    }

}
