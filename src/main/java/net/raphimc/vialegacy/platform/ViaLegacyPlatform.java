package net.raphimc.vialegacy.platform;

import com.google.common.collect.Range;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.protocol.ProtocolManager;
import com.viaversion.viaversion.protocols.base.BaseProtocol1_16;
import net.raphimc.vialegacy.ViaLegacy;
import net.raphimc.vialegacy.ViaLegacyConfig;
import net.raphimc.vialegacy.protocols.alpha.protocola1_0_16_2toa1_0_15.Protocola1_0_16_2toa1_0_15;
import net.raphimc.vialegacy.protocols.alpha.protocola1_0_17_1_0_17_4toa1_0_16_2.Protocola1_0_17_1_0_17_4toa1_0_16_2;
import net.raphimc.vialegacy.protocols.alpha.protocola1_0_17_1_0_17_4toa1_0_16_2.task.TimeLockTask;
import net.raphimc.vialegacy.protocols.alpha.protocola1_1_0_1_1_2_1toa1_0_17_1_0_17_4.Protocola1_1_0_1_1_2_1toa1_0_17_1_0_17_4;
import net.raphimc.vialegacy.protocols.alpha.protocola1_2_0_1_2_1_1toa1_1_0_1_1_2_1.Protocola1_2_0_1_2_1_1toa1_1_0_1_1_2_1;
import net.raphimc.vialegacy.protocols.alpha.protocola1_2_2toa1_2_0_1_2_1_1.Protocola1_2_2toa1_2_0_1_2_1_1;
import net.raphimc.vialegacy.protocols.alpha.protocola1_2_3_1_2_3_4toa1_2_2.Protocola1_2_3_1_2_3_4toa1_2_2;
import net.raphimc.vialegacy.protocols.alpha.protocola1_2_3_5_1_2_6toa1_2_3_1_2_3_4.Protocola1_2_3_5_1_2_6toa1_2_3_1_2_3_4;
import net.raphimc.vialegacy.protocols.alpha.protocolb1_0_1_1_1toa1_2_3_5_1_2_6.Protocolb1_0_1_1_1toa1_2_3_5_1_2_6;
import net.raphimc.vialegacy.protocols.alpha.protocolb1_0_1_1_1toa1_2_3_5_1_2_6.task.AlphaInventoryUpdateTask;
import net.raphimc.vialegacy.protocols.beta.protocol1_0_0_1tob1_8_0_1.Protocol1_0_0_1tob1_8_0_1;
import net.raphimc.vialegacy.protocols.beta.protocol1_0_0_1tob1_8_0_1.task.PlayerAirTimeUpdateTask;
import net.raphimc.vialegacy.protocols.beta.protocolb1_1_2tob1_0_1_1.Protocolb1_1_2tob1_0_1_1;
import net.raphimc.vialegacy.protocols.beta.protocolb1_2_0_2tob1_1_2.Protocolb1_2_0_2tob1_1_2;
import net.raphimc.vialegacy.protocols.beta.protocolb1_3_0_1tob1_2_0_2.Protocolb1_3_0_1tob1_2_0_2;
import net.raphimc.vialegacy.protocols.beta.protocolb1_3_0_1tob1_2_0_2.task.BlockDigTickTask;
import net.raphimc.vialegacy.protocols.beta.protocolb1_4_0_1tob1_3_0_1.Protocolb1_4_0_1tob1_3_0_1;
import net.raphimc.vialegacy.protocols.beta.protocolb1_5_0_2tob1_4_0_1.Protocolb1_5_0_2tob1_4_0_1;
import net.raphimc.vialegacy.protocols.beta.protocolb1_6_0_6tob1_5_0_2.Protocolb1_6_0_6tob1_5_0_2;
import net.raphimc.vialegacy.protocols.beta.protocolb1_6_0_6tob1_5_0_2.task.TimeTrackTask;
import net.raphimc.vialegacy.protocols.beta.protocolb1_7_0_3tob1_6_0_6.Protocolb1_7_0_3tob1_6_0_6;
import net.raphimc.vialegacy.protocols.beta.protocolb1_8_0_1tob1_7_0_3.Protocolb1_8_0_1tob1_7_0_3;
import net.raphimc.vialegacy.protocols.classic.protocola1_0_15toc0_28_30.Protocola1_0_15toc0_30;
import net.raphimc.vialegacy.protocols.classic.protocolc0_0_16a_02to0_0_15a_1.Protocolc0_0_16a_02to0_0_15a_1;
import net.raphimc.vialegacy.protocols.classic.protocolc0_0_18a_02toc0_0_16a_02.Protocolc0_0_18a_02toc0_0_16a_02;
import net.raphimc.vialegacy.protocols.classic.protocolc0_0_19a_06toc0_0_18a_02.Protocolc0_0_19a_06toc0_0_18a_02;
import net.raphimc.vialegacy.protocols.classic.protocolc0_0_20a_27toc0_0_19a_06.Protocolc0_27toc0_0_19a_06;
import net.raphimc.vialegacy.protocols.classic.protocolc0_28_30toc0_0_20a_27.Protocolc0_30toc0_27;
import net.raphimc.vialegacy.protocols.classic.protocolc0_28_30toc0_28_30cpe.Protocolc0_30toc0_30cpe;
import net.raphimc.vialegacy.protocols.classic.protocolc0_28_30toc0_28_30cpe.task.ClassicPingTask;
import net.raphimc.vialegacy.protocols.release.protocol1_1to1_0_0_1.Protocol1_1to1_0_0_1;
import net.raphimc.vialegacy.protocols.release.protocol1_2_1_3to1_1.Protocol1_2_1_3to1_1;
import net.raphimc.vialegacy.protocols.release.protocol1_2_1_3to1_1.tasks.BlockReceiveInvalidatorTask;
import net.raphimc.vialegacy.protocols.release.protocol1_2_4_5to1_2_1_3.Protocol1_2_4_5to1_2_1_3;
import net.raphimc.vialegacy.protocols.release.protocol1_3_1_2to1_2_4_5.Protocol1_3_1_2to1_2_4_5;
import net.raphimc.vialegacy.protocols.release.protocol1_3_1_2to1_2_4_5.task.EntityTrackerTickTask;
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
import net.raphimc.vialegacy.protocols.snapshot.protocol1_14to3D_Shareware.Protocol1_14to3D_Shareware;
import net.raphimc.vialegacy.protocols.snapshot.protocol1_16_2toCombatTest8c.Protocol1_16_2toCombatTest8c;
import net.raphimc.vialegacy.protocols.snapshot.protocol1_16to20w14infinite.Protocol1_16to20w14infinite;
import net.raphimc.vialegacy.protocols.snapshot.protocol3D_Sharewareto1_14.Protocol3D_Sharewareto1_14;
import net.raphimc.vialegacy.util.VersionEnum;

import java.io.File;
import java.util.logging.Logger;

public interface ViaLegacyPlatform {

    default void init(final File dataFolder) {
        final ViaLegacyConfig config = new ViaLegacyConfig(new File(dataFolder, "vialegacy.yml"));
        config.reloadConfig();

        ViaLegacy.init(this, config);
        VersionEnum.init();

        Via.getManager().getSubPlatforms().add("ViaLegacy-" + ViaLegacy.VERSION);

        final ProtocolManager protocolManager = Via.getManager().getProtocolManager();
        protocolManager.registerProtocol(new Protocol1_8to1_7_6_10(), VersionEnum.r1_8.getProtocol(), VersionEnum.r1_7_6tor1_7_10.getProtocol());
        protocolManager.registerProtocol(new Protocol1_7_6_10to1_7_2_5(), VersionEnum.r1_7_6tor1_7_10.getProtocol(), VersionEnum.r1_7_2tor1_7_5.getProtocol());
        protocolManager.registerProtocol(new Protocol1_7_2_5to1_6_4(), VersionEnum.r1_7_2tor1_7_5.getProtocol(), VersionEnum.r1_6_4.getProtocol());
        protocolManager.registerProtocol(new Protocol1_6_4to1_6_2(), VersionEnum.r1_6_4.getProtocol(), VersionEnum.r1_6_2.getProtocol());
        protocolManager.registerProtocol(new Protocol1_6_2to1_6_1(), VersionEnum.r1_6_2.getProtocol(), VersionEnum.r1_6_1.getProtocol());
        protocolManager.registerProtocol(new Protocol1_6_1to1_5_2(), VersionEnum.r1_6_1.getProtocol(), VersionEnum.r1_5_2.getProtocol());
        protocolManager.registerProtocol(new Protocol1_5_2to1_5_0_1(), VersionEnum.r1_5_2.getProtocol(), VersionEnum.r1_5tor1_5_1.getProtocol());
        protocolManager.registerProtocol(new Protocol1_5_0_1to1_4_6_7(), VersionEnum.r1_5tor1_5_1.getProtocol(), VersionEnum.r1_4_6tor1_4_7.getProtocol());
        protocolManager.registerProtocol(new Protocol1_4_6_7to1_4_4_5(), VersionEnum.r1_4_6tor1_4_7.getProtocol(), VersionEnum.r1_4_4tor1_4_5.getProtocol());
        protocolManager.registerProtocol(new Protocol1_4_4_5to1_4_2(), VersionEnum.r1_4_4tor1_4_5.getProtocol(), VersionEnum.r1_4_2.getProtocol());
        protocolManager.registerProtocol(new Protocol1_4_2to1_3_1_2(), VersionEnum.r1_4_2.getProtocol(), VersionEnum.r1_3_1tor1_3_2.getProtocol());
        protocolManager.registerProtocol(new Protocol1_3_1_2to1_2_4_5(), VersionEnum.r1_3_1tor1_3_2.getProtocol(), VersionEnum.r1_2_4tor1_2_5.getProtocol());
        protocolManager.registerProtocol(new Protocol1_2_4_5to1_2_1_3(), VersionEnum.r1_2_4tor1_2_5.getProtocol(), VersionEnum.r1_2_1tor1_2_3.getProtocol());
        protocolManager.registerProtocol(new Protocol1_2_1_3to1_1(), VersionEnum.r1_2_1tor1_2_3.getProtocol(), VersionEnum.r1_1.getProtocol());
        protocolManager.registerProtocol(new Protocol1_1to1_0_0_1(), VersionEnum.r1_1.getProtocol(), VersionEnum.r1_0_0tor1_0_1.getProtocol());
        protocolManager.registerProtocol(new Protocol1_0_0_1tob1_8_0_1(), VersionEnum.r1_0_0tor1_0_1.getProtocol(), VersionEnum.b1_8tob1_8_1.getProtocol());
        protocolManager.registerProtocol(new Protocolb1_8_0_1tob1_7_0_3(), VersionEnum.b1_8tob1_8_1.getProtocol(), VersionEnum.b1_7tob1_7_3.getProtocol());
        protocolManager.registerProtocol(new Protocolb1_7_0_3tob1_6_0_6(), VersionEnum.b1_7tob1_7_3.getProtocol(), VersionEnum.b1_6tob1_6_6.getProtocol());
        protocolManager.registerProtocol(new Protocolb1_6_0_6tob1_5_0_2(), VersionEnum.b1_6tob1_6_6.getProtocol(), VersionEnum.b1_5tob1_5_2.getProtocol());
        protocolManager.registerProtocol(new Protocolb1_5_0_2tob1_4_0_1(), VersionEnum.b1_5tob1_5_2.getProtocol(), VersionEnum.b1_4tob1_4_1.getProtocol());
        protocolManager.registerProtocol(new Protocolb1_4_0_1tob1_3_0_1(), VersionEnum.b1_4tob1_4_1.getProtocol(), VersionEnum.b1_3tob1_3_1.getProtocol());
        protocolManager.registerProtocol(new Protocolb1_3_0_1tob1_2_0_2(), VersionEnum.b1_3tob1_3_1.getProtocol(), VersionEnum.b1_2_0tob1_2_2.getProtocol());
        protocolManager.registerProtocol(new Protocolb1_2_0_2tob1_1_2(), VersionEnum.b1_2_0tob1_2_2.getProtocol(), VersionEnum.b1_1_2.getProtocol());
        protocolManager.registerProtocol(new Protocolb1_1_2tob1_0_1_1(), VersionEnum.b1_1_2.getProtocol(), VersionEnum.b1_0tob1_1_1.getProtocol());
        protocolManager.registerProtocol(new Protocolb1_0_1_1_1toa1_2_3_5_1_2_6(), VersionEnum.b1_0tob1_1_1.getProtocol(), VersionEnum.a1_2_3_5toa1_2_6.getProtocol());
        protocolManager.registerProtocol(new Protocola1_2_3_5_1_2_6toa1_2_3_1_2_3_4(), VersionEnum.a1_2_3_5toa1_2_6.getProtocol(), VersionEnum.a1_2_3toa1_2_3_4.getProtocol());
        protocolManager.registerProtocol(new Protocola1_2_3_1_2_3_4toa1_2_2(), VersionEnum.a1_2_3toa1_2_3_4.getProtocol(), VersionEnum.a1_2_2.getProtocol());
        protocolManager.registerProtocol(new Protocola1_2_2toa1_2_0_1_2_1_1(), VersionEnum.a1_2_2.getProtocol(), VersionEnum.a1_2_0toa1_2_1_1.getProtocol());
        protocolManager.registerProtocol(new Protocola1_2_0_1_2_1_1toa1_1_0_1_1_2_1(), VersionEnum.a1_2_0toa1_2_1_1.getProtocol(), VersionEnum.a1_1_0toa1_1_2_1.getProtocol());
        protocolManager.registerProtocol(new Protocola1_1_0_1_1_2_1toa1_0_17_1_0_17_4(), VersionEnum.a1_1_0toa1_1_2_1.getProtocol(), VersionEnum.a1_0_17toa1_0_17_4.getProtocol());
        protocolManager.registerProtocol(new Protocola1_0_17_1_0_17_4toa1_0_16_2(), VersionEnum.a1_0_17toa1_0_17_4.getProtocol(), VersionEnum.a1_0_16toa1_0_16_2.getProtocol());
        protocolManager.registerProtocol(new Protocola1_0_16_2toa1_0_15(), VersionEnum.a1_0_16toa1_0_16_2.getProtocol(), VersionEnum.a1_0_15.getProtocol());
        protocolManager.registerProtocol(new Protocola1_0_15toc0_30(), VersionEnum.a1_0_15.getProtocol(), VersionEnum.c0_28toc0_30.getProtocol());
        protocolManager.registerProtocol(new Protocolc0_30toc0_30cpe(), VersionEnum.c0_28toc0_30.getProtocol(), VersionEnum.c0_30cpe.getProtocol());
        protocolManager.registerProtocol(new Protocolc0_30toc0_27(), VersionEnum.c0_28toc0_30.getProtocol(), VersionEnum.c0_0_20ac0_27.getProtocol());
        protocolManager.registerProtocol(new Protocolc0_27toc0_0_19a_06(), VersionEnum.c0_0_20ac0_27.getProtocol(), VersionEnum.c0_0_19a_06.getProtocol());
        protocolManager.registerProtocol(new Protocolc0_0_19a_06toc0_0_18a_02(), VersionEnum.c0_0_19a_06.getProtocol(), VersionEnum.c0_0_18a_02.getProtocol());
        protocolManager.registerProtocol(new Protocolc0_0_18a_02toc0_0_16a_02(), VersionEnum.c0_0_18a_02.getProtocol(), VersionEnum.c0_0_16a_02.getProtocol());
        protocolManager.registerProtocol(new Protocolc0_0_16a_02to0_0_15a_1(), VersionEnum.c0_0_16a_02.getProtocol(), VersionEnum.c0_0_15a_1.getProtocol());

        protocolManager.registerProtocol(new Protocol1_14to3D_Shareware(), VersionEnum.r1_14.getProtocol(), VersionEnum.s3d_shareware.getProtocol());
        protocolManager.registerProtocol(new Protocol3D_Sharewareto1_14(), VersionEnum.s3d_shareware.getProtocol(), VersionEnum.r1_14.getProtocol());
        protocolManager.registerProtocol(new Protocol1_16to20w14infinite(), VersionEnum.r1_16.getProtocol(), VersionEnum.s20w14infinite.getProtocol());
        protocolManager.registerProtocol(new Protocol1_16_2toCombatTest8c(), VersionEnum.r1_16_2.getProtocol(), VersionEnum.sCombatTest8c.getProtocol());

        for (VersionEnum version : VersionEnum.LEGACY_VERSIONS) {
            Via.getManager().getProtocolManager().registerBaseProtocol(new EmptyBaseProtocol(), Range.singleton(version.getVersion()));
        }
        protocolManager.registerBaseProtocol(new BaseProtocol1_16(), Range.singleton(VersionEnum.s20w14infinite.getVersion()));
        protocolManager.registerBaseProtocol(new BaseProtocol1_16(), Range.singleton(VersionEnum.sCombatTest8c.getVersion()));

        if (ViaLegacy.getConfig().isSoundEmulation()) Via.getPlatform().runRepeatingSync(new EntityTrackerTickTask(), 1L);
        Via.getPlatform().runRepeatingSync(new BlockReceiveInvalidatorTask(), 1L);
        Via.getPlatform().runRepeatingSync(new PlayerAirTimeUpdateTask(), 1L);
        Via.getPlatform().runRepeatingSync(new TimeTrackTask(), 1L);
        Via.getPlatform().runRepeatingSync(new BlockDigTickTask(), 1L);
        Via.getPlatform().runRepeatingSync(new AlphaInventoryUpdateTask(), 20L);
        Via.getPlatform().runRepeatingSync(new TimeLockTask(), 20L);
        Via.getPlatform().runRepeatingSync(new ClassicPingTask(), 20L);
    }

    Logger getLogger();

    File getDataFolder();

}
