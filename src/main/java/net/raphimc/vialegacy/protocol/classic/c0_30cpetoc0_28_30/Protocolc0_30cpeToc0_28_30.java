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
package net.raphimc.vialegacy.protocol.classic.c0_30cpetoc0_28_30;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.BlockChangeRecord;
import com.viaversion.viaversion.api.minecraft.BlockChangeRecord1_8;
import com.viaversion.viaversion.api.minecraft.BlockPosition;
import com.viaversion.viaversion.api.platform.providers.ViaProviders;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.api.type.Types;
import com.viaversion.viaversion.api.type.types.FixedByteArrayType;
import com.viaversion.viaversion.util.IdAndData;
import net.raphimc.vialegacy.ViaLegacy;
import net.raphimc.vialegacy.api.data.BlockList1_6;
import net.raphimc.vialegacy.api.model.ChunkCoord;
import net.raphimc.vialegacy.api.protocol.StatelessProtocol;
import net.raphimc.vialegacy.api.splitter.PreNettySplitter;
import net.raphimc.vialegacy.protocol.alpha.a1_0_15toa1_0_16_2.Protocola1_0_15Toa1_0_16_2;
import net.raphimc.vialegacy.protocol.alpha.a1_0_15toa1_0_16_2.packet.ClientboundPacketsa1_0_15;
import net.raphimc.vialegacy.protocol.classic.c0_28_30toa1_0_15.data.ClassicBlocks;
import net.raphimc.vialegacy.protocol.classic.c0_28_30toa1_0_15.model.ClassicLevel;
import net.raphimc.vialegacy.protocol.classic.c0_28_30toa1_0_15.packet.ClientboundPacketsc0_28;
import net.raphimc.vialegacy.protocol.classic.c0_28_30toa1_0_15.packet.ServerboundPacketsc0_28;
import net.raphimc.vialegacy.protocol.classic.c0_28_30toa1_0_15.storage.ClassicBlockRemapper;
import net.raphimc.vialegacy.protocol.classic.c0_28_30toa1_0_15.storage.ClassicLevelStorage;
import net.raphimc.vialegacy.protocol.classic.c0_28_30toa1_0_15.storage.ClassicOpLevelStorage;
import net.raphimc.vialegacy.protocol.classic.c0_28_30toa1_0_15.storage.ClassicProgressStorage;
import net.raphimc.vialegacy.protocol.classic.c0_28_30toa1_0_15.types.Typesc0_30;
import net.raphimc.vialegacy.protocol.classic.c0_30cpetoc0_28_30.data.ClassicProtocolExtension;
import net.raphimc.vialegacy.protocol.classic.c0_30cpetoc0_28_30.data.ExtendedClassicBlocks;
import net.raphimc.vialegacy.protocol.classic.c0_30cpetoc0_28_30.packet.ClientboundPacketsc0_30cpe;
import net.raphimc.vialegacy.protocol.classic.c0_30cpetoc0_28_30.packet.ServerboundPacketsc0_30cpe;
import net.raphimc.vialegacy.protocol.classic.c0_30cpetoc0_28_30.storage.ExtBlockPermissionsStorage;
import net.raphimc.vialegacy.protocol.classic.c0_30cpetoc0_28_30.storage.ExtensionProtocolMetadataStorage;
import net.raphimc.vialegacy.protocol.classic.c0_30cpetoc0_28_30.task.ClassicPingTask;
import net.raphimc.vialegacy.protocol.release.r1_1tor1_2_1_3.types.Types1_1;
import net.raphimc.vialegacy.protocol.release.r1_6_1tor1_6_2.Protocolr1_6_1Tor1_6_2;
import net.raphimc.vialegacy.protocol.release.r1_6_4tor1_7_2_5.packet.ClientboundPackets1_6_4;
import net.raphimc.vialegacy.protocol.release.r1_6_4tor1_7_2_5.types.Types1_6_4;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Protocolc0_30cpeToc0_28_30 extends StatelessProtocol<ClientboundPacketsc0_30cpe, ClientboundPacketsc0_28, ServerboundPacketsc0_30cpe, ServerboundPacketsc0_28> {

    public Protocolc0_30cpeToc0_28_30() {
        super(ClientboundPacketsc0_30cpe.class, ClientboundPacketsc0_28.class, ServerboundPacketsc0_30cpe.class, ServerboundPacketsc0_28.class);
    }

    @Override
    protected void registerPackets() {
        this.registerClientbound(ClientboundPacketsc0_30cpe.LOGIN, wrapper -> {
            if (wrapper.user().getProtocolInfo().getPipeline().contains(Protocolr1_6_1Tor1_6_2.class)) {
                final ExtensionProtocolMetadataStorage protocolMetadataStorage = wrapper.user().get(ExtensionProtocolMetadataStorage.class);
                final PacketWrapper brand = PacketWrapper.create(ClientboundPackets1_6_4.CUSTOM_PAYLOAD, wrapper.user());
                brand.write(Types1_6_4.STRING, "MC|Brand");
                final byte[] brandBytes = protocolMetadataStorage.getServerSoftwareName().getBytes(StandardCharsets.UTF_8);
                brand.write(Types.SHORT, (short) brandBytes.length); // length
                brand.write(Types.REMAINING_BYTES, brandBytes); // data

                wrapper.send(Protocolc0_30cpeToc0_28_30.class);
                brand.send(Protocolr1_6_1Tor1_6_2.class);
                wrapper.cancel();
            }
        });
        this.registerClientbound(ClientboundPacketsc0_30cpe.EXTENSION_PROTOCOL_INFO, null, wrapper -> {
            wrapper.cancel();
            final ExtensionProtocolMetadataStorage protocolMetadataStorage = wrapper.user().get(ExtensionProtocolMetadataStorage.class);
            protocolMetadataStorage.setServerSoftwareName(wrapper.read(Typesc0_30.STRING)); // app name
            protocolMetadataStorage.setExtensionCount(wrapper.read(Types.SHORT)); // extension count

            final ClassicProgressStorage classicProgressStorage = wrapper.user().get(ClassicProgressStorage.class);
            classicProgressStorage.progress = 0;
            classicProgressStorage.upperBound = protocolMetadataStorage.getExtensionCount();
            classicProgressStorage.status = "Receiving extension list...";
        });
        this.registerClientbound(ClientboundPacketsc0_30cpe.EXTENSION_PROTOCOL_ENTRY, null, wrapper -> {
            wrapper.cancel();
            final ExtensionProtocolMetadataStorage protocolMetadataStorage = wrapper.user().get(ExtensionProtocolMetadataStorage.class);
            final String extensionName = wrapper.read(Typesc0_30.STRING); // name
            final int extensionVersion = wrapper.read(Types.INT); // version

            final ClassicProtocolExtension extension = ClassicProtocolExtension.byName(extensionName);
            if (extension != null) {
                protocolMetadataStorage.addServerExtension(extension, extensionVersion);
            } else {
                if (!Via.getConfig().isSuppressConversionWarnings() || Via.getManager().isDebug()) {
                    ViaLegacy.getPlatform().getLogger().warning("Received unknown classic protocol extension: (" + extensionName + " v" + extensionVersion + ")");
                }
            }

            protocolMetadataStorage.incrementReceivedExtensions();

            final ClassicProgressStorage classicProgressStorage = wrapper.user().get(ClassicProgressStorage.class);
            classicProgressStorage.progress = protocolMetadataStorage.getReceivedExtensions();

            if (protocolMetadataStorage.getReceivedExtensions() >= protocolMetadataStorage.getExtensionCount()) {
                classicProgressStorage.status = "Sending extension list...";
                final List<ClassicProtocolExtension> supportedExtensions = new ArrayList<>();
                for (ClassicProtocolExtension protocolExtension : ClassicProtocolExtension.values()) {
                    if (protocolExtension.isSupported()) {
                        supportedExtensions.add(protocolExtension);
                    }
                }

                if (supportedExtensions.contains(ClassicProtocolExtension.BLOCK_PERMISSIONS)) {
                    wrapper.user().put(new ExtBlockPermissionsStorage());
                }

                final PacketWrapper extensionProtocolInfo = PacketWrapper.create(ServerboundPacketsc0_30cpe.EXTENSION_PROTOCOL_INFO, wrapper.user());
                extensionProtocolInfo.write(Typesc0_30.STRING, ViaLegacy.getPlatform().getCpeAppName()); // app name
                extensionProtocolInfo.write(Types.SHORT, (short) supportedExtensions.size()); // extension count
                extensionProtocolInfo.sendToServer(Protocolc0_30cpeToc0_28_30.class);

                for (ClassicProtocolExtension protocolExtension : supportedExtensions) {
                    final PacketWrapper extensionProtocolEntry = PacketWrapper.create(ServerboundPacketsc0_30cpe.EXTENSION_PROTOCOL_ENTRY, wrapper.user());
                    extensionProtocolEntry.write(Typesc0_30.STRING, protocolExtension.getName()); // name
                    extensionProtocolEntry.write(Types.INT, protocolExtension.getHighestSupportedVersion()); // version
                    extensionProtocolEntry.sendToServer(Protocolc0_30cpeToc0_28_30.class);
                }
            }
        });
        this.registerClientbound(ClientboundPacketsc0_30cpe.EXT_CUSTOM_BLOCKS_SUPPORT_LEVEL, null, wrapper -> {
            wrapper.cancel();
            final byte level = wrapper.read(Types.BYTE); // support level
            if (level != 1) {
                ViaLegacy.getPlatform().getLogger().info("Classic server supports CustomBlocks level " + level);
            }
            final PacketWrapper response = PacketWrapper.create(ServerboundPacketsc0_30cpe.EXT_CUSTOM_BLOCKS_SUPPORT_LEVEL, wrapper.user());
            response.write(Types.BYTE, (byte) 1); // support level
            response.sendToServer(Protocolc0_30cpeToc0_28_30.class);
        });
        this.registerClientbound(ClientboundPacketsc0_30cpe.EXT_HACK_CONTROL, null, wrapper -> {
            wrapper.cancel();
            final ClassicOpLevelStorage opLevelStorage = wrapper.user().get(ClassicOpLevelStorage.class);
            final boolean flying = wrapper.read(Types.BOOLEAN); // flying
            final boolean noClip = wrapper.read(Types.BOOLEAN); // no clip
            final boolean speed = wrapper.read(Types.BOOLEAN); // speed
            final boolean respawn = wrapper.read(Types.BOOLEAN); // respawn key
            wrapper.read(Types.BOOLEAN); // third person view
            wrapper.read(Types.SHORT); // jump height

            opLevelStorage.updateHax(flying, noClip, speed, respawn);
        });
        this.registerClientbound(ClientboundPacketsc0_30cpe.EXT_SET_BLOCK_PERMISSION, null, wrapper -> {
            wrapper.cancel();
            final ExtBlockPermissionsStorage blockPermissionsStorage = wrapper.user().get(ExtBlockPermissionsStorage.class);
            final byte blockId = wrapper.read(Types.BYTE); // block id
            final boolean canPlace = wrapper.read(Types.BOOLEAN); // can place
            final boolean canDelete = wrapper.read(Types.BOOLEAN); // can delete

            if (canPlace) {
                blockPermissionsStorage.addPlaceable(blockId);
            } else {
                blockPermissionsStorage.removePlaceable(blockId);
            }
            if (canDelete) {
                blockPermissionsStorage.addBreakable(blockId);
            } else {
                blockPermissionsStorage.removeBreakable(blockId);
            }
        });
        this.registerClientbound(ClientboundPacketsc0_30cpe.EXT_BULK_BLOCK_UPDATE, null, wrapper -> {
            wrapper.cancel();
            final ClassicLevelStorage levelStorage = wrapper.user().get(ClassicLevelStorage.class);
            if (levelStorage == null || !levelStorage.hasReceivedLevel()) {
                return;
            }
            final ClassicBlockRemapper remapper = wrapper.user().get(ClassicBlockRemapper.class);
            final ClassicLevel level = levelStorage.getClassicLevel();

            final int count = wrapper.read(Types.UNSIGNED_BYTE) + 1; // count
            final byte[] indices = wrapper.read(new FixedByteArrayType(1024)); // indices
            final byte[] blocks = wrapper.read(new FixedByteArrayType(256)); // blocks

            if (wrapper.user().getProtocolInfo().getPipeline().contains(Protocola1_0_15Toa1_0_16_2.class)) {
                final Map<ChunkCoord, List<BlockChangeRecord>> records = new HashMap<>();
                for (int i = 0; i < count; i++) {
                    final int index = (indices[i * 4] & 255) << 24 | (indices[i * 4 + 1] & 255) << 16 | (indices[i * 4 + 2] & 255) << 8 | (indices[i * 4 + 3] & 255);
                    final BlockPosition pos = new BlockPosition(index % level.getSizeX(), (index / level.getSizeX()) / level.getSizeZ(), (index / level.getSizeX()) % level.getSizeZ());
                    final byte blockId = blocks[i];
                    level.setBlock(pos, blockId);
                    if (!levelStorage.isChunkLoaded(pos)) continue;
                    final IdAndData mappedBlock = remapper.mapper().get(blockId);
                    records.computeIfAbsent(new ChunkCoord(pos.x() >> 4, pos.z() >> 4), k -> new ArrayList<>()).add(new BlockChangeRecord1_8(pos.x() & 15, pos.y(), pos.z() & 15, mappedBlock.toRawData()));
                }

                for (Map.Entry<ChunkCoord, List<BlockChangeRecord>> entry : records.entrySet()) {
                    final PacketWrapper multiBlockChange = PacketWrapper.create(ClientboundPacketsa1_0_15.CHUNK_BLOCKS_UPDATE, wrapper.user());
                    multiBlockChange.write(Types.INT, entry.getKey().chunkX); // chunkX
                    multiBlockChange.write(Types.INT, entry.getKey().chunkZ); // chunkZ
                    multiBlockChange.write(Types1_1.BLOCK_CHANGE_RECORD_ARRAY, entry.getValue().toArray(new BlockChangeRecord[0])); // blockChangeRecords
                    multiBlockChange.send(Protocola1_0_15Toa1_0_16_2.class);
                }
            }
        });
        this.registerClientbound(ClientboundPacketsc0_30cpe.EXT_TWO_WAY_PING, ClientboundPacketsc0_28.KEEP_ALIVE, wrapper -> {
            final byte direction = wrapper.read(Types.BYTE); // direction
            final short data = wrapper.read(Types.SHORT); // data

            if (direction == 1) {
                final PacketWrapper pingResponse = PacketWrapper.create(ServerboundPacketsc0_30cpe.EXT_TWO_WAY_PING, wrapper.user());
                pingResponse.write(Types.BYTE, direction); // direction
                pingResponse.write(Types.SHORT, data); // data
                pingResponse.sendToServer(Protocolc0_30cpeToc0_28_30.class);
            }
        });

        this.registerServerbound(ServerboundPacketsc0_28.LOGIN, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.BYTE); // protocol id
                map(Typesc0_30.STRING); // username
                map(Typesc0_30.STRING); // mp pass
                map(Types.BYTE); // op level
                handler(wrapper -> {
                    wrapper.set(Types.BYTE, 1, (byte) 0x42); // extension protocol magic number
                });
            }
        });
        this.registerServerbound(ServerboundPacketsc0_28.CHAT, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.BYTE); // sender id
                map(Typesc0_30.STRING); // message
                handler(wrapper -> {
                    final ExtensionProtocolMetadataStorage protocolMetadata = wrapper.user().get(ExtensionProtocolMetadataStorage.class);
                    if (!protocolMetadata.hasServerExtension(ClassicProtocolExtension.LONGER_MESSAGES, 1)) return;
                    wrapper.cancel();

                    String message = wrapper.get(Typesc0_30.STRING, 0);
                    while (!message.isEmpty()) {
                        final int pos = Math.min(message.length(), 64);
                        final String msg = message.substring(0, pos);
                        message = message.substring(pos);
                        final PacketWrapper chatMessage = PacketWrapper.create(ServerboundPacketsc0_30cpe.CHAT, wrapper.user());
                        chatMessage.write(Types.BYTE, (byte) (!message.isEmpty() ? 1 : 0)); // 1 = more parts | 0 = last part
                        chatMessage.write(Typesc0_30.STRING, msg); // message
                        chatMessage.sendToServer(Protocolc0_30cpeToc0_28_30.class);
                    }
                });
            }
        });
        this.registerServerbound(ServerboundPacketsc0_28.USE_ITEM_ON, new PacketHandlers() {
            @Override
            public void register() {
                map(Typesc0_30.BLOCK_POSITION); // position
                map(Types.BOOLEAN); // place block
                map(Types.BYTE); // block id
                handler(wrapper -> {
                    if (!wrapper.user().has(ExtBlockPermissionsStorage.class)) return;

                    final ExtBlockPermissionsStorage blockPermissions = wrapper.user().get(ExtBlockPermissionsStorage.class);
                    final ClassicLevel level = wrapper.user().get(ClassicLevelStorage.class).getClassicLevel();

                    final BlockPosition position = wrapper.get(Typesc0_30.BLOCK_POSITION, 0);
                    final boolean placeBlock = wrapper.get(Types.BOOLEAN, 0);
                    final int blockId = wrapper.get(Types.BYTE, 0);

                    int block = level.getBlock(position);
                    final boolean disallow = (placeBlock && blockPermissions.isPlacingDenied(blockId)) || (!placeBlock && blockPermissions.isBreakingDenied(block));

                    if (disallow) {
                        wrapper.cancel();
                        final PacketWrapper chatMessage = PacketWrapper.create(ClientboundPacketsc0_30cpe.CHAT, wrapper.user());
                        chatMessage.write(Types.BYTE, (byte) 0); // sender id
                        chatMessage.write(Typesc0_30.STRING, "&cYou are not allowed to place/break this block"); // message
                        chatMessage.send(Protocolc0_30cpeToc0_28_30.class);
                    } else {
                        block = placeBlock ? blockId : ClassicBlocks.AIR;
                        level.setBlock(position, block);
                    }

                    final PacketWrapper blockChange = PacketWrapper.create(ClientboundPacketsc0_30cpe.BLOCK_UPDATE, wrapper.user());
                    blockChange.write(Typesc0_30.BLOCK_POSITION, position); // position
                    blockChange.write(Types.BYTE, (byte) block); // block id
                    blockChange.send(Protocolc0_30cpeToc0_28_30.class);
                });
            }
        });
    }

    @Override
    public void register(ViaProviders providers) {
        Via.getPlatform().runRepeatingSync(new ClassicPingTask(), 20L);
    }

    @Override
    public void init(UserConnection userConnection) {
        userConnection.put(new PreNettySplitter(Protocolc0_30cpeToc0_28_30.class, ClientboundPacketsc0_30cpe::getPacket));

        userConnection.put(new ExtensionProtocolMetadataStorage());
        userConnection.put(new ClassicOpLevelStorage(userConnection, true));

        final ClassicBlockRemapper previousRemapper = userConnection.get(ClassicBlockRemapper.class);
        userConnection.put(new ClassicBlockRemapper(i -> {
            if (ClassicBlocks.MAPPING.containsKey(i)) return previousRemapper.mapper().get(i);
            final ExtensionProtocolMetadataStorage extensionProtocol = userConnection.get(ExtensionProtocolMetadataStorage.class);
            if (extensionProtocol.hasServerExtension(ClassicProtocolExtension.CUSTOM_BLOCKS, 1)) {
                return ExtendedClassicBlocks.MAPPING.get(i);
            }
            return new IdAndData(BlockList1_6.stone.blockId(), 0);
        }, o -> {
            if (ClassicBlocks.REVERSE_MAPPING.containsKey(o)) return previousRemapper.reverseMapper().getInt(o);
            final ExtensionProtocolMetadataStorage extensionProtocol = userConnection.get(ExtensionProtocolMetadataStorage.class);
            if (extensionProtocol.hasServerExtension(ClassicProtocolExtension.CUSTOM_BLOCKS, 1)) {
                return ExtendedClassicBlocks.REVERSE_MAPPING.getInt(o);
            }
            return ClassicBlocks.STONE;
        }));
    }

}
