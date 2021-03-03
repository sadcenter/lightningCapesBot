package xyz.sadcenter.lightning.bot.sources.commands;

import com.mongodb.client.MongoDatabase;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bson.Document;
import xyz.sadcenter.lightning.bot.Boostrap;
import xyz.sadcenter.lightning.bot.api.Command;
import xyz.sadcenter.lightning.bot.sources.enums.Kit;
import xyz.sadcenter.lightning.bot.sources.utils.EmbedUtil;
import xyz.sadcenter.lightning.bot.sources.utils.ReaderUtils;
import xyz.sadcenter.lightning.bot.sources.utils.RoleUtil;

import java.io.File;
import java.util.concurrent.TimeUnit;

public final class KitCommand extends Command {

    public KitCommand(long channelId) {
        super(channelId);
    }

    @Override
    public void handle(Member user, Message message, TextChannel textChannel, String... args) {
        message.delete().queue();

        if (args.length == 0) {
            textChannel.sendMessage(EmbedUtil.getEmbed("Wyslij argument, nie pusta komende!",
                    "Musisz nam wyslac nam nazwe zestawu ktory chcesz :thinking:", null))
                    .delay(5, TimeUnit.SECONDS).flatMap(Message::delete).queue();
            return;
        }

        String name = Boostrap.getInstance().getInGameNamesManager().getName(user.getIdLong());
        String cape = args[0];
        if (cape.length() > 16) {
            return;
        }

        Kit found = Kit.getEquals(cape);

        if(found == null) {
            textChannel.sendMessage(EmbedUtil.getEmbed(":thinking:",
                    "Nie posiadasz do tego dostepu :thinking:",
                    name))
                    .delay(5, TimeUnit.SECONDS).flatMap(Message::delete).queue();
            return;
        }

        if (!RoleUtil.hasRole(user, found.getRankId())) {
            textChannel.sendMessage(EmbedUtil.getEmbed("Hola hola :rage:",
                    "Nie posiadasz do tego dostepu :thinking:",
                    name))
                    .delay(5, TimeUnit.SECONDS).flatMap(Message::delete).queue();
            return;
        }


        MongoDatabase mongoDatabase = Boostrap.getInstance().getMongoDatabase();
        mongoDatabase.getCollection("wings").replaceOne(new Document("name", name), new Document("name", name).append("wings", found.getWingsName()));
        mongoDatabase.getCollection("capes").replaceOne(new Document("name", name), new Document("name", name).append("cape", found.getCapeName()));
        mongoDatabase.getCollection("hats").replaceOne(new Document("name", name), new Document("name", name).append("hat", found.getHatName()));
        mongoDatabase.getCollection("items").replaceOne(new Document("name", name), new Document("name", name).append("item", found.getItemName()));

        textChannel.sendMessage(EmbedUtil.getEmbed("Skrzydla zostaly nadane :clap:",
                "Aby zobaczyc skrzydla zrestartuj minecrafta!",
                name))
                .delay(5, TimeUnit.SECONDS).flatMap(Message::delete).queue();
    }
}
