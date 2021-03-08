package xyz.lightningcapes.sources.commands;

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
import java.util.List;
import java.util.concurrent.TimeUnit;

public final class SkinCommand extends Command {

    //INTELKA TY KURWO PRZESTAN MI TU ZLE PODPOWIADAC
    private final String dir = "downloaded/skins/";
    private final MongoCollection<Document> collection = Bootstrap.getInstance().getMongoDatabase().getCollection("skins");

    public SkinCommand(long id) {
        super("skin", id);
    }

    @Override
    public void handle(Member user, Message message, TextChannel textChannel, String... args) {
        message.delete().queue();
        List<Message.Attachment> attachments = message.getAttachments();
        if (args.length == 0 && attachments.isEmpty()) {
            textChannel.sendMessage(EmbedUtil.getEmbed("Wyslij zalacznik, nie pusta komende!",
                    "Musisz wyslac nar argument :thinking:", null))
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
        if (attachments.isEmpty()) {
            textChannel.sendMessage(EmbedUtil.getEmbed("Musisz zalaczyc obraz!",
                    "Zalacz obraz :thinking:",
                    name))
                    .delay(5, TimeUnit.SECONDS).flatMap(Message::delete).queue();
            return;
        }

        try {
            try (InputStream in = attachments.get(0).retrieveInputStream().get()) {
                if (in.available() > 1_000_000) {
                    textChannel.sendMessage(EmbedUtil.getEmbed("Błąd", "Limit wielkości pliku to 1 MB!", name))
                            .delay(5, TimeUnit.SECONDS)
                            .flatMap(Message::delete)
                            .queue();
                    return;
                }
                textChannel.sendMessage(EmbedUtil.getEmbed("Sukces", "Nadano twoj wlasny skin :exploding_head:", name))
                        .delay(5, TimeUnit.SECONDS).flatMap(Message::delete).queue();
                String path = dir + name + ".png";
                Files.copy(in, Paths.get(path), StandardCopyOption.REPLACE_EXISTING);
                Document nameDocument = new Document("name", name);
                collection.replaceOne(nameDocument, nameDocument.append("skin", "/" + path));
            }
        } catch (Exception e) {
            e.printStackTrace();
            textChannel.sendMessage(EmbedUtil.getEmbed("Wystapil blad!", "sadcenter cos zjebalx", null))
                    .delay(5, TimeUnit.SECONDS).flatMap(Message::delete).queue();
        }
    }
}
