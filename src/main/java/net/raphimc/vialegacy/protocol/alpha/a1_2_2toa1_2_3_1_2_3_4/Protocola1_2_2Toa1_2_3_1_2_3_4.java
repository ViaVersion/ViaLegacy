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

package net.raphimc.vialegacy.protocol.alpha.a1_2_2toa1_2_3_1_2_3_4;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.api.type.Types;
import net.raphimc.vialegacy.api.protocol.StatelessProtocol;
import net.raphimc.vialegacy.api.splitter.PreNettySplitter;
import net.raphimc.vialegacy.protocol.alpha.a1_2_2toa1_2_3_1_2_3_4.packet.ClientboundPacketsa1_2_2;
import net.raphimc.vialegacy.protocol.alpha.a1_2_2toa1_2_3_1_2_3_4.packet.ServerboundPacketsa1_2_2;
import net.raphimc.vialegacy.protocol.alpha.a1_2_3_1_2_3_4toa1_2_3_5_1_2_6.packet.ClientboundPacketsa1_2_3;
import net.raphimc.vialegacy.protocol.alpha.a1_2_3_5_1_2_6tob1_0_1_1_1.packet.ServerboundPacketsa1_2_6;

public class Protocola1_2_2Toa1_2_3_1_2_3_4 extends StatelessProtocol<ClientboundPacketsa1_2_2, ClientboundPacketsa1_2_3, ServerboundPacketsa1_2_2, ServerboundPacketsa1_2_6> {

    public Protocola1_2_2Toa1_2_3_1_2_3_4() {
        super(ClientboundPacketsa1_2_2.class, ClientboundPacketsa1_2_3.class, ServerboundPacketsa1_2_2.class, ServerboundPacketsa1_2_6.class);
    }

    @Override
    protected void registerPackets() {
        this.registerClientbound(ClientboundPacketsa1_2_2.LOGIN, wrapper -> {
            final PacketWrapper updateHealth = PacketWrapper.create(ClientboundPacketsa1_2_3.SET_HEALTH, wrapper.user());
            updateHealth.write(Types.BYTE, (byte) 20); // health

            wrapper.send(Protocola1_2_2Toa1_2_3_1_2_3_4.class);
            updateHealth.send(Protocola1_2_2Toa1_2_3_1_2_3_4.class);
            wrapper.cancel();
        });

        this.registerServerbound(ServerboundPacketsa1_2_6.INTERACT, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // player id
                map(Types.INT); // entity id
                read(Types.BYTE); // mode
            }
        });
        this.cancelServerbound(ServerboundPacketsa1_2_6.RESPAWN);
    }

    @Override
    public void init(UserConnection userConnection) {
        userConnection.put(new PreNettySplitter(Protocola1_2_2Toa1_2_3_1_2_3_4.class, ClientboundPacketsa1_2_2::getPacket));
    }

}
