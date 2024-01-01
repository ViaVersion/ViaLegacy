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
package net.raphimc.vialegacy.protocols.release.protocol1_7_2_5to1_6_4.rewriter;

import com.viaversion.viaversion.libs.fastutil.objects.Object2ObjectMap;
import com.viaversion.viaversion.libs.fastutil.objects.Object2ObjectOpenHashMap;

public class SoundRewriter {

    private static final Object2ObjectMap<String, String> SOUNDS = new Object2ObjectOpenHashMap<>(196, 0.99F);

    static {
        SOUNDS.put("ambient.cave.cave", "ambient.cave.cave");
        SOUNDS.put("ambient.weather.rain", "ambient.weather.rain");
        SOUNDS.put("ambient.weather.thunder", "ambient.weather.thunder");
        SOUNDS.put("damage.fallbig", "game.neutral.hurt.fall.big");
        SOUNDS.put("damage.fallsmall", "game.neutral.hurt.fall.small");
        SOUNDS.put("damage.hit", "game.neutral.hurt");
        SOUNDS.put("dig.cloth", "dig.cloth");
        SOUNDS.put("dig.grass", "dig.grass");
        SOUNDS.put("dig.gravel", "dig.gravel");
        SOUNDS.put("dig.sand", "dig.sand");
        SOUNDS.put("dig.snow", "dig.snow");
        SOUNDS.put("dig.stone", "dig.stone");
        SOUNDS.put("dig.wood", "dig.wood");
        SOUNDS.put("fire.fire", "fire.fire");
        SOUNDS.put("fire.ignite", "fire.ignite");
        SOUNDS.put("fireworks.blast", "fireworks.blast");
        SOUNDS.put("fireworks.blast_far", "fireworks.blast_far");
        SOUNDS.put("fireworks.largeBlast", "fireworks.largeBlast");
        SOUNDS.put("fireworks.largeBlast_far", "fireworks.largeBlast_far");
        SOUNDS.put("fireworks.launch", "fireworks.launch");
        SOUNDS.put("fireworks.twinkle", "fireworks.twinkle");
        SOUNDS.put("fireworks.twinkle_far", "fireworks.twinkle_far");
        SOUNDS.put("liquid.lava", "liquid.lava");
        SOUNDS.put("liquid.lavapop", "liquid.lavapop");
        SOUNDS.put("liquid.splash", "game.neutral.swim.splash");
        SOUNDS.put("liquid.swim", "game.neutral.swim");
        SOUNDS.put("liquid.water", "liquid.water");
        SOUNDS.put("minecart.base", "minecart.base");
        SOUNDS.put("minecart.inside", "minecart.inside");
        SOUNDS.put("mob.bat.death", "mob.bat.death");
        SOUNDS.put("mob.bat.hurt", "mob.bat.hurt");
        SOUNDS.put("mob.bat.idle", "mob.bat.idle");
        SOUNDS.put("mob.bat.loop", "mob.bat.loop");
        SOUNDS.put("mob.bat.takeoff", "mob.bat.takeoff");
        SOUNDS.put("mob.blaze.breathe", "mob.blaze.breathe");
        SOUNDS.put("mob.blaze.death", "mob.blaze.death");
        SOUNDS.put("mob.blaze.hit", "mob.blaze.hit");
        SOUNDS.put("mob.cat.hiss", "mob.cat.hiss");
        SOUNDS.put("mob.cat.hitt", "mob.cat.hitt");
        SOUNDS.put("mob.cat.meow", "mob.cat.meow");
        SOUNDS.put("mob.cat.purr", "mob.cat.purr");
        SOUNDS.put("mob.cat.purreow", "mob.cat.purreow");
        SOUNDS.put("mob.chicken.hurt", "mob.chicken.hurt");
        SOUNDS.put("mob.chicken.plop", "mob.chicken.plop");
        SOUNDS.put("mob.chicken.say", "mob.chicken.say");
        SOUNDS.put("mob.chicken.step", "mob.chicken.step");
        SOUNDS.put("mob.cow.hurt", "mob.cow.hurt");
        SOUNDS.put("mob.cow.say", "mob.cow.say");
        SOUNDS.put("mob.cow.step", "mob.cow.step");
        SOUNDS.put("mob.creeper.death", "mob.creeper.death");
        SOUNDS.put("mob.creeper.say", "mob.creeper.say");
        SOUNDS.put("mob.enderdragon.end", "mob.enderdragon.end");
        SOUNDS.put("mob.enderdragon.growl", "mob.enderdragon.growl");
        SOUNDS.put("mob.enderdragon.hit", "mob.enderdragon.hit");
        SOUNDS.put("mob.enderdragon.wings", "mob.enderdragon.wings");
        SOUNDS.put("mob.endermen.death", "mob.endermen.death");
        SOUNDS.put("mob.endermen.hit", "mob.endermen.hit");
        SOUNDS.put("mob.endermen.idle", "mob.endermen.idle");
        SOUNDS.put("mob.endermen.portal", "mob.endermen.portal");
        SOUNDS.put("mob.endermen.scream", "mob.endermen.scream");
        SOUNDS.put("mob.endermen.stare", "mob.endermen.stare");
        SOUNDS.put("mob.ghast.affectionate_scream", "mob.ghast.affectionate_scream");
        SOUNDS.put("mob.ghast.charge", "mob.ghast.charge");
        SOUNDS.put("mob.ghast.death", "mob.ghast.death");
        SOUNDS.put("mob.ghast.fireball", "mob.ghast.fireball");
        SOUNDS.put("mob.ghast.moan", "mob.ghast.moan");
        SOUNDS.put("mob.ghast.scream", "mob.ghast.scream");
        SOUNDS.put("mob.horse.angry", "mob.horse.angry");
        SOUNDS.put("mob.horse.armor", "mob.horse.armor");
        SOUNDS.put("mob.horse.breathe", "mob.horse.breathe");
        SOUNDS.put("mob.horse.death", "mob.horse.death");
        SOUNDS.put("mob.horse.donkey.angry", "mob.horse.donkey.angry");
        SOUNDS.put("mob.horse.donkey.death", "mob.horse.donkey.death");
        SOUNDS.put("mob.horse.donkey.hit", "mob.horse.donkey.hit");
        SOUNDS.put("mob.horse.donkey.idle", "mob.horse.donkey.idle");
        SOUNDS.put("mob.horse.gallop", "mob.horse.gallop");
        SOUNDS.put("mob.horse.hit", "mob.horse.hit");
        SOUNDS.put("mob.horse.idle", "mob.horse.idle");
        SOUNDS.put("mob.horse.jump", "mob.horse.jump");
        SOUNDS.put("mob.horse.land", "mob.horse.land");
        SOUNDS.put("mob.horse.leather", "mob.horse.leather");
        SOUNDS.put("mob.horse.skeleton.death", "mob.horse.skeleton.death");
        SOUNDS.put("mob.horse.skeleton.hit", "mob.horse.skeleton.hit");
        SOUNDS.put("mob.horse.skeleton.idle", "mob.horse.skeleton.idle");
        SOUNDS.put("mob.horse.soft", "mob.horse.soft");
        SOUNDS.put("mob.horse.wood", "mob.horse.wood");
        SOUNDS.put("mob.horse.zombie.death", "mob.horse.zombie.death");
        SOUNDS.put("mob.horse.zombie.hit", "mob.horse.zombie.hit");
        SOUNDS.put("mob.horse.zombie.idle", "mob.horse.zombie.idle");
        SOUNDS.put("mob.irongolem.death", "mob.irongolem.death");
        SOUNDS.put("mob.irongolem.hit", "mob.irongolem.hit");
        SOUNDS.put("mob.irongolem.throw", "mob.irongolem.throw");
        SOUNDS.put("mob.irongolem.walk", "mob.irongolem.walk");
        SOUNDS.put("mob.magmacube.big", "mob.magmacube.big");
        SOUNDS.put("mob.magmacube.jump", "mob.magmacube.jump");
        SOUNDS.put("mob.magmacube.small", "mob.magmacube.small");
        SOUNDS.put("mob.pig.death", "mob.pig.death");
        SOUNDS.put("mob.pig.say", "mob.pig.say");
        SOUNDS.put("mob.pig.step", "mob.pig.step");
        SOUNDS.put("mob.sheep.say", "mob.sheep.say");
        SOUNDS.put("mob.sheep.shear", "mob.sheep.shear");
        SOUNDS.put("mob.sheep.step", "mob.sheep.step");
        SOUNDS.put("mob.silverfish.hit", "mob.silverfish.hit");
        SOUNDS.put("mob.silverfish.kill", "mob.silverfish.kill");
        SOUNDS.put("mob.silverfish.say", "mob.silverfish.say");
        SOUNDS.put("mob.silverfish.step", "mob.silverfish.step");
        SOUNDS.put("mob.skeleton.death", "mob.skeleton.death");
        SOUNDS.put("mob.skeleton.hurt", "mob.skeleton.hurt");
        SOUNDS.put("mob.skeleton.say", "mob.skeleton.say");
        SOUNDS.put("mob.skeleton.step", "mob.skeleton.step");
        SOUNDS.put("mob.slime.attack", "mob.slime.attack");
        SOUNDS.put("mob.slime.big", "mob.slime.big");
        SOUNDS.put("mob.slime.small", "mob.slime.small");
        SOUNDS.put("mob.spider.death", "mob.spider.death");
        SOUNDS.put("mob.spider.say", "mob.spider.say");
        SOUNDS.put("mob.spider.step", "mob.spider.step");
        SOUNDS.put("mob.villager.death", "mob.villager.death");
        SOUNDS.put("mob.villager.haggle", "mob.villager.haggle");
        SOUNDS.put("mob.villager.hit", "mob.villager.hit");
        SOUNDS.put("mob.villager.idle", "mob.villager.idle");
        SOUNDS.put("mob.villager.no", "mob.villager.no");
        SOUNDS.put("mob.villager.yes", "mob.villager.yes");
        SOUNDS.put("mob.wither.death", "mob.wither.death");
        SOUNDS.put("mob.wither.hurt", "mob.wither.hurt");
        SOUNDS.put("mob.wither.idle", "mob.wither.idle");
        SOUNDS.put("mob.wither.shoot", "mob.wither.shoot");
        SOUNDS.put("mob.wither.spawn", "mob.wither.spawn");
        SOUNDS.put("mob.wolf.bark", "mob.wolf.bark");
        SOUNDS.put("mob.wolf.death", "mob.wolf.death");
        SOUNDS.put("mob.wolf.growl", "mob.wolf.growl");
        SOUNDS.put("mob.wolf.howl", "mob.wolf.howl");
        SOUNDS.put("mob.wolf.hurt", "mob.wolf.hurt");
        SOUNDS.put("mob.wolf.panting", "mob.wolf.panting");
        SOUNDS.put("mob.wolf.shake", "mob.wolf.shake");
        SOUNDS.put("mob.wolf.step", "mob.wolf.step");
        SOUNDS.put("mob.wolf.whine", "mob.wolf.whine");
        SOUNDS.put("mob.zombie.death", "mob.zombie.death");
        SOUNDS.put("mob.zombie.hurt", "mob.zombie.hurt");
        SOUNDS.put("mob.zombie.infect", "mob.zombie.infect");
        SOUNDS.put("mob.zombie.metal", "mob.zombie.metal");
        SOUNDS.put("mob.zombie.remedy", "mob.zombie.remedy");
        SOUNDS.put("mob.zombie.say", "mob.zombie.say");
        SOUNDS.put("mob.zombie.step", "mob.zombie.step");
        SOUNDS.put("mob.zombie.unfect", "mob.zombie.unfect");
        SOUNDS.put("mob.zombie.wood", "mob.zombie.wood");
        SOUNDS.put("mob.zombie.woodbreak", "mob.zombie.woodbreak");
        SOUNDS.put("mob.zombiepig.zpig", "mob.zombiepig.zpig");
        SOUNDS.put("mob.zombiepig.zpigangry", "mob.zombiepig.zpigangry");
        SOUNDS.put("mob.zombiepig.zpigdeath", "mob.zombiepig.zpigdeath");
        SOUNDS.put("mob.zombiepig.zpighurt", "mob.zombiepig.zpighurt");
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
        SOUNDS.put("random.anvil_break", "random.anvil_break");
        SOUNDS.put("random.anvil_land", "random.anvil_land");
        SOUNDS.put("random.anvil_use", "random.anvil_use");
        SOUNDS.put("random.bow", "random.bow");
        SOUNDS.put("random.bowhit", "random.bowhit");
        SOUNDS.put("random.break", "random.break");
        SOUNDS.put("random.breath", "");
        SOUNDS.put("random.burp", "random.burp");
        SOUNDS.put("random.chestclosed", "random.chestclosed");
        SOUNDS.put("random.chestopen", "random.chestopen");
        SOUNDS.put("random.classic_hurt", "game.neutral.hurt");
        SOUNDS.put("random.click", "random.click");
        SOUNDS.put("random.door_close", "random.door_close");
        SOUNDS.put("random.door_open", "random.door_open");
        SOUNDS.put("random.drink", "random.drink");
        SOUNDS.put("random.eat", "random.eat");
        SOUNDS.put("random.explode", "random.explode");
        SOUNDS.put("random.fizz", "random.fizz");
        SOUNDS.put("random.fuse", "creeper.primed"); //similar to: game.tnt.primed
        SOUNDS.put("random.glass", "dig.glass");
        SOUNDS.put("random.levelup", "random.levelup");
        SOUNDS.put("random.orb", "random.orb");
        SOUNDS.put("random.pop", "random.pop");
        SOUNDS.put("random.splash", "random.splash");
        SOUNDS.put("random.successful_hit", "random.successful_hit");
        SOUNDS.put("random.wood_click", "random.wood_click");
        SOUNDS.put("step.cloth", "step.cloth");
        SOUNDS.put("step.grass", "step.grass");
        SOUNDS.put("step.gravel", "step.gravel");
        SOUNDS.put("step.ladder", "step.ladder");
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
