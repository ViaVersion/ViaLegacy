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
package net.raphimc.vialegacy.protocols.classic.protocolc0_28_30toc0_28_30cpe.storage;

import com.viaversion.viaversion.api.connection.StoredObject;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.type.Type;
import net.raphimc.vialegacy.protocols.release.protocol1_2_4_5to1_2_1_3.Protocol1_2_4_5to1_2_1_3;
import net.raphimc.vialegacy.protocols.release.protocol1_3_1_2to1_2_4_5.ClientboundPackets1_2_4;

public class ExtHackControlStorage extends StoredObject {

    public boolean flying = true;
    public boolean noClip = true;
    public boolean speed = true;
    public boolean respawn = true;
    public boolean thirdPerson = true;
    public float jumpHeight = 1.233F;

    public ExtHackControlStorage(final UserConnection user) {
        super(user);
    }

    public boolean update(final boolean flying, final boolean noClip, final boolean speed, final boolean respawn, final boolean thirdPerson, final short jumpHeight) throws Exception {
        float calculatedJumpHeight = jumpHeight / 32F;
        if (calculatedJumpHeight <= 0) calculatedJumpHeight = 1.233F;

        if (this.flying != flying && this.getUser().getProtocolInfo().getPipeline().contains(Protocol1_2_4_5to1_2_1_3.class)) {
            final PacketWrapper playerAbilities = PacketWrapper.create(ClientboundPackets1_2_4.PLAYER_ABILITIES, this.getUser());
            playerAbilities.write(Type.BOOLEAN, true); // invulnerable
            playerAbilities.write(Type.BOOLEAN, false); // flying
            playerAbilities.write(Type.BOOLEAN, flying); // allow flying
            playerAbilities.write(Type.BOOLEAN, true); // creative mode
            playerAbilities.send(Protocol1_2_4_5to1_2_1_3.class);
        }

        boolean changed = this.flying != flying;
        changed |= this.noClip != noClip;
        changed |= this.speed != speed;
        changed |= this.respawn != respawn;
        changed |= this.thirdPerson != thirdPerson;
        changed |= this.jumpHeight != calculatedJumpHeight;

        this.flying = flying;
        this.noClip = noClip;
        this.speed = speed;
        this.respawn = respawn;
        this.thirdPerson = thirdPerson;
        this.jumpHeight = calculatedJumpHeight;

        return changed;
    }

}
