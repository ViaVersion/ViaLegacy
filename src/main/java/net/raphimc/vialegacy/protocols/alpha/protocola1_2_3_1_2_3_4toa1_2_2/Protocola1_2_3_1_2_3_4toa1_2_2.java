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
package net.raphimc.vialegacy.protocols.alpha.protocola1_2_3_1_2_3_4toa1_2_2;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.api.type.Type;
import net.raphimc.vialegacy.api.protocol.StatelessProtocol;
import net.raphimc.vialegacy.api.splitter.PreNettySplitter;
import net.raphimc.vialegacy.protocols.alpha.protocola1_2_3_5_1_2_6toa1_2_3_1_2_3_4.ClientboundPacketsa1_2_3;
import net.raphimc.vialegacy.protocols.alpha.protocolb1_0_1_1_1toa1_2_3_5_1_2_6.ServerboundPacketsa1_2_6;

public class Protocola1_2_3_1_2_3_4toa1_2_2 extends StatelessProtocol<ClientboundPacketsa1_2_2, ClientboundPacketsa1_2_3, ServerboundPacketsa1_2_2, ServerboundPacketsa1_2_6> {

    public Protocola1_2_3_1_2_3_4toa1_2_2() {
        super(ClientboundPacketsa1_2_2.class, ClientboundPacketsa1_2_3.class, ServerboundPacketsa1_2_2.class, ServerboundPacketsa1_2_6.class);
    }

    @Override
    protected void registerPackets() {
        this.registerClientbound(ClientboundPacketsa1_2_2.JOIN_GAME, wrapper -> {
            final PacketWrapper updateHealth = PacketWrapper.create(ClientboundPacketsa1_2_3.UPDATE_HEALTH, wrapper.user());
            updateHealth.write(Type.BYTE, (byte) 20); // health

            wrapper.send(Protocola1_2_3_1_2_3_4toa1_2_2.class);
            updateHealth.send(Protocola1_2_3_1_2_3_4toa1_2_2.class);
            wrapper.cancel();
        });

        this.registerServerbound(ServerboundPacketsa1_2_6.INTERACT_ENTITY, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.INT); // player id
                map(Type.INT); // entity id
                read(Type.BYTE); // mode
            }
        });
        this.cancelServerbound(ServerboundPacketsa1_2_6.RESPAWN);
    }

    @Override
    public void init(UserConnection userConnection) {
        userConnection.put(new PreNettySplitter(Protocola1_2_3_1_2_3_4toa1_2_2.class, ClientboundPacketsa1_2_2::getPacket));
    }

}
