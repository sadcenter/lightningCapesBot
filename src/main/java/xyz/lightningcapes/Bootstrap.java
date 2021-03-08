package xyz.lightningcapes;

import lombok.Getter;
import xyz.lightningcapes.sources.LightningBot;

public class Bootstrap {

    @Getter
    private static LightningBot instance;

    public static void main(String[] args) {
        instance = new LightningBot();
    }
}