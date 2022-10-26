package club.aurorapvp.modules;

import static club.aurorapvp.listeners.EventListener.p;

import club.aurorapvp.config.ConfigHandler;
import net.kyori.adventure.text.Component;

public class JoinMessages {

  public static void sendJoinMessages() {

    for (Object path : ConfigHandler.get().getConfigurationSection("messages.joinMessages")
        .getKeys(false).toArray()) {
      p.sendMessage(Component.text(ConfigHandler.get().getString("messages.joinMessages." + path)));
    }

    if (!p.hasPlayedBefore()) {
      for (Object path : ConfigHandler.get().getConfigurationSection("messages.firstJoinMessages")
          .getKeys(false).toArray()) {
        p.sendMessage(
            Component.text(ConfigHandler.get().getString("messages.firstJoinMessages." + path)));
      }
    }
  }
}
