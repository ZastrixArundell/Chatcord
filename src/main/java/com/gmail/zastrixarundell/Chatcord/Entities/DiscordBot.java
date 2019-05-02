package com.gmail.zastrixarundell.Chatcord.Entities;

import com.gmail.zastrixarundell.Chatcord.Chatcord;
import com.gmail.zastrixarundell.Chatcord.Commands.DiscordChannelConnect;
import com.gmail.zastrixarundell.Chatcord.Listeners.DiscordMessageListener;
import org.bukkit.scheduler.BukkitRunnable;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

import java.util.logging.Level;

public class DiscordBot
{

    private static DiscordApi api;
    private static boolean isEnabled = false;

    public DiscordBot(Chatcord plugin, String token)
    {

        try
        {
            api = new DiscordApiBuilder().setToken(token).login().join();
        } catch (Exception e) {
            plugin.getLogger().log(Level.INFO, "Failed to start bot! Check the Token!");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
            return;
        }

        plugin.getLogger().log(Level.INFO, "The bot has been started!");

        api.addListener(new DiscordMessageListener(plugin));
        api.addListener(new DiscordChannelConnect(plugin));

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                String message = plugin.getActivity() != null ?
                        plugin.getActivity() : "";

                    api.updateActivity(message);

            }
        }.runTaskTimer(plugin, 0, 10*60*20);

        isEnabled = true;
    }

    public static void shutdown()
    {
        try
        {

            //Using this so that you don't get any console errors
            Runnable runnable = () -> api.disconnect();

            Thread thread = new Thread(runnable);
            thread.start();

            Thread.sleep(1000);

        }
        catch (Exception ignore)
        {
            //Exception will be ignored
        }
    }

    public static DiscordApi getApiInstance()
    {
        return api;
    }

    public static boolean isEnabled() {
        return isEnabled;
    }
}