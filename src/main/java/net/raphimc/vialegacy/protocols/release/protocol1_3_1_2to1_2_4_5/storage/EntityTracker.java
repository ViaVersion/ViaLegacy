/*
 * This file is part of ViaLegacy - https://github.com/RaphiMC/ViaLegacy
 * Copyright (C) 2020-2024 RK_01/RaphiMC and contributors
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
package net.raphimc.vialegacy.protocols.release.protocol1_3_1_2to1_2_4_5.storage;

import com.viaversion.viaversion.api.connection.StoredObject;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.entities.EntityTypes1_10;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.util.MathUtil;
import net.raphimc.vialegacy.ViaLegacy;
import net.raphimc.vialegacy.api.model.Location;
import net.raphimc.vialegacy.protocols.release.protocol1_3_1_2to1_2_4_5.Protocol1_3_1_2to1_2_4_5;
import net.raphimc.vialegacy.protocols.release.protocol1_3_1_2to1_2_4_5.model.AbstractTrackedEntity;
import net.raphimc.vialegacy.protocols.release.protocol1_3_1_2to1_2_4_5.model.ConfiguredSound;
import net.raphimc.vialegacy.protocols.release.protocol1_3_1_2to1_2_4_5.model.TrackedLivingEntity;
import net.raphimc.vialegacy.protocols.release.protocol1_3_1_2to1_2_4_5.sound.Sound;
import net.raphimc.vialegacy.protocols.release.protocol1_3_1_2to1_2_4_5.sound.SoundEmulation;
import net.raphimc.vialegacy.protocols.release.protocol1_3_1_2to1_2_4_5.sound.SoundType;
import net.raphimc.vialegacy.protocols.release.protocol1_4_2to1_3_1_2.ClientboundPackets1_3_1;
import net.raphimc.vialegacy.protocols.release.protocol1_7_2_5to1_6_4.types.Types1_6_4;

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

    public void updateEntityMetadata(int entityId, List<Metadata> metadataList) {
        final AbstractTrackedEntity entity = this.entityMap.get(entityId);
        if (entity instanceof TrackedLivingEntity) {
            final TrackedLivingEntity livingEntity = (TrackedLivingEntity) entity;
            livingEntity.updateMetadata(metadataList);
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
            if (entity instanceof TrackedLivingEntity) {
                final TrackedLivingEntity livingEntity = (TrackedLivingEntity) entity;
                livingEntity.tick(this);
            }
        }
    }

    public void playSound(int entityId, SoundType type) {
        if (this.playerID == entityId && type == SoundType.HURT) return; // Don't play HURT sound for the player

        if (this.entityMap.get(entityId) != null) {
            final AbstractTrackedEntity entity = this.entityMap.get(entityId);
            final ConfiguredSound sound = SoundEmulation.getEntitySound(entity.getEntityType(), type);
            final Location entityLocation = entity.getLocation();
            final Location playerLocation = this.entityMap.get(this.playerID).getLocation();

            if (entity instanceof TrackedLivingEntity && type == SoundType.IDLE) {
                final TrackedLivingEntity livingEntity = (TrackedLivingEntity) entity;
                livingEntity.applyPitch(this, sound);

                if (entity.getEntityType().isOrHasParent(EntityTypes1_10.EntityType.WOLF)) {
                    if (livingEntity.wolfIsAngry) {
                        sound.setSound(Sound.MOB_WOLF_GROWL);
                    } else if (RND.nextInt(3) == 0) {
                        sound.setSound(livingEntity.isTamed && livingEntity.wolfHealth < 10 ? Sound.MOB_WOLF_WHINE : Sound.MOB_WOLF_PANTING);
                    }
                } else if (entity.getEntityType().isOrHasParent(EntityTypes1_10.EntityType.OCELOT)) {
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

        try {
            final PacketWrapper entitySound = PacketWrapper.create(ClientboundPackets1_3_1.NAMED_SOUND, this.getUser());
            entitySound.write(Types1_6_4.STRING, sound.getSound().getSoundName()); // sound
            entitySound.write(Type.INT, ((int) sourceLocation.getX()) * 8); // x
            entitySound.write(Type.INT, ((int) sourceLocation.getY()) * 8); // y
            entitySound.write(Type.INT, ((int) sourceLocation.getZ()) * 8); // z
            entitySound.write(Type.FLOAT, vol); // volume
            entitySound.write(Type.UNSIGNED_BYTE, correctedPitch); // pitch
            entitySound.send(Protocol1_3_1_2to1_2_4_5.class);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

}
