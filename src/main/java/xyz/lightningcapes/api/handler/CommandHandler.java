package xyz.lightningcapes.api.handler;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import xyz.lightningcapes.Bootstrap;
import xyz.lightningcapes.api.Command;
import xyz.lightningcapes.sources.LightningBot;

import java.util.Arrays;

public class CommandHandler extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot())
            return;

        Message message = event.getMessage();
        String content = message.getContentRaw();
        TextChannel channel = event.getChannel();
        long channelId = channel.getIdLong();
        if (content.charAt(0) == LightningBot.PREFIX) {
            String[] raw = content.split(" ");
            if (raw.length == 0) {
                return;
            }
            String substring = raw[0].substring(1);
            Command command = Bootstrap.getInstance()
                    .getCommandManager().getByName(substring);

            if (command == null) {
                message.delete().queue();
                return;
            }

            if (command.getChannelId() != channelId) {
                message.delete().queue();
                return;
            }

            command.handle(event.getMember(), message, channel, Arrays.copyOfRange(raw, 1, raw.length));
        } else if (Bootstrap.getInstance().getConfiguration().notWriteChannels.contains(channelId)) {
            message.delete().queue();
        }
    }
}