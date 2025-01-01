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
package net.raphimc.vialegacy.protocol.beta.b1_6_0_6tob1_7_0_3;

import net.raphimc.vialegacy.api.protocol.StatelessProtocol;
import net.raphimc.vialegacy.protocol.beta.b1_7_0_3tob1_8_0_1.packet.ClientboundPacketsb1_7;
import net.raphimc.vialegacy.protocol.beta.b1_7_0_3tob1_8_0_1.packet.ServerboundPacketsb1_7;

public class Protocolb1_6_0_6Tob1_7_0_3 extends StatelessProtocol<ClientboundPacketsb1_7, ClientboundPacketsb1_7, ServerboundPacketsb1_7, ServerboundPacketsb1_7> {

    public Protocolb1_6_0_6Tob1_7_0_3() {
        super(ClientboundPacketsb1_7.class, ClientboundPacketsb1_7.class, ServerboundPacketsb1_7.class, ServerboundPacketsb1_7.class);
    }

}
