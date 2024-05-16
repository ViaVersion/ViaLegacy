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
package net.raphimc.vialegacy.protocols.beta.protocolb1_6_0_6tob1_5_0_2;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.Position;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.platform.providers.ViaProviders;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.api.type.Types;
import com.viaversion.viaversion.util.IdAndData;
import net.raphimc.vialegacy.api.data.BlockList1_6;
import net.raphimc.vialegacy.api.data.ItemList1_6;
import net.raphimc.vialegacy.api.protocol.StatelessProtocol;
import net.raphimc.vialegacy.api.splitter.PreNettySplitter;
import net.raphimc.vialegacy.protocols.beta.protocolb1_6_0_6tob1_5_0_2.storage.WorldTimeStorage;
import net.raphimc.vialegacy.protocols.beta.protocolb1_6_0_6tob1_5_0_2.task.TimeTrackTask;
import net.raphimc.vialegacy.protocols.beta.protocolb1_8_0_1tob1_7_0_3.ClientboundPacketsb1_7;
import net.raphimc.vialegacy.protocols.beta.protocolb1_8_0_1tob1_7_0_3.ServerboundPacketsb1_7;
import net.raphimc.vialegacy.protocols.release.protocol1_4_4_5to1_4_2.types.Types1_4_2;
import net.raphimc.vialegacy.protocols.release.protocol1_7_2_5to1_6_4.storage.ChunkTracker;
import net.raphimc.vialegacy.protocols.release.protocol1_7_2_5to1_6_4.storage.PlayerInfoStorage;
import net.raphimc.vialegacy.protocols.release.protocol1_7_2_5to1_6_4.types.Types1_6_4;
import net.raphimc.vialegacy.protocols.release.protocol1_8to1_7_6_10.types.Types1_7_6;

public class Protocolb1_6_0_6tob1_5_0_2 extends StatelessProtocol<ClientboundPacketsb1_5, ClientboundPacketsb1_7, ServerboundPacketsb1_5, ServerboundPacketsb1_7> {

    public Protocolb1_6_0_6tob1_5_0_2() {
        super(ClientboundPacketsb1_5.class, ClientboundPacketsb1_7.class, ServerboundPacketsb1_5.class, ServerboundPacketsb1_7.class);
    }

    @Override
    protected void registerPackets() {
        this.registerClientbound(ClientboundPacketsb1_5.SET_TIME, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.LONG); // time
                handler(wrapper -> wrapper.user().get(WorldTimeStorage.class).time = wrapper.get(Types.LONG, 0));
            }
        });
        this.registerClientbound(ClientboundPacketsb1_5.RESPAWN, new PacketHandlers() {
            @Override
            public void register() {
                create(Types.BYTE, (byte) 0); // dimension id
            }
        });
        this.registerClientbound(ClientboundPacketsb1_5.ADD_ENTITY, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // entity id
                map(Types.BYTE); // type id
                map(Types.INT); // x
                map(Types.INT); // y
                map(Types.INT); // z
                create(Types.INT, 0); // data
            }
        });

        this.registerServerbound(ServerboundPacketsb1_7.RESPAWN, new PacketHandlers() {
            @Override
            public void register() {
                read(Types.BYTE); // dimension id
            }
        });
        this.registerServerbound(ServerboundPacketsb1_7.USE_ITEM_ON, new PacketHandlers() {
            @Override
            public void register() {
                map(Types1_7_6.POSITION_UBYTE); // position
                map(Types.UNSIGNED_BYTE); // direction
                map(Types1_4_2.NBTLESS_ITEM); // item
                handler(wrapper -> {
                    final PlayerInfoStorage playerInfoStorage = wrapper.user().get(PlayerInfoStorage.class);
                    Position pos = wrapper.get(Types1_7_6.POSITION_UBYTE, 0);
                    IdAndData block = wrapper.user().get(ChunkTracker.class).getBlockNotNull(pos);
                    final Item item = wrapper.get(Types1_4_2.NBTLESS_ITEM, 0);
                    if (block.getId() == BlockList1_6.bed.blockID) {
                        final byte[][] headBlockToFootBlock = {{0, 1}, {-1, 0}, {0, -1}, {1, 0}};
                        final boolean isFoot = (block.getData() & 8) != 0;
                        if (!isFoot) {
                            final int bedDirection = block.getData() & 3;
                            pos = new Position(pos.x() + headBlockToFootBlock[bedDirection][0], pos.y(), pos.z() + headBlockToFootBlock[bedDirection][1]);
                            block = wrapper.user().get(ChunkTracker.class).getBlockNotNull(pos);
                            if (block.getId() != BlockList1_6.bed.blockID) return;
                        }

                        final boolean isOccupied = (block.getData() & 4) != 0;
                        if (isOccupied) {
                            final PacketWrapper chat = PacketWrapper.create(ClientboundPacketsb1_7.CHAT, wrapper.user());
                            chat.write(Types1_6_4.STRING, "This bed is occupied");
                            chat.send(Protocolb1_6_0_6tob1_5_0_2.class);
                            return;
                        }

                        int dayTime = (int) (wrapper.user().get(WorldTimeStorage.class).time % 24000L);
                        float dayPercent = (dayTime + 1.0F) / 24000F - 0.25F;
                        if (dayPercent < 0.0F) dayPercent++;
                        if (dayPercent > 1.0F) dayPercent--;

                        final float tempDayPercent = dayPercent;
                        dayPercent = 1.0F - (float) ((Math.cos((double) dayPercent * Math.PI) + 1.0D) / 2D);
                        dayPercent = tempDayPercent + (dayPercent - tempDayPercent) / 3F;
                        float skyRotation = (float) (1.0F - (Math.cos(dayPercent * Math.PI * 2.0F) * 2.0F + 0.5F));
                        if (skyRotation < 0.0F) skyRotation = 0.0F;
                        if (skyRotation > 1.0F) skyRotation = 1.0F;

                        final boolean isDayTime = (int) (skyRotation * 11F) < 4;

                        if (isDayTime) {
                            final PacketWrapper chat = PacketWrapper.create(ClientboundPacketsb1_7.CHAT, wrapper.user());
                            chat.write(Types1_6_4.STRING, "You can only sleep at night");
                            chat.send(Protocolb1_6_0_6tob1_5_0_2.class);
                            return;
                        }

                        if (Math.abs(playerInfoStorage.posX - (double) pos.x()) > 3D || Math.abs(playerInfoStorage.posY - (double) pos.y()) > 2D || Math.abs(playerInfoStorage.posZ - (double) pos.z()) > 3D) {
                            return;
                        }

                        final PacketWrapper useBed = PacketWrapper.create(ClientboundPacketsb1_7.PLAYER_SLEEP, wrapper.user());
                        useBed.write(Types.INT, playerInfoStorage.entityId); // entity id
                        useBed.write(Types.BYTE, (byte) 0); // magic value (always 0)
                        useBed.write(Types1_7_6.POSITION_BYTE, pos); // position
                        useBed.send(Protocolb1_6_0_6tob1_5_0_2.class);
                    } else if (block.getId() == BlockList1_6.jukebox.blockID) {
                        if (block.getData() > 0) {
                            final PacketWrapper effect = PacketWrapper.create(ClientboundPacketsb1_7.LEVEL_EVENT, wrapper.user());
                            effect.write(Types.INT, 1005); // effect id
                            effect.write(Types1_7_6.POSITION_UBYTE, pos); // position
                            effect.write(Types.INT, 0); // data
                            effect.send(Protocolb1_6_0_6tob1_5_0_2.class);
                        } else if (item != null && (item.identifier() == ItemList1_6.record13.itemID || item.identifier() == ItemList1_6.recordCat.itemID)) {
                            final PacketWrapper effect = PacketWrapper.create(ClientboundPacketsb1_7.LEVEL_EVENT, wrapper.user());
                            effect.write(Types.INT, 1005); // effect id
                            effect.write(Types1_7_6.POSITION_UBYTE, pos); // position
                            effect.write(Types.INT, item.identifier()); // data
                            effect.send(Protocolb1_6_0_6tob1_5_0_2.class);
                        }
                    }
                });
            }
        });
    }

    @Override
    public void register(ViaProviders providers) {
        Via.getPlatform().runRepeatingSync(new TimeTrackTask(), 1L);
    }

    @Override
    public void init(UserConnection userConnection) {
        userConnection.put(new PreNettySplitter(Protocolb1_6_0_6tob1_5_0_2.class, ClientboundPacketsb1_5::getPacket));

        userConnection.put(new WorldTimeStorage());
    }

}
