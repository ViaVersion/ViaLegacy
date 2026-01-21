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
package net.raphimc.vialegacy.protocol.release.r1_1tor1_2_1_3.biome;

import com.seedfinding.mcnoise.simplex.SimplexNoiseSampler;
import com.seedfinding.mcseed.rand.JRand;

public class BetaOctaveSimplexNoiseSampler {

    private final SimplexNoiseSampler[] octaveSamplers;
    private final double scaleX;
    private final double scaleY;
    private final double lacunarity;

    public BetaOctaveSimplexNoiseSampler(final JRand random, final int octaveCount, final double scaleX, final double scaleY, final double lacunarity) {
        this.octaveSamplers = new SimplexNoiseSampler[octaveCount];
        for (int i = 0; i < octaveCount; i++) {
            this.octaveSamplers[i] = new SimplexNoiseSampler(random);
        }
        this.scaleX = scaleX / 1.5D;
        this.scaleY = scaleY / 1.5D;
        this.lacunarity = lacunarity;
    }

    public double sample(final double x, final double y) {
        double noise = 0D;
        double frequency = 1D;
        double amplitude = 1D;
        for (SimplexNoiseSampler sampler : this.octaveSamplers) {
            noise += sampler.sample2D(x * this.scaleX * frequency + sampler.originX, y * this.scaleY * frequency + sampler.originY) * amplitude;
            frequency *= this.lacunarity;
            amplitude *= 2D;
        }
        return noise * 0.55D;
    }

}
