package net.raphimc.vialegacy.protocols.release.protocol1_3_1_2to1_2_4_5.types;

import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.type.Type;
import net.raphimc.vialegacy.protocols.release.protocol1_8to1_7_6_10.types.ItemArrayType;

public class Types1_2_4 {

    public static final Type<Item> ITEM = new ItemType(false);
    public static final Type<Item> COMPRESSED_NBT_ITEM = new ItemType(true);

    public static final Type<Item[]> ITEM_ARRAY = new ItemArrayType<>(ITEM);
    public static final Type<Item[]> COMPRESSED_NBT_ITEM_ARRAY = new ItemArrayType<>(COMPRESSED_NBT_ITEM);

}
