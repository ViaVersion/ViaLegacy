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
package net.raphimc.vialegacy.protocol.alpha.a1_0_16_2toa1_0_17_1_0_17_4.task;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.type.Types;
import net.raphimc.vialegacy.ViaLegacy;
import net.raphimc.vialegacy.protocol.alpha.a1_0_16_2toa1_0_17_1_0_17_4.Protocola1_0_16_2Toa1_0_17_1_0_17_4;
import net.raphimc.vialegacy.protocol.alpha.a1_0_16_2toa1_0_17_1_0_17_4.storage.TimeLockStorage;
import net.raphimc.vialegacy.protocol.alpha.a1_0_17_1_0_17_4toa1_1_0_1_1_2_1.packet.ClientboundPacketsa1_0_17;
import net.raphimc.vialegacy.protocol.release.r1_5_2tor1_6_1.Protocolr1_5_2Tor1_6_1;
import net.raphimc.vialegacy.protocol.release.r1_6_1tor1_6_2.packet.ClientboundPackets1_6_1;
import net.raphimc.vialegacy.protocol.release.r1_6_4tor1_7_2_5.storage.PlayerInfoStorage;

import java.util.logging.Level;

public class TimeLockTask implements Runnable {

    @Override
    public void run() {
        for (UserConnection info : Via.getManager().getConnectionManager().getConnections()) {
            final TimeLockStorage timeLockStorage = info.get(TimeLockStorage.class);
            final PlayerInfoStorage playerInfoStorage = info.get(PlayerInfoStorage.class);
            if (timeLockStorage != null && playerInfoStorage != null && playerInfoStorage.entityId != -1) {
                info.getChannel().eventLoop().submit(() -> {
                    if (!info.getChannel().isActive()) return;

                    try {
                        if (info.getProtocolInfo().getPipeline().contains(Protocolr1_5_2Tor1_6_1.class)) {
                            if (timeLockStorage.getTime() == 0) { // 0 always does the daylight cycle
                                timeLockStorage.setTime(1); // Set it to 1 which is close enough
                            }
                            final PacketWrapper updateTime = PacketWrapper.create(ClientboundPackets1_6_1.SET_TIME, info);
                            updateTime.write(Types.LONG, timeLockStorage.getTime()); // time
                            updateTime.write(Types.LONG, -(timeLockStorage.getTime() % 24000)); // time of day
                            updateTime.send(Protocolr1_5_2Tor1_6_1.class);
                        } else {
                            final PacketWrapper updateTime = PacketWrapper.create(ClientboundPacketsa1_0_17.SET_TIME, info);
                            updateTime.write(Types.LONG, timeLockStorage.getTime()); // time
                            updateTime.send(Protocola1_0_16_2Toa1_0_17_1_0_17_4.class);
                        }
                    } catch (Throwable e) {
                        ViaLegacy.getPlatform().getLogger().log(Level.WARNING, "Error sending time update", e);
                    }
                });
            }
        }
    }

}
