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
package net.raphimc.vialegacy.protocols.beta.protocolb1_1_2tob1_0_1_1;

import net.raphimc.vialegacy.api.protocol.StatelessProtocol;
import net.raphimc.vialegacy.protocols.beta.protocolb1_2_0_2tob1_1_2.ClientboundPacketsb1_1;
import net.raphimc.vialegacy.protocols.beta.protocolb1_2_0_2tob1_1_2.ServerboundPacketsb1_1;

public class Protocolb1_1_2tob1_0_1_1 extends StatelessProtocol<ClientboundPacketsb1_1, ClientboundPacketsb1_1, ServerboundPacketsb1_1, ServerboundPacketsb1_1> {

    public Protocolb1_1_2tob1_0_1_1() {
        super(ClientboundPacketsb1_1.class, ClientboundPacketsb1_1.class, ServerboundPacketsb1_1.class, ServerboundPacketsb1_1.class);
    }

}
