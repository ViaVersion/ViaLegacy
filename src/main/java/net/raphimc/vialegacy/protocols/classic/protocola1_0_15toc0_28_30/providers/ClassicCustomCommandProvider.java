/*
 * This file is part of ViaProtocolHack - https://github.com/RaphiMC/ViaProtocolHack
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
package net.raphimc.vialegacy.protocols.classic.protocola1_0_15toc0_28_30.providers;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.platform.providers.Provider;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import net.raphimc.vialegacy.protocols.alpha.protocola1_0_16_2toa1_0_15.ClientboundPacketsa1_0_15;
import net.raphimc.vialegacy.protocols.beta.protocolb1_8_0_1tob1_7_0_3.types.Typesb1_7_0_3;
import net.raphimc.vialegacy.protocols.classic.protocola1_0_15toc0_28_30.Protocola1_0_15toc0_30;

public class ClassicCustomCommandProvider implements Provider {

    public boolean handleChatMessage(final UserConnection user, final String message) {
        return false;
    }

    public void sendFeedback(final UserConnection user, final String message) throws Exception {
        final PacketWrapper chatMessage = PacketWrapper.create(ClientboundPacketsa1_0_15.CHAT_MESSAGE, user);
        chatMessage.write(Typesb1_7_0_3.STRING, message); // message
        chatMessage.send(Protocola1_0_15toc0_30.class);
    }

}
