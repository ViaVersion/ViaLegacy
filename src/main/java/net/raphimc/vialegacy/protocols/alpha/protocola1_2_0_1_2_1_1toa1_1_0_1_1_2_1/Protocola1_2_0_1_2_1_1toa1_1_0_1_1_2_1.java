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
package net.raphimc.vialegacy.protocols.alpha.protocola1_2_0_1_2_1_1toa1_1_0_1_1_2_1;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.api.type.Type;
import net.raphimc.vialegacy.api.protocol.StatelessProtocol;
import net.raphimc.vialegacy.api.splitter.PreNettySplitter;
import net.raphimc.vialegacy.protocols.alpha.protocola1_2_2toa1_2_0_1_2_1_1.ClientboundPacketsa1_2_0;
import net.raphimc.vialegacy.protocols.alpha.protocola1_2_2toa1_2_0_1_2_1_1.ServerboundPacketsa1_2_0;
import net.raphimc.vialegacy.protocols.beta.protocolb1_8_0_1tob1_7_0_3.types.Typesb1_7_0_3;

public class Protocola1_2_0_1_2_1_1toa1_1_0_1_1_2_1 extends StatelessProtocol<ClientboundPacketsa1_1_0, ClientboundPacketsa1_2_0, ServerboundPacketsa1_1_0, ServerboundPacketsa1_2_0> {

    public Protocola1_2_0_1_2_1_1toa1_1_0_1_1_2_1() {
        super(ClientboundPacketsa1_1_0.class, ClientboundPacketsa1_2_0.class, ServerboundPacketsa1_1_0.class, ServerboundPacketsa1_2_0.class);
    }

    @Override
    protected void registerPackets() {
        this.registerClientbound(ClientboundPacketsa1_1_0.JOIN_GAME, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.INT); // entity id
                map(Typesb1_7_0_3.STRING); // username
                map(Typesb1_7_0_3.STRING); // password
                create(Type.LONG, 0L); // seed
                create(Type.BYTE, (byte) 0); // dimension id
            }
        });

        this.registerServerbound(ServerboundPacketsa1_2_0.LOGIN, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.INT); // protocol id
                map(Typesb1_7_0_3.STRING); // username
                map(Typesb1_7_0_3.STRING); // password
                read(Type.LONG); // seed
                read(Type.BYTE); // dimension id
            }
        });
    }

    @Override
    public void init(UserConnection userConnection) {
        userConnection.put(new PreNettySplitter(Protocola1_2_0_1_2_1_1toa1_1_0_1_1_2_1.class, ClientboundPacketsa1_1_0::getPacket));
    }

}
