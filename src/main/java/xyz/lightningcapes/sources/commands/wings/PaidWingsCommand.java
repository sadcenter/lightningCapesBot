package xyz.lightningcapes.sources.commands.wings;

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

public final class PaidWingsCommand extends Command {

    private final String stringPath = "/paid/wings/";
    private final File path = new File(System.getProperty("user.dir") + stringPath);

    public PaidWingsCommand(long id) {
        super("pwings", id);
    }

    @Override
    public void execute(Member user, Message message, TextChannel textChannel, String... args) {
        message.delete().queue();

        if (args.length == 0) {
            textChannel.sendMessage(EmbedUtil.getEmbed("Wyslij id, nie pusta komende!",
                    "Musisz nam wyslac id itemu ktory chcesz :thinking:", null))
                    .delay(5, TimeUnit.SECONDS).flatMap(Message::delete).queue();
            return;
        }

        String name = Bootstrap.getInstance().getInGameNamesManager().getName(user.getIdLong());

        if (!RoleUtil.hasRole(user, Bootstrap.getInstance().getConfiguration().premiumId)) {
            textChannel.sendMessage(EmbedUtil.getEmbed("Hola hola :rage:",
                    "Nie posiadasz do tego dostepu :thinking:",
                    name))
                    .delay(5, TimeUnit.SECONDS).flatMap(Message::delete).queue();
            return;
        }

        String cape = args[0];
        if (cape.length() > 16) {
            return;
        }

        File found = ReaderUtils.found(path, args[0] + ".png");
        if (found == null) {
            textChannel.sendMessage(EmbedUtil.getEmbed("Nie znaleziono takichs skrzydel :frowning:",
                    "Takie skrzydla nie istnieje, zobacz wszystkie skrzydla na odpowiednim kanale",
                    name))
                    .delay(5, TimeUnit.SECONDS).flatMap(Message::delete).queue();
            return;
        }

        Bootstrap.getInstance().getMongoDatabase().getCollection("wings").replaceOne(new Document("name", name), new Document("name", name).append("wings", stringPath + found.getName()));
        textChannel.sendMessage(EmbedUtil.getEmbed("Skrzydla zostaly nadane :clap:",
                "Aby zobaczyc skrzydla zrestartuj minecrafta!",
                name))
                .delay(5, TimeUnit.SECONDS).flatMap(Message::delete).queue();
    }

}
