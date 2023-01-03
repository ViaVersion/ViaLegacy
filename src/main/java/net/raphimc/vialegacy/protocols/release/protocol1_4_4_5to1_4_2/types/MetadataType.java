package net.raphimc.vialegacy.protocols.release.protocol1_4_4_5to1_4_2.types;

import com.viaversion.viaversion.api.minecraft.metadata.MetaType;
import com.viaversion.viaversion.api.type.types.minecraft.OldMetaType;

public class MetadataType extends OldMetaType {

    @Override
    protected MetaType getType(int index) {
        return MetaType1_4_2.byId(index);
    }

}
