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
package net.raphimc.vialegacy.protocols.release.protocol1_8to1_7_6_10.model;

import net.raphimc.vialegacy.api.util.UuidUtil;

import java.util.*;

public class GameProfile {

    public static final GameProfile NULL = new GameProfile();

    public String userName;
    public UUID uuid;
    public Map<String, List<Property>> properties = new HashMap<>();

    private final UUID offlineUuid;

    private GameProfile() {
        this.offlineUuid = new UUID(0, 0);
    }

    public GameProfile(final String userName) {
        if (userName == null) throw new IllegalStateException("Username can't be null");
        this.userName = userName;
        this.offlineUuid = this.uuid = UuidUtil.createOfflinePlayerUuid(userName);
    }

    public GameProfile(final String userName, final UUID uuid) {
        if (userName == null || uuid == null) throw new IllegalStateException("Username and UUID can't be null");
        this.userName = userName;
        this.uuid = uuid;
        this.offlineUuid = UuidUtil.createOfflinePlayerUuid(userName);
    }

    public void addProperty(final Property property) {
        this.properties.computeIfAbsent(property.key, k -> new ArrayList<>()).add(property);
    }

    public List<Property> getAllProperties() {
        return this.properties.values().stream().reduce((p1, p2) -> {
            final List<GameProfile.Property> merge = new ArrayList<>();
            merge.addAll(p1);
            merge.addAll(p2);
            return merge;
        }).orElseGet(ArrayList::new);
    }

    public boolean isOffline() {
        return this.offlineUuid.equals(this.uuid);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameProfile that = (GameProfile) o;
        return Objects.equals(userName, that.userName) && Objects.equals(uuid, that.uuid) && Objects.equals(properties, that.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, uuid, properties);
    }

    @Override
    public String toString() {
        return "GameProfile{" +
                "userName='" + userName + '\'' +
                ", uuid=" + uuid +
                '}';
    }

    public static class Property {

        public String key;
        public String value;
        public String signature;

        public Property(final String key, final String value) {
            this.key = key;
            this.value = value;
        }

        public Property(final String key, final String value, final String signature) {
            this(key, value);
            this.signature = signature;
        }

    }

}
