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
package net.raphimc.vialegacy.protocol.classic.c0_28_30toa1_0_15.provider;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.platform.providers.Provider;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import net.raphimc.vialegacy.protocol.alpha.a1_0_15toa1_0_16_2.packet.ClientboundPacketsa1_0_15;
import net.raphimc.vialegacy.protocol.beta.b1_7_0_3tob1_8_0_1.types.Typesb1_7_0_3;
import net.raphimc.vialegacy.protocol.classic.c0_28_30toa1_0_15.Protocolc0_28_30Toa1_0_15;

public class ClassicCustomCommandProvider implements Provider {

    public boolean handleChatMessage(final UserConnection user, final String message) {
        return false;
    }

    public void sendFeedback(final UserConnection user, final String message) {
        final PacketWrapper chatMessage = PacketWrapper.create(ClientboundPacketsa1_0_15.CHAT, user);
        chatMessage.write(Typesb1_7_0_3.STRING, message); // message
        chatMessage.send(Protocolc0_28_30Toa1_0_15.class);
    }

}
