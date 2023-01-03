package net.raphimc.vialegacy.protocols.release.protocol1_3_1_2to1_2_4_5.providers;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.platform.providers.Provider;

public class OldAuthProvider implements Provider {

    public void sendAuthRequest(final UserConnection user, final String serverId) throws Throwable {
        throw new IllegalStateException("Online mode auth is not implemented!");
    }

}
