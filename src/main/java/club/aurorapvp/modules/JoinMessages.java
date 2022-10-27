package club.aurorapvp.modules;

import static club.aurorapvp.AuroraChat.config;
import static club.aurorapvp.listeners.EventListener.p;

import net.kyori.adventure.text.Component;

public class JoinMessages {

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
}
