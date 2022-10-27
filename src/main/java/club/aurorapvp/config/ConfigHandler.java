package club.aurorapvp.config;

import static club.aurorapvp.AuroraChat.DataFolder;
import static club.aurorapvp.AuroraChat.config;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class ConfigHandler {
  private static HashMap<String, String> Defaults = new HashMap<>();

  public static void generateDefaults() throws IOException {
    Defaults.put("messages.firstJoinMessages.default",
        "<gradient:#FFAA00:#FF55FF>Welcome to Aurora PvP!");
    Defaults.put("messages.joinMessages.default",
        "<gradient:#FFAA00:#FF55FF>Use /kits to select a kit, and go to the kit creator to create a kit!");
    Defaults.put("messages.auto-messages.default",
        "<gradient:#FFAA00:#FF55FF>Remember to go to the kit creator to create a kit!");
    Defaults.put("antispam.similarity-detection.enable", "true");
    Defaults.put("antispam.similarity-detection.timeout", "180000");
    Defaults.put("antispam.similarity-detection.similarity", ".80");
    Defaults.put("antispam.similarity-detection.max-violations", "6");
    Defaults.put("antispam.similarity-detection.violations-expire", "180");
    Defaults.put("antispam.cooldown.enable", "false");
    Defaults.put("antispam.cooldown.time", "0");
    Defaults.put("antispam.cooldown.max-violations", "6");
    Defaults.put("antispam.cooldown.violations-expire", "180");

    for (String path : Defaults.keySet()) {
      if (!config.contains(path) || config.getString(path) == null) {
        config.set(path, Defaults.get(path));
        config.save(new File(DataFolder, "config.yml"));
      }
    }
  }
}
