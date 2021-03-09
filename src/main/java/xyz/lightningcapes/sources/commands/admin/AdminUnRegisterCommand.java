package xyz.lightningcapes.sources.commands.admin;

import com.google.common.collect.ImmutableMap;
import com.mongodb.client.MongoDatabase;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bson.Document;
import xyz.lightningcapes.Bootstrap;
import xyz.lightningcapes.api.Command;
import xyz.lightningcapes.sources.utils.EmbedUtil;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class AdminUnRegisterCommand extends Command {

    public AdminUnRegisterCommand(long channelId) {
        super("unregister", channelId);
    }

    @Override
    public void handle(Member user, Message message, TextChannel textChannel, String... args) {
        message.delete().queue();
        List<Member> mentionedMembers = message.getMentionedMembers();
        if (args.length != 1 || mentionedMembers.isEmpty()) {
            return;
        }

        String admin = Bootstrap.getInstance().getInGameNamesManager().getName(user.getIdLong());
        long mentionedId = mentionedMembers.get(0).getIdLong();
        String name = Bootstrap.getInstance().getInGameNamesManager().getName(mentionedId);

        if (name == null) {
            textChannel.sendMessage(EmbedUtil.getEmbed(user, "Wystąpił błąd", null, ImmutableMap.<String, String>builder()
                    .put("Opis błędu", "Ten użytkownik nie jest zarejestrowany.")
                    .build()))
                    .delay(5, TimeUnit.SECONDS)
                    .flatMap(Message::delete)
                    .queue();
            return;
        }

        MongoDatabase mongoDatabase = Bootstrap.getInstance().getMongoDatabase();
        Document nameDocument = new Document("name", name);
        mongoDatabase.getCollection("inGameNicks").deleteOne(new Document("id", mentionedId));
        mongoDatabase.getCollection("wings").deleteOne(nameDocument);
        mongoDatabase.getCollection("capes").deleteOne(nameDocument);
        mongoDatabase.getCollection("hats").deleteOne(nameDocument);
        mongoDatabase.getCollection("items").deleteOne(nameDocument);
        Bootstrap.getInstance().getInGameNamesManager().getInGameCache().refresh(mentionedId);

        user.getGuild().removeRoleFromMember(mentionedId, Bootstrap.getInstance().getRoleCache().getRegisteredRole()).queue();
        textChannel.sendMessage(EmbedUtil.getEmbed(user, "Użytkownik został odrejestrowany.", name, ImmutableMap.<String, String>builder()
                .put("Nick", name)
                .build()))
                .delay(5, TimeUnit.SECONDS)
                .flatMap(Message::delete)
                .queue();
    }
}
