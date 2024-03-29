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


import com.viaversion.viaversion.libs.mcstructs.text.ATextComponent;
import com.viaversion.viaversion.libs.mcstructs.text.components.StringComponent;
import com.viaversion.viaversion.libs.mcstructs.text.components.TranslationComponent;
import com.viaversion.viaversion.libs.mcstructs.text.serializer.LegacyStringDeserializer;
import com.viaversion.viaversion.libs.mcstructs.text.serializer.TextComponentSerializer;
import com.viaversion.viaversion.libs.mcstructs.text.utils.TextUtils;

public class ChatComponentRewriter {

    public static String toClient(final String text) {
        final ATextComponent component = TextComponentSerializer.V1_6.deserialize(text);
        // Convert all section sign formatted strings to json formatted ones with styles so the formatting isn't reset on chat line split
        ATextComponent newComponent = TextUtils.replace(component, ".*", c -> LegacyStringDeserializer.parse(c.asSingleString(), true).setParentStyle(c.getStyle()));
        // Clickable URLs are handled clientside -> Add click events to the json components
        newComponent.forEach(c -> {
            if (c instanceof TranslationComponent) {
                final TranslationComponent translationComponent = (TranslationComponent) c;
                final Object[] args = translationComponent.getArgs();
                for (int i = 0; i < args.length; i++) {
                    if (args[i] instanceof ATextComponent) {
                        args[i] = TextUtils.makeURLsClickable((ATextComponent) args[i]);
                    } else {
                        args[i] = TextUtils.makeURLsClickable(new StringComponent(args[i].toString()));
                    }
                }
            }
        });
        newComponent = TextUtils.makeURLsClickable(newComponent);
        // Also convert "using" -> "with" in translatable components
        return TextComponentSerializer.V1_7.serialize(newComponent);
    }

    public static String toClientDisconnect(final String reason) {
        return TextComponentSerializer.V1_7.serialize(new StringComponent(reason));
    }

}
