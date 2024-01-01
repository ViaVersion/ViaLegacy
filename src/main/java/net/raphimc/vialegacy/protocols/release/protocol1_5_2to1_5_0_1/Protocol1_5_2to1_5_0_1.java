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
package net.raphimc.vialegacy.protocols.release.protocol1_5_2to1_5_0_1;

import net.raphimc.vialegacy.api.protocol.StatelessProtocol;
import net.raphimc.vialegacy.protocols.release.protocol1_6_1to1_5_2.ClientboundPackets1_5_2;
import net.raphimc.vialegacy.protocols.release.protocol1_6_1to1_5_2.ServerboundPackets1_5_2;

public class Protocol1_5_2to1_5_0_1 extends StatelessProtocol<ClientboundPackets1_5_2, ClientboundPackets1_5_2, ServerboundPackets1_5_2, ServerboundPackets1_5_2> {

    public Protocol1_5_2to1_5_0_1() {
        super(ClientboundPackets1_5_2.class, ClientboundPackets1_5_2.class, ServerboundPackets1_5_2.class, ServerboundPackets1_5_2.class);
    }

}
