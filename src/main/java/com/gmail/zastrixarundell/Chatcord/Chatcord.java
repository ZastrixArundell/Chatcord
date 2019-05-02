package com.gmail.zastrixarundell.Chatcord;

import com.gmail.zastrixarundell.Chatcord.Commands.SpigotEnterChat;
import com.gmail.zastrixarundell.Chatcord.Entities.DiscordBot;
import com.gmail.zastrixarundell.Chatcord.Listeners.SpigotMessageListener;
import me.clip.placeholderapi.metrics.bukkit.Metrics;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class Chatcord extends JavaPlugin
{

    @Override
    public void onEnable()
    {
        saveDefaultConfig();
        saveResource("placeholders.txt", true);

        new SpigotEnterChat(this);
        new SpigotMessageListener(this);

        if(getToken() == null)
        {
            getLogger().severe("The token is not set! Disabling");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        new DiscordBot(this, getToken());

        if(isEnabled())
        {
            if(getChatFormatDiscord() == null)
            {
                getLogger().severe("The discord chat format is not set! Disabling");
                getServer().getPluginManager().disablePlugin(this);
                return;
            }

            if(getChatFormatMinecraft() == null)
            {
                getLogger().severe("The minecraft chat format is not set! Disabling");
                getServer().getPluginManager().disablePlugin(this);
                return;
            }

            if(getwebhookURL() == null)
            {
                getLogger().severe("The webhook URL is not set! Disabling");
                getServer().getPluginManager().disablePlugin(this);
            }
        }

        Metrics metrics = new Metrics(this);

    }

    private String getToken()
    {
        return getConfig().getString("token");
    }

    @Override
    public void onDisable() {
        if(DiscordBot.isEnabled())
            DiscordBot.shutdown();
    }

    public static String getPrefix()
    {
        return ChatColor.GRAY + "[" + ChatColor.AQUA + "Chatcord" +
                ChatColor.GRAY + "] " + ChatColor.RESET;
    }

    public String getChannelId() { return getConfig().getString("channel"); }

    public String getChatFormatDiscord() { return getConfig().getString("formatDiscord"); }

    public String getChatFormatMinecraft() { return getConfig().getString("formatMinecraft"); }

    public String getwebhookURL() { return getConfig().getString("webhookURL"); }

    public String getCommandPrefix() { return getConfig().getString("prefix"); }

    public String getActivity() { return getConfig().getString("message"); }

    public boolean isDefault() { return getConfig().getBoolean("default"); }
}
