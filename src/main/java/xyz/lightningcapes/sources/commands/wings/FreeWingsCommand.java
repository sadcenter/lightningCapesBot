package xyz.lightningcapes.sources.commands.wings;

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

public final class FreeWingsCommand extends Command {

    private final String stringPath = "/free/wings/";
    private final File path = new File(System.getProperty("user.dir") + stringPath);

    public FreeWingsCommand(long id) {
        super("fwings", id);
    }

    @Override
    public void handle(Member user, Message message, TextChannel textChannel, String... args) {
        message.delete().queue();

        if (args.length == 0) {
            textChannel.sendMessage(EmbedUtil.getEmbed("Wyslij id, nie pusta komende!",
                    "Musisz nam wyslac id itemu ktory chcesz :thinking:", null))
                    .delay(5, TimeUnit.SECONDS).flatMap(Message::delete).queue();
            return;
        }

        String cape = args[0];
        if (cape.length() > 16) {
            return;
        }

        String name = Bootstrap.getInstance().getInGameNamesManager().getName(user.getIdLong());

        File found = ReaderUtils.found(path, cape + ".png");
        if (found == null) {
            textChannel.sendMessage(EmbedUtil.getEmbed("Nie znaleziono takich skrzydel :frowning:",
                    "Takie skrzydla nie istnieja, zobacz wszystkie skrzydla na odpowiednim kanale",
                    name))
                    .delay(5, TimeUnit.SECONDS).flatMap(Message::delete).queue();
            return;
        }


        Bootstrap.getInstance().getMongoDatabase().getCollection("wings")
                .replaceOne(new Document("name", name), new Document("name", name).append("wings", stringPath + found.getName()));
        textChannel.sendMessage(EmbedUtil.getEmbed("Skrzydla zostaly nadane :clap:",
                "Aby zobaczyc skrzydla zrestartuj minecrafta!",
                name))
                .delay(5, TimeUnit.SECONDS).flatMap(Message::delete).queue();
    }

}
