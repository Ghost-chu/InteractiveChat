package com.loohp.interactivechat.hooks.discordsrv;

import com.loohp.interactivechat.InteractiveChat;
import com.loohp.interactivechat.registry.Registry;
import com.loohp.interactivechat.utils.ChatColorUtils;
import com.loohp.interactivechat.utils.ComponentFont;
import com.loohp.interactivechat.utils.ComponentReplacing;
import com.loohp.interactivechat.utils.InteractiveChatComponentSerializer;

import github.scarsz.discordsrv.api.ListenerPriority;
import github.scarsz.discordsrv.api.Subscribe;
import github.scarsz.discordsrv.api.events.GameChatMessagePreProcessEvent;
import github.scarsz.discordsrv.dependencies.kyori.adventure.text.Component;
import github.scarsz.discordsrv.dependencies.kyori.adventure.text.TextReplacementConfig;
import github.scarsz.discordsrv.dependencies.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

public class DiscordSRVEvents {

	@Subscribe(priority = ListenerPriority.LOWEST)
	public void onGameToDiscord(GameChatMessagePreProcessEvent event) {
		Component component = event.getMessageComponent();
		event.setMessageComponent(process(component));
	}
	/*
	@Subscribe(priority = ListenerPriority.LOWEST)
	public void onVentureChatBungeeToDiscord(VentureChatMessagePreProcessEvent event) {
		Component component = event.getMessageComponent();
		event.setMessageComponent(process(component));
	}
	*/
	public static Component process(Component component) {
		component = convert(ComponentReplacing.replace(convert(component), Registry.ID_PATTERN.pattern(), net.kyori.adventure.text.Component.empty())).replaceText(TextReplacementConfig.builder().match(ChatColorUtils.COLOR_TAG_PATTERN).replacement((result, builder) -> {
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
		return component;
	}
	
	public static Component convert(net.kyori.adventure.text.Component component) {
		return GsonComponentSerializer.gson().deserialize(InteractiveChatComponentSerializer.gson().serialize(component));
	}
	
	public static net.kyori.adventure.text.Component convert(Component component) {
		return InteractiveChatComponentSerializer.gson().deserialize(GsonComponentSerializer.gson().serialize(component));
	}

}
