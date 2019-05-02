# Chatcord
A spigot plugin for discord chatting.

## What is this, how does it work?
Well, it is quite simple! This runs as a Discord bot but in the 
same time as a spigot plugin! ***NANI?!***

### Why as a bot though?
The bot part is crucial! You need a bot running so that
you could read messages off from Discord.  

### What else is needed?
In order for this to work you need to have:
- This plugin in the /plugin folder
- A Discord webhook

#### Why do I need to use a webhook now?
This plugin uses a Discord webhook to imitate fake users
on the chat channel. If you were to try to do this with
the bot only, it will look really bad or you would need
constantly to update your discord bot and have performance
issues on the server.

##### How do I get a discord webhook?
To get a discord webhook you can follow 
[this](https://support.discordapp.com/hc/en-us/articles/228383668-Intro-to-Webhooks)
link. 

## How do I set this up now?
### Pre-plugin setup
To set this up, you firstly need to go to [this](https://discordapp.com/developers/applications)
link to create the bot application you want. When you do that
add a bot to your application, invite the bot to your server and
then copy the token ID. You will as well need to create a webhook
for the channel you want to send messages in.

### Plugin setup
When you put your plugin, load it. (The preferred way would be
a complete stop and start of the server). 

After that head on to:
 
    /plugins/Chatcord/config.yml
 
 When you open this, you will have a couple of settings:
 - token
 - formatMinecraft
 - formatDiscord
 - webhookURL
 - prefix
 - message
 
 **token** - This is where you will put your copied bot token, it helps you connect
 to your bot. Plugin will disable if this is invalid.
 
 **formatMinecraft** - This is the format of the message in Minecraft when
 a Player sends a message. Plugin will disable if this is invalid.
 
 **formatDiscord** - This is the message format in Minecraft when a Discord
 user sends a message. Plugin will disable if this is invalid.
 
 **webhookURL** - This is where you place your webhook URL to connect
 to your webhook. Plugin will disable if this is invalid.
 
 **prefix** - This is the prefix of the ***set*** command. This can be
 empty but I would strongly recommend using something.
 
 **message** - This is the mood message for the bot. Can be empty.
 
 **default** - This is set to true when Chatcord is the default chat plugin.
 
 When you complete the config, restart the plugin, get on to the channel
 on Discord where you want to connect your chat to a simply use:
 
     ~set
     
Note, use *~set* when the prefix is ~. If the prefix was ! you would use
*!set*.

## How do I use this? 

Simply use this command to enter Discord chat and you're all done:

    /discordchat
   
