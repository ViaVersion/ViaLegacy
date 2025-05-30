/*
 * This file is part of ViaLegacy - https://github.com/RaphiMC/ViaLegacy
 * Copyright (C) 2020-2025 RK_01/RaphiMC and contributors
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
package net.raphimc.vialegacy.protocol.release.r1_6_2tor1_6_4;

import net.raphimc.vialegacy.api.protocol.StatelessProtocol;
import net.raphimc.vialegacy.protocol.release.r1_6_4tor1_7_2_5.packet.ClientboundPackets1_6_4;
import net.raphimc.vialegacy.protocol.release.r1_6_4tor1_7_2_5.packet.ServerboundPackets1_6_4;

public class Protocolr1_6_2Tor1_6_4 extends StatelessProtocol<ClientboundPackets1_6_4, ClientboundPackets1_6_4, ServerboundPackets1_6_4, ServerboundPackets1_6_4> {

    public Protocolr1_6_2Tor1_6_4() {
        super(ClientboundPackets1_6_4.class, ClientboundPackets1_6_4.class, ServerboundPackets1_6_4.class, ServerboundPackets1_6_4.class);
    }

}
