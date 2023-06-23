/*
 * This file is part of ViaLegacy - https://github.com/RaphiMC/ViaLegacy
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
package net.raphimc.vialegacy;

import com.viaversion.viaversion.util.Config;

import java.io.File;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ViaLegacyConfig extends Config implements net.raphimc.vialegacy.platform.ViaLegacyConfig {

    private boolean dynamicOnground;
    private boolean ignoreLongChannelNames;
    private boolean legacySkullLoading;
    private boolean legacySkinLoading;
    private boolean soundEmulation;
    private boolean oldBiomes;
    private boolean remapBasedOnColor;
    private int classicChunkRange;
    private int chunksPerTick;
    private boolean enableClassicFly;

    public ViaLegacyConfig(final File configFile) {
        super(configFile);
    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();
        this.loadFields();
    }

    private void loadFields() {
        this.dynamicOnground = this.getBoolean("dynamic-onground", true);
        this.ignoreLongChannelNames = this.getBoolean("ignore-long-1_8-channel-names", true);
        this.legacySkullLoading = this.getBoolean("legacy-skull-loading", false);
        this.legacySkinLoading = this.getBoolean("legacy-skin-loading", false);
        this.soundEmulation = this.getBoolean("sound-emulation", true);
        this.oldBiomes = this.getBoolean("old-biomes", true);
        this.remapBasedOnColor = this.getBoolean("remap-based-on-color", true);
        this.classicChunkRange = this.getInt("classic-chunk-range", 10);
        this.chunksPerTick = this.getInt("chunks-per-tick", -1);
        this.enableClassicFly = this.getBoolean("enable-classic-fly", false);
    }

    @Override
    public URL getDefaultConfigURL() {
        return this.getClass().getClassLoader().getResource("assets/vialegacy/vialegacy.yml");
    }

    @Override
    protected void handleConfig(Map<String, Object> map) {
    }

    @Override
    public List<String> getUnsupportedOptions() {
        return Collections.emptyList();
    }

    @Override
    public boolean isDynamicOnground() {
        return this.dynamicOnground;
    }

    @Override
    public boolean isIgnoreLong1_8ChannelNames() {
        return this.ignoreLongChannelNames;
    }

    @Override
    public boolean isLegacySkullLoading() {
        return this.legacySkullLoading;
    }

    @Override
    public boolean isLegacySkinLoading() {
        return this.legacySkinLoading;
    }

    @Override
    public boolean isSoundEmulation() {
        return this.soundEmulation;
    }

    @Override
    public boolean isOldBiomes() {
        return this.oldBiomes;
    }

    @Override
    public boolean isRemapBasedOnColor() {
        return this.remapBasedOnColor;
    }

    @Override
    public int getClassicChunkRange() {
        return this.classicChunkRange;
    }

    @Override
    public int getChunksPerTick() {
        return this.chunksPerTick;
    }

    @Override
    public boolean enableClassicFly() {
        return this.enableClassicFly;
    }

}
