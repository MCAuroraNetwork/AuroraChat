package club.aurorapvp.modules;

import static club.aurorapvp.AuroraChat.config;
import static club.aurorapvp.listeners.EventListener.p;

import net.kyori.adventure.text.Component;

public class HelpCommand {

  public static void helpCmd() {

    for (Object path : config.getConfigurationSection("messages.joinMessages")
        .getKeys(false).toArray()) {
      p.sendMessage(Component.text(config.getString("messages.help-command." + path)));
    }
  }
}