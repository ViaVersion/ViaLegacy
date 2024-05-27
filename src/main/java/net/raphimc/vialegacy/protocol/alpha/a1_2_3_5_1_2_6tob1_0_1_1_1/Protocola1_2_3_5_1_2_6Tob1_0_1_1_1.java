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
package net.raphimc.vialegacy.protocol.alpha.a1_2_3_5_1_2_6tob1_0_1_1_1;

import com.viaversion.nbt.tag.CompoundTag;
import com.viaversion.nbt.tag.ListTag;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.BlockPosition;
import com.viaversion.viaversion.api.minecraft.item.DataItem;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.platform.providers.ViaProviders;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.api.type.Types;
import com.viaversion.viaversion.util.IdAndData;
import net.raphimc.vialegacy.ViaLegacy;
import net.raphimc.vialegacy.api.data.BlockList1_6;
import net.raphimc.vialegacy.api.data.ItemList1_6;
import net.raphimc.vialegacy.api.protocol.StatelessProtocol;
import net.raphimc.vialegacy.api.splitter.PreNettySplitter;
import net.raphimc.vialegacy.protocol.alpha.a1_2_3_5_1_2_6tob1_0_1_1_1.data.AlphaItems;
import net.raphimc.vialegacy.protocol.alpha.a1_2_3_5_1_2_6tob1_0_1_1_1.packet.ClientboundPacketsa1_2_6;
import net.raphimc.vialegacy.protocol.alpha.a1_2_3_5_1_2_6tob1_0_1_1_1.packet.ServerboundPacketsa1_2_6;
import net.raphimc.vialegacy.protocol.alpha.a1_2_3_5_1_2_6tob1_0_1_1_1.provider.AlphaInventoryProvider;
import net.raphimc.vialegacy.protocol.alpha.a1_2_3_5_1_2_6tob1_0_1_1_1.provider.TrackingAlphaInventoryProvider;
import net.raphimc.vialegacy.protocol.alpha.a1_2_3_5_1_2_6tob1_0_1_1_1.storage.AlphaInventoryTracker;
import net.raphimc.vialegacy.protocol.alpha.a1_2_3_5_1_2_6tob1_0_1_1_1.storage.InventoryStorage;
import net.raphimc.vialegacy.protocol.alpha.a1_2_3_5_1_2_6tob1_0_1_1_1.task.AlphaInventoryUpdateTask;
import net.raphimc.vialegacy.protocol.beta.b1_1_2tob1_2_0_2.packet.ClientboundPacketsb1_1;
import net.raphimc.vialegacy.protocol.beta.b1_1_2tob1_2_0_2.packet.ServerboundPacketsb1_1;
import net.raphimc.vialegacy.protocol.beta.b1_1_2tob1_2_0_2.types.Typesb1_1;
import net.raphimc.vialegacy.protocol.beta.b1_7_0_3tob1_8_0_1.types.Typesb1_7_0_3;
import net.raphimc.vialegacy.protocol.release.r1_1tor1_2_1_3.Protocolr1_1Tor1_2_1_3;
import net.raphimc.vialegacy.protocol.release.r1_2_1_3tor1_2_4_5.packet.ClientboundPackets1_2_1;
import net.raphimc.vialegacy.protocol.release.r1_2_4_5tor1_3_1_2.data.EntityList1_2_4;
import net.raphimc.vialegacy.protocol.release.r1_3_1_2tor1_4_2.types.Types1_3_1;
import net.raphimc.vialegacy.protocol.release.r1_4_2tor1_4_4_5.types.Types1_4_2;
import net.raphimc.vialegacy.protocol.release.r1_6_4tor1_7_2_5.storage.ChunkTracker;
import net.raphimc.vialegacy.protocol.release.r1_6_4tor1_7_2_5.storage.PlayerInfoStorage;
import net.raphimc.vialegacy.protocol.release.r1_7_6_10tor1_8.types.Types1_7_6;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class Protocola1_2_3_5_1_2_6Tob1_0_1_1_1 extends StatelessProtocol<ClientboundPacketsa1_2_6, ClientboundPacketsb1_1, ServerboundPacketsa1_2_6, ServerboundPacketsb1_1> {

    public Protocola1_2_3_5_1_2_6Tob1_0_1_1_1() {
        super(ClientboundPacketsa1_2_6.class, ClientboundPacketsb1_1.class, ServerboundPacketsa1_2_6.class, ServerboundPacketsb1_1.class);
    }

    @Override
    protected void registerPackets() {
        this.registerClientbound(ClientboundPacketsa1_2_6.PLAYER_INVENTORY, ClientboundPacketsb1_1.CONTAINER_SET_CONTENT, wrapper -> {
            final InventoryStorage inventoryStorage = wrapper.user().get(InventoryStorage.class);
            final AlphaInventoryTracker inventoryTracker = wrapper.user().get(AlphaInventoryTracker.class);
            final int type = wrapper.read(Types.INT); // type
            Item[] items = wrapper.read(Types1_4_2.NBTLESS_ITEM_ARRAY); // items

            final Item[] windowItems = new Item[45];
            System.arraycopy(inventoryStorage.mainInventory, 0, windowItems, 36, 9);
            System.arraycopy(inventoryStorage.mainInventory, 9, windowItems, 9, 36 - 9);
            System.arraycopy(inventoryStorage.craftingInventory, 0, windowItems, 1, 4);
            System.arraycopy(inventoryStorage.armorInventory, 0, windowItems, 5, 4);

            switch (type) {
                case -1: // main
                    inventoryStorage.mainInventory = items;
                    if (inventoryTracker != null) inventoryTracker.setMainInventory(copyItems(items));
                    System.arraycopy(items, 0, windowItems, 36, 9);
                    System.arraycopy(items, 9, windowItems, 9, 36 - 9);
                    break;
                case -2: // crafting
                    inventoryStorage.craftingInventory = items;
                    if (inventoryTracker != null) inventoryTracker.setCraftingInventory(copyItems(items));
                    System.arraycopy(items, 0, windowItems, 1, 4);
                    break;
                case -3: // armor
                    inventoryStorage.armorInventory = items;
                    if (inventoryTracker != null) inventoryTracker.setArmorInventory(copyItems(items));
                    System.arraycopy(reverseArray(items), 0, windowItems, 5, 4);
            }

            wrapper.write(Types.BYTE, (byte) 0); // window id
            wrapper.write(Types1_4_2.NBTLESS_ITEM_ARRAY, copyItems(windowItems)); // items
        });
        this.registerClientbound(ClientboundPacketsa1_2_6.SET_HEALTH, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.BYTE, Types.SHORT); // health
            }
        });
        this.registerClientbound(ClientboundPacketsa1_2_6.RESPAWN, wrapper -> {
            wrapper.user().get(InventoryStorage.class).resetPlayerInventory();

            final AlphaInventoryTracker inventoryTracker = wrapper.user().get(AlphaInventoryTracker.class);
            if (inventoryTracker != null) inventoryTracker.onRespawn();
        });
        this.registerClientbound(ClientboundPacketsa1_2_6.SET_CARRIED_ITEM, ClientboundPacketsb1_1.SET_EQUIPPED_ITEM, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // entity id
                create(Types.SHORT, (short) 0); // slot (hand)
                map(Types.SHORT); // item id
                handler(wrapper -> {
                    if (wrapper.get(Types.SHORT, 1) == 0) {
                        wrapper.set(Types.SHORT, 1, (short) -1);
                    }
                });
            }
        });
        this.registerClientbound(ClientboundPacketsa1_2_6.ADD_TO_INVENTORY, null, wrapper -> {
            wrapper.cancel();
            final Item item = wrapper.read(Types1_3_1.NBTLESS_ITEM); // item
            Via.getManager().getProviders().get(AlphaInventoryProvider.class).addToInventory(wrapper.user(), item);
        });
        this.registerClientbound(ClientboundPacketsa1_2_6.PRE_CHUNK, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // chunkX
                map(Types.INT); // chunkZ
                map(Types.UNSIGNED_BYTE); // mode
                handler(wrapper -> wrapper.user().get(InventoryStorage.class).unload(wrapper.get(Types.INT, 0), wrapper.get(Types.INT, 1)));
            }
        });
        this.registerClientbound(ClientboundPacketsa1_2_6.BLOCK_ENTITY_DATA, null, wrapper -> {
            wrapper.cancel();
            final InventoryStorage tracker = wrapper.user().get(InventoryStorage.class);
            final BlockPosition pos = wrapper.read(Types1_7_6.BLOCK_POSITION_SHORT); // position
            final CompoundTag tag = wrapper.read(Types1_7_6.NBT); // data

            if (tag.getInt("x") != pos.x() || tag.getInt("y") != pos.y() || tag.getInt("z") != pos.z()) {
                return;
            }

            final IdAndData block = wrapper.user().get(ChunkTracker.class).getBlockNotNull(pos);
            final String blockName = tag.getString("id", "");

            if (block.getId() == BlockList1_6.signPost.blockId() || block.getId() == BlockList1_6.signWall.blockId() || blockName.equals("Sign")) {
                final PacketWrapper updateSign = PacketWrapper.create(ClientboundPacketsb1_1.UPDATE_SIGN, wrapper.user());
                updateSign.write(Types1_7_6.BLOCK_POSITION_SHORT, pos); // position
                updateSign.write(Typesb1_7_0_3.STRING, tag.getString("Text1", "")); // line 1
                updateSign.write(Typesb1_7_0_3.STRING, tag.getString("Text2", "")); // line 2
                updateSign.write(Typesb1_7_0_3.STRING, tag.getString("Text3", "")); // line 3
                updateSign.write(Typesb1_7_0_3.STRING, tag.getString("Text4", "")); // line 4
                updateSign.send(Protocola1_2_3_5_1_2_6Tob1_0_1_1_1.class);
            } else if (block.getId() == BlockList1_6.mobSpawner.blockId() || blockName.equals("MobSpawner")) {
                if (wrapper.user().getProtocolInfo().getPipeline().contains(Protocolr1_1Tor1_2_1_3.class)) {
                    final PacketWrapper spawnerData = PacketWrapper.create(ClientboundPackets1_2_1.BLOCK_ENTITY_DATA, wrapper.user());
                    spawnerData.write(Types1_7_6.BLOCK_POSITION_SHORT, pos); // position
                    spawnerData.write(Types.BYTE, (byte) 1); // type
                    spawnerData.write(Types.INT, EntityList1_2_4.getEntityId(tag.getString("EntityId"))); // entity id
                    spawnerData.write(Types.INT, 0); // unused
                    spawnerData.write(Types.INT, 0); // unused
                    spawnerData.send(Protocolr1_1Tor1_2_1_3.class);
                }
            } else if (block.getId() == BlockList1_6.chest.blockId() || blockName.equals("Chest")) {
                final Item[] chestItems = new Item[3 * 9];
                readItemsFromTag(tag, chestItems);
                tracker.containers.put(pos, chestItems);
                if (pos.equals(tracker.openContainerPos)) sendWindowItems(wrapper.user(), InventoryStorage.CHEST_WID, chestItems);
            } else if (block.getId() == BlockList1_6.furnaceIdle.blockId() || block.getId() == BlockList1_6.furnaceBurning.blockId() || blockName.equals("Furnace")) {
                final Item[] furnaceItems = new Item[3];
                readItemsFromTag(tag, furnaceItems);
                tracker.containers.put(pos, furnaceItems);
                if (pos.equals(tracker.openContainerPos)) {
                    sendWindowItems(wrapper.user(), InventoryStorage.FURNACE_WID, furnaceItems);
                    sendProgressUpdate(wrapper.user(), InventoryStorage.FURNACE_WID, (short) 0, tag.getShort("CookTime")); // cook time
                    sendProgressUpdate(wrapper.user(), InventoryStorage.FURNACE_WID, (short) 1, tag.getShort("BurnTime")); // furnace burn time
                    sendProgressUpdate(wrapper.user(), InventoryStorage.FURNACE_WID, (short) 2, getBurningTime(furnaceItems[1])); // item burn time
                }
            } else {
                ViaLegacy.getPlatform().getLogger().warning("Unhandled Complex Entity data: " + block + "@" + pos + ": '" + tag + "'");
            }
        });

        this.registerServerbound(ServerboundPacketsb1_1.PLAYER_ACTION, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.UNSIGNED_BYTE); // status
                map(Types1_7_6.BLOCK_POSITION_UBYTE); // position
                map(Types.UNSIGNED_BYTE); // direction
                handler(wrapper -> {
                    final short status = wrapper.get(Types.UNSIGNED_BYTE, 0);
                    if (status == 4) {
                        wrapper.cancel();

                        final Item selectedItem = fixItem(Via.getManager().getProviders().get(AlphaInventoryProvider.class).getHandItem(wrapper.user()));
                        if (selectedItem == null) {
                            return;
                        }

                        final AlphaInventoryTracker inventoryTracker = wrapper.user().get(AlphaInventoryTracker.class);
                        if (inventoryTracker != null) inventoryTracker.onHandItemDrop();

                        selectedItem.setAmount(1);
                        dropItem(wrapper.user(), selectedItem, false);
                    }
                });
            }
        });
        this.registerServerbound(ServerboundPacketsb1_1.USE_ITEM_ON, wrapper -> {
            final InventoryStorage tracker = wrapper.user().get(InventoryStorage.class);
            final AlphaInventoryTracker inventoryTracker = wrapper.user().get(AlphaInventoryTracker.class);
            final BlockPosition pos = wrapper.read(Types1_7_6.BLOCK_POSITION_UBYTE); // position
            final short direction = wrapper.read(Types.UNSIGNED_BYTE); // direction
            Item item = fixItem(wrapper.read(Typesb1_1.NBTLESS_ITEM)); // item

            if (item == null && inventoryTracker != null) {
                item = Via.getManager().getProviders().get(AlphaInventoryProvider.class).getHandItem(wrapper.user());
            }

            wrapper.write(Types.SHORT, item == null ? (short) -1 : (short) item.identifier()); // item id
            wrapper.write(Types1_7_6.BLOCK_POSITION_UBYTE, pos);
            wrapper.write(Types.UNSIGNED_BYTE, direction);

            if (inventoryTracker != null) inventoryTracker.onBlockPlace(pos, direction);

            if (direction == 255) return;

            final IdAndData block = wrapper.user().get(ChunkTracker.class).getBlockNotNull(pos);
            if (block.getId() != BlockList1_6.furnaceIdle.blockId() && block.getId() != BlockList1_6.furnaceBurning.blockId() && block.getId() != BlockList1_6.chest.blockId() && block.getId() != BlockList1_6.workbench.blockId()) {
                return;
            }

            final Item[] containerItems = tracker.containers.get(tracker.openContainerPos = pos);
            if (containerItems == null && block.getId() != BlockList1_6.workbench.blockId()) {
                tracker.openContainerPos = null;
                final PacketWrapper chatMessage = PacketWrapper.create(ClientboundPacketsb1_1.CHAT, wrapper.user());
                chatMessage.write(Typesb1_7_0_3.STRING, "§cMissing Container"); // message
                chatMessage.send(Protocola1_2_3_5_1_2_6Tob1_0_1_1_1.class);
                return;
            }

            final PacketWrapper openWindow = PacketWrapper.create(ClientboundPacketsb1_1.OPEN_SCREEN, wrapper.user());
            if (block.getId() == BlockList1_6.chest.blockId()) {
                openWindow.write(Types.UNSIGNED_BYTE, (short) InventoryStorage.CHEST_WID); // window id
                openWindow.write(Types.UNSIGNED_BYTE, (short) 0); // window type
                openWindow.write(Typesb1_7_0_3.STRING, "Chest"); // title
                openWindow.write(Types.UNSIGNED_BYTE, (short) (3 * 9)); // slots
                if (inventoryTracker != null) inventoryTracker.onWindowOpen(0, 3 * 9);
            } else if (block.getId() == BlockList1_6.workbench.blockId()) {
                openWindow.write(Types.UNSIGNED_BYTE, (short) InventoryStorage.WORKBENCH_WID); // window id
                openWindow.write(Types.UNSIGNED_BYTE, (short) 1); // window type
                openWindow.write(Typesb1_7_0_3.STRING, "Crafting Table"); // title
                openWindow.write(Types.UNSIGNED_BYTE, (short) 9); // slots
                if (inventoryTracker != null) inventoryTracker.onWindowOpen(1, 10);
            } else { // furnace
                openWindow.write(Types.UNSIGNED_BYTE, (short) InventoryStorage.FURNACE_WID); // window id
                openWindow.write(Types.UNSIGNED_BYTE, (short) 2); // window type
                openWindow.write(Typesb1_7_0_3.STRING, "Furnace"); // title
                openWindow.write(Types.UNSIGNED_BYTE, (short) 3); // slots
                if (inventoryTracker != null) inventoryTracker.onWindowOpen(2, 3);
            }
            openWindow.send(Protocola1_2_3_5_1_2_6Tob1_0_1_1_1.class);

            if (block.getId() != BlockList1_6.workbench.blockId()) {
                sendWindowItems(wrapper.user(), block.getId() == BlockList1_6.chest.blockId() ? InventoryStorage.CHEST_WID : InventoryStorage.FURNACE_WID, containerItems);
            }
        });
        this.registerServerbound(ServerboundPacketsb1_1.SET_CARRIED_ITEM, wrapper -> {
            final InventoryStorage inventoryStorage = wrapper.user().get(InventoryStorage.class);
            short slot = wrapper.read(Types.SHORT); // slot
            if (slot < 0 || slot > 8) slot = 0;
            inventoryStorage.selectedHotbarSlot = slot;
            final Item selectedItem = fixItem(Via.getManager().getProviders().get(AlphaInventoryProvider.class).getHandItem(wrapper.user()));
            if (Objects.equals(selectedItem, inventoryStorage.handItem)) {
                wrapper.cancel();
                return;
            }
            inventoryStorage.handItem = selectedItem;

            wrapper.write(Types.INT, 0); // entity id (always 0)
            wrapper.write(Types.SHORT, (short) (selectedItem == null ? 0 : selectedItem.identifier())); // item id
        });
        this.registerServerbound(ServerboundPacketsb1_1.CONTAINER_CLOSE, null, wrapper -> {
            wrapper.cancel();
            wrapper.user().get(InventoryStorage.class).openContainerPos = null;

            final AlphaInventoryTracker inventoryTracker = wrapper.user().get(AlphaInventoryTracker.class);
            if (inventoryTracker != null) inventoryTracker.onWindowClose();
        });
        this.registerServerbound(ServerboundPacketsb1_1.CONTAINER_CLICK, ServerboundPacketsa1_2_6.BLOCK_ENTITY_DATA, wrapper -> {
            final InventoryStorage tracker = wrapper.user().get(InventoryStorage.class);
            final AlphaInventoryTracker inventoryTracker = wrapper.user().get(AlphaInventoryTracker.class);
            final byte windowId = wrapper.read(Types.BYTE); // window id
            final short slot = wrapper.read(Types.SHORT); // slot
            final byte button = wrapper.read(Types.BYTE); // button
            final short action = wrapper.read(Types.SHORT); // action
            final Item item = fixItem(wrapper.read(Typesb1_1.NBTLESS_ITEM)); // item

            if (inventoryTracker != null) inventoryTracker.onWindowClick(windowId, slot, button, action, item);
            if ((windowId != InventoryStorage.CHEST_WID && windowId != InventoryStorage.FURNACE_WID) || tracker.openContainerPos == null) {
                wrapper.cancel();
                return;
            }

            final Item[] containerItems = fixItems(Via.getManager().getProviders().get(AlphaInventoryProvider.class).getContainerItems(wrapper.user()));
            if (Arrays.equals(tracker.containers.get(tracker.openContainerPos), containerItems)) {
                wrapper.cancel();
                return;
            }
            tracker.containers.put(tracker.openContainerPos, containerItems);

            final CompoundTag tag = new CompoundTag();
            tag.putString("id", windowId == InventoryStorage.CHEST_WID ? "Chest" : "Furnace");
            tag.putInt("x", tracker.openContainerPos.x());
            tag.putInt("y", tracker.openContainerPos.y());
            tag.putInt("z", tracker.openContainerPos.z());
            writeItemsToTag(tag, containerItems);

            wrapper.write(Types.INT, tracker.openContainerPos.x());
            wrapper.write(Types.SHORT, (short) tracker.openContainerPos.y());
            wrapper.write(Types.INT, tracker.openContainerPos.z());
            wrapper.write(Types1_7_6.NBT, tag);
        });
        this.registerServerbound(ServerboundPacketsb1_1.SIGN_UPDATE, ServerboundPacketsa1_2_6.BLOCK_ENTITY_DATA, wrapper -> {
            final BlockPosition pos = wrapper.passthrough(Types1_7_6.BLOCK_POSITION_SHORT); // position

            final CompoundTag tag = new CompoundTag();
            tag.putString("id", "Sign");
            tag.putInt("x", pos.x());
            tag.putInt("y", pos.y());
            tag.putInt("z", pos.z());
            tag.putString("Text1", wrapper.read(Typesb1_7_0_3.STRING)); // line 1
            tag.putString("Text2", wrapper.read(Typesb1_7_0_3.STRING)); // line 2
            tag.putString("Text3", wrapper.read(Typesb1_7_0_3.STRING)); // line 3
            tag.putString("Text4", wrapper.read(Typesb1_7_0_3.STRING)); // line 4
            wrapper.write(Types1_7_6.NBT, tag); // data
        });
        this.cancelServerbound(ServerboundPacketsb1_1.CONTAINER_ACK);
    }

    private void writeItemsToTag(final CompoundTag tag, final Item[] items) {
        final ListTag<CompoundTag> itemList = new ListTag<>(CompoundTag.class);
        for (int i = 0; i < items.length; i++) {
            final Item item = items[i];
            if (item == null) continue;
            final CompoundTag itemTag = new CompoundTag();
            itemTag.putByte("Slot", (byte) i);
            itemTag.putShort("id", (short) item.identifier());
            itemTag.putByte("Count", (byte) item.amount());
            itemTag.putShort("Damage", item.data());
            itemList.add(itemTag);
        }
        tag.put("Items", itemList);
    }

    private void readItemsFromTag(final CompoundTag tag, final Item[] items) {
        final ListTag<CompoundTag> itemList = tag.getListTag("Items", CompoundTag.class);
        for (CompoundTag itemTag : itemList) {
            items[itemTag.getByte("Slot") & 255] = new DataItem(itemTag.getShort("id"), itemTag.getByte("Count"), itemTag.getShort("Damage"), null);
        }
    }

    private void sendWindowItems(final UserConnection user, final byte windowId, final Item[] items) {
        final PacketWrapper windowItems = PacketWrapper.create(ClientboundPacketsb1_1.CONTAINER_SET_CONTENT, user);
        windowItems.write(Types.BYTE, windowId); // window id
        windowItems.write(Types1_4_2.NBTLESS_ITEM_ARRAY, copyItems(items)); // items
        windowItems.send(Protocola1_2_3_5_1_2_6Tob1_0_1_1_1.class);

        final AlphaInventoryTracker inventoryTracker = user.get(AlphaInventoryTracker.class);
        if (inventoryTracker != null) inventoryTracker.setOpenContainerItems(copyItems(items));
    }

    private void sendProgressUpdate(final UserConnection user, final short windowId, final short id, final short value) {
        final PacketWrapper windowProperty = PacketWrapper.create(ClientboundPacketsb1_1.CONTAINER_SET_DATA, user);
        windowProperty.write(Types.UNSIGNED_BYTE, windowId); // window id
        windowProperty.write(Types.SHORT, id); // progress bar id
        windowProperty.write(Types.SHORT, value); // progress bar value
        windowProperty.send(Protocola1_2_3_5_1_2_6Tob1_0_1_1_1.class);
    }

    private short getBurningTime(final Item item) {
        if (item == null) return 0;

        final int id = item.identifier();
        if (id == BlockList1_6.bookShelf.blockId() || id == BlockList1_6.chest.blockId() || id == BlockList1_6.fence.blockId() || id == BlockList1_6.jukebox.blockId() || id == BlockList1_6.wood.blockId() || id == BlockList1_6.planks.blockId() || id == BlockList1_6.doorWood.blockId() || id == BlockList1_6.signWall.blockId() || id == BlockList1_6.signPost.blockId() || id == BlockList1_6.workbench.blockId()) {
            return 300;
        } else if (id == ItemList1_6.stick.itemId()) {
            return 100;
        } else if (id == ItemList1_6.coal.itemId()) {
            return 1600;
        } else if (id == ItemList1_6.bucketLava.itemId()) {
            return 20000;
        }
        return 0;
    }

    public static void dropItem(final UserConnection user, final Item item, final boolean flag) {
        final PlayerInfoStorage playerInfoStorage = user.get(PlayerInfoStorage.class);
        final double itemX = playerInfoStorage.posX;
        final double itemY = playerInfoStorage.posY + 1.62F - 0.30000001192092896D + 0.12D;
        final double itemZ = playerInfoStorage.posZ;
        double motionX;
        double motionY;
        double motionZ;
        if (flag) {
            final float f2 = ThreadLocalRandom.current().nextFloat() * 0.5F;
            final float f1 = (float) (ThreadLocalRandom.current().nextFloat() * Math.PI * 2.0F);
            motionX = -Math.sin(f1) * f2;
            motionZ = Math.cos(f1) * f2;
            motionY = 0.20000000298023224D;
        } else {
            motionX = -Math.sin((playerInfoStorage.yaw / 180F) * Math.PI) * Math.cos((playerInfoStorage.pitch / 180F) * Math.PI) * 0.3F;
            motionZ = Math.cos((playerInfoStorage.yaw / 180F) * Math.PI) * Math.cos((playerInfoStorage.pitch / 180F) * Math.PI) * 0.3F;
            motionY = -Math.sin((playerInfoStorage.pitch / 180F) * Math.PI) * 0.3F + 0.1F;
            final float f1 = (float) (ThreadLocalRandom.current().nextFloat() * Math.PI * 2.0F);
            final float f2 = 0.02F * ThreadLocalRandom.current().nextFloat();
            motionX += Math.cos(f1) * (double) f2;
            motionY += (ThreadLocalRandom.current().nextFloat() - ThreadLocalRandom.current().nextFloat()) * 0.1F;
            motionZ += Math.sin(f1) * (double) f2;
        }

        final PacketWrapper spawnItem = PacketWrapper.create(ServerboundPacketsa1_2_6.SPAWN_ITEM, user);
        spawnItem.write(Types.INT, 0); // entity id
        spawnItem.write(Types.SHORT, (short) item.identifier()); // item id
        spawnItem.write(Types.BYTE, (byte) item.amount()); // item count
        spawnItem.write(Types.INT, (int) (itemX * 32)); // x
        spawnItem.write(Types.INT, (int) (itemY * 32)); // y
        spawnItem.write(Types.INT, (int) (itemZ * 32)); // z
        spawnItem.write(Types.BYTE, (byte) (motionX * 128)); // velocity x
        spawnItem.write(Types.BYTE, (byte) (motionY * 128)); // velocity y
        spawnItem.write(Types.BYTE, (byte) (motionZ * 128)); // velocity z
        spawnItem.sendToServer(Protocola1_2_3_5_1_2_6Tob1_0_1_1_1.class);
    }

    public static Item[] reverseArray(final Item[] array) {
        if (array == null) return null;
        final Item[] reversed = new Item[array.length];

        for (int i = 0; i < array.length / 2; i++) {
            reversed[i] = array[array.length - i - 1];
            reversed[array.length - i - 1] = array[i];
        }

        return reversed;
    }

    public static Item copyItem(final Item item) {
        return item == null ? null : item.copy();
    }

    public static Item[] copyItems(final Item[] items) {
        return Arrays.stream(items).map(Protocola1_2_3_5_1_2_6Tob1_0_1_1_1::copyItem).toArray(Item[]::new);
    }

    public static Item fixItem(final Item item) {
        if (item == null || !AlphaItems.isValid(item.identifier())) return null;
        item.setTag(null);
        return item;
    }

    public static Item[] fixItems(final Item[] items) {
        for (int i = 0; i < items.length; i++) {
            items[i] = fixItem(items[i]);
        }
        return items;
    }

    @Override
    public void register(ViaProviders providers) {
        providers.register(AlphaInventoryProvider.class, new TrackingAlphaInventoryProvider());

        Via.getPlatform().runRepeatingSync(new AlphaInventoryUpdateTask(), 20L);
    }

    @Override
    public void init(UserConnection userConnection) {
        userConnection.put(new PreNettySplitter(Protocola1_2_3_5_1_2_6Tob1_0_1_1_1.class, ClientboundPacketsa1_2_6::getPacket));

        userConnection.put(new InventoryStorage());
        if (Via.getManager().getProviders().get(AlphaInventoryProvider.class).usesInventoryTracker()) {
            userConnection.put(new AlphaInventoryTracker(userConnection));
        }
    }

}
