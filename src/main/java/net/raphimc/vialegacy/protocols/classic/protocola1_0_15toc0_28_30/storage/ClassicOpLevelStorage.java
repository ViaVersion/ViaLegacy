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
package net.raphimc.vialegacy.protocols.classic.protocola1_0_15toc0_28_30.storage;

import com.viaversion.viaversion.api.connection.StoredObject;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.type.Type;
import net.raphimc.vialegacy.protocols.alpha.protocola1_0_16_2toa1_0_15.ClientboundPacketsa1_0_15;
import net.raphimc.vialegacy.protocols.beta.protocolb1_8_0_1tob1_7_0_3.types.Typesb1_7_0_3;
import net.raphimc.vialegacy.protocols.classic.protocola1_0_15toc0_28_30.Protocola1_0_15toc0_30;
import net.raphimc.vialegacy.protocols.release.protocol1_2_4_5to1_2_1_3.Protocol1_2_4_5to1_2_1_3;
import net.raphimc.vialegacy.protocols.release.protocol1_3_1_2to1_2_4_5.ClientboundPackets1_2_4;

public class ClassicOpLevelStorage extends StoredObject {

    private byte opLevel;

    private boolean flying = true;
    private boolean noClip = true;
    private boolean speed = true;
    private boolean respawn = true;

    public ClassicOpLevelStorage(final UserConnection user) {
        super(user);
    }

    public void updateHax(final boolean flying, final boolean noClip, final boolean speed, final boolean respawn) throws Exception {
        boolean changed = this.flying != flying;
        changed |= this.noClip != noClip;
        changed |= this.speed != speed;
        changed |= this.respawn != respawn;

        if (this.flying != flying) {
            this.flying = flying;
            this.updateAbilities();
        }

        this.noClip = noClip;
        this.speed = speed;
        this.respawn = respawn;

        if (changed) {
            String statusMessage = "§6Hack control: ";
            statusMessage += this.flying ? "§aFlying" : "§cFlying";
            statusMessage += " ";
            statusMessage += this.noClip ? "§aNoClip" : "§cNoClip";
            statusMessage += " ";
            statusMessage += this.speed ? "§aSpeed" : "§cSpeed";
            statusMessage += " ";
            statusMessage += this.respawn ? "§aRespawn" : "§cRespawn";

            final PacketWrapper chatMessage = PacketWrapper.create(ClientboundPacketsa1_0_15.CHAT_MESSAGE, this.getUser());
            chatMessage.write(Typesb1_7_0_3.STRING, statusMessage); // message
            chatMessage.send(Protocola1_0_15toc0_30.class);
        }
    }

    public void setOpLevel(final byte opLevel) throws Exception {
        final boolean changed = this.opLevel != opLevel;
        this.opLevel = opLevel;

        final ClassicServerTitleStorage serverTitleStorage = this.getUser().get(ClassicServerTitleStorage.class);
        this.updateHax(serverTitleStorage.isFlyEffectivelyEnabled(), serverTitleStorage.isNoclipEffectivelyEnabled(), serverTitleStorage.isSpeedEffectivelyEnabled(), serverTitleStorage.isRespawnEffectivelyEnabled());
        if (changed) {
            this.updateAbilities();
        }
    }

    public byte getOpLevel() {
        return this.opLevel;
    }

    public void updateAbilities() throws Exception {
        if (this.getUser().getProtocolInfo().getPipeline().contains(Protocol1_2_4_5to1_2_1_3.class)) {
            final PacketWrapper playerAbilities = PacketWrapper.create(ClientboundPackets1_2_4.PLAYER_ABILITIES, this.getUser());
            playerAbilities.write(Type.BOOLEAN, true); // invulnerable
            playerAbilities.write(Type.BOOLEAN, false); // flying
            playerAbilities.write(Type.BOOLEAN, this.flying); // allow flying
            playerAbilities.write(Type.BOOLEAN, true); // creative mode
            playerAbilities.send(Protocol1_2_4_5to1_2_1_3.class);
        }
    }

}
