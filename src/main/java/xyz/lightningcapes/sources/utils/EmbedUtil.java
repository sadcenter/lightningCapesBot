package xyz.lightningcapes.sources.utils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.Date;

public class EmbedUtil {

    public static MessageEmbed getEmbed(String title, String desc, String nick) {
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setTitle(title)
                .setDescription(desc)
                .setTimestamp(new Date().toInstant())
                .setFooter("made by sadcenter");
        if (nick != null) embedBuilder.setThumbnail("https://mc-heads.net/head/" + nick);

        return embedBuilder.build();
    }

    public static MessageEmbed getEmbedWithURL(String title, String desc, String URL) {
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setTitle(title)
                .setDescription(desc)
                .setTimestamp(new Date().toInstant())
                .setFooter("made by sadcenter");
        if (URL != null) embedBuilder.setThumbnail(URL);

        return embedBuilder.build();
    }


}
