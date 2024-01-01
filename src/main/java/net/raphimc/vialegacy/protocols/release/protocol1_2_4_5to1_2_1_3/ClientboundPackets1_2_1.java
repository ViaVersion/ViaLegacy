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
package net.raphimc.vialegacy.protocols.release.protocol1_2_4_5to1_2_1_3;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
import io.netty.buffer.ByteBuf;
import net.raphimc.vialegacy.api.splitter.PreNettyPacketType;

import java.util.function.BiConsumer;

import static net.raphimc.vialegacy.api.splitter.PreNettyTypes.*;

public enum ClientboundPackets1_2_1 implements ClientboundPacketType, PreNettyPacketType {

    KEEP_ALIVE(0, (user, buf) -> buf.skipBytes(4)),
    JOIN_GAME(1, (user, buf) -> {
        buf.skipBytes(4);
        readString(buf);
        readString(buf);
        buf.skipBytes(11);
    }),
    HANDSHAKE(2, (user, buf) -> readString(buf)),
    CHAT_MESSAGE(3, (user, buf) -> readString(buf)),
    TIME_UPDATE(4, (user, buf) -> buf.skipBytes(8)),
    ENTITY_EQUIPMENT(5, (user, buf) -> buf.skipBytes(10)),
    SPAWN_POSITION(6, (user, buf) -> buf.skipBytes(12)),
    UPDATE_HEALTH(8, (user, buf) -> buf.skipBytes(8)),
    RESPAWN(9, (user, buf) -> {
        buf.skipBytes(8);
        readString(buf);
    }),
    PLAYER_POSITION_ONLY_ONGROUND(10, (user, buf) -> buf.skipBytes(1)),
    PLAYER_POSITION_ONLY_POSITION(11, (user, buf) -> buf.skipBytes(33)),
    PLAYER_POSITION_ONLY_LOOK(12, (user, buf) -> buf.skipBytes(9)),
    PLAYER_POSITION(13, (user, buf) -> buf.skipBytes(41)),
    USE_BED(17, (user, buf) -> buf.skipBytes(14)),
    ENTITY_ANIMATION(18, (user, buf) -> buf.skipBytes(5)),
    SPAWN_PLAYER(20, (user, buf) -> {
        buf.skipBytes(4);
        readString(buf);
        buf.skipBytes(16);
    }),
    SPAWN_ITEM(21, (user, buf) -> buf.skipBytes(24)),
    COLLECT_ITEM(22, (user, buf) -> buf.skipBytes(8)),
    SPAWN_ENTITY(23, (user, buf) -> {
        buf.skipBytes(17);
        int i = buf.readInt();
        if (i > 0) {
            buf.skipBytes(6);
        }
    }),
    SPAWN_MOB(24, (user, buf) -> {
        buf.skipBytes(20);
        readEntityMetadatab1_5(buf);
    }),
    SPAWN_PAINTING(25, (user, buf) -> {
        buf.skipBytes(4);
        readString(buf);
        buf.skipBytes(16);
    }),
    SPAWN_EXPERIENCE_ORB(26, (user, buf) -> buf.skipBytes(18)),
    ENTITY_VELOCITY(28, (user, buf) -> buf.skipBytes(10)),
    DESTROY_ENTITIES(29, (user, buf) -> buf.skipBytes(4)),
    ENTITY_MOVEMENT(30, (user, buf) -> buf.skipBytes(4)),
    ENTITY_POSITION(31, (user, buf) -> buf.skipBytes(7)),
    ENTITY_ROTATION(32, (user, buf) -> buf.skipBytes(6)),
    ENTITY_POSITION_AND_ROTATION(33, (user, buf) -> buf.skipBytes(9)),
    ENTITY_TELEPORT(34, (user, buf) -> buf.skipBytes(18)),
    ENTITY_HEAD_LOOK(35, (user, buf) -> buf.skipBytes(5)),
    ENTITY_STATUS(38, (user, buf) -> buf.skipBytes(5)),
    ATTACH_ENTITY(39, (user, buf) -> buf.skipBytes(8)),
    ENTITY_METADATA(40, (user, buf) -> {
        buf.skipBytes(4);
        readEntityMetadatab1_5(buf);
    }),
    ENTITY_EFFECT(41, (user, buf) -> buf.skipBytes(8)),
    REMOVE_ENTITY_EFFECT(42, (user, buf) -> buf.skipBytes(5)),
    SET_EXPERIENCE(43, (user, buf) -> buf.skipBytes(8)),
    PRE_CHUNK(50, (user, buf) -> buf.skipBytes(9)),
    CHUNK_DATA(51, (user, buf) -> {
        buf.skipBytes(13);
        int x = buf.readInt();
        buf.skipBytes(4);
        for (int i = 0; i < x; i++) buf.readByte();
    }),
    MULTI_BLOCK_CHANGE(52, (user, buf) -> {
        buf.skipBytes(10);
        int x = buf.readInt();
        for (int i = 0; i < x; i++) buf.readByte();
    }),
    BLOCK_CHANGE(53, (user, buf) -> buf.skipBytes(11)),
    BLOCK_ACTION(54, (user, buf) -> buf.skipBytes(12)),
    EXPLOSION(60, (user, buf) -> {
        buf.skipBytes(28);
        int x = buf.readInt();
        for (int i = 0; i < x; i++) {
            buf.skipBytes(3);
        }
    }),
    EFFECT(61, (user, buf) -> buf.skipBytes(17)),
    GAME_EVENT(70, (user, buf) -> buf.skipBytes(2)),
    SPAWN_GLOBAL_ENTITY(71, (user, buf) -> buf.skipBytes(17)),
    OPEN_WINDOW(100, (user, buf) -> {
        buf.skipBytes(2);
        readString(buf);
        buf.skipBytes(1);
    }),
    CLOSE_WINDOW(101, (user, buf) -> buf.skipBytes(1)),
    SET_SLOT(103, (user, buf) -> {
        buf.skipBytes(3);
        readItemStack1_0(buf);
    }),
    WINDOW_ITEMS(104, (user, buf) -> {
        buf.skipBytes(1);
        int x = buf.readShort();
        for (int i = 0; i < x; i++) readItemStack1_0(buf);
    }),
    WINDOW_PROPERTY(105, (user, buf) -> buf.skipBytes(5)),
    WINDOW_CONFIRMATION(106, (user, buf) -> buf.skipBytes(4)),
    CREATIVE_INVENTORY_ACTION(107, (user, buf) -> {
        buf.skipBytes(2);
        readItemStack1_0(buf);
    }),
    UPDATE_SIGN(130, (user, buf) -> {
        buf.skipBytes(10);
        readString(buf);
        readString(buf);
        readString(buf);
        readString(buf);
    }),
    MAP_DATA(131, (user, buf) -> {
        buf.skipBytes(4);
        short x = buf.readUnsignedByte();
        for (int i = 0; i < x; i++) buf.readByte();
    }),
    BLOCK_ENTITY_DATA(132, (user, buf) -> buf.skipBytes(23)),
    STATISTICS(200, (user, buf) -> buf.skipBytes(5)),
    PLAYER_INFO(201, (user, buf) -> {
        readString(buf);
        buf.skipBytes(3);
    }),
    PLUGIN_MESSAGE(250, (user, buf) -> {
        readString(buf);
        short s = buf.readShort();
        for (int i = 0; i < s; i++) buf.readByte();
    }),
    DISCONNECT(255, (user, buf) -> readString(buf));

    private static final ClientboundPackets1_2_1[] REGISTRY = new ClientboundPackets1_2_1[256];

    static {
        for (ClientboundPackets1_2_1 packet : values()) {
            REGISTRY[packet.id] = packet;
        }
    }

    public static ClientboundPackets1_2_1 getPacket(final int id) {
        return REGISTRY[id];
    }

    private final int id;
    private final BiConsumer<UserConnection, ByteBuf> packetReader;

    ClientboundPackets1_2_1(final int id, final BiConsumer<UserConnection, ByteBuf> packetReader) {
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
