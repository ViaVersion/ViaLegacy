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
package net.raphimc.vialegacy.protocols.alpha.protocola1_2_3_1_2_3_4toa1_2_2;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
import io.netty.buffer.ByteBuf;
import net.raphimc.vialegacy.api.splitter.PreNettyPacketType;
import net.raphimc.vialegacy.api.splitter.PreNettyTypes;

import java.util.function.BiConsumer;

public enum ClientboundPacketsa1_2_2 implements ClientboundPacketType, PreNettyPacketType {

    KEEP_ALIVE(0, (user, buf) -> {
    }),
    JOIN_GAME(1, (user, buf) -> {
        buf.skipBytes(4);
        PreNettyTypes.readUTF(buf);
        PreNettyTypes.readUTF(buf);
        buf.skipBytes(9);
    }),
    HANDSHAKE(2, (user, buf) -> PreNettyTypes.readUTF(buf)),
    CHAT_MESSAGE(3, (user, buf) -> PreNettyTypes.readUTF(buf)),
    TIME_UPDATE(4, (user, buf) -> buf.skipBytes(8)),
    PLAYER_INVENTORY(5, (user, buf) -> {
        buf.skipBytes(4);
        int x = buf.readShort();
        for (int i = 0; i < x; i++) PreNettyTypes.readItemStackb1_2(buf);
    }),
    SPAWN_POSITION(6, (user, buf) -> buf.skipBytes(12)),
    PLAYER_POSITION_ONLY_ONGROUND(10, (user, buf) -> buf.skipBytes(1)),
    PLAYER_POSITION_ONLY_POSITION(11, (user, buf) -> buf.skipBytes(33)),
    PLAYER_POSITION_ONLY_LOOK(12, (user, buf) -> buf.skipBytes(9)),
    PLAYER_POSITION(13, (user, buf) -> buf.skipBytes(41)),
    HELD_ITEM_CHANGE(16, (user, buf) -> buf.skipBytes(6)),
    ADD_TO_INVENTORY(17, (user, buf) -> buf.skipBytes(5)),
    ENTITY_ANIMATION(18, (user, buf) -> buf.skipBytes(5)),
    SPAWN_PLAYER(20, (user, buf) -> {
        buf.skipBytes(4);
        PreNettyTypes.readUTF(buf);
        buf.skipBytes(16);
    }),
    SPAWN_ITEM(21, (user, buf) -> buf.skipBytes(22)),
    COLLECT_ITEM(22, (user, buf) -> buf.skipBytes(8)),
    SPAWN_ENTITY(23, (user, buf) -> buf.skipBytes(17)),
    SPAWN_MOB(24, (user, buf) -> buf.skipBytes(19)),
    ENTITY_VELOCITY(28, (user, buf) -> buf.skipBytes(10)),
    DESTROY_ENTITIES(29, (user, buf) -> buf.skipBytes(4)),
    ENTITY_MOVEMENT(30, (user, buf) -> buf.skipBytes(4)),
    ENTITY_POSITION(31, (user, buf) -> buf.skipBytes(7)),
    ENTITY_ROTATION(32, (user, buf) -> buf.skipBytes(6)),
    ENTITY_POSITION_AND_ROTATION(33, (user, buf) -> buf.skipBytes(9)),
    ENTITY_TELEPORT(34, (user, buf) -> buf.skipBytes(18)),
    ATTACH_ENTITY(39, (user, buf) -> buf.skipBytes(8)),
    PRE_CHUNK(50, (user, buf) -> buf.skipBytes(9)),
    CHUNK_DATA(51, (user, buf) -> {
        buf.skipBytes(13);
        int x = buf.readInt();
        for (int i = 0; i < x; i++) buf.readByte();
    }),
    MULTI_BLOCK_CHANGE(52, (user, buf) -> {
        buf.skipBytes(8);
        short x = buf.readShort();
        for (int i = 0; i < x; i++) buf.readShort();
        for (int i = 0; i < x; i++) buf.readByte();
        for (int i = 0; i < x; i++) buf.readByte();
    }),
    BLOCK_CHANGE(53, (user, buf) -> buf.skipBytes(11)),
    COMPLEX_ENTITY(59, (user, buf) -> {
        buf.skipBytes(10);
        int x = buf.readUnsignedShort();
        for (int i = 0; i < x; i++) buf.readByte();
    }),
    DISCONNECT(255, (user, buf) -> PreNettyTypes.readUTF(buf));

    private static final ClientboundPacketsa1_2_2[] REGISTRY = new ClientboundPacketsa1_2_2[256];

    static {
        for (ClientboundPacketsa1_2_2 packet : values()) {
            REGISTRY[packet.id] = packet;
        }
    }

    public static ClientboundPacketsa1_2_2 getPacket(final int id) {
        return REGISTRY[id];
    }

    private final int id;
    private final BiConsumer<UserConnection, ByteBuf> packetReader;

    ClientboundPacketsa1_2_2(final int id, final BiConsumer<UserConnection, ByteBuf> packetReader) {
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
