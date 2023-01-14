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
package net.raphimc.vialegacy.protocols.classic.protocolc0_0_20a_27toc0_0_19a_06;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.protocol.packet.State;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.api.type.Type;
import net.raphimc.vialegacy.api.splitter.PreNettySplitter;
import net.raphimc.vialegacy.protocols.classic.protocola1_0_15toc0_28_30.ServerboundPacketsc0_28;
import net.raphimc.vialegacy.protocols.classic.protocola1_0_15toc0_28_30.data.ClassicBlocks;
import net.raphimc.vialegacy.protocols.classic.protocola1_0_15toc0_28_30.storage.ClassicBlockRemapper;
import net.raphimc.vialegacy.protocols.classic.protocola1_0_15toc0_28_30.types.Typesc0_30;
import net.raphimc.vialegacy.protocols.classic.protocolc0_0_19a_06toc0_0_18a_02.Protocolc0_0_19a_06toc0_0_18a_02;
import net.raphimc.vialegacy.protocols.classic.protocolc0_28_30toc0_0_20a_27.ClientboundPacketsc0_20a;

public class Protocolc0_27toc0_0_19a_06 extends AbstractProtocol<ClientboundPacketsc0_19a, ClientboundPacketsc0_20a, ServerboundPacketsc0_19a, ServerboundPacketsc0_28> {

    public Protocolc0_27toc0_0_19a_06() {
        super(ClientboundPacketsc0_19a.class, ClientboundPacketsc0_20a.class, ServerboundPacketsc0_19a.class, ServerboundPacketsc0_28.class);
    }

    @Override
    protected void registerPackets() {
        this.registerClientbound(ClientboundPacketsc0_19a.JOIN_GAME, new PacketRemapper() {
            @Override
            public void registerMap() {
                map(Type.BYTE); // protocol id
                map(Typesc0_30.STRING); // title
                map(Typesc0_30.STRING); // motd
                create(Type.BYTE, (byte) 0); // op level
            }
        });

        this.registerServerbound(State.LOGIN, ServerboundPacketsc0_19a.LOGIN.getId(), ServerboundPacketsc0_28.LOGIN.getId(), new PacketRemapper() {
            @Override
            public void registerMap() {
                map(Type.BYTE); // protocol id
                map(Typesc0_30.STRING); // username
                map(Typesc0_30.STRING); // mp pass
                read(Type.BYTE); // op level
            }
        });
    }

    @Override
    public void init(UserConnection userConnection) {
        userConnection.put(new PreNettySplitter(userConnection, Protocolc0_27toc0_0_19a_06.class, ClientboundPacketsc0_19a::getPacket));

        final ClassicBlockRemapper previousRemapper = userConnection.get(ClassicBlockRemapper.class);
        userConnection.put(new ClassicBlockRemapper(userConnection, previousRemapper.getMapper(), o -> {
            int block = previousRemapper.getReverseMapper().getInt(o);

            if (!userConnection.getProtocolInfo().getPipeline().contains(Protocolc0_0_19a_06toc0_0_18a_02.class)) {
                if (block != ClassicBlocks.STONE && block != ClassicBlocks.DIRT && block != ClassicBlocks.WOOD && block != ClassicBlocks.SAPLING && block != ClassicBlocks.GRAVEL && block != ClassicBlocks.LOG && block != ClassicBlocks.LEAVES && block != ClassicBlocks.SPONGE && block != ClassicBlocks.GLASS) {
                    block = ClassicBlocks.STONE;
                }
            }

            return block;
        }));
    }

}
