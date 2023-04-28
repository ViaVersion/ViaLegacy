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
        super(protocol, "1.7.10");

        this.addRemappedItem(8, 326, "Water Block");
        this.addRemappedItem(9, 326, "Stationary Water Block");
        this.addRemappedItem(10, 327, "Lava Block");
        this.addRemappedItem(11, 327, "Stationary Lava Block");
        this.addRemappedItem(51, 385, "Fire");
        this.addRemappedItem(90, 399, "Nether portal");
        this.addRemappedItem(119, 381, "End portal");
        this.addRemappedItem(127, 351, 3, "Cocoa Block");
        this.addRemappedItem(141, 391, "Carrot Crops");
        this.addRemappedItem(142, 392, "Potato Crops");
        this.addRemappedItem(43, 44, "Double Stone Slab");
        this.addRemappedItem(125, 126, "Double Wood Slab");

        this.addNonExistentItem(1, 1, 6);
        this.addNonExistentItem(3, 1);
        this.addNonExistentItem(19, 1);
        this.addNonExistentItemRange(165, 169);
        this.addNonExistentItemRange(179, 192);
        this.addNonExistentItem(383, 67);
        this.addNonExistentItem(383, 68);
        this.addNonExistentItem(383, 101);
        this.addNonExistentItemRange(409, 416);
        this.addNonExistentItemRange(423, 425);
        this.addNonExistentItemRange(427, 431);
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

            if (skullOwnerName != null && ViaLegacy.getConfig().isLegacySkullLoading()) {
                final GameProfileFetcher gameProfileFetcher = Via.getManager().getProviders().get(GameProfileFetcher.class);

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
