package com.gmail.zastrixarundell.Chatcord.Commands;

import com.gmail.zastrixarundell.Chatcord.Chatcord;
import com.gmail.zastrixarundell.Chatcord.Entities.DiscordBot;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

public class DiscordChannelConnect implements MessageCreateListener
{

    private Chatcord plugin;

    public DiscordChannelConnect(Chatcord plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public void onMessageCreate(MessageCreateEvent messageCreateEvent)
    {

        if(!(messageCreateEvent.getChannel() instanceof ServerTextChannel))
            return;

        String prefix = plugin.getCommandPrefix() != null ?
                plugin.getCommandPrefix() : "";

        if(!messageCreateEvent.getMessageContent().toLowerCase().equals(prefix + "set"))
            return;

        if(!messageCreateEvent.getMessageAuthor().isUser())
            return;

        MessageAuthor author = messageCreateEvent.getMessageAuthor();

        if(!author.isServerAdmin())
            return;

        setChannel(messageCreateEvent);
        sendEmbed(messageCreateEvent);
    }

    private void setChannel(MessageCreateEvent messageCreateEvent)
    {
        String id = messageCreateEvent.getChannel().getIdAsString();
        plugin.getConfig().set("channel", id);
        plugin.saveConfig();
    }

    private void sendEmbed(MessageCreateEvent messageCreateEvent)
    {
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Channel set!")
                .setImage(DiscordBot.getApiInstance().getYourself().getAvatar())
                .setDescription("This channel will now be used for chatting!");

        messageCreateEvent.getChannel().sendMessage(embed);
    }

}
