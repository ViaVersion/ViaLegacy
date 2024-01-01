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
package net.raphimc.vialegacy.protocols.classic.protocolc0_28_30toc0_28_30cpe;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.packet.ServerboundPacketType;
import io.netty.buffer.ByteBuf;
import net.raphimc.vialegacy.api.splitter.PreNettyPacketType;

import java.util.function.BiConsumer;

public enum ServerboundPacketsc0_30cpe implements ServerboundPacketType, PreNettyPacketType {

    LOGIN(0, (user, buf) -> buf.skipBytes(130)),
    PLAYER_BLOCK_PLACEMENT(5, (user, buf) -> buf.skipBytes(8)),
    PLAYER_POSITION_AND_ROTATION(8, (user, buf) -> buf.skipBytes(9)),
    CHAT_MESSAGE(13, (user, buf) -> buf.skipBytes(65)),
    EXTENSION_PROTOCOL_INFO(16, (user, buf) -> buf.skipBytes(66)),
    EXTENSION_PROTOCOL_ENTRY(17, (user, buf) -> buf.skipBytes(68)),
    EXT_CUSTOM_BLOCKS_SUPPORT_LEVEL(19, (user, buf) -> buf.skipBytes(1)),
    EXT_TWO_WAY_PING(43, (user, buf) -> buf.skipBytes(3));

    private static final ServerboundPacketsc0_30cpe[] REGISTRY = new ServerboundPacketsc0_30cpe[256];

    static {
        for (ServerboundPacketsc0_30cpe packet : values()) {
            REGISTRY[packet.id] = packet;
        }
    }

    public static ServerboundPacketsc0_30cpe getPacket(final int id) {
        return REGISTRY[id];
    }

    private final int id;
    private final BiConsumer<UserConnection, ByteBuf> packetReader;

    ServerboundPacketsc0_30cpe(final int id, final BiConsumer<UserConnection, ByteBuf> packetReader) {
        this.id = id;
        this.packetReader = packetReader;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return name();
    }

    @Override
    public BiConsumer<UserConnection, ByteBuf> getPacketReader() {
        return this.packetReader;
    }

}
