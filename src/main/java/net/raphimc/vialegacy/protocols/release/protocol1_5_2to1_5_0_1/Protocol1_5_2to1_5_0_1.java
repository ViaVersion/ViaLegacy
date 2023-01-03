package net.raphimc.vialegacy.protocols.release.protocol1_5_2to1_5_0_1;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import net.raphimc.vialegacy.protocols.release.protocol1_6_1to1_5_2.ClientboundPackets1_5_2;
import net.raphimc.vialegacy.protocols.release.protocol1_6_1to1_5_2.ServerboundPackets1_5_2;

public class Protocol1_5_2to1_5_0_1 extends AbstractProtocol<ClientboundPackets1_5_2, ClientboundPackets1_5_2, ServerboundPackets1_5_2, ServerboundPackets1_5_2> {

    public Protocol1_5_2to1_5_0_1() {
        super(ClientboundPackets1_5_2.class, ClientboundPackets1_5_2.class, ServerboundPackets1_5_2.class, ServerboundPackets1_5_2.class);
    }

}
