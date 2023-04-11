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
package net.raphimc.vialegacy.platform;

import com.google.common.collect.Range;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.protocol.ProtocolManager;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import net.raphimc.vialegacy.ViaLegacy;
import net.raphimc.vialegacy.ViaLegacyConfig;
import net.raphimc.vialegacy.api.LegacyProtocolVersion;
import net.raphimc.vialegacy.protocols.alpha.protocola1_0_16_2toa1_0_15.Protocola1_0_16_2toa1_0_15;
import net.raphimc.vialegacy.protocols.alpha.protocola1_0_17_1_0_17_4toa1_0_16_2.Protocola1_0_17_1_0_17_4toa1_0_16_2;
import net.raphimc.vialegacy.protocols.alpha.protocola1_1_0_1_1_2_1toa1_0_17_1_0_17_4.Protocola1_1_0_1_1_2_1toa1_0_17_1_0_17_4;
import net.raphimc.vialegacy.protocols.alpha.protocola1_2_0_1_2_1_1toa1_1_0_1_1_2_1.Protocola1_2_0_1_2_1_1toa1_1_0_1_1_2_1;
import net.raphimc.vialegacy.protocols.alpha.protocola1_2_2toa1_2_0_1_2_1_1.Protocola1_2_2toa1_2_0_1_2_1_1;
import net.raphimc.vialegacy.protocols.alpha.protocola1_2_3_1_2_3_4toa1_2_2.Protocola1_2_3_1_2_3_4toa1_2_2;
import net.raphimc.vialegacy.protocols.alpha.protocola1_2_3_5_1_2_6toa1_2_3_1_2_3_4.Protocola1_2_3_5_1_2_6toa1_2_3_1_2_3_4;
import net.raphimc.vialegacy.protocols.alpha.protocolb1_0_1_1_1toa1_2_3_5_1_2_6.Protocolb1_0_1_1_1toa1_2_3_5_1_2_6;
import net.raphimc.vialegacy.protocols.beta.protocol1_0_0_1tob1_8_0_1.Protocol1_0_0_1tob1_8_0_1;
import net.raphimc.vialegacy.protocols.beta.protocolb1_1_2tob1_0_1_1.Protocolb1_1_2tob1_0_1_1;
import net.raphimc.vialegacy.protocols.beta.protocolb1_2_0_2tob1_1_2.Protocolb1_2_0_2tob1_1_2;
import net.raphimc.vialegacy.protocols.beta.protocolb1_3_0_1tob1_2_0_2.Protocolb1_3_0_1tob1_2_0_2;
import net.raphimc.vialegacy.protocols.beta.protocolb1_4_0_1tob1_3_0_1.Protocolb1_4_0_1tob1_3_0_1;
import net.raphimc.vialegacy.protocols.beta.protocolb1_5_0_2tob1_4_0_1.Protocolb1_5_0_2tob1_4_0_1;
import net.raphimc.vialegacy.protocols.beta.protocolb1_6_0_6tob1_5_0_2.Protocolb1_6_0_6tob1_5_0_2;
import net.raphimc.vialegacy.protocols.beta.protocolb1_7_0_3tob1_6_0_6.Protocolb1_7_0_3tob1_6_0_6;
import net.raphimc.vialegacy.protocols.beta.protocolb1_8_0_1tob1_7_0_3.Protocolb1_8_0_1tob1_7_0_3;
import net.raphimc.vialegacy.protocols.classic.protocola1_0_15toc0_28_30.Protocola1_0_15toc0_30;
import net.raphimc.vialegacy.protocols.classic.protocolc0_0_16a_02to0_0_15a_1.Protocolc0_0_16a_02to0_0_15a_1;
import net.raphimc.vialegacy.protocols.classic.protocolc0_0_18a_02toc0_0_16a_02.Protocolc0_0_18a_02toc0_0_16a_02;
import net.raphimc.vialegacy.protocols.classic.protocolc0_0_19a_06toc0_0_18a_02.Protocolc0_0_19a_06toc0_0_18a_02;
import net.raphimc.vialegacy.protocols.classic.protocolc0_0_20a_27toc0_0_19a_06.Protocolc0_27toc0_0_19a_06;
import net.raphimc.vialegacy.protocols.classic.protocolc0_28_30toc0_0_20a_27.Protocolc0_30toc0_27;
import net.raphimc.vialegacy.protocols.classic.protocolc0_28_30toc0_28_30cpe.Protocolc0_30toc0_30cpe;
import net.raphimc.vialegacy.protocols.release.protocol1_1to1_0_0_1.Protocol1_1to1_0_0_1;
import net.raphimc.vialegacy.protocols.release.protocol1_2_1_3to1_1.Protocol1_2_1_3to1_1;
import net.raphimc.vialegacy.protocols.release.protocol1_2_4_5to1_2_1_3.Protocol1_2_4_5to1_2_1_3;
import net.raphimc.vialegacy.protocols.release.protocol1_3_1_2to1_2_4_5.Protocol1_3_1_2to1_2_4_5;
import net.raphimc.vialegacy.protocols.release.protocol1_4_2to1_3_1_2.Protocol1_4_2to1_3_1_2;
import net.raphimc.vialegacy.protocols.release.protocol1_4_4_5to1_4_2.Protocol1_4_4_5to1_4_2;
import net.raphimc.vialegacy.protocols.release.protocol1_4_6_7to1_4_4_5.Protocol1_4_6_7to1_4_4_5;
import net.raphimc.vialegacy.protocols.release.protocol1_5_0_1to1_4_6_7.Protocol1_5_0_1to1_4_6_7;
import net.raphimc.vialegacy.protocols.release.protocol1_5_2to1_5_0_1.Protocol1_5_2to1_5_0_1;
import net.raphimc.vialegacy.protocols.release.protocol1_6_1to1_5_2.Protocol1_6_1to1_5_2;
import net.raphimc.vialegacy.protocols.release.protocol1_6_2to1_6_1.Protocol1_6_2to1_6_1;
import net.raphimc.vialegacy.protocols.release.protocol1_6_4to1_6_2.Protocol1_6_4to1_6_2;
import net.raphimc.vialegacy.protocols.release.protocol1_7_2_5to1_6_4.Protocol1_7_2_5to1_6_4;
import net.raphimc.vialegacy.protocols.release.protocol1_7_2_5to1_6_4.baseprotocols.EmptyBaseProtocol;
import net.raphimc.vialegacy.protocols.release.protocol1_7_6_10to1_7_2_5.Protocol1_7_6_10to1_7_2_5;
import net.raphimc.vialegacy.protocols.release.protocol1_8to1_7_6_10.Protocol1_8to1_7_6_10;

import java.io.File;
import java.util.logging.Logger;

public interface ViaLegacyPlatform {

    default void init(final File dataFolder) {
        final ViaLegacyConfig config = new ViaLegacyConfig(new File(dataFolder, "vialegacy.yml"));
        config.reloadConfig();
        ViaLegacy.init(this, config);
        Via.getManager().getSubPlatforms().add("ViaLegacy-" + ViaLegacy.VERSION);

        final ProtocolManager protocolManager = Via.getManager().getProtocolManager();
        protocolManager.registerProtocol(new Protocol1_8to1_7_6_10(), ProtocolVersion.v1_8, ProtocolVersion.v1_7_6);
        protocolManager.registerProtocol(new Protocol1_7_6_10to1_7_2_5(), ProtocolVersion.v1_7_6, ProtocolVersion.v1_7_1);
        protocolManager.registerProtocol(new Protocol1_7_2_5to1_6_4(), ProtocolVersion.v1_7_1, LegacyProtocolVersion.r1_6_4);
        protocolManager.registerProtocol(new Protocol1_6_4to1_6_2(), LegacyProtocolVersion.r1_6_4, LegacyProtocolVersion.r1_6_2);
        protocolManager.registerProtocol(new Protocol1_6_2to1_6_1(), LegacyProtocolVersion.r1_6_2, LegacyProtocolVersion.r1_6_1);
        protocolManager.registerProtocol(new Protocol1_6_1to1_5_2(), LegacyProtocolVersion.r1_6_1, LegacyProtocolVersion.r1_5_2);
        protocolManager.registerProtocol(new Protocol1_5_2to1_5_0_1(), LegacyProtocolVersion.r1_5_2, LegacyProtocolVersion.r1_5tor1_5_1);
        protocolManager.registerProtocol(new Protocol1_5_0_1to1_4_6_7(), LegacyProtocolVersion.r1_5tor1_5_1, LegacyProtocolVersion.r1_4_6tor1_4_7);
        protocolManager.registerProtocol(new Protocol1_4_6_7to1_4_4_5(), LegacyProtocolVersion.r1_4_6tor1_4_7, LegacyProtocolVersion.r1_4_4tor1_4_5);
        protocolManager.registerProtocol(new Protocol1_4_4_5to1_4_2(), LegacyProtocolVersion.r1_4_4tor1_4_5, LegacyProtocolVersion.r1_4_2);
        protocolManager.registerProtocol(new Protocol1_4_2to1_3_1_2(), LegacyProtocolVersion.r1_4_2, LegacyProtocolVersion.r1_3_1tor1_3_2);
        protocolManager.registerProtocol(new Protocol1_3_1_2to1_2_4_5(), LegacyProtocolVersion.r1_3_1tor1_3_2, LegacyProtocolVersion.r1_2_4tor1_2_5);
        protocolManager.registerProtocol(new Protocol1_2_4_5to1_2_1_3(), LegacyProtocolVersion.r1_2_4tor1_2_5, LegacyProtocolVersion.r1_2_1tor1_2_3);
        protocolManager.registerProtocol(new Protocol1_2_1_3to1_1(), LegacyProtocolVersion.r1_2_1tor1_2_3, LegacyProtocolVersion.r1_1);
        protocolManager.registerProtocol(new Protocol1_1to1_0_0_1(), LegacyProtocolVersion.r1_1, LegacyProtocolVersion.r1_0_0tor1_0_1);
        protocolManager.registerProtocol(new Protocol1_0_0_1tob1_8_0_1(), LegacyProtocolVersion.r1_0_0tor1_0_1, LegacyProtocolVersion.b1_8tob1_8_1);
        protocolManager.registerProtocol(new Protocolb1_8_0_1tob1_7_0_3(), LegacyProtocolVersion.b1_8tob1_8_1, LegacyProtocolVersion.b1_7tob1_7_3);
        protocolManager.registerProtocol(new Protocolb1_7_0_3tob1_6_0_6(), LegacyProtocolVersion.b1_7tob1_7_3, LegacyProtocolVersion.b1_6tob1_6_6);
        protocolManager.registerProtocol(new Protocolb1_6_0_6tob1_5_0_2(), LegacyProtocolVersion.b1_6tob1_6_6, LegacyProtocolVersion.b1_5tob1_5_2);
        protocolManager.registerProtocol(new Protocolb1_5_0_2tob1_4_0_1(), LegacyProtocolVersion.b1_5tob1_5_2, LegacyProtocolVersion.b1_4tob1_4_1);
        protocolManager.registerProtocol(new Protocolb1_4_0_1tob1_3_0_1(), LegacyProtocolVersion.b1_4tob1_4_1, LegacyProtocolVersion.b1_3tob1_3_1);
        protocolManager.registerProtocol(new Protocolb1_3_0_1tob1_2_0_2(), LegacyProtocolVersion.b1_3tob1_3_1, LegacyProtocolVersion.b1_2_0tob1_2_2);
        protocolManager.registerProtocol(new Protocolb1_2_0_2tob1_1_2(), LegacyProtocolVersion.b1_2_0tob1_2_2, LegacyProtocolVersion.b1_1_2);
        protocolManager.registerProtocol(new Protocolb1_1_2tob1_0_1_1(), LegacyProtocolVersion.b1_1_2, LegacyProtocolVersion.b1_0tob1_1_1);
        protocolManager.registerProtocol(new Protocolb1_0_1_1_1toa1_2_3_5_1_2_6(), LegacyProtocolVersion.b1_0tob1_1_1, LegacyProtocolVersion.a1_2_3_5toa1_2_6);
        protocolManager.registerProtocol(new Protocola1_2_3_5_1_2_6toa1_2_3_1_2_3_4(), LegacyProtocolVersion.a1_2_3_5toa1_2_6, LegacyProtocolVersion.a1_2_3toa1_2_3_4);
        protocolManager.registerProtocol(new Protocola1_2_3_1_2_3_4toa1_2_2(), LegacyProtocolVersion.a1_2_3toa1_2_3_4, LegacyProtocolVersion.a1_2_2);
        protocolManager.registerProtocol(new Protocola1_2_2toa1_2_0_1_2_1_1(), LegacyProtocolVersion.a1_2_2, LegacyProtocolVersion.a1_2_0toa1_2_1_1);
        protocolManager.registerProtocol(new Protocola1_2_0_1_2_1_1toa1_1_0_1_1_2_1(), LegacyProtocolVersion.a1_2_0toa1_2_1_1, LegacyProtocolVersion.a1_1_0toa1_1_2_1);
        protocolManager.registerProtocol(new Protocola1_1_0_1_1_2_1toa1_0_17_1_0_17_4(), LegacyProtocolVersion.a1_1_0toa1_1_2_1, LegacyProtocolVersion.a1_0_17toa1_0_17_4);
        protocolManager.registerProtocol(new Protocola1_0_17_1_0_17_4toa1_0_16_2(), LegacyProtocolVersion.a1_0_17toa1_0_17_4, LegacyProtocolVersion.a1_0_16toa1_0_16_2);
        protocolManager.registerProtocol(new Protocola1_0_16_2toa1_0_15(), LegacyProtocolVersion.a1_0_16toa1_0_16_2, LegacyProtocolVersion.a1_0_15);
        protocolManager.registerProtocol(new Protocola1_0_15toc0_30(), LegacyProtocolVersion.a1_0_15, LegacyProtocolVersion.c0_28toc0_30);
        protocolManager.registerProtocol(new Protocolc0_30toc0_30cpe(), LegacyProtocolVersion.c0_28toc0_30, LegacyProtocolVersion.c0_30cpe);
        protocolManager.registerProtocol(new Protocolc0_30toc0_27(), LegacyProtocolVersion.c0_28toc0_30, LegacyProtocolVersion.c0_0_20ac0_27);
        protocolManager.registerProtocol(new Protocolc0_27toc0_0_19a_06(), LegacyProtocolVersion.c0_0_20ac0_27, LegacyProtocolVersion.c0_0_19a_06);
        protocolManager.registerProtocol(new Protocolc0_0_19a_06toc0_0_18a_02(), LegacyProtocolVersion.c0_0_19a_06, LegacyProtocolVersion.c0_0_18a_02);
        protocolManager.registerProtocol(new Protocolc0_0_18a_02toc0_0_16a_02(), LegacyProtocolVersion.c0_0_18a_02, LegacyProtocolVersion.c0_0_16a_02);
        protocolManager.registerProtocol(new Protocolc0_0_16a_02to0_0_15a_1(), LegacyProtocolVersion.c0_0_16a_02, LegacyProtocolVersion.c0_0_15a_1);

        for (ProtocolVersion version : LegacyProtocolVersion.PROTOCOLS) {
            Via.getManager().getProtocolManager().registerBaseProtocol(new EmptyBaseProtocol(), Range.singleton(version.getVersion()));
        }
    }

    Logger getLogger();

    File getDataFolder();

    /**
     * Returns the name which will be sent to classic CPE servers as the client's application name.
     *
     * @return The CPE application name.
     */
    default String getCpeAppName() {
        return "ViaLegacy " + ViaLegacy.VERSION;
    }

}
