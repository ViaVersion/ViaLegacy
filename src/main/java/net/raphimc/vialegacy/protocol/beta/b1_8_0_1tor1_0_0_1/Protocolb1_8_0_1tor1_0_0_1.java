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
package net.raphimc.vialegacy.protocol.beta.b1_8_0_1tor1_0_0_1;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.platform.providers.ViaProviders;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.api.type.Types;
import net.raphimc.vialegacy.api.protocol.StatelessProtocol;
import net.raphimc.vialegacy.api.splitter.PreNettySplitter;
import net.raphimc.vialegacy.protocol.beta.b1_8_0_1tor1_0_0_1.packet.ClientboundPacketsb1_8;
import net.raphimc.vialegacy.protocol.beta.b1_8_0_1tor1_0_0_1.packet.ServerboundPacketsb1_8;
import net.raphimc.vialegacy.protocol.beta.b1_8_0_1tor1_0_0_1.rewriter.ItemRewriter;
import net.raphimc.vialegacy.protocol.beta.b1_8_0_1tor1_0_0_1.storage.PlayerAirTimeStorage;
import net.raphimc.vialegacy.protocol.beta.b1_8_0_1tor1_0_0_1.task.PlayerAirTimeUpdateTask;
import net.raphimc.vialegacy.protocol.release.r1_0_0_1tor1_1.packet.ClientboundPackets1_0_0;
import net.raphimc.vialegacy.protocol.release.r1_0_0_1tor1_1.packet.ServerboundPackets1_0_0;
import net.raphimc.vialegacy.protocol.release.r1_2_4_5tor1_3_1_2.types.Types1_2_4;
import net.raphimc.vialegacy.protocol.release.r1_4_2tor1_4_4_5.types.Types1_4_2;
import net.raphimc.vialegacy.protocol.release.r1_7_6_10tor1_8.types.Types1_7_6;

public class Protocolb1_8_0_1tor1_0_0_1 extends StatelessProtocol<ClientboundPacketsb1_8, ClientboundPackets1_0_0, ServerboundPacketsb1_8, ServerboundPackets1_0_0> {

    private final ItemRewriter itemRewriter = new ItemRewriter(this);

    public Protocolb1_8_0_1tor1_0_0_1() {
        super(ClientboundPacketsb1_8.class, ClientboundPackets1_0_0.class, ServerboundPacketsb1_8.class, ServerboundPackets1_0_0.class);
    }

    @Override
    protected void registerPackets() {
        super.registerPackets();

        this.registerClientbound(ClientboundPacketsb1_8.SET_EXPERIENCE, wrapper -> {
            float experience = (float) wrapper.read(Types.BYTE);
            final byte experienceLevel = wrapper.read(Types.BYTE);
            final short experienceTotal = wrapper.read(Types.SHORT);
            experience = (experience - 1.0f) / (10 * experienceLevel);
            wrapper.write(Types.FLOAT, experience); // experience bar
            wrapper.write(Types.SHORT, (short) experienceLevel); // level
            wrapper.write(Types.SHORT, experienceTotal); // total experience
        });
        this.registerClientbound(ClientboundPacketsb1_8.CONTAINER_SET_SLOT, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.BYTE); // window id
                map(Types.SHORT); // slot
                map(Types1_4_2.NBTLESS_ITEM, Types1_2_4.NBT_ITEM); // item
            }
        });
        this.registerClientbound(ClientboundPacketsb1_8.CONTAINER_SET_CONTENT, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.BYTE); // window id
                map(Types1_4_2.NBTLESS_ITEM_ARRAY, Types1_2_4.NBT_ITEM_ARRAY); // item
            }
        });

        this.registerServerbound(ServerboundPackets1_0_0.USE_ITEM_ON, new PacketHandlers() {
            @Override
            public void register() {
                map(Types1_7_6.BLOCK_POSITION_UBYTE); // position
                map(Types.UNSIGNED_BYTE); // direction
                map(Types1_2_4.NBT_ITEM, Types1_4_2.NBTLESS_ITEM);
            }
        });
        this.registerServerbound(ServerboundPackets1_0_0.CONTAINER_CLICK, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.BYTE); // window id
                map(Types.SHORT); // slot
                map(Types.BYTE); // button
                map(Types.SHORT); // action
                map(Types.BYTE); // mode
                map(Types1_2_4.NBT_ITEM, Types1_4_2.NBTLESS_ITEM); // item
            }
        });
        this.cancelServerbound(ServerboundPackets1_0_0.CONTAINER_BUTTON_CLICK);
    }

    @Override
    public void register(ViaProviders providers) {
        Via.getPlatform().runRepeatingSync(new PlayerAirTimeUpdateTask(), 1L);
    }

    @Override
    public void init(UserConnection userConnection) {
        userConnection.put(new PreNettySplitter(Protocolb1_8_0_1tor1_0_0_1.class, ClientboundPacketsb1_8::getPacket));

        userConnection.put(new PlayerAirTimeStorage());
    }

    @Override
    public ItemRewriter getItemRewriter() {
        return this.itemRewriter;
    }

}
