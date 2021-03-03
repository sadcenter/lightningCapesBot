package xyz.sadcenter.lightning.bot.api.handler;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import xyz.sadcenter.lightning.bot.Boostrap;
import xyz.sadcenter.lightning.bot.api.Command;
import xyz.sadcenter.lightning.bot.sources.LightningBot;

import java.util.Arrays;

public class CommandHandler extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        if (event.getAuthor().isBot())
            return;


        String content = event.getMessage().getContentRaw();

        if (content.charAt(0) == LightningBot.PREFIX) {

            String[] raw = content.split(" ");
            String substring = raw[0].substring(1);

            Command command = Boostrap.getInstance()
                    .getCommands().get(Boostrap.getInstance().getCommands().keySet().stream().filter(s -> s.equalsIgnoreCase(substring)).findFirst().orElse("null"));

            if (command == null) {
                if (Boostrap.getInstance().getConfiguration().notWriteChannels.contains(event.getChannel().getIdLong())) {
                    event.getMessage().delete().queue();
                }
                return;
            }

            if (!event.getChannel().getId().equals(String.valueOf(command.getChannelId())))
                return;

            command.handle(event.getMember(), event.getMessage(), event.getChannel(), Arrays.copyOfRange(raw, 1, raw.length));
        } else if (Boostrap.getInstance().getConfiguration().notWriteChannels.contains(event.getChannel().getIdLong())) {
            event.getMessage().delete().queue();
        }

    }

}
