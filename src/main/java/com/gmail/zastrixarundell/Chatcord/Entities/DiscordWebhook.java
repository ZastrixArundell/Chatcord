package com.gmail.zastrixarundell.Chatcord.Entities;

import javax.json.Json;
import javax.json.JsonObject;
import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

public class DiscordWebhook {

    private final String webhookUrl;
    private String content;
    private String username;
    private String avatarUrl;

    public DiscordWebhook(String url) {
        this.webhookUrl = url;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void execute() throws IOException
    {
        JsonObject jsonObject = Json.createObjectBuilder()
                .add("content", content)
                .add("username", username)
                .add("avatar_url", avatarUrl)
                .build();

        URL url = new URL(webhookUrl);

        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.addRequestProperty("Content-Type", "application/json");
        connection.addRequestProperty("User-Agent", "Java application");
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");

        OutputStream stream = connection.getOutputStream();

        stream.write(jsonObject.toString().getBytes());

        stream.flush();
        stream.close();

        connection.getInputStream().close();
        connection.disconnect();
    }

}