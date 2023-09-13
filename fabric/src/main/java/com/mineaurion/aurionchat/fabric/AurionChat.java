package com.mineaurion.aurionchat.fabric;

import com.mineaurion.aurionchat.common.AbstractAurionChat;
import com.mineaurion.aurionchat.common.logger.PluginLogger;
import com.mineaurion.aurionchat.common.logger.Slf4jPluginLogger;
import com.mineaurion.aurionchat.fabric.command.ChatCommand;
import com.mineaurion.aurionchat.fabric.listeners.ChatListener;
import com.mineaurion.aurionchat.fabric.listeners.LoginListener;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import org.slf4j.LoggerFactory;

public class AurionChat extends AbstractAurionChat<AurionChatPlayer> implements DedicatedServerModInitializer {
    public static final String ID = "aurionchat";
    public static Config config;

    @Override
    public void onInitializeServer() {
        getlogger().info("AurionChat Initializing");
        config = new Config();
        config.load();
        ServerLifecycleEvents.SERVER_STARTED.register(server -> this.enable(
                Config.Rabbitmq.uri,
                Config.Options.spy,
                Config.Options.automessage,
                true
        ));

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> new ChatCommand(this, dispatcher));
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> this.disable());
    }

    @Override
    protected void registerPlatformListeners() {
        LoginListener loginListener = new LoginListener(this);
        ChatListener chatListener = new ChatListener(this);

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> loginListener.onPlayerJoin(handler.getPlayer()));
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> loginListener.onPlayerQuit(handler.getPlayer()));
        ServerMessageEvents.ALLOW_CHAT_MESSAGE.register(chatListener);
    }

    @Override
    protected void registerCommands() {
        // Nothing to do here for fabric
    }

    @Override
    protected void disablePlugin() {}

    @Override
    public PluginLogger getlogger() {
        return new Slf4jPluginLogger(LoggerFactory.getLogger(AurionChat.ID));
    }
}
