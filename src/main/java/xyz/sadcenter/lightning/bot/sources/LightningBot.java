package xyz.sadcenter.lightning.bot.sources;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.requests.GatewayIntent;
import xyz.sadcenter.lightning.bot.api.Command;
import xyz.sadcenter.lightning.bot.api.handler.CommandHandler;
import xyz.sadcenter.lightning.bot.sources.commands.DeleteAddonCommand;
import xyz.sadcenter.lightning.bot.sources.commands.KitCommand;
import xyz.sadcenter.lightning.bot.sources.commands.SkinCommand;
import xyz.sadcenter.lightning.bot.sources.commands.TakeCommand;
import xyz.sadcenter.lightning.bot.sources.commands.admin.AdminUnRegisterCommand;
import xyz.sadcenter.lightning.bot.sources.commands.cape.CapeCommand;
import xyz.sadcenter.lightning.bot.sources.commands.custom.CustomCapeCommand;
import xyz.sadcenter.lightning.bot.sources.commands.custom.CustomItemCommand;
import xyz.sadcenter.lightning.bot.sources.commands.hats.FreeHatCommand;
import xyz.sadcenter.lightning.bot.sources.commands.hats.PaidHatCommand;
import xyz.sadcenter.lightning.bot.sources.commands.item.FreeItemCommand;
import xyz.sadcenter.lightning.bot.sources.commands.item.PaidItemCommand;
import xyz.sadcenter.lightning.bot.sources.commands.wings.FreeWingsCommand;
import xyz.sadcenter.lightning.bot.sources.commands.wings.PaidWingsCommand;
import xyz.sadcenter.lightning.bot.sources.config.Configuration;
import xyz.sadcenter.lightning.bot.sources.config.ConfigurationStorage;
import xyz.sadcenter.lightning.bot.sources.listeners.DiscordJoinLeaveListener;
import xyz.sadcenter.lightning.bot.sources.manager.InGameNamesManager;
import xyz.sadcenter.lightning.bot.sources.tasks.CustomStatusUpdateTask;

import javax.security.auth.login.LoginException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public final class LightningBot {

    public static final char PREFIX = '!';

    private final MongoDatabase mongoDatabase;
    private final JDA api;

    private final InGameNamesManager inGameNamesManager;

    private final ConfigurationStorage configuration;

    private final Map<String, Command> commands = new ConcurrentHashMap<>();

    @SneakyThrows
    public LightningBot() throws LoginException {
        this.configuration = new Configuration().getConfigurationStorage();
        System.out.println(configuration.mongoString);
        this.mongoDatabase = MongoClients.create(configuration.mongoString).getDatabase(configuration.mongoDatabase);
        this.api = JDABuilder.createDefault(configuration.discordToken)
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .addEventListeners(new DiscordJoinLeaveListener())
                .addEventListeners(new CommandHandler())
                .build();

        this.inGameNamesManager = new InGameNamesManager(mongoDatabase.getCollection("inGameNicks"));

        System.out.println(api.getInviteUrl(Permission.values()));
        start();

    }

    private void start() {
        registerCommand("zajmij", new TakeCommand(configuration.takeID));
        registerCommand("fwings", new FreeWingsCommand(configuration.channelId));
        registerCommand("pwings", new PaidWingsCommand(configuration.channelId));
        registerCommand("fhat", new FreeHatCommand(configuration.channelId));
        registerCommand("phat", new PaidHatCommand(configuration.channelId));
        registerCommand("skin", new SkinCommand(configuration.channelId, mongoDatabase.getCollection("skins")));
        registerCommand("fitem", new FreeItemCommand(configuration.channelId));
        registerCommand("kit", new KitCommand(configuration.channelId));
        registerCommand("usun", new DeleteAddonCommand(configuration.channelId));
        registerCommand("unregister", new AdminUnRegisterCommand(configuration.adminCommandsChannel));
        registerCommand("pitem", new PaidItemCommand(configuration.channelId));
        //registerCommand("fwings", new (configuration.channelId));
        registerCommand("customcape", new CustomCapeCommand(configuration.channelId, mongoDatabase.getCollection("capes")));
        //registerCommand("customitem", new CustomItemCommand(configuration.channelId, mongoDatabase.getCollection("items")));
        registerCommand("customwings", new CustomCapeCommand(configuration.channelId, mongoDatabase.getCollection("wings")));
        registerCommand("cape", new CapeCommand(configuration.channelId));
        //registerCommand("", new PaidWingsCommand());
        //registerCommand("free", new FreeWingsCommand(configuration.channelId));
        new CustomStatusUpdateTask().startAsync();
    }


    public void registerCommand(String name, Command command) {
        this.commands.put(name, command);
    }

}
