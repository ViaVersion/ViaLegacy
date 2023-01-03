package net.raphimc.vialegacy.protocols.beta.protocol1_0_0_1tob1_8_0_1.types;

import com.viaversion.viaversion.api.minecraft.item.DataItem;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.type.Type;
import io.netty.buffer.ByteBuf;

public class NbtLessItemType extends Type<Item> {

    public NbtLessItemType() {
        super(Item.class);
    }

    public Item read(ByteBuf buffer) throws Exception {
        final short id = buffer.readShort();
        if (id < 0) {
            return null;
        } else {
            final Item item = new DataItem();
            item.setIdentifier(id);
            item.setAmount((byte) buffer.readShort());
            item.setData(buffer.readShort());
            return item;
        }
    }

    public void write(ByteBuf buffer, Item item) throws Exception {
        if (item == null) {
            buffer.writeShort(-1);
            buffer.writeShort(0);
            buffer.writeShort(0);
        } else {
            buffer.writeShort(item.identifier());
            buffer.writeShort(item.amount());
            buffer.writeShort(item.data());
        }
    }

}
