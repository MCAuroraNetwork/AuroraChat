package club.aurorapvp.modules;

import static club.aurorapvp.AuroraChat.config;
import static club.aurorapvp.AuroraChat.serializeComponent;
import static club.aurorapvp.util.StringSimilarity.similarity;

import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class SimilarMessageBlocker {
  private static final HashMap<String, Integer> violations = new HashMap<>();
  private static HashMap<Component, String> messageContent = new HashMap<>();
  private static HashMap<Component, Long> messageTime = new HashMap<>();


  public static boolean violationChecker(Player p, Component message) {
    messageContent.put(message, p.getName());
    messageTime.put(message, System.currentTimeMillis());

    if (analyzeMessage(p, message)) {
      if (violations.containsKey(p.getName())) {
        violations.put(p.getName(), violations.get(p.getName()) + 1);
      } else {
        violations.put(p.getName(), 1);
      }
      return violations.get(p.getName()) >=
          config.getInt("antispam.similarity-detection.max-violations");
    }
    return false;
  }

  public static boolean analyzeMessage(Player p, Component message) {
    for (Component loggedMessages : messageContent.keySet()) {
      if (Objects.equals(messageContent.get(loggedMessages), p.getName())) {
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