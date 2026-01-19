/*
 * This file is part of ViaLegacy - https://github.com/RaphiMC/ViaLegacy
 * Copyright (C) 2020-2026 RK_01/RaphiMC and contributors
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
package net.raphimc.vialegacy.protocol.beta.b1_2_0_2tob1_3_0_1.task;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.UserConnection;
import net.raphimc.vialegacy.ViaLegacy;
import net.raphimc.vialegacy.protocol.beta.b1_2_0_2tob1_3_0_1.storage.BlockDigStorage;

import java.util.logging.Level;

public class BlockDigTickTask implements Runnable {

    @Override
    public void run() {
        for (UserConnection info : Via.getManager().getConnectionManager().getConnections()) {
            final BlockDigStorage blockDigStorage = info.get(BlockDigStorage.class);
            if (blockDigStorage != null) {
                info.getChannel().eventLoop().submit(() -> {
                    if (!info.getChannel().isActive()) return;

                    try {
                        blockDigStorage.tick();
                    } catch (Throwable e) {
                        ViaLegacy.getPlatform().getLogger().log(Level.WARNING, "Error while ticking BlockDigStorage", e);
                    }
                });
            }
        }
    }

}
