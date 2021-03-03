package xyz.lightningcapes.sources;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import xyz.lightningcapes.api.handler.CommandHandler;
import xyz.lightningcapes.sources.commands.DeleteAddonCommand;
import xyz.lightningcapes.sources.commands.KitCommand;
import xyz.lightningcapes.sources.commands.SkinCommand;
import xyz.lightningcapes.sources.commands.TakeCommand;
import xyz.lightningcapes.sources.commands.admin.AdminUnRegisterCommand;
import xyz.lightningcapes.sources.commands.cape.CapeCommand;
import xyz.lightningcapes.sources.commands.custom.CustomCapeCommand;
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

    public static final char PREFIX = '!';

    private final MongoDatabase mongoDatabase;
    private final JDA api;

    private final InGameNamesManager inGameNamesManager;

    private final ConfigurationStorage configuration;

    private final CommandManager commandManager;

    @SneakyThrows
    public LightningBot() {
        this.configuration = new Configuration().getConfigurationStorage();
        System.out.println(configuration.mongoString);
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
                new AdminUnRegisterCommand(configuration.channelId),
                new PaidItemCommand(configuration.channelId),
                new CustomCapeCommand("customwings", configuration.channelId, mongoDatabase.getCollection("wings")),
                new CustomCapeCommand("customcape", configuration.channelId, mongoDatabase.getCollection("capes")),
                new CapeCommand(configuration.channelId));
        this.api = JDABuilder.createDefault(configuration.discordToken)
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .addEventListeners(new DiscordJoinLeaveListener())
                .addEventListeners(new CommandHandler())
                .build();

        this.inGameNamesManager = new InGameNamesManager(mongoDatabase.getCollection("inGameNicks"));

        //System.out.println(api.getInviteUrl(Permission.values()));
        start();

    }

    private void start() {
        new CustomStatusUpdateTask().startAsync();
    }
}