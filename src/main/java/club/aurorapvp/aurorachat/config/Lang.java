package club.aurorapvp.aurorachat.config;

import club.aurorapvp.aurorachat.AuroraChat;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import java.util.logging.Level;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.file.YamlConfiguration;

public class Lang {

  private final HashMap<String, String> PLACEHOLDERS = new HashMap<>();
  private final File FILE = new File(AuroraChat.getInstance().getDataFolder(), "lang.yml");
  private YamlConfiguration lang;

  public Lang() {
    this.reload();
    this.generateDefaults();
  }

  public void generateDefaults() {
    final HashMap<String, String> DEFAULTS = new HashMap<>();

    for (var path : getYaml().getKeys(false).toArray()) {
      if (Objects.requireNonNull(getYaml().getString((String) path)).startsWith("~")
          && Objects.requireNonNull(getYaml().getString((String) path)).endsWith("~")) {
        PLACEHOLDERS.put(
            (String) path,
            Objects.requireNonNull(getYaml().getString((String) path)).replace("~", ""));
      }
    }

    DEFAULTS.put("prefix", "~<gradient:#FFAA00:#FF55FF><bold>AuroraChat ><reset>~");
    DEFAULTS.put("reloaded", "prefix <green>Reloaded");
    DEFAULTS.put("message-similarity-violation",
        "prefix <red>You're sending too many similar messages!");
    DEFAULTS.put("cooldown-violation",
        "prefix <red>You're sending messages too quickly!");
    DEFAULTS.put("unknown-recipient",
        "prefix <red>Cannot find recipient!");
    DEFAULTS.put("message-format", "<yellow><bold>%1$s -> %2$s ><reset> %3$s");
    DEFAULTS.put("name-color-changed",
        "prefix <green>Your name color has been changed to <%1$s><bold>%1$s");
    DEFAULTS.put("player-ignored", "prefix <yellow>Marked player to be ignored");
    DEFAULTS.put("player-unignored", "prefix <green>Marked player to be unignored");
    DEFAULTS.put("player-already-ignored", "prefix <red>Player is already ignored");
    DEFAULTS.put("player-not-ignored", "prefix <red>Player not ignored");
    DEFAULTS.put("chat-disabled", "prefix <red>Chat is disabled");
    DEFAULTS.put("unknown-color", "prefix <red>Unknown color!");
    DEFAULTS.put("set-gradient", "prefix <green>Ready to set gradient! Run '/namecolor [color]' until you've added enough colors, then run '/namecolor done'");
    DEFAULTS.put("color-added", "prefix <green>Color added!");
    DEFAULTS.put("gradient-set", "prefix <green>Gradient set!");
    DEFAULTS.put("help", "");
    DEFAULTS.put("help-command",
        """
            prefix <yellow><bold>All Commands:
                    
                    
            <blue><bold>/msg [player]: <reset><yellow>Sends a private message to a player
            """);

    for (String path : DEFAULTS.keySet()) {
      if (!getYaml().contains(path) || getYaml().getString(path) == null) {
        getYaml().set(path, DEFAULTS.get(path));
      }
    }

    try {
      getYaml().save(FILE);
    } catch (IOException e) {
      AuroraChat.getInstance().getLogger().log(Level.SEVERE, "Failed to save lang file", e);
    }

    for (var path : getYaml().getKeys(false).toArray()) {
      if (Objects.requireNonNull(getYaml().getString((String) path)).startsWith("~")
          && Objects.requireNonNull(getYaml().getString((String) path)).endsWith("~")) {
        PLACEHOLDERS.put(
            (String) path,
            Objects.requireNonNull(getYaml().getString((String) path)).replace("~", ""));
      }
    }
  }

  public String getString(String message) {
    String pathString = getYaml().getString(message);
    for (String placeholder : PLACEHOLDERS.keySet()) {
      assert pathString != null;
      if (pathString.contains(placeholder)) {
        pathString = pathString.replace(placeholder, PLACEHOLDERS.get(placeholder));
      }
    }
    return pathString;
  }

  public Component formatComponent(String message, Object... args) {
    String pathString = getYaml().getString(message);
    assert pathString != null;
    for (String placeholder : PLACEHOLDERS.keySet()) {
      if (pathString.contains(placeholder)) {
        pathString = pathString.replace(placeholder, PLACEHOLDERS.get(placeholder));
      }
    }

    pathString = String.format(pathString, args);

    return MiniMessage.miniMessage().deserialize(pathString);
  }

  public Component getComponent(String message) {
    String pathString = getYaml().getString(message);
    assert pathString != null;

    for (String placeholder : PLACEHOLDERS.keySet()) {
      if (pathString.contains(placeholder)) {
        pathString = pathString.replace(placeholder, PLACEHOLDERS.get(placeholder));
      }
    }
    return MiniMessage.miniMessage().deserialize(pathString);
  }

  public YamlConfiguration getYaml() {
    return lang;
  }

  @SuppressWarnings("ResultOfMethodCallIgnored")
  public void reload() {
    if (!FILE.exists()) {
      try {
        FILE.getParentFile().mkdirs();
        FILE.createNewFile();

        lang = YamlConfiguration.loadConfiguration(FILE);

        this.generateDefaults();
      } catch (IOException e) {
        AuroraChat.getInstance().getLogger().log(Level.SEVERE, "Failed to generate lang file", e);
      }
    }
    lang = YamlConfiguration.loadConfiguration(FILE);
    AuroraChat.getInstance().getLogger().info("Lang reloaded!");
  }
}
