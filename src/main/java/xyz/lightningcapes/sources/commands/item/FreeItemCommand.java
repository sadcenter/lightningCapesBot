package xyz.lightningcapes.sources.commands.item;

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

public final class FreeItemCommand extends Command {

    private final String stringPath = "/free/items/";
    private final File path = new File(System.getProperty("user.dir") + stringPath);

    public FreeItemCommand(long id) {
        super("fitem", id);
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

        String name = Bootstrap.getInstance().getInGameNamesManager().getName(user.getIdLong());

        String cape = args[0];
        if (cape.length() > 16) {
            return;
        }

        File found = ReaderUtils.found(path, args[0] + ".png");
        if (found == null) {
            textChannel.sendMessage(EmbedUtil.getEmbed("Nie znaleziono takiego itemu :frowning:",
                    "Taki item nie istnieje, zobacz wszystkie perlerynki na odpowiednim kanale",
                    name))
                    .delay(5, TimeUnit.SECONDS).flatMap(Message::delete).queue();
            return;
        }


        Bootstrap.getInstance().getMongoDatabase().getCollection("items").replaceOne(new Document("name", name), new Document("name", name).append("item", stringPath + found.getName()));
        textChannel.sendMessage(EmbedUtil.getEmbed("Item zostal nadany :clap:",
                "Aby zobaczyc item zrestartuj minecrafta!",
                name))
                .delay(5, TimeUnit.SECONDS).flatMap(Message::delete).queue();
    }

}
