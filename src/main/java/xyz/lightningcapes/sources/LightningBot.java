package xyz.lightningcapes.sources;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import xyz.lightningcapes.api.handler.CommandHandler;
import xyz.lightningcapes.sources.commands.DeleteAddonCommand;
import xyz.lightningcapes.sources.commands.KitCommand;
import xyz.lightningcapes.sources.commands.SkinCommand;
import xyz.lightningcapes.sources.commands.TakeCommand;
import xyz.lightningcapes.sources.commands.admin.AdminUnRegisterCommand;
import xyz.lightningcapes.sources.commands.cape.CapeCommand;
import xyz.lightningcapes.sources.commands.custom.CustomCapeCommand;
import xyz.lightningcapes.sources.commands.custom.CustomItemCommand;
import xyz.lightningcapes.sources.commands.custom.CustomWingsCommand;
import xyz.lightningcapes.sources.commands.hats.FreeHatCommand;
import xyz.lightningcapes.sources.commands.hats.PaidHatCommand;
import xyz.lightningcapes.sources.commands.item.FreeItemCommand;
import xyz.lightningcapes.sources.commands.item.PaidItemCommand;
import xyz.lightningcapes.sources.commands.wings.FreeWingsCommand;
import xyz.lightningcapes.sources.commands.wings.PaidWingsCommand;
import xyz.lightningcapes.sources.config.Configuration;
import xyz.lightningcapes.sources.config.ConfigurationStorage;
import xyz.lightningcapes.sources.listeners.DiscordJoinLeaveListener;
import xyz.lightningcapes.sources.manager.CommandManager;
import xyz.lightningcapes.sources.manager.InGameNamesManager;
import xyz.lightningcapes.sources.tasks.CustomStatusUpdateTask;

@Getter
public final class LightningBot {

    private final char PREFIX = '!';
    private final JDA api;
    private final MongoDatabase mongoDatabase;
    private final InGameNamesManager inGameNamesManager;
    private final ConfigurationStorage configuration;
    private final CommandManager commandManager;

    @SneakyThrows
    public LightningBot() {
        this.configuration = new Configuration().getConfigurationStorage();
        this.mongoDatabase = MongoClients.create(configuration.mongoString).getDatabase(configuration.mongoDatabase);
        commandManager = new CommandManager(new TakeCommand(configuration.takeID),
                new FreeWingsCommand(configuration.channelId),
                new PaidWingsCommand(configuration.channelId),
                new FreeHatCommand(configuration.channelId),
                new PaidHatCommand(configuration.channelId),
                new SkinCommand(configuration.channelId, mongoDatabase.getCollection("skins")),
                new FreeItemCommand(configuration.channelId),
                new KitCommand(configuration.channelId),
                new DeleteAddonCommand(configuration.channelId),
                new AdminUnRegisterCommand(configuration.adminCommandsChannel),
                new PaidItemCommand(configuration.channelId),
                new CustomWingsCommand(configuration.channelId, mongoDatabase.getCollection("wings")),
                new CustomItemCommand(configuration.channelId, mongoDatabase.getCollection("items")),
                new CustomCapeCommand(configuration.channelId, mongoDatabase.getCollection("capes")),
                new CapeCommand(configuration.channelId));
        this.api = JDABuilder.create(configuration.discordToken, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES)
                .disableCache(CacheFlag.ACTIVITY, CacheFlag.VOICE_STATE, CacheFlag.EMOTE, CacheFlag.CLIENT_STATUS)
                .addEventListeners(new DiscordJoinLeaveListener(), new CommandHandler())
                .build();
        this.inGameNamesManager = new InGameNamesManager(mongoDatabase.getCollection("inGameNicks"));
        new CustomStatusUpdateTask().startAsync();
    }
}