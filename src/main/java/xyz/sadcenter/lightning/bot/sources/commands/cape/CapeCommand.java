package xyz.sadcenter.lightning.bot.sources.commands.cape;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bson.Document;
import xyz.sadcenter.lightning.bot.Boostrap;
import xyz.sadcenter.lightning.bot.api.Command;
import xyz.sadcenter.lightning.bot.sources.utils.EmbedUtil;
import xyz.sadcenter.lightning.bot.sources.utils.ReaderUtils;

import java.io.File;
import java.util.concurrent.TimeUnit;

public final class CapeCommand extends Command {

    private final File tierDirs = new File("allcapes");

    public CapeCommand(long id) {
        super(id);
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

        String cape = args[0];

        String name = Boostrap.getInstance().getInGameNamesManager().getName(user.getIdLong());
        File found = ReaderUtils.found(tierDirs, cape + ".png");
        if (found == null) {
            textChannel.sendMessage(EmbedUtil.getEmbed("Taka pelerynka nie istnieje!",
                    "Takie id nie istnieje :thinking:", name))
                    .delay(5, TimeUnit.SECONDS).flatMap(Message::delete).queue();
            return;
        }

        Boostrap.getInstance().getMongoDatabase().getCollection("capes").replaceOne(new Document("name", name), new Document("name", name).append("cape", "/allcapes/" + found.getName()));
        textChannel.sendMessage(EmbedUtil.getEmbed("Nadano pelerynke!",
                "Aby zobaczyc nowa pelerynke zrestartuj mc :exploding_head:", name))
                .delay(5, TimeUnit.SECONDS).flatMap(Message::delete).queue();

    }
}
