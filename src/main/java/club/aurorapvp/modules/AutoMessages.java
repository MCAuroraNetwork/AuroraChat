package club.aurorapvp.modules;

import static club.aurorapvp.AuroraChat.config;
import static club.aurorapvp.AuroraChat.deserializeComponent;
import static club.aurorapvp.listeners.EventListener.p;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import net.kyori.adventure.text.Component;

public class AutoMessages {

  public static void sendJoinMessages() {

    for (Object path : config.getConfigurationSection("messages.joinMessages")
        .getKeys(false).toArray()) {
      p.sendMessage(Component.text(config.getString("messages.joinMessages." + path)));
    }

    if (!p.hasPlayedBefore()) {
      for (Object path : config.getConfigurationSection("messages.firstJoinMessages")
          .getKeys(false).toArray()) {
        p.sendMessage(
            Component.text(config.getString("messages.firstJoinMessages." + path)));
      }
    }
  }

  public static void setup() {
    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    Runnable sendMessages = () -> {
      for (Object path : config.getConfigurationSection("messages.auto-messages")
          .getKeys(false).toArray()) {
        if (!path.equals("interval")) {
          p.sendMessage(deserializeComponent.deserialize(
              config.getString("messages.auto-messages." + path)));
        }
      }
    };
    executor.scheduleAtFixedRate(sendMessages, 0,
        config.getLong("messages.auto-messages.interval"), TimeUnit.SECONDS);
  }

}
