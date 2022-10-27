package club.aurorapvp.config;

import static club.aurorapvp.AuroraChat.config;

import java.io.IOException;
import java.util.HashMap;

public class ConfigHandler {
  private static HashMap<String, String> Defaults;

  public static void generateDefaults() throws IOException {
    Defaults.put("messages.firstJoinMessages.default",
        "<gradient:#FFAA00:#FF55FF>Welcome to Aurora PvP!");
    Defaults.put("messages.joinMessages.default",
        "<gradient:#FFAA00:#FF55FF>Use /kits to select a kit, and go to the kit creator to create a kit!");
    Defaults.put("messages.auto-messages.default",
        "<gradient:#FFAA00:#FF55FF>Remember to go to the kit creator to create a kit!");
    Defaults.put("antispam.enable", "true");
    Defaults.put("antispam.timeout", "180000");
    Defaults.put("antispam.similarity", ".80");
    Defaults.put("antispam.max-violations", "6");
    Defaults.put("antispam.violations-expire", "300");

    for (String path : Defaults.keySet()) {
      if (!config.contains(path) || config.getString(path) == null) {
        config.set(path, Defaults.get(path));
        config.save(config.getName());
      }
    }
  }
}
