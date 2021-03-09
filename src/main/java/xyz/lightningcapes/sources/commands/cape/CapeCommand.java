package xyz.lightningcapes.sources.commands.cape;

import com.google.common.collect.ImmutableMap;
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
        if (args.length != 1) {
            textChannel.sendMessage(EmbedUtil.getEmbed(user, "Wystąpił błąd", null, ImmutableMap.<String, String>builder()
                    .put("Opis błędu", "Musisz wysłać ID peleryny.")
                    .build()))
                    .delay(5, TimeUnit.SECONDS)
                    .flatMap(Message::delete)
                    .queue();
            return;
        }
        String name = Bootstrap.getInstance().getInGameNamesManager().getName(user.getIdLong());
        String capeId = args[0];
        File found = ReaderUtils.found(tierDirs, capeId + ".png");
        if (found == null) {
            textChannel.sendMessage(EmbedUtil.getEmbed(user, "Wystąpił błąd", null, ImmutableMap.<String, String>builder()
                    .put("Opis błędu", "Taka pelerynka nie istnieje.")
                    .build()))
                    .delay(5, TimeUnit.SECONDS)
                    .flatMap(Message::delete)
                    .queue();
            return;
        }
        Bootstrap.getInstance().getMongoDatabase().getCollection("capes")
                .replaceOne(new Document("name", name), new Document("name", name).append("cape", "/allcapes/" + found.getName()));
        textChannel.sendMessage(EmbedUtil.getEmbed(user, "Peleryna została nadana.", null, ImmutableMap.<String, String>builder()
                .put("Nick", name)
                .put("ID", capeId)
                .build()))
                .delay(5, TimeUnit.SECONDS)
                .flatMap(Message::delete)
                .queue();
    }
}