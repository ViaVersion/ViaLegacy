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
package net.raphimc.vialegacy.protocols.alpha.protocola1_2_2toa1_2_0_1_2_1_1;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.api.type.Type;
import net.raphimc.vialegacy.api.protocol.StatelessProtocol;
import net.raphimc.vialegacy.api.splitter.PreNettySplitter;
import net.raphimc.vialegacy.protocols.alpha.protocola1_2_3_1_2_3_4toa1_2_2.ClientboundPacketsa1_2_2;
import net.raphimc.vialegacy.protocols.alpha.protocola1_2_3_1_2_3_4toa1_2_2.ServerboundPacketsa1_2_2;

public class Protocola1_2_2toa1_2_0_1_2_1_1 extends StatelessProtocol<ClientboundPacketsa1_2_0, ClientboundPacketsa1_2_2, ServerboundPacketsa1_2_0, ServerboundPacketsa1_2_2> {

    public Protocola1_2_2toa1_2_0_1_2_1_1() {
        super(ClientboundPacketsa1_2_0.class, ClientboundPacketsa1_2_2.class, ServerboundPacketsa1_2_0.class, ServerboundPacketsa1_2_2.class);
    }

    @Override
    protected void registerPackets() {
        this.registerClientbound(ClientboundPacketsa1_2_0.SPAWN_MOB, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.INT); // entity id
                map(Type.UNSIGNED_BYTE); // type id
                map(Type.INT); // x
                map(Type.INT); // y
                map(Type.INT); // z
                map(Type.BYTE); // yaw
                map(Type.BYTE); // pitch
                handler(wrapper -> {
                    if (wrapper.get(Type.UNSIGNED_BYTE, 0) == 91) {
                        wrapper.set(Type.UNSIGNED_BYTE, 0, (short) 93);
                    }
                });
            }
        });

        this.cancelServerbound(ServerboundPacketsa1_2_2.INTERACT_ENTITY);
    }

    @Override
    public void init(UserConnection userConnection) {
        userConnection.put(new PreNettySplitter(Protocola1_2_2toa1_2_0_1_2_1_1.class, ClientboundPacketsa1_2_0::getPacket));
    }

}
