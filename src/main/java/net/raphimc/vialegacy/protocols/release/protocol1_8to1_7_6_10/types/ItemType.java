package net.raphimc.vialegacy.protocols.release.protocol1_8to1_7_6_10.types;

import com.viaversion.viaversion.api.minecraft.item.DataItem;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.type.Type;
import io.netty.buffer.ByteBuf;

public class ItemType extends Type<Item> {

    private final boolean compressed;

    public ItemType(boolean compressed) {
        super(Item.class);
        this.compressed = compressed;
    }

    @Override
    public Item read(ByteBuf buffer) throws Exception {
        final short id = buffer.readShort();
        if (id < 0) {
            return null;
        }

        final Item item = new DataItem();
        item.setIdentifier(id);
        item.setAmount(buffer.readByte());
        item.setData(buffer.readShort());
        item.setTag((this.compressed ? Types1_7_6.COMPRESSED_NBT : Types1_7_6.NBT).read(buffer));
        return item;
    }

    @Override
    public void write(ByteBuf buffer, Item item) throws Exception {
        if (item == null) {
            buffer.writeShort(-1);
        } else {
            buffer.writeShort(item.identifier());
            buffer.writeByte(item.amount());
            buffer.writeShort(item.data());
            (this.compressed ? Types1_7_6.COMPRESSED_NBT : Types1_7_6.NBT).write(buffer, item.tag());
        }
    }

}
