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
package net.raphimc.vialegacy.protocols.release.protocol1_8to1_7_6_10.types;

import com.viaversion.viaversion.api.minecraft.Position;
import com.viaversion.viaversion.api.type.Type;
import io.netty.buffer.ByteBuf;

import java.util.function.IntFunction;

public class PositionVarYType<T extends Number> extends Type<Position> {

    private final Type<T> yType;
    private final IntFunction<T> yConverter;

    public PositionVarYType(final Type<T> yType, final IntFunction<T> yConverter) {
        super(Position.class);
        this.yType = yType;
        this.yConverter = yConverter;
    }

    @Override
    public Position read(ByteBuf buffer) throws Exception {
        return new Position(buffer.readInt(), this.yType.read(buffer).intValue(), buffer.readInt());
    }

    @Override
    public void write(ByteBuf buffer, Position position) throws Exception {
        buffer.writeInt(position.x());
        this.yType.write(buffer, this.yConverter.apply(position.y()));
        buffer.writeInt(position.z());
    }

}
