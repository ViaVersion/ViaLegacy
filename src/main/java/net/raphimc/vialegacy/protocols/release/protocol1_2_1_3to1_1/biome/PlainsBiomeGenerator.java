package net.raphimc.vialegacy.protocols.release.protocol1_2_1_3to1_1.biome;

import net.raphimc.vialegacy.protocols.release.protocol1_2_1_3to1_1.biome.release.NewBiomeGenBase;

import java.util.Arrays;

public class PlainsBiomeGenerator implements IWorldChunkManager {

    private static final byte[] PLAINS_BIOME_DATA;

    static {
        PLAINS_BIOME_DATA = new byte[256];
        Arrays.fill(PLAINS_BIOME_DATA, (byte) NewBiomeGenBase.plains.biomeID);
    }

    @Override
    public byte[] getBiomeDataAt(int chunkX, int chunkZ) {
        return PLAINS_BIOME_DATA;
    }

}
