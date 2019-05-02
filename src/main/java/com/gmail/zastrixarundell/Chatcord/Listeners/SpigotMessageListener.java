package com.gmail.zastrixarundell.Chatcord.Listeners;

import com.gmail.zastrixarundell.Chatcord.Chatcord;
import com.gmail.zastrixarundell.Chatcord.Commands.SpigotEnterChat;
import com.gmail.zastrixarundell.Chatcord.Entities.DiscordBot;
import com.gmail.zastrixarundell.Chatcord.Entities.DiscordWebhook;
import com.gmail.zastrixarundell.Chatcord.Utilities.PlaceholderAPIUtilities;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

import java.util.ArrayList;
import java.util.UUID;

public class SpigotMessageListener implements Listener
{

    private DiscordWebhook webHook;
    private Chatcord plugin;

    public SpigotMessageListener(Chatcord plugin)
    {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
        webHook = new DiscordWebhook(plugin.getwebhookURL());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void doStuff(AsyncPlayerChatEvent event)
    {

        if(!plugin.isDefault())
            if(!SpigotEnterChat.getPlayers().contains(event.getPlayer().getUniqueId()))
                return;

        event.setCancelled(true);

        String messageChat;

        if(PlaceholderAPIUtilities.isEnabled())
             messageChat = PlaceholderAPI.setPlaceholders(event.getPlayer(), translateColors(plugin.getChatFormatMinecraft())
                            .replace("${username}", event.getPlayer().getDisplayName()))
                            + event.getMessage();

        else
            messageChat = translateColors(plugin.getChatFormatMinecraft())
                    .replace("${username}", event.getPlayer().getDisplayName())
                    + event.getMessage();

        System.out.println(messageChat);

        if(!plugin.isDefault())
        {
            ArrayList<UUID> toRemove = new ArrayList<>();

            for (UUID uuid : SpigotEnterChat.getPlayers()) {
                Player player = plugin.getServer().getPlayer(uuid);
                if (player == null)
                    toRemove.add(uuid);
                else
                    player.sendMessage(messageChat);
            }

            toRemove.forEach(SpigotEnterChat::removePlayer);
        }
        else
            plugin.getServer().getOnlinePlayers().forEach(player -> player.sendMessage(messageChat));

        String messageWebhook = event.getMessage();

        DiscordApi bot = DiscordBot.getApiInstance();
        for(Server server : bot.getServers())
            for(User user : server.getMembers())
                if(messageWebhook.contains("@" + user.getDiscriminatedName()))
                    messageWebhook = messageWebhook.replace("@" + user.getDiscriminatedName(),
                            user.getMentionTag());

        webHook.setUsername(event.getPlayer().getName());
        webHook.setAvatarUrl("https://minotar.net/avatar/" + event.getPlayer().getName());
        webHook.setContent(messageWebhook);

        Runnable execute = () ->
        {
            try
            {
                webHook.execute();
            }
            catch (Exception e)
            {
                event.getPlayer().sendMessage(Chatcord.getPrefix() + ChatColor.RED +
                        "An error happened!");
                plugin.getLogger().severe("Error while sending message on webhook! Is the link correct?");
            }
        };

        Thread executeThread = new Thread(execute);
        executeThread.start();

    }

    private static String translateColors(String input)
    {
        return ChatColor.translateAlternateColorCodes('&', input);
    }


}
