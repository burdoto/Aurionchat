package com.mineaurion.aurionchat.api;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.mineaurion.aurionchat.api.model.Named;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Optional;

import static net.kyori.adventure.text.serializer.gson.GsonComponentSerializer.gson;

public final class AurionPacket implements Named, Serializable {

    public static final Gson gson = new Gson();

    public static Builder builder() {
        return new Builder();
    }

    public AurionPacket(Type type, String source, @Nullable AurionPlayer player, @Nullable String channel, @Nullable String displayName, @NotNull String tellRawData) {
        this.type = type;
        this.source = source;
        this.player = player;
        this.channel = channel;
        this.displayName = displayName;
        this.displayString = getStringFromComponent(getComponent());
        this.tellRawData = tellRawData;
    }

    public static Builder chat(AurionPlayer player, Object tellRaw) {
        return AurionPacket.builder()
                .type(Type.CHAT)
                .source("ingame")
                .player(player)
                .tellRawData(tellRaw.toString());
    }

    public static Builder autoMessage(Object tellRaw) {
        return AurionPacket.builder()
                .type(Type.AUTO_MESSAGE)
                .source("automessage")
                .displayName("AutoMessage")
                .tellRawData(tellRaw.toString());
    }

    /**
     * Packet type
     */
    private final Type type;

    /**
     * One of: servername, 'discord' or 'ingame' literal
     */
    private final String source;

    /**
     * Related player
     */
    @Nullable
    private final AurionPlayer player;

    /**
     * Channel name
     */
    @Nullable
    private final String channel;

    /**
     * Display name of sender (one of: player name, automessage title)
     */
    @Nullable
    private final String displayName;

    /**
     * Only the string of the message without any colors or stuff from the game
     */
    @NotNull
    private final String displayString;

    /**
     * What ingame players see
     */
    @NotNull
    private final String tellRawData;

    /**
     * What to display in plaintext environments
     */
    public String getRawDisplay() {
        return displayName + ' ' + type.name().toLowerCase() + displayString;
    }

    public Component getComponent() {
        return gson().deserialize(tellRawData);
    }

    public static String getStringFromComponent(Component component){
        StringBuilder content = new StringBuilder();

        if(!component.children().isEmpty()){
            component.children().forEach( child -> content.append(getStringFromComponent(child)));
        }

        if(component instanceof TextComponent){
            content.append(((TextComponent) component).content());
        }
        // todo: support other types
        //else if (it instanceof BlockNBTComponent) ;
        //else if (it instanceof EntityNBTComponent) ;
        //else if (it instanceof KeybindComponent) ;
        //else if (it instanceof ScoreComponent) ;
        //else if (it instanceof SelectorComponent) ;
        //else if (it instanceof StorageNBTComponent) ;
        //else if (it instanceof TranslatableComponent) ;
        return content.toString();
    }

    @Override
    public String toString() {
        return gson.toJson(this);
    }

    public static AurionPacket fromJson(String jsonString){
        return gson.fromJson(jsonString, AurionPacket.class);
    }

    @Override
    public @Nullable String getName() {
        return Optional.ofNullable(player)
                .map(Named::getBestName)
                .orElse(null)
                ;
    }

    @Override
    public @Nullable String getDisplayName() {
        return displayName;
    }

    public Type getType() {
        return this.type;
    }

    public String getSource() {
        return this.source;
    }

    public @Nullable AurionPlayer getPlayer() {
        return this.player;
    }

    public @Nullable String getChannel() {
        return this.channel;
    }

    public @NotNull String getDisplayString(){
        return this.displayString;
    }

    public @NotNull String getTellRawData() {
        return this.tellRawData;
    }

    public Builder toBuilder() {
        return new Builder()
                .type(this.type)
                .source(this.source)
                .player(this.player)
                .channel(this.channel)
                .displayName(this.displayName)
                .tellRawData(this.tellRawData)
        ;
    }

    public enum Type {
        @SerializedName("chat")
        CHAT,
        @SerializedName("automessage")
        AUTO_MESSAGE,
        @SerializedName("event_join")
        EVENT_JOIN,
        @SerializedName("event_achievement")
        EVENT_ACHIEVEMENT,
    }


    public static class Builder {
        private Type type;
        private String source;
        private @Nullable AurionPlayer player;
        private @Nullable String channel;
        private @Nullable String displayName;
        private @NotNull String tellRawData = "";

        public Builder type(Type type) {
            this.type = type;
            return this;
        }

        public Builder source(String source) {
            this.source = source;
            return this;
        }

        public Builder player(@Nullable AurionPlayer player) {
            this.player = player;
            return this;
        }

        public Builder channel(@Nullable String channel) {
            this.channel = channel;
            return this;
        }

        public Builder displayName(@Nullable String displayName) {
            this.displayName = displayName;
            return this;
        }

        public Builder tellRawData(@NotNull String tellRawData) {
            this.tellRawData = tellRawData;
            return this;
        }

        public AurionPacket build() {
            return new AurionPacket(this.type, this.source, this.player, this.channel, this.displayName, this.tellRawData);
        }
    }
}
