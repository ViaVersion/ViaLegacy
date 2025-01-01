/*
 * This file is part of ViaLegacy - https://github.com/RaphiMC/ViaLegacy
 * Copyright (C) 2020-2025 RK_01/RaphiMC and contributors
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
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.common.util.concurrent.UncheckedExecutionException;
import com.viaversion.viaversion.api.platform.providers.Provider;
import net.raphimc.vialegacy.ViaLegacy;
import net.raphimc.vialegacy.api.util.UuidUtil;
import net.raphimc.vialegacy.protocol.release.r1_7_6_10tor1_8.model.GameProfile;

import java.util.UUID;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.regex.Pattern;

public abstract class GameProfileFetcher implements Provider {

    protected static final Pattern PATTERN_CONTROL_CODE = Pattern.compile("(?i)\\u00A7[0-9A-FK-OR]");

    private static final ThreadPoolExecutor LOADING_POOL = (ThreadPoolExecutor) Executors.newFixedThreadPool(2, new ThreadFactoryBuilder().setNameFormat("ViaLegacy GameProfile Loader #%d").setDaemon(true).build());

    private final LoadingCache<String, UUID> UUID_CACHE = CacheBuilder.newBuilder().expireAfterWrite(6, TimeUnit.HOURS).build(new CacheLoader<>() {
        @Override
        public UUID load(String key) throws Exception {
            return loadMojangUUID(key);
        }
    });
    private final LoadingCache<UUID, GameProfile> GAMEPROFILE_CACHE = CacheBuilder.newBuilder().expireAfterWrite(6, TimeUnit.HOURS).build(new CacheLoader<>() {
        @Override
        public GameProfile load(UUID key) throws Exception {
            return loadGameProfile(key);
        }
    });

    public boolean isUUIDLoaded(String playerName) {
        return UUID_CACHE.getIfPresent(playerName) != null;
    }

    public UUID getMojangUUID(String playerName) {
        playerName = PATTERN_CONTROL_CODE.matcher(playerName).replaceAll("");
        try {
            return UUID_CACHE.get(playerName);
        } catch (Throwable e) {
            while (e instanceof ExecutionException || e instanceof UncheckedExecutionException || e instanceof CompletionException) e = e.getCause();
            ViaLegacy.getPlatform().getLogger().log(Level.WARNING, "Failed to load uuid for player '" + playerName + "' (" + e.getClass().getName() + ")");
        }
        final UUID uuid = UuidUtil.createOfflinePlayerUuid(playerName);
        UUID_CACHE.put(playerName, uuid);
        return uuid;
    }

    public CompletableFuture<UUID> getMojangUUIDAsync(String playerName) {
        final CompletableFuture<UUID> future = new CompletableFuture<>();
        if (this.isUUIDLoaded(playerName)) {
            future.complete(this.getMojangUUID(playerName));
        } else {
            LOADING_POOL.submit(() -> {
                future.complete(this.getMojangUUID(playerName));
            });
        }
        return future;
    }

    public boolean isGameProfileLoaded(UUID uuid) {
        return GAMEPROFILE_CACHE.getIfPresent(uuid) != null;
    }

    public GameProfile getGameProfile(UUID uuid) {
        try {
            final GameProfile value = GAMEPROFILE_CACHE.get(uuid);
            if (GameProfile.NULL.equals(value)) return null;
            return value;
        } catch (Throwable e) {
            while (e instanceof ExecutionException || e instanceof UncheckedExecutionException || e instanceof CompletionException) e = e.getCause();
            ViaLegacy.getPlatform().getLogger().log(Level.WARNING, "Failed to load game profile for uuid '" + uuid + "' (" + e.getClass().getName() + ")");
        }
        GAMEPROFILE_CACHE.put(uuid, GameProfile.NULL);
        return null;
    }

    public CompletableFuture<GameProfile> getGameProfileAsync(UUID uuid) {
        final CompletableFuture<GameProfile> future = new CompletableFuture<>();
        if (this.isGameProfileLoaded(uuid)) {
            future.complete(this.getGameProfile(uuid));
        } else {
            LOADING_POOL.submit(() -> {
                future.complete(this.getGameProfile(uuid));
            });
        }
        return future;
    }

    public abstract UUID loadMojangUUID(final String playerName) throws Exception;

    public abstract GameProfile loadGameProfile(final UUID uuid) throws Exception;

}
