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
package net.raphimc.vialegacy.protocols.beta.protocol1_0_0_1tob1_8_0_1;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.platform.providers.ViaProviders;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.api.type.Type;
import net.raphimc.vialegacy.api.protocol.StatelessProtocol;
import net.raphimc.vialegacy.api.remapper.LegacyItemRewriter;
import net.raphimc.vialegacy.api.splitter.PreNettySplitter;
import net.raphimc.vialegacy.protocols.beta.protocol1_0_0_1tob1_8_0_1.rewriter.ItemRewriter;
import net.raphimc.vialegacy.protocols.beta.protocol1_0_0_1tob1_8_0_1.storage.PlayerAirTimeStorage;
import net.raphimc.vialegacy.protocols.beta.protocol1_0_0_1tob1_8_0_1.task.PlayerAirTimeUpdateTask;
import net.raphimc.vialegacy.protocols.release.protocol1_1to1_0_0_1.ClientboundPackets1_0;
import net.raphimc.vialegacy.protocols.release.protocol1_1to1_0_0_1.ServerboundPackets1_0;
import net.raphimc.vialegacy.protocols.release.protocol1_3_1_2to1_2_4_5.types.Types1_2_4;
import net.raphimc.vialegacy.protocols.release.protocol1_4_4_5to1_4_2.types.Types1_4_2;
import net.raphimc.vialegacy.protocols.release.protocol1_8to1_7_6_10.types.Types1_7_6;

public class Protocol1_0_0_1tob1_8_0_1 extends StatelessProtocol<ClientboundPacketsb1_8, ClientboundPackets1_0, ServerboundPacketsb1_8, ServerboundPackets1_0> {

    private final LegacyItemRewriter<Protocol1_0_0_1tob1_8_0_1> itemRewriter = new ItemRewriter(this);

    public Protocol1_0_0_1tob1_8_0_1() {
        super(ClientboundPacketsb1_8.class, ClientboundPackets1_0.class, ServerboundPacketsb1_8.class, ServerboundPackets1_0.class);
    }

    @Override
    protected void registerPackets() {
        this.itemRewriter.register();

        this.registerClientbound(ClientboundPacketsb1_8.SET_EXPERIENCE, wrapper -> {
            float experience = (float) wrapper.read(Type.BYTE);
            final byte experienceLevel = wrapper.read(Type.BYTE);
            final short experienceTotal = wrapper.read(Type.SHORT);
            experience = (experience - 1.0f) / (10 * experienceLevel);
            wrapper.write(Type.FLOAT, experience); // experience bar
            wrapper.write(Type.SHORT, (short) experienceLevel); // level
            wrapper.write(Type.SHORT, experienceTotal); // total experience
        });
        this.registerClientbound(ClientboundPacketsb1_8.SET_SLOT, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.BYTE); // window id
                map(Type.SHORT); // slot
                map(Types1_4_2.NBTLESS_ITEM, Types1_2_4.NBT_ITEM); // item
            }
        });
        this.registerClientbound(ClientboundPacketsb1_8.WINDOW_ITEMS, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.BYTE); // window id
                map(Types1_4_2.NBTLESS_ITEM_ARRAY, Types1_2_4.NBT_ITEM_ARRAY); // item
            }
        });

        this.registerServerbound(ServerboundPackets1_0.PLAYER_BLOCK_PLACEMENT, new PacketHandlers() {
            @Override
            public void register() {
                map(Types1_7_6.POSITION_UBYTE); // position
                map(Type.UNSIGNED_BYTE); // direction
                map(Types1_2_4.NBT_ITEM, Types1_4_2.NBTLESS_ITEM);
            }
        });
        this.registerServerbound(ServerboundPackets1_0.CLICK_WINDOW, new PacketHandlers() {
            @Override
            public void register() {
                map(Type.BYTE); // window id
                map(Type.SHORT); // slot
                map(Type.BYTE); // button
                map(Type.SHORT); // action
                map(Type.BYTE); // mode
                map(Types1_2_4.NBT_ITEM, Types1_4_2.NBTLESS_ITEM); // item
            }
        });
        this.cancelServerbound(ServerboundPackets1_0.CLICK_WINDOW_BUTTON);
    }

    @Override
    public void register(ViaProviders providers) {
        Via.getPlatform().runRepeatingSync(new PlayerAirTimeUpdateTask(), 1L);
    }

    @Override
    public void init(UserConnection userConnection) {
        userConnection.put(new PreNettySplitter(Protocol1_0_0_1tob1_8_0_1.class, ClientboundPacketsb1_8::getPacket));

        userConnection.put(new PlayerAirTimeStorage());
    }

    @Override
    public LegacyItemRewriter<Protocol1_0_0_1tob1_8_0_1> getItemRewriter() {
        return this.itemRewriter;
    }

}
