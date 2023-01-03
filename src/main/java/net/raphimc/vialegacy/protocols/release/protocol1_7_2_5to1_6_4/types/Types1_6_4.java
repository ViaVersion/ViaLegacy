package net.raphimc.vialegacy.protocols.release.protocol1_7_2_5to1_6_4.types;

import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.type.types.minecraft.MetaListType;

import java.util.List;

public class Types1_6_4 {

    public static final Type<String> STRING = new StringType();

    public static final Type<Metadata> METADATA = new MetadataType();
    public static final Type<List<Metadata>> METADATA_LIST = new MetaListType(METADATA);

}
