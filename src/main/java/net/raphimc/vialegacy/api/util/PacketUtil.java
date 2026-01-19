/*
 * This file is part of ViaLegacy - https://github.com/RaphiMC/ViaLegacy
 * Copyright (C) 2020-2026 RK_01/RaphiMC and contributors
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
package net.raphimc.vialegacy.api.util;

import com.viaversion.viaversion.api.protocol.packet.PacketType;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.protocol.packet.PacketWrapperImpl;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class PacketUtil {

    public static int calculateLength(final PacketWrapper wrapper) {
        final PacketType packetType = wrapper.getPacketType();
        wrapper.setPacketType(null);

        final ByteBuf lengthBuffer = Unpooled.buffer();
        if (wrapper instanceof PacketWrapperImpl impl && impl.getInputBuffer() != null) {
            impl.getInputBuffer().markReaderIndex();
        }

        wrapper.writeToBuffer(lengthBuffer);
        if (wrapper instanceof PacketWrapperImpl impl && impl.getInputBuffer() != null) {
            impl.getInputBuffer().resetReaderIndex();
        }

        final int length = lengthBuffer.readableBytes();
        lengthBuffer.release();

        wrapper.setPacketType(packetType);
        return length;
    }

}
