package net.raphimc.vialegacy.protocols.release.protocol1_4_2to1_3_1_2.types;

import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.type.types.minecraft.MetaListType;
import net.raphimc.vialegacy.protocols.release.protocol1_8to1_7_6_10.types.ItemArrayType;

import java.util.List;

public class Types1_3_1 {

    public static final Type<Item> NBTLESS_ITEM = new NbtLessItemType();
    public static final Type<Item[]> NBTLESS_ITEM_ARRAY = new ItemArrayType<>(NBTLESS_ITEM);

    public static final Type<Metadata> METADATA = new MetadataType();
    public static final Type<List<Metadata>> METADATA_LIST = new MetaListType(METADATA);

}
