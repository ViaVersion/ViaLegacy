package net.raphimc.vialegacy.protocols.release.protocol1_2_4_5to1_2_1_3;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import net.raphimc.vialegacy.api.splitter.PreNettySplitter;
import net.raphimc.vialegacy.protocols.release.protocol1_3_1_2to1_2_4_5.ClientboundPackets1_2_4;
import net.raphimc.vialegacy.protocols.release.protocol1_3_1_2to1_2_4_5.ServerboundPackets1_2_4;

public class Protocol1_2_4_5to1_2_1_3 extends AbstractProtocol<ClientboundPackets1_2_1, ClientboundPackets1_2_4, ServerboundPackets1_2_1, ServerboundPackets1_2_4> {

    public Protocol1_2_4_5to1_2_1_3() {
        super(ClientboundPackets1_2_1.class, ClientboundPackets1_2_4.class, ServerboundPackets1_2_1.class, ServerboundPackets1_2_4.class);
    }

    @Override
    protected void registerPackets() {
        this.cancelServerbound(ServerboundPackets1_2_4.PLAYER_ABILITIES);
    }

    @Override
    public void init(UserConnection userConnection) {
        userConnection.put(new PreNettySplitter(userConnection, Protocol1_2_4_5to1_2_1_3.class, ClientboundPackets1_2_1::getPacket));
    }

}
