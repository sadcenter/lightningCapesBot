package xyz.lightningcapes.sources.config;

import com.google.common.collect.Sets;

import java.io.Serializable;
import java.util.Set;

public final class ConfigurationStorage implements Serializable {

    public String discordToken = "ODE1MzE5ODgwNTY3MDk1MzY4.YDqrzw.haxdxdxCMz48DBX6eEbnoermFIaurwE";

    public String mongoString = "mongodb://135.125.183.236:27017/?readPreference=primary&ssl=false";

    public String mongoDatabase = "lighting";

    public long takeID = 815691886408368128L;

    public long channelId = 815691886408368128L;

    public long premiumId = 815861122279538720L;

    public long unregisteredId = 815687865362808842L;

    public long registeredId = 815689908525727786L;

    public long adminCommandsChannel = 0L;

    public Set<Long> notWriteChannels = Sets.newHashSet(5L);


}
