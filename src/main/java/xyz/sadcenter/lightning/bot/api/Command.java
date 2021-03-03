package xyz.sadcenter.lightning.bot.api;


import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public abstract class Command {

    private final long channelId;

    public Command(long channelId) {
        this.channelId = channelId;
    }

    public abstract void handle(Member user, Message message, TextChannel textChannel, String... args);

    public long getChannelId() {
        return channelId;
    }

}
