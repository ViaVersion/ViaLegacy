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
package net.raphimc.vialegacy.api;

import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.api.protocol.version.SubVersionRange;
import com.viaversion.viaversion.api.protocol.version.VersionType;
import com.viaversion.viaversion.protocol.RedirectProtocolVersion;

import java.util.ArrayList;
import java.util.List;

import static com.viaversion.viaversion.api.protocol.version.VersionType.*;

public class LegacyProtocolVersion {

    public static final List<ProtocolVersion> PROTOCOLS = new ArrayList<>();

    public static final ProtocolVersion c0_0_15a_1 = registerLegacy(CLASSIC, 0, "c0.0.15a-1"); // this version has no protocol id
    public static final ProtocolVersion c0_0_16a_02 = registerLegacy(CLASSIC, 3, "c0.0.16a-02");
    public static final ProtocolVersion c0_0_18a_02 = registerLegacy(CLASSIC, 4, "c0.0.18a-02");
    public static final ProtocolVersion c0_0_19a_06 = registerLegacy(CLASSIC, 5, "c0.0.19a-06");
    public static final ProtocolVersion c0_0_20ac0_27 = registerLegacy(CLASSIC, 6, "c0.0.20a-c0.27");
    public static final ProtocolVersion c0_28toc0_30 = registerLegacy(CLASSIC, 7, "c0.28-c0.30");
    public static final ProtocolVersion a1_0_15 = registerLegacy(ALPHA_INITIAL, 13, "a1.0.15");
    public static final ProtocolVersion a1_0_16toa1_0_16_2 = registerLegacy(ALPHA_INITIAL, 14, "a1.0.16-a1.0.16.2", new SubVersionRange("a1.0.16", 0, 2));
    public static final ProtocolVersion a1_0_17toa1_0_17_4 = registerLegacy(ALPHA_LATER, 1, "a1.0.17-a1.0.17.4", new SubVersionRange("a1.0.17", 0, 4));
    public static final ProtocolVersion a1_1_0toa1_1_2_1 = registerLegacy(ALPHA_LATER, 2, "a1.1.0-a1.1.2.1", new SubVersionRange("a1.1", 0, 2));
    public static final ProtocolVersion a1_2_0toa1_2_1_1 = registerLegacy(ALPHA_LATER, 3, "a1.2.0-a1.2.1.1", new SubVersionRange("a1.2", 0, 1));
    public static final ProtocolVersion a1_2_2 = registerLegacy(ALPHA_LATER, 4, "a1.2.2");
    public static final ProtocolVersion a1_2_3toa1_2_3_4 = registerLegacy(ALPHA_LATER, 5, "a1.2.3-a1.2.3.4", new SubVersionRange("a1.2.3", 0, 4));
    public static final ProtocolVersion a1_2_3_5toa1_2_6 = registerLegacy(ALPHA_LATER, 6, "a1.2.3.5-a1.2.6", new SubVersionRange("a1.2.3", 5, 6));
    public static final ProtocolVersion b1_0tob1_1_1 = registerLegacy(BETA_INITIAL, 7, "b1.0-b1.1.1", new SubVersionRange("b1.0", 0, 1));
    public static final ProtocolVersion b1_1_2 = registerLegacy(BETA_INITIAL, 8, "b1.1.2"); // yes its id 8 and incompatible with b1.2-b1.2.2. Thanks mojank
    public static final ProtocolVersion b1_2_0tob1_2_2 = registerLegacy(BETA_LATER, 8, "b1.2-b1.2.2", new SubVersionRange("b1.2", 0, 2));
    public static final ProtocolVersion b1_3tob1_3_1 = registerLegacy(BETA_LATER, 9, "b1.3-b1.3.1", new SubVersionRange("b1.3", 0, 1));
    public static final ProtocolVersion b1_4tob1_4_1 = registerLegacy(BETA_LATER, 10, "b1.4-b1.4.1", new SubVersionRange("b1.4", 0, 1));
    public static final ProtocolVersion b1_5tob1_5_2 = registerLegacy(BETA_LATER, 11, "b1.5-b1.5.2", new SubVersionRange("b1.5", 0, 2));
    public static final ProtocolVersion b1_6tob1_6_6 = registerLegacy(BETA_LATER, 13, "b1.6-b1.6.6", new SubVersionRange("b1.6", 0, 6));
    public static final ProtocolVersion b1_7tob1_7_3 = registerLegacy(BETA_LATER, 14, "b1.7-b1.7.3", new SubVersionRange("b1.7", 0, 3));
    public static final ProtocolVersion b1_8tob1_8_1 = registerLegacy(BETA_LATER, 17, "b1.8-b1.8.1", new SubVersionRange("b1.8", 0, 1));
    public static final ProtocolVersion r1_0_0tor1_0_1 = registerLegacy(RELEASE_INITIAL, 22, "1.0.0-1.0.1", new SubVersionRange("1.0", 0, 1));
    public static final ProtocolVersion r1_1 = registerLegacy(RELEASE_INITIAL, 23, "1.1");
    public static final ProtocolVersion r1_2_1tor1_2_3 = registerLegacy(RELEASE_INITIAL, 28, "1.2.1-1.2.3", new SubVersionRange("1.2", 1, 3));
    public static final ProtocolVersion r1_2_4tor1_2_5 = registerLegacy(RELEASE_INITIAL, 29, "1.2.4-1.2.5", new SubVersionRange("1.2", 4, 5));
    public static final ProtocolVersion r1_3_1tor1_3_2 = registerLegacy(RELEASE_INITIAL, 39, "1.3.1-1.3.2", new SubVersionRange("1.3", 1, 2));
    public static final ProtocolVersion r1_4_2 = registerLegacy(RELEASE_INITIAL, 47, "1.4.2");
    public static final ProtocolVersion r1_4_4tor1_4_5 = registerLegacy(RELEASE_INITIAL, 49, "1.4.4-1.4.5", new SubVersionRange("1.4", 4, 5));
    public static final ProtocolVersion r1_4_6tor1_4_7 = registerLegacy(RELEASE_INITIAL, 51, "1.4.6-1.4.7", new SubVersionRange("1.4", 6, 7));
    public static final ProtocolVersion r1_5tor1_5_1 = registerLegacy(RELEASE_INITIAL, 60, "1.5-1.5.1", new SubVersionRange("1.5", 0, 1));
    public static final ProtocolVersion r1_5_2 = registerLegacy(RELEASE_INITIAL, 61, "1.5.2");
    public static final ProtocolVersion r1_6_1 = registerLegacy(RELEASE_INITIAL, 73, "1.6.1");
    public static final ProtocolVersion r1_6_2 = registerLegacy(RELEASE_INITIAL, 74, "1.6.2");
    public static final ProtocolVersion r1_6_4 = registerLegacy(RELEASE_INITIAL, 78, "1.6.4");

    public static final ProtocolVersion c0_30cpe = new RedirectProtocolVersion(7, "c0.30 CPE", c0_28toc0_30);

    static {
        ProtocolVersion.register(c0_30cpe);
        PROTOCOLS.add(c0_30cpe);
    }

    private static ProtocolVersion registerLegacy(final VersionType versionType, final int version, final String name) {
        return registerLegacy(versionType, version, name, null);
    }

    private static ProtocolVersion registerLegacy(final VersionType versionType, final int version, final String name, final SubVersionRange versionRange) {
        final ProtocolVersion protocolVersion = new ProtocolVersion(versionType, version, -1, name, versionRange);
        ProtocolVersion.register(protocolVersion);
        PROTOCOLS.add(protocolVersion);
        return protocolVersion;
    }

}
