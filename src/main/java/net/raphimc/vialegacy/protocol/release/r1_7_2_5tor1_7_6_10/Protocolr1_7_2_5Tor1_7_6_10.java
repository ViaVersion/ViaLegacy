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
package net.raphimc.vialegacy.protocol.release.r1_7_2_5tor1_7_6_10;

import com.viaversion.nbt.tag.CompoundTag;
import com.viaversion.nbt.tag.ListTag;
import com.viaversion.nbt.tag.StringTag;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.minecraft.BlockPosition;
import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.packet.State;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.api.type.Types;
import com.viaversion.viaversion.protocols.base.ClientboundLoginPackets;
import com.viaversion.viaversion.protocols.base.v1_7.ClientboundBaseProtocol1_7;
import net.raphimc.vialegacy.ViaLegacy;
import net.raphimc.vialegacy.api.util.UuidUtil;
import net.raphimc.vialegacy.protocol.release.r1_7_2_5tor1_7_6_10.packet.ClientboundPackets1_7_2;
import net.raphimc.vialegacy.protocol.release.r1_7_2_5tor1_7_6_10.packet.ServerboundPackets1_7_2;
import net.raphimc.vialegacy.protocol.release.r1_7_2_5tor1_7_6_10.rewriter.TextRewriter;
import net.raphimc.vialegacy.protocol.release.r1_7_6_10tor1_8.model.GameProfile;
import net.raphimc.vialegacy.protocol.release.r1_7_6_10tor1_8.provider.GameProfileFetcher;
import net.raphimc.vialegacy.protocol.release.r1_7_6_10tor1_8.types.Types1_7_6;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class Protocolr1_7_2_5Tor1_7_6_10 extends AbstractProtocol<ClientboundPackets1_7_2, ClientboundPackets1_7_2, ServerboundPackets1_7_2, ServerboundPackets1_7_2> {

    private static final String UUID_PATTERN = "[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}";

    public Protocolr1_7_2_5Tor1_7_6_10() {
        super(ClientboundPackets1_7_2.class, ClientboundPackets1_7_2.class, ServerboundPackets1_7_2.class, ServerboundPackets1_7_2.class);
    }

    @Override
    protected void registerPackets() {
        this.registerClientbound(State.LOGIN, ClientboundLoginPackets.LOGIN_FINISHED, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.STRING); // uuid
                map(Types.STRING); // name
                handler(wrapper -> wrapper.set(Types.STRING, 0, fixGameProfileUuid(wrapper.get(Types.STRING, 0), wrapper.get(Types.STRING, 1))));
            }
        });
        this.registerClientbound(ClientboundPackets1_7_2.ADD_PLAYER, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.VAR_INT); // entity id
                map(Types.STRING); // uuid
                map(Types.STRING); // name
                create(Types.VAR_INT, 0); // properties count
                map(Types.INT); // x
                map(Types.INT); // y
                map(Types.INT); // z
                map(Types.BYTE); // yaw
                map(Types.BYTE); // pitch
                map(Types.SHORT); // item in hand
                map(Types1_7_6.ENTITY_DATA_LIST); // entity data
                handler(wrapper -> wrapper.set(Types.STRING, 0, fixGameProfileUuid(wrapper.get(Types.STRING, 0), wrapper.get(Types.STRING, 1))));
            }
        });
        this.registerClientbound(ClientboundPackets1_7_2.CHAT, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.STRING, Types.STRING, TextRewriter::toClient); // message
            }
        });
        this.registerClientbound(ClientboundPackets1_7_2.BLOCK_ENTITY_DATA, new PacketHandlers() {
            @Override
            public void register() {
                map(Types1_7_6.BLOCK_POSITION_SHORT); // position
                map(Types.UNSIGNED_BYTE); // type
                map(Types1_7_6.NBT); // data
                handler(wrapper -> {
                    final BlockPosition pos = wrapper.get(Types1_7_6.BLOCK_POSITION_SHORT, 0);
                    final short type = wrapper.get(Types.UNSIGNED_BYTE, 0);
                    final CompoundTag tag = wrapper.get(Types1_7_6.NBT, 0);
                    if (type != 4/*skull*/) return;
                    final byte skullType = tag.getByte("SkullType");
                    if (skullType != 3 /*player_skull*/) return;

                    final StringTag extraType = tag.removeUnchecked("ExtraType");
                    if (extraType == null || extraType.getValue().isEmpty()) return;

                    if (ViaLegacy.getConfig().isLegacySkullLoading()) {
                        final GameProfileFetcher gameProfileFetcher = Via.getManager().getProviders().get(GameProfileFetcher.class);

                        final String skullName = extraType.getValue();
                        final CompoundTag newTag = tag.copy();

                        if (gameProfileFetcher.isUUIDLoaded(skullName)) { // short cut if skull is already loaded
                            final UUID uuid = gameProfileFetcher.getMojangUUID(skullName);
                            if (gameProfileFetcher.isGameProfileLoaded(uuid)) {
                                final GameProfile skullProfile = gameProfileFetcher.getGameProfile(uuid);
                                if (skullProfile == null || skullProfile.isOffline()) return;

                                newTag.put("Owner", writeGameProfileToTag(skullProfile));
                                wrapper.set(Types1_7_6.NBT, 0, newTag);
                                return;
                            }
                        }

                        gameProfileFetcher.getMojangUUIDAsync(skullName).thenAccept(uuid -> {
                            final GameProfile skullProfile = gameProfileFetcher.getGameProfile(uuid);
                            if (skullProfile == null || skullProfile.isOffline()) return;

                            newTag.put("Owner", writeGameProfileToTag(skullProfile));
                            try {
                                final PacketWrapper updateSkull = PacketWrapper.create(ClientboundPackets1_7_2.BLOCK_ENTITY_DATA, wrapper.user());
                                updateSkull.write(Types1_7_6.BLOCK_POSITION_SHORT, pos);
                                updateSkull.write(Types.UNSIGNED_BYTE, type);
                                updateSkull.write(Types1_7_6.NBT, newTag);
                                updateSkull.send(Protocolr1_7_2_5Tor1_7_6_10.class);
                            } catch (Throwable e) {
                                ViaLegacy.getPlatform().getLogger().log(Level.WARNING, "Failed to update skull block entity data for " + skullName, e);
                            }
                        });
                    }
                });
            }
        });
    }

    public static CompoundTag writeGameProfileToTag(final GameProfile gameProfile) {
        final CompoundTag ownerTag = new CompoundTag();

        if (gameProfile.userName != null && !gameProfile.userName.isEmpty()) ownerTag.putString("Name", gameProfile.userName);
        if (gameProfile.uuid != null) ownerTag.putString("Id", gameProfile.uuid.toString());
        if (!gameProfile.properties.isEmpty()) {
            final CompoundTag propertiesTag = new CompoundTag();

            for (Map.Entry<String, List<GameProfile.Property>> entry : gameProfile.properties.entrySet()) {
                final ListTag<CompoundTag> propertiesList = new ListTag<>(CompoundTag.class);

                for (GameProfile.Property property : entry.getValue()) {
                    final CompoundTag propertyTag = new CompoundTag();
                    propertyTag.putString("Value", property.value);
                    if (property.signature != null) {
                        propertyTag.putString("Signature", property.signature);
                    }
                    propertiesList.add(propertyTag);
                }

                propertiesTag.put(entry.getKey(), propertiesList);
            }

            ownerTag.put("Properties", propertiesTag);
        }

        return ownerTag;
    }

    private static String fixGameProfileUuid(final String uuid, final String name) {
        if (uuid.matches(UUID_PATTERN)) {
            return uuid;
        } else if (uuid.length() == 32) {
            final String dashedUuid = ClientboundBaseProtocol1_7.addDashes(uuid);
            if (dashedUuid.matches(UUID_PATTERN)) {
                return dashedUuid;
            }
        }
        return UuidUtil.createOfflinePlayerUuid(name).toString();
    }

}
