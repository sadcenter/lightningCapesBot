package xyz.lightningcapes;

import xyz.lightningcapes.sources.LightningBot;

public class Bootstrap {

    private static LightningBot instance;

    public static void main(String[] args) {
        instance = new LightningBot();
    }

    public static LightningBot getInstance() {
        return instance;
    }
}