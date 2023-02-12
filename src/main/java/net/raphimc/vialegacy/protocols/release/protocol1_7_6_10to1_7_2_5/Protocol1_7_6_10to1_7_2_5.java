/*
 * This file is part of ViaLegacy - https://github.com/RaphiMC/ViaLegacy
 * Copyright (C) 2023 RK_01/RaphiMC and contributors
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
package net.raphimc.vialegacy.protocols.release.protocol1_7_6_10to1_7_2_5;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.minecraft.Position;
import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.packet.State;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ByteTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ListTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
import com.viaversion.viaversion.protocols.base.BaseProtocol1_7;
import com.viaversion.viaversion.protocols.base.ClientboundLoginPackets;
import net.raphimc.vialegacy.ViaLegacy;
import net.raphimc.vialegacy.protocols.release.protocol1_7_6_10to1_7_2_5.rewriter.TranslationRewriter;
import net.raphimc.vialegacy.protocols.release.protocol1_8to1_7_6_10.model.GameProfile;
import net.raphimc.vialegacy.protocols.release.protocol1_8to1_7_6_10.providers.GameProfileFetcher;
import net.raphimc.vialegacy.protocols.release.protocol1_8to1_7_6_10.types.Types1_7_6;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Protocol1_7_6_10to1_7_2_5 extends AbstractProtocol<ClientboundPackets1_7_2, ClientboundPackets1_7_2, ServerboundPackets1_7_2, ServerboundPackets1_7_2> {

    public Protocol1_7_6_10to1_7_2_5() {
        super(ClientboundPackets1_7_2.class, ClientboundPackets1_7_2.class, ServerboundPackets1_7_2.class, ServerboundPackets1_7_2.class);
    }

    @Override
    protected void registerPackets() {
        this.registerClientbound(State.LOGIN, ClientboundLoginPackets.GAME_PROFILE.getId(), ClientboundLoginPackets.GAME_PROFILE.getId(), new PacketHandlers() {
            @Override
            public void register() {
                map(Type.STRING, Type.STRING, BaseProtocol1_7::addDashes); // uuid
                map(Type.STRING); // name
            }
        });
        this.registerClientbound(ClientboundPackets1_7_2.SPAWN_PLAYER, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.VAR_INT); // entity id
                map(Type.STRING, Type.STRING, BaseProtocol1_7::addDashes); // uuid
                map(Type.STRING); // name
                create(Type.VAR_INT, 0); // properties count
                map(Type.INT); // x
                map(Type.INT); // y
                map(Type.INT); // z
                map(Type.BYTE); // yaw
                map(Type.BYTE); // pitch
                map(Type.SHORT); // item in hand
                map(Types1_7_6.METADATA_LIST); // metadata
            }
        });
        this.registerClientbound(ClientboundPackets1_7_2.CHAT_MESSAGE, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.STRING, Type.STRING, TranslationRewriter::toClient); // message
            }
        });
        this.registerClientbound(ClientboundPackets1_7_2.BLOCK_ENTITY_DATA, new PacketHandlers() {
            @Override
            public void register() {
                map(Types1_7_6.POSITION_SHORT); // position
                map(Type.UNSIGNED_BYTE); // type
                map(Types1_7_6.COMPRESSED_NBT); // data
                handler(wrapper -> {
                    final GameProfileFetcher gameProfileFetcher = Via.getManager().getProviders().get(GameProfileFetcher.class);

                    final Position pos = wrapper.get(Types1_7_6.POSITION_SHORT, 0);
                    final short type = wrapper.get(Type.UNSIGNED_BYTE, 0);
                    final CompoundTag tag = wrapper.get(Types1_7_6.COMPRESSED_NBT, 0);
                    if (type != 4/*skull*/) return;
                    final ByteTag skullType = tag.get("SkullType");
                    if (skullType == null || skullType.asByte() != 3/*player_skull*/) return;

                    final StringTag extraType = tag.remove("ExtraType");

                    if (!ViaLegacy.getConfig().isLegacySkullLoading()) return;

                    final String skullName = extraType == null ? "" : extraType.getValue();
                    final CompoundTag newTag = tag.clone();

                    if (gameProfileFetcher.isUUIDLoaded(skullName)) { // short cut if skull is already loaded
                        final UUID uuid = gameProfileFetcher.getMojangUUID(skullName);
                        if (gameProfileFetcher.isGameProfileLoaded(uuid)) {
                            final GameProfile skullProfile = gameProfileFetcher.getGameProfile(uuid);
                            if (skullProfile == null || skullProfile.isOffline()) return;

                            newTag.put("Owner", writeGameProfileToTag(skullProfile));
                            wrapper.set(Types1_7_6.COMPRESSED_NBT, 0, newTag);
                            return;
                        }
                    }

                    gameProfileFetcher.getMojangUUIDAsync(skullName).thenAccept(uuid -> {
                        final GameProfile skullProfile = gameProfileFetcher.getGameProfile(uuid);
                        if (skullProfile == null || skullProfile.isOffline()) return;

                        newTag.put("Owner", writeGameProfileToTag(skullProfile));
                        try {
                            final PacketWrapper updateSkull = PacketWrapper.create(ClientboundPackets1_7_2.BLOCK_ENTITY_DATA, wrapper.user());
                            updateSkull.write(Types1_7_6.POSITION_SHORT, pos);
                            updateSkull.write(Type.UNSIGNED_BYTE, type);
                            updateSkull.write(Types1_7_6.COMPRESSED_NBT, newTag);
                            updateSkull.send(Protocol1_7_6_10to1_7_2_5.class);
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    });
                });
            }
        });
    }

    public static CompoundTag writeGameProfileToTag(final GameProfile gameProfile) {
        final CompoundTag ownerTag = new CompoundTag();

        if (gameProfile.userName != null && !gameProfile.userName.isEmpty()) ownerTag.put("Name", new StringTag(gameProfile.userName));
        if (gameProfile.uuid != null) ownerTag.put("Id", new StringTag(gameProfile.uuid.toString()));
        if (!gameProfile.properties.isEmpty()) {
            final CompoundTag propertiesTag = new CompoundTag();

            for (Map.Entry<String, List<GameProfile.Property>> entry : gameProfile.properties.entrySet()) {
                final ListTag propertiesList = new ListTag();

                for (GameProfile.Property property : entry.getValue()) {
                    final CompoundTag propertyTag = new CompoundTag();
                    propertyTag.put("Value", new StringTag(property.value));
                    if (property.signature != null) {
                        propertyTag.put("Signature", new StringTag(property.signature));
                    }
                    propertiesList.add(propertyTag);
                }

                propertiesTag.put(entry.getKey(), propertiesList);
            }

            ownerTag.put("Properties", propertiesTag);
        }

        return ownerTag;
    }

}
