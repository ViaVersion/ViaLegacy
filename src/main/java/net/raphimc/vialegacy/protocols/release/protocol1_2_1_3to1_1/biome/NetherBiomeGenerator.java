package net.raphimc.vialegacy.protocols.release.protocol1_2_1_3to1_1.biome;

import net.raphimc.vialegacy.protocols.release.protocol1_2_1_3to1_1.biome.release.NewBiomeGenBase;

import java.util.Arrays;

public class NetherBiomeGenerator implements IWorldChunkManager {

    private static final byte[] NETHER_BIOME_DATA;

    static {
        NETHER_BIOME_DATA = new byte[256];
        Arrays.fill(NETHER_BIOME_DATA, (byte) NewBiomeGenBase.hell.biomeID);
    }

    @Override
    public byte[] getBiomeDataAt(int chunkX, int chunkZ) {
        return NETHER_BIOME_DATA;
    }

}
