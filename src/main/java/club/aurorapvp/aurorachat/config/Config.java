package club.aurorapvp.aurorachat.config;

import club.aurorapvp.aurorachat.AuroraChat;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import org.bukkit.configuration.file.YamlConfiguration;

public class Config {

  private final File FILE = new File(AuroraChat.getInstance().getDataFolder(), "config.yml");
  private YamlConfiguration config;

  public Config() {
    this.reload();
    this.generateDefaults();
  }

  public void generateDefaults() {
    final HashMap<String, Object> DEFAULTS = new HashMap<>();

    DEFAULTS.put("messages.auto-messages.interval", 300);
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
    DEFAULTS.put("nametag.default.refresh-rate", 10);
    DEFAULTS.put("nametag.default.billboard", "center");
    DEFAULTS.put("nametag.default.frames.1.text", "<placeholder:displayname>");
    DEFAULTS.put("nametag.default.frames.1.offset-y", 0.2);

    for (String path : DEFAULTS.keySet()) {
      if (!getYaml().isSet(path) || getYaml().getString(path) == null) {
        getYaml().set(path, DEFAULTS.get(path));
      }
    }

    try {
      getYaml().save(FILE);
    } catch (IOException e) {
      AuroraChat.getInstance().getLogger().log(Level.SEVERE, "Failed to save config file", e);
    }
  }

  public YamlConfiguration getYaml() {
    return config;
  }

  @SuppressWarnings("ResultOfMethodCallIgnored")
  public void reload() {
    if (!FILE.exists()) {
      try {
        FILE.getParentFile().mkdirs();
        FILE.createNewFile();

        config = YamlConfiguration.loadConfiguration(FILE);

        this.generateDefaults();
      } catch (IOException e) {
        AuroraChat.getInstance().getLogger()
            .log(Level.SEVERE, "Failed to generate config file", e);
      }
    }
    config = YamlConfiguration.loadConfiguration(FILE);
    AuroraChat.getInstance().getLogger().info("Config reloaded!");
  }
}