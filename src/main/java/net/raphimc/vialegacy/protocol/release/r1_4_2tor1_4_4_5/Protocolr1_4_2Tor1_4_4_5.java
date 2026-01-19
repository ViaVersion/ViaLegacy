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

package net.raphimc.vialegacy.protocol.release.r1_4_2tor1_4_4_5;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.entitydata.EntityData;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.api.type.Types;
import net.raphimc.vialegacy.api.protocol.StatelessProtocol;
import net.raphimc.vialegacy.api.splitter.PreNettySplitter;
import net.raphimc.vialegacy.protocol.release.r1_4_2tor1_4_4_5.packet.ClientboundPackets1_4_2;
import net.raphimc.vialegacy.protocol.release.r1_4_2tor1_4_4_5.rewriter.ItemRewriter;
import net.raphimc.vialegacy.protocol.release.r1_4_2tor1_4_4_5.types.Types1_4_2;
import net.raphimc.vialegacy.protocol.release.r1_4_4_5tor1_4_6_7.packet.ClientboundPackets1_4_4;
import net.raphimc.vialegacy.protocol.release.r1_5_2tor1_6_1.packet.ServerboundPackets1_5_2;
import net.raphimc.vialegacy.protocol.release.r1_6_4tor1_7_2_5.types.EntityDataTypes1_6_4;
import net.raphimc.vialegacy.protocol.release.r1_6_4tor1_7_2_5.types.Types1_6_4;

import java.util.List;

public class Protocolr1_4_2Tor1_4_4_5 extends StatelessProtocol<ClientboundPackets1_4_2, ClientboundPackets1_4_4, ServerboundPackets1_5_2, ServerboundPackets1_5_2> {

    private final ItemRewriter itemRewriter = new ItemRewriter(this);

    public Protocolr1_4_2Tor1_4_4_5() {
        super(ClientboundPackets1_4_2.class, ClientboundPackets1_4_4.class, ServerboundPackets1_5_2.class, ServerboundPackets1_5_2.class);
    }

    @Override
    protected void registerPackets() {
        super.registerPackets();

        this.registerClientbound(ClientboundPackets1_4_2.MAP_ITEM_DATA, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.SHORT); // item id
                map(Types.SHORT); // map id
                map(Types1_4_2.UNSIGNED_BYTE_BYTE_ARRAY, Types.SHORT_BYTE_ARRAY); // data
            }
        });
        this.registerClientbound(ClientboundPackets1_4_2.ADD_PLAYER, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // entity id
                map(Types1_6_4.STRING); // name
                map(Types.INT); // x
                map(Types.INT); // y
                map(Types.INT); // z
                map(Types.BYTE); // yaw
                map(Types.BYTE); // pitch
                map(Types.UNSIGNED_SHORT); // item
                map(Types1_4_2.ENTITY_DATA_LIST, Types1_6_4.ENTITY_DATA_LIST); // entity data
                handler(wrapper -> rewriteEntityData(wrapper.get(Types1_6_4.ENTITY_DATA_LIST, 0)));
            }
        });
        this.registerClientbound(ClientboundPackets1_4_2.ADD_MOB, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // entity id
                map(Types.UNSIGNED_BYTE); // type id
                map(Types.INT); // x
                map(Types.INT); // y
                map(Types.INT); // z
                map(Types.BYTE); // yaw
                map(Types.BYTE); // pitch
                map(Types.BYTE); // head yaw
                map(Types.SHORT); // velocity x
                map(Types.SHORT); // velocity y
                map(Types.SHORT); // velocity z
                map(Types1_4_2.ENTITY_DATA_LIST, Types1_6_4.ENTITY_DATA_LIST); // entity data
                handler(wrapper -> rewriteEntityData(wrapper.get(Types1_6_4.ENTITY_DATA_LIST, 0)));
            }
        });
        this.registerClientbound(ClientboundPackets1_4_2.SET_ENTITY_DATA, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // entity id
                map(Types1_4_2.ENTITY_DATA_LIST, Types1_6_4.ENTITY_DATA_LIST); // entity data
                handler(wrapper -> rewriteEntityData(wrapper.get(Types1_6_4.ENTITY_DATA_LIST, 0)));
            }
        });
    }

    private void rewriteEntityData(final List<EntityData> entityDataList) {
        for (EntityData entityData : entityDataList) {
            entityData.setDataType(EntityDataTypes1_6_4.byId(entityData.dataType().typeId()));
        }
    }

    @Override
    public void init(UserConnection userConnection) {
        userConnection.put(new PreNettySplitter(Protocolr1_4_2Tor1_4_4_5.class, ClientboundPackets1_4_2::getPacket));
    }

    @Override
    public ItemRewriter getItemRewriter() {
        return this.itemRewriter;
    }

}
