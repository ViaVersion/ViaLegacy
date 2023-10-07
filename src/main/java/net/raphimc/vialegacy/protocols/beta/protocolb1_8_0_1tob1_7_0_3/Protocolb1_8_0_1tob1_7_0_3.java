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
package net.raphimc.vialegacy.protocols.beta.protocolb1_8_0_1tob1_7_0_3;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.Position;
import com.viaversion.viaversion.api.minecraft.chunks.Chunk;
import com.viaversion.viaversion.api.minecraft.chunks.ChunkSection;
import com.viaversion.viaversion.api.minecraft.chunks.PaletteType;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.packet.State;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.api.type.Type;
import net.raphimc.vialegacy.api.data.BlockList1_6;
import net.raphimc.vialegacy.api.data.ItemList1_6;
import net.raphimc.vialegacy.api.protocol.StatelessProtocol;
import net.raphimc.vialegacy.api.splitter.PreNettySplitter;
import net.raphimc.vialegacy.protocols.alpha.protocolb1_0_1_1_1toa1_2_3_5_1_2_6.storage.AlphaInventoryTracker;
import net.raphimc.vialegacy.protocols.beta.protocol1_0_0_1tob1_8_0_1.ClientboundPacketsb1_8;
import net.raphimc.vialegacy.protocols.beta.protocol1_0_0_1tob1_8_0_1.ServerboundPacketsb1_8;
import net.raphimc.vialegacy.protocols.beta.protocol1_0_0_1tob1_8_0_1.types.Typesb1_8_0_1;
import net.raphimc.vialegacy.protocols.beta.protocolb1_8_0_1tob1_7_0_3.storage.PlayerHealthTracker;
import net.raphimc.vialegacy.protocols.beta.protocolb1_8_0_1tob1_7_0_3.storage.PlayerNameTracker;
import net.raphimc.vialegacy.protocols.beta.protocolb1_8_0_1tob1_7_0_3.types.Typesb1_7_0_3;
import net.raphimc.vialegacy.protocols.classic.protocolc0_28_30toc0_0_20a_27.Protocolc0_30toc0_27;
import net.raphimc.vialegacy.protocols.release.protocol1_2_1_3to1_1.chunks.NibbleArray1_1;
import net.raphimc.vialegacy.protocols.release.protocol1_2_1_3to1_1.storage.SeedStorage;
import net.raphimc.vialegacy.protocols.release.protocol1_2_1_3to1_1.types.Types1_1;
import net.raphimc.vialegacy.protocols.release.protocol1_3_1_2to1_2_4_5.storage.EntityTracker;
import net.raphimc.vialegacy.protocols.release.protocol1_4_2to1_3_1_2.types.Types1_3_1;
import net.raphimc.vialegacy.protocols.release.protocol1_4_4_5to1_4_2.types.Types1_4_2;
import net.raphimc.vialegacy.protocols.release.protocol1_7_2_5to1_6_4.storage.ChunkTracker;
import net.raphimc.vialegacy.protocols.release.protocol1_7_2_5to1_6_4.types.Types1_6_4;
import net.raphimc.vialegacy.protocols.release.protocol1_8to1_7_6_10.types.Types1_7_6;

import java.util.concurrent.ThreadLocalRandom;

public class Protocolb1_8_0_1tob1_7_0_3 extends StatelessProtocol<ClientboundPacketsb1_7, ClientboundPacketsb1_8, ServerboundPacketsb1_7, ServerboundPacketsb1_8> {

    public Protocolb1_8_0_1tob1_7_0_3() {
        super(ClientboundPacketsb1_7.class, ClientboundPacketsb1_8.class, ServerboundPacketsb1_7.class, ServerboundPacketsb1_8.class);
    }

    @Override
    protected void registerPackets() {
        this.registerClientbound(ClientboundPacketsb1_7.KEEP_ALIVE, wrapper -> {
            wrapper.write(Type.INT, ThreadLocalRandom.current().nextInt(1, Short.MAX_VALUE)); // key
        });
        this.registerClientbound(ClientboundPacketsb1_7.JOIN_GAME, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.INT); // entity id
                map(Types1_6_4.STRING); // username
                map(Type.LONG); // seed
                create(Type.INT, 0); // game mode
                map(Type.BYTE); // dimension id
                create(Type.BYTE, (byte) 1); // difficulty
                create(Type.BYTE, (byte) -128); // world height
                create(Type.BYTE, (byte) 100); // max players
                handler(wrapper -> {
                    final PacketWrapper playerListEntry = PacketWrapper.create(ClientboundPacketsb1_8.PLAYER_INFO, wrapper.user());
                    playerListEntry.write(Types1_6_4.STRING, wrapper.user().getProtocolInfo().getUsername()); // name
                    playerListEntry.write(Type.BOOLEAN, true); // online
                    playerListEntry.write(Type.SHORT, (short) 0); // ping

                    final PacketWrapper updateHealth = PacketWrapper.create(ClientboundPacketsb1_7.UPDATE_HEALTH, wrapper.user());
                    updateHealth.write(Type.SHORT, (short) 20); // health

                    wrapper.send(Protocolb1_8_0_1tob1_7_0_3.class);
                    wrapper.cancel();
                    playerListEntry.send(Protocolb1_8_0_1tob1_7_0_3.class);
                    updateHealth.send(Protocolb1_8_0_1tob1_7_0_3.class, false);
                });
            }
        });
        this.registerClientbound(ClientboundPacketsb1_7.UPDATE_HEALTH, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.SHORT); // health
                create(Type.SHORT, (short) 6); // food
                create(Type.FLOAT, 0F); // saturation
                handler(wrapper -> wrapper.user().get(PlayerHealthTracker.class).setHealth(wrapper.get(Type.SHORT, 0)));
            }
        });
        this.registerClientbound(ClientboundPacketsb1_7.RESPAWN, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.BYTE); // dimension id
                create(Type.BYTE, (byte) 1); // difficulty
                create(Type.BYTE, (byte) 0); // game mode
                create(Type.SHORT, (short) 128); // world height
                handler(wrapper -> wrapper.write(Type.LONG, wrapper.user().get(SeedStorage.class).seed)); // seed
            }
        });
        this.registerClientbound(ClientboundPacketsb1_7.SPAWN_PLAYER, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.INT); // entity id
                map(Types1_6_4.STRING); // username
                map(Type.INT); // x
                map(Type.INT); // y
                map(Type.INT); // z
                map(Type.BYTE); // yaw
                map(Type.BYTE); // pitch
                map(Type.UNSIGNED_SHORT); // item
                handler(wrapper -> {
                    final int entityId = wrapper.get(Type.INT, 0);
                    final PlayerNameTracker playerNameTracker = wrapper.user().get(PlayerNameTracker.class);
                    playerNameTracker.names.put(entityId, wrapper.get(Types1_6_4.STRING, 0));

                    final PacketWrapper playerListEntry = PacketWrapper.create(ClientboundPacketsb1_8.PLAYER_INFO, wrapper.user());
                    playerListEntry.write(Types1_6_4.STRING, playerNameTracker.names.get(entityId)); // name
                    playerListEntry.write(Type.BOOLEAN, true); // online
                    playerListEntry.write(Type.SHORT, (short) 0); // ping
                    playerListEntry.send(Protocolb1_8_0_1tob1_7_0_3.class);
                });
            }
        });
        this.registerClientbound(ClientboundPacketsb1_7.SPAWN_MOB, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.INT); // entity id
                map(Type.UNSIGNED_BYTE); // type id
                map(Type.INT); // x
                map(Type.INT); // y
                map(Type.INT); // z
                map(Type.BYTE); // yaw
                map(Type.BYTE); // pitch
                map(Types1_3_1.METADATA_LIST); // metadata
                handler(wrapper -> {
                    final short entityType = wrapper.get(Type.UNSIGNED_BYTE, 0);
                    if (entityType == 49) { // monster
                        final PacketWrapper spawnMonster = PacketWrapper.create(ClientboundPacketsb1_8.SPAWN_PLAYER, wrapper.user());
                        spawnMonster.write(Type.INT, wrapper.get(Type.INT, 0)); // entity id
                        spawnMonster.write(Types1_6_4.STRING, "Monster"); // username
                        spawnMonster.write(Type.INT, wrapper.get(Type.INT, 1)); // x
                        spawnMonster.write(Type.INT, wrapper.get(Type.INT, 2)); // y
                        spawnMonster.write(Type.INT, wrapper.get(Type.INT, 3)); // z
                        spawnMonster.write(Type.BYTE, wrapper.get(Type.BYTE, 0)); // yaw
                        spawnMonster.write(Type.BYTE, wrapper.get(Type.BYTE, 1)); // pitch
                        spawnMonster.write(Type.UNSIGNED_SHORT, 0); // item

                        final PacketWrapper entityMetadata = PacketWrapper.create(ClientboundPacketsb1_8.ENTITY_METADATA, wrapper.user());
                        entityMetadata.write(Type.INT, wrapper.get(Type.INT, 0)); // entity id
                        entityMetadata.write(Types1_3_1.METADATA_LIST, wrapper.get(Types1_3_1.METADATA_LIST, 0)); // metadata

                        wrapper.cancel();
                        spawnMonster.send(Protocolb1_8_0_1tob1_7_0_3.class);
                        entityMetadata.send(Protocolb1_8_0_1tob1_7_0_3.class);
                    }
                });
            }
        });
        this.registerClientbound(ClientboundPacketsb1_7.DESTROY_ENTITIES, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.INT); // entity id
                handler(wrapper -> {
                    final PlayerNameTracker playerNameTracker = wrapper.user().get(PlayerNameTracker.class);
                    final String name = playerNameTracker.names.get(wrapper.get(Type.INT, 0).intValue());
                    if (name != null) {
                        final PacketWrapper playerListEntry = PacketWrapper.create(ClientboundPacketsb1_8.PLAYER_INFO, wrapper.user());
                        playerListEntry.write(Types1_6_4.STRING, name); // name
                        playerListEntry.write(Type.BOOLEAN, false); // online
                        playerListEntry.write(Type.SHORT, (short) 0); // ping
                        playerListEntry.send(Protocolb1_8_0_1tob1_7_0_3.class);
                    }
                });
            }
        });
        this.registerClientbound(ClientboundPacketsb1_7.CHUNK_DATA, new PacketHandlers() {
            @Override
            public void register() {
                handler(wrapper -> {
                    final Chunk chunk = wrapper.passthrough(Types1_1.CHUNK);

                    boolean hasChest = false;
                    for (ChunkSection section : chunk.getSections()) {
                        if (section == null || !section.getLight().hasSkyLight()) continue;
                        for (int i = 0; i < section.palette(PaletteType.BLOCKS).size(); i++) {
                            if (section.palette(PaletteType.BLOCKS).idByIndex(i) >> 4 == BlockList1_6.chest.blockID) {
                                hasChest = true;
                                break;
                            }
                        }
                        if (!hasChest) continue;

                        final NibbleArray1_1 sectionSkyLight = new NibbleArray1_1(section.getLight().getSkyLight(), 4);
                        for (int y = 0; y < 16; y++)
                            for (int x = 0; x < 16; x++)
                                for (int z = 0; z < 16; z++)
                                    if (section.palette(PaletteType.BLOCKS).idAt(x, y, z) >> 4 == BlockList1_6.chest.blockID)
                                        sectionSkyLight.set(x, y, z, 15);
                    }
                });
            }
        });
        this.registerClientbound(ClientboundPacketsb1_7.GAME_EVENT, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.BYTE); // reason
                create(Type.BYTE, (byte) 0); // value
            }
        });
        this.registerClientbound(ClientboundPacketsb1_7.OPEN_WINDOW, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.UNSIGNED_BYTE); // window id
                map(Type.UNSIGNED_BYTE); // window type
                map(Typesb1_7_0_3.STRING, Types1_6_4.STRING); // title
                map(Type.UNSIGNED_BYTE); // slots
            }
        });

        this.registerServerbound(State.PLAY, ServerboundPacketsb1_8.SERVER_PING.getId(), -2, new PacketHandlers() {
            @Override
            public void register() {
                handler(wrapper -> {
                    if (wrapper.user().getProtocolInfo().getPipeline().contains(Protocolc0_30toc0_27.class)) {
                        // Classic servers have issues with sockets connecting and disconnecting without sending any data.
                        // Because of that we send an invalid packet id to force the server to disconnect us.
                        // This is the reason why the packet id is mapped to -2. (-1 gets handled internally by ViaVersion)
                        // This fix is needed for <= c0.27, >= c0.28 closes the socket after 3 seconds of inactivity.
                        wrapper.clearPacket();
                    } else {
                        wrapper.cancel();
                    }
                    final PacketWrapper pingResponse = PacketWrapper.create(ClientboundPacketsb1_8.DISCONNECT, wrapper.user());
                    pingResponse.write(Types1_6_4.STRING, "The server seems to be running!\nWait 5 seconds between each connection§0§1");
                    pingResponse.send(Protocolb1_8_0_1tob1_7_0_3.class);
                });
            }
        });
        this.registerServerbound(ServerboundPacketsb1_8.LOGIN, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.INT); // protocol id
                map(Types1_6_4.STRING); // username
                map(Type.LONG); // seed
                read(Type.INT); // game mode
                map(Type.BYTE); // dimension id
                read(Type.BYTE); // difficulty
                read(Type.BYTE); // world height
                read(Type.BYTE); // max players
            }
        });
        this.registerServerbound(ServerboundPacketsb1_8.RESPAWN, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.BYTE); // dimension id
                read(Type.BYTE); // difficulty
                read(Type.BYTE); // game mode
                read(Type.SHORT); // world height
                read(Type.LONG); // seed
            }
        });
        this.registerServerbound(ServerboundPacketsb1_8.PLAYER_DIGGING, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.UNSIGNED_BYTE); // status
                handler(wrapper -> {
                    final short status = wrapper.get(Type.UNSIGNED_BYTE, 0);
                    if (status == 5) wrapper.cancel(); // Stop using item
                });
                map(Types1_7_6.POSITION_UBYTE); // position
                map(Type.UNSIGNED_BYTE); // direction
            }
        });
        this.registerServerbound(ServerboundPacketsb1_8.PLAYER_BLOCK_PLACEMENT, new PacketHandlers() {
            @Override
            public void register() {
                map(Types1_7_6.POSITION_UBYTE); // position
                map(Type.UNSIGNED_BYTE); // direction
                map(Types1_4_2.NBTLESS_ITEM); // item
                handler(wrapper -> {
                    if (wrapper.get(Type.UNSIGNED_BYTE, 0) == 255) {
                        final Item item = wrapper.get(Types1_4_2.NBTLESS_ITEM, 0);
                        if (item != null && isSword(item)) {
                            wrapper.cancel();
                            final PacketWrapper entityStatus = PacketWrapper.create(ClientboundPacketsb1_8.ENTITY_STATUS, wrapper.user());
                            entityStatus.write(Type.INT, wrapper.user().get(EntityTracker.class).getPlayerID()); // entity id
                            entityStatus.write(Type.BYTE, (byte) 9); // status | 9 = STOP_ITEM_USE
                            entityStatus.send(Protocolb1_8_0_1tob1_7_0_3.class);
                        }
                    } else {
                        final Position pos = wrapper.get(Types1_7_6.POSITION_UBYTE, 0);
                        if (wrapper.user().get(ChunkTracker.class).getBlockNotNull(pos).id == BlockList1_6.cake.blockID) {
                            final PacketWrapper updateHealth = PacketWrapper.create(ClientboundPacketsb1_7.UPDATE_HEALTH, wrapper.user());
                            updateHealth.write(Type.SHORT, wrapper.user().get(PlayerHealthTracker.class).getHealth()); // health
                            updateHealth.send(Protocolb1_8_0_1tob1_7_0_3.class, false);
                        }
                    }
                });
            }
        });
        this.registerServerbound(ServerboundPacketsb1_8.ENTITY_ACTION, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.INT); // entity id
                map(Type.BYTE); // action id
                handler(wrapper -> {
                    if (wrapper.get(Type.BYTE, 0) > 3) wrapper.cancel();
                });
            }
        });
        this.registerServerbound(ServerboundPacketsb1_8.CLICK_WINDOW, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.BYTE); // window id
                handler(wrapper -> {
                    if (wrapper.passthrough(Type.SHORT) /*slot*/ == -1) wrapper.cancel();
                });
                map(Type.BYTE); // button
                map(Type.SHORT); // action
                map(Type.BYTE); // mode
                map(Types1_4_2.NBTLESS_ITEM); // item
            }
        });
        this.registerServerbound(ServerboundPacketsb1_8.CREATIVE_INVENTORY_ACTION, null, new PacketHandlers() {
            @Override
            public void register() {
                handler(wrapper -> {
                    wrapper.cancel();
                    // Track the item for later use in classic protocols
                    final AlphaInventoryTracker inventoryTracker = wrapper.user().get(AlphaInventoryTracker.class);
                    if (inventoryTracker != null) inventoryTracker.handleCreativeSetSlot(wrapper.read(Type.SHORT), wrapper.read(Typesb1_8_0_1.CREATIVE_ITEM));
                });
            }
        });
        this.registerServerbound(ServerboundPacketsb1_8.KEEP_ALIVE, wrapper -> {
            if (wrapper.read(Type.INT) != 0) { // beta client only sends this packet with the key set to 0 every second if in downloading terrain screen
                wrapper.cancel();
            }
        });
    }

    @Override
    public void init(UserConnection userConnection) {
        userConnection.put(new PreNettySplitter(userConnection, Protocolb1_8_0_1tob1_7_0_3.class, ClientboundPacketsb1_7::getPacket));

        userConnection.put(new PlayerNameTracker(userConnection));
        userConnection.put(new PlayerHealthTracker(userConnection));
    }

    private boolean isSword(final Item item) {
        return item.identifier() == ItemList1_6.swordWood.itemID ||
                item.identifier() == ItemList1_6.swordStone.itemID ||
                item.identifier() == ItemList1_6.swordIron.itemID ||
                item.identifier() == ItemList1_6.swordGold.itemID ||
                item.identifier() == ItemList1_6.swordDiamond.itemID;
    }

}
