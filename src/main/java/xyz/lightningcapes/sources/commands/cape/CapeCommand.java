package xyz.lightningcapes.sources.commands.cape;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bson.Document;
import xyz.lightningcapes.Bootstrap;
import xyz.lightningcapes.api.Command;
import xyz.lightningcapes.sources.utils.EmbedUtil;
import xyz.lightningcapes.sources.utils.ReaderUtils;

import java.io.File;
import java.util.concurrent.TimeUnit;

public final class CapeCommand extends Command {

    private final File tierDirs = new File("allcapes");

    public CapeCommand(long id) {
        super("cape", id);
    }

    @Override
    public void handle(Member user, Message message, TextChannel textChannel, String... args) {
        message.delete().queue();
        if (args.length == 0) {
            textChannel.sendMessage(EmbedUtil.getEmbed("Wyslij nick, nie pusta komende!",
                    "Musisz nam wyslac id itemu ktory chcesz :thinking:", null))
                    .delay(5, TimeUnit.SECONDS).flatMap(Message::delete).queue();
            return;
        }
        String name = Bootstrap.getInstance().getInGameNamesManager().getName(user.getIdLong());
        File found = ReaderUtils.found(tierDirs, args[0] + ".png");
        if (found == null) {
            textChannel.sendMessage(EmbedUtil.getEmbed("Taka pelerynka nie istnieje!",
                    "Takie id nie istnieje :thinking:", name))
                    .delay(5, TimeUnit.SECONDS).flatMap(Message::delete).queue();
            return;
        }
        Bootstrap.getInstance().getMongoDatabase().getCollection("capes")
                .replaceOne(new Document("name", name), new Document("name", name).append("cape", "/allcapes/" + found.getName()));
        textChannel.sendMessage(EmbedUtil.getEmbed("Nadano pelerynke!",
                "Aby zobaczyć nową pelerynkę zrestartuj mc :exploding_head:", name))
                .delay(5, TimeUnit.SECONDS)
                .flatMap(Message::delete)
                .queue();
    }
}