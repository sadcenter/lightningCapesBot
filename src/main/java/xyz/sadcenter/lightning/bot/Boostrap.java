package xyz.sadcenter.lightning.bot;

import xyz.sadcenter.lightning.bot.sources.LightningBot;

import javax.security.auth.login.LoginException;

public final class Boostrap {

    private static LightningBot bot;

    public static void main(String[] args) {
        try {
            bot = new LightningBot();
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

    public static LightningBot getInstance() {
        return bot;
    }
}
