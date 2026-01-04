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
package net.raphimc.vialegacy.protocol.beta.b1_2_0_2tob1_3_0_1.storage;

import com.viaversion.viaversion.api.connection.StoredObject;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.BlockPosition;
import net.raphimc.vialegacy.protocol.beta.b1_2_0_2tob1_3_0_1.Protocolb1_2_0_2Tob1_3_0_1;

public class BlockDigStorage extends StoredObject {

    public int tick = 1;
    private final BlockPosition position;
    private final short facing;

    public BlockDigStorage(UserConnection user, final BlockPosition position, final short facing) {
        super(user);
        this.position = position;
        this.facing = facing;
    }

    public void tick() {
        if (this.tick >= 5) {
            Protocolb1_2_0_2Tob1_3_0_1.sendBlockDigPacket(this.user(), (byte) 0, this.position, this.facing);
            this.tick = 0;
        } else {
            this.tick++;
        }
        Protocolb1_2_0_2Tob1_3_0_1.sendBlockDigPacket(this.user(), (byte) 1, this.position, this.facing);
    }

}
