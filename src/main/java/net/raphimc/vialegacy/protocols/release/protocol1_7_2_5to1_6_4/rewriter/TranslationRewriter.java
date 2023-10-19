/*
 * This file is part of ViaLegacy - https://github.com/RaphiMC/ViaLegacy
 * Copyright (C) 2023 RK_01/RaphiMC and contributors
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
import com.viaversion.viaversion.libs.gson.JsonObject;
import com.viaversion.viaversion.rewriter.ComponentRewriter;
import net.raphimc.vialegacy.protocols.release.protocol1_7_2_5to1_6_4.ClientboundPackets1_6_4;

public class TranslationRewriter {

    private static final Object2ObjectMap<String, String> TRANSLATIONS = new Object2ObjectOpenHashMap<>(37, 0.99F);

    static {
        TRANSLATIONS.put("menu.playdemo", "Play Demo World");
        TRANSLATIONS.put("options.ao.off", "Off");
        TRANSLATIONS.put("options.framerateLimit", "Performance");
        TRANSLATIONS.put("options.resourcepack", "Resource Packs");
        TRANSLATIONS.put("performance.max", "Max FPS");
        TRANSLATIONS.put("performance.balanced", "Balanced");
        TRANSLATIONS.put("performance.powersaver", "Power saver");
        TRANSLATIONS.put("key.forward", "Forward");
        TRANSLATIONS.put("key.left", "Left");
        TRANSLATIONS.put("key.back", "Back");
        TRANSLATIONS.put("key.right", "Right");
        TRANSLATIONS.put("key.drop", "Drop");
        TRANSLATIONS.put("key.chat", "Chat");
        TRANSLATIONS.put("key.fog", "Toggle Fog");
        TRANSLATIONS.put("key.attack", "Attack");
        TRANSLATIONS.put("key.use", "Use Item");
        TRANSLATIONS.put("key.command", "Command");
        TRANSLATIONS.put("resourcePack.title", "Select Resource Pack");
        TRANSLATIONS.put("tile.dirt.name", "Dirt");
        TRANSLATIONS.put("tile.sand.name", "Sand");
        TRANSLATIONS.put("tile.flower.name", "Flower");
        TRANSLATIONS.put("tile.rose.name", "Rose");
        TRANSLATIONS.put("item.fishRaw.name", "Raw Fish");
        TRANSLATIONS.put("item.fishCooked.name", "Cooked Fish");
        TRANSLATIONS.put("commands.give.usage", "/give <player> <item> [amount] [data]");
        TRANSLATIONS.put("commands.give.success", "Given %s (ID %s) * %s to %s");
        TRANSLATIONS.put("commands.scoreboard.objectives.add.wrongType", "Invalid objective criteria type. Valid types are: %s");
        TRANSLATIONS.put("commands.scoreboard.objectives.list.count", "Showing %s objective(s) on scoreboard");
        TRANSLATIONS.put("commands.scoreboard.players.list.count", "Showing %s tracked players on the scoreboard");
        TRANSLATIONS.put("commands.scoreboard.players.list.player.count", "Showing %s tracked objective(s) for %s");
        TRANSLATIONS.put("commands.scoreboard.teams.list.count", "Showing %s teams on the scoreboard");
        TRANSLATIONS.put("commands.scoreboard.teams.list.player.count", "Showing %s player(s) in team %s");
        TRANSLATIONS.put("commands.scoreboard.teams.empty.usage", "/scoreboard teams clear <name>");
        TRANSLATIONS.put("commands.scoreboard.teams.option.usage", "/scoreboard teams option <team> <friendlyfire|color> <value>");
        TRANSLATIONS.put("commands.weather.usage", "/weather <clear/rain/thunder> [duration in seconds]");
        TRANSLATIONS.put("mco.configure.world.subscription.extend", "Extend");
        TRANSLATIONS.put("mco.configure.world.restore.question.line1", "Your realm will be restored to a previous version");
    }

    private static final ComponentRewriter<ClientboundPackets1_6_4> REWRITER = new ComponentRewriter<ClientboundPackets1_6_4>(null, ComponentRewriter.ReadType.JSON) {
        @Override
        protected void handleTranslate(JsonObject object, String translate) {
            final String text = TRANSLATIONS.get(translate);
            if (text != null) {
                object.addProperty("translate", text);
            }
        }
    };

    public static String toClient(final String text) {
        return REWRITER.processText(text).toString();
    }

}
