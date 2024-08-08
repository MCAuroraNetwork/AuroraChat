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
    ConfigurationSection joinSection = messages.getConfigurationSection("join-message");
    ConfigurationSection firstJoinSection = messages.getConfigurationSection("first-join-message");

    autoMessages = getStrings(autoSection);
    joinMessages = getStrings(joinSection);
    firstJoinMessages = getStrings(firstJoinSection);

    if (executor != null && !executor.isShutdown()) {
      executor.shutdownNow();
    }

    executor = Executors.newScheduledThreadPool(1);

    executor.scheduleAtFixedRate(sendAutoMessage, 0,
        AuroraChat.getInstance().getConfig().getLong("messages.auto-messages.interval"),
        TimeUnit.SECONDS);

    AuroraChat.getInstance().getLogger().log(Level.INFO, "AutoMessages module reloaded");
  }

  private static String[] getStrings(ConfigurationSection section) {
    if (section == null) {
      return new String[0];
    }

    Set<String> keys = section.getKeys(false);
    String[] strings = new String[keys.size()];

    int i = 0;
    for (String key : keys) {
      strings[i] = section.getString(key);
      i++;
    }

    return strings;
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
