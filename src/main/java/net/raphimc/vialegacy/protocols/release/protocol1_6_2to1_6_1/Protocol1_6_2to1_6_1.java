package net.raphimc.vialegacy.protocols.release.protocol1_6_2to1_6_1;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.Position;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.api.type.Type;
import net.raphimc.vialegacy.api.ItemList1_6;
import net.raphimc.vialegacy.protocols.release.protocol1_7_2_5to1_6_4.ClientboundPackets1_6_4;
import net.raphimc.vialegacy.protocols.release.protocol1_7_2_5to1_6_4.ServerboundPackets1_6_4;
import net.raphimc.vialegacy.protocols.release.protocol1_7_2_5to1_6_4.types.Types1_6_4;
import net.raphimc.vialegacy.protocols.release.protocol1_8to1_7_6_10.types.Types1_7_6;
import net.raphimc.vialegacy.util.BlockFaceUtils;
import net.raphimc.vialegacy.util.PreNettySplitter;

import java.nio.charset.StandardCharsets;

public class Protocol1_6_2to1_6_1 extends AbstractProtocol<ClientboundPackets1_6_1, ClientboundPackets1_6_4, ServerboundPackets1_6_4, ServerboundPackets1_6_4> {

    public Protocol1_6_2to1_6_1() {
        super(ClientboundPackets1_6_1.class, ClientboundPackets1_6_4.class, ServerboundPackets1_6_4.class, ServerboundPackets1_6_4.class);
    }

    @Override
    protected void registerPackets() {
        this.registerClientbound(ClientboundPackets1_6_1.JOIN_GAME, new PacketRemapper() {
            @Override
            public void registerMap() {
                handler(wrapper -> {
                    final PacketWrapper brand = PacketWrapper.create(ClientboundPackets1_6_4.PLUGIN_MESSAGE, wrapper.user());
                    brand.write(Types1_6_4.STRING, "MC|Brand");
                    final byte[] brandBytes = "legacy".getBytes(StandardCharsets.UTF_8);
                    brand.write(Type.SHORT, (short) brandBytes.length); // data length
                    brand.write(Type.REMAINING_BYTES, brandBytes); // data

                    wrapper.send(Protocol1_6_2to1_6_1.class);
                    brand.send(Protocol1_6_2to1_6_1.class);
                    wrapper.cancel();
                });
            }
        });
        this.registerClientbound(ClientboundPackets1_6_1.ENTITY_PROPERTIES, new PacketRemapper() {
            @Override
            public void registerMap() {
                map(Type.INT); // entity id
                handler(wrapper -> {
                    final int amount = wrapper.passthrough(Type.INT); // count
                    for (int i = 0; i < amount; i++) {
                        wrapper.passthrough(Types1_6_4.STRING); // id
                        wrapper.passthrough(Type.DOUBLE); // baseValue
                        wrapper.write(Type.SHORT, (short) 0); // modifier count
                    }
                });
            }
        });

        this.registerServerbound(ServerboundPackets1_6_4.PLAYER_BLOCK_PLACEMENT, new PacketRemapper() {
            @Override
            public void registerMap() {
                map(Types1_7_6.POSITION_UBYTE); // position
                map(Type.UNSIGNED_BYTE); // direction
                map(Types1_7_6.COMPRESSED_ITEM); // item
                map(Type.UNSIGNED_BYTE); // offset x
                map(Type.UNSIGNED_BYTE); // offset y
                map(Type.UNSIGNED_BYTE); // offset z
                handler(wrapper -> {
                    final Position pos = wrapper.get(Types1_7_6.POSITION_UBYTE, 0);
                    final short direction = wrapper.get(Type.UNSIGNED_BYTE, 0);
                    final Item item = wrapper.get(Types1_7_6.COMPRESSED_ITEM, 0);

                    if (item != null && item.identifier() == ItemList1_6.sign.itemID && direction != 255 && direction != 0) { // If placed item is a sign then cancel and send a OPEN_SIGN_EDITOR packet
                        final PacketWrapper openSignEditor = PacketWrapper.create(ClientboundPackets1_6_4.OPEN_SIGN_EDITOR, wrapper.user());
                        openSignEditor.write(Type.BYTE, (byte) 0); // magic value
                        openSignEditor.write(Types1_7_6.POSITION_INT, pos.getRelative(BlockFaceUtils.getFace(direction)));
                        openSignEditor.send(Protocol1_6_2to1_6_1.class);
                    }
                });
            }
        });
    }

    @Override
    public void init(UserConnection userConnection) {
        userConnection.put(new PreNettySplitter(userConnection, Protocol1_6_2to1_6_1.class, ClientboundPackets1_6_1::getPacket));
    }

}
