package com.gmail.zastrixarundell.Chatcord.Listeners;

import com.gmail.zastrixarundell.Chatcord.Chatcord;
import com.gmail.zastrixarundell.Chatcord.Commands.SpigotEnterChat;
import com.gmail.zastrixarundell.Chatcord.Utilities.ColorUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;

public class DiscordMessageListener implements MessageCreateListener
{

    private Chatcord plugin;

    public DiscordMessageListener(Chatcord plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public void onMessageCreate(MessageCreateEvent event)
    {

        if(!(event.getChannel() instanceof ServerTextChannel))
            return;

        if(!event.getMessageAuthor().isUser())
            return;

        if(plugin.getChannelId() == null)
        {
            plugin.getLogger().log(Level.SEVERE, "The channel is not set!");
            return;
        }

        if(!event.getChannel().getIdAsString().equals(plugin.getChannelId()))
            return;

        Message _message = event.getMessage();
        String messageContent = _message.getContent();

        if(_message.getAttachments().isEmpty())
            if(messageContent == null || messageContent.isEmpty())
                return;

        //Change <@36351684638464> to <@userName#0000>
        for(User user : _message.getMentionedUsers())
        {
            messageContent = messageContent.replace(user.getMentionTag(),
                    "@" + user.getDiscriminatedName());

            if(messageContent.contains("<@!" + user.getIdAsString() + ">"))
                messageContent = messageContent.replace("<@!" + user.getIdAsString() + ">",
                        "@" + user.getDiscriminatedName());

        }

        /*
            This does gives unhecked errors in the IDE but if you look at the
            if statements at the beginning of the method you can see that it
            is check if it is a ServerTextChannel and if the user is a
         */

        messageContent =
                translateColors(plugin.getChatFormatDiscord())
                        .replace("${username}", _message.getAuthor().getDiscriminatedName())
                        .replace("${nickname}", _message.getAuthor().getDisplayName())
                    + messageContent;

        if(messageContent.contains("${primaryrole}"))
            if(_message.getServer().get().getHighestRole(_message.getUserAuthor().get()).isPresent())
            {
                Role role = _message.getServer().get().getHighestRole(_message.getUserAuthor().get()).get();

                boolean skip = false;

                if(role.getName().equalsIgnoreCase("@everyone"))
                {
                    messageContent = messageContent.replace("${primaryrole}", "");
                    skip = true;
                }

                if(!skip)
                    if(role.getColor().isPresent())
                    {
                        java.awt.Color color = role.getColor().get();
                        org.bukkit.ChatColor colorChat = ColorUtil.fromRGB(color.getRed(), color.getGreen(), color.getBlue());
                        messageContent = messageContent.replace("${primaryrole}",
                                colorChat + role.getName());
                    }
                    else messageContent = messageContent.replace("${primaryrole}", role.getName());

            }

        //Set to max MC length
        messageContent = StringUtils.left(messageContent, 257);

        System.out.println(messageContent);

        TextComponent message =
                new TextComponent(messageContent);

        if(!_message.getContent().isEmpty())
        {
            if (messageContent.equals(" "))
                message = new TextComponent("");
            else
                message = new TextComponent(messageContent + " ");
        }

        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder(ChatColor.AQUA + "Click to get the user tag.").create()));

        message.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,
                "@" + event.getMessageAuthor().getDiscriminatedName()));

        for(int i=0; i < _message.getAttachments().size(); i++)
        {
            String url = _message.getAttachments().get(i).getUrl().toString();
            String name = _message.getAttachments().get(i).getFileName();

            BaseComponent[] baseComponent = new ComponentBuilder(name)
                    .color(ChatColor.DARK_AQUA).create();

            TextComponent attachmentComponent = new TextComponent(baseComponent);

            attachmentComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                    new ComponentBuilder("Click to open " + name).color(ChatColor.AQUA).create()));

            attachmentComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));

            message.addExtra(attachmentComponent);
        }

        if(!plugin.isDefault())
        {
            ArrayList<UUID> toRemove = new ArrayList<>();

            for (UUID uuid : SpigotEnterChat.getPlayers())
            {
                Player player = plugin.getServer().getPlayer(uuid);
                if (player == null)
                    toRemove.add(uuid);
                else
                    player.spigot().sendMessage(message);
            }

            toRemove.forEach(SpigotEnterChat::removePlayer);
        }
        else
            for(Player player : plugin.getServer().getOnlinePlayers())
                player.spigot().sendMessage(message);

    }

    private static String translateColors(String input)
    {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

}
