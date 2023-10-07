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
package net.raphimc.vialegacy.protocols.alpha.protocola1_1_0_1_1_2_1toa1_0_17_1_0_17_4;

import com.viaversion.viaversion.api.connection.UserConnection;
import net.raphimc.vialegacy.api.protocol.StatelessProtocol;
import net.raphimc.vialegacy.api.splitter.PreNettySplitter;
import net.raphimc.vialegacy.protocols.alpha.protocola1_2_0_1_2_1_1toa1_1_0_1_1_2_1.ClientboundPacketsa1_1_0;
import net.raphimc.vialegacy.protocols.alpha.protocola1_2_0_1_2_1_1toa1_1_0_1_1_2_1.ServerboundPacketsa1_1_0;

public class Protocola1_1_0_1_1_2_1toa1_0_17_1_0_17_4 extends StatelessProtocol<ClientboundPacketsa1_0_17, ClientboundPacketsa1_1_0, ServerboundPacketsa1_0_17, ServerboundPacketsa1_1_0> {

    public Protocola1_1_0_1_1_2_1toa1_0_17_1_0_17_4() {
        super(ClientboundPacketsa1_0_17.class, ClientboundPacketsa1_1_0.class, ServerboundPacketsa1_0_17.class, ServerboundPacketsa1_1_0.class);
    }

    @Override
    protected void registerPackets() {
        this.cancelServerbound(ServerboundPacketsa1_1_0.COMPLEX_ENTITY);
        this.cancelServerbound(ServerboundPacketsa1_1_0.PLAYER_INVENTORY);
    }

    @Override
    public void init(UserConnection userConnection) {
        userConnection.put(new PreNettySplitter(userConnection, Protocola1_1_0_1_1_2_1toa1_0_17_1_0_17_4.class, ClientboundPacketsa1_0_17::getPacket));
    }

}
