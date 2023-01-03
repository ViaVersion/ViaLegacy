package net.raphimc.vialegacy.protocols.release.protocol1_2_1_3to1_1.storage;

import com.viaversion.viaversion.api.connection.StoredObject;
import com.viaversion.viaversion.api.connection.UserConnection;

public class DimensionTracker extends StoredObject {

    private int dimensionId = 0;

    public DimensionTracker(UserConnection user) {
        super(user);
    }

    public void setDimension(final int dimensionId) {
        this.dimensionId = dimensionId;
    }

    public int getDimensionId() {
        return this.dimensionId;
    }

}
