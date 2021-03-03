package xyz.sadcenter.lightning.bot.sources.enums;

import java.util.Arrays;

public enum Kit {

    NORMAL(803895879962460171L, "/allcapes/0001.png", "/free/wings/0001.png", "/free/items/0001.png", "/free/hats/0001.png"),
    YOUTUBER(801020521985736775L, "/allcapes/0171.png", "/other/wings/yt.png", "/other/items/0001.png", "/other/items/0001.png"),
    STREAMER(814588722690523186L, "/allcapes/0172.png", "/other/wings/streamer.png", "/other/items/streamer.png", "/other/hats/streamer.png"),
    BOOSTER(801465703987281922L, "/allcapes/0188.png", "/other/wings/boost.png", "/other/items/boost.png", "/other/hats/boost.png"),
    SUPPORTER(808671312314236959L, "/allcapes/0189.png", "/other/wings/donator.png", "/other/items/donator.png", "/other/hats/donator.png"),
    PARTNER(800450897792204830L, "/allcapes/0190.png", "/other/wings/partner.png", "/other/items/partner.png", "/other/hats/partner.png"),
    NORM(814590871525392414L, "/allcapes/NORM.png", "/other/wings/NORM.png", "/other/items/NORM.png", "/other/hats/NORM.png"),
    LIDER(801036293302910986L, "/allcapes/0191.png", "/other/wings/LIDER.png", "/other/items/LIDER.png", "/other/hats/LIDER.png");

    private final long rankId;
    private final String capeName;
    private final String wingsName;
    private final String itemName;
    private final String hatName;

    Kit(long rankId, String capeName, String wingsName, String itemName, String hatName) {
        this.rankId = rankId;
        this.capeName = capeName;
        this.wingsName = wingsName;
        this.itemName = itemName;
        this.hatName = hatName;
    }


    public long getRankId() {
        return rankId;
    }

    public String getCapeName() {
        return capeName;
    }

    public String getWingsName() {
        return wingsName;
    }

    public String getItemName() {
        return itemName;
    }

    public String getHatName() {
        return hatName;
    }

    //

    public static Kit getEquals(String name) {
        return Arrays.stream(values()).filter(kit -> kit.name().equalsIgnoreCase(name)).findFirst().orElse(null);
    }


}
