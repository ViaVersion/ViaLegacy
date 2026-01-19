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
package net.raphimc.vialegacy.protocol.classic.c0_0_19a_06toc0_0_20a_27;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.api.type.Types;
import net.raphimc.vialegacy.api.LegacyProtocolVersion;
import net.raphimc.vialegacy.api.protocol.StatelessProtocol;
import net.raphimc.vialegacy.api.splitter.PreNettySplitter;
import net.raphimc.vialegacy.protocol.classic.c0_0_19a_06toc0_0_20a_27.packet.ClientboundPacketsc0_19a;
import net.raphimc.vialegacy.protocol.classic.c0_0_19a_06toc0_0_20a_27.packet.ServerboundPacketsc0_19a;
import net.raphimc.vialegacy.protocol.classic.c0_0_20a_27toc0_28_30.packet.ClientboundPacketsc0_20a;
import net.raphimc.vialegacy.protocol.classic.c0_28_30toa1_0_15.data.ClassicBlocks;
import net.raphimc.vialegacy.protocol.classic.c0_28_30toa1_0_15.packet.ServerboundPacketsc0_28;
import net.raphimc.vialegacy.protocol.classic.c0_28_30toa1_0_15.storage.ClassicBlockRemapper;
import net.raphimc.vialegacy.protocol.classic.c0_28_30toa1_0_15.types.Typesc0_30;

public class Protocolc0_0_19a_06Toc0_0_20a_27 extends StatelessProtocol<ClientboundPacketsc0_19a, ClientboundPacketsc0_20a, ServerboundPacketsc0_19a, ServerboundPacketsc0_28> {

    public Protocolc0_0_19a_06Toc0_0_20a_27() {
        super(ClientboundPacketsc0_19a.class, ClientboundPacketsc0_20a.class, ServerboundPacketsc0_19a.class, ServerboundPacketsc0_28.class);
    }

    @Override
    protected void registerPackets() {
        this.registerClientbound(ClientboundPacketsc0_19a.LOGIN, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.BYTE); // protocol id
                map(Typesc0_30.STRING); // title
                map(Typesc0_30.STRING); // motd
                create(Types.BYTE, (byte) 0); // op level
            }
        });

        this.registerServerbound(ServerboundPacketsc0_28.LOGIN, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.BYTE); // protocol id
                map(Typesc0_30.STRING); // username
                map(Typesc0_30.STRING); // mp pass
                read(Types.BYTE); // op level
            }
        });
    }

    @Override
    public void init(UserConnection userConnection) {
        userConnection.put(new PreNettySplitter(Protocolc0_0_19a_06Toc0_0_20a_27.class, ClientboundPacketsc0_19a::getPacket));

        final ClassicBlockRemapper previousRemapper = userConnection.get(ClassicBlockRemapper.class);
        userConnection.put(new ClassicBlockRemapper(previousRemapper.mapper(), o -> {
            int block = previousRemapper.reverseMapper().getInt(o);

            if (userConnection.getProtocolInfo().serverProtocolVersion().equals(LegacyProtocolVersion.c0_0_19a_06)) {
                if (block != ClassicBlocks.STONE && block != ClassicBlocks.DIRT && block != ClassicBlocks.WOOD && block != ClassicBlocks.SAPLING && block != ClassicBlocks.GRAVEL && block != ClassicBlocks.LOG && block != ClassicBlocks.LEAVES && block != ClassicBlocks.SPONGE && block != ClassicBlocks.GLASS) {
                    block = ClassicBlocks.STONE;
                }
            }

            return block;
        }));
    }

}
