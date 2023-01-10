package net.raphimc.vialegacy.protocols.release.protocol1_3_1_2to1_2_4_5.model;

import com.viaversion.viaversion.api.minecraft.entities.Entity1_10Types;
import net.raphimc.vialegacy.api.model.Location;

public class TrackedEntity extends AbstractTrackedEntity {

    public TrackedEntity(int entityId, Location location, Entity1_10Types.EntityType entityType) {
        super(entityId, location, entityType);
    }

}
