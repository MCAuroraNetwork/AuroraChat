package club.aurorapvp.aurorachat.modules;

import club.aurorapvp.aurorachat.AuroraChat;
import club.aurorapvp.aurorachat.util.StringUtil;
import io.papermc.paper.event.player.AsyncChatEvent;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class SimilarMessageBlocker {

  private static final Map<Player, Set<AbstractMap.SimpleEntry<String, Long>>> playerMessages =
      new HashMap<>();
  private static final Map<Player, Integer> violations = new HashMap<>();
  private static final Map<Player, Timer> nextViolationClear = new HashMap<>();
  private static long violationExpiryDelay;
  protected static int maxViolations;
  private static double similarityThreshold;
  private static long messageExpiryDelay;

  public static void reload() {
    violationExpiryDelay =
        AuroraChat.getInstance().getConfig()
            .getLong("antispam.similarity-detection.violations-expire") * 1000;
    maxViolations = AuroraChat.getInstance().getConfig()
        .getInt("antispam.similarity-detection.max-violations");
    similarityThreshold = AuroraChat.getInstance().getConfig()
        .getDouble("antispam.similarity-detection.similarity");
    messageExpiryDelay = AuroraChat.getInstance().getConfig()
        .getLong("antispam.similarity-detection.timeout");

    AuroraChat.getInstance().getLogger().log(Level.INFO, "SimilarMessageBlocker module reloaded");
  }

  public static void onAsyncChat(AsyncChatEvent event) {
    Player p = event.getPlayer();
    Component message = event.message();

    String messageContent = PlainTextComponentSerializer.plainText().serialize(message);

    if (playerMessages.containsKey(p)) {
      Set<AbstractMap.SimpleEntry<String, Long>> messages = playerMessages.get(p);

      for (AbstractMap.SimpleEntry<String, Long> pair : messages) {
        String oldMessage = pair.getKey();
        long oldTime = pair.getValue();

        double distance = StringUtil.similarity(messageContent, oldMessage);

        if (distance < similarityThreshold) {
          break;
        }

        if (System.currentTimeMillis() - oldTime < messageExpiryDelay * 1000) {
          SimilarMessageBlocker.addViolation(p, event);
          return;
        } else {
          messages.remove(pair);
        }
      }

      messages.add(new AbstractMap.SimpleEntry<>(messageContent, System.currentTimeMillis()));
    } else {
      Set<AbstractMap.SimpleEntry<String, Long>> messages = new HashSet<>();
      messages.add(new AbstractMap.SimpleEntry<>(messageContent, System.currentTimeMillis()));
      playerMessages.put(p, messages);
    }
  }

  private static void addViolation(Player p, Cancellable event) {
    violations.put(p, violations.getOrDefault(p, 0) + 1);

    Timer timer = nextViolationClear.get(p);

    if (timer != null) {
      timer.cancel();
    }

    timer = new Timer();
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        clearViolations(p);
      }
    }, SimilarMessageBlocker.getExpirationDelay());

    nextViolationClear.put(p, timer);

    if (violations.get(p) >= maxViolations) {
      event.setCancelled(true);

      p.sendMessage(AuroraChat.getInstance().getLang().getComponent("message-similarity-violation"));
    }
  }

  private static void clearViolations(Player p) {
    violations.put(p, 0);

    Timer timer = nextViolationClear.remove(p);
    if (timer != null) {
      timer.cancel();
    }
  }

  private static long getExpirationDelay() {
    return violationExpiryDelay;
  }
}