package com.mineaurion.aurionchat.common.locale;

import com.mineaurion.aurionchat.common.command.sender.Sender;
import com.mineaurion.aurionchat.common.config.Channel;
import com.mineaurion.aurionchat.common.plugin.AurionChatPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.TextComponent;

import java.util.List;

import static net.kyori.adventure.text.Component.*;
import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.TextDecoration.BOLD;

public interface Message {

    Component PREFIX_COMPONENT = text()
            .color(GRAY)
            .append(text('['))
            .append(text()
                    .decoration(BOLD, true)
                    .append(text(AurionChatPlugin.NAME, AQUA))
            )
            .append(text(']'))
            .build();

    static TextComponent prefixed(ComponentLike component){
        return text()
                .append(PREFIX_COMPONENT)
                .append(space())
                .append(component)
                .build();
    }
    Args0 COMMAND_NO_PERMISSION = () -> prefixed(text("You do no have permission to use this command")).color(RED);

    Args1<String> CHANNEL_JOIN = (channel) -> joinNewline(
            prefixed(text()
                    .color(AQUA)
                    .append(text(String.format("You have joined the channel %s", channel), WHITE))
            )
    );

    Args1<String> CHANNEL_LEAVE = (channel) -> joinNewline(
            prefixed(text()
                    .color(AQUA)
                    .append(text(String.format("You have leaved the channel %s", channel), WHITE))
            )
    );

    Args1<String> CHANNEL_SPY = (channel) -> joinNewline(
            prefixed(text()
                    .color(AQUA)
                    .append(text(String.format("You have listenned the channel %s", channel), WHITE))
            )
    );

    Args0 CHANNEL_ALLLISTEN = () -> prefixed(text("You have listenned to all channel")).color(WHITE);

    Args1<List<Channel>> CHANNEL_INFO = (channels) -> joinNewline(
            prefixed(text()
                    .color(AQUA)
                    .append(text("- ", WHITE))
                    .append(text("Player: "))
            ),
            prefixed(text()
                    .color(AQUA)
                    .append(text("- ", WHITE))
                    .append(text("Amount: "))
            )
    );

    Args1<String> CHANNEL_NOT_FOUND = (channel) -> joinNewline(
            prefixed(text()
                    .color(AQUA)
                    .append(text(String.format("The channel %s was not found", channel), WHITE))
            )
    );

    Args1<String> REQUIRED_ARGUMENT = name -> text()
            .color(DARK_GRAY)
            .append(text('<'))
            .append(text(name, GRAY))
            .append(text('>'))
            .build();

    Args1<String> OPTIONAL_ARGUMENT = name -> text()
            .color(DARK_GRAY)
            .append(text('['))
            .append(text(name, GRAY))
            .append(text(']'))
            .build();


    static Component joinNewline(final ComponentLike... components) {
        return join(JoinConfiguration.newlines(), components);
    }

    interface Args0 {
        Component build();

        default void send(Sender sender) {
            sender.sendMessage(build());
        }
    }

    interface Args1<A0> {
        Component build(A0 arg0);

        default void send(Sender sender, A0 arg0) {
            sender.sendMessage(build(arg0));
        }
    }

    interface Args2<A0, A1> {
        Component build(A0 arg0, A1 arg1);

        default void send(Sender sender, A0 arg0, A1 arg1) {
            sender.sendMessage(build(arg0, arg1));
        }
    }
}