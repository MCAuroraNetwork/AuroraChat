package club.aurorapvp.modules;

import club.aurorapvp.AuroraChat;
import club.aurorapvp.config.Config;
import club.aurorapvp.config.Lang;
import club.aurorapvp.util.StringUtil;
import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;

public class SimilarMessageBlocker extends ViolationModule {
  private final Map<Player, Set<AbstractMap.SimpleEntry<String, Long>>> playerMessages =
      new HashMap<>();
  private double similarityThreshold;
  private long messageExpiryDelay;

  public SimilarMessageBlocker() {
    super("SimilarMessageBlocker", Config.get().getLong("antispam.similarity-detection.violations-expire") * 1000,
        Config.get().getInt("antispam.similarity-detection.max-violations"));
  }

  public void init(JavaPlugin plugin) {
    long startTime = System.currentTimeMillis();

    similarityThreshold = Config.get().getDouble("antispam.similarity-detection.similarity");
    messageExpiryDelay = Config.get().getLong("antispam.similarity-detection.timeout");

    Bukkit.getPluginManager().registerEvents(this, AuroraChat.INSTANCE);

    boolean enabled = Config.get().getBoolean("antispam.similarity-detection.enable");

    this.setEnabled(enabled);

    AuroraChat.INSTANCE.getLogger().info(
        "SimilarityDetection module loaded in " + (System.currentTimeMillis() - startTime) +
            "ms. Enabled: " + enabled);
  }

  protected void punish(Player p, Cancellable event) {
    event.setCancelled(true);

    p.sendMessage(Lang.getComponent("message-similarity-violation"));
  }

  @EventHandler
  public void onAsyncChat(AsyncChatEvent event) {
    Player p = event.getPlayer();
    Component message = event.message();

    String messageContent = PlainTextComponentSerializer.plainText().serialize(message);

    if (playerMessages.containsKey(p)) {
      Set<AbstractMap.SimpleEntry<String, Long>> messages = playerMessages.get(p);

      for (AbstractMap.SimpleEntry<String, Long> pair : messages) {
        String oldMessage = pair.getKey();
        long oldTime = pair.getValue();

        double distance = StringUtil.similarity(messageContent, oldMessage);

        if (!(distance <= similarityThreshold)) {
          break;
        }

        if (System.currentTimeMillis() - oldTime < messageExpiryDelay * 1000) {
          this.addViolation(p, event);

        } else {
          messages.remove(pair);
        }
      }

      if (this.getViolations(p) >= maxViolations) {
        this.punish(p, event);
      }

      messages.add(new AbstractMap.SimpleEntry<>(messageContent, System.currentTimeMillis()));
    } else {
      Set<AbstractMap.SimpleEntry<String, Long>> messages = new HashSet<>();
      messages.add(new AbstractMap.SimpleEntry<>(messageContent, System.currentTimeMillis()));
      playerMessages.put(p, messages);
    }
  }
}