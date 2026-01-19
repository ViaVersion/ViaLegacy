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

public class CraftingManager {

    private static final List<CraftingRecipe> recipes = new ArrayList<>();

    private static final String[][] tools_pattern = new String[][]{{"XXX", " # ", " # "}, {"X", "#", "#"}, {"XX", "X#", " #"}, {"XX", " #", " #"}};
    private static final int[][] tools_ingredients = new int[][]{{BlockList1_6.planks.blockId(), BlockList1_6.cobblestone.blockId(), ItemList1_6.ingotIron.itemId(), ItemList1_6.diamond.itemId(), ItemList1_6.ingotGold.itemId()}, {ItemList1_6.pickaxeWood.itemId(), ItemList1_6.pickaxeStone.itemId(), ItemList1_6.pickaxeIron.itemId(), ItemList1_6.pickaxeDiamond.itemId(), ItemList1_6.pickaxeGold.itemId()}, {ItemList1_6.shovelWood.itemId(), ItemList1_6.shovelStone.itemId(), ItemList1_6.shovelIron.itemId(), ItemList1_6.shovelDiamond.itemId(), ItemList1_6.shovelGold.itemId()}, {ItemList1_6.axeWood.itemId(), ItemList1_6.axeStone.itemId(), ItemList1_6.axeIron.itemId(), ItemList1_6.axeDiamond.itemId(), ItemList1_6.axeGold.itemId()}, {ItemList1_6.hoeWood.itemId(), ItemList1_6.hoeStone.itemId(), ItemList1_6.hoeIron.itemId(), ItemList1_6.hoeDiamond.itemId(), ItemList1_6.hoeGold.itemId()}};

    private static final String[][] weapons_pattern = new String[][]{{"X", "X", "#"}};
    private static final int[][] weapons_ingredients = new int[][]{{BlockList1_6.planks.blockId(), BlockList1_6.cobblestone.blockId(), ItemList1_6.ingotIron.itemId(), ItemList1_6.diamond.itemId(), ItemList1_6.ingotGold.itemId()}, {ItemList1_6.swordWood.itemId(), ItemList1_6.swordStone.itemId(), ItemList1_6.swordIron.itemId(), ItemList1_6.swordDiamond.itemId(), ItemList1_6.swordGold.itemId()}};

    private static final int[][] ingots_ingredients = new int[][]{{BlockList1_6.blockGold.blockId(), ItemList1_6.ingotGold.itemId()}, {BlockList1_6.blockIron.blockId(), ItemList1_6.ingotIron.itemId()}, {BlockList1_6.blockDiamond.blockId(), ItemList1_6.diamond.itemId()}};

    private static final String[][] armor_pattern = new String[][]{{"XXX", "X X"}, {"X X", "XXX", "XXX"}, {"XXX", "X X", "X X"}, {"X X", "X X"}};
    private static final int[][] armor_ingredients = new int[][]{{ItemList1_6.leather.itemId(), BlockList1_6.fire.blockId(), ItemList1_6.ingotIron.itemId(), ItemList1_6.diamond.itemId(), ItemList1_6.ingotGold.itemId()}, {ItemList1_6.helmetLeather.itemId(), ItemList1_6.helmetChain.itemId(), ItemList1_6.helmetIron.itemId(), ItemList1_6.helmetDiamond.itemId(), ItemList1_6.helmetGold.itemId()}, {ItemList1_6.plateLeather.itemId(), ItemList1_6.plateChain.itemId(), ItemList1_6.plateIron.itemId(), ItemList1_6.plateDiamond.itemId(), ItemList1_6.plateGold.itemId()}, {ItemList1_6.legsLeather.itemId(), ItemList1_6.legsChain.itemId(), ItemList1_6.legsIron.itemId(), ItemList1_6.legsDiamond.itemId(), ItemList1_6.legsGold.itemId()}, {ItemList1_6.bootsLeather.itemId(), ItemList1_6.bootsChain.itemId(), ItemList1_6.bootsIron.itemId(), ItemList1_6.bootsDiamond.itemId(), ItemList1_6.bootsGold.itemId()}};

    static {
        for (int i = 0; i < tools_ingredients[0].length; ++i) {
            for (int i1 = 0; i1 < tools_ingredients.length - 1; ++i1) {
                addRecipe(new DataItem(tools_ingredients[i1 + 1][i], (byte) 1, (short) 0, null), tools_pattern[i1], '#', ItemList1_6.stick.itemId(), 'X', tools_ingredients[0][i]);
            }
        }
        for (int i = 0; i < weapons_ingredients[0].length; ++i) {
            for (int i1 = 0; i1 < weapons_ingredients.length - 1; ++i1) {
                addRecipe(new DataItem(weapons_ingredients[i1 + 1][i], (byte) 1, (short) 0, null), weapons_pattern[i1], '#', ItemList1_6.stick.itemId(), 'X', weapons_ingredients[0][i]);
            }
        }
        addRecipe(new DataItem(ItemList1_6.bow.itemId(), (byte) 1, (short) 0, null), " #X", "# X", " #X", 'X', ItemList1_6.silk.itemId(), '#', ItemList1_6.stick.itemId());
        addRecipe(new DataItem(ItemList1_6.arrow.itemId(), (byte) 4, (short) 0, null), "X", "#", "Y", 'Y', ItemList1_6.feather.itemId(), 'X', ItemList1_6.flint.itemId(), '#', ItemList1_6.stick.itemId());
        for (int[] ingots_ingredient : ingots_ingredients) {
            addRecipe(new DataItem(ingots_ingredient[0], (byte) 1, (short) 0, null), "###", "###", "###", '#', ingots_ingredient[1]);
            addRecipe(new DataItem(ingots_ingredient[1], (byte) 9, (short) 0, null), "#", '#', ingots_ingredient[0]);
        }
        addRecipe(new DataItem(ItemList1_6.bowlSoup.itemId(), (byte) 1, (short) 0, null), "Y", "X", "#", 'X', BlockList1_6.mushroomBrown.blockId(), 'Y', BlockList1_6.mushroomRed.blockId(), '#', ItemList1_6.bowlEmpty.itemId());
        addRecipe(new DataItem(ItemList1_6.bowlSoup.itemId(), (byte) 1, (short) 0, null), "Y", "X", "#", 'X', BlockList1_6.mushroomRed.blockId(), 'Y', BlockList1_6.mushroomBrown.blockId(), '#', ItemList1_6.bowlEmpty.itemId());
        addRecipe(new DataItem(BlockList1_6.chest.blockId(), (byte) 1, (short) 0, null), "###", "# #", "###", '#', BlockList1_6.planks.blockId());
        addRecipe(new DataItem(BlockList1_6.furnaceIdle.blockId(), (byte) 1, (short) 0, null), "###", "# #", "###", '#', BlockList1_6.cobblestone.blockId());
        addRecipe(new DataItem(BlockList1_6.workbench.blockId(), (byte) 1, (short) 0, null), "##", "##", '#', BlockList1_6.planks.blockId());
        for (int i = 0; i < armor_ingredients[0].length; ++i) {
            for (int i1 = 0; i1 < armor_ingredients.length - 1; ++i1) {
                addRecipe(new DataItem(armor_ingredients[i1 + 1][i], (byte) 1, (short) 0, null), armor_pattern[i1], 'X', armor_ingredients[0][i]);
            }
        }
        addRecipe(new DataItem(ItemList1_6.paper.itemId(), (byte) 3, (short) 0, null), "###", '#', ItemList1_6.reed.itemId());
        addRecipe(new DataItem(ItemList1_6.book.itemId(), (byte) 1, (short) 0, null), "#", "#", "#", '#', ItemList1_6.paper.itemId());
        addRecipe(new DataItem(BlockList1_6.fence.blockId(), (byte) 2, (short) 0, null), "###", "###", '#', ItemList1_6.stick.itemId());
        addRecipe(new DataItem(BlockList1_6.jukebox.blockId(), (byte) 1, (short) 0, null), "###", "#X#", "###", '#', BlockList1_6.planks.blockId(), 'X', ItemList1_6.diamond.itemId());
        addRecipe(new DataItem(BlockList1_6.bookShelf.blockId(), (byte) 1, (short) 0, null), "###", "XXX", "###", '#', BlockList1_6.planks.blockId(), 'X', ItemList1_6.book.itemId());
        addRecipe(new DataItem(BlockList1_6.blockSnow.blockId(), (byte) 1, (short) 0, null), "##", "##", '#', ItemList1_6.snowball.itemId());
        addRecipe(new DataItem(BlockList1_6.blockClay.blockId(), (byte) 1, (short) 0, null), "##", "##", '#', ItemList1_6.clay.itemId());
        addRecipe(new DataItem(BlockList1_6.brick.blockId(), (byte) 1, (short) 0, null), "##", "##", '#', ItemList1_6.brick.itemId());
        addRecipe(new DataItem(BlockList1_6.glowStone.blockId(), (byte) 1, (short) 0, null), "###", "###", "###", '#', ItemList1_6.glowstone.itemId());
        addRecipe(new DataItem(BlockList1_6.cloth.blockId(), (byte) 1, (short) 0, null), "###", "###", "###", '#', ItemList1_6.silk.itemId());
        addRecipe(new DataItem(BlockList1_6.tnt.blockId(), (byte) 1, (short) 0, null), "X#X", "#X#", "X#X", 'X', ItemList1_6.gunpowder.itemId(), '#', BlockList1_6.sand.blockId());
        addRecipe(new DataItem(BlockList1_6.stoneSingleSlab.blockId(), (byte) 3, (short) 0, null), "###", '#', BlockList1_6.cobblestone.blockId());
        addRecipe(new DataItem(BlockList1_6.ladder.blockId(), (byte) 1, (short) 0, null), "# #", "###", "# #", '#', ItemList1_6.stick.itemId());
        addRecipe(new DataItem(ItemList1_6.doorWood.itemId(), (byte) 1, (short) 0, null), "##", "##", "##", '#', BlockList1_6.planks.blockId());
        addRecipe(new DataItem(ItemList1_6.doorIron.itemId(), (byte) 1, (short) 0, null), "##", "##", "##", '#', ItemList1_6.ingotIron.itemId());
        addRecipe(new DataItem(ItemList1_6.sign.itemId(), (byte) 1, (short) 0, null), "###", "###", " X ", '#', BlockList1_6.planks.blockId(), 'X', ItemList1_6.stick.itemId());
        addRecipe(new DataItem(BlockList1_6.planks.blockId(), (byte) 4, (short) 0, null), "#", '#', BlockList1_6.wood.blockId());
        addRecipe(new DataItem(ItemList1_6.stick.itemId(), (byte) 4, (short) 0, null), "#", "#", '#', BlockList1_6.planks.blockId());
        addRecipe(new DataItem(BlockList1_6.torchWood.blockId(), (byte) 4, (short) 0, null), "X", "#", 'X', ItemList1_6.coal.itemId(), '#', ItemList1_6.stick.itemId());
        addRecipe(new DataItem(ItemList1_6.bowlEmpty.itemId(), (byte) 4, (short) 0, null), "# #", " # ", '#', BlockList1_6.planks.blockId());
        addRecipe(new DataItem(BlockList1_6.rail.blockId(), (byte) 16, (short) 0, null), "X X", "X#X", "X X", 'X', ItemList1_6.ingotIron.itemId(), '#', ItemList1_6.stick.itemId());
        addRecipe(new DataItem(ItemList1_6.minecartEmpty.itemId(), (byte) 1, (short) 0, null), "# #", "###", '#', ItemList1_6.ingotIron.itemId());
        addRecipe(new DataItem(BlockList1_6.pumpkinLantern.blockId(), (byte) 1, (short) 0, null), "A", "B", 'A', BlockList1_6.pumpkin.blockId(), 'B', BlockList1_6.torchWood.blockId());
        addRecipe(new DataItem(ItemList1_6.minecartCrate.itemId(), (byte) 1, (short) 0, null), "A", "B", 'A', BlockList1_6.chest.blockId(), 'B', ItemList1_6.minecartEmpty.itemId());
        addRecipe(new DataItem(ItemList1_6.minecartPowered.itemId(), (byte) 1, (short) 0, null), "A", "B", 'A', BlockList1_6.furnaceIdle.blockId(), 'B', ItemList1_6.minecartEmpty.itemId());
        addRecipe(new DataItem(ItemList1_6.boat.itemId(), (byte) 1, (short) 0, null), "# #", "###", '#', BlockList1_6.planks.blockId());
        addRecipe(new DataItem(ItemList1_6.bucketEmpty.itemId(), (byte) 1, (short) 0, null), "# #", " # ", '#', ItemList1_6.ingotIron.itemId());
        addRecipe(new DataItem(ItemList1_6.flintAndSteel.itemId(), (byte) 1, (short) 0, null), "A ", " B", 'A', ItemList1_6.ingotIron.itemId(), 'B', ItemList1_6.flint.itemId());
        addRecipe(new DataItem(ItemList1_6.bread.itemId(), (byte) 1, (short) 0, null), "###", '#', ItemList1_6.wheat.itemId());
        addRecipe(new DataItem(BlockList1_6.stairsWoodOak.blockId(), (byte) 4, (short) 0, null), "#  ", "## ", "###", '#', BlockList1_6.planks.blockId());
        addRecipe(new DataItem(ItemList1_6.fishingRod.itemId(), (byte) 1, (short) 0, null), "  #", " #X", "# X", '#', ItemList1_6.stick.itemId(), 'X', ItemList1_6.silk.itemId());
        addRecipe(new DataItem(BlockList1_6.stairsCobblestone.blockId(), (byte) 4, (short) 0, null), "#  ", "## ", "###", '#', BlockList1_6.cobblestone.blockId());
        addRecipe(new DataItem(ItemList1_6.painting.itemId(), (byte) 1, (short) 0, null), "###", "#X#", "###", '#', ItemList1_6.stick.itemId(), 'X', BlockList1_6.cloth.blockId());
        addRecipe(new DataItem(ItemList1_6.appleGold.itemId(), (byte) 1, (short) 0, null), "###", "#X#", "###", '#', BlockList1_6.blockGold.blockId(), 'X', ItemList1_6.appleRed.itemId());
        addRecipe(new DataItem(BlockList1_6.lever.blockId(), (byte) 1, (short) 0, null), "X", "#", '#', BlockList1_6.cobblestone.blockId(), 'X', ItemList1_6.stick.itemId());
        addRecipe(new DataItem(BlockList1_6.torchRedstoneActive.blockId(), (byte) 1, (short) 0, null), "X", "#", '#', ItemList1_6.stick.itemId(), 'X', ItemList1_6.redstone.itemId());
        addRecipe(new DataItem(ItemList1_6.pocketSundial.itemId(), (byte) 1, (short) 0, null), " # ", "#X#", " # ", '#', ItemList1_6.ingotGold.itemId(), 'X', ItemList1_6.redstone.itemId());
        addRecipe(new DataItem(ItemList1_6.compass.itemId(), (byte) 1, (short) 0, null), " # ", "#X#", " # ", '#', ItemList1_6.ingotIron.itemId(), 'X', ItemList1_6.redstone.itemId());
        addRecipe(new DataItem(BlockList1_6.stoneButton.blockId(), (byte) 1, (short) 0, null), "#", "#", '#', BlockList1_6.stone.blockId());
        addRecipe(new DataItem(BlockList1_6.pressurePlateStone.blockId(), (byte) 1, (short) 0, null), "###", '#', BlockList1_6.stone.blockId());
        addRecipe(new DataItem(BlockList1_6.pressurePlatePlanks.blockId(), (byte) 1, (short) 0, null), "###", '#', BlockList1_6.planks.blockId());
        recipes.sort((o1, o2) -> Integer.compare(o2.getRecipeSize(), o1.getRecipeSize()));
    }

    private static void addRecipe(final Item resultItem, final Object... objects) {
        StringBuilder var3 = new StringBuilder();
        int pos = 0;
        int width = 0;
        int height = 0;

        if (objects[pos] instanceof String[]) {
            String[] var11 = (String[]) objects[pos++];

            for (String var9 : var11) {
                height++;
                width = var9.length();
                var3.append(var9);
            }
        } else {
            while (objects[pos] instanceof String) {
                String var7 = (String) objects[pos++];
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

        recipes.add(new CraftingRecipe(width, height, ingredientMap, resultItem));
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
        for (CraftingRecipe recipe : recipes) {
            if (recipe.matches(ingredientMap)) {
                return recipe.createResult();
            }
        }

        return null;
    }

}
