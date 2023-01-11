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
package net.raphimc.vialegacy.protocols.release.protocol1_8to1_7_6_10.types;

import com.viaversion.viaversion.api.minecraft.chunks.Chunk;
import com.viaversion.viaversion.api.type.PartialType;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.type.types.CustomByteType;
import com.viaversion.viaversion.api.type.types.minecraft.BaseChunkBulkType;
import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import com.viaversion.viaversion.util.Pair;
import io.netty.buffer.ByteBuf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class ChunkBulk1_7_6Type extends PartialType<Chunk[], ClientWorld> {

    public ChunkBulk1_7_6Type(final ClientWorld clientWorld) {
        super(clientWorld, Chunk[].class);
    }

    @Override
    public Class<? extends Type> getBaseClass() {
        return BaseChunkBulkType.class;
    }

    /**
     * This method is here to allow overriding the code for 1.4.5 -{@literal >} 1.4.7
     *
     * @param byteBuf     The buffer
     * @param clientWorld The ClientWorld
     * @return Read skylight array or not
     */
    protected boolean readHasSkyLight(final ByteBuf byteBuf, final ClientWorld clientWorld) {
        return byteBuf.readBoolean();
    }

    /**
     * This method is here to allow overriding the code for 1.4.5 -{@literal >} 1.4.7
     *
     * @param byteBuf     The buffer
     * @param clientWorld The ClientWorld
     * @param hasSkyLight Has skylight
     */
    protected void writeHasSkyLight(final ByteBuf byteBuf, final ClientWorld clientWorld, final boolean hasSkyLight) {
        byteBuf.writeBoolean(hasSkyLight);
    }

    @Override
    public Chunk[] read(ByteBuf byteBuf, ClientWorld clientWorld) throws Exception {
        final short chunkCount = byteBuf.readShort();
        final int compressedSize = byteBuf.readInt();
        final boolean hasSkyLight = this.readHasSkyLight(byteBuf, clientWorld);
        final byte[] data = new CustomByteType(compressedSize).read(byteBuf);
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

        final byte[] uncompressedData = new byte[Chunk1_7_6Type.getSize((short) 0xFFFF, (short) 0xFFFF, true, hasSkyLight) * chunkCount];
        final Inflater inflater = new Inflater();
        try {
            inflater.setInput(data, 0, compressedSize);
            inflater.inflate(uncompressedData);
        } catch (DataFormatException ex) {
            throw new IOException("Bad compressed data format");
        } finally {
            inflater.end();
        }

        final Chunk[] chunks = new Chunk[chunkCount];
        int dataPosition = 0;
        for (int i = 0; i < chunkCount; i++) {
            final byte[] chunkData = new byte[Chunk1_7_6Type.getSize(primaryBitMask[i], additionalBitMask[i], true, hasSkyLight)];
            System.arraycopy(uncompressedData, dataPosition, chunkData, 0, chunkData.length);
            chunks[i] = Chunk1_7_6Type.deserialize(chunkX[i], chunkZ[i], true, hasSkyLight, primaryBitMask[i], additionalBitMask[i], chunkData);
            dataPosition += chunkData.length;
        }

        return chunks;
    }

    @Override
    public void write(ByteBuf byteBuf, ClientWorld clientWorld, Chunk[] chunks) throws Exception {
        final int chunkCount = chunks.length;
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final int[] chunkX = new int[chunkCount];
        final int[] chunkZ = new int[chunkCount];
        final short[] primaryBitMask = new short[chunkCount];
        final short[] additionalBitMask = new short[chunkCount];

        for (int i = 0; i < chunkCount; i++) {
            final Chunk chunk = chunks[i];
            final Pair<byte[], Short> chunkData = Chunk1_7_6Type.serialize(chunk);
            output.write(chunkData.key());
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

        byteBuf.writeShort(chunkCount);
        byteBuf.writeInt(compressedSize);
        this.writeHasSkyLight(byteBuf, clientWorld, true);
        byteBuf.writeBytes(compressedData, 0, compressedSize);

        for (int i = 0; i < chunkCount; i++) {
            byteBuf.writeInt(chunkX[i]);
            byteBuf.writeInt(chunkZ[i]);
            byteBuf.writeShort(primaryBitMask[i]);
            byteBuf.writeShort(additionalBitMask[i]);
        }
    }

}
