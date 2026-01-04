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
package net.raphimc.vialegacy.protocol.release.r1_7_6_10tor1_8.types;

import com.viaversion.viaversion.api.minecraft.chunks.*;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.util.IdAndData;
import com.viaversion.viaversion.util.Pair;
import io.netty.buffer.ByteBuf;
import net.raphimc.vialegacy.protocol.release.r1_7_6_10tor1_8.model.ExtendedBlockStorage;

import java.util.ArrayList;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class ChunkType extends Type<Chunk> {

    private final boolean hasSkyLight;

    public ChunkType(final boolean hasSkyLight) {
        super(Chunk.class);
        this.hasSkyLight = hasSkyLight;
    }

    /**
     * This method is here to allow overriding the code for 1.2.5 -{@literal >} 1.3.2 because it introduced an unused int
     *
     * @param byteBuf The buffer
     */
    protected void readUnusedInt(final ByteBuf byteBuf) {
    }

    /**
     * This method is here to allow overriding the code for 1.2.5 -{@literal >} 1.3.2 because it introduced an unused int
     *
     * @param byteBuf The buffer
     * @param chunk   The Chunk
     */
    protected void writeUnusedInt(final ByteBuf byteBuf, final Chunk chunk) {
    }

    @Override
    public Chunk read(ByteBuf byteBuf) {
        final int chunkX = byteBuf.readInt();
        final int chunkZ = byteBuf.readInt();
        final boolean fullChunk = byteBuf.readBoolean();
        final short primaryBitMask = byteBuf.readShort();
        final short additionalBitMask = byteBuf.readShort();
        final int compressedSize = byteBuf.readInt();
        this.readUnusedInt(byteBuf);
        final byte[] data = new byte[compressedSize];
        byteBuf.readBytes(data);

        final byte[] uncompressedData = new byte[getSize(primaryBitMask, additionalBitMask, fullChunk, this.hasSkyLight)];
        final Inflater inflater = new Inflater();
        try {
            inflater.setInput(data, 0, compressedSize);
            inflater.inflate(uncompressedData);
        } catch (DataFormatException ex) {
            throw new RuntimeException("Bad compressed data format");
        } finally {
            inflater.end();
        }

        // Check if the chunk is an unload packet and return early
        if (fullChunk && primaryBitMask == 0) {
            return new BaseChunk(chunkX, chunkZ, true, false, 0, new ChunkSection[16], null, new ArrayList<>());
        }

        return deserialize(chunkX, chunkZ, fullChunk, this.hasSkyLight, primaryBitMask, additionalBitMask, uncompressedData);
    }

    @Override
    public void write(ByteBuf byteBuf, Chunk chunk) {
        final Pair<byte[], Short> chunkData = serialize(chunk);
        final byte[] data = chunkData.key();
        final short additionalBitMask = chunkData.value();

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

        byteBuf.writeInt(chunk.getX());
        byteBuf.writeInt(chunk.getZ());
        byteBuf.writeBoolean(chunk.isFullChunk());
        byteBuf.writeShort(chunk.getBitmask());
        byteBuf.writeShort(additionalBitMask);
        byteBuf.writeInt(compressedSize);
        this.writeUnusedInt(byteBuf, chunk);
        byteBuf.writeBytes(compressedData, 0, compressedSize);
    }

    public static Chunk deserialize(final int chunkX, final int chunkZ, final boolean fullChunk, final boolean skyLight, final int primaryBitMask, final int additionalBitMask, final byte[] chunkData) {
        final ExtendedBlockStorage[] storageArrays = new ExtendedBlockStorage[16];

        int dataPosition = 0;
        for (int i = 0; i < storageArrays.length; i++) {
            if ((primaryBitMask & 1 << i) != 0) {
                if (storageArrays[i] == null) {
                    storageArrays[i] = new ExtendedBlockStorage(skyLight);
                }

                final byte[] blockLSBArray = storageArrays[i].getBlockLSBArray();
                System.arraycopy(chunkData, dataPosition, blockLSBArray, 0, blockLSBArray.length);
                dataPosition += blockLSBArray.length;
            }
        }

        for (int i = 0; i < storageArrays.length; i++) {
            if ((primaryBitMask & 1 << i) != 0 && storageArrays[i] != null) {
                final byte[] blockMetadataArray = storageArrays[i].getBlockMetadataArray().getHandle();
                System.arraycopy(chunkData, dataPosition, blockMetadataArray, 0, blockMetadataArray.length);
                dataPosition += blockMetadataArray.length;
            }
        }

        for (int i = 0; i < storageArrays.length; i++) {
            if ((primaryBitMask & 1 << i) != 0 && storageArrays[i] != null) {
                final byte[] blockLightArray = storageArrays[i].getBlockLightArray().getHandle();
                System.arraycopy(chunkData, dataPosition, blockLightArray, 0, blockLightArray.length);
                dataPosition += blockLightArray.length;
            }
        }

        if (skyLight) {
            for (int i = 0; i < storageArrays.length; i++) {
                if ((primaryBitMask & 1 << i) != 0 && storageArrays[i] != null) {
                    final byte[] skyLightArray = storageArrays[i].getSkyLightArray().getHandle();
                    System.arraycopy(chunkData, dataPosition, skyLightArray, 0, skyLightArray.length);
                    dataPosition += skyLightArray.length;
                }
            }
        }

        for (int i = 0; i < storageArrays.length; i++) {
            if ((additionalBitMask & 1 << i) != 0) {
                if (storageArrays[i] != null) {
                    final byte[] blockMSBArray = storageArrays[i].getOrCreateBlockMSBArray().getHandle();
                    System.arraycopy(chunkData, dataPosition, blockMSBArray, 0, blockMSBArray.length);
                    dataPosition += blockMSBArray.length;
                } else {
                    dataPosition += ChunkSection.SIZE / 2;
                }
            }
        }

        int[] biomeData = null;
        if (fullChunk) {
            biomeData = new int[256];
            for (int i = 0; i < biomeData.length; i++) {
                biomeData[i] = chunkData[dataPosition + i] & 255;
            }
            dataPosition += biomeData.length;
        }

        final ChunkSection[] sections = new ChunkSection[16];
        for (int i = 0; i < storageArrays.length; i++) {
            final ExtendedBlockStorage storage = storageArrays[i];
            if (storage != null) {
                final ChunkSection section = sections[i] = new ChunkSectionImpl(true);
                section.palette(PaletteType.BLOCKS).addId(0);
                for (int x = 0; x < 16; x++) {
                    for (int z = 0; z < 16; z++) {
                        for (int y = 0; y < 16; y++) {
                            section.palette(PaletteType.BLOCKS).setIdAt(x, y, z, IdAndData.toRawData(storage.getBlockId(x, y, z), storage.getBlockMetadata(x, y, z)));
                        }
                    }
                }
                section.getLight().setBlockLight(storage.getBlockLightArray().getHandle());
                if (skyLight) {
                    section.getLight().setSkyLight(storage.getSkyLightArray().getHandle());
                }
            }
        }

        return new BaseChunk(chunkX, chunkZ, fullChunk, false, primaryBitMask, sections, biomeData, new ArrayList<>());
    }

    public static Pair<byte[], Short> serialize(final Chunk chunk) {
        final ExtendedBlockStorage[] storageArrays = new ExtendedBlockStorage[16];
        for (int i = 0; i < storageArrays.length; i++) {
            final ChunkSection section = chunk.getSections()[i];
            if (section != null) {
                final ExtendedBlockStorage storage = storageArrays[i] = new ExtendedBlockStorage(section.getLight().hasSkyLight());
                for (int x = 0; x < 16; x++) {
                    for (int z = 0; z < 16; z++) {
                        for (int y = 0; y < 16; y++) {
                            final int flatBlock = section.palette(PaletteType.BLOCKS).idAt(x, y, z);
                            storage.setBlockId(x, y, z, flatBlock >> 4);
                            storage.setBlockMetadata(x, y, z, flatBlock & 15);
                        }
                    }
                }
                storage.getBlockLightArray().setHandle(section.getLight().getBlockLight());
                if (section.getLight().hasSkyLight()) {
                    storage.getSkyLightArray().setHandle(section.getLight().getSkyLight());
                }
            }
        }

        final boolean biomes = chunk.isFullChunk() && chunk.getBiomeData() != null;
        final int totalSize = getSize(storageArrays, (short) chunk.getBitmask(), biomes);
        final byte[] chunkData = new byte[totalSize];

        int dataPosition = 0;
        for (int i = 0; i < storageArrays.length; i++) {
            if ((chunk.getBitmask() & 1 << i) != 0) {
                final byte[] blockLSBArray = storageArrays[i].getBlockLSBArray();
                System.arraycopy(blockLSBArray, 0, chunkData, dataPosition, blockLSBArray.length);
                dataPosition += blockLSBArray.length;
            }
        }

        for (int i = 0; i < storageArrays.length; i++) {
            if ((chunk.getBitmask() & 1 << i) != 0) {
                final byte[] blockMetadataArray = storageArrays[i].getBlockMetadataArray().getHandle();
                System.arraycopy(blockMetadataArray, 0, chunkData, dataPosition, blockMetadataArray.length);
                dataPosition += blockMetadataArray.length;
            }
        }

        for (int i = 0; i < storageArrays.length; i++) {
            if ((chunk.getBitmask() & 1 << i) != 0) {
                final byte[] blockLightArray = storageArrays[i].getBlockLightArray().getHandle();
                System.arraycopy(blockLightArray, 0, chunkData, dataPosition, blockLightArray.length);
                dataPosition += blockLightArray.length;
            }
        }

        for (int i = 0; i < storageArrays.length; i++) {
            if ((chunk.getBitmask() & 1 << i) != 0 && storageArrays[i].getSkyLightArray() != null) {
                final byte[] skyLightArray = storageArrays[i].getSkyLightArray().getHandle();
                System.arraycopy(skyLightArray, 0, chunkData, dataPosition, skyLightArray.length);
                dataPosition += skyLightArray.length;
            }
        }

        short additionalBitMask = 0;
        for (int i = 0; i < storageArrays.length; i++) {
            if ((chunk.getBitmask() & 1 << i) != 0 && storageArrays[i].hasBlockMSBArray()) {
                additionalBitMask |= (short) (1 << i);
                final byte[] blockMSBArray = storageArrays[i].getOrCreateBlockMSBArray().getHandle();
                System.arraycopy(blockMSBArray, 0, chunkData, dataPosition, blockMSBArray.length);
                dataPosition += blockMSBArray.length;
            }
        }

        if (biomes) {
            for (int biome : chunk.getBiomeData()) {
                chunkData[dataPosition++] = (byte) biome;
            }
        }

        return new Pair<>(chunkData, additionalBitMask);
    }

    public static int getSize(final short primaryBitMask, final short additionalBitMask, final boolean fullChunk, final boolean skyLight) {
        final int primarySectionCount = Integer.bitCount(primaryBitMask & 0xFFFF);
        final int additionalSectionCount = Integer.bitCount(additionalBitMask & 0xFFFF);

        int size = ((ChunkSection.SIZE + ChunkSection.SIZE / 2 + ChunkSectionLight.LIGHT_LENGTH) * primarySectionCount) + (ChunkSection.SIZE / 2 * additionalSectionCount);
        if (skyLight) size += ChunkSectionLight.LIGHT_LENGTH * primarySectionCount;
        if (fullChunk) size += 256;

        return size;
    }

    public static int getSize(final ExtendedBlockStorage[] storageArrays, final short bitmask, final boolean biomes) {
        int size = 0;
        for (int i = 0; i < storageArrays.length; i++) {
            if ((bitmask & 1 << i) != 0) {
                size += ChunkSection.SIZE; // Block lsb array
                size += ChunkSection.SIZE / 2; // Block metadata array
                size += ChunkSectionLight.LIGHT_LENGTH; // Block light array
                if (storageArrays[i].getSkyLightArray() != null) {
                    size += ChunkSectionLight.LIGHT_LENGTH;
                }
                if (storageArrays[i].hasBlockMSBArray()) {
                    size += ChunkSection.SIZE / 2; // Block msb array
                }
            }
        }
        if (biomes) {
            size += 256;
        }
        return size;
    }

}
