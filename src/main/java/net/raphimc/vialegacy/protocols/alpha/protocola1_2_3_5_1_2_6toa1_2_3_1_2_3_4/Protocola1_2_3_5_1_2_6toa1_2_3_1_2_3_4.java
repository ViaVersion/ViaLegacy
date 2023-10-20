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
package net.raphimc.vialegacy.protocols.alpha.protocola1_2_3_5_1_2_6toa1_2_3_1_2_3_4;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.api.type.Type;
import net.raphimc.vialegacy.api.protocol.StatelessProtocol;
import net.raphimc.vialegacy.api.splitter.PreNettySplitter;
import net.raphimc.vialegacy.protocols.alpha.protocolb1_0_1_1_1toa1_2_3_5_1_2_6.ClientboundPacketsa1_2_6;
import net.raphimc.vialegacy.protocols.alpha.protocolb1_0_1_1_1toa1_2_3_5_1_2_6.ServerboundPacketsa1_2_6;

public class Protocola1_2_3_5_1_2_6toa1_2_3_1_2_3_4 extends StatelessProtocol<ClientboundPacketsa1_2_3, ClientboundPacketsa1_2_6, ServerboundPacketsa1_2_6, ServerboundPacketsa1_2_6> {

    public Protocola1_2_3_5_1_2_6toa1_2_3_1_2_3_4() {
        super(ClientboundPacketsa1_2_3.class, ClientboundPacketsa1_2_6.class, ServerboundPacketsa1_2_6.class, ServerboundPacketsa1_2_6.class);
    }

    @Override
    protected void registerPackets() {
        this.registerClientbound(ClientboundPacketsa1_2_3.ENTITY_VELOCITY, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.INT); // entity id
                map(Type.SHORT, Type.SHORT, v -> (short) (v / 4.0F)); // velocity x
                map(Type.SHORT, Type.SHORT, v -> (short) (v / 4.0F)); // velocity y
                map(Type.SHORT, Type.SHORT, v -> (short) (v / 4.0F)); // velocity z
            }
        });
    }

    @Override
    public void init(UserConnection userConnection) {
        userConnection.put(new PreNettySplitter(Protocola1_2_3_5_1_2_6toa1_2_3_1_2_3_4.class, ClientboundPacketsa1_2_3::getPacket));
    }

}
