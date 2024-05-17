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
package net.raphimc.vialegacy.protocol.release.r1_1tor1_2_1_3.biome.beta;

import java.util.Random;

public class NoiseGeneratorOctaves2 {

    public NoiseGeneratorOctaves2(Random random, int i) {
        field_4233_b = i;
        field_4234_a = new NoiseGenerator2[i];
        for (int j = 0; j < i; j++) {
            field_4234_a[j] = new NoiseGenerator2(random);
        }

    }

    public double[] func_4112_a(double ad[], double d, double d1, int i, int j,
                                double d2, double d3, double d4) {
        return func_4111_a(ad, d, d1, i, j, d2, d3, d4, 0.5D);
    }

    public double[] func_4111_a(double ad[], double d, double d1, int i, int j,
                                double d2, double d3, double d4, double d5) {
        d2 /= 1.5D;
        d3 /= 1.5D;
        if (ad == null || ad.length < i * j) {
            ad = new double[i * j];
        } else {
            for (int k = 0; k < ad.length; k++) {
                ad[k] = 0.0D;
            }

        }
        double d6 = 1.0D;
        double d7 = 1.0D;
        for (int l = 0; l < field_4233_b; l++) {
            field_4234_a[l].func_4157_a(ad, d, d1, i, j, d2 * d7, d3 * d7, 0.55000000000000004D / d6);
            d7 *= d4;
            d6 *= d5;
        }

        return ad;
    }

    private final NoiseGenerator2[] field_4234_a;
    private final int field_4233_b;

}
