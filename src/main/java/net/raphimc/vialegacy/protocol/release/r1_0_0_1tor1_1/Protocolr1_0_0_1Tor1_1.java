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
package net.raphimc.vialegacy.protocol.release.r1_0_0_1tor1_1;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.api.type.Types;
import net.raphimc.vialegacy.api.protocol.StatelessProtocol;
import net.raphimc.vialegacy.api.splitter.PreNettySplitter;
import net.raphimc.vialegacy.protocol.release.r1_0_0_1tor1_1.packet.ClientboundPackets1_0_0;
import net.raphimc.vialegacy.protocol.release.r1_0_0_1tor1_1.packet.ServerboundPackets1_0_0;
import net.raphimc.vialegacy.protocol.release.r1_0_0_1tor1_1.rewriter.ChatFilter;
import net.raphimc.vialegacy.protocol.release.r1_0_0_1tor1_1.rewriter.ItemRewriter;
import net.raphimc.vialegacy.protocol.release.r1_1tor1_2_1_3.packet.ClientboundPackets1_1;
import net.raphimc.vialegacy.protocol.release.r1_1tor1_2_1_3.packet.ServerboundPackets1_1;
import net.raphimc.vialegacy.protocol.release.r1_6_4tor1_7_2_5.types.Types1_6_4;
import net.raphimc.vialegacy.protocol.release.r1_7_6_10tor1_8.types.Types1_7_6;

public class Protocolr1_0_0_1Tor1_1 extends StatelessProtocol<ClientboundPackets1_0_0, ClientboundPackets1_1, ServerboundPackets1_0_0, ServerboundPackets1_1> {

    private final ItemRewriter itemRewriter = new ItemRewriter(this);

    public Protocolr1_0_0_1Tor1_1() {
        super(ClientboundPackets1_0_0.class, ClientboundPackets1_1.class, ServerboundPackets1_0_0.class, ServerboundPackets1_1.class);
    }

    @Override
    protected void registerPackets() {
        super.registerPackets();

        this.registerServerbound(ServerboundPackets1_1.LOGIN, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // protocol id
                map(Types1_6_4.STRING); // username
                map(Types.LONG); // seed
                read(Types1_6_4.STRING); // level type
                map(Types.INT); // game mode
                map(Types.BYTE); // dimension id
                map(Types.BYTE); // difficulty
                map(Types.BYTE); // world height
                map(Types.BYTE); // max players
            }
        });
        this.registerServerbound(ServerboundPackets1_1.CHAT, new PacketHandlers() {
            @Override
            public void register() {
                map(Types1_6_4.STRING, Types1_6_4.STRING, ChatFilter::filter); // message
            }
        });
        this.registerServerbound(ServerboundPackets1_1.RESPAWN, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.BYTE); // dimension id
                map(Types.BYTE); // difficulty
                map(Types.BYTE); // game mode
                map(Types.SHORT); // world height
                map(Types.LONG); // seed
                read(Types1_6_4.STRING); // level type
            }
        });
        this.cancelServerbound(ServerboundPackets1_1.CUSTOM_PAYLOAD);

        this.registerClientbound(ClientboundPackets1_0_0.LOGIN, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // entity id
                map(Types1_6_4.STRING); // username
                map(Types.LONG); // seed
                create(Types1_6_4.STRING, "default_1_1"); // level type
                map(Types.INT); // game mode
                map(Types.BYTE); // dimension id
                map(Types.BYTE); // difficulty
                map(Types.BYTE); // world height
                map(Types.BYTE); // max players
            }
        });
        this.registerClientbound(ClientboundPackets1_0_0.CHAT, new PacketHandlers() {
            @Override
            public void register() {
                map(Types1_6_4.STRING, Types1_6_4.STRING, msg -> msg.replace("\u00C2", "")); // message
            }
        });
        this.registerClientbound(ClientboundPackets1_0_0.RESPAWN, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.BYTE); // dimension id
                map(Types.BYTE); // difficulty
                map(Types.BYTE); // game mode
                map(Types.SHORT); // world height
                map(Types.LONG); // seed
                create(Types1_6_4.STRING, "default_1_1"); // level type
            }
        });
        this.registerClientbound(ClientboundPackets1_0_0.UPDATE_SIGN, new PacketHandlers() {
            @Override
            public void register() {
                map(Types1_7_6.BLOCK_POSITION_SHORT); // position
                map(Types1_6_4.STRING, Types1_6_4.STRING, msg -> msg.replace("\u00C2", "")); // line 1
                map(Types1_6_4.STRING, Types1_6_4.STRING, msg -> msg.replace("\u00C2", "")); // line 2
                map(Types1_6_4.STRING, Types1_6_4.STRING, msg -> msg.replace("\u00C2", "")); // line 3
                map(Types1_6_4.STRING, Types1_6_4.STRING, msg -> msg.replace("\u00C2", "")); // line 4
            }
        });
        this.registerClientbound(ClientboundPackets1_0_0.DISCONNECT, new PacketHandlers() {
            @Override
            public void register() {
                map(Types1_6_4.STRING, Types1_6_4.STRING, reason -> reason.replace("\u00C2", "")); // reason
            }
        });

        //C->S Packet27Position is unused (no need to handle or remap)
    }

    @Override
    public void init(UserConnection userConnection) {
        userConnection.put(new PreNettySplitter(Protocolr1_0_0_1Tor1_1.class, ClientboundPackets1_0_0::getPacket));
    }

    @Override
    public ItemRewriter getItemRewriter() {
        return this.itemRewriter;
    }

}
