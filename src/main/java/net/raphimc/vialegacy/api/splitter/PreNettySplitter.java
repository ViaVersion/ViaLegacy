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
package net.raphimc.vialegacy.api.splitter;

import com.viaversion.viaversion.api.connection.StorableObject;
import com.viaversion.viaversion.api.protocol.Protocol;

import java.util.function.IntFunction;

public class PreNettySplitter implements StorableObject {

    private final IntFunction<PreNettyPacketType> packetTypeSupplier;
    private final Class<? extends Protocol<?, ?, ?, ?>> protocolClass;

    public PreNettySplitter(Class<? extends Protocol<?, ?, ?, ?>> protocolClass, IntFunction<PreNettyPacketType> packetTypeSupplier) {
        this.protocolClass = protocolClass;
        this.packetTypeSupplier = packetTypeSupplier;
    }

    public PreNettyPacketType getPacketType(final int packetId) {
        return this.packetTypeSupplier.apply(packetId);
    }

    public String getProtocolName() {
        return this.protocolClass.getSimpleName();
    }

}
