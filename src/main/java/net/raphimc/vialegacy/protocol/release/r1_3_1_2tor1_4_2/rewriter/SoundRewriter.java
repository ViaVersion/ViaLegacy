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
package net.raphimc.vialegacy.protocol.release.r1_3_1_2tor1_4_2.rewriter;

import com.viaversion.viaversion.libs.fastutil.objects.Object2ObjectMap;
import com.viaversion.viaversion.libs.fastutil.objects.Object2ObjectOpenHashMap;

public class SoundRewriter {

    private static final Object2ObjectMap<String, String> SOUNDS = new Object2ObjectOpenHashMap<>(121, 0.99F);

    static {
        SOUNDS.put("ambient.cave.cave", "ambient.cave.cave");
        SOUNDS.put("ambient.weather.rain", "ambient.weather.rain");
        SOUNDS.put("ambient.weather.thunder", "ambient.weather.thunder");
        SOUNDS.put("damage.fallbig", "damage.fallbig");
        SOUNDS.put("damage.fallsmall", "damage.fallsmall");
        SOUNDS.put("damage.hurtflesh", "damage.hit");
        SOUNDS.put("fire.fire", "fire.fire");
        SOUNDS.put("fire.ignite", "fire.ignite");
        SOUNDS.put("liquid.lava", "liquid.lava");
        SOUNDS.put("liquid.lavapop", "liquid.lavapop");
        SOUNDS.put("liquid.splash", "liquid.splash");
        SOUNDS.put("liquid.water", "liquid.water");
        SOUNDS.put("mob.blaze.breathe", "mob.blaze.breathe");
        SOUNDS.put("mob.blaze.death", "mob.blaze.death");
        SOUNDS.put("mob.blaze.hit", "mob.blaze.hit");
        SOUNDS.put("mob.cat.hiss", "mob.cat.hiss");
        SOUNDS.put("mob.cat.hitt", "mob.cat.hitt");
        SOUNDS.put("mob.cat.meow", "mob.cat.meow");
        SOUNDS.put("mob.cat.purr", "mob.cat.purr");
        SOUNDS.put("mob.cat.purreow", "mob.cat.purreow");
        SOUNDS.put("mob.chicken", "mob.chicken.say");
        SOUNDS.put("mob.chickenhurt", "mob.chicken.hurt");
        SOUNDS.put("mob.chickenplop", "mob.chicken.plop");
        SOUNDS.put("mob.cow", "mob.cow.say");
        SOUNDS.put("mob.cowhurt", "mob.cow.hurt");
        SOUNDS.put("mob.creeper", "mob.creeper.say");
        SOUNDS.put("mob.creeperdeath", "mob.creeper.death");
        SOUNDS.put("mob.endermen.death", "mob.endermen.death");
        SOUNDS.put("mob.endermen.hit", "mob.endermen.hit");
        SOUNDS.put("mob.endermen.idle", "mob.endermen.idle");
        SOUNDS.put("mob.endermen.portal", "mob.endermen.portal");
        SOUNDS.put("mob.endermen.scream", "mob.endermen.scream");
        SOUNDS.put("mob.endermen.stare", "mob.endermen.stare");
        SOUNDS.put("mob.ghast.affectionate scream", "mob.ghast.affectionate_scream");
        SOUNDS.put("mob.ghast.charge", "mob.ghast.charge");
        SOUNDS.put("mob.ghast.death", "mob.ghast.death");
        SOUNDS.put("mob.ghast.fireball", "mob.ghast.fireball");
        SOUNDS.put("mob.ghast.moan", "mob.ghast.moan");
        SOUNDS.put("mob.ghast.scream", "mob.ghast.scream");
        SOUNDS.put("mob.irongolem.death", "mob.irongolem.death");
        SOUNDS.put("mob.irongolem.hit", "mob.irongolem.hit");
        SOUNDS.put("mob.irongolem.throw", "mob.irongolem.throw");
        SOUNDS.put("mob.irongolem.walk", "mob.irongolem.walk");
        SOUNDS.put("mob.magmacube.big", "mob.magmacube.big");
        SOUNDS.put("mob.magmacube.jump", "mob.magmacube.jump");
        SOUNDS.put("mob.magmacube.small", "mob.magmacube.small");
        SOUNDS.put("mob.pig", "mob.pig.say");
        SOUNDS.put("mob.pigdeath", "mob.pig.death");
        SOUNDS.put("mob.sheep", "mob.sheep.say");
        SOUNDS.put("mob.silverfish.hit", "mob.silverfish.hit");
        SOUNDS.put("mob.silverfish.kill", "mob.silverfish.kill");
        SOUNDS.put("mob.silverfish.say", "mob.silverfish.say");
        SOUNDS.put("mob.silverfish.step", "mob.silverfish.step");
        SOUNDS.put("mob.skeleton", "mob.skeleton.say");
        SOUNDS.put("mob.skeletondeath", "mob.skeleton.death");
        SOUNDS.put("mob.skeletonhurt", "mob.skeleton.hurt");
        SOUNDS.put("mob.slime", "mob.slime.small");
        SOUNDS.put("mob.slimeattack", "mob.slime.attack");
        SOUNDS.put("mob.spider", "mob.spider.say");
        SOUNDS.put("mob.spiderdeath", "mob.spider.death");
        SOUNDS.put("mob.wolf.bark", "mob.wolf.bark");
        SOUNDS.put("mob.wolf.death", "mob.wolf.death");
        SOUNDS.put("mob.wolf.growl", "mob.wolf.growl");
        SOUNDS.put("mob.wolf.howl", "mob.wolf.howl");
        SOUNDS.put("mob.wolf.hurt", "mob.wolf.hurt");
        SOUNDS.put("mob.wolf.panting", "mob.wolf.panting");
        SOUNDS.put("mob.wolf.shake", "mob.wolf.shake");
        SOUNDS.put("mob.wolf.whine", "mob.wolf.whine");
        SOUNDS.put("mob.zombie.metal", "mob.zombie.metal");
        SOUNDS.put("mob.zombie", "mob.zombie.say");
        SOUNDS.put("mob.zombie.wood", "mob.zombie.wood");
        SOUNDS.put("mob.zombie.woodbreak", "mob.zombie.woodbreak");
        SOUNDS.put("mob.zombiedeath", "mob.zombie.death");
        SOUNDS.put("mob.zombiehurt", "mob.zombie.hurt");
        SOUNDS.put("mob.zombiepig.zpig", "mob.zombiepig.zpig");
        SOUNDS.put("mob.zombiepig.zpigangry", "mob.zombiepig.zpigangry");
        SOUNDS.put("mob.zombiepig.zpigdeath", "mob.zombiepig.zpigdeath");
        SOUNDS.put("mob.zombiepig.zpighurt", "mob.zombiepig.zpighurt");
        SOUNDS.put("mob.villager.default", "mob.villager.default");
        SOUNDS.put("mob.villager.defaulthurt", "mob.villager.defaulthurt");
        SOUNDS.put("mob.villager.defaultdeath", "mob.villager.defaultdeath");
        SOUNDS.put("note.bass", "note.bass");
        SOUNDS.put("note.bassattack", "note.bassattack");
        SOUNDS.put("note.bd", "note.bd");
        SOUNDS.put("note.harp", "note.harp");
        SOUNDS.put("note.hat", "note.hat");
        SOUNDS.put("note.pling", "note.pling");
        SOUNDS.put("note.snare", "note.snare");
        SOUNDS.put("portal.portal", "portal.portal");
        SOUNDS.put("portal.travel", "portal.travel");
        SOUNDS.put("portal.trigger", "portal.trigger");
        SOUNDS.put("random.bow", "random.bow");
        SOUNDS.put("random.bowhit", "random.bowhit");
        SOUNDS.put("random.break", "random.break");
        SOUNDS.put("random.breath", "random.breath");
        SOUNDS.put("random.burp", "random.burp");
        SOUNDS.put("random.chestclosed", "random.chestclosed");
        SOUNDS.put("random.chestopen", "random.chestopen");
        SOUNDS.put("random.click", "random.click");
        SOUNDS.put("random.door_close", "random.door_close");
        SOUNDS.put("random.door_open", "random.door_open");
        SOUNDS.put("random.drink", "random.drink");
        SOUNDS.put("random.drr", "");
        SOUNDS.put("random.eat", "random.eat");
        SOUNDS.put("random.explode", "random.explode");
        SOUNDS.put("random.fizz", "random.fizz");
        SOUNDS.put("random.fuse", "random.fuse");
        SOUNDS.put("random.glass", "random.glass");
        SOUNDS.put("random.hurt", "damage.hit");
        SOUNDS.put("random.levelup", "random.levelup");
        SOUNDS.put("random.old_explode", "random.explode");
        SOUNDS.put("random.orb", "random.orb");
        SOUNDS.put("random.pop", "random.pop");
        SOUNDS.put("random.splash", "random.splash");
        SOUNDS.put("random.wood click", "random.wood_click");
        SOUNDS.put("step.cloth", "step.cloth");
        SOUNDS.put("step.grass", "step.grass");
        SOUNDS.put("step.gravel", "step.gravel");
        SOUNDS.put("step.sand", "step.sand");
        SOUNDS.put("step.snow", "step.snow");
        SOUNDS.put("step.stone", "step.stone");
        SOUNDS.put("step.wood", "step.wood");
        SOUNDS.put("tile.piston.in", "tile.piston.in");
        SOUNDS.put("tile.piston.out", "tile.piston.out");
    }

    public static String map(final String sound) {
        return SOUNDS.get(sound);
    }

}
