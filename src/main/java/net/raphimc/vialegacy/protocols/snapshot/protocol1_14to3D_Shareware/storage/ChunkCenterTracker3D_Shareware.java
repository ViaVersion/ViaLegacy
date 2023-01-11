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
package net.raphimc.vialegacy.protocols.snapshot.protocol1_14to3D_Shareware.storage;

import com.viaversion.viaversion.api.connection.StoredObject;
import com.viaversion.viaversion.api.connection.UserConnection;

public class ChunkCenterTracker3D_Shareware extends StoredObject {

    private boolean forceSendCenterChunk = true;
    private int chunkCenterX, chunkCenterZ;

    public ChunkCenterTracker3D_Shareware(UserConnection user) {
        super(user);
    }

    public boolean isForceSendCenterChunk() {
        return forceSendCenterChunk;
    }

    public void setForceSendCenterChunk(boolean forceSendCenterChunk) {
        this.forceSendCenterChunk = forceSendCenterChunk;
    }

    public int getChunkCenterX() {
        return chunkCenterX;
    }

    public void setChunkCenterX(int chunkCenterX) {
        this.chunkCenterX = chunkCenterX;
    }

    public int getChunkCenterZ() {
        return chunkCenterZ;
    }

    public void setChunkCenterZ(int chunkCenterZ) {
        this.chunkCenterZ = chunkCenterZ;
    }

}
