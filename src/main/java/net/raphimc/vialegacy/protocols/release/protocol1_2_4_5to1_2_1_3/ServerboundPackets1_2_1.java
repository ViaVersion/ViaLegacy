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
import com.viaversion.viaversion.api.protocol.packet.ServerboundPacketType;
import io.netty.buffer.ByteBuf;
import net.raphimc.vialegacy.api.splitter.PreNettyPacketType;

import java.util.function.BiConsumer;

import static net.raphimc.vialegacy.api.splitter.PreNettyTypes.readItemStack1_0;
import static net.raphimc.vialegacy.api.splitter.PreNettyTypes.readString;

public enum ServerboundPackets1_2_1 implements ServerboundPacketType, PreNettyPacketType {

    KEEP_ALIVE(0, (user, buf) -> buf.skipBytes(4)),
    LOGIN(1, (user, buf) -> {
        buf.skipBytes(4);
        readString(buf);
        readString(buf);
        buf.skipBytes(11);
    }),
    HANDSHAKE(2, (user, buf) -> readString(buf)),
    CHAT_MESSAGE(3, (user, buf) -> readString(buf)),
    INTERACT_ENTITY(7, (user, buf) -> buf.skipBytes(9)),
    RESPAWN(9, (user, buf) -> {
        buf.skipBytes(8);
        readString(buf);
    }),
    PLAYER_MOVEMENT(10, (user, buf) -> buf.skipBytes(1)),
    PLAYER_POSITION(11, (user, buf) -> buf.skipBytes(33)),
    PLAYER_ROTATION(12, (user, buf) -> buf.skipBytes(9)),
    PLAYER_POSITION_AND_ROTATION(13, (user, buf) -> buf.skipBytes(41)),
    PLAYER_DIGGING(14, (user, buf) -> buf.skipBytes(11)),
    PLAYER_BLOCK_PLACEMENT(15, (user, buf) -> {
        buf.skipBytes(10);
        readItemStack1_0(buf);
    }),
    HELD_ITEM_CHANGE(16, (user, buf) -> buf.skipBytes(2)),
    ANIMATION(18, (user, buf) -> buf.skipBytes(5)),
    ENTITY_ACTION(19, (user, buf) -> buf.skipBytes(5)),
    CLOSE_WINDOW(101, (user, buf) -> buf.skipBytes(1)),
    CLICK_WINDOW(102, (user, buf) -> {
        buf.skipBytes(7);
        readItemStack1_0(buf);
    }),
    WINDOW_CONFIRMATION(106, (user, buf) -> buf.skipBytes(4)),
    CREATIVE_INVENTORY_ACTION(107, (user, buf) -> {
        buf.skipBytes(2);
        readItemStack1_0(buf);
    }),
    CLICK_WINDOW_BUTTON(108, (user, buf) -> buf.skipBytes(2)),
    UPDATE_SIGN(130, (user, buf) -> {
        buf.skipBytes(10);
        readString(buf);
        readString(buf);
        readString(buf);
        readString(buf);
    }),
    PLUGIN_MESSAGE(250, (user, buf) -> {
        readString(buf);
        short s = buf.readShort();
        for (int i = 0; i < s; i++) buf.readByte();
    }),
    SERVER_PING(254, (user, buf) -> {
    }),
    DISCONNECT(255, (user, buf) -> readString(buf));

    private static final ServerboundPackets1_2_1[] REGISTRY = new ServerboundPackets1_2_1[256];

    static {
        for (ServerboundPackets1_2_1 packet : values()) {
            REGISTRY[packet.id] = packet;
        }
    }

    public static ServerboundPackets1_2_1 getPacket(final int id) {
        return REGISTRY[id];
    }

    private final int id;
    private final BiConsumer<UserConnection, ByteBuf> packetReader;

    ServerboundPackets1_2_1(final int id, final BiConsumer<UserConnection, ByteBuf> packetReader) {
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
