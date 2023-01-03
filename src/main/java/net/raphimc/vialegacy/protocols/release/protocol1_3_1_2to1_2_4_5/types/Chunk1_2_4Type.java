package net.raphimc.vialegacy.protocols.release.protocol1_3_1_2to1_2_4_5.types;

import com.viaversion.viaversion.api.minecraft.chunks.Chunk;
import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import io.netty.buffer.ByteBuf;
import net.raphimc.vialegacy.protocols.release.protocol1_8to1_7_6_10.types.Chunk1_7_6Type;

public class Chunk1_2_4Type extends Chunk1_7_6Type {

    public Chunk1_2_4Type(ClientWorld clientWorld) {
        super(clientWorld);
    }

    @Override
    protected void readUnusedInt(ByteBuf byteBuf, ClientWorld clientWorld) {
        byteBuf.readInt();
    }

    @Override
    protected void writeUnusedInt(ByteBuf byteBuf, ClientWorld clientWorld, Chunk chunk) {
        byteBuf.writeInt(0);
    }

}
