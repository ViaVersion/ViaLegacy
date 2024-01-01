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
package net.raphimc.vialegacy.protocols.release.protocol1_5_0_1to1_4_6_7;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.entities.EntityTypes1_10;
import com.viaversion.viaversion.api.minecraft.item.DataItem;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.api.type.Type;
import net.raphimc.vialegacy.api.protocol.StatelessProtocol;
import net.raphimc.vialegacy.api.remapper.LegacyItemRewriter;
import net.raphimc.vialegacy.api.splitter.PreNettySplitter;
import net.raphimc.vialegacy.protocols.release.protocol1_5_0_1to1_4_6_7.rewriter.ItemRewriter;
import net.raphimc.vialegacy.protocols.release.protocol1_6_1to1_5_2.ClientboundPackets1_5_2;
import net.raphimc.vialegacy.protocols.release.protocol1_6_1to1_5_2.ServerboundPackets1_5_2;
import net.raphimc.vialegacy.protocols.release.protocol1_7_2_5to1_6_4.types.Types1_6_4;
import net.raphimc.vialegacy.protocols.release.protocol1_8to1_7_6_10.types.Types1_7_6;

public class Protocol1_5_0_1to1_4_6_7 extends StatelessProtocol<ClientboundPackets1_4_6, ClientboundPackets1_5_2, ServerboundPackets1_5_2, ServerboundPackets1_5_2> {

    private final LegacyItemRewriter<Protocol1_5_0_1to1_4_6_7> itemRewriter = new ItemRewriter(this);

    public Protocol1_5_0_1to1_4_6_7() {
        super(ClientboundPackets1_4_6.class, ClientboundPackets1_5_2.class, ServerboundPackets1_5_2.class, ServerboundPackets1_5_2.class);
    }

    @Override
    protected void registerPackets() {
        this.itemRewriter.register();

        this.registerClientbound(ClientboundPackets1_4_6.SPAWN_ENTITY, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.INT); // entity id
                map(Type.BYTE); // type id
                map(Type.INT); // x
                map(Type.INT); // y
                map(Type.INT); // z
                map(Type.BYTE); // yaw
                map(Type.BYTE); // pitch
                map(Type.INT); // data
                handler(wrapper -> {
                    final byte typeId = wrapper.get(Type.BYTE, 0);
                    if (typeId == 10 || typeId == 11 || typeId == 12) {
                        wrapper.set(Type.BYTE, 0, (byte) EntityTypes1_10.ObjectType.MINECART.getId());
                    }
                    int throwerEntityId = wrapper.get(Type.INT, 4);
                    short speedX = 0;
                    short speedY = 0;
                    short speedZ = 0;
                    if (throwerEntityId > 0) {
                        speedX = wrapper.read(Type.SHORT); // velocity x
                        speedY = wrapper.read(Type.SHORT); // velocity y
                        speedZ = wrapper.read(Type.SHORT); // velocity z
                    }
                    if (typeId == 10) throwerEntityId = 0; // normal minecart
                    if (typeId == 11) throwerEntityId = 1; // chest minecart
                    if (typeId == 12) throwerEntityId = 2; // oven minecart
                    wrapper.set(Type.INT, 4, throwerEntityId);
                    if (throwerEntityId > 0) {
                        wrapper.write(Type.SHORT, speedX);
                        wrapper.write(Type.SHORT, speedY);
                        wrapper.write(Type.SHORT, speedZ);
                    }
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_4_6.OPEN_WINDOW, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.UNSIGNED_BYTE); // window id
                map(Type.UNSIGNED_BYTE); // window type
                map(Types1_6_4.STRING); // title
                map(Type.UNSIGNED_BYTE); // slots
                create(Type.BOOLEAN, false); // use provided title
            }
        });

        this.registerServerbound(ServerboundPackets1_5_2.CLICK_WINDOW, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.BYTE); // window id
                map(Type.SHORT); // slot
                map(Type.BYTE); // button
                map(Type.SHORT); // action
                map(Type.BYTE); // mode
                map(Types1_7_6.ITEM); // item
                handler(wrapper -> {
                    final short slot = wrapper.get(Type.SHORT, 0);
                    final byte button = wrapper.get(Type.BYTE, 1);
                    final byte mode = wrapper.get(Type.BYTE, 2);

                    if (mode > 3) {
                        boolean startDragging = false;
                        boolean endDragging = false;
                        boolean droppingUsingQ = false;
                        boolean addSlot = false;

                        switch (mode) {
                            case 4:
                                droppingUsingQ = button + (slot != -999 ? 2 : 0) == 2;
                                break;
                            case 5:
                                startDragging = button == 0;
                                endDragging = button == 2;
                                addSlot = button == 1;
                                break;
                        }

                        final boolean leftClick = startDragging || addSlot || endDragging;
                        final boolean clickingOutside = slot == -999 && mode != 5;
                        final int mouseClick = leftClick ? 0 : 1;

                        if (droppingUsingQ) {
                            final PacketWrapper closeWindow = PacketWrapper.create(ClientboundPackets1_5_2.CLOSE_WINDOW, wrapper.user());
                            closeWindow.write(Type.BYTE, (byte) 0); // window id
                            closeWindow.send(Protocol1_5_0_1to1_4_6_7.class);
                            wrapper.cancel();
                            return;
                        }
                        if (slot < 0 && !clickingOutside) {
                            wrapper.cancel();
                            return;
                        }

                        wrapper.set(Type.BYTE, 1, (byte) mouseClick);
                        wrapper.set(Type.BYTE, 2, (byte) 0);
                        wrapper.set(Types1_7_6.ITEM, 0, new DataItem(34, (byte) 0, (short) 0, null));
                    }
                });
            }
        });
    }

    @Override
    public void init(UserConnection userConnection) {
        userConnection.put(new PreNettySplitter(Protocol1_5_0_1to1_4_6_7.class, ClientboundPackets1_4_6::getPacket));
    }

    @Override
    public LegacyItemRewriter<Protocol1_5_0_1to1_4_6_7> getItemRewriter() {
        return this.itemRewriter;
    }

}
