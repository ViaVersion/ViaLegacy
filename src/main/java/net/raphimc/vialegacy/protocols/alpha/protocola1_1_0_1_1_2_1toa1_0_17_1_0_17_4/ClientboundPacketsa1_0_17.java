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
package net.raphimc.vialegacy.protocols.alpha.protocola1_1_0_1_1_2_1toa1_0_17_1_0_17_4;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
import io.netty.buffer.ByteBuf;
import net.raphimc.vialegacy.api.splitter.PreNettyPacketType;

import java.util.function.BiConsumer;

import static net.raphimc.vialegacy.api.splitter.PreNettyTypes.readUTF;

public enum ClientboundPacketsa1_0_17 implements ClientboundPacketType, PreNettyPacketType {

    KEEP_ALIVE(0, (user, buf) -> {
    }),
    JOIN_GAME(1, (user, buf) -> {
        buf.readInt();
        readUTF(buf);
        readUTF(buf);
    }),
    HANDSHAKE(2, (user, buf) -> {
        readUTF(buf);
    }),
    CHAT_MESSAGE(3, (user, buf) -> {
        readUTF(buf);
    }),
    TIME_UPDATE(4, (user, buf) -> {
        buf.readLong();
    }),
    PLAYER_POSITION_ONLY_ONGROUND(10, (user, buf) -> {
        buf.readUnsignedByte();
    }),
    PLAYER_POSITION_ONLY_POSITION(11, (user, buf) -> {
        buf.readDouble();
        buf.readDouble();
        buf.readDouble();
        buf.readDouble();
        buf.readUnsignedByte();
    }),
    PLAYER_POSITION_ONLY_LOOK(12, (user, buf) -> {
        buf.readFloat();
        buf.readFloat();
        buf.readUnsignedByte();
    }),
    PLAYER_POSITION(13, (user, buf) -> {
        buf.readDouble();
        buf.readDouble();
        buf.readDouble();
        buf.readDouble();
        buf.readFloat();
        buf.readFloat();
        buf.readUnsignedByte();
    }),
    HELD_ITEM_CHANGE(16, (user, buf) -> {
        buf.readInt();
        buf.readShort();
    }),
    ADD_TO_INVENTORY(17, (user, buf) -> {
        buf.readShort();
        buf.readByte();
        buf.readShort();
    }),
    ENTITY_ANIMATION(18, (user, buf) -> {
        buf.readInt();
        buf.readByte();
    }),
    SPAWN_PLAYER(20, (user, buf) -> {
        buf.readInt();
        readUTF(buf);
        buf.readInt();
        buf.readInt();
        buf.readInt();
        buf.readByte();
        buf.readByte();
        buf.readShort();
    }),
    SPAWN_ITEM(21, (user, buf) -> {
        buf.readInt();
        buf.readShort();
        buf.readByte();
        buf.readInt();
        buf.readInt();
        buf.readInt();
        buf.readByte();
        buf.readByte();
        buf.readByte();
    }),
    COLLECT_ITEM(22, (user, buf) -> {
        buf.readInt();
        buf.readInt();
    }),
    SPAWN_ENTITY(23, (user, buf) -> {
        buf.readInt();
        buf.readByte();
        buf.readInt();
        buf.readInt();
        buf.readInt();
    }),
    SPAWN_MOB(24, (user, buf) -> {
        buf.readInt();
        buf.readByte();
        buf.readInt();
        buf.readInt();
        buf.readInt();
        buf.readByte();
        buf.readByte();
    }),
    DESTROY_ENTITIES(29, (user, buf) -> {
        buf.readInt();
    }),
    ENTITY_MOVEMENT(30, (user, buf) -> {
        buf.readInt();
    }),
    ENTITY_POSITION(31, (user, buf) -> {
        buf.readInt();
        buf.readByte();
        buf.readByte();
        buf.readByte();
    }),
    ENTITY_ROTATION(32, (user, buf) -> {
        buf.readInt();
        buf.readByte();
        buf.readByte();
    }),
    ENTITY_POSITION_AND_ROTATION(33, (user, buf) -> {
        buf.readInt();
        buf.readByte();
        buf.readByte();
        buf.readByte();
        buf.readByte();
        buf.readByte();
    }),
    ENTITY_TELEPORT(34, (user, buf) -> {
        buf.readInt();
        buf.readInt();
        buf.readInt();
        buf.readInt();
        buf.readByte();
        buf.readByte();
    }),
    PRE_CHUNK(50, (user, buf) -> {
        buf.readInt();
        buf.readInt();
        buf.readByte();
    }),
    CHUNK_DATA(51, (user, buf) -> {
        buf.readInt();
        buf.readShort();
        buf.readInt();
        buf.readByte();
        buf.readByte();
        buf.readByte();
        int x = buf.readInt();
        for (int i = 0; i < x; i++) buf.readByte();
    }),
    MULTI_BLOCK_CHANGE(52, (user, buf) -> {
        buf.readInt();
        buf.readInt();
        short x = buf.readShort();
        for (int i = 0; i < x; i++) buf.readShort();
        for (int i = 0; i < x; i++) buf.readByte();
        for (int i = 0; i < x; i++) buf.readByte();
    }),
    BLOCK_CHANGE(53, (user, buf) -> {
        buf.readInt();
        buf.readUnsignedByte();
        buf.readInt();
        buf.readUnsignedByte();
        buf.readUnsignedByte();
    }),
    DISCONNECT(255, (user, buf) -> {
        readUTF(buf);
    });

    private static final ClientboundPacketsa1_0_17[] REGISTRY = new ClientboundPacketsa1_0_17[256];

    static {
        for (ClientboundPacketsa1_0_17 packet : values()) {
            REGISTRY[packet.id] = packet;
        }
    }

    public static ClientboundPacketsa1_0_17 getPacket(final int id) {
        return REGISTRY[id];
    }

    private final int id;
    private final BiConsumer<UserConnection, ByteBuf> packetReader;

    ClientboundPacketsa1_0_17(final int id, final BiConsumer<UserConnection, ByteBuf> packetReader) {
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
