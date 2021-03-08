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
        if (content.length() != 0 && content.charAt(0) == LightningBot.PREFIX) {
            String[] split = content.split(" ");
            int rawLength = split.length;
            if (rawLength == 0) {
                return;
            }
            Command command = Bootstrap.getInstance().getCommandManager()
                    .getByName(split[0].substring(1));

            if (command == null || command.getChannelId() != channelId) message.delete().queue();
            else command.handle(event.getMember(), message, channel, Arrays.copyOfRange(split, 1, rawLength));
        } else if (Bootstrap.getInstance().getConfiguration().notWriteChannels.contains(channelId)) {
            message.delete().queue();
        }
    }
}