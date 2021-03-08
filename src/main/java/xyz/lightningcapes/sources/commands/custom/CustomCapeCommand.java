package xyz.lightningcapes.sources.commands.custom;

import com.mongodb.client.MongoCollection;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bson.Document;
import xyz.lightningcapes.Bootstrap;
import xyz.lightningcapes.api.Command;
import xyz.lightningcapes.sources.utils.EmbedUtil;
import xyz.lightningcapes.sources.utils.RoleUtil;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.TimeUnit;

public final class CustomCapeCommand extends Command {

    private final String dir = "downloaded/capes/";

    public CustomCapeCommand(long id) {
        super("customcape", id);
    }

    @Override
    public void handle(Member user, Message message, TextChannel textChannel, String... args) {
        message.delete().queue();
        if (args.length == 0 && message.getAttachments().isEmpty()) {
            textChannel.sendMessage(EmbedUtil.getEmbed("Wyslij argument, nie pusta komende!",
                    "Musisz wyslac nam item ktory chcesz :thinking:", null))
                    .delay(5, TimeUnit.SECONDS).flatMap(Message::delete).queue();
            message.delete().queue();
            return;
        }

        String name = Bootstrap.getInstance().getInGameNamesManager().getName(user.getIdLong());

        if (!RoleUtil.hasRole(user, Bootstrap.getInstance().getConfiguration().premiumId)) {
            textChannel.sendMessage(EmbedUtil.getEmbed("Hola hola :rage:",
                    "Nie posiadasz do tego dostepu :thinking:",
                    name))
                    .delay(5, TimeUnit.SECONDS).flatMap(Message::delete).queue();
            message.delete().queue();
            return;
        }

        //String raw = args[0];
        //String[] split = raw.split("/");
        //String pathName = dir + split[split.length - 1];

        if (message.getAttachments().isEmpty()) {
            textChannel.sendMessage(EmbedUtil.getEmbed("Musisz zalaczyc obraz!",
                    "Zalacz obraz :thinking:",
                    name))
                    .delay(5, TimeUnit.SECONDS).flatMap(Message::delete).queue();
            message.delete().queue();
            return;
        }

        try {
            //FileUtils.copyURLToFile(new URL(raw), new File(pathName));
            try (InputStream in = message.getAttachments().get(0).retrieveInputStream().get()) {
                if (in.available() > 100_000) {
                    textChannel.sendMessage(EmbedUtil.getEmbed("Błąd", "Limit wielkości pliku to 1 MB!", name))
                            .delay(5, TimeUnit.SECONDS)
                            .flatMap(Message::delete)
                            .queue();
                    message.delete().queue();
                    return;
                }
                textChannel.sendMessage(EmbedUtil.getEmbed("Sukces", "Nadano twoja wlasna pelerynke :exploding_head:", name))
                        .delay(5, TimeUnit.SECONDS).flatMap(Message::delete).queue();
                String path = dir + name + ".png";
                Files.copy(in, Paths.get(path), StandardCopyOption.REPLACE_EXISTING);
                message.delete().queue();
                Bootstrap.getInstance().getMongoDatabase().getCollection("capes").replaceOne(new Document("name", name), new Document("name", name).append("cape", "/" + path));
            }
        } catch (Exception e) {
            e.printStackTrace();
            textChannel.sendMessage(EmbedUtil.getEmbed("Wystapil blad!", "sadcenter cos zjebalx", null))
                    .delay(5, TimeUnit.SECONDS).flatMap(Message::delete).queue();
        }
    }
}