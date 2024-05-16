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

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.entities.EntityTypes1_10;
import com.viaversion.viaversion.api.minecraft.entitydata.EntityData;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.util.IdAndData;
import net.raphimc.vialegacy.ViaLegacy;
import net.raphimc.vialegacy.protocols.release.protocol1_8to1_7_6_10.Protocol1_8to1_7_6_10;
import net.raphimc.vialegacy.protocols.release.protocol1_8to1_7_6_10.storage.ChunkTracker;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class MetadataRewriter {

    private final Protocol1_8to1_7_6_10 protocol;

    public MetadataRewriter(final Protocol1_8to1_7_6_10 protocol) {
        this.protocol = protocol;
    }

    public void transform(final UserConnection user, final EntityTypes1_10.EntityType type, final List<EntityData> list) {
        for (EntityData entry : new ArrayList<>(list)) {
            final MetaIndex1_8to1_7_6 metaIndex = MetaIndex1_8to1_7_6.searchIndex(type, entry.id());
            try {
                if (metaIndex == null) {
                    if (!Via.getConfig().isSuppressConversionWarnings() || Via.getManager().isDebug()) {
                        ViaLegacy.getPlatform().getLogger().warning("Could not find valid metaindex entry for " + type.name() + ": " + entry);
                    }
                    list.remove(entry);
                    continue;
                }

                final Object value = entry.getValue();
                entry.setTypeAndValue(metaIndex.getOldType(), value); // check if metadata type is the expected type from metaindex entry
                entry.setDataTypeUnsafe(metaIndex.getNewType());
                entry.setId(metaIndex.getNewIndex());

                if (metaIndex == MetaIndex1_8to1_7_6.ENTITY_AGEABLE_AGE) {
                    entry.setValue(((int) value) < 0 ? (byte) -1 : (byte) 0);
                    continue;
                } else if (metaIndex == MetaIndex1_8to1_7_6.ITEM_FRAME_ROTATION) {
                    entry.setValue(Integer.valueOf((Byte) value * 2).byteValue());
                    continue;
                } else if (metaIndex == MetaIndex1_8to1_7_6.ENDERMAN_CARRIED_BLOCK) {
                    final byte id = (byte) value;
                    EntityData blockDataMeta = null;
                    for (EntityData metadata : list) {
                        if (metadata.id() == MetaIndex1_8to1_7_6.ENDERMAN_CARRIED_BLOCK_DATA.getOldIndex()) {
                            blockDataMeta = metadata;
                            list.remove(blockDataMeta);
                            break;
                        }
                    }
                    final byte data = blockDataMeta != null ? (Byte) blockDataMeta.getValue() : 0;
                    final IdAndData block = new IdAndData(id, data);
                    user.get(ChunkTracker.class).remapBlockParticle(block);
                    entry.setValue((short) (block.getId() | (block.getData() << 12)));
                    continue;
                } else if (metaIndex == MetaIndex1_8to1_7_6.HUMAN_SKIN_FLAGS) {
                    byte flags = (byte) value;
                    boolean cape = (flags & 2) == 0;
                    flags = (byte) (cape ? 127 : 126);
                    entry.setValue(flags);
                    continue;
                }

                switch (metaIndex.getNewType()) {
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
                        this.protocol.getItemRewriter().handleItemToClient(user, (Item) value);
                        break;
                    case STRING:
                    case BLOCK_POSITION:
                    case ROTATIONS:
                        break;
                    default:
                        if (!Via.getConfig().isSuppressConversionWarnings() || Via.getManager().isDebug()) {
                            ViaLegacy.getPlatform().getLogger().warning("1.7.10 MetaDataType: Unhandled Type: " + metaIndex.getNewType() + " " + entry);
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
