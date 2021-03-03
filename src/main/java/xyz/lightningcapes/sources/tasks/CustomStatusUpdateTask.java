package xyz.lightningcapes.sources.tasks;

import com.google.common.util.concurrent.AbstractScheduledService;
import net.dv8tion.jda.api.entities.Activity;
import xyz.lightningcapes.Bootstrap;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public final class CustomStatusUpdateTask extends AbstractScheduledService {

    private final List<String> activities = Arrays.asList(
            "by sadcenter & warsztat",
            "Czy wiesz, że nie pisze się lighting tylko lightning?",
            "Nie widzisz kanałów? Zajmij nick *duży mózg*",
            "LOVE FLOPPA",
            "Mój prefix to: !",
            "Nie reklamuj się, bo to nieładnie :P",
            "Premium ma dostęp do nadawania customowych skrzydeł i itemków!",
            "Ranga donator dostępna jest od 1zł i ma dostęp do wszystkich dodatków",
            "A wiedziałeś, że mamy około 70 dodatków i około 200 pelerynek?",
            "Booster, YouTuber, Streamer, Lider Gildi oraz partner serwera discord ma dostęp do premium",
            "Premium jest za 5 zaproszeń! * b o g a c t w o *",
            "FLOPPA JEST SUPER"
    );

    private int index = 0;

    @Override
    protected void runOneIteration() {
        if (index >= this.activities.size()) {
            index = 0;
        }
        Bootstrap.getInstance().getApi().getPresence().setActivity(Activity.playing(activities.get(index)));
        index++;
    }

    @Override
    protected Scheduler scheduler() {
        return Scheduler.newFixedDelaySchedule(0L, 10L, TimeUnit.SECONDS);
    }
}
