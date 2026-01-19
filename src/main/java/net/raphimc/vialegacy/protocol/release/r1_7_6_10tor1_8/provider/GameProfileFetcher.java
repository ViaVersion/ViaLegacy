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
package net.raphimc.vialegacy.protocol.release.r1_7_6_10tor1_8.provider;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.ExecutionError;
import com.google.common.util.concurrent.UncheckedExecutionException;
import com.viaversion.viaversion.api.minecraft.GameProfile;
import com.viaversion.viaversion.api.platform.providers.Provider;
import net.raphimc.vialegacy.ViaLegacy;
import net.raphimc.vialegacy.api.util.GameProfileUtil;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.regex.Pattern;

public abstract class GameProfileFetcher implements Provider {

    private static final GameProfile NULL_GAME_PROFILE = new GameProfile(null, null);
    private static final Pattern COLOR_CODE_PATTERN = Pattern.compile("(?i)\\u00A7[0-9A-FK-OR]");

    private final LoadingCache<String, UUID> uuidCache = CacheBuilder.newBuilder().expireAfterWrite(6, TimeUnit.HOURS).build(new CacheLoader<>() {
        @Override
        public UUID load(String key) throws Exception {
            return GameProfileFetcher.this.loadMojangUuid(key);
        }
    });
    private final LoadingCache<UUID, GameProfile> gameProfileCache = CacheBuilder.newBuilder().expireAfterWrite(6, TimeUnit.HOURS).build(new CacheLoader<>() {
        @Override
        public GameProfile load(UUID key) throws Exception {
            return GameProfileFetcher.this.loadGameProfile(key);
        }
    });

    public boolean isUuidLoaded(final String playerName) {
        return this.uuidCache.getIfPresent(playerName) != null;
    }

    public UUID getMojangUuid(String playerName) {
        playerName = COLOR_CODE_PATTERN.matcher(playerName).replaceAll("");
        try {
            return this.uuidCache.get(playerName);
        } catch (Throwable e) {
            while (e instanceof ExecutionException || e instanceof UncheckedExecutionException || e instanceof CompletionException || e instanceof ExecutionError) e = e.getCause();
            ViaLegacy.getPlatform().getLogger().log(Level.WARNING, "Failed to load uuid for player '" + playerName + "' (" + e.getClass().getName() + ")");
        }
        final UUID uuid = GameProfileUtil.getOfflinePlayerUuid(playerName);
        this.uuidCache.put(playerName, uuid);
        return uuid;
    }

    public CompletableFuture<UUID> getMojangUuidAsync(final String playerName) {
        if (this.isUuidLoaded(playerName)) {
            return CompletableFuture.completedFuture(this.getMojangUuid(playerName));
        } else {
            return CompletableFuture.supplyAsync(() -> this.getMojangUuid(playerName));
        }
    }

    public boolean isGameProfileLoaded(final UUID uuid) {
        return this.gameProfileCache.getIfPresent(uuid) != null;
    }

    public GameProfile getGameProfile(final UUID uuid) {
        try {
            final GameProfile value = this.gameProfileCache.get(uuid);
            if (NULL_GAME_PROFILE.equals(value)) {
                return null;
            }
            return value;
        } catch (Throwable e) {
            while (e instanceof ExecutionException || e instanceof UncheckedExecutionException || e instanceof CompletionException || e instanceof ExecutionError) e = e.getCause();
            ViaLegacy.getPlatform().getLogger().log(Level.WARNING, "Failed to load game profile for uuid '" + uuid + "' (" + e.getClass().getName() + ")");
        }
        this.gameProfileCache.put(uuid, NULL_GAME_PROFILE);
        return null;
    }

    public CompletableFuture<GameProfile> getGameProfileAsync(final UUID uuid) {
        if (this.isGameProfileLoaded(uuid)) {
            return CompletableFuture.completedFuture(this.getGameProfile(uuid));
        } else {
            return CompletableFuture.supplyAsync(() -> this.getGameProfile(uuid));
        }
    }

    public abstract UUID loadMojangUuid(final String playerName) throws Exception;

    public abstract GameProfile loadGameProfile(final UUID uuid) throws Exception;

}
