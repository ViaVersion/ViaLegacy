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
import com.viaversion.viaversion.libs.opennbt.tag.builtin.*;

import java.util.List;

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
        this.tagName = protocolName.replace(".", "_") + "_ViaLegacy_" + System.currentTimeMillis();
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
        //Set ViaLegacy tag for later remapping
        final CompoundTag viaLegacyTag = (item.tag() != null && item.tag().contains(tagName) ? item.tag().get(tagName) : new CompoundTag());
        if (item.tag() == null || !item.tag().contains(tagName)) {
            viaLegacyTag.put("Id", new IntTag(item.identifier()));
            viaLegacyTag.put("Meta", new ShortTag(item.data()));
        }

        //Get Item tag
        CompoundTag tag = item.tag();
        if (tag == null) {
            tag = new CompoundTag();
            item.setTag(tag);
            viaLegacyTag.put("RemoveTag", new IntTag(0));
        }
        tag.put(tagName, viaLegacyTag);

        //Set name/lore of item
        CompoundTag display = tag.get("display");
        if (display == null) {
            display = new CompoundTag();
            tag.put("display", display);
            viaLegacyTag.put("RemoveDisplayTag", new IntTag(0));
        }
        if (display.contains("Name")) {
            ListTag lore = display.get("Lore");
            if (lore == null) {
                lore = new ListTag();
                display.put("Lore", lore);
                viaLegacyTag.put("RemoveLore", new IntTag(0));
            }
            lore.add(new StringTag("§r " + this.protocolName + " Item ID: " + item.identifier() + " (" + name + ")"));
            viaLegacyTag.put("RemoveLastLore", new IntTag(0));
        } else {
            display.put("Name", new StringTag("§r" + this.protocolName + " " + name));
            viaLegacyTag.put("RemoveDisplayName", new IntTag(0));
        }
    }

    private void setRemappedTagWrite(final Item item) {
        if (item.tag() == null) return;
        if (!item.tag().contains(tagName)) return;

        final CompoundTag tag = item.tag();
        final CompoundTag viaLegacyTag = tag.get(tagName);
        tag.remove(tagName);

        item.setIdentifier(((IntTag) viaLegacyTag.get("Id")).asInt());
        item.setData(((ShortTag) viaLegacyTag.get("Meta")).asShort());
        if (viaLegacyTag.contains("RemoveLastLore")) {
            ListTag lore = ((CompoundTag) tag.get("display")).get("Lore");
            List<Tag> tags = lore.getValue();
            tags.remove(lore.size() - 1);
            lore.setValue(tags);
        }
        if (viaLegacyTag.contains("RemoveLore")) {
            ((CompoundTag) tag.get("display")).remove("Lore");
        }
        if (viaLegacyTag.contains("RemoveDisplayName")) {
            ((CompoundTag) tag.get("display")).remove("Name");
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
