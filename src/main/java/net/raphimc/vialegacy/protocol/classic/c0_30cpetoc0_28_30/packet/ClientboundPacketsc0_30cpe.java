/*
 * This file is part of ViaLegacy - https://github.com/RaphiMC/ViaLegacy
 * Copyright (C) 2020-2025 RK_01/RaphiMC and contributors
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
package net.raphimc.vialegacy.protocol.classic.c0_30cpetoc0_28_30.packet;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
import io.netty.buffer.ByteBuf;
import net.raphimc.vialegacy.api.splitter.PreNettyPacketType;

import java.util.function.BiConsumer;

public enum ClientboundPacketsc0_30cpe implements ClientboundPacketType, PreNettyPacketType {

    LOGIN(0, (user, buf) -> buf.skipBytes(130)),
    KEEP_ALIVE(1, (user, buf) -> {
    }),
    LEVEL_INIT(2, (user, buf) -> {
    }),
    LEVEL_DATA(3, (user, buf) -> buf.skipBytes(1027)),
    LEVEL_FINALIZE(4, (user, buf) -> buf.skipBytes(6)),
    BLOCK_UPDATE(6, (user, buf) -> buf.skipBytes(7)),
    ADD_PLAYER(7, (user, buf) -> buf.skipBytes(73)),
    TELEPORT_ENTITY(8, (user, buf) -> buf.skipBytes(9)),
    MOVE_ENTITY_POS_ROT(9, (user, buf) -> buf.skipBytes(6)),
    MOVE_ENTITY_POS(10, (user, buf) -> buf.skipBytes(4)),
    MOVE_ENTITY_ROT(11, (user, buf) -> buf.skipBytes(3)),
    REMOVE_ENTITIES(12, (user, buf) -> buf.skipBytes(1)),
    CHAT(13, (user, buf) -> buf.skipBytes(65)),
    DISCONNECT(14, (user, buf) -> buf.skipBytes(64)),
    OP_LEVEL_UPDATE(15, (user, buf) -> buf.skipBytes(1)),
    EXTENSION_PROTOCOL_INFO(16, (user, buf) -> buf.skipBytes(66)),
    EXTENSION_PROTOCOL_ENTRY(17, (user, buf) -> buf.skipBytes(68)),
    EXT_CUSTOM_BLOCKS_SUPPORT_LEVEL(19, (user, buf) -> buf.skipBytes(1)),
    EXT_SET_BLOCK_PERMISSION(28, (user, buf) -> buf.skipBytes(3)),
    EXT_HACK_CONTROL(32, (user, buf) -> buf.skipBytes(7)),
    EXT_BULK_BLOCK_UPDATE(38, (user, buf) -> buf.skipBytes(1281)),
    EXT_TWO_WAY_PING(43, (user, buf) -> buf.skipBytes(3));

    private static final ClientboundPacketsc0_30cpe[] REGISTRY = new ClientboundPacketsc0_30cpe[256];

    static {
        for (ClientboundPacketsc0_30cpe packet : values()) {
            REGISTRY[packet.id] = packet;
        }
    }

    public static ClientboundPacketsc0_30cpe getPacket(final int id) {
        return REGISTRY[id];
    }

    private final int id;
    private final BiConsumer<UserConnection, ByteBuf> packetReader;

    ClientboundPacketsc0_30cpe(final int id, final BiConsumer<UserConnection, ByteBuf> packetReader) {
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
