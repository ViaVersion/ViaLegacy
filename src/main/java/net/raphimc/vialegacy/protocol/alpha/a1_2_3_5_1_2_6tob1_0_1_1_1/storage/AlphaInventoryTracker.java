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
package net.raphimc.vialegacy.protocol.alpha.a1_2_3_5_1_2_6tob1_0_1_1_1.storage;

import com.viaversion.viaversion.api.connection.StoredObject;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.BlockPosition;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.type.Types;
import com.viaversion.viaversion.util.IdAndData;
import net.raphimc.vialegacy.api.LegacyProtocolVersion;
import net.raphimc.vialegacy.api.data.BlockList1_6;
import net.raphimc.vialegacy.api.data.ItemList1_6;
import net.raphimc.vialegacy.api.util.BlockFaceUtil;
import net.raphimc.vialegacy.protocol.alpha.a1_2_3_5_1_2_6tob1_0_1_1_1.Protocola1_2_3_5_1_2_6Tob1_0_1_1_1;
import net.raphimc.vialegacy.protocol.alpha.a1_2_3_5_1_2_6tob1_0_1_1_1.data.AlphaItems;
import net.raphimc.vialegacy.protocol.alpha.a1_2_3_5_1_2_6tob1_0_1_1_1.data.CraftingManager;
import net.raphimc.vialegacy.protocol.beta.b1_1_2tob1_2_0_2.packet.ClientboundPacketsb1_1;
import net.raphimc.vialegacy.protocol.beta.b1_1_2tob1_2_0_2.types.Typesb1_1;
import net.raphimc.vialegacy.protocol.release.r1_4_2tor1_4_4_5.types.Types1_4_2;
import net.raphimc.vialegacy.protocol.release.r1_6_4tor1_7_2_5.storage.ChunkTracker;

import java.util.Arrays;

import static net.raphimc.vialegacy.protocol.alpha.a1_2_3_5_1_2_6tob1_0_1_1_1.Protocola1_2_3_5_1_2_6Tob1_0_1_1_1.*;

public class AlphaInventoryTracker extends StoredObject {

    private boolean creativeMode;

    private Item[] mainInventory = null;
    private Item[] craftingInventory = null;
    private Item[] armorInventory = null;

    private Item[] openContainerItems = null;
    private Item cursorItem = null;

    private int openWindowType = -1;

    final InventoryStorage inventoryStorage;

    public AlphaInventoryTracker(UserConnection user) {
        super(user);
        this.inventoryStorage = user.get(InventoryStorage.class);
        this.onRespawn();
    }

    public void onWindowOpen(final int windowType, final int containerSlots) {
        this.openWindowType = windowType;
        this.openContainerItems = new Item[containerSlots];
    }

    public void onWindowClose() {
        if (this.openWindowType == 1) { // crafting table
            for (int i = 1; i <= 9; i++) {
                final Item item = this.openContainerItems[i];
                if (item == null) continue;
                dropItem(this.user(), item, false);
                this.openContainerItems[i] = null;
            }
        }
        for (int i = 0; i < 4; i++) {
            final Item item = this.craftingInventory[i];
            if (item == null) continue;
            dropItem(this.user(), item, false);
            this.craftingInventory[i] = null;
        }

        if (this.cursorItem != null) {
            dropItem(this.user(), this.cursorItem, false);
            this.cursorItem = null;
        }
        this.openWindowType = -1;

        this.updatePlayerInventory();
        this.updateCursorItem();
    }

    public void onWindowClick(final byte windowId, final short slot, final byte button, final short action, final Item item) {
        final boolean leftClick = button != 1;

        if (slot == -999) {
            if (this.cursorItem != null) {
                if (leftClick) {
                    dropItem(this.user(), this.cursorItem, false);
                    this.cursorItem = null;
                } else {
                    dropItem(this.user(), this.splitStack(this.cursorItem, 1), false);
                }
            }
        } else {
            Item[] slots = null;
            boolean slotTakesItems = true;
            int slotStackLimit = 64;
            boolean isCraftingResultSlot = false;
            switch (windowId) {
                case 0 -> {
                    slots = new Item[45];
                    System.arraycopy(this.mainInventory, 0, slots, 36, 9);
                    System.arraycopy(this.mainInventory, 9, slots, 9, 36 - 9);
                    System.arraycopy(this.craftingInventory, 0, slots, 1, 4);
                    System.arraycopy(reverseArray(this.armorInventory), 0, slots, 5, 4);
                    isCraftingResultSlot = slot == 0; // crafting result slot
                    slotTakesItems = !isCraftingResultSlot;
                    slotStackLimit = slot >= 5 && slot <= 8 ? 1 : 64; // armor slots
                    if (isCraftingResultSlot) {
                        slots[0] = CraftingManager.getResult(this.craftingInventory);
                    }
                }
                case InventoryStorage.WORKBENCH_WID -> {
                    slots = new Item[46];
                    System.arraycopy(this.openContainerItems, 0, slots, 0, 10);
                    System.arraycopy(this.mainInventory, 0, slots, 37, 9);
                    System.arraycopy(this.mainInventory, 9, slots, 10, 36 - 9);
                    isCraftingResultSlot = slot == 0; // crafting result slot
                    slotTakesItems = !isCraftingResultSlot;
                    if (isCraftingResultSlot) {
                        final Item[] craftingGrid = Arrays.copyOfRange(this.openContainerItems, 1, 10);
                        slots[0] = CraftingManager.getResult(craftingGrid);
                    }
                }
                case InventoryStorage.FURNACE_WID -> {
                    slots = new Item[39];
                    System.arraycopy(this.openContainerItems, 0, slots, 0, 3);
                    System.arraycopy(this.mainInventory, 0, slots, 30, 9);
                    System.arraycopy(this.mainInventory, 9, slots, 3, 36 - 9);
                }
                case InventoryStorage.CHEST_WID -> {
                    slots = new Item[63];
                    System.arraycopy(this.openContainerItems, 0, slots, 0, 3 * 9);
                    System.arraycopy(this.mainInventory, 0, slots, 54, 9);
                    System.arraycopy(this.mainInventory, 9, slots, 27, 36 - 9);
                }
            }

            if (slots != null) {
                final Item itm = slots[slot];
                if (itm == null) { // click empty slot
                    if (this.cursorItem != null && slotTakesItems) { // with item on cursor
                        int amount = leftClick ? this.cursorItem.amount() : 1;
                        if (amount > slotStackLimit) amount = slotStackLimit;
                        slots[slot] = splitStack(this.cursorItem, amount);
                    }
                } else if (this.cursorItem == null) { // click filled slot with no item on cursor
                    int amount = leftClick ? itm.amount() : (itm.amount() + 1) / 2;
                    if (isCraftingResultSlot) amount = itm.amount(); // crafting result slot does not support taking half the stack
                    this.cursorItem = splitStack(itm, amount);
                    if (isCraftingResultSlot) this.onCraftingResultPickup(windowId, slots);
                } else if (slotTakesItems) { // click filled slot with item on cursor
                    if (itm.identifier() != this.cursorItem.identifier()) { // items are different
                        if (this.cursorItem.amount() <= slotStackLimit) {
                            slots[slot] = this.cursorItem;
                            this.cursorItem = itm;
                        }
                    } else { // items are the same
                        int amount = leftClick ? this.cursorItem.amount() : 1;
                        if (amount > slotStackLimit - itm.amount()) amount = slotStackLimit - itm.amount();
                        if (amount > AlphaItems.getMaxStackSize(this.cursorItem.identifier()) - itm.amount()) {
                            amount = AlphaItems.getMaxStackSize(this.cursorItem.identifier()) - itm.amount();
                        }
                        this.cursorItem.setAmount(this.cursorItem.amount() - amount);
                        itm.setAmount(itm.amount() + amount);
                    }
                } else if (itm.identifier() == this.cursorItem.identifier() && AlphaItems.getMaxStackSize(this.cursorItem.identifier()) > 1) {
                    int amount = itm.amount();
                    if (amount > 0 && amount + this.cursorItem.amount() <= AlphaItems.getMaxStackSize(this.cursorItem.identifier())) {
                        itm.setAmount(itm.amount() - amount);
                        this.cursorItem.setAmount(this.cursorItem.amount() + amount);
                        if (isCraftingResultSlot) this.onCraftingResultPickup(windowId, slots);
                    }
                }

                for (int i = 0; i < slots.length; i++) {
                    final Item slotItem = slots[i];
                    if (slotItem != null && slotItem.amount() == 0) slots[i] = null;
                }

                switch (windowId) {
                    case 0 -> {
                        System.arraycopy(slots, 36, this.mainInventory, 0, 9);
                        System.arraycopy(slots, 9, this.mainInventory, 9, 36 - 9);
                        System.arraycopy(slots, 1, this.craftingInventory, 0, 4);
                        System.arraycopy(slots, 5, this.armorInventory, 0, 4);
                        this.armorInventory = reverseArray(this.armorInventory);
                    }
                    case InventoryStorage.WORKBENCH_WID -> {
                        System.arraycopy(slots, 0, this.openContainerItems, 0, 10);
                        System.arraycopy(slots, 37, this.mainInventory, 0, 9);
                        System.arraycopy(slots, 10, this.mainInventory, 9, 36 - 9);
                    }
                    case InventoryStorage.FURNACE_WID -> {
                        System.arraycopy(slots, 0, this.openContainerItems, 0, 3);
                        System.arraycopy(slots, 30, this.mainInventory, 0, 9);
                        System.arraycopy(slots, 3, this.mainInventory, 9, 36 - 9);
                    }
                    case InventoryStorage.CHEST_WID -> {
                        System.arraycopy(slots, 0, this.openContainerItems, 0, 3 * 9);
                        System.arraycopy(slots, 54, this.mainInventory, 0, 9);
                        System.arraycopy(slots, 27, this.mainInventory, 9, 36 - 9);
                    }
                }
                this.updateInventory(windowId, slots);

                boolean updateCraftResultSlot = false;
                switch (windowId) {
                    case 0 -> {
                        updateCraftResultSlot = !this.isEmpty(this.craftingInventory);
                        if (updateCraftResultSlot) {
                            slots[0] = CraftingManager.getResult(this.craftingInventory);
                        }
                    }
                    case InventoryStorage.WORKBENCH_WID -> {
                        final Item[] craftingGrid = Arrays.copyOfRange(this.openContainerItems, 1, 10);
                        updateCraftResultSlot = !this.isEmpty(craftingGrid);
                        if (updateCraftResultSlot) {
                            slots[0] = CraftingManager.getResult(craftingGrid);
                        }
                    }
                }

                if (updateCraftResultSlot) this.updateInventorySlot(windowId, (short) 0, slots[0]);
            } else {
                this.updatePlayerInventory();
            }
        }

        if (this.cursorItem != null && this.cursorItem.amount() == 0) {
            this.cursorItem = null;
        }
        this.updateCursorItem();
    }

    public void onBlockPlace(final BlockPosition position, final short direction) {
        if (this.creativeMode) return;

        final Item handItem = this.mainInventory[this.inventoryStorage.selectedHotbarSlot];
        if (handItem == null) return;

        if (direction == 255) { // interact
            AlphaItems.doInteract(handItem);
        } else { // place
            final IdAndData placedAgainst = this.user().get(ChunkTracker.class).getBlockNotNull(position);
            final IdAndData targetBlock = this.user().get(ChunkTracker.class).getBlockNotNull(position.getRelative(BlockFaceUtil.getFace(direction)));
            AlphaItems.doPlace(handItem, direction, placedAgainst);

            if (handItem.identifier() < 256 || handItem.identifier() == ItemList1_6.reed.itemId()) { // block item
                if (targetBlock.getId() == 0 || targetBlock.getId() == BlockList1_6.waterStill.blockId() || targetBlock.getId() == BlockList1_6.waterMoving.blockId() || targetBlock.getId() == BlockList1_6.lavaStill.blockId() || targetBlock.getId() == BlockList1_6.lavaMoving.blockId() || targetBlock.getId() == BlockList1_6.fire.blockId() || targetBlock.getId() == BlockList1_6.snow.blockId()) {
                    handItem.setAmount(handItem.amount() - 1);
                }
            } else if (handItem.identifier() == ItemList1_6.sign.itemId()) {
                if (direction != 0 && targetBlock.getId() == 0) handItem.setAmount(handItem.amount() - 1);
            } else if (handItem.identifier() == ItemList1_6.redstone.itemId()) {
                if (targetBlock.getId() == 0) handItem.setAmount(handItem.amount() - 1);
            }
        }

        if (handItem.amount() == 0) {
            this.mainInventory[this.inventoryStorage.selectedHotbarSlot] = null;
        }
        this.updatePlayerInventorySlot(this.inventoryStorage.selectedHotbarSlot);
    }

    public void onHandItemDrop() {
        final Item handItem = this.mainInventory[this.inventoryStorage.selectedHotbarSlot];
        if (handItem == null) return;

        handItem.setAmount(handItem.amount() - 1);
        if (handItem.amount() == 0) {
            this.mainInventory[this.inventoryStorage.selectedHotbarSlot] = null;
        }
        this.updatePlayerInventorySlot(this.inventoryStorage.selectedHotbarSlot);
    }

    public void onRespawn() {
        this.mainInventory = new Item[37];
        this.craftingInventory = new Item[4];
        this.armorInventory = new Item[4];
        this.openContainerItems = new Item[0];
        this.cursorItem = null;
        this.openWindowType = -1;
    }

    public void addItem(final Item item) {
        if (item == null) return;
        if (item.amount() == 0) return;

        if (item.data() == 0) { // item is stackable, try to merge it with stacks of same type
            int slot = -1;
            for (int i = 0; i < this.mainInventory.length; i++) {
                if (this.mainInventory[i] != null && this.mainInventory[i].identifier() == item.identifier() && this.mainInventory[i].amount() < AlphaItems.getMaxStackSize(this.mainInventory[i].identifier())) {
                    slot = i;
                    break;
                }
            }

            if (slot != -1) {
                int amount = item.amount();
                if (amount > AlphaItems.getMaxStackSize(this.mainInventory[slot].identifier()) - this.mainInventory[slot].amount()) {
                    amount = AlphaItems.getMaxStackSize(this.mainInventory[slot].identifier()) - this.mainInventory[slot].amount();
                }

                item.setAmount(item.amount() - amount);
                this.mainInventory[slot].setAmount(this.mainInventory[slot].amount() + amount);
                this.updatePlayerInventorySlot((short) slot);
            }
        }

        if (item.amount() != 0) { // if the item couldn't be fully merged with another stack or is unstackable
            int slot = -1;
            for (int i = 0; i < this.mainInventory.length; i++) {
                if (this.mainInventory[i] == null) {
                    slot = i;
                    break;
                }
            }

            if (slot != -1) {
                this.mainInventory[slot] = item;
                this.updatePlayerInventorySlot((short) slot);
            }
        }
    }

    // Add support for cheating items and classic block placement
    public void handleCreativeSetSlot(short slot, Item item) {
        if (!this.user().getProtocolInfo().serverProtocolVersion().equals(LegacyProtocolVersion.c0_30cpe)) item = fixItem(item);
        if (slot <= 0) return;

        if (slot <= 4) {
            slot--; // remove result slot
            this.craftingInventory[slot] = item;
        } else if (slot <= 8) {
            slot -= 5;
            slot = (short) (3 - slot); // reverse array
            this.armorInventory[slot] = item;
        } else if (slot <= 44) {
            if (slot >= 36) slot -= 36;
            this.mainInventory[slot] = item;
        }
    }

    public void setCreativeMode(final boolean creativeMode) {
        this.creativeMode = creativeMode;
    }

    public void setMainInventory(final Item[] items) {
        this.mainInventory = items;
    }

    public void setCraftingInventory(final Item[] items) {
        this.craftingInventory = items;
    }

    public void setArmorInventory(final Item[] items) {
        this.armorInventory = items;
    }

    public void setOpenContainerItems(final Item[] items) {
        this.openContainerItems = items;
    }

    public Item[] getMainInventory() {
        return this.mainInventory;
    }

    public Item[] getCraftingInventory() {
        return this.craftingInventory;
    }

    public Item[] getArmorInventory() {
        return this.armorInventory;
    }

    public Item[] getOpenContainerItems() {
        return this.openContainerItems;
    }

    public Item getCursorItem() {
        return this.cursorItem;
    }

    private void updatePlayerInventorySlot(final short slot) {
        this.updateInventorySlot((byte) 0, slot >= 0 && slot < 9 ? (short) (slot + 36) : slot, this.mainInventory[slot]);
    }

    private void updateCursorItem() {
        this.updateInventorySlot((byte) -1, (short) 0, this.cursorItem);
    }

    private void updateInventorySlot(final byte windowId, final short slot, final Item item) {
        final PacketWrapper setSlot = PacketWrapper.create(ClientboundPacketsb1_1.CONTAINER_SET_SLOT, this.user());
        setSlot.write(Types.BYTE, windowId); // window id
        setSlot.write(Types.SHORT, slot); // slot
        setSlot.write(Typesb1_1.NBTLESS_ITEM, copyItem(item)); // item
        setSlot.send(Protocola1_2_3_5_1_2_6Tob1_0_1_1_1.class);
    }

    private void updatePlayerInventory() {
        final Item[] items = new Item[45];
        System.arraycopy(this.mainInventory, 0, items, 36, 9);
        System.arraycopy(this.mainInventory, 9, items, 9, 36 - 9);
        System.arraycopy(this.craftingInventory, 0, items, 1, 4);
        System.arraycopy(reverseArray(this.armorInventory), 0, items, 5, 4);

        this.updateInventory((byte) 0, items);
    }

    private void updateInventory(final byte windowId, final Item[] items) {
        final PacketWrapper windowItems = PacketWrapper.create(ClientboundPacketsb1_1.CONTAINER_SET_CONTENT, this.user());
        windowItems.write(Types.BYTE, windowId); // window id
        windowItems.write(Types1_4_2.NBTLESS_ITEM_ARRAY, copyItems(items)); // items
        windowItems.send(Protocola1_2_3_5_1_2_6Tob1_0_1_1_1.class);
    }

    private Item splitStack(final Item item, final int size) {
        item.setAmount(item.amount() - size);
        final Item newItem = item.copy();
        newItem.setAmount(size);
        return newItem;
    }

    private void onCraftingResultPickup(final byte windowId, final Item[] slots) {
        for (int i = 1; i < 1 + (windowId == 0 ? 4 : 9); i++) {
            final Item item = slots[i];
            if (item == null) continue;
            item.setAmount(item.amount() - 1);
        }
    }

    private boolean isEmpty(final Item[] items) {
        for (Item item : items) {
            if (item != null && item.amount() != 0) return false;
        }
        return true;
    }

}
