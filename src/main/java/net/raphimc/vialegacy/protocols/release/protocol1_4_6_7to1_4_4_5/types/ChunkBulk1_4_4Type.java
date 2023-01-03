package net.raphimc.vialegacy.protocols.release.protocol1_4_6_7to1_4_4_5.types;

import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import io.netty.buffer.ByteBuf;
import net.raphimc.vialegacy.protocols.release.protocol1_8to1_7_6_10.types.ChunkBulk1_7_6Type;

public class ChunkBulk1_4_4Type extends ChunkBulk1_7_6Type {

    public ChunkBulk1_4_4Type(ClientWorld clientWorld) {
        super(clientWorld);
    }

    @Override
    protected boolean readHasSkyLight(ByteBuf byteBuf, ClientWorld clientWorld) {
        return true;
    }

    @Override
    protected void writeHasSkyLight(ByteBuf byteBuf, ClientWorld clientWorld, boolean hasSkyLight) {
    }

}
