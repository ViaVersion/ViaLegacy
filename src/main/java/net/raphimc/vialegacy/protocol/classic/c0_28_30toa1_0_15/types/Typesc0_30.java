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
package net.raphimc.vialegacy.protocol.classic.c0_28_30toa1_0_15.types;

import com.viaversion.viaversion.api.minecraft.BlockPosition;
import com.viaversion.viaversion.api.type.Type;

public class Typesc0_30 {

    public static final Type<String> STRING = new StringType();

    public static final Type<byte[]> BYTE_ARRAY = new ByteArrayType();

    public static final Type<BlockPosition> BLOCK_POSITION = new BlockPositionType();

}
