package xyz.lightningcapes.sources.utils;

import net.dv8tion.jda.api.entities.Member;

public final class RoleUtil {

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean hasRole(Member member, long id) {
        return member.getRoles().contains(member.getGuild().getRoleById(id));
    }
}