package net.raphimc.vialegacy.protocols.beta.protocolb1_5_0_2tob1_4_0_1.types;

import com.viaversion.viaversion.api.minecraft.metadata.MetaType;
import com.viaversion.viaversion.api.type.types.minecraft.OldMetaType;

public class MetadataType extends OldMetaType {

    @Override
    protected MetaType getType(int index) {
        return MetaTypeb1_4.byId(index);
    }

}
