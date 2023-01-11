/*
 * This file is part of ViaProtocolHack - https://github.com/RaphiMC/ViaProtocolHack
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
package net.raphimc.vialegacy.protocols.alpha.protocola1_0_17_1_0_17_4toa1_0_16_2.task;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.type.Type;
import net.raphimc.vialegacy.protocols.alpha.protocola1_0_17_1_0_17_4toa1_0_16_2.Protocola1_0_17_1_0_17_4toa1_0_16_2;
import net.raphimc.vialegacy.protocols.alpha.protocola1_0_17_1_0_17_4toa1_0_16_2.storage.TimeLockStorage;
import net.raphimc.vialegacy.protocols.alpha.protocola1_1_0_1_1_2_1toa1_0_17_1_0_17_4.ClientboundPacketsa1_0_17;
import net.raphimc.vialegacy.protocols.release.protocol1_7_2_5to1_6_4.storage.PlayerInfoStorage;

public class TimeLockTask implements Runnable {

    @Override
    public void run() {
        for (UserConnection info : Via.getManager().getConnectionManager().getConnections()) {
            final TimeLockStorage timeLockStorage = info.get(TimeLockStorage.class);
            final PlayerInfoStorage playerInfoStorage = info.get(PlayerInfoStorage.class);
            if (timeLockStorage != null && playerInfoStorage != null && playerInfoStorage.entityId != -1) {
                try {
                    final PacketWrapper updateTime = PacketWrapper.create(ClientboundPacketsa1_0_17.TIME_UPDATE, info);
                    updateTime.write(Type.LONG, timeLockStorage.getTime() % 24_000L);
                    updateTime.send(Protocola1_0_17_1_0_17_4toa1_0_16_2.class);
                } catch (Throwable ignored) {
                }
            }
        }
    }

}
