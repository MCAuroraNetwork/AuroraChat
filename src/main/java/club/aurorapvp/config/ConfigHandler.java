package club.aurorapvp.config;

import static club.aurorapvp.AuroraChat.DataFolder;
import static club.aurorapvp.AuroraChat.plugin;

import java.io.File;
import java.io.IOException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigHandler {
  private static File file = new File(DataFolder, "config.yml");
  private static FileConfiguration customFile;

  // Finds or generates the custom config file
  public static void setup() {
    file = new File(DataFolder, "config.yml");

    if (!file.exists()) {
      try {
        file.createNewFile();
      } catch (IOException e) {
        plugin.getLogger().warning("Couldn't create config");
      }
    }
    customFile = YamlConfiguration.loadConfiguration(file);
  }

  public static FileConfiguration get() {
    return customFile;
  }

  public static void save() {
    try {
      customFile.save(file);
    } catch (IOException e) {
      plugin.getLogger().warning("Couldn't save");
    }
  }

  public static void generateDefaults() {
    get().addDefault("firstJoinMessages.default", "Welcome to Aurora PvP!");
    get().addDefault("joinMessages.default",
        "Use /kits to select a kit, and /createkit to create a kit");

    get().addDefault("antispam.enable", "true");
    get().addDefault("antispam.timeout", "180000");
    get().addDefault("antispam.similarity", ".80");
    get().addDefault("antispam.max-violations", "6");
    get().addDefault("antispam.violations-expire", "3");
    save();
  }

  public static void reload() {
    customFile = YamlConfiguration.loadConfiguration(file);
  }
}
