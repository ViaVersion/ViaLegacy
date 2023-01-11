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
package net.raphimc.vialegacy.protocols.release.protocol1_3_1_2to1_2_4_5.types;

import com.viaversion.viaversion.api.minecraft.chunks.Chunk;
import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import io.netty.buffer.ByteBuf;
import net.raphimc.vialegacy.protocols.release.protocol1_8to1_7_6_10.types.Chunk1_7_6Type;

public class Chunk1_2_4Type extends Chunk1_7_6Type {

    public Chunk1_2_4Type(ClientWorld clientWorld) {
        super(clientWorld);
    }

    @Override
    protected void readUnusedInt(ByteBuf byteBuf, ClientWorld clientWorld) {
        byteBuf.readInt();
    }

    @Override
    protected void writeUnusedInt(ByteBuf byteBuf, ClientWorld clientWorld, Chunk chunk) {
        byteBuf.writeInt(0);
    }

}
