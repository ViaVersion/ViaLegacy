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
package net.raphimc.vialegacy.protocol.alpha.a1_2_3_5_1_2_6tob1_0_1_1_1.model;

import com.viaversion.viaversion.api.minecraft.item.Item;

public class CraftingRecipe {

    private final int width;
    private final int height;
    private final int[] ingredientMap;
    private final Item resultItem;

    public CraftingRecipe(final int width, final int height, final int[] ingredientMap, final Item resultItem) {
        this.width = width;
        this.height = height;
        this.ingredientMap = ingredientMap;
        this.resultItem = resultItem;
    }

    public boolean matches(final int[] ingredientMap) {
        for (int x = 0; x <= 3 - this.width; x++) {
            for (int y = 0; y <= 3 - this.height; y++) {
                if (this.matches(ingredientMap, x, y, true)) {
                    return true;
                } else if (this.matches(ingredientMap, x, y, false)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean matches(final int[] ingredientMap, final int x, final int y, final boolean b) {
        for (int xx = 0; xx < 3; xx++) {
            for (int yy = 0; yy < 3; yy++) {
                final int rx = xx - x;
                final int ry = yy - y;
                int var9 = -1;
                if (rx >= 0 && ry >= 0 && rx < this.width && ry < this.height) {
                    if (b) {
                        var9 = this.ingredientMap[this.width - rx - 1 + ry * this.width];
                    } else {
                        var9 = this.ingredientMap[rx + ry * this.width];
                    }
                }

                if (ingredientMap[xx + yy * 3] != var9) {
                    return false;
                }
            }
        }

        return true;
    }

    public Item createResult() {
        return this.resultItem.copy();
    }

    public int getRecipeSize() {
        return this.width * this.height;
    }

}
