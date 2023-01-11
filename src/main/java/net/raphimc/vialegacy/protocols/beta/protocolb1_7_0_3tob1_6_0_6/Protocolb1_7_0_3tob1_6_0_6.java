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
package net.raphimc.vialegacy.protocols.beta.protocolb1_7_0_3tob1_6_0_6;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import net.raphimc.vialegacy.protocols.beta.protocolb1_8_0_1tob1_7_0_3.ClientboundPacketsb1_7;
import net.raphimc.vialegacy.protocols.beta.protocolb1_8_0_1tob1_7_0_3.ServerboundPacketsb1_7;

public class Protocolb1_7_0_3tob1_6_0_6 extends AbstractProtocol<ClientboundPacketsb1_7, ClientboundPacketsb1_7, ServerboundPacketsb1_7, ServerboundPacketsb1_7> {

    public Protocolb1_7_0_3tob1_6_0_6() {
        super(ClientboundPacketsb1_7.class, ClientboundPacketsb1_7.class, ServerboundPacketsb1_7.class, ServerboundPacketsb1_7.class);
    }

}
