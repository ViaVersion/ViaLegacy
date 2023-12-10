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
package net.raphimc.vialegacy.protocols.alpha.protocola1_2_0_1_2_1_1toa1_1_0_1_1_2_1;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.packet.ServerboundPacketType;
import io.netty.buffer.ByteBuf;
import net.raphimc.vialegacy.api.splitter.PreNettyPacketType;

import java.util.function.BiConsumer;

import static net.raphimc.vialegacy.api.splitter.PreNettyTypes.readItemStackb1_2;
import static net.raphimc.vialegacy.api.splitter.PreNettyTypes.readUTF;

public enum ServerboundPacketsa1_1_0 implements ServerboundPacketType, PreNettyPacketType {

    KEEP_ALIVE(0, (user, buf) -> {
    }),
    LOGIN(1, (user, buf) -> {
        buf.skipBytes(4);
        readUTF(buf);
        readUTF(buf);
    }),
    HANDSHAKE(2, (user, buf) -> readUTF(buf)),
    CHAT_MESSAGE(3, (user, buf) -> readUTF(buf)),
    PLAYER_INVENTORY(5, (user, buf) -> {
        buf.skipBytes(4);
        int x = buf.readShort();
        for (int i = 0; i < x; i++) readItemStackb1_2(buf);
    }),
    PLAYER_MOVEMENT(10, (user, buf) -> buf.skipBytes(1)),
    PLAYER_POSITION(11, (user, buf) -> buf.skipBytes(33)),
    PLAYER_ROTATION(12, (user, buf) -> buf.skipBytes(9)),
    PLAYER_POSITION_AND_ROTATION(13, (user, buf) -> buf.skipBytes(41)),
    PLAYER_DIGGING(14, (user, buf) -> buf.skipBytes(11)),
    PLAYER_BLOCK_PLACEMENT(15, (user, buf) -> buf.skipBytes(12)),
    HELD_ITEM_CHANGE(16, (user, buf) -> buf.skipBytes(6)),
    ANIMATION(18, (user, buf) -> buf.skipBytes(5)),
    SPAWN_ITEM(21, (user, buf) -> buf.skipBytes(22)),
    COMPLEX_ENTITY(59, (user, buf) -> {
        buf.skipBytes(10);
        int x = buf.readUnsignedShort();
        for (int i = 0; i < x; i++) buf.readByte();
    }),
    DISCONNECT(255, (user, buf) -> readUTF(buf));

    private static final ServerboundPacketsa1_1_0[] REGISTRY = new ServerboundPacketsa1_1_0[256];

    static {
        for (ServerboundPacketsa1_1_0 packet : values()) {
            REGISTRY[packet.id] = packet;
        }
    }

    public static ServerboundPacketsa1_1_0 getPacket(final int id) {
        return REGISTRY[id];
    }

    private final int id;
    private final BiConsumer<UserConnection, ByteBuf> packetReader;

    ServerboundPacketsa1_1_0(final int id, final BiConsumer<UserConnection, ByteBuf> packetReader) {
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
