package net.raphimc.vialegacy.protocols.release.protocol1_8to1_7_6_10.storage;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.chunks.DataPalette;
import net.raphimc.vialegacy.api.data.BlockList1_6;
import net.raphimc.vialegacy.api.model.IdAndData;
import net.raphimc.vialegacy.api.remapper.AbstractChunkTracker;

public class ChunkTracker extends AbstractChunkTracker {

    public ChunkTracker(final UserConnection user) {
        super(user, BlockList1_6.obsidian.blockID, BlockList1_6.portal.blockID);

        for (int i = 9; i < 16; i++) { // double plant
            this.registerReplacement(new IdAndData(175, i), new IdAndData(175, 0));
        }
        for (int i = 1; i < 16; i++) { // air
            this.registerReplacement(new IdAndData(0, i), new IdAndData(0, 0));
        }
    }

    @Override
    protected void remapBlock(IdAndData block, int x, int y, int z) {
        if (block.id == BlockList1_6.portal.blockID && block.data == 0) {
            if (this.getBlockNotNull(x - 1, y, z).id == BlockList1_6.obsidian.blockID || this.getBlockNotNull(x + 1, y, z).id == BlockList1_6.obsidian.blockID) {
                block.data = 1;
            } else {
                block.data = 2;
            }
        }
    }

    @Override
    protected void postRemap(DataPalette palette) {
        palette.replaceId(BlockList1_6.portal.blockID << 4, 0);
    }

}
