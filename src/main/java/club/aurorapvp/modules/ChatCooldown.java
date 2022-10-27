package club.aurorapvp.modules;

import static club.aurorapvp.AuroraChat.config;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.bukkit.entity.Player;

public class ChatCooldown {

  private static final HashMap<String, Integer> violations = new HashMap<>();
  private static final HashMap<String, Long> timeOfLastMessage = new HashMap<>();

  public static boolean checkCooldown(Player p) {
    if (timeOfLastMessage.containsKey(p.getName()) &&
        Math.abs(timeOfLastMessage.get(p.getName()) - System.currentTimeMillis()) <
            config.getLong("antispam.cooldown.time") * 1000) {
      violations.put(p.getName(), violations.get(p.getName()) + 1);
    } else if (!timeOfLastMessage.containsKey(p.getName()) &&
        Math.abs(timeOfLastMessage.get(p.getName()) - System.currentTimeMillis()) <
            config.getLong("antispam.cooldown.time") * 1000) {
      violations.put(p.getName(), 1);
    }
    timeOfLastMessage.put(p.getName(), System.currentTimeMillis());
    return violations.get(p.getName()) >= config.getInt("antispam.cooldown.max-violations");
  }

  public static void setup() {
    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    Runnable clearViolations = violations::clear;
    executor.scheduleAtFixedRate(clearViolations, 0,
        config.getInt("antispam.cooldown.violations-expire"), TimeUnit.SECONDS);
  }
}
