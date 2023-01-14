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
package net.raphimc.vialegacy.protocols.release.protocol1_1to1_0_0_1;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.packet.ServerboundPacketType;
import io.netty.buffer.ByteBuf;
import net.raphimc.vialegacy.api.splitter.PreNettyPacketType;

import java.util.function.BiConsumer;

import static net.raphimc.vialegacy.api.splitter.PreNettyTypes.readItemStack1_0;
import static net.raphimc.vialegacy.api.splitter.PreNettyTypes.readString;

public enum ServerboundPackets1_0 implements ServerboundPacketType, PreNettyPacketType {

    KEEP_ALIVE(0, (user, buf) -> {
        buf.readInt();
    }),
    LOGIN(1, (user, buf) -> {
        buf.readInt();
        readString(buf);
        buf.readLong();
        buf.readInt();
        buf.readByte();
        buf.readByte();
        buf.readByte();
        buf.readByte();
    }),
    HANDSHAKE(2, (user, buf) -> {
        readString(buf);
    }),
    CHAT_MESSAGE(3, (user, buf) -> {
        readString(buf);
    }),
    INTERACT_ENTITY(7, (user, buf) -> {
        buf.readInt();
        buf.readInt();
        buf.readByte();
    }),
    RESPAWN(9, (user, buf) -> {
        buf.readByte();
        buf.readByte();
        buf.readByte();
        buf.readShort();
        buf.readLong();
    }),
    PLAYER_MOVEMENT(10, (user, buf) -> {
        buf.readUnsignedByte();
    }),
    PLAYER_POSITION(11, (user, buf) -> {
        buf.readDouble();
        buf.readDouble();
        buf.readDouble();
        buf.readDouble();
        buf.readUnsignedByte();
    }),
    PLAYER_ROTATION(12, (user, buf) -> {
        buf.readFloat();
        buf.readFloat();
        buf.readUnsignedByte();
    }),
    PLAYER_POSITION_AND_ROTATION(13, (user, buf) -> {
        buf.readDouble();
        buf.readDouble();
        buf.readDouble();
        buf.readDouble();
        buf.readFloat();
        buf.readFloat();
        buf.readUnsignedByte();
    }),
    PLAYER_DIGGING(14, (user, buf) -> {
        buf.readUnsignedByte();
        buf.readInt();
        buf.readUnsignedByte();
        buf.readInt();
        buf.readUnsignedByte();
    }),
    PLAYER_BLOCK_PLACEMENT(15, (user, buf) -> {
        buf.readInt();
        buf.readUnsignedByte();
        buf.readInt();
        buf.readUnsignedByte();
        readItemStack1_0(buf);
    }),
    HELD_ITEM_CHANGE(16, (user, buf) -> {
        buf.readShort();
    }),
    ANIMATION(18, (user, buf) -> {
        buf.readInt();
        buf.readByte();
    }),
    ENTITY_ACTION(19, (user, buf) -> {
        buf.readInt();
        buf.readByte();
    }),
    POSITION(27, (user, buf) -> {
        buf.readFloat();
        buf.readFloat();
        buf.readFloat();
        buf.readFloat();
        buf.readBoolean();
        buf.readBoolean();
    }),
    CLOSE_WINDOW(101, (user, buf) -> {
        buf.readByte();
    }),
    CLICK_WINDOW(102, (user, buf) -> {
        buf.readByte();
        buf.readShort();
        buf.readByte();
        buf.readShort();
        buf.readBoolean();
        readItemStack1_0(buf);
    }),
    WINDOW_CONFIRMATION(106, (user, buf) -> {
        buf.readByte();
        buf.readShort();
        buf.readByte();
    }),
    CREATIVE_INVENTORY_ACTION(107, (user, buf) -> {
        buf.readShort();
        readItemStack1_0(buf);
    }),
    CLICK_WINDOW_BUTTON(108, (user, buf) -> {
        buf.readByte();
        buf.readByte();
    }),
    UPDATE_SIGN(130, (user, buf) -> {
        buf.readInt();
        buf.readShort();
        buf.readInt();
        readString(buf);
        readString(buf);
        readString(buf);
        readString(buf);
    }),
    SERVER_PING(254, (user, buf) -> {
    }),
    DISCONNECT(255, (user, buf) -> {
        readString(buf);
    });

    private static final ServerboundPackets1_0[] REGISTRY = new ServerboundPackets1_0[256];

    static {
        for (ServerboundPackets1_0 packet : values()) {
            REGISTRY[packet.id] = packet;
        }
    }

    public static ServerboundPackets1_0 getPacket(final int id) {
        return REGISTRY[id];
    }

    private final int id;
    private final BiConsumer<UserConnection, ByteBuf> packetReader;

    ServerboundPackets1_0(final int id, final BiConsumer<UserConnection, ByteBuf> packetReader) {
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
