package club.aurorapvp.modules;

import static club.aurorapvp.listeners.EventListener.p;

import club.aurorapvp.config.ConfigHandler;
import net.kyori.adventure.text.Component;

public class HelpCommand {

  public static void helpCmd() {

    for (Object path : ConfigHandler.get().getConfigurationSection("messages.joinMessages")
        .getKeys(false).toArray()) {
      p.sendMessage(Component.text(ConfigHandler.get().getString("messages.help-command." + path)));
    }
  }
}