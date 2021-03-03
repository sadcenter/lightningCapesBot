package xyz.sadcenter.lightning.bot.sources.commands.hats;

import lombok.SneakyThrows;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bson.Document;
import xyz.sadcenter.lightning.bot.Boostrap;
import xyz.sadcenter.lightning.bot.api.Command;
import xyz.sadcenter.lightning.bot.sources.utils.EmbedUtil;
import xyz.sadcenter.lightning.bot.sources.utils.ReaderUtils;
import xyz.sadcenter.lightning.bot.sources.utils.RoleUtil;

import java.io.File;
import java.util.concurrent.TimeUnit;

public final class PaidHatCommand extends Command {

    private final String stringPath = "/paid/hats/";
    private final File path = new File(System.getProperty("user.dir") + stringPath);

    public PaidHatCommand(long id) {
        super(id);
    }

    @SneakyThrows
    @Override
    public void handle(Member user, Message message, TextChannel textChannel, String... args) {
        message.delete().queue();

        if (args.length == 0) {
            textChannel.sendMessage(EmbedUtil.getEmbed("Wyslij id, nie pusta komende!",
                    "Musisz nam wyslac id itemu ktory chcesz :thinking:", null))
                    .delay(5, TimeUnit.SECONDS).flatMap(Message::delete).queue();
            return;
        }

        String name = Boostrap.getInstance().getInGameNamesManager().getName(user.getIdLong());

        if (!RoleUtil.hasRole(user, Boostrap.getInstance().getConfiguration().premiumId)) {
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
            textChannel.sendMessage(EmbedUtil.getEmbed("Nie znaleziono takiej czapki :frowning:",
                    "Taka czapka nie istnieje, zobacz wszystkie czapki na odpowiednim kanale",
                    name))
                    .delay(5, TimeUnit.SECONDS).flatMap(Message::delete).queue();
            return;
        }

        Boostrap.getInstance().getMongoDatabase().getCollection("hats").replaceOne(new Document("name", name), new Document("name", name).append("hat", stringPath + found.getName()));
        textChannel.sendMessage(EmbedUtil.getEmbed("Czapka zostala nadana :clap:",
                "Aby zobaczyc czapke zrestartuj minecrafta!",
                name))
                .delay(5, TimeUnit.SECONDS).flatMap(Message::delete).queue();
    }

}
