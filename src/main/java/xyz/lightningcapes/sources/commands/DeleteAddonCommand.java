package xyz.lightningcapes.sources.commands;

import com.mongodb.client.MongoDatabase;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bson.Document;
import xyz.lightningcapes.Bootstrap;
import xyz.lightningcapes.api.Command;
import xyz.lightningcapes.sources.utils.EmbedUtil;

import java.util.concurrent.TimeUnit;

public final class DeleteAddonCommand extends Command {

    public DeleteAddonCommand(long channelId) {
        super("usun", channelId);
    }

    @Override
    public void handle(Member user, Message message, TextChannel textChannel, String... args) {
        message.delete().queue();
        if (args.length == 0) {
            return;
        }

        MongoDatabase mongoDatabase = Bootstrap.getInstance().getMongoDatabase();

        String name = Bootstrap.getInstance().getInGameNamesManager().getName(user.getIdLong());
        String addonType = args[0];
        Document nameDocument = new Document("name", name);
        if (addonType.equalsIgnoreCase("cape")) {
            mongoDatabase.getCollection("capes").replaceOne(nameDocument, nameDocument.append("cape", "null"));
            textChannel.sendMessage(EmbedUtil.getEmbed("Usunieto twoja pelerynke!", "Twoja pelerynka zostala usunieta", name))
                    .delay(5, TimeUnit.SECONDS).flatMap(Message::delete).queue();
        } else if (addonType.equalsIgnoreCase("wings")) {
            mongoDatabase.getCollection("wings").replaceOne(nameDocument, nameDocument.append("wings", "null"));
            textChannel.sendMessage(EmbedUtil.getEmbed("Usunieto twoje skrzydla!", "Twoje skrzydla zostaly usuniete", name))
                    .delay(5, TimeUnit.SECONDS).flatMap(Message::delete).queue();
        } else if (addonType.equalsIgnoreCase("item")) {
            mongoDatabase.getCollection("items").replaceOne(nameDocument, nameDocument.append("item", "null"));
            textChannel.sendMessage(EmbedUtil.getEmbed("Usunieto twoj itemek!", "Twoj itemek zostal usuniety", name))
                    .delay(5, TimeUnit.SECONDS).flatMap(Message::delete).queue();
        } else if (addonType.equalsIgnoreCase("skin")) {
            mongoDatabase.getCollection("skins").replaceOne(nameDocument, nameDocument.append("skin", "null"));
            textChannel.sendMessage(EmbedUtil.getEmbed("Usunieto twoj itemek!", "Twoj itemek zostal usuniety", name))
                    .delay(5, TimeUnit.SECONDS).flatMap(Message::delete).queue();
        } else if (addonType.equalsIgnoreCase("hat")) {
            mongoDatabase.getCollection("hats").replaceOne(nameDocument, nameDocument.append("hat", "null"));
            textChannel.sendMessage(EmbedUtil.getEmbed("Usunieto twoja czapke!", "Twoja czapka zostala usunieta!", name))
                    .delay(5, TimeUnit.SECONDS).flatMap(Message::delete).queue();
        } else {
            textChannel.sendMessage(EmbedUtil.getEmbed("Poprawne uzycie: " + Bootstrap.getInstance().getPREFIX() + "usun cape/wings/item/skin/hat!", ":thinking:", name))
                    .delay(5, TimeUnit.SECONDS).flatMap(Message::delete).queue();
        }


    }
}
