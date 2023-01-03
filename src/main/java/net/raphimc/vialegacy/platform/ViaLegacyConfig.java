package net.raphimc.vialegacy.platform;

public interface ViaLegacyConfig {

    boolean isDynamicOnground();

    boolean isIgnoreLong1_8ChannelNames();

    boolean isLegacySkullLoading();

    boolean isLegacySkinLoading();

    boolean isSoundEmulation();

    boolean isOldBiomes();

    boolean isRemapBasedOnColor();

    int getClassicChunkRange();

    int getChunksPerTick();

}
