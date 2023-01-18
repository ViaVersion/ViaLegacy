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
package net.raphimc.vialegacy.protocols.release.protocol1_8to1_7_6_10.rewriter;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
import net.raphimc.vialegacy.ViaLegacy;
import net.raphimc.vialegacy.api.data.ItemList1_6;
import net.raphimc.vialegacy.api.remapper.LegacyItemRewriter;
import net.raphimc.vialegacy.protocols.release.protocol1_7_6_10to1_7_2_5.Protocol1_7_6_10to1_7_2_5;
import net.raphimc.vialegacy.protocols.release.protocol1_8to1_7_6_10.Protocol1_8to1_7_6_10;
import net.raphimc.vialegacy.protocols.release.protocol1_8to1_7_6_10.model.GameProfile;
import net.raphimc.vialegacy.protocols.release.protocol1_8to1_7_6_10.providers.GameProfileFetcher;

import java.util.UUID;

public class ItemRewriter extends LegacyItemRewriter<Protocol1_8to1_7_6_10> {

    public ItemRewriter(final Protocol1_8to1_7_6_10 protocol) {
        super(protocol, "1.7");
        addRemappedItem(8, 326, "Water Block");
        addRemappedItem(9, 326, "Stationary Water Block");
        addRemappedItem(10, 327, "Lava Block");
        addRemappedItem(11, 327, "Stationary Lava Block");
        addRemappedItem(51, 385, "Fire");
        addRemappedItem(90, 399, "Nether portal");
        addRemappedItem(119, 381, "End portal");
        addRemappedItem(127, 351, 3, "Cocoa Block");
        addRemappedItem(141, 391, "Carrot Crops");
        addRemappedItem(142, 392, "Potato Crops");
        addRemappedItem(43, 44, "Double Stone Slab");
        addRemappedItem(125, 126, "Double Wood Slab");
    }

    @Override
    public Item handleItemToClient(Item item) {
        super.handleItemToClient(item);
        if (item == null) return null;

        if (item.identifier() == ItemList1_6.skull.itemID && item.data() == 3 && item.tag() != null) { // player_skull
            if (!item.tag().contains("SkullOwner")) return item;

            String skullOwnerName = null;
            if (item.tag().get("SkullOwner") instanceof StringTag) {
                final StringTag skullOwnerTag = item.tag().remove("SkullOwner");
                item.tag().put("1_7_SkullOwner", skullOwnerTag);
                skullOwnerName = skullOwnerTag.getValue();
            } else if (item.tag().get("SkullOwner") instanceof CompoundTag) {
                final CompoundTag skullOwnerTag = item.tag().get("SkullOwner");
                if (skullOwnerTag.get("Name") instanceof StringTag && !skullOwnerTag.contains("Id")) {
                    final StringTag skullOwnerNameTag = skullOwnerTag.get("Name");
                    skullOwnerName = skullOwnerNameTag.getValue();
                }
            }

            if (skullOwnerName != null) {
                final GameProfileFetcher gameProfileFetcher = Via.getManager().getProviders().get(GameProfileFetcher.class);
                if (!ViaLegacy.getConfig().isLegacySkullLoading()) return item;

                if (gameProfileFetcher.isUUIDLoaded(skullOwnerName)) {
                    final UUID uuid = gameProfileFetcher.getMojangUUID(skullOwnerName);
                    if (gameProfileFetcher.isGameProfileLoaded(uuid)) {
                        final GameProfile skullProfile = gameProfileFetcher.getGameProfile(uuid);
                        if (skullProfile == null || skullProfile.isOffline()) return item;
                        item.tag().put("SkullOwner", Protocol1_7_6_10to1_7_2_5.writeGameProfileToTag(skullProfile));
                        return item;
                    }
                }

                gameProfileFetcher.getMojangUUIDAsync(skullOwnerName).thenAccept(gameProfileFetcher::getGameProfile);
            }
        }

        return item;
    }

    @Override
    public Item handleItemToServer(Item item) {
        if (item == null) return null;

        NOT_VALID:
        if (item.identifier() == ItemList1_6.skull.itemID && item.data() == 3 && item.tag() != null) { // player_skull
            if (!item.tag().contains("1_7_SkullOwner")) break NOT_VALID;
            if (item.tag().get("1_7_SkullOwner") instanceof StringTag) {
                item.tag().put("SkullOwner", item.tag().remove("1_7_SkullOwner"));
            }
        }

        return super.handleItemToServer(item);
    }

}
