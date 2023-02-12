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
package net.raphimc.vialegacy.protocols.alpha.protocola1_0_16_2toa1_0_15;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.packet.State;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import net.raphimc.vialegacy.api.splitter.PreNettySplitter;
import net.raphimc.vialegacy.protocols.alpha.protocola1_0_17_1_0_17_4toa1_0_16_2.ClientboundPacketsa1_0_16;
import net.raphimc.vialegacy.protocols.alpha.protocola1_1_0_1_1_2_1toa1_0_17_1_0_17_4.ServerboundPacketsa1_0_17;
import net.raphimc.vialegacy.protocols.beta.protocolb1_8_0_1tob1_7_0_3.types.Typesb1_7_0_3;

public class Protocola1_0_16_2toa1_0_15 extends AbstractProtocol<ClientboundPacketsa1_0_15, ClientboundPacketsa1_0_16, ServerboundPacketsa1_0_15, ServerboundPacketsa1_0_17> {

    public Protocola1_0_16_2toa1_0_15() {
        super(ClientboundPacketsa1_0_15.class, ClientboundPacketsa1_0_16.class, ServerboundPacketsa1_0_15.class, ServerboundPacketsa1_0_17.class);
    }

    @Override
    protected void registerPackets() {
        this.registerServerbound(State.LOGIN, -1, ServerboundPacketsa1_0_17.HANDSHAKE.getId(), new PacketHandlers() {
            @Override
            public void register() {
                handler(wrapper -> {
                    wrapper.cancel();
                    final PacketWrapper handshake = PacketWrapper.create(ClientboundPacketsa1_0_16.HANDSHAKE, wrapper.user());
                    handshake.write(Typesb1_7_0_3.STRING, "-"); // server hash
                    handshake.send(Protocola1_0_16_2toa1_0_15.class);
                });
            }
        });
        this.cancelServerbound(ServerboundPacketsa1_0_17.HANDSHAKE);
    }

    @Override
    public void init(UserConnection userConnection) {
        userConnection.put(new PreNettySplitter(userConnection, Protocola1_0_16_2toa1_0_15.class, ClientboundPacketsa1_0_15::getPacket));
    }

}
