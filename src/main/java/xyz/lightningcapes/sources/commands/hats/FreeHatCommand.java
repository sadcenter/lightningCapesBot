package xyz.lightningcapes.sources.commands.hats;

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

public final class FreeHatCommand extends Command {

    private final String stringPath = "/free/hats/";
    private final File path = new File(System.getProperty("user.dir") + stringPath);

    public FreeHatCommand(long id) {
        super("fhat", id);
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

        File found = ReaderUtils.found(path, args[0] + ".png");
        if (found == null) {
            textChannel.sendMessage(EmbedUtil.getEmbed("Nie znaleziono takiej czapki :frowning:",
                    "Taka czapka nie istnieje, zobacz wszystkie czapki na odpowiednim kanale",
                    name))
                    .delay(5, TimeUnit.SECONDS).flatMap(Message::delete).queue();
            return;
        }


        Document nameDocument = new Document("name", name);
        Bootstrap.getInstance().getMongoDatabase().getCollection("hats")
                .replaceOne(nameDocument, nameDocument.append("hat", stringPath + found.getName()));
        textChannel.sendMessage(EmbedUtil.getEmbed("Czapka zostala nadana :clap:",
                "Aby zobaczyc czapke zrestartuj minecrafta!",
                name))
                .delay(5, TimeUnit.SECONDS).flatMap(Message::delete).queue();
    }
}
