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
package net.raphimc.vialegacy;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.protocol.ProtocolManagerImpl;
import net.raphimc.vialegacy.platform.ViaLegacyPlatform;

import java.io.File;
import java.util.logging.Logger;

public class ViaLegacyPlatformImpl implements ViaLegacyPlatform {

    private final Logger logger;

    public ViaLegacyPlatformImpl() {
        this.logger = Via.getPlatform().createLogger("ViaLegacy");
        this.init(new File(this.getDataFolder(), "vialegacy.yml"));

        Via.getManager().addPostEnableListener(() -> {
            Via.getManager().getProtocolManager().setMaxProtocolPathSize(Integer.MAX_VALUE);
            Via.getManager().getProtocolManager().setMaxPathDeltaIncrease(-1);
            ((ProtocolManagerImpl) Via.getManager().getProtocolManager()).refreshVersions();
        });
    }

    @Override
    public Logger getLogger() {
        return this.logger;
    }

    @Override
    public File getDataFolder() {
        return Via.getPlatform().getDataFolder();
    }

}
