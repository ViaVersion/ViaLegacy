package net.raphimc.vialegacy.protocols.release.protocol1_8to1_7_6_10.types;

import com.viaversion.viaversion.api.minecraft.metadata.MetaType;
import com.viaversion.viaversion.api.type.Type;

public enum MetaType1_7_6 implements MetaType {

    Byte(0, Type.BYTE),
    Short(1, Type.SHORT),
    Int(2, Type.INT),
    Float(3, Type.FLOAT),
    String(4, Type.STRING),
    Slot(5, Types1_7_6.COMPRESSED_ITEM),
    Position(6, Type.VECTOR);

    private final int typeID;
    private final Type<?> type;

    MetaType1_7_6(int typeID, Type<?> type) {
        this.typeID = typeID;
        this.type = type;
    }

    public static MetaType1_7_6 byId(int id) {
        return values()[id];
    }

    public int typeId() {
        return this.typeID;
    }

    public Type<?> type() {
        return this.type;
    }

}
