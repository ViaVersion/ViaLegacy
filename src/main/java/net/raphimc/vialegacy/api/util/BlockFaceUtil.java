package net.raphimc.vialegacy.api.util;

import com.viaversion.viaversion.api.minecraft.BlockFace;

public class BlockFaceUtil {

    public static BlockFace getFace(final int direction) {
        switch (direction) {
            case 0:
                return BlockFace.BOTTOM;
            default:
            case 1:
                return BlockFace.TOP;
            case 2:
                return BlockFace.NORTH;
            case 3:
                return BlockFace.SOUTH;
            case 4:
                return BlockFace.WEST;
            case 5:
                return BlockFace.EAST;
        }
    }

}
