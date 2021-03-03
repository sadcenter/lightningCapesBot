package xyz.sadcenter.lightning.bot.sources.commands.admin;

import com.mongodb.client.MongoDatabase;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bson.Document;
import xyz.sadcenter.lightning.bot.Boostrap;
import xyz.sadcenter.lightning.bot.api.Command;
import xyz.sadcenter.lightning.bot.sources.utils.EmbedUtil;

import java.util.List;

public class AdminUnRegisterCommand extends Command {

    public AdminUnRegisterCommand(long channelId) {
        super(channelId);
    }

    @Override
    public void handle(Member user, Message message, TextChannel textChannel, String... args) {
        message.delete().queue();
        List<Member> mentionedMembers = message.getMentionedMembers();
        if (args.length == 0 || mentionedMembers.isEmpty()) {
            return;
        }

        String admin = Boostrap.getInstance().getInGameNamesManager().getName(user.getIdLong());

        Member mentioned = mentionedMembers.get(0);
        String name = Boostrap.getInstance().getInGameNamesManager().getName(mentioned.getIdLong());

        if (name == null) {
            textChannel.sendMessage(EmbedUtil.getEmbed("Nie znaleziono takiego uzytkownika!", "Ten ziomek nie jest zarejestrowany :thinking:", admin))
                    .queue();
            return;
        }

        MongoDatabase mongoDatabase = Boostrap.getInstance().getMongoDatabase();
        mongoDatabase.getCollection("inGameNicks").deleteOne(new Document("id", mentioned.getIdLong()));
        mongoDatabase.getCollection("wings").deleteOne(new Document("name", name));
        mongoDatabase.getCollection("capes").deleteOne(new Document("name", name));
        mongoDatabase.getCollection("hats").deleteOne(new Document("name", name));
        mongoDatabase.getCollection("items").deleteOne(new Document("name", name));
        Boostrap.getInstance().getInGameNamesManager().getRaw().refresh(mentioned.getIdLong());

        mentioned.getGuild().removeRoleFromMember(mentioned.getIdLong(), mentioned.getGuild().getRoleById(Boostrap.getInstance().getConfiguration().registeredId)).queue();
        textChannel.sendMessage(EmbedUtil.getEmbed("Odrejestrowano typka!", "Typek odrejestrowany :thinking:", admin))
                .queue();
    }
}
