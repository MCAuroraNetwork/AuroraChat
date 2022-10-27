package club.aurorapvp.config;

import static club.aurorapvp.AuroraChat.DataFolder;
import static club.aurorapvp.AuroraChat.deserializeComponent;
import static club.aurorapvp.AuroraChat.lang;
import static club.aurorapvp.AuroraChat.plugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import net.kyori.adventure.text.Component;

public class LangHandler {
  private static HashMap<String, String> placeholders;
  private static String pathString;

  public static void setup() {
    File file = new File(DataFolder, "lang.yml");

    if (!file.exists()) {
      try {
        file.createNewFile();
      } catch (IOException e) {
        plugin.getLogger().warning("Couldn't create lang.yml");
      }
    }

    for (Object path : lang.getKeys(false).toArray()) {
      if (lang.getString((String) path).startsWith("{") &&
          lang.getString((String) path).endsWith("}")) {
        placeholders.put((String) path, lang.getString((String) path));
      }
    }
  }

  public static Component getLangComponent(String path) {

    if (lang.contains(path)) {
      pathString = lang.getString(path);
      for (CharSequence placeholder : placeholders.keySet()) {
        if (pathString.contains(placeholder)) {
          pathString = pathString.replace(placeholder, placeholders.get(placeholder));
        }
      }
      return deserializeComponent.deserialize(pathString);
    }
    return null;
  }
}
