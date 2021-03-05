package xyz.lightningcapes.sources.commands.item;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bson.Document;
import xyz.lightningcapes.Bootstrap;
import xyz.lightningcapes.api.Command;
import xyz.lightningcapes.sources.utils.EmbedUtil;
import xyz.lightningcapes.sources.utils.ReaderUtils;
import xyz.lightningcapes.sources.utils.RoleUtil;

import java.io.File;
import java.util.concurrent.TimeUnit;

public final class PaidItemCommand extends Command {

    private final String stringPath = "/paid/items/";
    private final File path = new File(System.getProperty("user.dir") + stringPath);

    public PaidItemCommand(long id) {
        super("pitem", id);
    }

    @Override
    public void handle(Member user, Message message, TextChannel textChannel, String... args) {
        message.delete().queue();

        if (args.length != 1) {
            textChannel.sendMessage(EmbedUtil.getEmbed("Wyslij id, nie pusta komende!",
                    "Musisz nam wyslac id itemu ktory chcesz :thinking:", null))
                    .delay(5, TimeUnit.SECONDS).flatMap(Message::delete).queue();
            return;
        }

        String name = Bootstrap.getInstance().getInGameNamesManager().getName(user.getIdLong());

        if (!RoleUtil.hasRole(user, Bootstrap.getInstance().getConfiguration().premiumId)) {
            textChannel.sendMessage(EmbedUtil.getEmbed("Hola hola :rage:",
                    "Nie posiadasz do tego dostepu :thinking:", name))
                    .delay(5, TimeUnit.SECONDS).flatMap(Message::delete).queue();
            return;
        }

        String item = args[0];
        if (item.length() > 16) {
            return;
        }

        File found = ReaderUtils.found(path, item + ".png");
        if (found == null) {
            textChannel.sendMessage(EmbedUtil.getEmbed("Nie znaleziono takiego itemu :frowning:",
                    "Taka pelerynka nie istnieje, zobacz wszystkie perlerynki na odpowiednim kanale", name))
                    .delay(5, TimeUnit.SECONDS).flatMap(Message::delete).queue();
            return;
        }

        Document nameDocument = new Document("name", name);
        Bootstrap.getInstance().getMongoDatabase().getCollection("items")
                .replaceOne(nameDocument, nameDocument.append("item", stringPath + found.getName()));
        textChannel.sendMessage(EmbedUtil.getEmbed("Item zostal nadany :clap:",
                "Aby zobaczyc item zrestartuj minecrafta!", name))
                .delay(5, TimeUnit.SECONDS).flatMap(Message::delete).queue();
    }

}
