package xyz.lightningcapes.sources.listeners;

import com.mongodb.client.MongoDatabase;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;
import xyz.lightningcapes.Bootstrap;

public final class DiscordJoinLeaveListener extends ListenerAdapter {

    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {
        System.out.println("cos");
        Member member = event.getMember();
        String name = Bootstrap.getInstance().getInGameNamesManager().getName(member.getIdLong());
        if (name == null)
            return;

        MongoDatabase mongoDatabase = Bootstrap.getInstance().getMongoDatabase();

        mongoDatabase
                .getCollection("inGameNicks")
                .deleteOne(new Document("id", member.getIdLong()));

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

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
      /*  OffsetDateTime timeCreated = OffsetDateTime.now();
        boolean b = timeCreated.toInstant().toEpochMilli() > System.currentTimeMillis() - fourteenDays;
        System.out.println(b);
        if (b) {
            //event.getGuild().ban(event.getUser(), 1).queue();
            event.getUser().openPrivateChannel().complete().sendMessage("Twoje konto zostalo zbanowane! Twoje konto nie ma wiecej niz 14 dni :frowning:").queue();
            return;
        }

        event.getGuild().addRoleToMember(event.getMember().getId(), event.getGuild().getRoleById(Boostrap.getInstance().getConfiguration().unregisteredId)).queue();


       */
    }
}
