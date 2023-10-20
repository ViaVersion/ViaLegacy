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
package net.raphimc.vialegacy.protocols.classic.protocolc0_28_30toc0_0_20a_27;

import com.viaversion.viaversion.api.connection.UserConnection;
import net.raphimc.vialegacy.api.protocol.StatelessProtocol;
import net.raphimc.vialegacy.api.splitter.PreNettySplitter;
import net.raphimc.vialegacy.protocols.classic.protocola1_0_15toc0_28_30.ClientboundPacketsc0_28;
import net.raphimc.vialegacy.protocols.classic.protocola1_0_15toc0_28_30.ServerboundPacketsc0_28;
import net.raphimc.vialegacy.protocols.classic.protocola1_0_15toc0_28_30.data.ClassicBlocks;
import net.raphimc.vialegacy.protocols.classic.protocola1_0_15toc0_28_30.storage.ClassicBlockRemapper;
import net.raphimc.vialegacy.protocols.classic.protocolc0_0_20a_27toc0_0_19a_06.Protocolc0_27toc0_0_19a_06;

public class Protocolc0_30toc0_27 extends StatelessProtocol<ClientboundPacketsc0_20a, ClientboundPacketsc0_28, ServerboundPacketsc0_28, ServerboundPacketsc0_28> {

    public Protocolc0_30toc0_27() {
        super(ClientboundPacketsc0_20a.class, ClientboundPacketsc0_28.class, ServerboundPacketsc0_28.class, ServerboundPacketsc0_28.class);
    }

    @Override
    public void init(UserConnection userConnection) {
        userConnection.put(new PreNettySplitter(Protocolc0_30toc0_27.class, ClientboundPacketsc0_20a::getPacket));

        final ClassicBlockRemapper previousRemapper = userConnection.get(ClassicBlockRemapper.class);
        userConnection.put(new ClassicBlockRemapper(previousRemapper.getMapper(), o -> {
            int block = previousRemapper.getReverseMapper().getInt(o);

            if (!userConnection.getProtocolInfo().getPipeline().contains(Protocolc0_27toc0_0_19a_06.class)) {
                if (block == ClassicBlocks.GOLD_ORE || block == ClassicBlocks.IRON_ORE || block == ClassicBlocks.COAL_ORE || block == ClassicBlocks.SLAB || block == ClassicBlocks.BRICK || block == ClassicBlocks.TNT || block == ClassicBlocks.BOOKSHELF || block == ClassicBlocks.MOSSY_COBBLESTONE || block == ClassicBlocks.OBSIDIAN) {
                    block = ClassicBlocks.STONE;
                }
            }

            return block;
        }));
    }

}
