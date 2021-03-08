package xyz.lightningcapes.sources.utils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.time.OffsetDateTime;

public class EmbedUtil {

    public static MessageEmbed getEmbed(String title, String description, String name) {
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setTitle(title)
                .setDescription(description)
                .setTimestamp(OffsetDateTime.now())
                .setFooter("made by sadcenter & warsztat");
        if (name != null) embedBuilder.setThumbnail("https://mc-heads.net/head/" + name);
        return embedBuilder.build();
    }
}