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
package net.raphimc.vialegacy.protocols.release.protocol1_1to1_0_0_1;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.api.type.Type;
import net.raphimc.vialegacy.api.protocol.StatelessProtocol;
import net.raphimc.vialegacy.api.remapper.LegacyItemRewriter;
import net.raphimc.vialegacy.api.splitter.PreNettySplitter;
import net.raphimc.vialegacy.protocols.release.protocol1_1to1_0_0_1.rewriter.ChatFilter;
import net.raphimc.vialegacy.protocols.release.protocol1_1to1_0_0_1.rewriter.ItemRewriter;
import net.raphimc.vialegacy.protocols.release.protocol1_2_1_3to1_1.ClientboundPackets1_1;
import net.raphimc.vialegacy.protocols.release.protocol1_2_1_3to1_1.ServerboundPackets1_1;
import net.raphimc.vialegacy.protocols.release.protocol1_7_2_5to1_6_4.types.Types1_6_4;
import net.raphimc.vialegacy.protocols.release.protocol1_8to1_7_6_10.types.Types1_7_6;

public class Protocol1_1to1_0_0_1 extends StatelessProtocol<ClientboundPackets1_0, ClientboundPackets1_1, ServerboundPackets1_0, ServerboundPackets1_1> {

    private final LegacyItemRewriter<Protocol1_1to1_0_0_1> itemRewriter = new ItemRewriter(this);

    public Protocol1_1to1_0_0_1() {
        super(ClientboundPackets1_0.class, ClientboundPackets1_1.class, ServerboundPackets1_0.class, ServerboundPackets1_1.class);
    }

    @Override
    protected void registerPackets() {
        this.itemRewriter.register();

        this.registerServerbound(ServerboundPackets1_1.LOGIN, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.INT); // protocol id
                map(Types1_6_4.STRING); // username
                map(Type.LONG); // seed
                read(Types1_6_4.STRING); // level type
                map(Type.INT); // game mode
                map(Type.BYTE); // dimension id
                map(Type.BYTE); // difficulty
                map(Type.BYTE); // world height
                map(Type.BYTE); // max players
            }
        });
        this.registerServerbound(ServerboundPackets1_1.CHAT_MESSAGE, new PacketHandlers() {
            @Override
            public void register() {
                map(Types1_6_4.STRING, Types1_6_4.STRING, ChatFilter::filter); // message
            }
        });
        this.registerServerbound(ServerboundPackets1_1.RESPAWN, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.BYTE); // dimension id
                map(Type.BYTE); // difficulty
                map(Type.BYTE); // game mode
                map(Type.SHORT); // world height
                map(Type.LONG); // seed
                read(Types1_6_4.STRING); // level type
            }
        });
        this.cancelServerbound(ServerboundPackets1_1.PLUGIN_MESSAGE);

        this.registerClientbound(ClientboundPackets1_0.JOIN_GAME, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.INT); // entity id
                map(Types1_6_4.STRING); // username
                map(Type.LONG); // seed
                create(Types1_6_4.STRING, "default_1_1"); // level type
                map(Type.INT); // game mode
                map(Type.BYTE); // dimension id
                map(Type.BYTE); // difficulty
                map(Type.BYTE); // world height
                map(Type.BYTE); // max players
            }
        });
        this.registerClientbound(ClientboundPackets1_0.CHAT_MESSAGE, new PacketHandlers() {
            @Override
            public void register() {
                map(Types1_6_4.STRING, Types1_6_4.STRING, msg -> msg.replace("\u00C2", "")); // message
            }
        });
        this.registerClientbound(ClientboundPackets1_0.RESPAWN, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.BYTE); // dimension id
                map(Type.BYTE); // difficulty
                map(Type.BYTE); // game mode
                map(Type.SHORT); // world height
                map(Type.LONG); // seed
                create(Types1_6_4.STRING, "default_1_1"); // level type
            }
        });
        this.registerClientbound(ClientboundPackets1_0.UPDATE_SIGN, new PacketHandlers() {
            @Override
            public void register() {
                map(Types1_7_6.POSITION_SHORT); // position
                map(Types1_6_4.STRING, Types1_6_4.STRING, msg -> msg.replace("\u00C2", "")); // line 1
                map(Types1_6_4.STRING, Types1_6_4.STRING, msg -> msg.replace("\u00C2", "")); // line 2
                map(Types1_6_4.STRING, Types1_6_4.STRING, msg -> msg.replace("\u00C2", "")); // line 3
                map(Types1_6_4.STRING, Types1_6_4.STRING, msg -> msg.replace("\u00C2", "")); // line 4
            }
        });
        this.registerClientbound(ClientboundPackets1_0.DISCONNECT, new PacketHandlers() {
            @Override
            public void register() {
                map(Types1_6_4.STRING, Types1_6_4.STRING, reason -> reason.replace("\u00C2", "")); // reason
            }
        });

        //C->S Packet27Position is unused (no need to handle or remap)
    }

    @Override
    public void init(UserConnection userConnection) {
        userConnection.put(new PreNettySplitter(Protocol1_1to1_0_0_1.class, ClientboundPackets1_0::getPacket));
    }

    @Override
    public LegacyItemRewriter<Protocol1_1to1_0_0_1> getItemRewriter() {
        return this.itemRewriter;
    }

}
