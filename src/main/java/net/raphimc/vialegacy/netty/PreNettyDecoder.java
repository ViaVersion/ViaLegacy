package net.raphimc.vialegacy.netty;

import com.google.common.collect.EvictingQueue;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.type.Type;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import net.raphimc.vialegacy.ViaLegacy;
import net.raphimc.vialegacy.util.PreNettyPacketType;
import net.raphimc.vialegacy.util.PreNettySplitter;

import java.util.List;

public class PreNettyDecoder extends ByteToMessageDecoder {

    protected final UserConnection user;
    private final EvictingQueue<Integer> lastPackets = EvictingQueue.create(8);

    public PreNettyDecoder(final UserConnection user) {
        this.user = user;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        if (!in.isReadable() || in.readableBytes() <= 0) {
            return;
        }
        final PreNettySplitter splitter = this.user.get(PreNettySplitter.class);
        if (splitter == null) {
            ViaLegacy.getPlatform().getLogger().severe("Received data, but no splitter is set");
            return;
        }

        while (in.readableBytes() > 0) {
            in.markReaderIndex();
            final int packetId = in.readUnsignedByte();
            final PreNettyPacketType packetType = splitter.getPacketType(packetId);
            if (packetType == null) {
                ViaLegacy.getPlatform().getLogger().severe("Encountered undefined packet: " + packetId + " in " + splitter.getProtocolName());
                ViaLegacy.getPlatform().getLogger().severe(ByteBufUtil.hexDump(in.readSlice(in.readableBytes())));
                ViaLegacy.getPlatform().getLogger().severe("Last 8 read packet ids: " + this.lastPackets);
                ctx.channel().close();
                return;
            }
            this.lastPackets.add(packetId);
            try {
                final int begin = in.readerIndex();
                packetType.getPacketReader().accept(this.user, in);
                final int length = in.readerIndex() - begin;
                in.readerIndex(begin);

                int totalLength = length;
                for (int i = 1; i < 5; ++i) {
                    if ((packetId & -1 << i * 7) == 0) {
                        totalLength += i;
                        break;
                    }
                }

                final ByteBuf buf = ctx.alloc().buffer();
                Type.VAR_INT.writePrimitive(buf, totalLength); // Length
                Type.VAR_INT.writePrimitive(buf, packetId); // id
                buf.writeBytes(in.readSlice(length)); // content
                out.add(buf);
            } catch (IndexOutOfBoundsException e) { // Not enough data
                in.resetReaderIndex();
                return;
            }
        }
    }

}
