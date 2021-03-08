package xyz.lightningcapes.api;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

@Getter
@RequiredArgsConstructor
public abstract class Command {

    private final String name;
    private final long channelId;

    public abstract void handle(Member user, Message message, TextChannel textChannel, String... args);
}