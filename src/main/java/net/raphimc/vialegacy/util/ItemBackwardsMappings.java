/*
 * This file is part of ViaProtocolHack - https://github.com/RaphiMC/ViaProtocolHack
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
package net.raphimc.vialegacy.util;

import com.viaversion.viabackwards.api.data.BackwardsMappings;
import com.viaversion.viaversion.api.data.MappingDataLoader;
import com.viaversion.viaversion.api.data.Mappings;
import com.viaversion.viaversion.api.data.ParticleMappings;
import com.viaversion.viaversion.libs.fastutil.objects.Object2IntMap;
import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viaversion.libs.gson.JsonObject;
import com.viaversion.viaversion.util.Int2IntBiHashMap;

import java.util.Map;

@Deprecated // Will be moved into ViaSnapshots
public class ItemBackwardsMappings extends BackwardsMappings {

    public ItemBackwardsMappings(String oldVersion, String newVersion) {
        super(oldVersion, newVersion, null, true);
        this.loadItems = true;
    }

    @Override
    public void load() {
        this.getLogger().info("Loading " + this.oldVersion + " -> " + this.newVersion + " mappings...");
        JsonObject diffmapping = this.hasDiffFile ? this.loadDiffFile() : null;
        JsonObject oldMappings = MappingDataLoader.loadData("mapping-" + this.oldVersion + ".json", true);
        JsonObject newMappings = MappingDataLoader.loadData("mapping-" + this.newVersion + ".json", true);
        this.blockMappings = this.loadFromObject(oldMappings, newMappings, diffmapping, "blocks");
        this.blockStateMappings = this.loadFromObject(oldMappings, newMappings, diffmapping, "blockstates");
        this.blockEntityMappings = this.loadFromArray(oldMappings, newMappings, diffmapping, "blockentities");
        this.soundMappings = this.loadFromArray(oldMappings, newMappings, diffmapping, "sounds");
        this.statisticsMappings = this.loadFromArray(oldMappings, newMappings, diffmapping, "statistics");
        Mappings particles = this.loadFromArray(oldMappings, newMappings, diffmapping, "particles");
        if (particles != null) {
            particleMappings = new ParticleMappings(oldMappings.getAsJsonArray("particles"), newMappings.getAsJsonArray("particles"), particles);
        }

        if (this.loadItems && newMappings.has("items")) {
            this.itemMappings = new Int2IntBiHashMap();
            this.itemMappings.defaultReturnValue(-1);
            final JsonObject oldItems = oldMappings.getAsJsonObject("items");
            final JsonObject newItems = newMappings.getAsJsonObject("items");
            final JsonObject diffItems = diffmapping != null ? diffmapping.getAsJsonObject("items") : null;

            final Object2IntMap<String> newIdentifierMap = MappingDataLoader.indexedObjectToMap(newItems);
            for (Map.Entry<String, JsonElement> entry : oldItems.entrySet()) {
                if (!newIdentifierMap.containsKey((entry.getValue()).getAsString())) {
                    if (diffItems == null || !diffItems.has(entry.getValue().getAsString())) continue;
                    newItems.add(String.valueOf(Integer.MAX_VALUE - Integer.parseInt(entry.getKey())), entry.getValue());
                }
            }

            MappingDataLoader.mapIdentifiers(this.itemMappings, oldItems, newItems, diffItems, true);
        }

        if (diffmapping != null && diffmapping.has("tags")) {
            throw new IllegalStateException("I hope this doesn't get executed");
        }

        this.loadExtras(oldMappings, newMappings, diffmapping);
    }

}
