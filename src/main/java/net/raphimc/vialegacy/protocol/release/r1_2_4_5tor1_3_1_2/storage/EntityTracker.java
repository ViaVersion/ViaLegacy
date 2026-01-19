/*
 * This file is part of ViaLegacy - https://github.com/RaphiMC/ViaLegacy
 * Copyright (C) 2020-2026 RK_01/RaphiMC and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.raphimc.vialegacy.protocol.release.r1_2_4_5tor1_3_1_2.storage;

import com.viaversion.viaversion.api.connection.StoredObject;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.entities.EntityTypes1_8;
import com.viaversion.viaversion.api.minecraft.entitydata.EntityData;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.type.Types;
import com.viaversion.viaversion.util.MathUtil;
import net.raphimc.vialegacy.ViaLegacy;
import net.raphimc.vialegacy.api.model.Location;
import net.raphimc.vialegacy.protocol.release.r1_2_4_5tor1_3_1_2.Protocolr1_2_4_5Tor1_3_1_2;
import net.raphimc.vialegacy.protocol.release.r1_2_4_5tor1_3_1_2.data.sound.Sound;
import net.raphimc.vialegacy.protocol.release.r1_2_4_5tor1_3_1_2.data.sound.SoundRegistry1_2_4;
import net.raphimc.vialegacy.protocol.release.r1_2_4_5tor1_3_1_2.data.sound.SoundType;
import net.raphimc.vialegacy.protocol.release.r1_2_4_5tor1_3_1_2.model.AbstractTrackedEntity;
import net.raphimc.vialegacy.protocol.release.r1_2_4_5tor1_3_1_2.model.ConfiguredSound;
import net.raphimc.vialegacy.protocol.release.r1_2_4_5tor1_3_1_2.model.TrackedLivingEntity;
import net.raphimc.vialegacy.protocol.release.r1_3_1_2tor1_4_2.packet.ClientboundPackets1_3_1;
import net.raphimc.vialegacy.protocol.release.r1_6_4tor1_7_2_5.types.Types1_6_4;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

public class EntityTracker extends StoredObject {

    public final Random RND = new Random();
    private final Map<Integer, AbstractTrackedEntity> entityMap = new ConcurrentHashMap<>();

    private int playerID;

    public EntityTracker(UserConnection user) {
        super(user);
    }

    public int getPlayerID() {
        return this.playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public Map<Integer, AbstractTrackedEntity> getTrackedEntities() {
        return this.entityMap;
    }

    public void updateEntityLocation(int entityId, int x, int y, int z, boolean relative) {
        final AbstractTrackedEntity entity = this.entityMap.get(entityId);
        if (entity != null) {
            final Location oldLoc = entity.getLocation();

            final double xPos = x / 32.0D;
            final double yPos = y / 32.0D;
            final double zPos = z / 32.0D;

            Location newLoc;
            if (relative) {
                newLoc = new Location(oldLoc.getX() + xPos, oldLoc.getY() + yPos, oldLoc.getZ() + zPos);
            } else {
                newLoc = new Location(xPos, yPos, zPos);
            }

            entity.setLocation(newLoc);
        }
    }

    public void updateEntityDataList(int entityId, List<EntityData> entityDataList) {
        final AbstractTrackedEntity entity = this.entityMap.get(entityId);
        if (entity instanceof TrackedLivingEntity livingEntity) {
            livingEntity.updateEntityData(entityDataList);
        }
    }

    public Optional<AbstractTrackedEntity> getNearestEntity(Location location, double range, Predicate<AbstractTrackedEntity> entityPredicate) {
        return this.entityMap.values().stream()
                .filter(entityPredicate)
                .filter(e -> !e.getLocation().equals(location))
                .filter(e -> e.getLocation().distanceTo(location) <= range)
                .min(Comparator.comparingDouble(o -> o.getLocation().distanceTo(location)));
    }

    public void tick() {
        for (AbstractTrackedEntity entity : this.entityMap.values()) {
            if (entity instanceof TrackedLivingEntity livingEntity) {
                livingEntity.tick(this);
            }
        }
    }

    public void playSound(int entityId, SoundType type) {
        if (this.playerID == entityId && type == SoundType.HURT) return; // Don't play HURT sound for the player

        if (this.entityMap.get(entityId) != null) {
            final AbstractTrackedEntity entity = this.entityMap.get(entityId);
            final ConfiguredSound sound = SoundRegistry1_2_4.getEntitySound(entity.getEntityType(), type);
            final Location entityLocation = entity.getLocation();
            final Location playerLocation = this.entityMap.get(this.playerID).getLocation();

            if (entity instanceof TrackedLivingEntity livingEntity && type == SoundType.IDLE) {
                livingEntity.applyPitch(this, sound);

                if (entity.getEntityType().isOrHasParent(EntityTypes1_8.EntityType.WOLF)) {
                    if (livingEntity.wolfIsAngry) {
                        sound.setSound(Sound.MOB_WOLF_GROWL);
                    } else if (RND.nextInt(3) == 0) {
                        sound.setSound(livingEntity.isTamed && livingEntity.wolfHealth < 10 ? Sound.MOB_WOLF_WHINE : Sound.MOB_WOLF_PANTING);
                    }
                } else if (entity.getEntityType().isOrHasParent(EntityTypes1_8.EntityType.OCELOT)) {
                    if (livingEntity.isTamed) {
                        sound.setSound(RND.nextInt(4) == 0 ? Sound.MOB_CAT_PURREOW : Sound.MOB_CAT_MEOW);
                    }
                }
            }

            if (Sound.NO_SOUND.equals(sound.getSound())) return;
            this.playSoundAt(entityLocation, playerLocation, sound);
        }
    }

    public void playSoundAt(Location entityLocation, Sound sound, float volume, float pitch) {
        final Location playerLocation = this.entityMap.get(this.playerID).getLocation();

        this.playSoundAt(entityLocation, playerLocation, new ConfiguredSound(sound, volume, pitch));
    }

    public static short constrainToRange(short value, short min, short max) {
        return value < min ? min : value < max ? value : max;
    }

    private void playSoundAt(Location sourceLocation, Location targetLocation, ConfiguredSound sound) {
        if (!ViaLegacy.getConfig().isSoundEmulation()) return;
        final short correctedPitch = (short) MathUtil.clamp((int) (sound.getPitch() * 63.0F), 0, 255);
        final float vol = sound.getVolume();
        float range = 16F;
        if (vol > 1.0F) range *= vol;

        if (targetLocation.distanceTo(sourceLocation) > range) { // cancel if outside of allowed range
            return;
        }

        final PacketWrapper entitySound = PacketWrapper.create(ClientboundPackets1_3_1.CUSTOM_SOUND, this.user());
        entitySound.write(Types1_6_4.STRING, sound.getSound().getSoundName()); // sound
        entitySound.write(Types.INT, ((int) sourceLocation.getX()) * 8); // x
        entitySound.write(Types.INT, ((int) sourceLocation.getY()) * 8); // y
        entitySound.write(Types.INT, ((int) sourceLocation.getZ()) * 8); // z
        entitySound.write(Types.FLOAT, vol); // volume
        entitySound.write(Types.UNSIGNED_BYTE, correctedPitch); // pitch
        entitySound.send(Protocolr1_2_4_5Tor1_3_1_2.class);
    }

}
