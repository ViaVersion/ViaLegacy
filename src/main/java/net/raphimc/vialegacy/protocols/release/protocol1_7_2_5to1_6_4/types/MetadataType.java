package net.raphimc.vialegacy.protocols.release.protocol1_7_2_5to1_6_4.types;

import com.viaversion.viaversion.api.minecraft.metadata.MetaType;
import com.viaversion.viaversion.api.type.types.minecraft.OldMetaType;

public class MetadataType extends OldMetaType {

    @Override
    protected MetaType getType(int index) {
        return MetaType1_6_4.byId(index);
    }

}
