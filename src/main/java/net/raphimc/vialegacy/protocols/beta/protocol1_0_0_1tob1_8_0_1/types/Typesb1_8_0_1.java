package net.raphimc.vialegacy.protocols.beta.protocol1_0_0_1tob1_8_0_1.types;

import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.type.Type;
import net.raphimc.vialegacy.protocols.release.protocol1_8to1_7_6_10.types.ItemArrayType;

public class Typesb1_8_0_1 {

    public static final Type<Item> CREATIVE_ITEM = new NbtLessItemType();
    public static final Type<Item[]> CREATIVE_ITEM_ARRAY = new ItemArrayType<>(CREATIVE_ITEM);

}
