/*
 * This file is part of ViaLegacy - https://github.com/RaphiMC/ViaLegacy
 * Copyright (C) 2020-2025 RK_01/RaphiMC and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.raphimc.vialegacy.protocol.release.r1_6_4tor1_7_2_5.storage;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.chunks.DataPalette;
import com.viaversion.viaversion.util.IdAndData;
import net.raphimc.vialegacy.api.LegacyProtocolVersion;
import net.raphimc.vialegacy.api.data.BlockList1_6;
import net.raphimc.vialegacy.api.remapper.AbstractChunkTracker;

import java.util.HashMap;
import java.util.Map;

public class ChunkTracker extends AbstractChunkTracker {

    private static final Map<Integer, Boolean> SOLID_BLOCKS = new HashMap<>();
    private final boolean b173;

    static {
        SOLID_BLOCKS.put(0, false);
        SOLID_BLOCKS.put(1, true);
        SOLID_BLOCKS.put(2, true);
        SOLID_BLOCKS.put(3, true);
        SOLID_BLOCKS.put(4, true);
        SOLID_BLOCKS.put(5, true);
        SOLID_BLOCKS.put(6, false);
        SOLID_BLOCKS.put(7, true);
        SOLID_BLOCKS.put(8, false);
        SOLID_BLOCKS.put(9, false);
        SOLID_BLOCKS.put(10, false);
        SOLID_BLOCKS.put(11, false);
        SOLID_BLOCKS.put(12, true);
        SOLID_BLOCKS.put(13, true);
        SOLID_BLOCKS.put(14, true);
        SOLID_BLOCKS.put(15, true);
        SOLID_BLOCKS.put(16, true);
        SOLID_BLOCKS.put(17, true);
        SOLID_BLOCKS.put(18, true);
        SOLID_BLOCKS.put(19, true);
        SOLID_BLOCKS.put(20, false);
        SOLID_BLOCKS.put(21, true);
        SOLID_BLOCKS.put(22, true);
        SOLID_BLOCKS.put(23, true);
        SOLID_BLOCKS.put(24, true);
        SOLID_BLOCKS.put(25, true);
        SOLID_BLOCKS.put(26, false);
        SOLID_BLOCKS.put(27, false);
        SOLID_BLOCKS.put(28, false);
        SOLID_BLOCKS.put(29, false);
        SOLID_BLOCKS.put(30, false);
        SOLID_BLOCKS.put(31, false);
        SOLID_BLOCKS.put(32, false);
        SOLID_BLOCKS.put(33, false);
        SOLID_BLOCKS.put(34, false);
        SOLID_BLOCKS.put(35, true);
        SOLID_BLOCKS.put(36, false);
        SOLID_BLOCKS.put(37, false);
        SOLID_BLOCKS.put(38, false);
        SOLID_BLOCKS.put(39, false);
        SOLID_BLOCKS.put(40, false);
        SOLID_BLOCKS.put(41, true);
        SOLID_BLOCKS.put(42, true);
        SOLID_BLOCKS.put(43, true);
        SOLID_BLOCKS.put(44, false);
        SOLID_BLOCKS.put(45, true);
        SOLID_BLOCKS.put(46, true);
        SOLID_BLOCKS.put(47, true);
        SOLID_BLOCKS.put(48, true);
        SOLID_BLOCKS.put(49, true);
        SOLID_BLOCKS.put(50, false);
        SOLID_BLOCKS.put(51, false);
        SOLID_BLOCKS.put(52, false);
        SOLID_BLOCKS.put(53, false);
        SOLID_BLOCKS.put(54, false);
        SOLID_BLOCKS.put(55, false);
        SOLID_BLOCKS.put(56, true);
        SOLID_BLOCKS.put(57, true);
        SOLID_BLOCKS.put(58, true);
        SOLID_BLOCKS.put(59, false);
        SOLID_BLOCKS.put(60, false);
        SOLID_BLOCKS.put(61, true);
        SOLID_BLOCKS.put(62, true);
        SOLID_BLOCKS.put(63, false);
        SOLID_BLOCKS.put(64, false);
        SOLID_BLOCKS.put(65, false);
        SOLID_BLOCKS.put(66, false);
        SOLID_BLOCKS.put(67, false);
        SOLID_BLOCKS.put(68, false);
        SOLID_BLOCKS.put(69, false);
        SOLID_BLOCKS.put(70, false);
        SOLID_BLOCKS.put(71, false);
        SOLID_BLOCKS.put(72, false);
        SOLID_BLOCKS.put(73, true);
        SOLID_BLOCKS.put(74, true);
        SOLID_BLOCKS.put(75, false);
        SOLID_BLOCKS.put(76, false);
        SOLID_BLOCKS.put(77, false);
        SOLID_BLOCKS.put(78, false);
        SOLID_BLOCKS.put(79, false);
        SOLID_BLOCKS.put(80, true);
        SOLID_BLOCKS.put(81, false);
        SOLID_BLOCKS.put(82, true);
        SOLID_BLOCKS.put(83, false);
        SOLID_BLOCKS.put(84, true);
        SOLID_BLOCKS.put(85, false);
        SOLID_BLOCKS.put(86, true);
        SOLID_BLOCKS.put(87, true);
        SOLID_BLOCKS.put(88, true);
        SOLID_BLOCKS.put(89, true);
        SOLID_BLOCKS.put(90, false);
        SOLID_BLOCKS.put(91, true);
        SOLID_BLOCKS.put(92, false);
        SOLID_BLOCKS.put(93, false);
        SOLID_BLOCKS.put(94, false);
        SOLID_BLOCKS.put(95, false);
        SOLID_BLOCKS.put(96, false);
        SOLID_BLOCKS.put(97, true);
        SOLID_BLOCKS.put(98, true);
        SOLID_BLOCKS.put(99, true);
        SOLID_BLOCKS.put(100, true);
        SOLID_BLOCKS.put(101, false);
        SOLID_BLOCKS.put(102, false);
        SOLID_BLOCKS.put(103, true);
        SOLID_BLOCKS.put(104, false);
        SOLID_BLOCKS.put(105, false);
        SOLID_BLOCKS.put(106, false);
        SOLID_BLOCKS.put(107, false);
        SOLID_BLOCKS.put(108, false);
        SOLID_BLOCKS.put(109, false);
        SOLID_BLOCKS.put(110, true);
        SOLID_BLOCKS.put(111, false);
        SOLID_BLOCKS.put(112, true);
        SOLID_BLOCKS.put(113, false);
        SOLID_BLOCKS.put(114, false);
        SOLID_BLOCKS.put(115, false);
        SOLID_BLOCKS.put(116, false);
        SOLID_BLOCKS.put(117, false);
        SOLID_BLOCKS.put(118, false);
        SOLID_BLOCKS.put(119, false);
        SOLID_BLOCKS.put(120, false);
        SOLID_BLOCKS.put(121, true);
        SOLID_BLOCKS.put(122, false);
        SOLID_BLOCKS.put(123, true);
        SOLID_BLOCKS.put(124, true);
        SOLID_BLOCKS.put(125, true);
        SOLID_BLOCKS.put(126, false);
        SOLID_BLOCKS.put(127, false);
        SOLID_BLOCKS.put(128, false);
        SOLID_BLOCKS.put(129, true);
        SOLID_BLOCKS.put(130, false);
        SOLID_BLOCKS.put(131, false);
        SOLID_BLOCKS.put(132, false);
        SOLID_BLOCKS.put(133, true);
        SOLID_BLOCKS.put(134, false);
        SOLID_BLOCKS.put(135, false);
        SOLID_BLOCKS.put(136, false);
        SOLID_BLOCKS.put(137, true);
        SOLID_BLOCKS.put(138, false);
        SOLID_BLOCKS.put(139, false);
        SOLID_BLOCKS.put(140, false);
        SOLID_BLOCKS.put(141, false);
        SOLID_BLOCKS.put(142, false);
        SOLID_BLOCKS.put(143, false);
        SOLID_BLOCKS.put(144, false);
        SOLID_BLOCKS.put(145, false);
        SOLID_BLOCKS.put(146, false);
        SOLID_BLOCKS.put(147, false);
        SOLID_BLOCKS.put(148, false);
        SOLID_BLOCKS.put(149, false);
        SOLID_BLOCKS.put(150, false);
        SOLID_BLOCKS.put(151, false);
        SOLID_BLOCKS.put(152, true);
        SOLID_BLOCKS.put(153, true);
        SOLID_BLOCKS.put(154, false);
        SOLID_BLOCKS.put(155, true);
        SOLID_BLOCKS.put(156, false);
        SOLID_BLOCKS.put(157, false);
        SOLID_BLOCKS.put(158, true);
        SOLID_BLOCKS.put(159, true);
        SOLID_BLOCKS.put(160, false);
        SOLID_BLOCKS.put(161, true);
        SOLID_BLOCKS.put(162, true);
        SOLID_BLOCKS.put(163, false);
        SOLID_BLOCKS.put(164, false);
        SOLID_BLOCKS.put(170, true);
        SOLID_BLOCKS.put(171, false);
        SOLID_BLOCKS.put(172, true);
        SOLID_BLOCKS.put(173, true);
        SOLID_BLOCKS.put(174, true);
        SOLID_BLOCKS.put(175, false);
    }

    public ChunkTracker(final UserConnection user) {
        super(0);
        this.b173 = user.getProtocolInfo().serverProtocolVersion().olderThanOrEqualTo(LegacyProtocolVersion.b1_7tob1_7_3);

        this.registerReplacement(new IdAndData(BlockList1_6.signWall.blockId(), 0), new IdAndData(BlockList1_6.signWall.blockId(), 3));
        for (int i = 0; i < 16; i++) {
            this.registerReplacement(new IdAndData(BlockList1_6.lockedChest.blockId(), i), new IdAndData(BlockList1_6.bedrock.blockId(), 0));
        }
        if (user.getProtocolInfo().serverProtocolVersion().olderThanOrEqualTo(LegacyProtocolVersion.r1_1)) {
            for (int i = 9; i < 16; i++) {
                this.registerReplacement(new IdAndData(BlockList1_6.doorWood.blockId(), i), new IdAndData(BlockList1_6.doorWood.blockId(), 8));
                this.registerReplacement(new IdAndData(BlockList1_6.doorIron.blockId(), i), new IdAndData(BlockList1_6.doorIron.blockId(), 8));
            }
        }

        if (user.getProtocolInfo().serverProtocolVersion().olderThanOrEqualTo(LegacyProtocolVersion.b1_5tob1_5_2)) {
            this.registerReplacement(new IdAndData(BlockList1_6.jukebox.blockId(), 2), new IdAndData(BlockList1_6.jukebox.blockId(), 1));
        }
        if (user.getProtocolInfo().serverProtocolVersion().olderThanOrEqualTo(LegacyProtocolVersion.b1_4tob1_4_1)) {
            for (int i = 1; i < 8; i++) {
                this.registerReplacement(new IdAndData(BlockList1_6.sapling.blockId(), i), new IdAndData(BlockList1_6.sapling.blockId(), 0));
            }
            for (int i = 9; i < 16; i++) {
                this.registerReplacement(new IdAndData(BlockList1_6.sapling.blockId(), i), new IdAndData(BlockList1_6.sapling.blockId(), 8));
            }
        }
        if (user.getProtocolInfo().serverProtocolVersion().olderThanOrEqualTo(LegacyProtocolVersion.b1_1_2)) {
            for (int i = 1; i < 16; i++) {
                this.registerReplacement(new IdAndData(BlockList1_6.leaves.blockId(), i), new IdAndData(BlockList1_6.leaves.blockId(), 0));
            }
        }
    }

    @Override
    protected void remapBlock(final IdAndData block, final int x, final int y, final int z) {
        if (this.b173 && block.getId() == BlockList1_6.chest.blockId()) {
            byte blockData = 3;
            int rot1 = this.getBlockNotNull(x, y, z - 1).getId();
            int rot2 = this.getBlockNotNull(x, y, z + 1).getId();
            int rot3 = this.getBlockNotNull(x - 1, y, z).getId();
            int rot4 = this.getBlockNotNull(x + 1, y, z).getId();

            int rot5;
            if (rot3 == BlockList1_6.chest.blockId()) {
                rot5 = this.getBlockNotNull(x - 1, y, z - 1).getId();
            } else {
                rot5 = this.getBlockNotNull(x + 1, y, z - 1).getId();
            }

            int rot6;
            if (rot3 == BlockList1_6.chest.blockId()) {
                rot6 = this.getBlockNotNull(x - 1, y, z + 1).getId();
            } else {
                rot6 = this.getBlockNotNull(x + 1, y, z + 1).getId();
            }

            if (rot1 != BlockList1_6.chest.blockId() && rot2 != BlockList1_6.chest.blockId()) {
                if (rot3 != BlockList1_6.chest.blockId() && rot4 != BlockList1_6.chest.blockId()) {
                    if (SOLID_BLOCKS.get(rot2) && !SOLID_BLOCKS.get(rot1)) {
                        blockData = 2;
                    }

                    if (SOLID_BLOCKS.get(rot3) && !SOLID_BLOCKS.get(rot4)) {
                        blockData = 5;
                    }

                    if (SOLID_BLOCKS.get(rot4) && !SOLID_BLOCKS.get(rot3)) {
                        blockData = 4;
                    }
                } else {
                    if (SOLID_BLOCKS.get(rot2) || SOLID_BLOCKS.get(rot6)) {
                        if (!SOLID_BLOCKS.get(rot1) && !SOLID_BLOCKS.get(rot5)) {
                            blockData = 2;
                        }
                    }
                }
            } else {
                blockData = 5;

                if (rot1 == BlockList1_6.chest.blockId()) {
                    rot5 = this.getBlockNotNull(x - 1, y, z - 1).getId();
                } else {
                    rot5 = this.getBlockNotNull(x - 1, y, z + 1).getId();
                }

                if (rot1 == BlockList1_6.chest.blockId()) {
                    rot6 = this.getBlockNotNull(x + 1, y, z - 1).getId();
                } else {
                    rot6 = this.getBlockNotNull(x + 1, y, z + 1).getId();
                }

                if (SOLID_BLOCKS.get(rot4) || SOLID_BLOCKS.get(rot6)) {
                    if (!SOLID_BLOCKS.get(rot3) && !SOLID_BLOCKS.get(rot5)) {
                        blockData = 4;
                    }
                }
            }
            block.setData(blockData);
        }
    }

    @Override
    protected void postRemap(DataPalette palette) {
        if (this.b173) {
            palette.replaceId(BlockList1_6.chest.blockId() << 4, 0);
            palette.replaceId(BlockList1_6.chest.blockId() << 4 | 1, 0);
        }
    }

}
