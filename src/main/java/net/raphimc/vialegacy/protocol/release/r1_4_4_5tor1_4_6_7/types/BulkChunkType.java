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
package net.raphimc.vialegacy.protocol.release.r1_4_4_5tor1_4_6_7.types;

import io.netty.buffer.ByteBuf;

public class BulkChunkType extends net.raphimc.vialegacy.protocol.release.r1_7_6_10tor1_8.types.BulkChunkType {

    @Override
    protected boolean readHasSkyLight(ByteBuf byteBuf) {
        return true;
    }

    @Override
    protected void writeHasSkyLight(ByteBuf byteBuf, boolean hasSkyLight) {
    }

}
