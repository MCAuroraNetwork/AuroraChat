package club.aurorapvp.modules;

import static club.aurorapvp.AuroraChat.deserializeComponent;
import static club.aurorapvp.listeners.EventListener.p;

import club.aurorapvp.config.ConfigHandler;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TimedMessages {

  public static void setup() {
    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    Runnable sendMessages = () -> {
      for (Object path : ConfigHandler.get().getConfigurationSection("messages.auto-messages")
          .getKeys(false).toArray()) {
        if (!path.equals("interval")) {
          p.sendMessage(deserializeComponent.deserialize(
              ConfigHandler.get().getString("messages.auto-messages." + path)));
        }
      }
    };
    executor.scheduleAtFixedRate(sendMessages, 0,
        ConfigHandler.get().getLong("messages.auto-messages.interval"), TimeUnit.SECONDS);
  }
}
