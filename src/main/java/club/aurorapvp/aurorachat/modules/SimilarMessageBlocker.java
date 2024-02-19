package club.aurorapvp.aurorachat.modules;

import club.aurorapvp.aurorachat.AuroraChat;
import club.aurorapvp.aurorachat.util.StringUtil;
import io.papermc.paper.event.player.AsyncChatEvent;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;

public class SimilarMessageBlocker {

  private static final Map<Player, Set<AbstractMap.SimpleEntry<String, Long>>> playerMessages =
      new HashMap<>();
  private static ViolationHandler violationHandler;
  private static double similarityThreshold;
  private static double messageTimeout;

  public static void reload() {
    violationHandler = new ViolationHandler(
        AuroraChat.getInstance().getConfig().getLong("antispam.cooldown.violations-expire") * 1000,
        AuroraChat.getInstance().getConfig().getInt("antispam.cooldown.max-violations"));
    similarityThreshold = AuroraChat.getInstance().getConfig().getDouble("antispam.similarity-detection.similarity");
    messageTimeout = AuroraChat.getInstance().getConfig().getDouble("antispam.similarity-detection.timeout");

    AuroraChat.getInstance().getLogger().log(Level.INFO, "ChatCooldown module reloaded");
  }

  public static void onAsyncChat(AsyncChatEvent event) {
    if (!AuroraChat.getInstance().getConfig().getBoolean("antispam.similarity-detection.enable")) {
      return;
    }

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

        if (System.currentTimeMillis() - oldTime < messageTimeout * 1000) {
          violationHandler.addViolation(p, event);
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
}