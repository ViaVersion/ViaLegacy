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
package net.raphimc.vialegacy.protocols.beta.protocolb1_3_0_1tob1_2_0_2.storage;

import com.viaversion.viaversion.api.connection.StoredObject;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.Position;
import net.raphimc.vialegacy.ViaLegacy;
import net.raphimc.vialegacy.protocols.beta.protocolb1_3_0_1tob1_2_0_2.Protocolb1_3_0_1tob1_2_0_2;

import java.util.logging.Level;

public class BlockDigStorage extends StoredObject {

    public int tick = 1;
    private final Position position;
    private final short facing;

    public BlockDigStorage(UserConnection user, final Position position, final short facing) {
        super(user);
        this.position = position;
        this.facing = facing;
    }

    public void tick() {
        try {
            if (tick >= 5) {
                Protocolb1_3_0_1tob1_2_0_2.sendBlockDigPacket(this.getUser(), (byte) 0, position, facing);
                tick = 0;
            } else {
                tick++;
            }
            Protocolb1_3_0_1tob1_2_0_2.sendBlockDigPacket(this.getUser(), (byte) 1, position, facing);
        } catch (Throwable e) {
            ViaLegacy.getPlatform().getLogger().log(Level.WARNING, "Error while ticking BlockDigStorage", e);
        }
    }

}
