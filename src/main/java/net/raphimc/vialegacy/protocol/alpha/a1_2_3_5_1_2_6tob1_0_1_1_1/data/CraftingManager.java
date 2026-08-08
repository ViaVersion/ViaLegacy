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
package net.raphimc.vialegacy.protocol.alpha.a1_2_3_5_1_2_6tob1_0_1_1_1.data;

import com.viaversion.viaversion.api.minecraft.item.DataItem;
import com.viaversion.viaversion.api.minecraft.item.Item;
import net.raphimc.vialegacy.api.data.BlockList1_6;
import net.raphimc.vialegacy.api.data.ItemList1_6;
import net.raphimc.vialegacy.protocol.alpha.a1_2_3_5_1_2_6tob1_0_1_1_1.model.CraftingRecipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class CraftingManager {

    private static final List<CraftingRecipe> RECIPES = new ArrayList<>();

    private static final String[][] TOOLS_PATTERN = new String[][]{{"XXX", " # ", " # "}, {"X", "#", "#"}, {"XX", "X#", " #"}, {"XX", " #", " #"}};
    private static final int[][] TOOLS_INGREDIENTS = new int[][]{{BlockList1_6.PLANKS, BlockList1_6.COBBLESTONE, ItemList1_6.INGOT_IRON, ItemList1_6.DIAMOND, ItemList1_6.INGOT_GOLD}, {ItemList1_6.PICKAXE_WOOD, ItemList1_6.PICKAXE_STONE, ItemList1_6.PICKAXE_IRON, ItemList1_6.PICKAXE_DIAMOND, ItemList1_6.PICKAXE_GOLD}, {ItemList1_6.SHOVEL_WOOD, ItemList1_6.SHOVEL_STONE, ItemList1_6.SHOVEL_IRON, ItemList1_6.SHOVEL_DIAMOND, ItemList1_6.SHOVEL_GOLD}, {ItemList1_6.AXE_WOOD, ItemList1_6.AXE_STONE, ItemList1_6.AXE_IRON, ItemList1_6.AXE_DIAMOND, ItemList1_6.AXE_GOLD}, {ItemList1_6.HOE_WOOD, ItemList1_6.HOE_STONE, ItemList1_6.HOE_IRON, ItemList1_6.HOE_DIAMOND, ItemList1_6.HOE_GOLD}};

    private static final String[][] WEAPONS_PATTERN = new String[][]{{"X", "X", "#"}};
    private static final int[][] WEAPONS_INGREDIENTS = new int[][]{{BlockList1_6.PLANKS, BlockList1_6.COBBLESTONE, ItemList1_6.INGOT_IRON, ItemList1_6.DIAMOND, ItemList1_6.INGOT_GOLD}, {ItemList1_6.SWORD_WOOD, ItemList1_6.SWORD_STONE, ItemList1_6.SWORD_IRON, ItemList1_6.SWORD_DIAMOND, ItemList1_6.SWORD_GOLD}};

    private static final int[][] INGOTS_INGREDIENTS = new int[][]{{BlockList1_6.BLOCK_GOLD, ItemList1_6.INGOT_GOLD}, {BlockList1_6.BLOCK_IRON, ItemList1_6.INGOT_IRON}, {BlockList1_6.BLOCK_DIAMOND, ItemList1_6.DIAMOND}};

    private static final String[][] ARMOR_PATTERN = new String[][]{{"XXX", "X X"}, {"X X", "XXX", "XXX"}, {"XXX", "X X", "X X"}, {"X X", "X X"}};
    private static final int[][] ARMOR_INGREDIENTS = new int[][]{{ItemList1_6.LEATHER, BlockList1_6.FIRE, ItemList1_6.INGOT_IRON, ItemList1_6.DIAMOND, ItemList1_6.INGOT_GOLD}, {ItemList1_6.HELMET_LEATHER, ItemList1_6.HELMET_CHAIN, ItemList1_6.HELMET_IRON, ItemList1_6.HELMET_DIAMOND, ItemList1_6.HELMET_GOLD}, {ItemList1_6.PLATE_LEATHER, ItemList1_6.PLATE_CHAIN, ItemList1_6.PLATE_IRON, ItemList1_6.PLATE_DIAMOND, ItemList1_6.PLATE_GOLD}, {ItemList1_6.LEGS_LEATHER, ItemList1_6.LEGS_CHAIN, ItemList1_6.LEGS_IRON, ItemList1_6.LEGS_DIAMOND, ItemList1_6.LEGS_GOLD}, {ItemList1_6.BOOTS_LEATHER, ItemList1_6.BOOTS_CHAIN, ItemList1_6.BOOTS_IRON, ItemList1_6.BOOTS_DIAMOND, ItemList1_6.BOOTS_GOLD}};

    static {
        for (int i = 0; i < TOOLS_INGREDIENTS[0].length; ++i) {
            for (int i1 = 0; i1 < TOOLS_INGREDIENTS.length - 1; ++i1) {
                addRecipe(new DataItem(TOOLS_INGREDIENTS[i1 + 1][i], (byte) 1, (short) 0, null), TOOLS_PATTERN[i1], '#', ItemList1_6.STICK, 'X', TOOLS_INGREDIENTS[0][i]);
            }
        }
        for (int i = 0; i < WEAPONS_INGREDIENTS[0].length; ++i) {
            for (int i1 = 0; i1 < WEAPONS_INGREDIENTS.length - 1; ++i1) {
                addRecipe(new DataItem(WEAPONS_INGREDIENTS[i1 + 1][i], (byte) 1, (short) 0, null), WEAPONS_PATTERN[i1], '#', ItemList1_6.STICK, 'X', WEAPONS_INGREDIENTS[0][i]);
            }
        }
        addRecipe(new DataItem(ItemList1_6.BOW, (byte) 1, (short) 0, null), " #X", "# X", " #X", 'X', ItemList1_6.SILK, '#', ItemList1_6.STICK);
        addRecipe(new DataItem(ItemList1_6.ARROW, (byte) 4, (short) 0, null), "X", "#", "Y", 'Y', ItemList1_6.FEATHER, 'X', ItemList1_6.FLINT, '#', ItemList1_6.STICK);
        for (int[] ingotsIngredient : INGOTS_INGREDIENTS) {
            addRecipe(new DataItem(ingotsIngredient[0], (byte) 1, (short) 0, null), "###", "###", "###", '#', ingotsIngredient[1]);
            addRecipe(new DataItem(ingotsIngredient[1], (byte) 9, (short) 0, null), "#", '#', ingotsIngredient[0]);
        }
        addRecipe(new DataItem(ItemList1_6.BOWL_SOUP, (byte) 1, (short) 0, null), "Y", "X", "#", 'X', BlockList1_6.MUSHROOM_BROWN, 'Y', BlockList1_6.MUSHROOM_RED, '#', ItemList1_6.BOWL_EMPTY);
        addRecipe(new DataItem(ItemList1_6.BOWL_SOUP, (byte) 1, (short) 0, null), "Y", "X", "#", 'X', BlockList1_6.MUSHROOM_RED, 'Y', BlockList1_6.MUSHROOM_BROWN, '#', ItemList1_6.BOWL_EMPTY);
        addRecipe(new DataItem(BlockList1_6.CHEST, (byte) 1, (short) 0, null), "###", "# #", "###", '#', BlockList1_6.PLANKS);
        addRecipe(new DataItem(BlockList1_6.FURNACE_IDLE, (byte) 1, (short) 0, null), "###", "# #", "###", '#', BlockList1_6.COBBLESTONE);
        addRecipe(new DataItem(BlockList1_6.WORKBENCH, (byte) 1, (short) 0, null), "##", "##", '#', BlockList1_6.PLANKS);
        for (int i = 0; i < ARMOR_INGREDIENTS[0].length; ++i) {
            for (int i1 = 0; i1 < ARMOR_INGREDIENTS.length - 1; ++i1) {
                addRecipe(new DataItem(ARMOR_INGREDIENTS[i1 + 1][i], (byte) 1, (short) 0, null), ARMOR_PATTERN[i1], 'X', ARMOR_INGREDIENTS[0][i]);
            }
        }
        addRecipe(new DataItem(ItemList1_6.PAPER, (byte) 3, (short) 0, null), "###", '#', ItemList1_6.REED);
        addRecipe(new DataItem(ItemList1_6.BOOK, (byte) 1, (short) 0, null), "#", "#", "#", '#', ItemList1_6.PAPER);
        addRecipe(new DataItem(BlockList1_6.FENCE, (byte) 2, (short) 0, null), "###", "###", '#', ItemList1_6.STICK);
        addRecipe(new DataItem(BlockList1_6.JUKEBOX, (byte) 1, (short) 0, null), "###", "#X#", "###", '#', BlockList1_6.PLANKS, 'X', ItemList1_6.DIAMOND);
        addRecipe(new DataItem(BlockList1_6.BOOK_SHELF, (byte) 1, (short) 0, null), "###", "XXX", "###", '#', BlockList1_6.PLANKS, 'X', ItemList1_6.BOOK);
        addRecipe(new DataItem(BlockList1_6.BLOCK_SNOW, (byte) 1, (short) 0, null), "##", "##", '#', ItemList1_6.SNOWBALL);
        addRecipe(new DataItem(BlockList1_6.BLOCK_CLAY, (byte) 1, (short) 0, null), "##", "##", '#', ItemList1_6.CLAY);
        addRecipe(new DataItem(BlockList1_6.BRICK, (byte) 1, (short) 0, null), "##", "##", '#', ItemList1_6.BRICK);
        addRecipe(new DataItem(BlockList1_6.GLOW_STONE, (byte) 1, (short) 0, null), "###", "###", "###", '#', ItemList1_6.GLOWSTONE);
        addRecipe(new DataItem(BlockList1_6.CLOTH, (byte) 1, (short) 0, null), "###", "###", "###", '#', ItemList1_6.SILK);
        addRecipe(new DataItem(BlockList1_6.TNT, (byte) 1, (short) 0, null), "X#X", "#X#", "X#X", 'X', ItemList1_6.GUNPOWDER, '#', BlockList1_6.SAND);
        addRecipe(new DataItem(BlockList1_6.STONE_SINGLE_SLAB, (byte) 3, (short) 0, null), "###", '#', BlockList1_6.COBBLESTONE);
        addRecipe(new DataItem(BlockList1_6.LADDER, (byte) 1, (short) 0, null), "# #", "###", "# #", '#', ItemList1_6.STICK);
        addRecipe(new DataItem(ItemList1_6.DOOR_WOOD, (byte) 1, (short) 0, null), "##", "##", "##", '#', BlockList1_6.PLANKS);
        addRecipe(new DataItem(ItemList1_6.DOOR_IRON, (byte) 1, (short) 0, null), "##", "##", "##", '#', ItemList1_6.INGOT_IRON);
        addRecipe(new DataItem(ItemList1_6.SIGN, (byte) 1, (short) 0, null), "###", "###", " X ", '#', BlockList1_6.PLANKS, 'X', ItemList1_6.STICK);
        addRecipe(new DataItem(BlockList1_6.PLANKS, (byte) 4, (short) 0, null), "#", '#', BlockList1_6.WOOD);
        addRecipe(new DataItem(ItemList1_6.STICK, (byte) 4, (short) 0, null), "#", "#", '#', BlockList1_6.PLANKS);
        addRecipe(new DataItem(BlockList1_6.TORCH_WOOD, (byte) 4, (short) 0, null), "X", "#", 'X', ItemList1_6.COAL, '#', ItemList1_6.STICK);
        addRecipe(new DataItem(ItemList1_6.BOWL_EMPTY, (byte) 4, (short) 0, null), "# #", " # ", '#', BlockList1_6.PLANKS);
        addRecipe(new DataItem(BlockList1_6.RAIL, (byte) 16, (short) 0, null), "X X", "X#X", "X X", 'X', ItemList1_6.INGOT_IRON, '#', ItemList1_6.STICK);
        addRecipe(new DataItem(ItemList1_6.MINECART_EMPTY, (byte) 1, (short) 0, null), "# #", "###", '#', ItemList1_6.INGOT_IRON);
        addRecipe(new DataItem(BlockList1_6.PUMPKIN_LANTERN, (byte) 1, (short) 0, null), "A", "B", 'A', BlockList1_6.PUMPKIN, 'B', BlockList1_6.TORCH_WOOD);
        addRecipe(new DataItem(ItemList1_6.MINECART_CRATE, (byte) 1, (short) 0, null), "A", "B", 'A', BlockList1_6.CHEST, 'B', ItemList1_6.MINECART_EMPTY);
        addRecipe(new DataItem(ItemList1_6.MINECART_POWERED, (byte) 1, (short) 0, null), "A", "B", 'A', BlockList1_6.FURNACE_IDLE, 'B', ItemList1_6.MINECART_EMPTY);
        addRecipe(new DataItem(ItemList1_6.BOAT, (byte) 1, (short) 0, null), "# #", "###", '#', BlockList1_6.PLANKS);
        addRecipe(new DataItem(ItemList1_6.BUCKET_EMPTY, (byte) 1, (short) 0, null), "# #", " # ", '#', ItemList1_6.INGOT_IRON);
        addRecipe(new DataItem(ItemList1_6.FLINT_AND_STEEL, (byte) 1, (short) 0, null), "A ", " B", 'A', ItemList1_6.INGOT_IRON, 'B', ItemList1_6.FLINT);
        addRecipe(new DataItem(ItemList1_6.BREAD, (byte) 1, (short) 0, null), "###", '#', ItemList1_6.WHEAT);
        addRecipe(new DataItem(BlockList1_6.STAIRS_WOOD_OAK, (byte) 4, (short) 0, null), "#  ", "## ", "###", '#', BlockList1_6.PLANKS);
        addRecipe(new DataItem(ItemList1_6.FISHING_ROD, (byte) 1, (short) 0, null), "  #", " #X", "# X", '#', ItemList1_6.STICK, 'X', ItemList1_6.SILK);
        addRecipe(new DataItem(BlockList1_6.STAIRS_COBBLESTONE, (byte) 4, (short) 0, null), "#  ", "## ", "###", '#', BlockList1_6.COBBLESTONE);
        addRecipe(new DataItem(ItemList1_6.PAINTING, (byte) 1, (short) 0, null), "###", "#X#", "###", '#', ItemList1_6.STICK, 'X', BlockList1_6.CLOTH);
        addRecipe(new DataItem(ItemList1_6.APPLE_GOLD, (byte) 1, (short) 0, null), "###", "#X#", "###", '#', BlockList1_6.BLOCK_GOLD, 'X', ItemList1_6.APPLE_RED);
        addRecipe(new DataItem(BlockList1_6.LEVER, (byte) 1, (short) 0, null), "X", "#", '#', BlockList1_6.COBBLESTONE, 'X', ItemList1_6.STICK);
        addRecipe(new DataItem(BlockList1_6.TORCH_REDSTONE_ACTIVE, (byte) 1, (short) 0, null), "X", "#", '#', ItemList1_6.STICK, 'X', ItemList1_6.REDSTONE);
        addRecipe(new DataItem(ItemList1_6.POCKET_SUNDIAL, (byte) 1, (short) 0, null), " # ", "#X#", " # ", '#', ItemList1_6.INGOT_GOLD, 'X', ItemList1_6.REDSTONE);
        addRecipe(new DataItem(ItemList1_6.COMPASS, (byte) 1, (short) 0, null), " # ", "#X#", " # ", '#', ItemList1_6.INGOT_IRON, 'X', ItemList1_6.REDSTONE);
        addRecipe(new DataItem(BlockList1_6.STONE_BUTTON, (byte) 1, (short) 0, null), "#", "#", '#', BlockList1_6.STONE);
        addRecipe(new DataItem(BlockList1_6.PRESSURE_PLATE_STONE, (byte) 1, (short) 0, null), "###", '#', BlockList1_6.STONE);
        addRecipe(new DataItem(BlockList1_6.PRESSURE_PLATE_PLANKS, (byte) 1, (short) 0, null), "###", '#', BlockList1_6.PLANKS);
        RECIPES.sort((o1, o2) -> Integer.compare(o2.getRecipeSize(), o1.getRecipeSize()));
    }

    private CraftingManager() {
    }

    private static void addRecipe(final Item resultItem, final Object... objects) {
        final StringBuilder var3 = new StringBuilder();
        int pos = 0;
        int width = 0;
        int height = 0;

        if (objects[pos] instanceof String[]) {
            final String[] var11 = (String[]) objects[pos++];

            for (String var9 : var11) {
                height++;
                width = var9.length();
                var3.append(var9);
            }
        } else {
            while (objects[pos] instanceof String) {
                final String var7 = (String) objects[pos++];
                height++;
                width = var7.length();
                var3.append(var7);
            }
        }

        final HashMap<Character, Integer> lookup = new HashMap<>();
        while (pos < objects.length) {
            lookup.put((char) objects[pos], (int) objects[pos + 1]);
            pos += 2;
        }

        final int[] ingredientMap = new int[width * height];
        for (int i = 0; i < ingredientMap.length; i++) {
            ingredientMap[i] = lookup.getOrDefault(var3.charAt(i), -1);
        }

        RECIPES.add(new CraftingRecipe(width, height, ingredientMap, resultItem));
    }

    public static Item getResult(final Item[] craftingGrid) {
        final int gridSize = (int) Math.sqrt(craftingGrid.length);
        final int[] ingredientMap = new int[9];
        for (int x = 0; x < 3; ++x) {
            for (int y = 0; y < 3; ++y) {
                int ingredient = -1;
                if (x < gridSize && y < gridSize) {
                    final Item item = craftingGrid[x + y * gridSize];
                    if (item != null) {
                        ingredient = item.identifier();
                    }
                }
                ingredientMap[x + y * 3] = ingredient;
            }
        }

        return getResult(ingredientMap);
    }

    public static Item getResult(final int[] ingredientMap) {
        for (CraftingRecipe recipe : RECIPES) {
            if (recipe.matches(ingredientMap)) {
                return recipe.createResult();
            }
        }

        return null;
    }

}
