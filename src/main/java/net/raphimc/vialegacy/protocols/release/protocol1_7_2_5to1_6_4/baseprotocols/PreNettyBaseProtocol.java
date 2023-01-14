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
package net.raphimc.vialegacy.protocols.release.protocol1_7_2_5to1_6_4.baseprotocols;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.AbstractSimpleProtocol;
import com.viaversion.viaversion.api.protocol.packet.State;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.protocols.base.ServerboundHandshakePackets;
import net.raphimc.vialegacy.protocols.release.protocol1_7_2_5to1_6_4.storage.HandshakeStorage;
import net.raphimc.vialegacy.protocols.release.protocol1_7_2_5to1_6_4.storage.ProtocolMetadataStorage;

public class PreNettyBaseProtocol extends AbstractSimpleProtocol {

    public static final PreNettyBaseProtocol INSTANCE = new PreNettyBaseProtocol();

    private PreNettyBaseProtocol() {
        this.initialize();
    }

    @Override
    protected void registerPackets() {
        this.registerServerbound(State.HANDSHAKE, ServerboundHandshakePackets.CLIENT_INTENTION.getId(), ServerboundHandshakePackets.CLIENT_INTENTION.getId(), new PacketRemapper() {
            @Override
            public void registerMap() {
                handler(wrapper -> {
                    wrapper.cancel();
                    wrapper.read(Type.VAR_INT); // protocolVersion
                    final String hostname = wrapper.read(Type.STRING); // hostName
                    final int port = wrapper.read(Type.UNSIGNED_SHORT); // port
                    wrapper.user().put(new HandshakeStorage(wrapper.user(), hostname, port));
                });
            }
        });
    }

    @Override
    public void init(UserConnection userConnection) {
        userConnection.put(new ProtocolMetadataStorage(userConnection));
    }

    @Override
    public boolean isBaseProtocol() {
        return true;
    }

}
