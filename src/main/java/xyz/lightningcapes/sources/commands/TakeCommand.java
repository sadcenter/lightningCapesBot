package xyz.lightningcapes.sources.commands;

import com.mongodb.client.MongoDatabase;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bson.Document;
import xyz.lightningcapes.Bootstrap;
import xyz.lightningcapes.api.Command;
import xyz.lightningcapes.sources.utils.EmbedUtil;
import xyz.lightningcapes.sources.utils.RegisterUtil;

import java.util.concurrent.TimeUnit;

public final class TakeCommand extends Command {

    public TakeCommand(long id) {
        super("zajmij", id);
    }

    @Override
    public void handle(Member user, Message message, TextChannel textChannel, String... args) {
        message.delete().queue();
        if (args.length == 0) {
            textChannel.sendMessage(EmbedUtil.getEmbed("Wyslij nick, nie pusta komende!",
                    "Musisz nam podac twoj nick :thinking:", null))
                    .delay(5, TimeUnit.SECONDS).flatMap(Message::delete).queue();
            return;
        }

        String name = args[0];
        if (name.length() > 16 || name.length() <= 2 || !RegisterUtil.isAlphanumeric(name)) {
            textChannel.sendMessage(EmbedUtil.getEmbed("Podales za dlugi, za krotki lub nie alfanumeryczny nick!",
                    "Nie da sie miec takiego nicku w minecrafcie :frowning:", null))
                    .delay(5, TimeUnit.SECONDS).flatMap(Message::delete).queue();
            return;
        }

        if (Bootstrap.getInstance().getInGameNamesManager().exists(name)) {
            textChannel.sendMessage(EmbedUtil.getEmbed("Takie konto jest juz zarejestrowane :frowning:",
                    "Ktos z takim nickiem ma juz konto.",
                    name))
                    .delay(5, TimeUnit.SECONDS).flatMap(Message::delete).queue();
            return;
        }

        if (Bootstrap.getInstance().getInGameNamesManager().getName(user.getIdLong()) != null) {
            textChannel.sendMessage(EmbedUtil.getEmbed("Jestes juz zarejestrowany :thinking:",
                    "Masz juz zarejestrowanego :thinking:",
                    name))
                    .delay(5, TimeUnit.SECONDS).flatMap(Message::delete).queue();
            return;
        }

        user.getGuild().addRoleToMember(user.getIdLong(), Bootstrap.getInstance().getRoleCache().getRegisteredRole()).queue();
        Bootstrap.getInstance().getInGameNamesManager().register(user.getIdLong(), name);
        textChannel.sendMessage(EmbedUtil.getEmbed("Zarejestrowano!",
                "Zostales zarejestrowany :clap:", name))
                .delay(5, TimeUnit.SECONDS).flatMap(Message::delete).queue();
        MongoDatabase mongoDatabase = Bootstrap.getInstance().getMongoDatabase();
        mongoDatabase.getCollection("capes")
                .insertOne(new Document("name", name).append("cape", "/allcapes/0001.png"));

        mongoDatabase.getCollection("wings")
                .insertOne(new Document("name", name).append("wings", "null"));

        mongoDatabase.getCollection("items")
                .insertOne(new Document("name", name).append("item", "null"));

        mongoDatabase.getCollection("hats")
                .insertOne(new Document("name", name).append("hat", "null"));

        mongoDatabase.getCollection("skins")
                .insertOne(new Document("name", name).append("skin", "null"));

    }

}
