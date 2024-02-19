package club.aurorapvp.aurorachat.modules;

import club.aurorapvp.aurorachat.AuroraChat;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

public class AutoMessages {

  private static ScheduledExecutorService executor;
  private static String[] autoMessages;
  private static String[] joinMessages;
  private static String[] firstJoinMessages;

  private static final Runnable sendAutoMessage = () -> {
    String message = autoMessages[new Random().nextInt(autoMessages.length - 1)];

    for (Player player : Bukkit.getOnlinePlayers()) {
      player.sendMessage(MiniMessage.miniMessage().deserialize(message));
    }
  };

  public static void reload() {
    ConfigurationSection messages = AuroraChat.getInstance().getConfig()
        .getConfigurationSection("messages");

    assert messages != null;
    ConfigurationSection autoSection = messages.getConfigurationSection("auto-messages.messages");
    ConfigurationSection joinSection = messages.getConfigurationSection("join-messages");
    ConfigurationSection firstJoinSection =
        messages.getConfigurationSection("first-join-messages");

    assert autoSection != null;
    assert joinSection != null;
    assert firstJoinSection != null;

    Set<String> autoKeys = autoSection.getKeys(false);
    Set<String> joinKeys = joinSection.getKeys(false);
    Set<String> firstJoinKeys = firstJoinSection.getKeys(false);

    autoMessages = new String[autoKeys.size()];
    joinMessages = new String[joinKeys.size()];
    firstJoinMessages = new String[firstJoinKeys.size()];

    int i = 0;
    for (String key : autoKeys) {
      autoMessages[i] = autoSection.getString(key);
      i++;
    }

    i = 0;
    for (String key : joinKeys) {
      joinMessages[i] = joinSection.getString(key);
      i++;
    }

    i = 0;
    for (String key : firstJoinKeys) {
      firstJoinMessages[i] = firstJoinSection.getString(key);
      i++;

    }

    if (executor != null && !executor.isShutdown()) {
      executor.shutdownNow();
    }

    executor = Executors.newScheduledThreadPool(1);

    executor.scheduleAtFixedRate(sendAutoMessage, 0,
        AuroraChat.getInstance().getConfig().getLong("messages.auto-messages.interval"),
        TimeUnit.SECONDS);

    AuroraChat.getInstance().getLogger().log(Level.INFO, "AutoMessages module reloaded");
  }

  public static void sendJoinMessages(PlayerJoinEvent event) {
    Player player = event.getPlayer();

    for (String message : joinMessages) {
      player.sendMessage(MiniMessage.miniMessage().deserialize(message));
    }

    if (!player.hasPlayedBefore()) {
      return;
    }

    for (String message : firstJoinMessages) {
      player.sendMessage(MiniMessage.miniMessage().deserialize(message));
    }
  }
}
