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
package net.raphimc.vialegacy.protocols.classic.protocolc0_28_30toc0_28_30cpe.data;

import com.viaversion.viaversion.libs.fastutil.ints.IntOpenHashSet;
import com.viaversion.viaversion.libs.fastutil.ints.IntSet;

import java.util.Arrays;

// https://wiki.vg/Classic_Protocol_Extension
public enum ClassicProtocolExtension {

    CLICK_DISTANCE("ClickDistance"),
    CUSTOM_BLOCKS("CustomBlocks", 1),
    HELD_BLOCK("HeldBlock"),
    TEXT_HOT_KEY("TextHotKey"),
    EXT_PLAYER_LIST("ExtPlayerList"),
    ENV_COLORS("EnvColors"),
    SELECTION_CUBOID("SelectionCuboid"),
    BLOCK_PERMISSIONS("BlockPermissions", 1),
    CHANGE_MODEL("ChangeModel"),
    ENV_MAP_APPEARANCE("EnvMapAppearance"),
    ENV_WEATHER_TYPE("EnvWeatherType"),
    HACK_CONTROL("HackControl", 1),
    EMOTE_FIX("EmoteFix", 1),
    MESSAGE_TYPES("MessageTypes"),
    LONGER_MESSAGES("LongerMessages", 1),
    FULL_CP437("FullCP437", 1),
    BLOCK_DEFINITIONS("BlockDefinitions"),
    BLOCK_DEFINITIONS_EXT("BlockDefinitionsExt"),
    TEXT_COLORS("TextColors"),
    BULK_BLOCK_UPDATE("BulkBlockUpdate", 1),
    ENV_MAP_ASPECT("EnvMapAspect"),
    PLAYER_CLICK("PlayerClick"),
    ENTITY_PROPERTY("EntityProperty"),
    EXT_ENTITY_POSITIONS("ExtEntityPositions"),
    TWO_WAY_PING("TwoWayPing", 1),
    INVENTORY_ORDER("InventoryOrder"),
    INSTANT_MOTD("InstantMOTD", 1),
    EXTENDED_BLOCKS("ExtendedBlocks"),
    FAST_MAP("FastMap"),
    EXTENDED_TEXTURES("ExtendedTextures"),
    SET_HOTBAR("SetHotbar"),
    SET_SPAWNPOINT("SetSpawnpoint"),
    VELOCITY_CONTROL("VelocityControl"),
    CUSTOM_PARTICLES("CustomParticles"),
    CUSTOM_MODELS("CustomModels"),
    EXT_ENTITY_TELEPORT("ExtEntityTeleport");

    private final String name;
    private final IntSet supportedVersions;

    ClassicProtocolExtension(String name, int... supportedVersions) {
        this.name = name;
        this.supportedVersions = new IntOpenHashSet();
        for (int supportedVersion : supportedVersions) {
            this.supportedVersions.add(supportedVersion);
        }
    }

    public static ClassicProtocolExtension byName(String name) {
        return Arrays.stream(values()).filter(e -> e.name.equals(name)).findFirst().orElse(null);
    }

    public static ClassicProtocolExtension byNameAndVersion(String name, int version) {
        final ClassicProtocolExtension extension = byName(name);
        if (extension == null || !extension.supportsVersion(version)) {
            return null;
        }
        return extension;
    }

    public boolean supportsVersion(int version) {
        return this.supportedVersions.contains(version);
    }

    public IntSet getSupportedVersions() {
        return supportedVersions;
    }

    public int getHighestSupportedVersion() {
        int highest = 0;
        for (int supportedVersion : this.supportedVersions) {
            if (supportedVersion > highest) highest = supportedVersion;
        }
        return highest;
    }

    public boolean isSupported() {
        return !this.supportedVersions.isEmpty();
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

}
