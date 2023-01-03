package net.raphimc.vialegacy.protocols.beta.protocolb1_3_0_1tob1_2_0_2.types;

import com.viaversion.viaversion.api.minecraft.metadata.MetaType;
import com.viaversion.viaversion.api.type.types.minecraft.OldMetaType;

public class MetadataType extends OldMetaType {

    @Override
    protected MetaType getType(int index) {
        return MetaTypeb1_2.byId(index);
    }

}
