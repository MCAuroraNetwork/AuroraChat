package club.aurorapvp.config;

import static club.aurorapvp.AuroraChat.DataFolder;
import static club.aurorapvp.AuroraChat.plugin;

import java.io.File;
import java.io.IOException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigHandler {
  private static File file;
  private static FileConfiguration customFile;

  public static void setup() {
    file = new File(DataFolder, "lang.yml");

    if (!file.exists()) {
      try {
        file.createNewFile();
      } catch (IOException e) {
        plugin.getLogger().warning("Couldn't create lang.yml");
      }
    }
    customFile = YamlConfiguration.loadConfiguration(new File(DataFolder, "config.yml"));
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

  public static void reload() {
    customFile = YamlConfiguration.loadConfiguration(file);
  }
}
