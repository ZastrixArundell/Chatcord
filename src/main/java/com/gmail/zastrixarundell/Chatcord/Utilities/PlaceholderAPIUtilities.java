package com.gmail.zastrixarundell.Chatcord.Utilities;

import org.bukkit.Bukkit;

public class PlaceholderAPIUtilities
{

    public static boolean isEnabled()
    {
        return Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;
    }

}
