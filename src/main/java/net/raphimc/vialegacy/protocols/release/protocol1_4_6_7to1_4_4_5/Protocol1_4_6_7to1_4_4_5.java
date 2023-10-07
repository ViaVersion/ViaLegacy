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
package net.raphimc.vialegacy.protocols.release.protocol1_4_6_7to1_4_4_5;

import com.google.common.collect.Lists;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_10Types;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import net.raphimc.vialegacy.api.protocol.StatelessProtocol;
import net.raphimc.vialegacy.api.remapper.LegacyItemRewriter;
import net.raphimc.vialegacy.api.splitter.PreNettySplitter;
import net.raphimc.vialegacy.protocols.release.protocol1_4_6_7to1_4_4_5.rewriter.ItemRewriter;
import net.raphimc.vialegacy.protocols.release.protocol1_4_6_7to1_4_4_5.types.ChunkBulk1_4_4Type;
import net.raphimc.vialegacy.protocols.release.protocol1_5_0_1to1_4_6_7.ClientboundPackets1_4_6;
import net.raphimc.vialegacy.protocols.release.protocol1_6_1to1_5_2.ServerboundPackets1_5_2;
import net.raphimc.vialegacy.protocols.release.protocol1_7_2_5to1_6_4.types.MetaType1_6_4;
import net.raphimc.vialegacy.protocols.release.protocol1_7_2_5to1_6_4.types.Types1_6_4;
import net.raphimc.vialegacy.protocols.release.protocol1_8to1_7_6_10.metadata.MetaIndex1_8to1_7_6;
import net.raphimc.vialegacy.protocols.release.protocol1_8to1_7_6_10.types.ChunkBulk1_7_6Type;
import net.raphimc.vialegacy.protocols.release.protocol1_8to1_7_6_10.types.Types1_7_6;

public class Protocol1_4_6_7to1_4_4_5 extends StatelessProtocol<ClientboundPackets1_4_4, ClientboundPackets1_4_6, ServerboundPackets1_5_2, ServerboundPackets1_5_2> {

    private final LegacyItemRewriter<Protocol1_4_6_7to1_4_4_5> itemRewriter = new ItemRewriter(this);

    public Protocol1_4_6_7to1_4_4_5() {
        super(ClientboundPackets1_4_4.class, ClientboundPackets1_4_6.class, ServerboundPackets1_5_2.class, ServerboundPackets1_5_2.class);
    }

    @Override
    protected void registerPackets() {
        this.itemRewriter.register();

        this.registerClientbound(ClientboundPackets1_4_4.JOIN_GAME, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.INT); // entity id
                map(Types1_6_4.STRING); // level type
                map(Type.BYTE); // game mode
                map(Type.BYTE); // dimension id
                map(Type.BYTE); // difficulty
                map(Type.BYTE); // world height
                map(Type.BYTE); // max players
                handler(wrapper -> wrapper.user().get(ClientWorld.class).setEnvironment(wrapper.get(Type.BYTE, 1)));
            }
        });
        this.registerClientbound(ClientboundPackets1_4_4.RESPAWN, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.INT); // dimension id
                map(Type.BYTE); // difficulty
                map(Type.BYTE); // game mode
                map(Type.SHORT); // world height
                map(Types1_6_4.STRING); // worldType
                handler(wrapper -> wrapper.user().get(ClientWorld.class).setEnvironment(wrapper.get(Type.INT, 0)));
            }
        });
        this.registerClientbound(ClientboundPackets1_4_4.SPAWN_ITEM, ClientboundPackets1_4_6.SPAWN_ENTITY, new PacketHandlers() {
            @Override
            public void register() {
                handler(wrapper -> {
                    final int entityId = wrapper.read(Type.INT); // entity id
                    final Item item = wrapper.read(Types1_7_6.COMPRESSED_ITEM); // item
                    final int x = wrapper.read(Type.INT); // x
                    final int y = wrapper.read(Type.INT); // y
                    final int z = wrapper.read(Type.INT); // z
                    final int motionX = wrapper.read(Type.BYTE); // velocity x
                    final int motionY = wrapper.read(Type.BYTE); // velocity y
                    final int motionZ = wrapper.read(Type.BYTE); // velocity z

                    wrapper.write(Type.INT, entityId); // entity id
                    wrapper.write(Type.BYTE, (byte) Entity1_10Types.ObjectType.ITEM.getId()); // type id
                    wrapper.write(Type.INT, x); // x
                    wrapper.write(Type.INT, y); // y
                    wrapper.write(Type.INT, z); // z
                    wrapper.write(Type.BYTE, (byte) 0); // yaw
                    wrapper.write(Type.BYTE, (byte) 0); // pitch
                    wrapper.write(Type.INT, 1); // data (any value above 0)
                    wrapper.write(Type.SHORT, (short) (motionX / 128F * 8000F)); // velocity x
                    wrapper.write(Type.SHORT, (short) (motionY / 128F * 8000F)); // velocity y
                    wrapper.write(Type.SHORT, (short) (motionZ / 128F * 8000F)); // velocity z

                    final PacketWrapper metadata = PacketWrapper.create(ClientboundPackets1_4_6.ENTITY_METADATA, wrapper.user());
                    metadata.write(Type.INT, entityId); // entity id
                    metadata.write(Types1_6_4.METADATA_LIST, Lists.newArrayList(new Metadata(MetaIndex1_8to1_7_6.ITEM_ITEM.getOldIndex(), MetaType1_6_4.Slot, item))); // metadata

                    wrapper.send(Protocol1_4_6_7to1_4_4_5.class);
                    metadata.send(Protocol1_4_6_7to1_4_4_5.class);
                    wrapper.cancel();
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_4_4.SPAWN_ENTITY, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.INT); // entity id
                map(Type.BYTE); // type id
                map(Type.INT); // x
                map(Type.INT); // y
                map(Type.INT); // z
                create(Type.BYTE, (byte) 0); // pitch
                create(Type.BYTE, (byte) 0); // yaw
                map(Type.INT); // data
                // more conditional data
            }
        });
        this.registerClientbound(ClientboundPackets1_4_4.MAP_BULK_CHUNK, new PacketHandlers() {
            @Override
            public void register() {
                handler(wrapper -> {
                    final ClientWorld clientWorld = wrapper.user().get(ClientWorld.class);
                    wrapper.write(new ChunkBulk1_7_6Type(clientWorld), wrapper.read(new ChunkBulk1_4_4Type(clientWorld)));
                });
            }
        });

        this.registerServerbound(ServerboundPackets1_5_2.PLAYER_DIGGING, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.UNSIGNED_BYTE); // status
                handler(wrapper -> {
                    final short status = wrapper.get(Type.UNSIGNED_BYTE, 0);
                    if (status == 3) {
                        wrapper.set(Type.UNSIGNED_BYTE, 0, (short) 4);
                    }
                });
                map(Types1_7_6.POSITION_UBYTE); // position
                map(Type.UNSIGNED_BYTE); // direction
            }
        });
    }

    @Override
    public void init(UserConnection userConnection) {
        userConnection.put(new PreNettySplitter(userConnection, Protocol1_4_6_7to1_4_4_5.class, ClientboundPackets1_4_4::getPacket));

        if (!userConnection.has(ClientWorld.class)) {
            userConnection.put(new ClientWorld(userConnection));
        }
    }

    @Override
    public LegacyItemRewriter<Protocol1_4_6_7to1_4_4_5> getItemRewriter() {
        return this.itemRewriter;
    }

}
