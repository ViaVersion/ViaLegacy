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
package net.raphimc.vialegacy.protocol.beta.b1_7_0_3tob1_8_0_1;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.BlockPosition;
import com.viaversion.viaversion.api.minecraft.chunks.Chunk;
import com.viaversion.viaversion.api.minecraft.chunks.ChunkSection;
import com.viaversion.viaversion.api.minecraft.chunks.PaletteType;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.packet.State;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.api.type.Types;
import net.raphimc.vialegacy.ViaLegacy;
import net.raphimc.vialegacy.api.data.BlockList1_6;
import net.raphimc.vialegacy.api.data.ItemList1_6;
import net.raphimc.vialegacy.api.protocol.StatelessProtocol;
import net.raphimc.vialegacy.api.splitter.PreNettySplitter;
import net.raphimc.vialegacy.protocol.alpha.a1_2_3_5_1_2_6tob1_0_1_1_1.storage.AlphaInventoryTracker;
import net.raphimc.vialegacy.protocol.beta.b1_7_0_3tob1_8_0_1.packet.ClientboundPacketsb1_7;
import net.raphimc.vialegacy.protocol.beta.b1_7_0_3tob1_8_0_1.packet.ServerboundPacketsb1_7;
import net.raphimc.vialegacy.protocol.beta.b1_7_0_3tob1_8_0_1.storage.PlayerHealthTracker;
import net.raphimc.vialegacy.protocol.beta.b1_7_0_3tob1_8_0_1.storage.PlayerNameTracker;
import net.raphimc.vialegacy.protocol.beta.b1_7_0_3tob1_8_0_1.types.Typesb1_7_0_3;
import net.raphimc.vialegacy.protocol.beta.b1_8_0_1tor1_0_0_1.packet.ClientboundPacketsb1_8;
import net.raphimc.vialegacy.protocol.beta.b1_8_0_1tor1_0_0_1.packet.ServerboundPacketsb1_8;
import net.raphimc.vialegacy.protocol.beta.b1_8_0_1tor1_0_0_1.types.Typesb1_8_0_1;
import net.raphimc.vialegacy.protocol.classic.c0_0_20a_27toc0_28_30.Protocolc0_0_20a_27Toc0_28_30;
import net.raphimc.vialegacy.protocol.release.r1_1tor1_2_1_3.model.LegacyNibbleArray;
import net.raphimc.vialegacy.protocol.release.r1_1tor1_2_1_3.storage.SeedStorage;
import net.raphimc.vialegacy.protocol.release.r1_1tor1_2_1_3.types.Types1_1;
import net.raphimc.vialegacy.protocol.release.r1_2_4_5tor1_3_1_2.storage.EntityTracker;
import net.raphimc.vialegacy.protocol.release.r1_3_1_2tor1_4_2.types.Types1_3_1;
import net.raphimc.vialegacy.protocol.release.r1_4_2tor1_4_4_5.types.Types1_4_2;
import net.raphimc.vialegacy.protocol.release.r1_6_4tor1_7_2_5.storage.ChunkTracker;
import net.raphimc.vialegacy.protocol.release.r1_6_4tor1_7_2_5.types.Types1_6_4;
import net.raphimc.vialegacy.protocol.release.r1_7_6_10tor1_8.types.Types1_7_6;

import java.util.concurrent.ThreadLocalRandom;

public class Protocolb1_7_0_3Tob1_8_0_1 extends StatelessProtocol<ClientboundPacketsb1_7, ClientboundPacketsb1_8, ServerboundPacketsb1_7, ServerboundPacketsb1_8> {

    public Protocolb1_7_0_3Tob1_8_0_1() {
        super(ClientboundPacketsb1_7.class, ClientboundPacketsb1_8.class, ServerboundPacketsb1_7.class, ServerboundPacketsb1_8.class);
    }

    @Override
    protected void registerPackets() {
        this.registerClientbound(ClientboundPacketsb1_7.KEEP_ALIVE, wrapper -> {
            wrapper.write(Types.INT, ThreadLocalRandom.current().nextInt(1, Short.MAX_VALUE)); // key
        });
        this.registerClientbound(ClientboundPacketsb1_7.LOGIN, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // entity id
                map(Types1_6_4.STRING); // username
                map(Types.LONG); // seed
                create(Types.INT, 0); // game mode
                map(Types.BYTE); // dimension id
                create(Types.BYTE, (byte) 1); // difficulty
                create(Types.BYTE, (byte) -128); // world height
                create(Types.BYTE, (byte) 100); // max players
                handler(wrapper -> {
                    final PacketWrapper playerListEntry = PacketWrapper.create(ClientboundPacketsb1_8.PLAYER_INFO, wrapper.user());
                    playerListEntry.write(Types1_6_4.STRING, wrapper.user().getProtocolInfo().getUsername()); // name
                    playerListEntry.write(Types.BOOLEAN, true); // online
                    playerListEntry.write(Types.SHORT, (short) 0); // ping

                    final PacketWrapper updateHealth = PacketWrapper.create(ClientboundPacketsb1_7.SET_HEALTH, wrapper.user());
                    updateHealth.write(Types.SHORT, (short) 20); // health

                    wrapper.send(Protocolb1_7_0_3Tob1_8_0_1.class);
                    wrapper.cancel();
                    playerListEntry.send(Protocolb1_7_0_3Tob1_8_0_1.class);
                    updateHealth.send(Protocolb1_7_0_3Tob1_8_0_1.class, false);
                });
            }
        });
        this.registerClientbound(ClientboundPacketsb1_7.SET_HEALTH, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.SHORT); // health
                create(Types.SHORT, (short) 6); // food
                create(Types.FLOAT, 0F); // saturation
                handler(wrapper -> wrapper.user().get(PlayerHealthTracker.class).setHealth(wrapper.get(Types.SHORT, 0)));
                handler(wrapper -> {
                    if (ViaLegacy.getConfig().enableB1_7_3Sprinting()) {
                        wrapper.set(Types.SHORT, 1, (short) 20); // food
                        wrapper.set(Types.FLOAT, 0, 0F); // saturation
                    }
                });
            }
        });
        this.registerClientbound(ClientboundPacketsb1_7.RESPAWN, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.BYTE); // dimension id
                create(Types.BYTE, (byte) 1); // difficulty
                create(Types.BYTE, (byte) 0); // game mode
                create(Types.SHORT, (short) 128); // world height
                handler(wrapper -> wrapper.write(Types.LONG, wrapper.user().get(SeedStorage.class).seed)); // seed
            }
        });
        this.registerClientbound(ClientboundPacketsb1_7.ADD_PLAYER, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // entity id
                map(Types1_6_4.STRING); // username
                map(Types.INT); // x
                map(Types.INT); // y
                map(Types.INT); // z
                map(Types.BYTE); // yaw
                map(Types.BYTE); // pitch
                map(Types.UNSIGNED_SHORT); // item
                handler(wrapper -> {
                    final int entityId = wrapper.get(Types.INT, 0);
                    final PlayerNameTracker playerNameTracker = wrapper.user().get(PlayerNameTracker.class);
                    playerNameTracker.names.put(entityId, wrapper.get(Types1_6_4.STRING, 0));

                    final PacketWrapper playerListEntry = PacketWrapper.create(ClientboundPacketsb1_8.PLAYER_INFO, wrapper.user());
                    playerListEntry.write(Types1_6_4.STRING, playerNameTracker.names.get(entityId)); // name
                    playerListEntry.write(Types.BOOLEAN, true); // online
                    playerListEntry.write(Types.SHORT, (short) 0); // ping
                    playerListEntry.send(Protocolb1_7_0_3Tob1_8_0_1.class);
                });
            }
        });
        this.registerClientbound(ClientboundPacketsb1_7.ADD_MOB, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // entity id
                map(Types.UNSIGNED_BYTE); // type id
                map(Types.INT); // x
                map(Types.INT); // y
                map(Types.INT); // z
                map(Types.BYTE); // yaw
                map(Types.BYTE); // pitch
                map(Types1_3_1.ENTITY_DATA_LIST); // entity data
                handler(wrapper -> {
                    final short entityType = wrapper.get(Types.UNSIGNED_BYTE, 0);
                    if (entityType == 49) { // monster
                        final PacketWrapper spawnMonster = PacketWrapper.create(ClientboundPacketsb1_8.ADD_PLAYER, wrapper.user());
                        spawnMonster.write(Types.INT, wrapper.get(Types.INT, 0)); // entity id
                        spawnMonster.write(Types1_6_4.STRING, "Monster"); // username
                        spawnMonster.write(Types.INT, wrapper.get(Types.INT, 1)); // x
                        spawnMonster.write(Types.INT, wrapper.get(Types.INT, 2)); // y
                        spawnMonster.write(Types.INT, wrapper.get(Types.INT, 3)); // z
                        spawnMonster.write(Types.BYTE, wrapper.get(Types.BYTE, 0)); // yaw
                        spawnMonster.write(Types.BYTE, wrapper.get(Types.BYTE, 1)); // pitch
                        spawnMonster.write(Types.UNSIGNED_SHORT, 0); // item

                        final PacketWrapper setEntityData = PacketWrapper.create(ClientboundPacketsb1_8.SET_ENTITY_DATA, wrapper.user());
                        setEntityData.write(Types.INT, wrapper.get(Types.INT, 0)); // entity id
                        setEntityData.write(Types1_3_1.ENTITY_DATA_LIST, wrapper.get(Types1_3_1.ENTITY_DATA_LIST, 0)); // entity data

                        wrapper.cancel();
                        spawnMonster.send(Protocolb1_7_0_3Tob1_8_0_1.class);
                        setEntityData.send(Protocolb1_7_0_3Tob1_8_0_1.class);
                    }
                });
            }
        });
        this.registerClientbound(ClientboundPacketsb1_7.REMOVE_ENTITIES, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // entity id
                handler(wrapper -> {
                    final PlayerNameTracker playerNameTracker = wrapper.user().get(PlayerNameTracker.class);
                    final String name = playerNameTracker.names.get(wrapper.get(Types.INT, 0).intValue());
                    if (name != null) {
                        final PacketWrapper playerListEntry = PacketWrapper.create(ClientboundPacketsb1_8.PLAYER_INFO, wrapper.user());
                        playerListEntry.write(Types1_6_4.STRING, name); // name
                        playerListEntry.write(Types.BOOLEAN, false); // online
                        playerListEntry.write(Types.SHORT, (short) 0); // ping
                        playerListEntry.send(Protocolb1_7_0_3Tob1_8_0_1.class);
                    }
                });
            }
        });
        this.registerClientbound(ClientboundPacketsb1_7.LEVEL_CHUNK, wrapper -> {
            final Chunk chunk = wrapper.passthrough(Types1_1.CHUNK);

            boolean hasChest = false;
            for (ChunkSection section : chunk.getSections()) {
                if (section == null || !section.getLight().hasSkyLight()) continue;
                for (int i = 0; i < section.palette(PaletteType.BLOCKS).size(); i++) {
                    if (section.palette(PaletteType.BLOCKS).idByIndex(i) >> 4 == BlockList1_6.chest.blockId()) {
                        hasChest = true;
                        break;
                    }
                }
                if (!hasChest) continue;

                final LegacyNibbleArray sectionSkyLight = new LegacyNibbleArray(section.getLight().getSkyLight(), 4);
                for (int y = 0; y < 16; y++)
                    for (int x = 0; x < 16; x++)
                        for (int z = 0; z < 16; z++)
                            if (section.palette(PaletteType.BLOCKS).idAt(x, y, z) >> 4 == BlockList1_6.chest.blockId())
                                sectionSkyLight.set(x, y, z, 15);
            }
        });
        this.registerClientbound(ClientboundPacketsb1_7.GAME_EVENT, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.BYTE); // reason
                create(Types.BYTE, (byte) 0); // value
            }
        });
        this.registerClientbound(ClientboundPacketsb1_7.OPEN_SCREEN, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.UNSIGNED_BYTE); // window id
                map(Types.UNSIGNED_BYTE); // window type
                map(Typesb1_7_0_3.STRING, Types1_6_4.STRING); // title
                map(Types.UNSIGNED_BYTE); // slots
            }
        });

        this.registerServerbound(State.PLAY, ServerboundPacketsb1_8.SERVER_PING.getId(), -2, wrapper -> {
            if (wrapper.user().getProtocolInfo().getPipeline().contains(Protocolc0_0_20a_27Toc0_28_30.class)) {
                // Classic servers have issues with sockets connecting and disconnecting without sending any data.
                // Because of that we send an invalid packet id to force the server to disconnect us.
                // This is the reason why the packet id is mapped to -2. (-1 gets handled internally by ViaVersion)
                // This fix is needed for <= c0.27, >= c0.28 closes the socket after 3 seconds of inactivity.
                wrapper.clearPacket();
            } else {
                wrapper.cancel();
            }
            final PacketWrapper pingResponse = PacketWrapper.create(ClientboundPacketsb1_8.DISCONNECT, wrapper.user());
            pingResponse.write(Types1_6_4.STRING, ViaLegacy.getConfig().getB1_7_3Motd() + "§0§1");
            pingResponse.send(Protocolb1_7_0_3Tob1_8_0_1.class);
        });
        this.registerServerbound(ServerboundPacketsb1_8.LOGIN, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // protocol id
                map(Types1_6_4.STRING); // username
                map(Types.LONG); // seed
                read(Types.INT); // game mode
                map(Types.BYTE); // dimension id
                read(Types.BYTE); // difficulty
                read(Types.BYTE); // world height
                read(Types.BYTE); // max players
            }
        });
        this.registerServerbound(ServerboundPacketsb1_8.RESPAWN, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.BYTE); // dimension id
                read(Types.BYTE); // difficulty
                read(Types.BYTE); // game mode
                read(Types.SHORT); // world height
                read(Types.LONG); // seed
            }
        });
        this.registerServerbound(ServerboundPacketsb1_8.PLAYER_ACTION, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.UNSIGNED_BYTE); // status
                handler(wrapper -> {
                    final short status = wrapper.get(Types.UNSIGNED_BYTE, 0);
                    if (status == 5) wrapper.cancel(); // Stop using item
                });
                map(Types1_7_6.BLOCK_POSITION_UBYTE); // position
                map(Types.UNSIGNED_BYTE); // direction
            }
        });
        this.registerServerbound(ServerboundPacketsb1_8.USE_ITEM_ON, new PacketHandlers() {
            @Override
            public void register() {
                map(Types1_7_6.BLOCK_POSITION_UBYTE); // position
                map(Types.UNSIGNED_BYTE); // direction
                map(Types1_4_2.NBTLESS_ITEM); // item
                handler(wrapper -> {
                    if (wrapper.get(Types.UNSIGNED_BYTE, 0) == 255) {
                        final Item item = wrapper.get(Types1_4_2.NBTLESS_ITEM, 0);
                        if (item != null && isSword(item)) {
                            wrapper.cancel();
                            final PacketWrapper entityStatus = PacketWrapper.create(ClientboundPacketsb1_8.ENTITY_EVENT, wrapper.user());
                            entityStatus.write(Types.INT, wrapper.user().get(EntityTracker.class).getPlayerID()); // entity id
                            entityStatus.write(Types.BYTE, (byte) 9); // status | 9 = STOP_ITEM_USE
                            entityStatus.send(Protocolb1_7_0_3Tob1_8_0_1.class);
                        }
                    } else {
                        final BlockPosition pos = wrapper.get(Types1_7_6.BLOCK_POSITION_UBYTE, 0);
                        if (wrapper.user().get(ChunkTracker.class).getBlockNotNull(pos).getId() == BlockList1_6.cake.blockId()) {
                            final PacketWrapper updateHealth = PacketWrapper.create(ClientboundPacketsb1_7.SET_HEALTH, wrapper.user());
                            updateHealth.write(Types.SHORT, wrapper.user().get(PlayerHealthTracker.class).getHealth()); // health
                            updateHealth.send(Protocolb1_7_0_3Tob1_8_0_1.class, false);
                        }
                    }
                });
            }
        });
        this.registerServerbound(ServerboundPacketsb1_8.PLAYER_COMMAND, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // entity id
                map(Types.BYTE); // action id
                handler(wrapper -> {
                    if (wrapper.get(Types.BYTE, 0) > 3) wrapper.cancel();
                });
            }
        });
        this.registerServerbound(ServerboundPacketsb1_8.CONTAINER_CLICK, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.BYTE); // window id
                handler(wrapper -> {
                    if (wrapper.passthrough(Types.SHORT) /*slot*/ == -1) wrapper.cancel();
                });
                map(Types.BYTE); // button
                map(Types.SHORT); // action
                map(Types.BYTE); // mode
                map(Types1_4_2.NBTLESS_ITEM); // item
            }
        });
        this.registerServerbound(ServerboundPacketsb1_8.SET_CREATIVE_MODE_SLOT, null, wrapper -> {
            wrapper.cancel();
            // Track the item for later use in classic protocols
            final AlphaInventoryTracker inventoryTracker = wrapper.user().get(AlphaInventoryTracker.class);
            if (inventoryTracker != null) inventoryTracker.handleCreativeSetSlot(wrapper.read(Types.SHORT), wrapper.read(Typesb1_8_0_1.CREATIVE_ITEM));
        });
        this.registerServerbound(ServerboundPacketsb1_8.KEEP_ALIVE, wrapper -> {
            if (wrapper.read(Types.INT) != 0) { // beta client only sends this packet with the key set to 0 every second if in downloading terrain screen
                wrapper.cancel();
            }
        });
    }

    @Override
    public void init(UserConnection userConnection) {
        userConnection.put(new PreNettySplitter(Protocolb1_7_0_3Tob1_8_0_1.class, ClientboundPacketsb1_7::getPacket));

        userConnection.put(new PlayerNameTracker());
        userConnection.put(new PlayerHealthTracker());
    }

    private boolean isSword(final Item item) {
        return item.identifier() == ItemList1_6.swordWood.itemId() ||
                item.identifier() == ItemList1_6.swordStone.itemId() ||
                item.identifier() == ItemList1_6.swordIron.itemId() ||
                item.identifier() == ItemList1_6.swordGold.itemId() ||
                item.identifier() == ItemList1_6.swordDiamond.itemId();
    }

}
