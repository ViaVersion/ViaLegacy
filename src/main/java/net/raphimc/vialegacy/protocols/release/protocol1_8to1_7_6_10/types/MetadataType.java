package net.raphimc.vialegacy.protocols.release.protocol1_8to1_7_6_10.types;

import com.viaversion.viaversion.api.minecraft.metadata.MetaType;
import com.viaversion.viaversion.api.type.types.minecraft.OldMetaType;

public class MetadataType extends OldMetaType {

    @Override
    protected MetaType getType(int index) {
        return MetaType1_7_6.byId(index);
    }

}
