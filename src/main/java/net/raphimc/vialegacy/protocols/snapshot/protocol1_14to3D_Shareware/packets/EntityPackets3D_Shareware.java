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
package net.raphimc.vialegacy.protocols.snapshot.protocol1_14to3D_Shareware.packets;

import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import com.viaversion.viaversion.api.minecraft.metadata.types.MetaType1_14;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.type.types.version.Types1_14;
import net.raphimc.vialegacy.protocols.snapshot.protocol1_14to3D_Shareware.ClientboundPackets3D_Shareware;
import net.raphimc.vialegacy.protocols.snapshot.protocol1_14to3D_Shareware.Protocol1_14to3D_Shareware;

import java.util.List;

public class EntityPackets3D_Shareware {

    private final Protocol1_14to3D_Shareware protocol;

    public EntityPackets3D_Shareware(final Protocol1_14to3D_Shareware protocol) {
        this.protocol = protocol;
    }

    public void registerPackets() {
        this.protocol.registerClientbound(ClientboundPackets3D_Shareware.SPAWN_MOB, new PacketRemapper() {
            @Override
            public void registerMap() {
                map(Type.VAR_INT); // 0 - Entity ID
                map(Type.UUID); // 1 - Entity UUID
                map(Type.VAR_INT); // 2 - Entity Type
                map(Type.DOUBLE); // 3 - X
                map(Type.DOUBLE); // 4 - Y
                map(Type.DOUBLE); // 5 - Z
                map(Type.BYTE); // 6 - Yaw
                map(Type.BYTE); // 7 - Pitch
                map(Type.BYTE); // 8 - Head Pitch
                map(Type.SHORT); // 9 - Velocity X
                map(Type.SHORT); // 10 - Velocity Y
                map(Type.SHORT); // 11 - Velocity Z
                map(Types1_14.METADATA_LIST); // 12 - Metadata
                handler(packetWrapper -> handleMetadata(packetWrapper.get(Types1_14.METADATA_LIST, 0)));
            }
        });
        this.protocol.registerClientbound(ClientboundPackets3D_Shareware.SPAWN_PLAYER, new PacketRemapper() {
            @Override
            public void registerMap() {
                map(Type.VAR_INT); // 0 - Entity ID
                map(Type.UUID); // 1 - Player UUID
                map(Type.DOUBLE); // 2 - X
                map(Type.DOUBLE); // 3 - Y
                map(Type.DOUBLE); // 4 - Z
                map(Type.BYTE); // 5 - Yaw
                map(Type.BYTE); // 6 - Pitch
                map(Types1_14.METADATA_LIST); // 7 - Metadata
                handler(packetWrapper -> handleMetadata(packetWrapper.get(Types1_14.METADATA_LIST, 0)));
            }
        });
        this.protocol.registerClientbound(ClientboundPackets3D_Shareware.ENTITY_METADATA, new PacketRemapper() {
            @Override
            public void registerMap() {
                map(Type.VAR_INT); // 0 - Entity ID
                map(Types1_14.METADATA_LIST);
                handler(packetWrapper -> handleMetadata(packetWrapper.get(Types1_14.METADATA_LIST, 0)));
            }
        });
    }

    public void handleMetadata(final List<Metadata> metadataList) {
        for (Metadata metadata : metadataList) {
            if (metadata.metaType() == MetaType1_14.Slot) {
                metadata.setValue(this.protocol.getItemRewriter().handleItemToClient(metadata.value()));
            }
        }
    }

}
