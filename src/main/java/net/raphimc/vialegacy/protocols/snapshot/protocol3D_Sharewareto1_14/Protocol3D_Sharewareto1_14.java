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
package net.raphimc.vialegacy.protocols.snapshot.protocol3D_Sharewareto1_14;

import com.viaversion.viabackwards.api.BackwardsProtocol;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.ClientboundPackets1_14;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.ServerboundPackets1_14;
import net.raphimc.vialegacy.protocols.snapshot.protocol1_14to3D_Shareware.ClientboundPackets3D_Shareware;
import net.raphimc.vialegacy.protocols.snapshot.protocol1_14to3D_Shareware.ServerboundPackets3D_Shareware;

public class Protocol3D_Sharewareto1_14 extends BackwardsProtocol<ClientboundPackets1_14, ClientboundPackets3D_Shareware, ServerboundPackets1_14, ServerboundPackets3D_Shareware> {

    public Protocol3D_Sharewareto1_14() {
        super(ClientboundPackets1_14.class, ClientboundPackets3D_Shareware.class, ServerboundPackets1_14.class, ServerboundPackets3D_Shareware.class);
    }

    @Override
    protected void registerPackets() {
        this.cancelClientbound(ClientboundPackets1_14.UPDATE_VIEW_POSITION);
        this.cancelClientbound(ClientboundPackets1_14.ACKNOWLEDGE_PLAYER_DIGGING);
    }

}
