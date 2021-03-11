package xyz.lightningcapes.sources.utils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.time.OffsetDateTime;
import java.util.Map;

public class EmbedUtil {

    public static MessageEmbed getEmbed(Member member, String title, String name, Map<String, String> fields) {
        User user = member.getUser();
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setTitle(title)
                .setColor(Color.YELLOW)
                .setFooter("LightningCapes | Użyte przez "+user.getName()+"#"+user.getDiscriminator());
        for (Map.Entry<String, String> field : fields.entrySet()) {
            embedBuilder.addField(field.getKey(), field.getValue(), true);
        }
        if (name != null) embedBuilder.setThumbnail("https://mc-heads.net/head/" + name);
        return embedBuilder.build();
    }

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