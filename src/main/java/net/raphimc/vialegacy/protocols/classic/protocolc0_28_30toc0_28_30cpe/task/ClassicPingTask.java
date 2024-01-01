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
package net.raphimc.vialegacy.protocols.classic.protocolc0_28_30toc0_28_30cpe.task;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.type.Type;
import net.raphimc.vialegacy.ViaLegacy;
import net.raphimc.vialegacy.protocols.classic.protocolc0_28_30toc0_28_30cpe.Protocolc0_30toc0_30cpe;
import net.raphimc.vialegacy.protocols.classic.protocolc0_28_30toc0_28_30cpe.ServerboundPacketsc0_30cpe;
import net.raphimc.vialegacy.protocols.classic.protocolc0_28_30toc0_28_30cpe.data.ClassicProtocolExtension;
import net.raphimc.vialegacy.protocols.classic.protocolc0_28_30toc0_28_30cpe.storage.ExtensionProtocolMetadataStorage;

import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;

public class ClassicPingTask implements Runnable {

    @Override
    public void run() {
        for (UserConnection info : Via.getManager().getConnectionManager().getConnections()) {
            final ExtensionProtocolMetadataStorage protocolMetadata = info.get(ExtensionProtocolMetadataStorage.class);
            if (protocolMetadata == null) continue;
            if (!protocolMetadata.hasServerExtension(ClassicProtocolExtension.TWO_WAY_PING, 1)) continue;
            info.getChannel().eventLoop().submit(() -> {
                if (!info.getChannel().isActive()) return;

                try {
                    final PacketWrapper pingRequest = PacketWrapper.create(ServerboundPacketsc0_30cpe.EXT_TWO_WAY_PING, info);
                    pingRequest.write(Type.BYTE, (byte) 0); // direction
                    pingRequest.write(Type.SHORT, (short) (ThreadLocalRandom.current().nextInt() % Short.MAX_VALUE)); // data
                    pingRequest.sendToServer(Protocolc0_30toc0_30cpe.class);
                } catch (Throwable e) {
                    ViaLegacy.getPlatform().getLogger().log(Level.WARNING, "Error sending TwoWayPing extension ping packet", e);
                }
            });
        }
    }

}
