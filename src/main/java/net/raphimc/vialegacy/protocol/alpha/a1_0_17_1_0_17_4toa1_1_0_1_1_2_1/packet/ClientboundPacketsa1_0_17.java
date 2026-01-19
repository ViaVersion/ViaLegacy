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
package net.raphimc.vialegacy.protocol.alpha.a1_0_17_1_0_17_4toa1_1_0_1_1_2_1.packet;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
import io.netty.buffer.ByteBuf;
import net.raphimc.vialegacy.api.splitter.PreNettyPacketType;

import java.util.function.BiConsumer;

import static net.raphimc.vialegacy.api.splitter.PreNettyTypes.readUTF;

public enum ClientboundPacketsa1_0_17 implements ClientboundPacketType, PreNettyPacketType {

    KEEP_ALIVE(0, (user, buf) -> {
    }),
    LOGIN(1, (user, buf) -> {
        buf.skipBytes(4);
        readUTF(buf);
        readUTF(buf);
    }),
    HANDSHAKE(2, (user, buf) -> readUTF(buf)),
    CHAT(3, (user, buf) -> readUTF(buf)),
    SET_TIME(4, (user, buf) -> buf.skipBytes(8)),
    MOVE_PLAYER_STATUS_ONLY(10, (user, buf) -> buf.skipBytes(1)),
    MOVE_PLAYER_POS(11, (user, buf) -> buf.skipBytes(33)),
    MOVE_PLAYER_ROT(12, (user, buf) -> buf.skipBytes(9)),
    PLAYER_POSITION(13, (user, buf) -> buf.skipBytes(41)),
    SET_CARRIED_ITEM(16, (user, buf) -> buf.skipBytes(6)),
    ADD_TO_INVENTORY(17, (user, buf) -> buf.skipBytes(5)),
    ANIMATE(18, (user, buf) -> buf.skipBytes(5)),
    ADD_PLAYER(20, (user, buf) -> {
        buf.skipBytes(4);
        readUTF(buf);
        buf.skipBytes(16);
    }),
    SPAWN_ITEM(21, (user, buf) -> buf.skipBytes(22)),
    TAKE_ITEM_ENTITY(22, (user, buf) -> buf.skipBytes(8)),
    ADD_ENTITY(23, (user, buf) -> buf.skipBytes(17)),
    ADD_MOB(24, (user, buf) -> buf.skipBytes(19)),
    REMOVE_ENTITIES(29, (user, buf) -> buf.skipBytes(4)),
    MOVE_ENTITY(30, (user, buf) -> buf.skipBytes(4)),
    MOVE_ENTITY_POS(31, (user, buf) -> buf.skipBytes(7)),
    MOVE_ENTITY_ROT(32, (user, buf) -> buf.skipBytes(6)),
    MOVE_ENTITY_POS_ROT(33, (user, buf) -> buf.skipBytes(9)),
    TELEPORT_ENTITY(34, (user, buf) -> buf.skipBytes(18)),
    PRE_CHUNK(50, (user, buf) -> buf.skipBytes(9)),
    LEVEL_CHUNK(51, (user, buf) -> {
        buf.skipBytes(13);
        int x = buf.readInt();
        for (int i = 0; i < x; i++) buf.readByte();
    }),
    CHUNK_BLOCKS_UPDATE(52, (user, buf) -> {
        buf.skipBytes(8);
        short x = buf.readShort();
        for (int i = 0; i < x; i++) buf.readShort();
        for (int i = 0; i < x; i++) buf.readByte();
        for (int i = 0; i < x; i++) buf.readByte();
    }),
    BLOCK_UPDATE(53, (user, buf) -> buf.skipBytes(11)),
    DISCONNECT(255, (user, buf) -> readUTF(buf));

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
