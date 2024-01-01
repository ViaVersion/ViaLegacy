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
package net.raphimc.vialegacy.protocols.release.protocol1_4_6_7to1_4_4_5.types;

import io.netty.buffer.ByteBuf;
import net.raphimc.vialegacy.protocols.release.protocol1_8to1_7_6_10.types.BulkChunkType1_7_6;

public class BulkChunkType1_4_4 extends BulkChunkType1_7_6 {

    @Override
    protected boolean readHasSkyLight(ByteBuf byteBuf) {
        return true;
    }

    @Override
    protected void writeHasSkyLight(ByteBuf byteBuf, boolean hasSkyLight) {
    }

}
