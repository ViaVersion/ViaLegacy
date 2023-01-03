package net.raphimc.vialegacy.protocols.release.protocol1_7_2_5to1_6_4.baseprotocols;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.AbstractSimpleProtocol;
import com.viaversion.viaversion.api.protocol.packet.State;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.protocols.base.ServerboundHandshakePackets;
import net.raphimc.vialegacy.protocols.release.protocol1_7_2_5to1_6_4.storage.HandshakeStorage;
import net.raphimc.vialegacy.protocols.release.protocol1_7_2_5to1_6_4.storage.ProtocolMetadataStorage;

public class PreNettyBaseProtocol extends AbstractSimpleProtocol {

    public static final PreNettyBaseProtocol INSTANCE = new PreNettyBaseProtocol();

    private PreNettyBaseProtocol() {
        this.initialize();
    }

    @Override
    protected void registerPackets() {
        this.registerServerbound(State.HANDSHAKE, ServerboundHandshakePackets.CLIENT_INTENTION.getId(), ServerboundHandshakePackets.CLIENT_INTENTION.getId(), new PacketRemapper() {
            @Override
            public void registerMap() {
                handler(wrapper -> {
                    wrapper.cancel();
                    wrapper.read(Type.VAR_INT); // protocolVersion
                    final String hostname = wrapper.read(Type.STRING); // hostName
                    final int port = wrapper.read(Type.UNSIGNED_SHORT); // port
                    wrapper.user().put(new HandshakeStorage(wrapper.user(), hostname, port));
                });
            }
        });
    }

    @Override
    public void init(UserConnection userConnection) {
        userConnection.put(new ProtocolMetadataStorage(userConnection));
    }

    @Override
    public boolean isBaseProtocol() {
        return true;
    }

}
