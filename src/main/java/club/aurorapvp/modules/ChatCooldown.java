package club.aurorapvp.modules;

import static club.aurorapvp.AuroraChat.config;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.bukkit.entity.Player;

public class ChatCooldown {

  private static final HashMap<UUID, Integer> violations = new HashMap<>();
  private static final HashMap<UUID, Integer> timeOfLastMessage = new HashMap<>();

  public static boolean checkCooldown(Player p) {

    if (timeOfLastMessage.containsKey(p.getUniqueId()) &&
        timeOfLastMessage.get(p.getUniqueId()) - System.currentTimeMillis() > 1000) {
      violations.put(p.getUniqueId(), violations.get(p.getUniqueId()) + 1);
    } else if (!timeOfLastMessage.containsKey(p.getUniqueId()) &&
        timeOfLastMessage.get(p.getUniqueId()) - System.currentTimeMillis() > 1000) {
      violations.put(p.getUniqueId(), 1);
    }

    return violations.get(p.getUniqueId()) >= config.getInt("antispam.cooldown.max-violations");
  }

  public static void setup() {
    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    Runnable clearViolations = violations::clear;
    executor.scheduleAtFixedRate(clearViolations, 0,
        config.getInt("antispam.cooldown.violations-expire"), TimeUnit.SECONDS);
  }
}
