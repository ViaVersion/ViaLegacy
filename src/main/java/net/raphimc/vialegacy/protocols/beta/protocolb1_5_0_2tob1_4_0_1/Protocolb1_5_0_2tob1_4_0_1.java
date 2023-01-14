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
package net.raphimc.vialegacy.protocols.beta.protocolb1_5_0_2tob1_4_0_1;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.protocol.packet.State;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.api.type.Type;
import net.raphimc.vialegacy.api.splitter.PreNettySplitter;
import net.raphimc.vialegacy.protocols.beta.protocolb1_5_0_2tob1_4_0_1.types.Typesb1_4;
import net.raphimc.vialegacy.protocols.beta.protocolb1_6_0_6tob1_5_0_2.ClientboundPacketsb1_5;
import net.raphimc.vialegacy.protocols.beta.protocolb1_6_0_6tob1_5_0_2.ServerboundPacketsb1_5;
import net.raphimc.vialegacy.protocols.beta.protocolb1_8_0_1tob1_7_0_3.types.Typesb1_7_0_3;
import net.raphimc.vialegacy.protocols.release.protocol1_4_2to1_3_1_2.types.MetaType1_3_1;
import net.raphimc.vialegacy.protocols.release.protocol1_4_2to1_3_1_2.types.Types1_3_1;
import net.raphimc.vialegacy.protocols.release.protocol1_4_4_5to1_4_2.types.Types1_4_2;
import net.raphimc.vialegacy.protocols.release.protocol1_7_2_5to1_6_4.types.Types1_6_4;
import net.raphimc.vialegacy.protocols.release.protocol1_8to1_7_6_10.types.Types1_7_6;

import java.util.List;

public class Protocolb1_5_0_2tob1_4_0_1 extends AbstractProtocol<ClientboundPacketsb1_4, ClientboundPacketsb1_5, ServerboundPacketsb1_4, ServerboundPacketsb1_5> {

    public Protocolb1_5_0_2tob1_4_0_1() {
        super(ClientboundPacketsb1_4.class, ClientboundPacketsb1_5.class, ServerboundPacketsb1_4.class, ServerboundPacketsb1_5.class);
    }

    @Override
    protected void registerPackets() {
        this.registerClientbound(State.LOGIN, ClientboundPacketsb1_4.HANDSHAKE.getId(), ClientboundPacketsb1_5.HANDSHAKE.getId(), new PacketRemapper() {
            @Override
            public void registerMap() {
                map(Typesb1_7_0_3.STRING, Types1_6_4.STRING); // server hash
            }
        });
        this.registerClientbound(State.LOGIN, ClientboundPacketsb1_4.DISCONNECT.getId(), ClientboundPacketsb1_5.DISCONNECT.getId(), new PacketRemapper() {
            @Override
            public void registerMap() {
                map(Typesb1_7_0_3.STRING, Types1_6_4.STRING); // reason
            }
        });
        this.registerClientbound(ClientboundPacketsb1_4.JOIN_GAME, new PacketRemapper() {
            @Override
            public void registerMap() {
                map(Type.INT); // entity id
                map(Typesb1_7_0_3.STRING, Types1_6_4.STRING); // username
                read(Typesb1_7_0_3.STRING); // password
                map(Type.LONG); // seed
                map(Type.BYTE); // dimension id
            }
        });
        this.registerClientbound(ClientboundPacketsb1_4.CHAT_MESSAGE, new PacketRemapper() {
            @Override
            public void registerMap() {
                map(Typesb1_7_0_3.STRING, Types1_6_4.STRING); // message
            }
        });
        this.registerClientbound(ClientboundPacketsb1_4.SPAWN_PLAYER, new PacketRemapper() {
            @Override
            public void registerMap() {
                map(Type.INT); // entity id
                map(Typesb1_7_0_3.STRING, Types1_6_4.STRING); // username
                handler(wrapper -> {
                    String name = wrapper.get(Types1_6_4.STRING, 0);
                    name = name.substring(0, Math.min(name.length(), 16));
                    wrapper.set(Types1_6_4.STRING, 0, name);
                });
                map(Type.INT); // x
                map(Type.INT); // y
                map(Type.INT); // z
                map(Type.BYTE); // yaw
                map(Type.BYTE); // pitch
                map(Type.UNSIGNED_SHORT); // item
            }
        });
        this.registerClientbound(ClientboundPacketsb1_4.SPAWN_MOB, new PacketRemapper() {
            @Override
            public void registerMap() {
                map(Type.INT); // entity id
                map(Type.UNSIGNED_BYTE); // type id
                map(Type.INT); // x
                map(Type.INT); // y
                map(Type.INT); // z
                map(Type.BYTE); // yaw
                map(Type.BYTE); // pitch
                map(Typesb1_4.METADATA_LIST, Types1_3_1.METADATA_LIST); // metadata
                handler(wrapper -> rewriteMetadata(wrapper.get(Types1_3_1.METADATA_LIST, 0)));
            }
        });
        this.registerClientbound(ClientboundPacketsb1_4.SPAWN_PAINTING, new PacketRemapper() {
            @Override
            public void registerMap() {
                map(Type.INT); // entity id
                map(Typesb1_7_0_3.STRING, Types1_6_4.STRING); // motive
                map(Types1_7_6.POSITION_INT); // position
                map(Type.INT); // rotation
            }
        });
        this.registerClientbound(ClientboundPacketsb1_4.ENTITY_METADATA, new PacketRemapper() {
            @Override
            public void registerMap() {
                map(Type.INT); // entity id
                map(Typesb1_4.METADATA_LIST, Types1_3_1.METADATA_LIST); // metadata
                handler(wrapper -> rewriteMetadata(wrapper.get(Types1_3_1.METADATA_LIST, 0)));
            }
        });
        this.registerClientbound(ClientboundPacketsb1_4.UPDATE_SIGN, new PacketRemapper() {
            @Override
            public void registerMap() {
                map(Types1_7_6.POSITION_SHORT); // position
                map(Typesb1_7_0_3.STRING, Types1_6_4.STRING); // line 1
                map(Typesb1_7_0_3.STRING, Types1_6_4.STRING); // line 2
                map(Typesb1_7_0_3.STRING, Types1_6_4.STRING); // line 3
                map(Typesb1_7_0_3.STRING, Types1_6_4.STRING); // line 4
            }
        });
        this.registerClientbound(ClientboundPacketsb1_4.DISCONNECT, new PacketRemapper() {
            @Override
            public void registerMap() {
                map(Typesb1_7_0_3.STRING, Types1_6_4.STRING); // reason
            }
        });

        this.registerServerbound(State.LOGIN, ServerboundPacketsb1_5.HANDSHAKE.getId(), ServerboundPacketsb1_4.HANDSHAKE.getId(), new PacketRemapper() {
            @Override
            public void registerMap() {
                map(Types1_6_4.STRING, Typesb1_7_0_3.STRING); // username
            }
        });
        this.registerServerbound(State.LOGIN, ServerboundPacketsb1_4.LOGIN.getId(), ServerboundPacketsb1_5.LOGIN.getId(), new PacketRemapper() {
            @Override
            public void registerMap() {
                map(Type.INT); // protocol id
                map(Types1_6_4.STRING, Typesb1_7_0_3.STRING); // username
                create(Typesb1_7_0_3.STRING, "Password"); // password
                map(Type.LONG); // seed
                map(Type.BYTE); // dimension id
            }
        });
        this.registerServerbound(ServerboundPacketsb1_5.CHAT_MESSAGE, new PacketRemapper() {
            @Override
            public void registerMap() {
                map(Types1_6_4.STRING, Typesb1_7_0_3.STRING); // message
            }
        });
        this.registerServerbound(ServerboundPacketsb1_5.CLICK_WINDOW, new PacketRemapper() {
            @Override
            public void registerMap() {
                map(Type.BYTE); // window id
                map(Type.SHORT); // slot
                map(Type.BYTE); // button
                map(Type.SHORT); // action
                read(Type.BYTE); // mode
                map(Types1_4_2.NBTLESS_ITEM); // item
            }
        });
        this.registerServerbound(ServerboundPacketsb1_5.UPDATE_SIGN, new PacketRemapper() {
            @Override
            public void registerMap() {
                map(Types1_7_6.POSITION_SHORT); // position
                map(Types1_6_4.STRING, Typesb1_7_0_3.STRING); // line 1
                map(Types1_6_4.STRING, Typesb1_7_0_3.STRING); // line 2
                map(Types1_6_4.STRING, Typesb1_7_0_3.STRING); // line 3
                map(Types1_6_4.STRING, Typesb1_7_0_3.STRING); // line 4
            }
        });
        this.registerServerbound(ServerboundPacketsb1_5.DISCONNECT, new PacketRemapper() {
            @Override
            public void registerMap() {
                map(Types1_6_4.STRING, Typesb1_7_0_3.STRING); // reason
            }
        });
    }

    private void rewriteMetadata(final List<Metadata> metadataList) {
        for (Metadata metadata : metadataList) {
            metadata.setMetaType(MetaType1_3_1.byId(metadata.metaType().typeId()));
        }
    }

    @Override
    public void init(UserConnection userConnection) {
        userConnection.put(new PreNettySplitter(userConnection, Protocolb1_5_0_2tob1_4_0_1.class, ClientboundPacketsb1_4::getPacket));
    }

}
