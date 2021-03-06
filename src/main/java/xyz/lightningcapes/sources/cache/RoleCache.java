package xyz.lightningcapes.sources.cache;

import lombok.Getter;
import net.dv8tion.jda.api.entities.Role;
import xyz.lightningcapes.Bootstrap;

@Getter
public class RoleCache {

    private final Role registeredRole = Bootstrap.getInstance().getApi().getRoleById(Bootstrap.getInstance().getConfiguration().registeredId);
}