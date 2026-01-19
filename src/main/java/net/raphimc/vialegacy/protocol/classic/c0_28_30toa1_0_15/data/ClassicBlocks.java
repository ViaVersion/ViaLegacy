/*
 * This file is part of ViaLegacy - https://github.com/RaphiMC/ViaLegacy
 * Copyright (C) 2020-2026 RK_01/RaphiMC and contributors
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

package net.raphimc.vialegacy.protocol.classic.c0_28_30toa1_0_15.data;

import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectMap;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectOpenHashMap;
import com.viaversion.viaversion.libs.fastutil.objects.Object2IntMap;
import com.viaversion.viaversion.libs.fastutil.objects.Object2IntOpenHashMap;
import com.viaversion.viaversion.util.IdAndData;
import net.raphimc.vialegacy.api.data.BlockList1_6;
import net.raphimc.vialegacy.api.data.ItemList1_6;

public class ClassicBlocks {

    public static final int AIR = 0;
    public static final int STONE = 1;
    public static final int GRASS = 2;
    public static final int DIRT = 3;
    public static final int COBBLESTONE = 4;
    public static final int WOOD = 5;
    public static final int SAPLING = 6;
    public static final int BEDROCK = 7;
    public static final int WATER = 8;
    public static final int STATIONARY_WATER = 9;
    public static final int LAVA = 10;
    public static final int STATIONARY_LAVA = 11;
    public static final int SAND = 12;
    public static final int GRAVEL = 13;
    public static final int GOLD_ORE = 14;
    public static final int IRON_ORE = 15;
    public static final int COAL_ORE = 16;
    public static final int LOG = 17;
    public static final int LEAVES = 18;
    public static final int SPONGE = 19;
    public static final int GLASS = 20;
    public static final int RED_WOOL = 21;
    public static final int ORANGE_WOOL = 22;
    public static final int YELLOW_WOOL = 23;
    public static final int LIME_WOOL = 24;
    public static final int GREEN_WOOL = 25;
    public static final int TEAL_WOOL = 26;
    public static final int AQUA_BLUE_WOOL = 27;
    public static final int CYAN_WOOL = 28;
    public static final int BLUE_WOOL = 29;
    public static final int INDIGO_WOOL = 30;
    public static final int VIOLET_WOOL = 31;
    public static final int MAGENTA_WOOL = 32;
    public static final int PINK_WOOL = 33;
    public static final int BLACK_WOOL = 34;
    public static final int GRAY_WOOL = 35;
    public static final int WHITE_WOOL = 36;
    public static final int DANDELION = 37;
    public static final int ROSE = 38;
    public static final int BROWN_MUSHROOM = 39;
    public static final int RED_MUSHROOM = 40;
    public static final int GOLD_BLOCK = 41;
    public static final int IRON_BLOCK = 42;
    public static final int DOUBLE_SLAB = 43;
    public static final int SLAB = 44;
    public static final int BRICK = 45;
    public static final int TNT = 46;
    public static final int BOOKSHELF = 47;
    public static final int MOSSY_COBBLESTONE = 48;
    public static final int OBSIDIAN = 49;

    public static Int2ObjectMap<IdAndData> MAPPING = new Int2ObjectOpenHashMap<>(50, 0.99F);
    public static Object2IntMap<IdAndData> REVERSE_MAPPING = new Object2IntOpenHashMap<>(42, 0.99F);

    static {
        MAPPING.defaultReturnValue(new IdAndData(BlockList1_6.stone.blockId(), 0));
        REVERSE_MAPPING.defaultReturnValue(STONE);

        MAPPING.put(AIR, new IdAndData(0, 0));
        MAPPING.put(STONE, new IdAndData(BlockList1_6.stone.blockId(), 0));
        MAPPING.put(GRASS, new IdAndData(BlockList1_6.grass.blockId(), 0));
        MAPPING.put(DIRT, new IdAndData(BlockList1_6.dirt.blockId(), 0));
        MAPPING.put(COBBLESTONE, new IdAndData(BlockList1_6.cobblestone.blockId(), 0));
        MAPPING.put(WOOD, new IdAndData(BlockList1_6.planks.blockId(), 0));
        MAPPING.put(SAPLING, new IdAndData(BlockList1_6.sapling.blockId(), 0));
        MAPPING.put(BEDROCK, new IdAndData(BlockList1_6.bedrock.blockId(), 0));
        MAPPING.put(WATER, new IdAndData(BlockList1_6.waterMoving.blockId(), 0));
        MAPPING.put(STATIONARY_WATER, new IdAndData(BlockList1_6.waterStill.blockId(), 0));
        MAPPING.put(LAVA, new IdAndData(BlockList1_6.lavaMoving.blockId(), 0));
        MAPPING.put(STATIONARY_LAVA, new IdAndData(BlockList1_6.lavaStill.blockId(), 0));
        MAPPING.put(SAND, new IdAndData(BlockList1_6.sand.blockId(), 0));
        MAPPING.put(GRAVEL, new IdAndData(BlockList1_6.gravel.blockId(), 0));
        MAPPING.put(GOLD_ORE, new IdAndData(BlockList1_6.oreGold.blockId(), 0));
        MAPPING.put(IRON_ORE, new IdAndData(BlockList1_6.oreIron.blockId(), 0));
        MAPPING.put(COAL_ORE, new IdAndData(BlockList1_6.oreCoal.blockId(), 0));
        MAPPING.put(LOG, new IdAndData(BlockList1_6.wood.blockId(), 0));
        MAPPING.put(LEAVES, new IdAndData(BlockList1_6.leaves.blockId(), 0));
        MAPPING.put(SPONGE, new IdAndData(BlockList1_6.sponge.blockId(), 0));
        MAPPING.put(GLASS, new IdAndData(BlockList1_6.glass.blockId(), 0));
        MAPPING.put(RED_WOOL, new IdAndData(BlockList1_6.cloth.blockId(), 14));
        MAPPING.put(ORANGE_WOOL, new IdAndData(BlockList1_6.cloth.blockId(), 1));
        MAPPING.put(YELLOW_WOOL, new IdAndData(BlockList1_6.cloth.blockId(), 4));
        MAPPING.put(LIME_WOOL, new IdAndData(BlockList1_6.cloth.blockId(), 5));
        MAPPING.put(GREEN_WOOL, new IdAndData(BlockList1_6.cloth.blockId(), 13));
        MAPPING.put(TEAL_WOOL, new IdAndData(BlockList1_6.cloth.blockId(), 5));
        MAPPING.put(AQUA_BLUE_WOOL, new IdAndData(BlockList1_6.cloth.blockId(), 3));
        MAPPING.put(CYAN_WOOL, new IdAndData(BlockList1_6.cloth.blockId(), 9));
        MAPPING.put(BLUE_WOOL, new IdAndData(BlockList1_6.cloth.blockId(), 11));
        MAPPING.put(INDIGO_WOOL, new IdAndData(BlockList1_6.cloth.blockId(), 10));
        MAPPING.put(VIOLET_WOOL, new IdAndData(BlockList1_6.cloth.blockId(), 2));
        MAPPING.put(MAGENTA_WOOL, new IdAndData(BlockList1_6.cloth.blockId(), 2));
        MAPPING.put(PINK_WOOL, new IdAndData(BlockList1_6.cloth.blockId(), 6));
        MAPPING.put(BLACK_WOOL, new IdAndData(BlockList1_6.cloth.blockId(), 7));
        MAPPING.put(GRAY_WOOL, new IdAndData(BlockList1_6.cloth.blockId(), 8));
        MAPPING.put(WHITE_WOOL, new IdAndData(BlockList1_6.cloth.blockId(), 0));
        MAPPING.put(DANDELION, new IdAndData(BlockList1_6.plantYellow.blockId(), 0));
        MAPPING.put(ROSE, new IdAndData(BlockList1_6.plantRed.blockId(), 0));
        MAPPING.put(BROWN_MUSHROOM, new IdAndData(BlockList1_6.mushroomBrown.blockId(), 0));
        MAPPING.put(RED_MUSHROOM, new IdAndData(BlockList1_6.mushroomRed.blockId(), 0));
        MAPPING.put(GOLD_BLOCK, new IdAndData(BlockList1_6.blockGold.blockId(), 0));
        MAPPING.put(IRON_BLOCK, new IdAndData(BlockList1_6.blockIron.blockId(), 0));
        MAPPING.put(DOUBLE_SLAB, new IdAndData(BlockList1_6.stoneDoubleSlab.blockId(), 0));
        MAPPING.put(SLAB, new IdAndData(BlockList1_6.stoneSingleSlab.blockId(), 0));
        MAPPING.put(BRICK, new IdAndData(BlockList1_6.brick.blockId(), 0));
        MAPPING.put(TNT, new IdAndData(BlockList1_6.tnt.blockId(), 0));
        MAPPING.put(BOOKSHELF, new IdAndData(BlockList1_6.bookShelf.blockId(), 0));
        MAPPING.put(MOSSY_COBBLESTONE, new IdAndData(BlockList1_6.cobblestoneMossy.blockId(), 0));
        MAPPING.put(OBSIDIAN, new IdAndData(BlockList1_6.obsidian.blockId(), 0));

        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.stone.blockId(), 0), STONE);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.grass.blockId(), 0), GRASS); // normally not placeable
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.dirt.blockId(), 0), DIRT);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.cobblestone.blockId(), 0), COBBLESTONE);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.planks.blockId(), 0), WOOD);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.sapling.blockId(), 0), SAPLING);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.bedrock.blockId(), 0), BEDROCK); // normally not placeable
        REVERSE_MAPPING.put(new IdAndData(ItemList1_6.bucketWater.itemId(), 0), STATIONARY_WATER); // normally not placeable
        REVERSE_MAPPING.put(new IdAndData(ItemList1_6.bucketLava.itemId(), 0), STATIONARY_LAVA); // normally not placeable
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.sand.blockId(), 0), SAND);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.gravel.blockId(), 0), GRAVEL);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.oreGold.blockId(), 0), GOLD_ORE);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.oreIron.blockId(), 0), IRON_ORE);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.oreCoal.blockId(), 0), COAL_ORE);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.wood.blockId(), 0), LOG);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.leaves.blockId(), 0), LEAVES);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.sponge.blockId(), 0), SPONGE);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.glass.blockId(), 0), GLASS);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.plantYellow.blockId(), 0), DANDELION);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.plantRed.blockId(), 0), ROSE);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.mushroomBrown.blockId(), 0), BROWN_MUSHROOM);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.mushroomRed.blockId(), 0), RED_MUSHROOM);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.cloth.blockId(), 14), RED_WOOL);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.cloth.blockId(), 1), ORANGE_WOOL);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.cloth.blockId(), 4), YELLOW_WOOL);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.cloth.blockId(), 5), LIME_WOOL);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.cloth.blockId(), 13), GREEN_WOOL);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.cloth.blockId(), 3), AQUA_BLUE_WOOL);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.cloth.blockId(), 9), CYAN_WOOL);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.cloth.blockId(), 11), BLUE_WOOL);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.cloth.blockId(), 10), INDIGO_WOOL);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.cloth.blockId(), 2), MAGENTA_WOOL);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.cloth.blockId(), 6), PINK_WOOL);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.cloth.blockId(), 7), BLACK_WOOL);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.cloth.blockId(), 8), GRAY_WOOL);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.cloth.blockId(), 0), WHITE_WOOL);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.blockGold.blockId(), 0), GOLD_BLOCK);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.blockIron.blockId(), 0), IRON_BLOCK);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.stoneSingleSlab.blockId(), 0), SLAB);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.brick.blockId(), 0), BRICK);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.tnt.blockId(), 0), TNT);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.bookShelf.blockId(), 0), BOOKSHELF);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.cobblestoneMossy.blockId(), 0), MOSSY_COBBLESTONE);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.obsidian.blockId(), 0), OBSIDIAN);
    }

}
