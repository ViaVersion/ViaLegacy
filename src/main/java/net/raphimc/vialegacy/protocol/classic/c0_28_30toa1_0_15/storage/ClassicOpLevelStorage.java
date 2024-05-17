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
package net.raphimc.vialegacy.protocol.classic.c0_28_30toa1_0_15.storage;

import com.viaversion.viaversion.api.connection.StoredObject;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.type.Types;
import net.raphimc.vialegacy.protocol.alpha.a1_0_15toa1_0_16_2.packet.ClientboundPacketsa1_0_15;
import net.raphimc.vialegacy.protocol.beta.b1_7_0_3tob1_8_0_1.types.Typesb1_7_0_3;
import net.raphimc.vialegacy.protocol.classic.c0_28_30toa1_0_15.Protocolc0_28_30Toa1_0_15;
import net.raphimc.vialegacy.protocol.release.r1_2_1_3tor1_2_4_5.Protocolr1_2_1_3Tor1_2_4_5;
import net.raphimc.vialegacy.protocol.release.r1_2_4_5tor1_3_1_2.packet.ClientboundPackets1_2_4;

public class ClassicOpLevelStorage extends StoredObject {

    private byte opLevel;

    private final boolean haxEnabled;
    private boolean flying = false;
    private boolean noClip = false;
    private boolean speed = false;
    private boolean respawn = false;

    public ClassicOpLevelStorage(final UserConnection user, final boolean haxEnabled) {
        super(user);
        this.haxEnabled = haxEnabled;

        if (haxEnabled) {
            this.flying = true;
            this.noClip = true;
            this.speed = true;
            this.respawn = true;
        }
    }

    public void updateHax(final boolean flying, final boolean noClip, final boolean speed, final boolean respawn) {
        if (!this.haxEnabled) return;

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

            final PacketWrapper chatMessage = PacketWrapper.create(ClientboundPacketsa1_0_15.CHAT, this.getUser());
            chatMessage.write(Typesb1_7_0_3.STRING, statusMessage); // message
            chatMessage.send(Protocolc0_28_30Toa1_0_15.class);
        }
    }

    public void setOpLevel(final byte opLevel) {
        final boolean changed = this.opLevel != opLevel;
        this.opLevel = opLevel;

        if (this.haxEnabled) {
            final ClassicServerTitleStorage serverTitleStorage = this.getUser().get(ClassicServerTitleStorage.class);
            this.updateHax(serverTitleStorage.isFlyEffectivelyEnabled(), serverTitleStorage.isNoclipEffectivelyEnabled(), serverTitleStorage.isSpeedEffectivelyEnabled(), serverTitleStorage.isRespawnEffectivelyEnabled());
            if (changed) {
                this.updateAbilities();
            }
        }
    }

    public byte getOpLevel() {
        return this.opLevel;
    }

    public void updateAbilities() {
        if (this.getUser().getProtocolInfo().getPipeline().contains(Protocolr1_2_1_3Tor1_2_4_5.class)) {
            final PacketWrapper playerAbilities = PacketWrapper.create(ClientboundPackets1_2_4.PLAYER_ABILITIES, this.getUser());
            playerAbilities.write(Types.BOOLEAN, true); // invulnerable
            playerAbilities.write(Types.BOOLEAN, false); // flying
            playerAbilities.write(Types.BOOLEAN, this.flying); // allow flying
            playerAbilities.write(Types.BOOLEAN, true); // creative mode
            playerAbilities.scheduleSend(Protocolr1_2_1_3Tor1_2_4_5.class);
        }
    }

}
