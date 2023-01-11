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
package net.raphimc.vialegacy.protocols.snapshot.protocol1_14to3D_Shareware.packets;

import com.viaversion.viabackwards.api.rewriters.ItemRewriter;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.ServerboundPackets1_14;
import net.raphimc.vialegacy.protocols.snapshot.protocol1_14to3D_Shareware.ClientboundPackets3D_Shareware;
import net.raphimc.vialegacy.protocols.snapshot.protocol1_14to3D_Shareware.Protocol1_14to3D_Shareware;

public class BlockItemPackets3D_Shareware extends ItemRewriter<Protocol1_14to3D_Shareware> {

    public BlockItemPackets3D_Shareware(Protocol1_14to3D_Shareware protocol) {
        super(protocol);
    }

    @Override
    protected void registerPackets() {
        this.registerSetCooldown(ClientboundPackets3D_Shareware.COOLDOWN);
        this.registerWindowItems(ClientboundPackets3D_Shareware.WINDOW_ITEMS, Type.FLAT_VAR_INT_ITEM_ARRAY);
        this.registerSetSlot(ClientboundPackets3D_Shareware.SET_SLOT, Type.FLAT_VAR_INT_ITEM);
        this.registerEntityEquipment(ClientboundPackets3D_Shareware.ENTITY_EQUIPMENT, Type.FLAT_VAR_INT_ITEM);
        this.registerAdvancements(ClientboundPackets3D_Shareware.ADVANCEMENTS, Type.FLAT_VAR_INT_ITEM);
        this.registerClickWindow(ServerboundPackets1_14.CLICK_WINDOW, Type.FLAT_VAR_INT_ITEM);
        this.registerCreativeInvAction(ServerboundPackets1_14.CREATIVE_INVENTORY_ACTION, Type.FLAT_VAR_INT_ITEM);
        this.registerSpawnParticle(ClientboundPackets3D_Shareware.SPAWN_PARTICLE, Type.FLAT_VAR_INT_ITEM, Type.FLOAT);

        this.protocol.registerClientbound(ClientboundPackets3D_Shareware.TRADE_LIST, new PacketRemapper() {
            public void registerMap() {
                this.handler((wrapper) -> {
                    wrapper.passthrough(Type.VAR_INT);
                    int size = wrapper.passthrough(Type.UNSIGNED_BYTE);

                    for (int i = 0; i < size; ++i) {
                        BlockItemPackets3D_Shareware.this.handleItemToClient(wrapper.passthrough(Type.FLAT_VAR_INT_ITEM));
                        BlockItemPackets3D_Shareware.this.handleItemToClient(wrapper.passthrough(Type.FLAT_VAR_INT_ITEM));
                        if (wrapper.passthrough(Type.BOOLEAN)) {
                            BlockItemPackets3D_Shareware.this.handleItemToClient(wrapper.passthrough(Type.FLAT_VAR_INT_ITEM));
                        }

                        wrapper.passthrough(Type.BOOLEAN);
                        wrapper.passthrough(Type.INT);
                        wrapper.passthrough(Type.INT);
                        wrapper.passthrough(Type.INT);
                        wrapper.passthrough(Type.INT);
                        wrapper.passthrough(Type.FLOAT);
                    }

                });
            }
        });
    }

}
