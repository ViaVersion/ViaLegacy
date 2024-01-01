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
package net.raphimc.vialegacy.protocols.classic.protocola1_0_15toc0_28_30.storage;

import com.viaversion.viaversion.api.connection.StoredObject;
import com.viaversion.viaversion.api.connection.UserConnection;

public class ClassicServerTitleStorage extends StoredObject {

    private final String title;
    private final String motd;

    public ClassicServerTitleStorage(UserConnection user, String title, String motd) {
        super(user);
        this.title = title;
        this.motd = motd;
    }

    public String getTitle() {
        return this.title;
    }

    public String getMotd() {
        return this.motd;
    }

    public boolean isHaxEnabled() {
        return this.motd.contains("+hax");
    }

    public boolean isHaxDisabled() {
        return this.motd.contains("-hax");
    }

    public boolean isFlyEnabled() {
        return this.motd.contains("+fly");
    }

    public boolean isFlyDisabled() {
        return this.motd.contains("-fly");
    }

    public boolean isFlyEffectivelyEnabled() {
        final boolean isOp = this.getUser().get(ClassicOpLevelStorage.class).getOpLevel() >= 100;
        return (this.isHaxDisabled() ? this.isFlyEnabled() : !this.isFlyDisabled()) || (isOp && this.isOphaxEnabled());
    }

    public boolean isNoclipEnabled() {
        return this.motd.contains("+noclip");
    }

    public boolean isNoclipDisabled() {
        return this.motd.contains("-noclip");
    }

    public boolean isNoclipEffectivelyEnabled() {
        final boolean isOp = this.getUser().get(ClassicOpLevelStorage.class).getOpLevel() >= 100;
        return (this.isHaxDisabled() ? this.isNoclipEnabled() : !this.isNoclipDisabled()) || (isOp && this.isOphaxEnabled());
    }

    public boolean isRespawnEnabled() {
        return this.motd.contains("+respawn");
    }

    public boolean isRespawnDisabled() {
        return this.motd.contains("-respawn");
    }

    public boolean isRespawnEffectivelyEnabled() {
        final boolean isOp = this.getUser().get(ClassicOpLevelStorage.class).getOpLevel() >= 100;
        return (this.isHaxDisabled() ? this.isRespawnEnabled() : !this.isRespawnDisabled()) || (isOp && this.isOphaxEnabled());
    }

    public boolean isSpeedEnabled() {
        return this.motd.contains("+speed");
    }

    public boolean isSpeedDisabled() {
        return this.motd.contains("-speed");
    }

    public boolean isSpeedEffectivelyEnabled() {
        final boolean isOp = this.getUser().get(ClassicOpLevelStorage.class).getOpLevel() >= 100;
        return (this.isHaxDisabled() ? this.isSpeedEnabled() : !this.isSpeedDisabled()) || (isOp && this.isOphaxEnabled());
    }

    public boolean isOphaxEnabled() {
        return this.motd.contains("+ophax");
    }

}
