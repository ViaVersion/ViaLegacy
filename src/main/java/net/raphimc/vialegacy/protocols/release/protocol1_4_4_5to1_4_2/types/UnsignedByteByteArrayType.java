package net.raphimc.vialegacy.protocols.release.protocol1_4_4_5to1_4_2.types;

import com.viaversion.viaversion.api.type.Type;
import io.netty.buffer.ByteBuf;

public class UnsignedByteByteArrayType extends Type<byte[]> {

    public UnsignedByteByteArrayType() {
        super(byte[].class);
    }

    public void write(ByteBuf buffer, byte[] array) throws Exception {
        buffer.writeByte(array.length & 255);
        buffer.writeBytes(array);
    }

    public byte[] read(ByteBuf buffer) throws Exception {
        final byte[] array = new byte[buffer.readUnsignedByte()];
        buffer.readBytes(array);
        return array;
    }

}
