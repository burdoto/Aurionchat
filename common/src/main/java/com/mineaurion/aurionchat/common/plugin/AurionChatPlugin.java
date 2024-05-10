package com.mineaurion.aurionchat.common.plugin;

import com.mineaurion.aurionchat.common.ChatService;
import com.mineaurion.aurionchat.common.config.ConfigurationAdapter;
import com.mineaurion.aurionchat.common.logger.PluginLogger;

public interface AurionChatPlugin {

    String MOD_ID = "aurionchat";

    String NAME = "AurionChat";

    AurionChatBootstrap getBootstrap();

    PluginLogger getLogger();

    ChatService getChatService();

    ConfigurationAdapter getConfigurationAdapter();
}
