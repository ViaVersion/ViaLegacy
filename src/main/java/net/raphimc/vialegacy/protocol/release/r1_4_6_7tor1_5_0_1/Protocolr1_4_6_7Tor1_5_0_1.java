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
package net.raphimc.vialegacy.protocol.release.r1_4_6_7tor1_5_0_1;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.entities.EntityTypes1_8;
import com.viaversion.viaversion.api.minecraft.item.DataItem;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.api.type.Types;
import net.raphimc.vialegacy.api.protocol.StatelessProtocol;
import net.raphimc.vialegacy.api.splitter.PreNettySplitter;
import net.raphimc.vialegacy.protocol.release.r1_4_6_7tor1_5_0_1.packet.ClientboundPackets1_4_6;
import net.raphimc.vialegacy.protocol.release.r1_4_6_7tor1_5_0_1.rewriter.ItemRewriter;
import net.raphimc.vialegacy.protocol.release.r1_5_2tor1_6_1.packet.ClientboundPackets1_5_2;
import net.raphimc.vialegacy.protocol.release.r1_5_2tor1_6_1.packet.ServerboundPackets1_5_2;
import net.raphimc.vialegacy.protocol.release.r1_6_4tor1_7_2_5.types.Types1_6_4;
import net.raphimc.vialegacy.protocol.release.r1_7_6_10tor1_8.types.Types1_7_6;

public class Protocolr1_4_6_7Tor1_5_0_1 extends StatelessProtocol<ClientboundPackets1_4_6, ClientboundPackets1_5_2, ServerboundPackets1_5_2, ServerboundPackets1_5_2> {

    private final ItemRewriter itemRewriter = new ItemRewriter(this);

    public Protocolr1_4_6_7Tor1_5_0_1() {
        super(ClientboundPackets1_4_6.class, ClientboundPackets1_5_2.class, ServerboundPackets1_5_2.class, ServerboundPackets1_5_2.class);
    }

    @Override
    protected void registerPackets() {
        super.registerPackets();

        this.registerClientbound(ClientboundPackets1_4_6.ADD_ENTITY, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // entity id
                map(Types.BYTE); // type id
                map(Types.INT); // x
                map(Types.INT); // y
                map(Types.INT); // z
                map(Types.BYTE); // yaw
                map(Types.BYTE); // pitch
                map(Types.INT); // data
                handler(wrapper -> {
                    final byte typeId = wrapper.get(Types.BYTE, 0);
                    if (typeId == 10 || typeId == 11 || typeId == 12) {
                        wrapper.set(Types.BYTE, 0, (byte) EntityTypes1_8.ObjectType.MINECART.getId());
                    }
                    int throwerEntityId = wrapper.get(Types.INT, 4);
                    short speedX = 0;
                    short speedY = 0;
                    short speedZ = 0;
                    if (throwerEntityId > 0) {
                        speedX = wrapper.read(Types.SHORT); // velocity x
                        speedY = wrapper.read(Types.SHORT); // velocity y
                        speedZ = wrapper.read(Types.SHORT); // velocity z
                    }
                    if (typeId == 10) throwerEntityId = 0; // normal minecart
                    if (typeId == 11) throwerEntityId = 1; // chest minecart
                    if (typeId == 12) throwerEntityId = 2; // oven minecart
                    wrapper.set(Types.INT, 4, throwerEntityId);
                    if (throwerEntityId > 0) {
                        wrapper.write(Types.SHORT, speedX);
                        wrapper.write(Types.SHORT, speedY);
                        wrapper.write(Types.SHORT, speedZ);
                    }
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_4_6.OPEN_SCREEN, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.UNSIGNED_BYTE); // window id
                map(Types.UNSIGNED_BYTE); // window type
                map(Types1_6_4.STRING); // title
                map(Types.UNSIGNED_BYTE); // slots
                create(Types.BOOLEAN, false); // use provided title
            }
        });

        this.registerServerbound(ServerboundPackets1_5_2.CONTAINER_CLICK, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.BYTE); // window id
                map(Types.SHORT); // slot
                map(Types.BYTE); // button
                map(Types.SHORT); // action
                map(Types.BYTE); // mode
                map(Types1_7_6.ITEM); // item
                handler(wrapper -> {
                    final short slot = wrapper.get(Types.SHORT, 0);
                    final byte button = wrapper.get(Types.BYTE, 1);
                    final byte mode = wrapper.get(Types.BYTE, 2);

                    if (mode > 3) {
                        boolean startDragging = false;
                        boolean endDragging = false;
                        boolean droppingUsingQ = false;
                        boolean addSlot = false;

                        switch (mode) {
                            case 4 -> droppingUsingQ = button + (slot != -999 ? 2 : 0) == 2;
                            case 5 -> {
                                startDragging = button == 0;
                                endDragging = button == 2;
                                addSlot = button == 1;
                            }
                        }

                        final boolean leftClick = startDragging || addSlot || endDragging;
                        final boolean clickingOutside = slot == -999 && mode != 5;
                        final int mouseClick = leftClick ? 0 : 1;

                        if (droppingUsingQ) {
                            final PacketWrapper closeWindow = PacketWrapper.create(ClientboundPackets1_5_2.CONTAINER_CLOSE, wrapper.user());
                            closeWindow.write(Types.BYTE, (byte) 0); // window id
                            closeWindow.send(Protocolr1_4_6_7Tor1_5_0_1.class);
                            wrapper.cancel();
                            return;
                        }
                        if (slot < 0 && !clickingOutside) {
                            wrapper.cancel();
                            return;
                        }

                        wrapper.set(Types.BYTE, 1, (byte) mouseClick);
                        wrapper.set(Types.BYTE, 2, (byte) 0);
                        wrapper.set(Types1_7_6.ITEM, 0, new DataItem(34, (byte) 0, (short) 0, null));
                    }
                });
            }
        });
    }

    @Override
    public void init(UserConnection userConnection) {
        userConnection.put(new PreNettySplitter(Protocolr1_4_6_7Tor1_5_0_1.class, ClientboundPackets1_4_6::getPacket));
    }

    @Override
    public ItemRewriter getItemRewriter() {
        return this.itemRewriter;
    }

}
