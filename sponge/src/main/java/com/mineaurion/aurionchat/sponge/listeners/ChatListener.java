package com.mineaurion.aurionchat.sponge.listeners;

import com.mineaurion.aurionchat.api.AurionPacket;
import com.mineaurion.aurionchat.common.AurionChatPlayer;
import com.mineaurion.aurionchat.common.Utils;
import com.mineaurion.aurionchat.common.config.Channel;
import com.mineaurion.aurionchat.sponge.AurionChat;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.IsCancelled;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.message.PlayerChatEvent;
import org.spongepowered.api.util.Tristate;

import static net.kyori.adventure.text.serializer.gson.GsonComponentSerializer.gson;

public class ChatListener {

    private final AurionChat plugin;

    public ChatListener(AurionChat plugin){
        this.plugin = plugin;
    }

    @Listener @IsCancelled(Tristate.UNDEFINED)
    public void onPlayerChat(PlayerChatEvent event, @First ServerPlayer player) {
        if (event.isCancelled()) {
            return;
        }
        if (!player.hasPermission("aurionchat.chat.speak")) {
            event.setCancelled(true);
            return;
        }
        AurionChatPlayer aurionChatPlayer = this.plugin.getAurionChatPlayers().get(player.uniqueId());

        String currentChannel = aurionChatPlayer.getCurrentChannel();
        Channel channel = plugin.getConfigurationAdapter().getChannels().get(currentChannel);
        Component messageFormat = Utils.processMessage(
                channel.format,
                event.message(),
                aurionChatPlayer,
                channel.urlMode
        );
        AurionPacket.Builder packet = AurionPacket.chat(aurionChatPlayer, gson().serialize(messageFormat))
                .channel(currentChannel);
        try {
            plugin.getChatService().send(packet);
        } catch (Exception e) {
            this.plugin.getlogger().severe(e.getMessage());
        }
        event.setCancelled(true);  // TODO: need to remove that. Need to adapt rabbitmq to a fanout exchange for the chat.
    }
}
