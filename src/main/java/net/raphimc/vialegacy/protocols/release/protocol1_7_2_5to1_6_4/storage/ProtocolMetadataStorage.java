package net.raphimc.vialegacy.protocols.release.protocol1_7_2_5to1_6_4.storage;

import com.viaversion.viaversion.api.connection.StoredObject;
import com.viaversion.viaversion.api.connection.UserConnection;

public class ProtocolMetadataStorage extends StoredObject {

    public boolean authenticate;

    public boolean skipEncryption;

    public ProtocolMetadataStorage(UserConnection user) {
        super(user);
    }

}
