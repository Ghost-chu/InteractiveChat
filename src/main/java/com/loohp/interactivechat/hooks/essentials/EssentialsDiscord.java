/*
 * This file is part of InteractiveChat.
 *
 * Copyright (C) 2022. LoohpJames <jamesloohp@gmail.com>
 * Copyright (C) 2022. Contributors
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
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.loohp.interactivechat.hooks.essentials;

import com.loohp.interactivechat.InteractiveChat;
import com.loohp.interactivechat.registry.Registry;
import com.loohp.interactivechat.utils.ChatColorUtils;
import com.loohp.interactivechat.utils.ComponentFont;
import net.essentialsx.api.v2.events.discord.DiscordChatMessageEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class EssentialsDiscord implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDiscordChatMessage(DiscordChatMessageEvent event) {
        Component component = LegacyComponentSerializer.legacySection().deserialize(event.getMessage());
        component = component.replaceText(TextReplacementConfig.builder().match(Registry.ID_PATTERN).replacement("").build()).replaceText(TextReplacementConfig.builder().match(ChatColorUtils.COLOR_TAG_PATTERN).replacement((result, builder) -> {
            String escape = result.group(1);
            String replacement = escape == null ? "" : escape;
            return builder.content(replacement);
        }).build());
        if (InteractiveChat.fontTags) {
            component = component.replaceText(TextReplacementConfig.builder().match(ComponentFont.FONT_TAG_PATTERN).replacement((result, builder) -> {
                String escape = result.group(1);
                String replacement = escape == null ? "" : escape;
                return builder.content(replacement);
            }).build());
        }
        event.setMessage(LegacyComponentSerializer.legacySection().serialize(component));
    }

}
