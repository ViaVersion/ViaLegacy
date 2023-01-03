package net.raphimc.vialegacy.protocols.release.protocol1_8to1_7_6_10.types;

import com.viaversion.viaversion.api.type.Type;
import io.netty.buffer.ByteBuf;

public class IntArrayType extends Type<int[]> {

    public IntArrayType() {
        super(int[].class);
    }

    @Override
    public int[] read(ByteBuf buffer) throws Exception {
        final byte length = buffer.readByte();
        final int[] array = new int[length];

        for (byte i = 0; i < length; i++) {
            array[i] = buffer.readInt();
        }
        return array;
    }

    @Override
    public void write(ByteBuf buffer, int[] array) throws Exception {
        buffer.writeByte(array.length);
        for (int i : array) buffer.writeInt(i);
    }

}
