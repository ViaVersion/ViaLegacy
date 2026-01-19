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
package net.raphimc.vialegacy.api.splitter;

import io.netty.buffer.ByteBuf;
import net.raphimc.vialegacy.protocol.release.r1_2_4_5tor1_3_1_2.data.NbtItemList1_2_4;

public class PreNettyTypes {

    public static void readString(final ByteBuf buffer) {
        final short size = buffer.readShort();
        for (int i = 0; i < size; i++) {
            buffer.readShort();
        }
    }

    public static void readUTF(final ByteBuf buffer) {
        final int size = buffer.readUnsignedShort();
        for (int i = 0; i < size; i++) {
            buffer.readByte();
        }
    }

    public static void readString64(final ByteBuf buffer) {
        for (int i = 0; i < 64; i++) {
            buffer.readByte();
        }
    }

    public static void readItemStack1_3_1(final ByteBuf buffer) {
        final short id = buffer.readShort();
        if (id > -1) {
            buffer.readByte();
            buffer.readShort();
            readTag(buffer);
        }
    }

    public static void readItemStack1_0(final ByteBuf buffer) {
        final short id = buffer.readShort();
        if (id > -1) {
            buffer.readByte();
            buffer.readShort();
            if (NbtItemList1_2_4.hasNbt(id)) {
                readTag(buffer);
            }
        }
    }

    public static void readItemStackb1_2(final ByteBuf buffer) {
        final short id = buffer.readShort();
        if (id > -1) {
            buffer.readByte();
            buffer.readShort();
        }
    }

    public static void readItemStackb1_1(final ByteBuf buffer) {
        final short id = buffer.readShort();
        if (id >= 0) {
            buffer.readByte();
            buffer.readByte();
        }
    }

    public static void readByteArray(final ByteBuf buffer) {
        final short size = buffer.readShort();
        for (int i = 0; i < size; i++) {
            buffer.readByte();
        }
    }

    public static void readByteArray1024(final ByteBuf buffer) {
        for (int i = 0; i < 1024; i++) {
            buffer.readByte();
        }
    }

    public static void readTag(final ByteBuf buffer) {
        final int size = buffer.readShort();
        for (int i = 0; i < size; i++) {
            buffer.readByte();
        }
    }

    public static void readEntityDataList1_4_4(final ByteBuf buffer) {
        for (byte b = buffer.readByte(); b != 127; b = buffer.readByte()) {
            switch ((b & 224) >> 5) {
                case 0 -> buffer.readByte();
                case 1 -> buffer.readShort();
                case 2 -> buffer.readInt();
                case 3 -> buffer.readFloat();
                case 4 -> readString(buffer);
                case 5 -> readItemStack1_3_1(buffer);
                case 6 -> {
                    buffer.readInt();
                    buffer.readInt();
                    buffer.readInt();
                }
            }
        }
    }

    public static void readEntityDataList1_4_2(final ByteBuf buffer) {
        for (byte b = buffer.readByte(); b != 127; b = buffer.readByte()) {
            switch ((b & 224) >> 5) {
                case 0 -> buffer.readByte();
                case 1 -> buffer.readShort();
                case 2 -> buffer.readInt();
                case 3 -> buffer.readFloat();
                case 4 -> readString(buffer);
                case 5 -> {
                    final short id = buffer.readShort();
                    if (id > -1) {
                        buffer.readByte();
                        buffer.readShort();
                    }
                }
                case 6 -> {
                    buffer.readInt();
                    buffer.readInt();
                    buffer.readInt();
                }
            }
        }
    }

    public static void readEntityDataListb1_5(final ByteBuf buffer) {
        for (byte b = buffer.readByte(); b != 127; b = buffer.readByte()) {
            switch ((b & 224) >> 5) {
                case 0 -> buffer.readByte();
                case 1 -> buffer.readShort();
                case 2 -> buffer.readInt();
                case 3 -> buffer.readFloat();
                case 4 -> readString(buffer);
                case 5 -> {
                    buffer.readShort();
                    buffer.readByte();
                    buffer.readShort();
                }
                case 6 -> {
                    buffer.readInt();
                    buffer.readInt();
                    buffer.readInt();
                }
            }
        }
    }

    public static void readEntityDataListb1_3(final ByteBuf buffer) {
        for (byte b = buffer.readByte(); b != 127; b = buffer.readByte()) {
            switch ((b & 224) >> 5) {
                case 0 -> buffer.readByte();
                case 1 -> buffer.readShort();
                case 2 -> buffer.readInt();
                case 3 -> buffer.readFloat();
                case 4 -> readUTF(buffer);
                case 5 -> {
                    buffer.readShort();
                    buffer.readByte();
                    buffer.readShort();
                }
                case 6 -> {
                    buffer.readInt();
                    buffer.readInt();
                    buffer.readInt();
                }
            }
        }
    }

    public static void readEntityDataListb1_2(final ByteBuf buffer) {
        for (byte b = buffer.readByte(); b != 127; b = buffer.readByte()) {
            switch ((b & 224) >> 5) {
                case 0 -> buffer.readByte();
                case 1 -> buffer.readShort();
                case 2 -> buffer.readInt();
                case 3 -> buffer.readFloat();
                case 4 -> readUTF(buffer);
                case 5 -> {
                    buffer.readShort();
                    buffer.readByte();
                    buffer.readShort();
                }
            }
        }
    }

}
