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
package net.raphimc.vialegacy.protocols.classic.protocola1_0_15toc0_28_30;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.packet.ServerboundPacketType;
import io.netty.buffer.ByteBuf;
import net.raphimc.vialegacy.api.splitter.PreNettyPacketType;

import java.util.function.BiConsumer;

import static net.raphimc.vialegacy.api.splitter.PreNettyTypes.readString64;

public enum ServerboundPacketsc0_28 implements ServerboundPacketType, PreNettyPacketType {

    LOGIN(0, (user, buf) -> {
        buf.readByte();
        readString64(buf);
        readString64(buf);
        buf.readByte();
    }),
    PLAYER_BLOCK_PLACEMENT(5, (user, buf) -> {
        buf.readShort();
        buf.readShort();
        buf.readShort();
        buf.readByte();
        buf.readByte();
    }),
    PLAYER_POSITION_AND_ROTATION(8, (user, buf) -> {
        buf.readByte();
        buf.readShort();
        buf.readShort();
        buf.readShort();
        buf.readByte();
        buf.readByte();
    }),
    CHAT_MESSAGE(13, (user, buf) -> {
        buf.readByte();
        readString64(buf);
    });

    private static final ServerboundPacketsc0_28[] REGISTRY = new ServerboundPacketsc0_28[256];

    static {
        for (ServerboundPacketsc0_28 packet : values()) {
            REGISTRY[packet.id] = packet;
        }
    }

    public static ServerboundPacketsc0_28 getPacket(final int id) {
        return REGISTRY[id];
    }

    private final int id;
    private final BiConsumer<UserConnection, ByteBuf> packetReader;

    ServerboundPacketsc0_28(final int id, final BiConsumer<UserConnection, ByteBuf> packetReader) {
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
