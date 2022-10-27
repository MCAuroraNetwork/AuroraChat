package club.aurorapvp.config;

import static club.aurorapvp.AuroraChat.DataFolder;
import static club.aurorapvp.AuroraChat.deserializeComponent;
import static club.aurorapvp.AuroraChat.lang;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.file.YamlConfiguration;

public class LangHandler {
  private static HashMap<String, String> placeholders = new HashMap<>();
  private static String pathString;
  private static HashMap<String, String> Defaults = new HashMap<>();

  public static void setup() throws IOException {
    if (!new File(DataFolder, "lang.yml").exists()) {
      new File(DataFolder, "lang.yml").createNewFile();
    }

    for (Object path : lang.getKeys(false).toArray()) {
      if (lang.getString((String) path).startsWith("~") &&
          lang.getString((String) path).endsWith("~")) {
        placeholders.put((String) path, lang.getString((String) path));
      }
    }
  }

  public static void generateDefaults() throws IOException {
    Defaults.put("prefix", "~<gradient:#FFAA00:#FF55FF><bold>AuroraChat > <reset>~");
    Defaults.put("message-similarity-violation",
        "prefix <gradient:#FFAA00:#FF55FF>You're sending too many similar messages!");
    Defaults.put("cooldown-violation",
        "prefix <gradient:#FFAA00:#FF55FF>You're sending messages too quickly!");
    Defaults.put("player-not-online",
        "prefix <gradient:#FFAA00:#FF55FF>That player is not online!");

    for (String path : Defaults.keySet()) {
      if (!lang.contains(path) || lang.getString(path) == null) {
        lang.set(path, Defaults.get(path));
        lang.save(new File(DataFolder, "lang.yml"));
      }
    }
  }

  public static Component getLangComponent(String path) {

    if (lang.contains(path)) {
      pathString = lang.getString(path);
      for (CharSequence placeholder : placeholders.keySet()) {
        if (pathString.contains(placeholder)) {
          pathString = pathString.replace(placeholder,
              placeholders.get(placeholder));
        }
      }
      return deserializeComponent.deserialize(pathString);
    }
    return null;
  }
}
