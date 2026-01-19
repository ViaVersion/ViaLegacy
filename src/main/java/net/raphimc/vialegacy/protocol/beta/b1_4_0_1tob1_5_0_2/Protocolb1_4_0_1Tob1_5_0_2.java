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
package net.raphimc.vialegacy.protocol.beta.b1_4_0_1tob1_5_0_2;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.entitydata.EntityData;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.api.type.Types;
import net.raphimc.vialegacy.api.protocol.StatelessProtocol;
import net.raphimc.vialegacy.api.splitter.PreNettySplitter;
import net.raphimc.vialegacy.protocol.beta.b1_4_0_1tob1_5_0_2.packet.ClientboundPacketsb1_4;
import net.raphimc.vialegacy.protocol.beta.b1_4_0_1tob1_5_0_2.packet.ServerboundPacketsb1_4;
import net.raphimc.vialegacy.protocol.beta.b1_4_0_1tob1_5_0_2.types.Typesb1_4;
import net.raphimc.vialegacy.protocol.beta.b1_5_0_2tob1_6_0_6.packet.ClientboundPacketsb1_5;
import net.raphimc.vialegacy.protocol.beta.b1_5_0_2tob1_6_0_6.packet.ServerboundPacketsb1_5;
import net.raphimc.vialegacy.protocol.beta.b1_7_0_3tob1_8_0_1.types.Typesb1_7_0_3;
import net.raphimc.vialegacy.protocol.release.r1_3_1_2tor1_4_2.types.EntityDataTypes1_3_1;
import net.raphimc.vialegacy.protocol.release.r1_3_1_2tor1_4_2.types.Types1_3_1;
import net.raphimc.vialegacy.protocol.release.r1_4_2tor1_4_4_5.types.Types1_4_2;
import net.raphimc.vialegacy.protocol.release.r1_6_4tor1_7_2_5.types.Types1_6_4;
import net.raphimc.vialegacy.protocol.release.r1_7_6_10tor1_8.types.Types1_7_6;

import java.util.List;

public class Protocolb1_4_0_1Tob1_5_0_2 extends StatelessProtocol<ClientboundPacketsb1_4, ClientboundPacketsb1_5, ServerboundPacketsb1_4, ServerboundPacketsb1_5> {

    public Protocolb1_4_0_1Tob1_5_0_2() {
        super(ClientboundPacketsb1_4.class, ClientboundPacketsb1_5.class, ServerboundPacketsb1_4.class, ServerboundPacketsb1_5.class);
    }

    @Override
    protected void registerPackets() {
        this.registerClientbound(ClientboundPacketsb1_4.HANDSHAKE, new PacketHandlers() {
            @Override
            public void register() {
                map(Typesb1_7_0_3.STRING, Types1_6_4.STRING); // server hash
            }
        });
        this.registerClientbound(ClientboundPacketsb1_4.LOGIN, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // entity id
                map(Typesb1_7_0_3.STRING, Types1_6_4.STRING); // username
                read(Typesb1_7_0_3.STRING); // password
                map(Types.LONG); // seed
                map(Types.BYTE); // dimension id
            }
        });
        this.registerClientbound(ClientboundPacketsb1_4.CHAT, new PacketHandlers() {
            @Override
            public void register() {
                map(Typesb1_7_0_3.STRING, Types1_6_4.STRING); // message
            }
        });
        this.registerClientbound(ClientboundPacketsb1_4.ADD_PLAYER, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // entity id
                map(Typesb1_7_0_3.STRING, Types1_6_4.STRING); // username
                handler(wrapper -> {
                    String name = wrapper.get(Types1_6_4.STRING, 0);
                    name = name.substring(0, Math.min(name.length(), 16));
                    wrapper.set(Types1_6_4.STRING, 0, name);
                });
                map(Types.INT); // x
                map(Types.INT); // y
                map(Types.INT); // z
                map(Types.BYTE); // yaw
                map(Types.BYTE); // pitch
                map(Types.UNSIGNED_SHORT); // item
            }
        });
        this.registerClientbound(ClientboundPacketsb1_4.ADD_MOB, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // entity id
                map(Types.UNSIGNED_BYTE); // type id
                map(Types.INT); // x
                map(Types.INT); // y
                map(Types.INT); // z
                map(Types.BYTE); // yaw
                map(Types.BYTE); // pitch
                map(Typesb1_4.ENTITY_DATA_LIST, Types1_3_1.ENTITY_DATA_LIST); // entity data
                handler(wrapper -> rewriteEntityData(wrapper.get(Types1_3_1.ENTITY_DATA_LIST, 0)));
            }
        });
        this.registerClientbound(ClientboundPacketsb1_4.ADD_PAINTING, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // entity id
                map(Typesb1_7_0_3.STRING, Types1_6_4.STRING); // motive
                map(Types1_7_6.BLOCK_POSITION_INT); // position
                map(Types.INT); // rotation
            }
        });
        this.registerClientbound(ClientboundPacketsb1_4.SET_ENTITY_DATA, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // entity id
                map(Typesb1_4.ENTITY_DATA_LIST, Types1_3_1.ENTITY_DATA_LIST); // entity data
                handler(wrapper -> rewriteEntityData(wrapper.get(Types1_3_1.ENTITY_DATA_LIST, 0)));
            }
        });
        this.registerClientbound(ClientboundPacketsb1_4.UPDATE_SIGN, new PacketHandlers() {
            @Override
            public void register() {
                map(Types1_7_6.BLOCK_POSITION_SHORT); // position
                map(Typesb1_7_0_3.STRING, Types1_6_4.STRING); // line 1
                map(Typesb1_7_0_3.STRING, Types1_6_4.STRING); // line 2
                map(Typesb1_7_0_3.STRING, Types1_6_4.STRING); // line 3
                map(Typesb1_7_0_3.STRING, Types1_6_4.STRING); // line 4
            }
        });
        this.registerClientbound(ClientboundPacketsb1_4.DISCONNECT, new PacketHandlers() {
            @Override
            public void register() {
                map(Typesb1_7_0_3.STRING, Types1_6_4.STRING); // reason
            }
        });

        this.registerServerbound(ServerboundPacketsb1_5.HANDSHAKE, new PacketHandlers() {
            @Override
            public void register() {
                map(Types1_6_4.STRING, Typesb1_7_0_3.STRING); // username
            }
        });
        this.registerServerbound(ServerboundPacketsb1_5.LOGIN, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.INT); // protocol id
                map(Types1_6_4.STRING, Typesb1_7_0_3.STRING); // username
                create(Typesb1_7_0_3.STRING, "Password"); // password
                map(Types.LONG); // seed
                map(Types.BYTE); // dimension id
            }
        });
        this.registerServerbound(ServerboundPacketsb1_5.CHAT, new PacketHandlers() {
            @Override
            public void register() {
                map(Types1_6_4.STRING, Typesb1_7_0_3.STRING); // message
            }
        });
        this.registerServerbound(ServerboundPacketsb1_5.CONTAINER_CLICK, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.BYTE); // window id
                map(Types.SHORT); // slot
                map(Types.BYTE); // button
                map(Types.SHORT); // action
                read(Types.BYTE); // mode
                map(Types1_4_2.NBTLESS_ITEM); // item
            }
        });
        this.registerServerbound(ServerboundPacketsb1_5.SIGN_UPDATE, new PacketHandlers() {
            @Override
            public void register() {
                map(Types1_7_6.BLOCK_POSITION_SHORT); // position
                map(Types1_6_4.STRING, Typesb1_7_0_3.STRING); // line 1
                map(Types1_6_4.STRING, Typesb1_7_0_3.STRING); // line 2
                map(Types1_6_4.STRING, Typesb1_7_0_3.STRING); // line 3
                map(Types1_6_4.STRING, Typesb1_7_0_3.STRING); // line 4
            }
        });
        this.registerServerbound(ServerboundPacketsb1_5.DISCONNECT, new PacketHandlers() {
            @Override
            public void register() {
                map(Types1_6_4.STRING, Typesb1_7_0_3.STRING); // reason
            }
        });
    }

    private void rewriteEntityData(final List<EntityData> entityDataList) {
        for (EntityData entityData : entityDataList) {
            entityData.setDataType(EntityDataTypes1_3_1.byId(entityData.dataType().typeId()));
        }
    }

    @Override
    public void init(UserConnection userConnection) {
        userConnection.put(new PreNettySplitter(Protocolb1_4_0_1Tob1_5_0_2.class, ClientboundPacketsb1_4::getPacket));
    }

}
