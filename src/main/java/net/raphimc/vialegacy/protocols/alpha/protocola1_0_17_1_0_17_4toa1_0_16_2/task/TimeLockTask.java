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
package net.raphimc.vialegacy.protocols.alpha.protocola1_0_17_1_0_17_4toa1_0_16_2.task;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.type.Type;
import net.raphimc.vialegacy.ViaLegacy;
import net.raphimc.vialegacy.protocols.alpha.protocola1_0_17_1_0_17_4toa1_0_16_2.Protocola1_0_17_1_0_17_4toa1_0_16_2;
import net.raphimc.vialegacy.protocols.alpha.protocola1_0_17_1_0_17_4toa1_0_16_2.storage.TimeLockStorage;
import net.raphimc.vialegacy.protocols.alpha.protocola1_1_0_1_1_2_1toa1_0_17_1_0_17_4.ClientboundPacketsa1_0_17;
import net.raphimc.vialegacy.protocols.release.protocol1_6_1to1_5_2.Protocol1_6_1to1_5_2;
import net.raphimc.vialegacy.protocols.release.protocol1_6_2to1_6_1.ClientboundPackets1_6_1;
import net.raphimc.vialegacy.protocols.release.protocol1_7_2_5to1_6_4.storage.PlayerInfoStorage;

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
                        if (info.getProtocolInfo().getPipeline().contains(Protocol1_6_1to1_5_2.class)) {
                            if (timeLockStorage.getTime() == 0) { // 0 always does the daylight cycle
                                timeLockStorage.setTime(1); // Set it to 1 which is close enough
                            }
                            final PacketWrapper updateTime = PacketWrapper.create(ClientboundPackets1_6_1.TIME_UPDATE, info);
                            updateTime.write(Type.LONG, timeLockStorage.getTime()); // time
                            updateTime.write(Type.LONG, -(timeLockStorage.getTime() % 24000)); // time of day
                            updateTime.send(Protocol1_6_1to1_5_2.class);
                        } else {
                            final PacketWrapper updateTime = PacketWrapper.create(ClientboundPacketsa1_0_17.TIME_UPDATE, info);
                            updateTime.write(Type.LONG, timeLockStorage.getTime()); // time
                            updateTime.send(Protocola1_0_17_1_0_17_4toa1_0_16_2.class);
                        }
                    } catch (Throwable e) {
                        ViaLegacy.getPlatform().getLogger().log(Level.WARNING, "Error sending time update", e);
                    }
                });
            }
        }
    }

}
