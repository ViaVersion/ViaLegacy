package net.raphimc.vialegacy.protocols.release.protocol1_3_1_2to1_2_4_5.task;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.UserConnection;
import net.raphimc.vialegacy.protocols.release.protocol1_3_1_2to1_2_4_5.storage.EntityTracker;

public class EntityTrackerTickTask implements Runnable {

    @Override
    public void run() {
        for (UserConnection info : Via.getManager().getConnectionManager().getConnections()) {
            final EntityTracker entityTracker = info.get(EntityTracker.class);
            if (entityTracker != null) entityTracker.tick();
        }
    }

}
