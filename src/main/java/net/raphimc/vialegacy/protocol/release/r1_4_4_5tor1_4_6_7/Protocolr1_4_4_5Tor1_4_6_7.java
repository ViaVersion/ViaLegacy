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
package net.raphimc.vialegacy.protocol.release.r1_4_4_5tor1_4_6_7;

import com.google.common.collect.Lists;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.entities.EntityTypes1_8;
import com.viaversion.viaversion.api.minecraft.entitydata.EntityData;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.api.type.Types;
import net.raphimc.vialegacy.api.protocol.StatelessProtocol;
import net.raphimc.vialegacy.api.splitter.PreNettySplitter;
import net.raphimc.vialegacy.protocol.release.r1_4_4_5tor1_4_6_7.packet.ClientboundPackets1_4_4;
import net.raphimc.vialegacy.protocol.release.r1_4_4_5tor1_4_6_7.rewriter.ItemRewriter;
import net.raphimc.vialegacy.protocol.release.r1_4_4_5tor1_4_6_7.types.Types1_4_4;
import net.raphimc.vialegacy.protocol.release.r1_4_6_7tor1_5_0_1.packet.ClientboundPackets1_4_6;
import net.raphimc.vialegacy.protocol.release.r1_5_2tor1_6_1.packet.ServerboundPackets1_5_2;
import net.raphimc.vialegacy.protocol.release.r1_6_4tor1_7_2_5.types.EntityDataTypes1_6_4;
import net.raphimc.vialegacy.protocol.release.r1_6_4tor1_7_2_5.types.Types1_6_4;
import net.raphimc.vialegacy.protocol.release.r1_7_6_10tor1_8.data.EntityDataIndex1_7_6;
import net.raphimc.vialegacy.protocol.release.r1_7_6_10tor1_8.types.Types1_7_6;

public class Protocolr1_4_4_5Tor1_4_6_7 extends StatelessProtocol<ClientboundPackets1_4_4, ClientboundPackets1_4_6, ServerboundPackets1_5_2, ServerboundPackets1_5_2> {

    private final ItemRewriter itemRewriter = new ItemRewriter(this);

    public Protocolr1_4_4_5Tor1_4_6_7() {
        super(ClientboundPackets1_4_4.class, ClientboundPackets1_4_6.class, ServerboundPackets1_5_2.class, ServerboundPackets1_5_2.class);
    }

    @Override
    protected void registerPackets() {
        super.registerPackets();

        this.registerClientbound(ClientboundPackets1_4_4.SPAWN_ITEM, ClientboundPackets1_4_6.ADD_ENTITY, wrapper -> {
            final int entityId = wrapper.read(Types.INT); // entity id
            final Item item = wrapper.read(Types1_7_6.ITEM); // item
            final int x = wrapper.read(Types.INT); // x
            final int y = wrapper.read(Types.INT); // y
            final int z = wrapper.read(Types.INT); // z
            final int motionX = wrapper.read(Types.BYTE); // velocity x
            final int motionY = wrapper.read(Types.BYTE); // velocity y
            final int motionZ = wrapper.read(Types.BYTE); // velocity z

            wrapper.write(Types.INT, entityId); // entity id
            wrapper.write(Types.BYTE, (byte) EntityTypes1_8.ObjectType.ITEM.getId()); // type id
            wrapper.write(Types.INT, x); // x
            wrapper.write(Types.INT, y); // y
            wrapper.write(Types.INT, z); // z
            wrapper.write(Types.BYTE, (byte) 0); // yaw
            wrapper.write(Types.BYTE, (byte) 0); // pitch
            wrapper.write(Types.INT, 1); // data (any value above 0)
            wrapper.write(Types.SHORT, (short) (motionX / 128F * 8000F)); // velocity x
            wrapper.write(Types.SHORT, (short) (motionY / 128F * 8000F)); // velocity y
            wrapper.write(Types.SHORT, (short) (motionZ / 128F * 8000F)); // velocity z

            final PacketWrapper setEntityData = PacketWrapper.create(ClientboundPackets1_4_6.SET_ENTITY_DATA, wrapper.user());
            setEntityData.write(Types.INT, entityId); // entity id
            setEntityData.write(Types1_6_4.ENTITY_DATA_LIST, Lists.newArrayList(new EntityData(EntityDataIndex1_7_6.ITEM_ITEM.getOldIndex(), EntityDataTypes1_6_4.ITEM, item))); // entity data

            wrapper.send(Protocolr1_4_4_5Tor1_4_6_7.class);
            setEntityData.send(Protocolr1_4_4_5Tor1_4_6_7.class);
            wrapper.cancel();
        });
        this.registerClientbound(ClientboundPackets1_4_4.ADD_ENTITY, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // entity id
                map(Types.BYTE); // type id
                map(Types.INT); // x
                map(Types.INT); // y
                map(Types.INT); // z
                create(Types.BYTE, (byte) 0); // pitch
                create(Types.BYTE, (byte) 0); // yaw
                map(Types.INT); // data
                // more conditional data
            }
        });
        this.registerClientbound(ClientboundPackets1_4_4.MAP_BULK_CHUNK, new PacketHandlers() {
            @Override
            public void register() {
                map(Types1_4_4.CHUNK_BULK, Types1_7_6.CHUNK_BULK);
            }
        });

        this.registerServerbound(ServerboundPackets1_5_2.PLAYER_ACTION, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.UNSIGNED_BYTE); // status
                handler(wrapper -> {
                    final short status = wrapper.get(Types.UNSIGNED_BYTE, 0);
                    if (status == 3) {
                        wrapper.set(Types.UNSIGNED_BYTE, 0, (short) 4);
                    }
                });
                map(Types1_7_6.BLOCK_POSITION_UBYTE); // position
                map(Types.UNSIGNED_BYTE); // direction
            }
        });
    }

    @Override
    public void init(UserConnection userConnection) {
        userConnection.put(new PreNettySplitter(Protocolr1_4_4_5Tor1_4_6_7.class, ClientboundPackets1_4_4::getPacket));
    }

    @Override
    public ItemRewriter getItemRewriter() {
        return this.itemRewriter;
    }

}
