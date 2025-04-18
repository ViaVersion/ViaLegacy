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
package net.raphimc.vialegacy.protocol.classic.c0_28_30toa1_0_15;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.BlockPosition;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.platform.providers.ViaProviders;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.api.type.Types;
import com.viaversion.viaversion.protocols.v1_8to1_9.packet.ClientboundPackets1_8;
import com.viaversion.viaversion.util.IdAndData;
import net.raphimc.vialegacy.ViaLegacy;
import net.raphimc.vialegacy.api.data.BlockList1_6;
import net.raphimc.vialegacy.api.model.ChunkCoord;
import net.raphimc.vialegacy.api.protocol.StatelessProtocol;
import net.raphimc.vialegacy.api.splitter.PreNettySplitter;
import net.raphimc.vialegacy.api.util.BlockFaceUtil;
import net.raphimc.vialegacy.protocol.alpha.a1_0_15toa1_0_16_2.packet.ClientboundPacketsa1_0_15;
import net.raphimc.vialegacy.protocol.alpha.a1_0_15toa1_0_16_2.packet.ServerboundPacketsa1_0_15;
import net.raphimc.vialegacy.protocol.alpha.a1_0_16_2toa1_0_17_1_0_17_4.storage.TimeLockStorage;
import net.raphimc.vialegacy.protocol.alpha.a1_0_17_1_0_17_4toa1_1_0_1_1_2_1.Protocola1_0_17_1_0_17_4Toa1_1_0_1_1_2_1;
import net.raphimc.vialegacy.protocol.alpha.a1_1_0_1_1_2_1toa1_2_0_1_2_1_1.packet.ClientboundPacketsa1_1_0;
import net.raphimc.vialegacy.protocol.alpha.a1_2_3_5_1_2_6tob1_0_1_1_1.provider.AlphaInventoryProvider;
import net.raphimc.vialegacy.protocol.alpha.a1_2_3_5_1_2_6tob1_0_1_1_1.storage.AlphaInventoryTracker;
import net.raphimc.vialegacy.protocol.beta.b1_2_0_2tob1_3_0_1.storage.BlockDigStorage;
import net.raphimc.vialegacy.protocol.beta.b1_5_0_2tob1_6_0_6.Protocolb1_5_0_2Tob1_6_0_6;
import net.raphimc.vialegacy.protocol.beta.b1_7_0_3tob1_8_0_1.Protocolb1_7_0_3Tob1_8_0_1;
import net.raphimc.vialegacy.protocol.beta.b1_7_0_3tob1_8_0_1.packet.ClientboundPacketsb1_7;
import net.raphimc.vialegacy.protocol.beta.b1_7_0_3tob1_8_0_1.types.Typesb1_7_0_3;
import net.raphimc.vialegacy.protocol.beta.b1_8_0_1tor1_0_0_1.packet.ClientboundPacketsb1_8;
import net.raphimc.vialegacy.protocol.classic.c0_28_30toa1_0_15.data.ClassicBlocks;
import net.raphimc.vialegacy.protocol.classic.c0_28_30toa1_0_15.model.ClassicLevel;
import net.raphimc.vialegacy.protocol.classic.c0_28_30toa1_0_15.packet.ClientboundPacketsc0_28;
import net.raphimc.vialegacy.protocol.classic.c0_28_30toa1_0_15.packet.ServerboundPacketsc0_28;
import net.raphimc.vialegacy.protocol.classic.c0_28_30toa1_0_15.provider.ClassicCustomCommandProvider;
import net.raphimc.vialegacy.protocol.classic.c0_28_30toa1_0_15.provider.ClassicMPPassProvider;
import net.raphimc.vialegacy.protocol.classic.c0_28_30toa1_0_15.provider.ClassicWorldHeightProvider;
import net.raphimc.vialegacy.protocol.classic.c0_28_30toa1_0_15.storage.*;
import net.raphimc.vialegacy.protocol.classic.c0_28_30toa1_0_15.task.ClassicLevelStorageTickTask;
import net.raphimc.vialegacy.protocol.classic.c0_28_30toa1_0_15.types.Typesc0_30;
import net.raphimc.vialegacy.protocol.classic.c0_30cpetoc0_28_30.Protocolc0_30cpeToc0_28_30;
import net.raphimc.vialegacy.protocol.classic.c0_30cpetoc0_28_30.storage.ExtBlockPermissionsStorage;
import net.raphimc.vialegacy.protocol.release.r1_7_6_10tor1_8.Protocolr1_7_6_10Tor1_8;
import net.raphimc.vialegacy.protocol.release.r1_7_6_10tor1_8.types.Types1_7_6;

public class Protocolc0_28_30Toa1_0_15 extends StatelessProtocol<ClientboundPacketsc0_28, ClientboundPacketsa1_0_15, ServerboundPacketsc0_28, ServerboundPacketsa1_0_15> {

    public Protocolc0_28_30Toa1_0_15() {
        super(ClientboundPacketsc0_28.class, ClientboundPacketsa1_0_15.class, ServerboundPacketsc0_28.class, ServerboundPacketsa1_0_15.class);
    }

    @Override
    protected void registerPackets() {
        this.registerClientbound(ClientboundPacketsc0_28.LOGIN, new PacketHandlers() {
            @Override
            public void register() {
                read(Types.BYTE); // protocol id
                handler(wrapper -> {
                    final String title = wrapper.read(Typesc0_30.STRING).replace("&", "§"); // title
                    final String motd = wrapper.read(Typesc0_30.STRING).replace("&", "§"); // motd
                    final byte opLevel = wrapper.read(Types.BYTE); // op level
                    wrapper.user().put(new ClassicServerTitleStorage(wrapper.user(), title, motd));
                    wrapper.user().get(ClassicOpLevelStorage.class).setOpLevel(opLevel);

                    wrapper.write(Types.INT, wrapper.user().getProtocolInfo().getUsername().hashCode()); // entity id
                    wrapper.write(Typesb1_7_0_3.STRING, wrapper.user().getProtocolInfo().getUsername()); // username
                    wrapper.write(Typesb1_7_0_3.STRING, ""); // password

                    if (wrapper.user().has(ClassicLevelStorage.class)) { // World already loaded
                        wrapper.cancel();
                    }

                    if (wrapper.user().getProtocolInfo().getPipeline().contains(Protocolr1_7_6_10Tor1_8.class)) {
                        final PacketWrapper tabList = PacketWrapper.create(ClientboundPackets1_8.TAB_LIST, wrapper.user());
                        tabList.write(Types.STRING, Protocolr1_7_6_10Tor1_8.LEGACY_TO_JSON.transform(wrapper, "§6" + title + "\n")); // header
                        tabList.write(Types.STRING, Protocolr1_7_6_10Tor1_8.LEGACY_TO_JSON.transform(wrapper, "\n§b" + motd)); // footer
                        tabList.send(Protocolr1_7_6_10Tor1_8.class);
                    }

                    final ClassicProgressStorage classicProgressStorage = wrapper.user().get(ClassicProgressStorage.class);
                    classicProgressStorage.progress = 1;
                    classicProgressStorage.upperBound = 2;
                    classicProgressStorage.status = "Waiting for server...";
                });
            }
        });
        this.registerClientbound(ClientboundPacketsc0_28.LEVEL_INIT, null, wrapper -> {
            wrapper.cancel();

            if (wrapper.user().has(ClassicLevelStorage.class)) { // Switch world
                if (wrapper.user().getProtocolInfo().getPipeline().contains(Protocolb1_5_0_2Tob1_6_0_6.class)) {
                    final PacketWrapper fakeRespawn = PacketWrapper.create(ClientboundPacketsb1_7.RESPAWN, wrapper.user());
                    fakeRespawn.write(Types.BYTE, (byte) -1); // dimension id
                    fakeRespawn.send(Protocolb1_5_0_2Tob1_6_0_6.class);
                    final PacketWrapper respawn = PacketWrapper.create(ClientboundPacketsb1_7.RESPAWN, wrapper.user());
                    respawn.write(Types.BYTE, (byte) 0); // dimension id
                    respawn.send(Protocolb1_5_0_2Tob1_6_0_6.class);
                    wrapper.user().get(ClassicPositionTracker.class).spawned = false;
                }
            }
            if (wrapper.user().getProtocolInfo().getPipeline().contains(Protocolb1_7_0_3Tob1_8_0_1.class)) {
                final PacketWrapper gameEvent = PacketWrapper.create(ClientboundPacketsb1_8.GAME_EVENT, wrapper.user());
                gameEvent.write(Types.BYTE, (byte) 3); // reason (game mode)
                gameEvent.write(Types.BYTE, (byte) 1); // value (creative)
                gameEvent.send(Protocolb1_7_0_3Tob1_8_0_1.class);
            }
            wrapper.user().get(ClassicOpLevelStorage.class).updateAbilities();

            wrapper.user().put(new ClassicLevelStorage(wrapper.user()));

            final ClassicProgressStorage classicProgressStorage = wrapper.user().get(ClassicProgressStorage.class);
            classicProgressStorage.progress = 2;
            classicProgressStorage.upperBound = 2;
            classicProgressStorage.status = "Waiting for server...";
        });
        this.registerClientbound(ClientboundPacketsc0_28.LEVEL_DATA, null, wrapper -> {
            wrapper.cancel();
            final short partSize = wrapper.read(Types.SHORT); // part size
            final byte[] data = wrapper.read(Typesc0_30.BYTE_ARRAY); // data
            final byte progress = wrapper.read(Types.BYTE); // progress

            wrapper.user().get(ClassicLevelStorage.class).addDataPart(data, partSize);

            final ClassicProgressStorage classicProgressStorage = wrapper.user().get(ClassicProgressStorage.class);
            classicProgressStorage.upperBound = 100;
            classicProgressStorage.progress = progress;
            classicProgressStorage.status = "Receiving level... §7" + progress + "%";
        });
        this.registerClientbound(ClientboundPacketsc0_28.LEVEL_FINALIZE, null, wrapper -> {
            wrapper.cancel();
            final short sizeX = wrapper.read(Types.SHORT);
            final short sizeY = wrapper.read(Types.SHORT);
            final short sizeZ = wrapper.read(Types.SHORT);

            final ClassicProgressStorage classicProgressStorage = wrapper.user().get(ClassicProgressStorage.class);
            final ClassicLevelStorage levelStorage = wrapper.user().get(ClassicLevelStorage.class);
            final short maxChunkSectionCount = Via.getManager().getProviders().get(ClassicWorldHeightProvider.class).getMaxChunkSectionCount(wrapper.user());

            classicProgressStorage.upperBound = 2;
            classicProgressStorage.progress = 0;
            classicProgressStorage.status = "Finishing level... §7Decompressing";
            levelStorage.finish(sizeX, sizeY, sizeZ);
            levelStorage.sendChunk(new ChunkCoord(0, 0));

            if (wrapper.user().getProtocolInfo().getPipeline().contains(Protocolr1_7_6_10Tor1_8.class)) {
                final PacketWrapper worldBorder = PacketWrapper.create(ClientboundPackets1_8.SET_BORDER, wrapper.user());
                worldBorder.write(Types.VAR_INT, 3); // action (INITIALIZE)
                worldBorder.write(Types.DOUBLE, sizeX / 2D); // centerX
                worldBorder.write(Types.DOUBLE, sizeZ / 2D); // centerZ
                worldBorder.write(Types.DOUBLE, 0.0D); // diameter
                worldBorder.write(Types.DOUBLE, (double) Math.max(sizeX, sizeZ)); // target size
                worldBorder.write(Types.VAR_LONG, 0L); // time until target size
                worldBorder.write(Types.VAR_INT, Math.max(sizeX, sizeZ)); // size
                worldBorder.write(Types.VAR_INT, 0); // warning distance
                worldBorder.write(Types.VAR_INT, 0); // warning time
                worldBorder.send(Protocolr1_7_6_10Tor1_8.class);
            }

            sendChatMessage(wrapper.user(), "§aWorld dimensions: §6" + sizeX + "§ax§6" + sizeY + "§ax§6" + sizeZ);
            if (sizeY > (maxChunkSectionCount << 4)) {
                sendChatMessage(wrapper.user(), "§cThis server has a world higher than " + (maxChunkSectionCount << 4) + " blocks! Expect world errors");
            }

            classicProgressStorage.progress = 1;
            classicProgressStorage.status = "Finishing level... §7Waiting for server";
        });
        this.registerClientbound(ClientboundPacketsc0_28.BLOCK_UPDATE, new PacketHandlers() {
            @Override
            public void register() {
                map(Typesc0_30.BLOCK_POSITION, Types1_7_6.BLOCK_POSITION_UBYTE); // position
                handler(wrapper -> {
                    final ClassicLevelStorage levelStorage = wrapper.user().get(ClassicLevelStorage.class);
                    if (levelStorage == null || !levelStorage.hasReceivedLevel()) {
                        wrapper.cancel();
                        return;
                    }
                    final ClassicBlockRemapper remapper = wrapper.user().get(ClassicBlockRemapper.class);
                    final BlockPosition pos = wrapper.get(Types1_7_6.BLOCK_POSITION_UBYTE, 0);
                    final byte blockId = wrapper.read(Types.BYTE); // block id
                    levelStorage.getClassicLevel().setBlock(pos, blockId);
                    if (!levelStorage.isChunkLoaded(pos)) {
                        wrapper.cancel();
                        return;
                    }
                    final IdAndData mappedBlock = remapper.mapper().get(blockId);
                    wrapper.write(Types.UNSIGNED_BYTE, (short) mappedBlock.getId()); // block id
                    wrapper.write(Types.UNSIGNED_BYTE, (short) mappedBlock.getData()); // block data
                });
            }
        });
        this.registerClientbound(ClientboundPacketsc0_28.ADD_PLAYER, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.BYTE, Types.INT); // entity id
                map(Typesc0_30.STRING, Typesb1_7_0_3.STRING, n -> n.replace("&", "§")); // username
                map(Types.SHORT, Types.INT); // x
                map(Types.SHORT, Types.INT); // y
                map(Types.SHORT, Types.INT); // z
                map(Types.BYTE, Types.BYTE, yaw -> (byte) (yaw + 128)); // yaw
                map(Types.BYTE); // pitch
                create(Types.UNSIGNED_SHORT, 0); // item
                handler(wrapper -> {
                    if (wrapper.get(Types.INT, 0) < 0) { // client player
                        wrapper.cancel();
                        final int x = wrapper.get(Types.INT, 1);
                        final int y = wrapper.get(Types.INT, 2);
                        final int z = wrapper.get(Types.INT, 3);
                        final byte yaw = wrapper.get(Types.BYTE, 0);
                        final byte pitch = wrapper.get(Types.BYTE, 1);

                        final ClassicProgressStorage classicProgressStorage = wrapper.user().get(ClassicProgressStorage.class);
                        classicProgressStorage.progress = 2;
                        classicProgressStorage.status = "Finishing level... §7Loading spawn chunks";

                        final ClassicPositionTracker classicPositionTracker = wrapper.user().get(ClassicPositionTracker.class);
                        classicPositionTracker.posX = (x / 32.0F);
                        classicPositionTracker.stance = (y / 32.0F) + 0.714F;
                        classicPositionTracker.posZ = (z / 32.0F);
                        classicPositionTracker.yaw = yaw * 360 / 256.0F;
                        classicPositionTracker.pitch = pitch * 360 / 256.0F;
                        wrapper.user().get(ClassicLevelStorage.class).sendChunks(classicPositionTracker.getChunkPosition(), 1);

                        if (wrapper.user().getProtocolInfo().getPipeline().contains(Protocola1_0_17_1_0_17_4Toa1_1_0_1_1_2_1.class)) {
                            final PacketWrapper spawnPosition = PacketWrapper.create(ClientboundPacketsa1_1_0.SET_DEFAULT_SPAWN_POSITION, wrapper.user());
                            spawnPosition.write(Types1_7_6.BLOCK_POSITION_INT, new BlockPosition((int) classicPositionTracker.posX, (int) (classicPositionTracker.stance), (int) classicPositionTracker.posZ));
                            spawnPosition.send(Protocola1_0_17_1_0_17_4Toa1_1_0_1_1_2_1.class);
                        }

                        final PacketWrapper playerPosition = PacketWrapper.create(ClientboundPacketsa1_0_15.PLAYER_POSITION, wrapper.user());
                        playerPosition.write(Types.DOUBLE, classicPositionTracker.posX); // x
                        playerPosition.write(Types.DOUBLE, classicPositionTracker.stance); // stance
                        playerPosition.write(Types.DOUBLE, classicPositionTracker.stance - 1.62F); // y
                        playerPosition.write(Types.DOUBLE, classicPositionTracker.posZ); // z
                        playerPosition.write(Types.FLOAT, classicPositionTracker.yaw); // yaw
                        playerPosition.write(Types.FLOAT, classicPositionTracker.pitch); // pitch
                        playerPosition.write(Types.BOOLEAN, true); // onGround
                        playerPosition.send(Protocolc0_28_30Toa1_0_15.class);
                        classicPositionTracker.spawned = true;
                    } else {
                        wrapper.set(Types.INT, 2, wrapper.get(Types.INT, 2) - Float.valueOf(1.62F * 32).intValue());
                    }
                });
            }
        });
        this.registerClientbound(ClientboundPacketsc0_28.TELEPORT_ENTITY, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.BYTE, Types.INT); // entity id
                map(Types.SHORT, Types.INT); // x
                map(Types.SHORT, Types.INT); // y
                map(Types.SHORT, Types.INT); // z
                map(Types.BYTE, Types.BYTE, yaw -> (byte) (yaw + 128)); // yaw
                map(Types.BYTE); // pitch
                handler(wrapper -> {
                    if (wrapper.get(Types.INT, 0) < 0) { // client player
                        wrapper.set(Types.INT, 2, wrapper.get(Types.INT, 2) - 29);
                        wrapper.set(Types.INT, 0, wrapper.user().getProtocolInfo().getUsername().hashCode());
                    } else {
                        wrapper.set(Types.INT, 2, wrapper.get(Types.INT, 2) - Float.valueOf(1.62F * 32).intValue());
                    }
                });
            }
        });
        this.registerClientbound(ClientboundPacketsc0_28.MOVE_ENTITY_POS_ROT, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.BYTE, Types.INT); // entity id
                map(Types.BYTE); // x
                map(Types.BYTE); // y
                map(Types.BYTE); // z
                map(Types.BYTE, Types.BYTE, yaw -> (byte) (yaw + 128)); // yaw
                map(Types.BYTE); // pitch
            }
        });
        this.registerClientbound(ClientboundPacketsc0_28.MOVE_ENTITY_POS, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.BYTE, Types.INT); // entity id
                map(Types.BYTE); // x
                map(Types.BYTE); // y
                map(Types.BYTE); // z
            }
        });
        this.registerClientbound(ClientboundPacketsc0_28.MOVE_ENTITY_ROT, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.BYTE, Types.INT); // entity id
                map(Types.BYTE, Types.BYTE, yaw -> (byte) (yaw + 128)); // yaw
                map(Types.BYTE); // pitch
            }
        });
        this.registerClientbound(ClientboundPacketsc0_28.REMOVE_ENTITIES, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.BYTE, Types.INT); // entity id
            }
        });
        this.registerClientbound(ClientboundPacketsc0_28.CHAT, new PacketHandlers() {
            @Override
            public void register() {
                handler(packetWrapper -> {
                    final byte senderId = packetWrapper.read(Types.BYTE); // sender id
                    String message = packetWrapper.read(Typesc0_30.STRING).replace("&", "§"); // message
                    if (senderId < 0) {
                        message = "§e" + message;
                    }
                    packetWrapper.write(Typesb1_7_0_3.STRING, message);
                });
            }
        });
        this.registerClientbound(ClientboundPacketsc0_28.DISCONNECT, new PacketHandlers() {
            @Override
            public void register() {
                map(Typesc0_30.STRING, Typesb1_7_0_3.STRING, s -> s.replace("&", "§")); // reason
            }
        });
        this.registerClientbound(ClientboundPacketsc0_28.OP_LEVEL_UPDATE, null, wrapper -> {
            wrapper.cancel();
            final ClassicOpLevelStorage opLevelStorage = wrapper.user().get(ClassicOpLevelStorage.class);
            final byte opLevel = wrapper.read(Types.BYTE); // op level
            opLevelStorage.setOpLevel(opLevel);
        });

        this.registerServerbound(ServerboundPacketsa1_0_15.LOGIN, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT, Types.BYTE); // protocol id
                map(Typesb1_7_0_3.STRING, Typesc0_30.STRING); // username
                read(Typesb1_7_0_3.STRING); // password
                handler(wrapper -> {
                    wrapper.write(Typesc0_30.STRING, Via.getManager().getProviders().get(ClassicMPPassProvider.class).getMpPass(wrapper.user())); // mp pass
                    wrapper.write(Types.BYTE, (byte) 0); // op level

                    final ClassicProgressStorage classicProgressStorage = wrapper.user().get(ClassicProgressStorage.class);
                    classicProgressStorage.upperBound = 2;
                    classicProgressStorage.status = "Logging in...";
                });
            }
        });
        this.registerServerbound(ServerboundPacketsa1_0_15.CHAT, wrapper -> {
            final String message = wrapper.read(Typesb1_7_0_3.STRING); // message
            wrapper.write(Types.BYTE, (byte) -1); // sender id
            wrapper.write(Typesc0_30.STRING, message); // message
            if (Via.getManager().getProviders().get(ClassicCustomCommandProvider.class).handleChatMessage(wrapper.user(), message)) {
                wrapper.cancel();
            }
        });
        this.registerServerbound(ServerboundPacketsa1_0_15.MOVE_PLAYER_STATUS_ONLY, ServerboundPacketsc0_28.MOVE_PLAYER_POS_ROT, new PacketHandlers() {
            @Override
            public void register() {
                read(Types.BOOLEAN); // onGround
                handler(wrapper -> wrapper.user().get(ClassicPositionTracker.class).writeToPacket(wrapper));
            }
        });
        this.registerServerbound(ServerboundPacketsa1_0_15.MOVE_PLAYER_POS, ServerboundPacketsc0_28.MOVE_PLAYER_POS_ROT, wrapper -> {
            final ClassicPositionTracker positionTracker = wrapper.user().get(ClassicPositionTracker.class);
            positionTracker.posX = wrapper.read(Types.DOUBLE); // x
            wrapper.read(Types.DOUBLE); // y
            positionTracker.stance = wrapper.read(Types.DOUBLE); // stance
            positionTracker.posZ = wrapper.read(Types.DOUBLE); // z
            wrapper.read(Types.BOOLEAN); // onGround

            positionTracker.writeToPacket(wrapper);
        });
        this.registerServerbound(ServerboundPacketsa1_0_15.MOVE_PLAYER_ROT, ServerboundPacketsc0_28.MOVE_PLAYER_POS_ROT, wrapper -> {
            final ClassicPositionTracker positionTracker = wrapper.user().get(ClassicPositionTracker.class);
            positionTracker.yaw = wrapper.read(Types.FLOAT); // yaw
            positionTracker.pitch = wrapper.read(Types.FLOAT); // pitch
            wrapper.read(Types.BOOLEAN); // onGround

            positionTracker.writeToPacket(wrapper);
        });
        this.registerServerbound(ServerboundPacketsa1_0_15.MOVE_PLAYER_POS_ROT, wrapper -> {
            final ClassicPositionTracker positionTracker = wrapper.user().get(ClassicPositionTracker.class);
            positionTracker.posX = wrapper.read(Types.DOUBLE); // x
            wrapper.read(Types.DOUBLE); // y
            positionTracker.stance = wrapper.read(Types.DOUBLE); // stance
            positionTracker.posZ = wrapper.read(Types.DOUBLE); // z
            positionTracker.yaw = wrapper.read(Types.FLOAT); // yaw
            positionTracker.pitch = wrapper.read(Types.FLOAT); // pitch
            wrapper.read(Types.BOOLEAN); // onGround

            positionTracker.writeToPacket(wrapper);
        });
        this.registerServerbound(ServerboundPacketsa1_0_15.PLAYER_ACTION, ServerboundPacketsc0_28.USE_ITEM_ON, wrapper -> {
            wrapper.user().getStoredObjects().remove(BlockDigStorage.class);
            final ClassicLevel level = wrapper.user().get(ClassicLevelStorage.class).getClassicLevel();
            final ClassicOpLevelStorage opTracker = wrapper.user().get(ClassicOpLevelStorage.class);
            final boolean extendedVerification = wrapper.user().has(ExtBlockPermissionsStorage.class);

            final short status = wrapper.read(Types.UNSIGNED_BYTE); // status
            final BlockPosition pos = wrapper.read(Types1_7_6.BLOCK_POSITION_UBYTE); // position
            wrapper.read(Types.UNSIGNED_BYTE); // direction
            final int blockId = level.getBlock(pos);

            final boolean hasCreative = wrapper.user().getProtocolInfo().getPipeline().contains(Protocolb1_7_0_3Tob1_8_0_1.class);
            if ((status == 0 && hasCreative) || (status == 2 && !hasCreative)) {
                if (!extendedVerification && blockId == ClassicBlocks.BEDROCK && opTracker.getOpLevel() < 100) {
                    wrapper.cancel();
                    sendChatMessage(wrapper.user(), "§cOnly op players can break bedrock!");
                    sendBlockChange(wrapper.user(), pos, new IdAndData(BlockList1_6.bedrock.blockId(), 0));
                    return;
                }
                if (!extendedVerification) {
                    level.setBlock(pos, ClassicBlocks.AIR);
                    sendBlockChange(wrapper.user(), pos, new IdAndData(0, 0));
                }

                wrapper.write(Typesc0_30.BLOCK_POSITION, pos); // position
                wrapper.write(Types.BOOLEAN, false); // place block
                wrapper.write(Types.BYTE, (byte) ClassicBlocks.STONE); // block id
            } else {
                wrapper.cancel();
            }
        });
        this.registerServerbound(ServerboundPacketsa1_0_15.USE_ITEM_ON, wrapper -> {
            final ClassicLevel level = wrapper.user().get(ClassicLevelStorage.class).getClassicLevel();
            final ClassicBlockRemapper remapper = wrapper.user().get(ClassicBlockRemapper.class);
            final boolean extendedVerification = wrapper.user().has(ExtBlockPermissionsStorage.class);

            wrapper.read(Types.SHORT); // item id (is useless because it has no item damage)
            BlockPosition pos = wrapper.read(Types1_7_6.BLOCK_POSITION_UBYTE); // position
            final short direction = wrapper.read(Types.UNSIGNED_BYTE); // direction

            final Item item = Via.getManager().getProviders().get(AlphaInventoryProvider.class).getHandItem(wrapper.user());
            if (item == null || direction == 255) {
                wrapper.cancel();
                return;
            }
            pos = pos.getRelative(BlockFaceUtil.getFace(direction));

            if (pos.y() >= level.getSizeY()) {
                wrapper.cancel();
                sendChatMessage(wrapper.user(), "§cHeight limit for building is " + level.getSizeY() + " blocks");
                sendBlockChange(wrapper.user(), pos, new IdAndData(0, 0));
                return;
            }

            final byte classicBlock = (byte) remapper.reverseMapper().getInt(new IdAndData(item.identifier(), item.data() & 15));

            if (!extendedVerification) {
                level.setBlock(pos, classicBlock);
                sendBlockChange(wrapper.user(), pos, remapper.mapper().get(classicBlock));
            }

            wrapper.write(Typesc0_30.BLOCK_POSITION, pos); // position
            wrapper.write(Types.BOOLEAN, true); // place block
            wrapper.write(Types.BYTE, classicBlock); // block id
        });
        this.cancelServerbound(ServerboundPacketsa1_0_15.KEEP_ALIVE);
        this.cancelServerbound(ServerboundPacketsa1_0_15.SET_CARRIED_ITEM);
        this.cancelServerbound(ServerboundPacketsa1_0_15.SWING);
        this.cancelServerbound(ServerboundPacketsa1_0_15.SPAWN_ITEM);
        this.cancelServerbound(ServerboundPacketsa1_0_15.DISCONNECT);
    }

    private void sendChatMessage(final UserConnection user, final String msg) {
        final PacketWrapper message = PacketWrapper.create(ClientboundPacketsa1_0_15.CHAT, user);
        message.write(Typesb1_7_0_3.STRING, msg); // message
        message.send(Protocolc0_28_30Toa1_0_15.class);
    }

    private void sendBlockChange(final UserConnection user, final BlockPosition pos, final IdAndData block) {
        final PacketWrapper blockChange = PacketWrapper.create(ClientboundPacketsa1_0_15.BLOCK_UPDATE, user);
        blockChange.write(Types1_7_6.BLOCK_POSITION_UBYTE, pos); // position
        blockChange.write(Types.UNSIGNED_BYTE, (short) block.getId()); // block id
        blockChange.write(Types.UNSIGNED_BYTE, (short) block.getData()); // block data
        blockChange.send(Protocolc0_28_30Toa1_0_15.class);
    }

    @Override
    public void register(ViaProviders providers) {
        providers.register(ClassicWorldHeightProvider.class, new ClassicWorldHeightProvider());
        providers.register(ClassicMPPassProvider.class, new ClassicMPPassProvider());
        providers.register(ClassicCustomCommandProvider.class, new ClassicCustomCommandProvider());

        Via.getPlatform().runRepeatingSync(new ClassicLevelStorageTickTask(), 2L);
    }

    @Override
    public void init(UserConnection userConnection) {
        userConnection.put(new PreNettySplitter(Protocolc0_28_30Toa1_0_15.class, ClientboundPacketsc0_28::getPacket));

        userConnection.put(new ClassicPositionTracker());
        userConnection.put(new ClassicOpLevelStorage(userConnection, ViaLegacy.getConfig().enableClassicFly()));
        userConnection.put(new ClassicProgressStorage());
        userConnection.put(new ClassicBlockRemapper(i -> ClassicBlocks.MAPPING.get(i), o -> {
            int block = ClassicBlocks.REVERSE_MAPPING.getInt(o);

            if (!userConnection.getProtocolInfo().getPipeline().contains(Protocolc0_30cpeToc0_28_30.class)) {
                if (block == ClassicBlocks.GRASS) block = ClassicBlocks.DIRT;
                else if (block == ClassicBlocks.BEDROCK) block = ClassicBlocks.STONE;
                else if (block == ClassicBlocks.STATIONARY_WATER) block = ClassicBlocks.BLUE_WOOL;
                else if (block == ClassicBlocks.STATIONARY_LAVA) block = ClassicBlocks.ORANGE_WOOL;
            }

            return block;
        }));

        if (userConnection.has(AlphaInventoryTracker.class)) userConnection.get(AlphaInventoryTracker.class).setCreativeMode(true);
        if (userConnection.has(TimeLockStorage.class)) userConnection.get(TimeLockStorage.class).setTime(6000L);
    }

}
