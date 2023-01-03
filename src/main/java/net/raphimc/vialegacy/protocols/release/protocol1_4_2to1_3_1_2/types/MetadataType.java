package net.raphimc.vialegacy.protocols.release.protocol1_4_2to1_3_1_2.types;

import com.viaversion.viaversion.api.minecraft.metadata.MetaType;
import com.viaversion.viaversion.api.type.types.minecraft.OldMetaType;

public class MetadataType extends OldMetaType {

    @Override
    protected MetaType getType(int index) {
        return MetaType1_3_1.byId(index);
    }

}
