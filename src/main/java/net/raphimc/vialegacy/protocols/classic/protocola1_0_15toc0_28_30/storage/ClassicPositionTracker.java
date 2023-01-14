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
package net.raphimc.vialegacy.protocols.classic.protocola1_0_15toc0_28_30.storage;

import com.viaversion.viaversion.api.connection.StoredObject;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.Position;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.type.Type;
import net.raphimc.vialegacy.api.model.ChunkCoord;

public class ClassicPositionTracker extends StoredObject {

    public double posX;
    public double stance;
    public double posZ;
    public float yaw;
    public float pitch;

    public boolean spawned;

    public ClassicPositionTracker(final UserConnection user) {
        super(user);
    }

    public void writeToPacket(final PacketWrapper wrapper) {
        final int x = (int) (this.posX * 32.0F);
        final int y = (int) (this.stance * 32.0F);
        final int z = (int) (this.posZ * 32.0F);
        final int yaw = (int) (this.yaw * 256.0F / 360.0F) & 255;
        final int pitch = (int) (this.pitch * 256.0F / 360.0F) & 255;

        wrapper.write(Type.BYTE, (byte) -1); // entity id
        wrapper.write(Type.SHORT, (short) x); // x
        wrapper.write(Type.SHORT, (short) y); // y
        wrapper.write(Type.SHORT, (short) z); // z
        wrapper.write(Type.BYTE, (byte) (yaw - 128)); // yaw
        wrapper.write(Type.BYTE, (byte) pitch); // pitch
    }

    public Position getBlockPosition() {
        return new Position(floor(this.posX), floor(this.stance), floor(this.posZ));
    }

    public ChunkCoord getChunkPosition() {
        final Position pos = this.getBlockPosition();
        return new ChunkCoord(pos.x() >> 4, pos.z() >> 4);
    }

    private static int floor(double f) {
        int i = (int) f;
        return f < (double) i ? i - 1 : i;
    }

}
