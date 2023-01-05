package net.raphimc.vialegacy.protocols.beta.protocol1_0_0_1tob1_8_0_1;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.platform.providers.ViaProviders;
import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.api.type.Type;
import net.raphimc.vialegacy.protocols.beta.protocol1_0_0_1tob1_8_0_1.storage.PlayerAirTimeStorage;
import net.raphimc.vialegacy.protocols.beta.protocol1_0_0_1tob1_8_0_1.task.PlayerAirTimeUpdateTask;
import net.raphimc.vialegacy.protocols.beta.protocol1_0_0_1tob1_8_0_1.types.Typesb1_8_0_1;
import net.raphimc.vialegacy.protocols.release.protocol1_1to1_0_0_1.ClientboundPackets1_0;
import net.raphimc.vialegacy.protocols.release.protocol1_1to1_0_0_1.ServerboundPackets1_0;
import net.raphimc.vialegacy.protocols.release.protocol1_3_1_2to1_2_4_5.types.Types1_2_4;
import net.raphimc.vialegacy.protocols.release.protocol1_4_4_5to1_4_2.types.Types1_4_2;
import net.raphimc.vialegacy.protocols.release.protocol1_8to1_7_6_10.types.Types1_7_6;
import net.raphimc.vialegacy.util.PreNettySplitter;

public class Protocol1_0_0_1tob1_8_0_1 extends AbstractProtocol<ClientboundPacketsb1_8, ClientboundPackets1_0, ServerboundPacketsb1_8, ServerboundPackets1_0> {

    public Protocol1_0_0_1tob1_8_0_1() {
        super(ClientboundPacketsb1_8.class, ClientboundPackets1_0.class, ServerboundPacketsb1_8.class, ServerboundPackets1_0.class);
    }

    @Override
    protected void registerPackets() {
        this.registerClientbound(ClientboundPacketsb1_8.SET_EXPERIENCE, new PacketRemapper() {
            @Override
            public void registerMap() {
                handler(wrapper -> {
                    float experience = (float) wrapper.read(Type.BYTE);
                    final byte experienceLevel = wrapper.read(Type.BYTE);
                    final short experienceTotal = wrapper.read(Type.SHORT);
                    experience = (experience - 1.0f) / (10 * experienceLevel);
                    wrapper.write(Type.FLOAT, experience); // experience bar
                    wrapper.write(Type.SHORT, (short) experienceLevel); // level
                    wrapper.write(Type.SHORT, experienceTotal); // total experience
                });
            }
        });
        this.registerClientbound(ClientboundPacketsb1_8.SET_SLOT, new PacketRemapper() {
            @Override
            public void registerMap() {
                map(Type.BYTE); // window id
                map(Type.SHORT); // slot
                map(Types1_4_2.NBTLESS_ITEM, Types1_2_4.COMPRESSED_NBT_ITEM); // item
            }
        });
        this.registerClientbound(ClientboundPacketsb1_8.WINDOW_ITEMS, new PacketRemapper() {
            @Override
            public void registerMap() {
                map(Type.BYTE); // window id
                map(Types1_4_2.NBTLESS_ITEM_ARRAY, Types1_2_4.COMPRESSED_NBT_ITEM_ARRAY); // item
            }
        });

        this.registerServerbound(ServerboundPackets1_0.PLAYER_BLOCK_PLACEMENT, new PacketRemapper() {
            @Override
            public void registerMap() {
                map(Types1_7_6.POSITION_UBYTE); // position
                map(Type.UNSIGNED_BYTE); // direction
                map(Types1_2_4.COMPRESSED_NBT_ITEM, Types1_4_2.NBTLESS_ITEM);
            }
        });
        this.registerServerbound(ServerboundPackets1_0.CLICK_WINDOW, new PacketRemapper() {
            @Override
            public void registerMap() {
                map(Type.BYTE); // window id
                map(Type.SHORT); // slot
                map(Type.BYTE); // button
                map(Type.SHORT); // action
                map(Type.BYTE); // mode
                map(Types1_2_4.COMPRESSED_NBT_ITEM, Types1_4_2.NBTLESS_ITEM); // item
            }
        });
        this.registerServerbound(ServerboundPackets1_0.CREATIVE_INVENTORY_ACTION, new PacketRemapper() {
            @Override
            public void registerMap() {
                map(Type.SHORT); // slot
                map(Types1_2_4.COMPRESSED_NBT_ITEM, Typesb1_8_0_1.CREATIVE_ITEM); // item
            }
        });
        this.cancelServerbound(ServerboundPackets1_0.CLICK_WINDOW_BUTTON);
    }

    @Override
    public void register(ViaProviders providers) {
        Via.getPlatform().runRepeatingSync(new PlayerAirTimeUpdateTask(), 1L);
    }

    @Override
    public void init(UserConnection userConnection) {
        userConnection.put(new PreNettySplitter(userConnection, Protocol1_0_0_1tob1_8_0_1.class, ClientboundPacketsb1_8::getPacket));

        userConnection.put(new PlayerAirTimeStorage(userConnection));
    }

}
