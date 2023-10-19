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
package net.raphimc.vialegacy.protocols.release.protocol1_4_6_7to1_4_4_5.types;

import com.viaversion.viaversion.api.minecraft.ClientWorld;
import io.netty.buffer.ByteBuf;
import net.raphimc.vialegacy.protocols.release.protocol1_8to1_7_6_10.types.ChunkBulkType1_7_6;

public class ChunkBulkType1_4_4 extends ChunkBulkType1_7_6 {

    public ChunkBulkType1_4_4(ClientWorld clientWorld) {
        super(clientWorld);
    }

    @Override
    protected boolean readHasSkyLight(ByteBuf byteBuf, ClientWorld clientWorld) {
        return true;
    }

    @Override
    protected void writeHasSkyLight(ByteBuf byteBuf, ClientWorld clientWorld, boolean hasSkyLight) {
    }

}
