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
package net.raphimc.vialegacy.protocols.release.protocol1_4_4_5to1_4_2;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.api.type.Type;
import net.raphimc.vialegacy.api.protocol.StatelessProtocol;
import net.raphimc.vialegacy.api.remapper.LegacyItemRewriter;
import net.raphimc.vialegacy.api.splitter.PreNettySplitter;
import net.raphimc.vialegacy.protocols.release.protocol1_4_4_5to1_4_2.rewriter.ItemRewriter;
import net.raphimc.vialegacy.protocols.release.protocol1_4_4_5to1_4_2.types.Types1_4_2;
import net.raphimc.vialegacy.protocols.release.protocol1_4_6_7to1_4_4_5.ClientboundPackets1_4_4;
import net.raphimc.vialegacy.protocols.release.protocol1_6_1to1_5_2.ServerboundPackets1_5_2;
import net.raphimc.vialegacy.protocols.release.protocol1_7_2_5to1_6_4.types.MetaType1_6_4;
import net.raphimc.vialegacy.protocols.release.protocol1_7_2_5to1_6_4.types.Types1_6_4;

import java.util.List;

public class Protocol1_4_4_5to1_4_2 extends StatelessProtocol<ClientboundPackets1_4_2, ClientboundPackets1_4_4, ServerboundPackets1_5_2, ServerboundPackets1_5_2> {

    private final LegacyItemRewriter<Protocol1_4_4_5to1_4_2> itemRewriter = new ItemRewriter(this);

    public Protocol1_4_4_5to1_4_2() {
        super(ClientboundPackets1_4_2.class, ClientboundPackets1_4_4.class, ServerboundPackets1_5_2.class, ServerboundPackets1_5_2.class);
    }

    @Override
    protected void registerPackets() {
        this.itemRewriter.register();

        this.registerClientbound(ClientboundPackets1_4_2.MAP_DATA, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.SHORT); // item id
                map(Type.SHORT); // map id
                map(Types1_4_2.UNSIGNED_BYTE_BYTE_ARRAY, Type.SHORT_BYTE_ARRAY); // data
            }
        });
        this.registerClientbound(ClientboundPackets1_4_2.SPAWN_PLAYER, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.INT); // entity id
                map(Types1_6_4.STRING); // name
                map(Type.INT); // x
                map(Type.INT); // y
                map(Type.INT); // z
                map(Type.BYTE); // yaw
                map(Type.BYTE); // pitch
                map(Type.UNSIGNED_SHORT); // item
                map(Types1_4_2.METADATA_LIST, Types1_6_4.METADATA_LIST); // metadata
                handler(wrapper -> rewriteMetadata(wrapper.get(Types1_6_4.METADATA_LIST, 0)));
            }
        });
        this.registerClientbound(ClientboundPackets1_4_2.SPAWN_MOB, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.INT); // entity id
                map(Type.UNSIGNED_BYTE); // type id
                map(Type.INT); // x
                map(Type.INT); // y
                map(Type.INT); // z
                map(Type.BYTE); // yaw
                map(Type.BYTE); // pitch
                map(Type.BYTE); // head yaw
                map(Type.SHORT); // velocity x
                map(Type.SHORT); // velocity y
                map(Type.SHORT); // velocity z
                map(Types1_4_2.METADATA_LIST, Types1_6_4.METADATA_LIST); // metadata
                handler(wrapper -> rewriteMetadata(wrapper.get(Types1_6_4.METADATA_LIST, 0)));
            }
        });
        this.registerClientbound(ClientboundPackets1_4_2.ENTITY_METADATA, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.INT); // entity id
                map(Types1_4_2.METADATA_LIST, Types1_6_4.METADATA_LIST); // metadata
                handler(wrapper -> rewriteMetadata(wrapper.get(Types1_6_4.METADATA_LIST, 0)));
            }
        });
    }

    private void rewriteMetadata(final List<Metadata> metadataList) {
        for (Metadata metadata : metadataList) {
            metadata.setMetaType(MetaType1_6_4.byId(metadata.metaType().typeId()));
        }
    }

    @Override
    public void init(UserConnection userConnection) {
        userConnection.put(new PreNettySplitter(Protocol1_4_4_5to1_4_2.class, ClientboundPackets1_4_2::getPacket));
    }

    @Override
    public LegacyItemRewriter<Protocol1_4_4_5to1_4_2> getItemRewriter() {
        return this.itemRewriter;
    }

}
