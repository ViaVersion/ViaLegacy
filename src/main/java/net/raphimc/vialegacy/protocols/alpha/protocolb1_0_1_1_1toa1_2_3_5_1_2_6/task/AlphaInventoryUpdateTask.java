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
package net.raphimc.vialegacy.protocols.alpha.protocolb1_0_1_1_1toa1_2_3_5_1_2_6.task;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.type.Type;
import net.raphimc.vialegacy.ViaLegacy;
import net.raphimc.vialegacy.protocols.alpha.protocolb1_0_1_1_1toa1_2_3_5_1_2_6.Protocolb1_0_1_1_1toa1_2_3_5_1_2_6;
import net.raphimc.vialegacy.protocols.alpha.protocolb1_0_1_1_1toa1_2_3_5_1_2_6.ServerboundPacketsa1_2_6;
import net.raphimc.vialegacy.protocols.alpha.protocolb1_0_1_1_1toa1_2_3_5_1_2_6.providers.AlphaInventoryProvider;
import net.raphimc.vialegacy.protocols.alpha.protocolb1_0_1_1_1toa1_2_3_5_1_2_6.storage.InventoryStorage;
import net.raphimc.vialegacy.protocols.beta.protocolb1_2_0_2tob1_1_2.ServerboundPacketsb1_1;
import net.raphimc.vialegacy.protocols.release.protocol1_4_4_5to1_4_2.types.Types1_4_2;

import java.util.Arrays;
import java.util.Objects;
import java.util.logging.Level;

import static net.raphimc.vialegacy.protocols.alpha.protocolb1_0_1_1_1toa1_2_3_5_1_2_6.Protocolb1_0_1_1_1toa1_2_3_5_1_2_6.*;

public class AlphaInventoryUpdateTask implements Runnable {

    @Override
    public void run() {
        for (UserConnection info : Via.getManager().getConnectionManager().getConnections()) {
            final InventoryStorage inventoryStorage = info.get(InventoryStorage.class);
            if (inventoryStorage == null) continue;

            info.getChannel().eventLoop().submit(() -> {
                try {
                    final Item[] mainInventory = fixItems(Via.getManager().getProviders().get(AlphaInventoryProvider.class).getMainInventoryItems(info));
                    final Item[] craftingInventory = fixItems(Via.getManager().getProviders().get(AlphaInventoryProvider.class).getCraftingInventoryItems(info));
                    final Item[] armorInventory = fixItems(Via.getManager().getProviders().get(AlphaInventoryProvider.class).getArmorInventoryItems(info));
                    final Item handItem = fixItem(Via.getManager().getProviders().get(AlphaInventoryProvider.class).getHandItem(info));

                    if (!Objects.equals(handItem, inventoryStorage.handItem)) {
                        final PacketWrapper heldItemChange = PacketWrapper.create(ServerboundPacketsb1_1.HELD_ITEM_CHANGE, info);
                        heldItemChange.write(Type.SHORT, inventoryStorage.selectedHotbarSlot); // slot
                        heldItemChange.sendToServer(Protocolb1_0_1_1_1toa1_2_3_5_1_2_6.class, false);
                    }

                    final Item[] mergedMainInventory = copyItems(inventoryStorage.mainInventory);
                    final Item[] mergedCraftingInventory = copyItems(inventoryStorage.craftingInventory);
                    final Item[] mergedArmorInventory = copyItems(inventoryStorage.armorInventory);
                    System.arraycopy(mainInventory, 0, mergedMainInventory, 0, mainInventory.length);
                    System.arraycopy(craftingInventory, 0, mergedCraftingInventory, 0, craftingInventory.length);
                    System.arraycopy(armorInventory, 0, mergedArmorInventory, 0, armorInventory.length);

                    boolean hasChanged = !Arrays.equals(mergedMainInventory, inventoryStorage.mainInventory) || !Arrays.equals(mergedCraftingInventory, inventoryStorage.craftingInventory) || !Arrays.equals(mergedArmorInventory, inventoryStorage.armorInventory);
                    if (!hasChanged) return;

                    inventoryStorage.mainInventory = copyItems(mergedMainInventory);
                    inventoryStorage.craftingInventory = copyItems(mergedCraftingInventory);
                    inventoryStorage.armorInventory = copyItems(mergedArmorInventory);

                    final PacketWrapper mainContent = PacketWrapper.create(ServerboundPacketsa1_2_6.PLAYER_INVENTORY, info);
                    mainContent.write(Type.INT, -1); // type
                    mainContent.write(Types1_4_2.NBTLESS_ITEM_ARRAY, mergedMainInventory); // items

                    final PacketWrapper craftingContent = PacketWrapper.create(ServerboundPacketsa1_2_6.PLAYER_INVENTORY, info);
                    craftingContent.write(Type.INT, -2); // type
                    craftingContent.write(Types1_4_2.NBTLESS_ITEM_ARRAY, mergedCraftingInventory); // items

                    final PacketWrapper armorContent = PacketWrapper.create(ServerboundPacketsa1_2_6.PLAYER_INVENTORY, info);
                    armorContent.write(Type.INT, -3); // type
                    armorContent.write(Types1_4_2.NBTLESS_ITEM_ARRAY, mergedArmorInventory); // items

                    mainContent.sendToServer(Protocolb1_0_1_1_1toa1_2_3_5_1_2_6.class);
                    craftingContent.sendToServer(Protocolb1_0_1_1_1toa1_2_3_5_1_2_6.class);
                    armorContent.sendToServer(Protocolb1_0_1_1_1toa1_2_3_5_1_2_6.class);
                } catch (Throwable e) {
                    ViaLegacy.getPlatform().getLogger().log(Level.WARNING, "Error sending inventory update packets", e);
                }
            });
        }
    }

}
