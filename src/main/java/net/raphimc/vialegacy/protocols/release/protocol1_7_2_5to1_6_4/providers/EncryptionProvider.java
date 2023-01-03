package net.raphimc.vialegacy.protocols.release.protocol1_7_2_5to1_6_4.providers;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.platform.providers.Provider;

public abstract class EncryptionProvider implements Provider {

    public abstract void enableDecryption(final UserConnection user);

}
