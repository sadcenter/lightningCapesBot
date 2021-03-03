package xyz.sadcenter.lightning.bot.sources.commands.custom;

import com.mongodb.client.MongoCollection;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bson.Document;
import xyz.sadcenter.lightning.bot.Boostrap;
import xyz.sadcenter.lightning.bot.api.Command;
import xyz.sadcenter.lightning.bot.sources.utils.EmbedUtil;
import xyz.sadcenter.lightning.bot.sources.utils.RoleUtil;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.TimeUnit;

public final class CustomItemCommand extends Command {

    private final String dir = "downloaded/items/";
    private final MongoCollection<Document> collection;

    public CustomItemCommand(long id, MongoCollection<Document> collection) {
        super(id);
        this.collection = collection;
    }

    @Override
    public void handle(Member user, Message message, TextChannel textChannel, String... args) {
        if (args.length == 0 && message.getAttachments().isEmpty()) {
            message.delete().queue();
            return;
        }

        String name = Boostrap.getInstance().getInGameNamesManager().getName(user.getIdLong());

        if (!RoleUtil.hasRole(user, Boostrap.getInstance().getConfiguration().premiumId)) {
            textChannel.sendMessage(EmbedUtil.getEmbed("Hola hola :rage:",
                    "Nie posiadasz do tego dostepu :thinking:",
                    name))
                    .delay(5, TimeUnit.SECONDS).flatMap(Message::delete).queue();
            message.delete().queue();
            return;
        }

        if(message.getAttachments().isEmpty()) {
            textChannel.sendMessage(EmbedUtil.getEmbed("Musisz zalaczyc obraz!",
                    "Zalacz obraz :thinking:",
                    name))
                    .delay(5, TimeUnit.SECONDS).flatMap(Message::delete).queue();
            message.delete().queue();
            return;
        }

      //  String raw = args[0];
        //String[] split = raw.split("/");
        //String pathName = dir + split[split.length - 1];

        try {
            //FileUtils.copyURLToFile(new URL(raw), new File(pathName));

            try (InputStream in = message.getAttachments().get(0).retrieveInputStream().get()) {
                if (in.available() > 1_000_000) {
                    textChannel.sendMessage(EmbedUtil.getEmbed("Wielkosc pliku jest zbyt duza!", "Limit wielkosci itemu to 1mb!", name))
                            .delay(5, TimeUnit.SECONDS).flatMap(Message::delete).queue();
                    message.delete().queue();
                    return;
                }
                textChannel.sendMessage(EmbedUtil.getEmbed("Sukces", "Nadano twoj wlasny item :exploding_head:", name))
                        .delay(5, TimeUnit.SECONDS).flatMap(Message::delete).queue();
                String pathName = dir + name + ".png";
                Files.copy(in, Paths.get(pathName), StandardCopyOption.REPLACE_EXISTING);
                message.delete().queue();
                collection.replaceOne(new Document("name", name), new Document("item", "/" + pathName).append("name", name));
            }
        } catch (Exception e) {
            e.printStackTrace();
            textChannel.sendMessage(EmbedUtil.getEmbed("Wystapil blad!", "sadcenter cos zjebalx", null))
                    .delay(5, TimeUnit.SECONDS).flatMap(Message::delete).queue();
            message.delete().queue();
        }

    }


}
