package xyz.lightningcapes.sources.commands.custom;

import com.google.common.collect.ImmutableMap;
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
import java.util.List;
import java.util.concurrent.TimeUnit;

public final class CustomCapeCommand extends Command {

    private final String dir = "downloaded/capes/";

    public CustomCapeCommand(long id) {
        super("customcape", id);
    }

    @Override
    public void handle(Member user, Message message, TextChannel textChannel, String... args) {
        List<Message.Attachment> attachments = message.getAttachments();
        message.delete().queue();
        if (args.length == 0 && attachments.isEmpty()) {
            textChannel.sendMessage(EmbedUtil.getEmbed(user, "Wystąpił błąd", null, ImmutableMap.<String, String>builder()
                    .put("Opis błędu", "Złe użycie komendy.")
                    .build()))
                    .delay(5, TimeUnit.SECONDS)
                    .flatMap(Message::delete)
                    .queue();
            return;
        }

        String name = Bootstrap.getInstance().getInGameNamesManager().getName(user.getIdLong());

        if (!RoleUtil.hasRole(user, Bootstrap.getInstance().getConfiguration().premiumId)) {
            textChannel.sendMessage(EmbedUtil.getEmbed(user, "Wystąpił błąd", null, ImmutableMap.<String, String>builder()
                    .put("Opis błędu", "Nie posiadasz do tego dostępu.")
                    .build()))
                    .delay(5, TimeUnit.SECONDS)
                    .flatMap(Message::delete)
                    .queue();
            return;
        }

        //String raw = args[0];
        //String[] split = raw.split("/");
        //String pathName = dir + split[split.length - 1];

        if (attachments.isEmpty()) {
            textChannel.sendMessage(EmbedUtil.getEmbed(user, "Wystąpił błąd", null, ImmutableMap.<String, String>builder()
                    .put("Opis błędu", "Musisz załączyć obraz do wiadomości.")
                    .build()))
                    .delay(5, TimeUnit.SECONDS)
                    .flatMap(Message::delete)
                    .queue();
            return;
        }

        try {
            //FileUtils.copyURLToFile(new URL(raw), new File(pathName));
            try (InputStream in = attachments.get(0).retrieveInputStream().get()) {
                if (in.available() > 100_000) {
                    textChannel.sendMessage(EmbedUtil.getEmbed(user, "Wystąpił błąd", null, ImmutableMap.<String, String>builder()
                            .put("Opis błędu", "Limit wielkości pliku to 1 MB")
                            .build()))
                            .delay(5, TimeUnit.SECONDS)
                            .flatMap(Message::delete)
                            .queue();
                    return;
                }
                textChannel.sendMessage(EmbedUtil.getEmbed("Sukces", "Nadano twoja wlasna pelerynke :exploding_head:", name))
                        .delay(5, TimeUnit.SECONDS).flatMap(Message::delete).queue();
                String path = dir + name + ".png";
                Files.copy(in, Paths.get(path), StandardCopyOption.REPLACE_EXISTING);
                Bootstrap.getInstance().getMongoDatabase().getCollection("capes").replaceOne(new Document("name", name), new Document("name", name).append("cape", "/" + path));
            }
        } catch (Exception e) {
            e.printStackTrace();
            textChannel.sendMessage(EmbedUtil.getEmbed("Wystapil blad!", "sadcenter cos zjebalx", null))
                    .delay(5, TimeUnit.SECONDS).flatMap(Message::delete).queue();
        }
    }
}