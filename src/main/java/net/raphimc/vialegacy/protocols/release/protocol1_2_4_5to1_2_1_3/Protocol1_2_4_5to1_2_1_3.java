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
package net.raphimc.vialegacy.protocols.release.protocol1_2_4_5to1_2_1_3;

import com.viaversion.viaversion.api.connection.UserConnection;
import net.raphimc.vialegacy.api.protocol.StatelessProtocol;
import net.raphimc.vialegacy.api.remapper.LegacyItemRewriter;
import net.raphimc.vialegacy.api.splitter.PreNettySplitter;
import net.raphimc.vialegacy.protocols.release.protocol1_2_4_5to1_2_1_3.rewriter.ItemRewriter;
import net.raphimc.vialegacy.protocols.release.protocol1_3_1_2to1_2_4_5.ClientboundPackets1_2_4;
import net.raphimc.vialegacy.protocols.release.protocol1_3_1_2to1_2_4_5.ServerboundPackets1_2_4;

public class Protocol1_2_4_5to1_2_1_3 extends StatelessProtocol<ClientboundPackets1_2_1, ClientboundPackets1_2_4, ServerboundPackets1_2_1, ServerboundPackets1_2_4> {

    private final LegacyItemRewriter<Protocol1_2_4_5to1_2_1_3> itemRewriter = new ItemRewriter(this);

    public Protocol1_2_4_5to1_2_1_3() {
        super(ClientboundPackets1_2_1.class, ClientboundPackets1_2_4.class, ServerboundPackets1_2_1.class, ServerboundPackets1_2_4.class);
    }

    @Override
    protected void registerPackets() {
        this.itemRewriter.register();

        this.cancelServerbound(ServerboundPackets1_2_4.PLAYER_ABILITIES);
    }

    @Override
    public void init(UserConnection userConnection) {
        userConnection.put(new PreNettySplitter(Protocol1_2_4_5to1_2_1_3.class, ClientboundPackets1_2_1::getPacket));
    }

    @Override
    public LegacyItemRewriter<Protocol1_2_4_5to1_2_1_3> getItemRewriter() {
        return this.itemRewriter;
    }

}
