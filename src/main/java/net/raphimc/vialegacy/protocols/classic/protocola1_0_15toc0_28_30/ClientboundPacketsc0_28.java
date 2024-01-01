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
package net.raphimc.vialegacy.protocols.classic.protocola1_0_15toc0_28_30;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
import io.netty.buffer.ByteBuf;
import net.raphimc.vialegacy.api.splitter.PreNettyPacketType;

import java.util.function.BiConsumer;

public enum ClientboundPacketsc0_28 implements ClientboundPacketType, PreNettyPacketType {

    JOIN_GAME(0, (user, buf) -> buf.skipBytes(130)),
    KEEP_ALIVE(1, (user, buf) -> {
    }),
    LEVEL_INIT(2, (user, buf) -> {
    }),
    LEVEL_DATA(3, (user, buf) -> buf.skipBytes(1027)),
    LEVEL_FINALIZE(4, (user, buf) -> buf.skipBytes(6)),
    BLOCK_CHANGE(6, (user, buf) -> buf.skipBytes(7)),
    SPAWN_PLAYER(7, (user, buf) -> buf.skipBytes(73)),
    ENTITY_TELEPORT(8, (user, buf) -> buf.skipBytes(9)),
    ENTITY_POSITION_AND_ROTATION(9, (user, buf) -> buf.skipBytes(6)),
    ENTITY_POSITION(10, (user, buf) -> buf.skipBytes(4)),
    ENTITY_ROTATION(11, (user, buf) -> buf.skipBytes(3)),
    DESTROY_ENTITIES(12, (user, buf) -> buf.skipBytes(1)),
    CHAT_MESSAGE(13, (user, buf) -> buf.skipBytes(65)),
    DISCONNECT(14, (user, buf) -> buf.skipBytes(64)),
    OP_LEVEL_UPDATE(15, (user, buf) -> buf.skipBytes(1));

    private static final ClientboundPacketsc0_28[] REGISTRY = new ClientboundPacketsc0_28[256];

    static {
        for (ClientboundPacketsc0_28 packet : values()) {
            REGISTRY[packet.id] = packet;
        }
    }

    public static ClientboundPacketsc0_28 getPacket(final int id) {
        return REGISTRY[id];
    }

    private final int id;
    private final BiConsumer<UserConnection, ByteBuf> packetReader;

    ClientboundPacketsc0_28(final int id, final BiConsumer<UserConnection, ByteBuf> packetReader) {
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
