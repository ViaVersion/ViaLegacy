/*
 * This file is part of ViaProtocolHack - https://github.com/RaphiMC/ViaProtocolHack
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
import net.raphimc.vialegacy.protocols.classic.protocolc0_28_30toc0_28_30cpe.data.ClassicProtocolExtension;

import java.util.EnumMap;

public class ExtensionProtocolMetadataStorage extends StoredObject {

    private String serverSoftwareName = "classic";
    private short extensionCount = -1;
    private short receivedExtensions = 0;

    private final EnumMap<ClassicProtocolExtension, Integer> serverExtensions = new EnumMap<>(ClassicProtocolExtension.class);

    public ExtensionProtocolMetadataStorage(final UserConnection user) {
        super(user);
    }

    public void setServerSoftwareName(final String serverSoftwareName) {
        if (serverSoftwareName.isEmpty()) return;
        this.serverSoftwareName = serverSoftwareName;
    }

    public String getServerSoftwareName() {
        return this.serverSoftwareName;
    }

    public void setExtensionCount(final short extensionCount) {
        this.extensionCount = extensionCount;
    }

    public short getExtensionCount() {
        return this.extensionCount;
    }

    public void incrementReceivedExtensions() {
        this.receivedExtensions++;
    }

    public short getReceivedExtensions() {
        return this.receivedExtensions;
    }

    public void addServerExtension(final ClassicProtocolExtension extension, final int version) {
        this.serverExtensions.put(extension, version);
    }

    public boolean hasServerExtension(final ClassicProtocolExtension extension, final int... versions) {
        final Integer extensionVersion = this.serverExtensions.get(extension);
        if (extensionVersion == null) return false;
        if (versions.length == 0) return true;

        for (int version : versions) {
            if (version == extensionVersion) return true;
        }
        return false;
    }

}
