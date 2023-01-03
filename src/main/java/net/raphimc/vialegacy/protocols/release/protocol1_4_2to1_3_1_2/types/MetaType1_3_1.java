package net.raphimc.vialegacy.protocols.release.protocol1_4_2to1_3_1_2.types;

import com.viaversion.viaversion.api.minecraft.metadata.MetaType;
import com.viaversion.viaversion.api.type.Type;
import net.raphimc.vialegacy.protocols.release.protocol1_7_2_5to1_6_4.types.Types1_6_4;

public enum MetaType1_3_1 implements MetaType {

    Byte(0, Type.BYTE),
    Short(1, Type.SHORT),
    Int(2, Type.INT),
    Float(3, Type.FLOAT),
    String(4, Types1_6_4.STRING),
    Slot(5, Types1_3_1.NBTLESS_ITEM),
    Position(6, Type.VECTOR);

    private final int typeID;
    private final Type<?> type;

    MetaType1_3_1(int typeID, Type<?> type) {
        this.typeID = typeID;
        this.type = type;
    }

    public static MetaType1_3_1 byId(int id) {
        return values()[id];
    }

    public int typeId() {
        return this.typeID;
    }

    public Type<?> type() {
        return this.type;
    }

}
