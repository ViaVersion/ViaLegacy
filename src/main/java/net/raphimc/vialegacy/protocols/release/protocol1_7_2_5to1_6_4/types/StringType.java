package net.raphimc.vialegacy.protocols.release.protocol1_7_2_5to1_6_4.types;

import com.viaversion.viaversion.api.type.Type;
import io.netty.buffer.ByteBuf;

public class StringType extends Type<String> {

    public StringType() {
        super(String.class);
    }

    public String read(ByteBuf buffer) {
        final short length = buffer.readShort();
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append(buffer.readChar());
        }
        return builder.toString();
    }

    public void write(ByteBuf buffer, String s) {
        buffer.writeShort(s.length());
        for (char c : s.toCharArray()) {
            buffer.writeChar(c);
        }
    }

}
