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
package net.raphimc.vialegacy.protocols.release.protocol1_6_2to1_6_1;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.Position;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.api.type.Type;
import net.raphimc.vialegacy.api.data.ItemList1_6;
import net.raphimc.vialegacy.api.protocol.StatelessProtocol;
import net.raphimc.vialegacy.api.splitter.PreNettySplitter;
import net.raphimc.vialegacy.api.util.BlockFaceUtil;
import net.raphimc.vialegacy.protocols.release.protocol1_7_2_5to1_6_4.ClientboundPackets1_6_4;
import net.raphimc.vialegacy.protocols.release.protocol1_7_2_5to1_6_4.ServerboundPackets1_6_4;
import net.raphimc.vialegacy.protocols.release.protocol1_7_2_5to1_6_4.types.Types1_6_4;
import net.raphimc.vialegacy.protocols.release.protocol1_8to1_7_6_10.types.Types1_7_6;

import java.nio.charset.StandardCharsets;

public class Protocol1_6_2to1_6_1 extends StatelessProtocol<ClientboundPackets1_6_1, ClientboundPackets1_6_4, ServerboundPackets1_6_4, ServerboundPackets1_6_4> {

    public Protocol1_6_2to1_6_1() {
        super(ClientboundPackets1_6_1.class, ClientboundPackets1_6_4.class, ServerboundPackets1_6_4.class, ServerboundPackets1_6_4.class);
    }

    @Override
    protected void registerPackets() {
        this.registerClientbound(ClientboundPackets1_6_1.JOIN_GAME, wrapper -> {
            final PacketWrapper brand = PacketWrapper.create(ClientboundPackets1_6_4.PLUGIN_MESSAGE, wrapper.user());
            brand.write(Types1_6_4.STRING, "MC|Brand");
            final byte[] brandBytes = "legacy".getBytes(StandardCharsets.UTF_8);
            brand.write(Type.SHORT, (short) brandBytes.length); // data length
            brand.write(Type.REMAINING_BYTES, brandBytes); // data

            wrapper.send(Protocol1_6_2to1_6_1.class);
            brand.send(Protocol1_6_2to1_6_1.class);
            wrapper.cancel();
        });
        this.registerClientbound(ClientboundPackets1_6_1.ENTITY_PROPERTIES, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.INT); // entity id
                handler(wrapper -> {
                    final int amount = wrapper.passthrough(Type.INT); // count
                    for (int i = 0; i < amount; i++) {
                        wrapper.passthrough(Types1_6_4.STRING); // id
                        wrapper.passthrough(Type.DOUBLE); // baseValue
                        wrapper.write(Type.SHORT, (short) 0); // modifier count
                    }
                });
            }
        });

        this.registerServerbound(ServerboundPackets1_6_4.PLAYER_BLOCK_PLACEMENT, new PacketHandlers() {
            @Override
            public void register() {
                map(Types1_7_6.POSITION_UBYTE); // position
                map(Type.UNSIGNED_BYTE); // direction
                map(Types1_7_6.ITEM); // item
                map(Type.UNSIGNED_BYTE); // offset x
                map(Type.UNSIGNED_BYTE); // offset y
                map(Type.UNSIGNED_BYTE); // offset z
                handler(wrapper -> {
                    final Position pos = wrapper.get(Types1_7_6.POSITION_UBYTE, 0);
                    final short direction = wrapper.get(Type.UNSIGNED_BYTE, 0);
                    final Item item = wrapper.get(Types1_7_6.ITEM, 0);

                    if (item != null && item.identifier() == ItemList1_6.sign.itemID && direction != 255 && direction != 0) { // If placed item is a sign then cancel and send a OPEN_SIGN_EDITOR packet
                        final PacketWrapper openSignEditor = PacketWrapper.create(ClientboundPackets1_6_4.OPEN_SIGN_EDITOR, wrapper.user());
                        openSignEditor.write(Type.BYTE, (byte) 0); // magic value
                        openSignEditor.write(Types1_7_6.POSITION_INT, pos.getRelative(BlockFaceUtil.getFace(direction)));
                        openSignEditor.send(Protocol1_6_2to1_6_1.class);
                    }
                });
            }
        });
    }

    @Override
    public void init(UserConnection userConnection) {
        userConnection.put(new PreNettySplitter(Protocol1_6_2to1_6_1.class, ClientboundPackets1_6_1::getPacket));
    }

}
