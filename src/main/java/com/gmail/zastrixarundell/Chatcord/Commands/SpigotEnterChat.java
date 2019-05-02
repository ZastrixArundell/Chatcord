package com.gmail.zastrixarundell.Chatcord.Commands;

import com.gmail.zastrixarundell.Chatcord.Chatcord;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class SpigotEnterChat implements CommandExecutor

{

    private Chatcord plugin;

    public SpigotEnterChat(Chatcord plugin)
    {
        this.plugin = plugin;
        plugin.getCommand("discordchat").setExecutor(this);
    }

    private static ArrayList<UUID> players = new ArrayList<>();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
    {

        if(!command.getName().equalsIgnoreCase("discordchat"))
            return false;

        if(!(commandSender instanceof Player))
        {
            commandSender.sendMessage(ChatColor.RED + "This command is only meant for players!");
            return true;
        }

        if(plugin.isDefault())
        {
            commandSender.sendMessage(Chatcord.getPrefix() + ChatColor.RED + "Chatcord is the default chat plugin so you may not " +
                    "leave it.");
            return true;
        }

        UUID uuid = ((Player) commandSender).getUniqueId();

        if(!players.contains(uuid))
        {
            commandSender.sendMessage(Chatcord.getPrefix() + ChatColor.GREEN + "Chatting on discord now!");
            players.add(uuid);
        }
        else
        {
            commandSender.sendMessage(Chatcord.getPrefix() + ChatColor.RED + "Not chatting on discord anymore!");
            players.remove(uuid);
        }

        return true;
    }

    public static ArrayList<UUID> getPlayers()
    {
        return players;
    }

    public static void removePlayer(UUID uuid)
    {
        players.remove(uuid);
    }
}
