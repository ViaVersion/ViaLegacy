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
package net.raphimc.vialegacy.protocol.release.r1_7_6_10tor1_8.types;

import com.viaversion.viaversion.api.minecraft.chunks.Chunk;
import com.viaversion.viaversion.api.minecraft.chunks.ChunkSection;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.util.Pair;
import io.netty.buffer.ByteBuf;

import java.io.ByteArrayOutputStream;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class BulkChunkType extends Type<Chunk[]> {

    public BulkChunkType() {
        super(Chunk[].class);
    }

    /**
     * This method is here to allow overriding the code for 1.4.5 -{@literal >} 1.4.7
     *
     * @param byteBuf The buffer
     * @return Read skylight array or not
     */
    protected boolean readHasSkyLight(final ByteBuf byteBuf) {
        return byteBuf.readBoolean();
    }

    /**
     * This method is here to allow overriding the code for 1.4.5 -{@literal >} 1.4.7
     *
     * @param byteBuf     The buffer
     * @param hasSkyLight Has skylight
     */
    protected void writeHasSkyLight(final ByteBuf byteBuf, final boolean hasSkyLight) {
        byteBuf.writeBoolean(hasSkyLight);
    }

    @Override
    public Chunk[] read(ByteBuf byteBuf) {
        final short chunkCount = byteBuf.readShort();
        final int compressedSize = byteBuf.readInt();
        final boolean hasSkyLight = this.readHasSkyLight(byteBuf);
        final byte[] data = new byte[compressedSize];
        byteBuf.readBytes(data);
        final int[] chunkX = new int[chunkCount];
        final int[] chunkZ = new int[chunkCount];
        final short[] primaryBitMask = new short[chunkCount];
        final short[] additionalBitMask = new short[chunkCount];
        for (int i = 0; i < chunkCount; i++) {
            chunkX[i] = byteBuf.readInt();
            chunkZ[i] = byteBuf.readInt();
            primaryBitMask[i] = byteBuf.readShort();
            additionalBitMask[i] = byteBuf.readShort();
        }

        final byte[] uncompressedData = new byte[ChunkType.getSize((short) 0xFFFF, (short) 0xFFFF, true, hasSkyLight) * chunkCount];
        final Inflater inflater = new Inflater();
        try {
            inflater.setInput(data, 0, compressedSize);
            inflater.inflate(uncompressedData);
        } catch (DataFormatException ex) {
            throw new RuntimeException("Bad compressed data format");
        } finally {
            inflater.end();
        }

        final Chunk[] chunks = new Chunk[chunkCount];
        int dataPosition = 0;
        for (int i = 0; i < chunkCount; i++) {
            final byte[] chunkData = new byte[ChunkType.getSize(primaryBitMask[i], additionalBitMask[i], true, hasSkyLight)];
            System.arraycopy(uncompressedData, dataPosition, chunkData, 0, chunkData.length);
            chunks[i] = ChunkType.deserialize(chunkX[i], chunkZ[i], true, hasSkyLight, primaryBitMask[i], additionalBitMask[i], chunkData);
            dataPosition += chunkData.length;
        }

        return chunks;
    }

    @Override
    public void write(ByteBuf byteBuf, Chunk[] chunks) {
        final int chunkCount = chunks.length;
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final int[] chunkX = new int[chunkCount];
        final int[] chunkZ = new int[chunkCount];
        final short[] primaryBitMask = new short[chunkCount];
        final short[] additionalBitMask = new short[chunkCount];

        for (int i = 0; i < chunkCount; i++) {
            final Chunk chunk = chunks[i];
            final Pair<byte[], Short> chunkData = ChunkType.serialize(chunk);
            output.writeBytes(chunkData.key());
            chunkX[i] = chunk.getX();
            chunkZ[i] = chunk.getZ();
            primaryBitMask[i] = (short) chunk.getBitmask();
            additionalBitMask[i] = chunkData.value();
        }
        final byte[] data = output.toByteArray();

        final Deflater deflater = new Deflater();
        byte[] compressedData;
        int compressedSize;
        try {
            deflater.setInput(data, 0, data.length);
            deflater.finish();
            compressedData = new byte[data.length];
            compressedSize = deflater.deflate(compressedData);
        } finally {
            deflater.end();
        }

        boolean skyLight = false;
        loop1:
        for (Chunk chunk : chunks) {
            for (ChunkSection section : chunk.getSections()) {
                if (section != null && section.getLight().hasSkyLight()) {
                    skyLight = true;
                    break loop1;
                }
            }
        }

        byteBuf.writeShort(chunkCount);
        byteBuf.writeInt(compressedSize);
        this.writeHasSkyLight(byteBuf, skyLight);
        byteBuf.writeBytes(compressedData, 0, compressedSize);

        for (int i = 0; i < chunkCount; i++) {
            byteBuf.writeInt(chunkX[i]);
            byteBuf.writeInt(chunkZ[i]);
            byteBuf.writeShort(primaryBitMask[i]);
            byteBuf.writeShort(additionalBitMask[i]);
        }
    }

}
