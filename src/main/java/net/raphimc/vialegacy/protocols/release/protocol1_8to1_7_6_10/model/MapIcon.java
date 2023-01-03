package net.raphimc.vialegacy.protocols.release.protocol1_8to1_7_6_10.model;

public class MapIcon {

    public byte direction;
    public byte type;
    public byte x;
    public byte z;

    public MapIcon(final byte direction, final byte type, final byte x, final byte z) {
        this.direction = direction;
        this.type = type;
        this.x = x;
        this.z = z;
    }

}
