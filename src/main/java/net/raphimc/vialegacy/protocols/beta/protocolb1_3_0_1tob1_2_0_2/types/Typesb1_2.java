package net.raphimc.vialegacy.protocols.beta.protocolb1_3_0_1tob1_2_0_2.types;

import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.type.types.minecraft.MetaListType;

import java.util.List;

public class Typesb1_2 {

    public static final Type<Metadata> METADATA = new MetadataType();
    public static final Type<List<Metadata>> METADATA_LIST = new MetaListType(METADATA);

}
