package net.raphimc.vialegacy.util;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.api.protocol.version.VersionRange;

import java.util.*;

public enum VersionEnum {

    c0_0_15a_1(-2 << 2 | 1, "c0.0.15a-1"), // this version has no id
    c0_0_16a_02(-3 << 2 | 1, "c0.0.16a-02"),
    c0_0_18a_02(-4 << 2 | 1, "c0.0.18a-02"),
    c0_0_19a_06(-5 << 2 | 1, "c0.0.19a-06"),
    c0_0_20ac0_27(-6 << 2 | 1, "c0.0.20a-c0.27"),
    c0_30cpe(-7 << 2 | 2, "c0.30 CPE"),
    c0_28toc0_30(-7 << 2 | 1, "c0.28-c0.30"),
    a1_0_15(-13 << 2 | 1, "a1.0.15"),
    a1_0_16toa1_0_16_2(-14 << 2 | 1, "a1.0.16-a1.0.16.2"),
    a1_0_17toa1_0_17_4(-27 << 2, "a1.0.17-a1.0.17.4"),
    a1_1_0toa1_1_2_1(-2 << 2, "a1.1.0-a1.1.2.1"),
    a1_2_0toa1_2_1_1(-3 << 2, "a1.2.0-a1.2.1.1"),
    a1_2_2(-4 << 2, "a1.2.2"),
    a1_2_3toa1_2_3_4(-5 << 2, "a1.2.3-a1.2.3.4"),
    a1_2_3_5toa1_2_6(-6 << 2, "a1.2.3.5-a1.2.6"),
    b1_0tob1_1_1(-7 << 2, "b1.0-b1.1.1"),
    b1_1_2(-8 << 2 | 1, "b1.1.2"), // yes its 100% id 8 and incompatible with b1.2-b1.2.2
    b1_2_0tob1_2_2(-8 << 2, "b1.2-b1.2.2"),
    b1_3tob1_3_1(-9 << 2, "b1.3-b1.3.1"),
    b1_4tob1_4_1(-10 << 2, "b1.4-b1.4.1"),
    b1_5tob1_5_2(-11 << 2, "b1.5-b1.5.2"),
    b1_6tob1_6_6(-13 << 2, "b1.6-b1.6.6"),
    b1_7tob1_7_3(-14 << 2, "b1.7-b1.7.3"),
    b1_8tob1_8_1(-17 << 2, "b1.8-b1.8.1"),
    r1_0_0tor1_0_1(-22 << 2, "1.0.0-1.0.1", new VersionRange("1.0", 0, 1)),
    r1_1(-23 << 2, "1.1"),
    r1_2_1tor1_2_3(-28 << 2, "1.2.1-1.2.3", new VersionRange("1.2", 1, 3)),
    r1_2_4tor1_2_5(-29 << 2, "1.2.4-1.2.5", new VersionRange("1.2", 4, 5)),
    r1_3_1tor1_3_2(-39 << 2, "1.3.1-1.3.2", new VersionRange("1.3", 1, 2)),
    r1_4_2(-47 << 2, "1.4.2"),
    r1_4_4tor1_4_5(-49 << 2, "1.4.4-1.4.5", new VersionRange("1.4", 4, 5)),
    r1_4_6tor1_4_7(-51 << 2, "1.4.6-1.4.7", new VersionRange("1.4", 6, 7)),
    r1_5tor1_5_1(-60 << 2, "1.5-1.5.1"),
    r1_5_2(-61 << 2, "1.5.2"),
    r1_6_1(-73 << 2, "1.6.1"),
    r1_6_2(-74 << 2, "1.6.2"),
    r1_6_3(-77 << 2, "1.6.3"),
    r1_6_4(-78 << 2, "1.6.4"),
    r1_7_2tor1_7_5(ProtocolVersion.v1_7_1),
    r1_7_6tor1_7_10(ProtocolVersion.v1_7_6),
    r1_8(ProtocolVersion.v1_8),
    r1_9(ProtocolVersion.v1_9),
    r1_9_1(ProtocolVersion.v1_9_1),
    r1_9_2(ProtocolVersion.v1_9_2),
    r1_9_3tor1_9_4(ProtocolVersion.v1_9_3),
    r1_10(ProtocolVersion.v1_10),
    r1_11(ProtocolVersion.v1_11),
    r1_11_1to1_11_2(ProtocolVersion.v1_11_1),
    r1_12(ProtocolVersion.v1_12),
    r1_12_1(ProtocolVersion.v1_12_1),
    r1_12_2(ProtocolVersion.v1_12_2),
    r1_13(ProtocolVersion.v1_13),
    r1_13_1(ProtocolVersion.v1_13_1),
    r1_13_2(ProtocolVersion.v1_13_2),
    s3d_shareware(1, "3D Shareware"),
    r1_14(ProtocolVersion.v1_14),
    r1_14_1(ProtocolVersion.v1_14_1),
    r1_14_2(ProtocolVersion.v1_14_2),
    r1_14_3(ProtocolVersion.v1_14_3),
    r1_14_4(ProtocolVersion.v1_14_4),
    r1_15(ProtocolVersion.v1_15),
    r1_15_1(ProtocolVersion.v1_15_1),
    r1_15_2(ProtocolVersion.v1_15_2),
    s20w14infinite(709, "20w14infinite"),
    r1_16(ProtocolVersion.v1_16),
    r1_16_1(ProtocolVersion.v1_16_1),
    sCombatTest8c(803, "Combat Test 8c"),
    r1_16_2(ProtocolVersion.v1_16_2),
    r1_16_3(ProtocolVersion.v1_16_3),
    r1_16_4tor1_16_5(ProtocolVersion.v1_16_4),
    r1_17(ProtocolVersion.v1_17),
    r1_17_1(ProtocolVersion.v1_17_1),
    r1_18tor1_18_1(ProtocolVersion.v1_18),
    r1_18_2(ProtocolVersion.v1_18_2),
    r1_19(ProtocolVersion.v1_19),
    r1_19_1tor1_19_2(ProtocolVersion.v1_19_1),
    r1_19_3(ProtocolVersion.v1_19_3),

    //
    UNKNOWN(ProtocolVersion.unknown), // Not in Registry
    ;


    private static final Map<ProtocolVersion, VersionEnum> VERSION_REGISTRY = new HashMap<>();
    public static final List<VersionEnum> RENDER_VERSIONS = new ArrayList<>();
    public static final List<VersionEnum> LEGACY_VERSIONS = new ArrayList<>();
    public static final List<VersionEnum> OFFICIAL_SUPPORTED_PROTOCOLS = new ArrayList<>();

    static {
        for (VersionEnum version : VersionEnum.values()) {
            if (version == UNKNOWN) continue;
            VERSION_REGISTRY.put(version.getProtocol(), version);
        }
        for (VersionEnum version : VersionEnum.getAllVersions()) {
            if (version.isOlderThan(VersionEnum.r1_7_2tor1_7_5)) {
                LEGACY_VERSIONS.add(version);
            }
        }
        for (VersionEnum version : VersionEnum.getAllVersions()) {
            if (version.isNewerThan(VersionEnum.r1_6_4) && version != VersionEnum.s3d_shareware && version != VersionEnum.s20w14infinite && version != VersionEnum.sCombatTest8c) {
                OFFICIAL_SUPPORTED_PROTOCOLS.add(version);
            }
        }

        RENDER_VERSIONS.add(r1_19_3);
        RENDER_VERSIONS.add(r1_19_1tor1_19_2);
        RENDER_VERSIONS.add(r1_19);
        RENDER_VERSIONS.add(r1_18_2);
        RENDER_VERSIONS.add(r1_18tor1_18_1);
        RENDER_VERSIONS.add(r1_17_1);
        RENDER_VERSIONS.add(r1_17);
        RENDER_VERSIONS.add(r1_16_4tor1_16_5);
        RENDER_VERSIONS.add(r1_16_3);
        RENDER_VERSIONS.add(r1_16_2);
        RENDER_VERSIONS.add(r1_16_1);
        RENDER_VERSIONS.add(r1_16);
        RENDER_VERSIONS.add(r1_15_2);
        RENDER_VERSIONS.add(r1_15_1);
        RENDER_VERSIONS.add(r1_15);
        RENDER_VERSIONS.add(r1_14_4);
        RENDER_VERSIONS.add(r1_14_3);
        RENDER_VERSIONS.add(r1_14_2);
        RENDER_VERSIONS.add(r1_14_1);
        RENDER_VERSIONS.add(r1_14);
        RENDER_VERSIONS.add(r1_13_2);
        RENDER_VERSIONS.add(r1_13_1);
        RENDER_VERSIONS.add(r1_13);
        RENDER_VERSIONS.add(r1_12_2);
        RENDER_VERSIONS.add(r1_12_1);
        RENDER_VERSIONS.add(r1_12);
        RENDER_VERSIONS.add(r1_11_1to1_11_2);
        RENDER_VERSIONS.add(r1_11);
        RENDER_VERSIONS.add(r1_10);
        RENDER_VERSIONS.add(r1_9_3tor1_9_4);
        RENDER_VERSIONS.add(r1_9_2);
        RENDER_VERSIONS.add(r1_9_1);
        RENDER_VERSIONS.add(r1_9);
        RENDER_VERSIONS.add(r1_8);
        RENDER_VERSIONS.add(r1_7_6tor1_7_10);
        RENDER_VERSIONS.add(r1_7_2tor1_7_5);
        RENDER_VERSIONS.add(r1_6_4);
//        RENDER_VERSIONS.add(r1_6_3);
        RENDER_VERSIONS.add(r1_6_2);
        RENDER_VERSIONS.add(r1_6_1);
        RENDER_VERSIONS.add(r1_5_2);
        RENDER_VERSIONS.add(r1_5tor1_5_1);
        RENDER_VERSIONS.add(r1_4_6tor1_4_7);
        RENDER_VERSIONS.add(r1_4_4tor1_4_5);
        RENDER_VERSIONS.add(r1_4_2);
        RENDER_VERSIONS.add(r1_3_1tor1_3_2);
        RENDER_VERSIONS.add(r1_2_4tor1_2_5);
        RENDER_VERSIONS.add(r1_2_1tor1_2_3);
        RENDER_VERSIONS.add(r1_1);
        RENDER_VERSIONS.add(r1_0_0tor1_0_1);
        RENDER_VERSIONS.add(b1_8tob1_8_1);
        RENDER_VERSIONS.add(b1_7tob1_7_3);
        RENDER_VERSIONS.add(b1_6tob1_6_6);
        RENDER_VERSIONS.add(b1_5tob1_5_2);
        RENDER_VERSIONS.add(b1_4tob1_4_1);
        RENDER_VERSIONS.add(b1_3tob1_3_1);
        RENDER_VERSIONS.add(b1_2_0tob1_2_2);
        RENDER_VERSIONS.add(b1_1_2);
        RENDER_VERSIONS.add(b1_0tob1_1_1);
        RENDER_VERSIONS.add(a1_2_3_5toa1_2_6);
        RENDER_VERSIONS.add(a1_2_3toa1_2_3_4);
        RENDER_VERSIONS.add(a1_2_2);
        RENDER_VERSIONS.add(a1_2_0toa1_2_1_1);
        RENDER_VERSIONS.add(a1_1_0toa1_1_2_1);
        RENDER_VERSIONS.add(a1_0_17toa1_0_17_4);
        RENDER_VERSIONS.add(a1_0_16toa1_0_16_2);
        RENDER_VERSIONS.add(a1_0_15);
        RENDER_VERSIONS.add(c0_28toc0_30);
        RENDER_VERSIONS.add(c0_0_20ac0_27);
        RENDER_VERSIONS.add(c0_0_19a_06);
        RENDER_VERSIONS.add(c0_0_18a_02);
        RENDER_VERSIONS.add(c0_0_16a_02);
        RENDER_VERSIONS.add(c0_0_15a_1);
        RENDER_VERSIONS.add(sCombatTest8c);
        RENDER_VERSIONS.add(s20w14infinite);
        RENDER_VERSIONS.add(s3d_shareware);
        RENDER_VERSIONS.add(c0_30cpe);
    }

    public static void init() {
    }

    public static VersionEnum fromProtocolVersion(final ProtocolVersion protocolVersion) {
        if (!protocolVersion.isKnown()) return UNKNOWN;
        return VERSION_REGISTRY.getOrDefault(protocolVersion, UNKNOWN);
    }

    public static VersionEnum fromProtocolId(final int protocolId) {
        return fromProtocolVersion(ProtocolVersion.getProtocol(protocolId));
    }

    public static VersionEnum fromUserConnection(final UserConnection userConnection) {
        return fromUserConnection(userConnection, true);
    }

    public static VersionEnum fromUserConnection(final UserConnection userConnection, final boolean serverProtocol) {
        return fromProtocolId(serverProtocol ? userConnection.getProtocolInfo().getServerProtocolVersion() : userConnection.getProtocolInfo().getProtocolVersion());
    }

    public static Collection<VersionEnum> getAllVersions() {
        return VERSION_REGISTRY.values();
    }


    private final ProtocolVersion protocolVersion;

    VersionEnum(final int version, final String name) {
        this(ProtocolVersion.register(version, name));
    }

    VersionEnum(final int version, final String name, final VersionRange versionRange) {
        this(ProtocolVersion.register(version, name, versionRange));
    }

    VersionEnum(final ProtocolVersion protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public ProtocolVersion getProtocol() {
        return this.protocolVersion;
    }

    public String getName() {
        return this.protocolVersion.getName();
    }

    public int getVersion() {
        return this.protocolVersion.getVersion();
    }

    public int getOriginalVersion() {
        return this.protocolVersion.getOriginalVersion();
    }

    public boolean isOlderThan(final VersionEnum other) {
        return this.ordinal() < other.ordinal();
    }

    public boolean isOlderThanOrEqualTo(final VersionEnum other) {
        return this.ordinal() <= other.ordinal();
    }

    public boolean isNewerThan(final VersionEnum other) {
        return this.ordinal() > other.ordinal();
    }

    public boolean isNewerThanOrEqualTo(final VersionEnum other) {
        return this.ordinal() >= other.ordinal();
    }

    public boolean isBetweenInclusive(final VersionEnum min, final VersionEnum max) {
        return this.isNewerThanOrEqualTo(min) && this.isOlderThanOrEqualTo(max);
    }

    public boolean isBetweenExclusive(final VersionEnum min, final VersionEnum max) {
        return this.isNewerThan(min) && this.isOlderThan(max);
    }

}
