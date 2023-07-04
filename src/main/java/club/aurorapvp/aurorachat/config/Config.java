package club.aurorapvp.aurorachat.config;

import club.aurorapvp.aurorachat.AuroraChat;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import org.bukkit.configuration.file.YamlConfiguration;

public class Config {
  private static final File FILE = new File(AuroraChat.INSTANCE.getDataFolder(), "config.yml");
  private static YamlConfiguration config;

  public static void init() {
    reload();
    generateDefaults();
  }

  public static void generateDefaults() {
    final HashMap<String, Object> DEFAULTS = new HashMap<>();

    DEFAULTS.put("messages.enabled", true);
    DEFAULTS.put("messages.first-join-messages.default",
        "<gradient:#FFAA00:#FF55FF>Welcome to Aurora PvP!");
    DEFAULTS.put("messages.join-messages.default",
        "<gradient:#FFAA00:#FF55FF>Use /kits to select a kit, and go to the kit creator to create a kit!");
    DEFAULTS.put("messages.auto-messages.default",
        "<gradient:#FFAA00:#FF55FF>Remember to go to the kit creator to create a kit!");
    DEFAULTS.put("messages.auto-messages-interval", 300);
    DEFAULTS.put("message-commands.enable", true);
    DEFAULTS.put("antispam.similarity-detection.enable", true);
    DEFAULTS.put("antispam.similarity-detection.timeout", 180);
    DEFAULTS.put("antispam.similarity-detection.similarity", .80);
    DEFAULTS.put("antispam.similarity-detection.max-violations", 6);
    DEFAULTS.put("antispam.similarity-detection.violations-expire", 180);
    DEFAULTS.put("antispam.cooldown.enable", false);
    DEFAULTS.put("antispam.cooldown.time", 0);
    DEFAULTS.put("antispam.cooldown.max-violations", 6);
    DEFAULTS.put("antispam.cooldown.violations-expire", 5);

    for (String path : DEFAULTS.keySet()) {
      if (!get().isSet(path) || get().getString(path) == null) {
        get().set(path, DEFAULTS.get(path));
      }
    }

    try {
      get().save(FILE);
    } catch (IOException e) {
      AuroraChat.INSTANCE.getLogger().severe("Failed to save config file");
    }
  }

  public static YamlConfiguration get() {
    return config;
  }

  public static void reload() {
    if (!FILE.exists()) {
      try {
        FILE.getParentFile().mkdirs();
        FILE.createNewFile();
      } catch (IOException e) {
        AuroraChat.INSTANCE.getLogger().severe("Failed to generate config file");
      }
    }

    config = YamlConfiguration.loadConfiguration(FILE);
    AuroraChat.INSTANCE.getLogger().info("Config reloaded!");
  }
}
