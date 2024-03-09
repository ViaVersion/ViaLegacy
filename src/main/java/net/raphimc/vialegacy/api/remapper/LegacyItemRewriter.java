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
package net.raphimc.vialegacy.api.remapper;

import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.packet.ServerboundPacketType;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.api.rewriter.ItemRewriter;
import com.viaversion.viaversion.api.rewriter.RewriterBase;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectArrayList;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectList;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ListTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;

public abstract class LegacyItemRewriter<P extends Protocol> extends RewriterBase<P> implements ItemRewriter<P> {

    private final ObjectList<RewriteEntry> rewriteEntries = new ObjectArrayList<>();
    private final ObjectList<NonExistentEntry> nonExistentItems = new ObjectArrayList<>();
    protected final String tagName;
    protected final String protocolName;
    private final Type<Item> itemType;
    private final Type<Item> mappedItemType;
    private final Type<Item[]> itemArrayType;
    private final Type<Item[]> mappedItemArrayType;

    public LegacyItemRewriter(final P protocol, final String protocolName, final Type<Item> itemType, final Type<Item[]> itemArrayType) {
        this(protocol, protocolName, itemType, itemArrayType, itemType, itemArrayType);
    }

    public LegacyItemRewriter(final P protocol, final String protocolName, final Type<Item> itemType, final Type<Item[]> itemArrayType, final Type<Item> mappedItemType, final Type<Item[]> mappedItemArrayType) {
        super(protocol);
        this.tagName = "ViaLegacy_" + protocolName.replace(".", "_");
        this.protocolName = protocolName;
        this.itemType = itemType;
        this.itemArrayType = itemArrayType;
        this.mappedItemType = mappedItemType;
        this.mappedItemArrayType = mappedItemArrayType;
    }

    protected void addRemappedItem(final int oldItemId, final int newItemId, final String newItemName) {
        this.addRemappedItem(oldItemId, newItemId, -1, newItemName);
    }

    protected void addRemappedItem(final int oldItemId, final int newItemId, final int newItemMeta, final String newItemName) {
        this.addRemappedItem(oldItemId, -1, newItemId, newItemMeta, newItemName);
    }

    protected void addRemappedItem(final int oldItemId, final int oldItemMeta, final int newItemId, final int newItemMeta, final String newItemName) {
        this.rewriteEntries.add(new RewriteEntry(oldItemId, (short) oldItemMeta, newItemId, (short) newItemMeta, newItemName));
    }

    protected void addNonExistentItem(final int itemId, final int itemMeta) {
        this.nonExistentItems.add(new NonExistentEntry(itemId, (short) itemMeta));
    }

    protected void addNonExistentItem(final int itemId, final int startItemMeta, final int endItemMeta) {
        for (int i = startItemMeta; i <= endItemMeta; i++) {
            this.nonExistentItems.add(new NonExistentEntry(itemId, (short) i));
        }
    }

    protected void addNonExistentItems(final int... itemIds) {
        for (int itemId : itemIds) {
            this.nonExistentItems.add(new NonExistentEntry(itemId, (short) -1));
        }
    }

    protected void addNonExistentItemRange(final int startItemId, final int endItemId) {
        for (int i = startItemId; i <= endItemId; i++) {
            this.nonExistentItems.add(new NonExistentEntry(i, (short) -1));
        }
    }


    public void registerCreativeInventoryAction(final ServerboundPacketType packetType) {
        this.protocol.registerServerbound(packetType, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.SHORT); // slot
                handler(wrapper -> handleServerboundItem(wrapper));
            }
        });
    }

    @Override
    public Item handleItemToClient(final Item item) {
        if (item == null) return null;

        for (RewriteEntry rewriteEntry : this.rewriteEntries) {
            if (rewriteEntry.rewrites(item)) {
                this.setRemappedNameRead(item, rewriteEntry.newItemName);
                if (rewriteEntry.newItemMeta != -1) {
                    item.setData(rewriteEntry.newItemMeta);
                }
                item.setIdentifier(rewriteEntry.newItemID);
            }
        }

        return item;
    }

    @Override
    public Item handleItemToServer(final Item item) {
        if (item == null) return null;

        for (NonExistentEntry nonExistentEntry : this.nonExistentItems) {
            if (nonExistentEntry.rewrites(item)) {
                item.setIdentifier(1);
                item.setData((short) 0);
                return item;
            }
        }

        this.setRemappedTagWrite(item);

        return item;
    }

    @Override
    public Type<Item> itemType() {
        return this.itemType;
    }

    @Override
    public Type<Item[]> itemArrayType() {
        return this.itemArrayType;
    }

    @Override
    public Type<Item> mappedItemType() {
        return this.mappedItemType;
    }

    @Override
    public Type<Item[]> mappedItemArrayType() {
        return this.mappedItemArrayType;
    }


    private void handleClientboundItem(final PacketWrapper wrapper) throws Exception {
        final Item item = this.handleItemToClient(wrapper.read(this.itemType));
        wrapper.write(this.mappedItemType, item);
    }

    private void handleServerboundItem(final PacketWrapper wrapper) throws Exception {
        final Item item = this.handleItemToServer(wrapper.read(this.mappedItemType));
        wrapper.write(this.itemType, item);
    }

    private void setRemappedNameRead(final Item item, final String name) {
        final CompoundTag viaLegacyTag = new CompoundTag();
        viaLegacyTag.putInt("Id", item.identifier());
        viaLegacyTag.putShort("Meta", item.data());

        CompoundTag tag = item.tag();
        if (tag == null) {
            tag = new CompoundTag();
            item.setTag(tag);
            viaLegacyTag.putBoolean("RemoveTag", true);
        }
        tag.put(this.tagName, viaLegacyTag);

        CompoundTag display = tag.getCompoundTag("display");
        if (display == null) {
            display = new CompoundTag();
            tag.put("display", display);
            viaLegacyTag.putBoolean("RemoveDisplayTag", true);
        }
        if (display.contains("Name")) {
            ListTag<StringTag> lore = display.getListTag("Lore", StringTag.class);
            if (lore == null) {
                lore = new ListTag<>(StringTag.class);
                display.put("Lore", lore);
                viaLegacyTag.putBoolean("RemoveLore", true);
            }
            lore.add(new StringTag("§r " + this.protocolName + " Item ID: " + item.identifier() + " (" + name + ")"));
            viaLegacyTag.putBoolean("RemoveLastLore", true);
        } else {
            display.putString("Name", "§r" + this.protocolName + " " + name);
            viaLegacyTag.putBoolean("RemoveDisplayName", true);
        }
    }

    private void setRemappedTagWrite(final Item item) {
        final CompoundTag tag = item.tag();
        if (tag == null) return;
        final CompoundTag viaLegacyTag = tag.removeUnchecked(this.tagName);
        if (viaLegacyTag == null) return;

        item.setIdentifier(viaLegacyTag.getNumberTag("Id").asInt());
        item.setData(viaLegacyTag.getNumberTag("Meta").asShort());
        if (viaLegacyTag.contains("RemoveLastLore")) {
            final ListTag<StringTag> lore = tag.getCompoundTag("display").getListTag("Lore", StringTag.class);
            lore.remove(lore.size() - 1);
        }
        if (viaLegacyTag.contains("RemoveLore")) {
            tag.getCompoundTag("display").remove("Lore");
        }
        if (viaLegacyTag.contains("RemoveDisplayName")) {
            tag.getCompoundTag("display").remove("Name");
        }
        if (viaLegacyTag.contains("RemoveDisplayTag")) {
            tag.remove("display");
        }
        if (viaLegacyTag.contains("RemoveTag")) {
            item.setTag(null);
        }
    }


    private static class RewriteEntry {

        private final int oldItemID;
        private final short oldItemMeta;
        private final int newItemID;
        private final short newItemMeta;
        private final String newItemName;

        public RewriteEntry(final int oldItemID, final short oldItemMeta, final int newItemID, final short newItemMeta, final String newItemName) {
            this.oldItemID = oldItemID;
            this.oldItemMeta = oldItemMeta;
            this.newItemID = newItemID;
            this.newItemMeta = newItemMeta;
            this.newItemName = newItemName;
        }

        public boolean rewrites(final Item item) {
            return item.identifier() == this.oldItemID && (this.oldItemMeta == -1 || this.oldItemMeta == item.data());
        }

    }

    private static class NonExistentEntry {

        private final int itemId;
        private final short itemMeta;

        public NonExistentEntry(final int itemId, final short itemMeta) {
            this.itemId = itemId;
            this.itemMeta = itemMeta;
        }

        public boolean rewrites(final Item item) {
            return item.identifier() == this.itemId && (this.itemMeta == -1 || this.itemMeta == item.data());
        }

    }

}
