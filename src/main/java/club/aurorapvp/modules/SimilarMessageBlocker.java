package club.aurorapvp.modules;

import static club.aurorapvp.AuroraChat.config;
import static club.aurorapvp.AuroraChat.serializeComponent;
import static club.aurorapvp.util.StringSimilarity.similarity;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class SimilarMessageBlocker {
  private static final HashMap<UUID, Integer> violations = new HashMap<>();
  private static Component message;
  private static HashMap<Component, UUID> messageContent = new HashMap<>();
  private static HashMap<Component, Long> messageTime = new HashMap<>();
  private static Player p;


  public static boolean violationChecker(Player p, Component message) {
    messageContent.put(message, p.getUniqueId());
    messageTime.put(message, System.currentTimeMillis());

    if (analyzeMessage()) {
      if (violations.containsKey(p.getUniqueId())) {
        violations.put(p.getUniqueId(), violations.get(p.getUniqueId()) + 1);
      } else {
        violations.put(p.getUniqueId(), 1);
      }
      return violations.get(p.getUniqueId()) >=
          config.getInt("antispam.similarity-detection.max-violations");
    }
    return false;
  }

  public static boolean analyzeMessage() {
    for (Component loggedMessages : messageContent.keySet()) {
      if (messageContent.get(loggedMessages) == p.getUniqueId()) {
        return similarity(String.valueOf(serializeComponent.serialize(loggedMessages)),
            String.valueOf(serializeComponent.serialize(message))) >=
            config.getDouble("antispam.similarity-detection.similarity");
      }
    }
    return false;
  }

  public static void setup() {
    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    Runnable checkValues = () -> {
      for (Component loggedMessages : messageTime.keySet()) {
        if (messageTime.get(loggedMessages) +
            config.getLong("antispam.similarity-detection.timeout") <=
            System.currentTimeMillis()) {

          messageTime.remove(loggedMessages);
          messageContent.remove(loggedMessages);
        }
      }
    };
    executor.scheduleAtFixedRate(checkValues, 0, 15, TimeUnit.SECONDS);
    Runnable clearViolations = violations::clear;
    executor.scheduleAtFixedRate(clearViolations, 0,
        config.getInt("antispam.similarity-detection.violations-expire"), TimeUnit.SECONDS);
  }
}