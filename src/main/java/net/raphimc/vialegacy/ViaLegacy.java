package net.raphimc.vialegacy;

import net.raphimc.vialegacy.platform.ViaLegacyConfig;
import net.raphimc.vialegacy.platform.ViaLegacyPlatform;

public class ViaLegacy {

    private static ViaLegacyPlatform platform;
    private static ViaLegacyConfig config;

    private ViaLegacy() {
    }

    public static void init(final ViaLegacyPlatform platform, final ViaLegacyConfig config) {
        if (ViaLegacy.platform != null) throw new IllegalStateException("ViaLegacy is already initialized");

        ViaLegacy.platform = platform;
        ViaLegacy.config = config;
    }

    public static ViaLegacyPlatform getPlatform() {
        return ViaLegacy.platform;
    }

    public static ViaLegacyConfig getConfig() {
        return ViaLegacy.config;
    }

}
