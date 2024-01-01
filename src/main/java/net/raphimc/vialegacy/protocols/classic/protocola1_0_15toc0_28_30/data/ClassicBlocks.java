/*
 * This file is part of ViaLegacy - https://github.com/RaphiMC/ViaLegacy
 * Copyright (C) 2020-2024 RK_01/RaphiMC and contributors
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
package net.raphimc.vialegacy.protocols.classic.protocola1_0_15toc0_28_30.data;

import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectMap;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectOpenHashMap;
import com.viaversion.viaversion.libs.fastutil.objects.Object2IntMap;
import com.viaversion.viaversion.libs.fastutil.objects.Object2IntOpenHashMap;
import net.raphimc.vialegacy.api.data.BlockList1_6;
import net.raphimc.vialegacy.api.data.ItemList1_6;
import net.raphimc.vialegacy.api.model.IdAndData;

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
        MAPPING.defaultReturnValue(new IdAndData(BlockList1_6.stone.blockID, 0));
        REVERSE_MAPPING.defaultReturnValue(STONE);

        MAPPING.put(AIR, new IdAndData(0, 0));
        MAPPING.put(STONE, new IdAndData(BlockList1_6.stone.blockID, 0));
        MAPPING.put(GRASS, new IdAndData(BlockList1_6.grass.blockID, 0));
        MAPPING.put(DIRT, new IdAndData(BlockList1_6.dirt.blockID, 0));
        MAPPING.put(COBBLESTONE, new IdAndData(BlockList1_6.cobblestone.blockID, 0));
        MAPPING.put(WOOD, new IdAndData(BlockList1_6.planks.blockID, 0));
        MAPPING.put(SAPLING, new IdAndData(BlockList1_6.sapling.blockID, 0));
        MAPPING.put(BEDROCK, new IdAndData(BlockList1_6.bedrock.blockID, 0));
        MAPPING.put(WATER, new IdAndData(BlockList1_6.waterMoving.blockID, 0));
        MAPPING.put(STATIONARY_WATER, new IdAndData(BlockList1_6.waterStill.blockID, 0));
        MAPPING.put(LAVA, new IdAndData(BlockList1_6.lavaMoving.blockID, 0));
        MAPPING.put(STATIONARY_LAVA, new IdAndData(BlockList1_6.lavaStill.blockID, 0));
        MAPPING.put(SAND, new IdAndData(BlockList1_6.sand.blockID, 0));
        MAPPING.put(GRAVEL, new IdAndData(BlockList1_6.gravel.blockID, 0));
        MAPPING.put(GOLD_ORE, new IdAndData(BlockList1_6.oreGold.blockID, 0));
        MAPPING.put(IRON_ORE, new IdAndData(BlockList1_6.oreIron.blockID, 0));
        MAPPING.put(COAL_ORE, new IdAndData(BlockList1_6.oreCoal.blockID, 0));
        MAPPING.put(LOG, new IdAndData(BlockList1_6.wood.blockID, 0));
        MAPPING.put(LEAVES, new IdAndData(BlockList1_6.leaves.blockID, 0));
        MAPPING.put(SPONGE, new IdAndData(BlockList1_6.sponge.blockID, 0));
        MAPPING.put(GLASS, new IdAndData(BlockList1_6.glass.blockID, 0));
        MAPPING.put(RED_WOOL, new IdAndData(BlockList1_6.cloth.blockID, 14));
        MAPPING.put(ORANGE_WOOL, new IdAndData(BlockList1_6.cloth.blockID, 1));
        MAPPING.put(YELLOW_WOOL, new IdAndData(BlockList1_6.cloth.blockID, 4));
        MAPPING.put(LIME_WOOL, new IdAndData(BlockList1_6.cloth.blockID, 5));
        MAPPING.put(GREEN_WOOL, new IdAndData(BlockList1_6.cloth.blockID, 13));
        MAPPING.put(TEAL_WOOL, new IdAndData(BlockList1_6.cloth.blockID, 5));
        MAPPING.put(AQUA_BLUE_WOOL, new IdAndData(BlockList1_6.cloth.blockID, 3));
        MAPPING.put(CYAN_WOOL, new IdAndData(BlockList1_6.cloth.blockID, 9));
        MAPPING.put(BLUE_WOOL, new IdAndData(BlockList1_6.cloth.blockID, 11));
        MAPPING.put(INDIGO_WOOL, new IdAndData(BlockList1_6.cloth.blockID, 10));
        MAPPING.put(VIOLET_WOOL, new IdAndData(BlockList1_6.cloth.blockID, 2));
        MAPPING.put(MAGENTA_WOOL, new IdAndData(BlockList1_6.cloth.blockID, 2));
        MAPPING.put(PINK_WOOL, new IdAndData(BlockList1_6.cloth.blockID, 6));
        MAPPING.put(BLACK_WOOL, new IdAndData(BlockList1_6.cloth.blockID, 7));
        MAPPING.put(GRAY_WOOL, new IdAndData(BlockList1_6.cloth.blockID, 8));
        MAPPING.put(WHITE_WOOL, new IdAndData(BlockList1_6.cloth.blockID, 0));
        MAPPING.put(DANDELION, new IdAndData(BlockList1_6.plantYellow.blockID, 0));
        MAPPING.put(ROSE, new IdAndData(BlockList1_6.plantRed.blockID, 0));
        MAPPING.put(BROWN_MUSHROOM, new IdAndData(BlockList1_6.mushroomBrown.blockID, 0));
        MAPPING.put(RED_MUSHROOM, new IdAndData(BlockList1_6.mushroomRed.blockID, 0));
        MAPPING.put(GOLD_BLOCK, new IdAndData(BlockList1_6.blockGold.blockID, 0));
        MAPPING.put(IRON_BLOCK, new IdAndData(BlockList1_6.blockIron.blockID, 0));
        MAPPING.put(DOUBLE_SLAB, new IdAndData(BlockList1_6.stoneDoubleSlab.blockID, 0));
        MAPPING.put(SLAB, new IdAndData(BlockList1_6.stoneSingleSlab.blockID, 0));
        MAPPING.put(BRICK, new IdAndData(BlockList1_6.brick.blockID, 0));
        MAPPING.put(TNT, new IdAndData(BlockList1_6.tnt.blockID, 0));
        MAPPING.put(BOOKSHELF, new IdAndData(BlockList1_6.bookShelf.blockID, 0));
        MAPPING.put(MOSSY_COBBLESTONE, new IdAndData(BlockList1_6.cobblestoneMossy.blockID, 0));
        MAPPING.put(OBSIDIAN, new IdAndData(BlockList1_6.obsidian.blockID, 0));

        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.stone.blockID, 0), STONE);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.grass.blockID, 0), GRASS); // normally not placeable
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.dirt.blockID, 0), DIRT);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.cobblestone.blockID, 0), COBBLESTONE);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.planks.blockID, 0), WOOD);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.sapling.blockID, 0), SAPLING);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.bedrock.blockID, 0), BEDROCK); // normally not placeable
        REVERSE_MAPPING.put(new IdAndData(ItemList1_6.bucketWater.itemID, 0), STATIONARY_WATER); // normally not placeable
        REVERSE_MAPPING.put(new IdAndData(ItemList1_6.bucketLava.itemID, 0), STATIONARY_LAVA); // normally not placeable
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.sand.blockID, 0), SAND);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.gravel.blockID, 0), GRAVEL);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.oreGold.blockID, 0), GOLD_ORE);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.oreIron.blockID, 0), IRON_ORE);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.oreCoal.blockID, 0), COAL_ORE);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.wood.blockID, 0), LOG);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.leaves.blockID, 0), LEAVES);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.sponge.blockID, 0), SPONGE);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.glass.blockID, 0), GLASS);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.plantYellow.blockID, 0), DANDELION);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.plantRed.blockID, 0), ROSE);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.mushroomBrown.blockID, 0), BROWN_MUSHROOM);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.mushroomRed.blockID, 0), RED_MUSHROOM);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.cloth.blockID, 14), RED_WOOL);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.cloth.blockID, 1), ORANGE_WOOL);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.cloth.blockID, 4), YELLOW_WOOL);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.cloth.blockID, 5), LIME_WOOL);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.cloth.blockID, 13), GREEN_WOOL);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.cloth.blockID, 3), AQUA_BLUE_WOOL);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.cloth.blockID, 9), CYAN_WOOL);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.cloth.blockID, 11), BLUE_WOOL);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.cloth.blockID, 10), INDIGO_WOOL);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.cloth.blockID, 2), MAGENTA_WOOL);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.cloth.blockID, 6), PINK_WOOL);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.cloth.blockID, 7), BLACK_WOOL);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.cloth.blockID, 8), GRAY_WOOL);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.cloth.blockID, 0), WHITE_WOOL);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.blockGold.blockID, 0), GOLD_BLOCK);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.blockIron.blockID, 0), IRON_BLOCK);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.stoneSingleSlab.blockID, 0), SLAB);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.brick.blockID, 0), BRICK);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.tnt.blockID, 0), TNT);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.bookShelf.blockID, 0), BOOKSHELF);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.cobblestoneMossy.blockID, 0), MOSSY_COBBLESTONE);
        REVERSE_MAPPING.put(new IdAndData(BlockList1_6.obsidian.blockID, 0), OBSIDIAN);
    }

}
