package xyz.lightningcapes.sources.listeners;

import com.mongodb.client.MongoDatabase;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;
import xyz.lightningcapes.Bootstrap;

public final class DiscordJoinLeaveListener extends ListenerAdapter {

    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {
        long id = event.getUser().getIdLong();
        String name = Bootstrap.getInstance().getInGameNamesManager().getName(id);
        if (name == null)
            return;

        MongoDatabase mongoDatabase = Bootstrap.getInstance().getMongoDatabase();

        mongoDatabase
                .getCollection("inGameNicks")
                .deleteOne(new Document("id", id));

        mongoDatabase
                .getCollection("wings")
                .deleteOne(new Document("name", name));

        mongoDatabase
                .getCollection("capes")
                .deleteOne(new Document("name", name));

        mongoDatabase
                .getCollection("items")
                .deleteOne(new Document("name", name));

        mongoDatabase
                .getCollection("hats")
                .deleteOne(new Document("name", name));

        mongoDatabase
                .getCollection("skins")
                .deleteOne(new Document("name", name));
    }
}