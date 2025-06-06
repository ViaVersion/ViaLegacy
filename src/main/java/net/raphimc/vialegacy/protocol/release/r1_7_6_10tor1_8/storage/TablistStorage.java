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
package net.raphimc.vialegacy.protocol.release.r1_7_6_10tor1_8.storage;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.StoredObject;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.type.Types;
import com.viaversion.viaversion.protocols.v1_8to1_9.packet.ClientboundPackets1_8;
import net.raphimc.vialegacy.api.util.GameProfileUtil;
import net.raphimc.vialegacy.protocol.release.r1_7_6_10tor1_8.Protocolr1_7_6_10Tor1_8;
import net.raphimc.vialegacy.protocol.release.r1_7_6_10tor1_8.model.TabListEntry;

import java.util.HashMap;
import java.util.Map;

public class TablistStorage extends StoredObject {

    public final Map<String, TabListEntry> tablist = new HashMap<>();

    public TablistStorage(UserConnection user) {
        super(user);
    }

    public void sendTempEntry(final TabListEntry entry) {
        entry.ping = -1;
        this.sendAddEntry(entry); // send tablist entry before spawning player
        Via.getPlatform().runSync(() -> this.sendRemoveEntry(entry), GameProfileUtil.isOfflineGameProfile(entry.gameProfile) ? 2L : 60L); // wait for the client to load the skin then remove the fake tablist entry
    }

    public void sendAddEntry(final TabListEntry entry) {
        final PacketWrapper addPlayerListItemPacket = PacketWrapper.create(ClientboundPackets1_8.PLAYER_INFO, this.user());
        addPlayerListItemPacket.write(Types.VAR_INT, 0); // action
        addPlayerListItemPacket.write(Types.VAR_INT, 1); // count
        addPlayerListItemPacket.write(Types.UUID, entry.gameProfile.id()); // uuid
        addPlayerListItemPacket.write(Types.STRING, entry.gameProfile.name()); // name
        addPlayerListItemPacket.write(Types.PROFILE_PROPERTY_ARRAY, entry.gameProfile.properties()); // properties
        addPlayerListItemPacket.write(Types.VAR_INT, entry.gameMode); // gamemode
        addPlayerListItemPacket.write(Types.VAR_INT, entry.ping); // ping
        addPlayerListItemPacket.write(Types.OPTIONAL_STRING, null); // display name
        addPlayerListItemPacket.send(Protocolr1_7_6_10Tor1_8.class);
    }

    public void sendRemoveEntry(final TabListEntry entry) {
        final PacketWrapper removePlayerListItemPacket = PacketWrapper.create(ClientboundPackets1_8.PLAYER_INFO, this.user());
        removePlayerListItemPacket.write(Types.VAR_INT, 4); // action
        removePlayerListItemPacket.write(Types.VAR_INT, 1); // count
        removePlayerListItemPacket.write(Types.UUID, entry.gameProfile.id()); // uuid
        removePlayerListItemPacket.send(Protocolr1_7_6_10Tor1_8.class);
    }

}
