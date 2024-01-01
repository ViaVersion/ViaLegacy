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
package net.raphimc.vialegacy.api.protocol;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.protocol.packet.*;

public abstract class StatelessProtocol<CU extends ClientboundPacketType, CM extends ClientboundPacketType, SM extends ServerboundPacketType, SU extends ServerboundPacketType> extends AbstractProtocol<CU, CM, SM, SU> {

    public StatelessProtocol(final Class<CU> unmappedClientboundPacketType, final Class<CM> mappedClientboundPacketType, final Class<SM> mappedServerboundPacketType, final Class<SU> unmappedServerboundPacketType) {
        super(unmappedClientboundPacketType, mappedClientboundPacketType, mappedServerboundPacketType, unmappedServerboundPacketType);
    }

    @Override
    public void transform(Direction direction, State state, PacketWrapper packetWrapper) throws Exception {
        super.transform(direction, direction == Direction.SERVERBOUND ? state : State.PLAY, packetWrapper);
    }

}
