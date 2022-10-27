package club.aurorapvp.config;

import static club.aurorapvp.AuroraChat.deserializeComponent;
import static club.aurorapvp.AuroraChat.lang;

import java.io.IOException;
import java.util.HashMap;
import net.kyori.adventure.text.Component;

public class LangHandler {
  private static HashMap<String, String> placeholders;
  private static String pathString;
  private static HashMap<String, String> Defaults;

  public static void setup() {
    for (Object path : lang.getKeys(false).toArray()) {
      if (lang.getString((String) path).startsWith("{") &&
          lang.getString((String) path).endsWith("}")) {
        placeholders.put((String) path, lang.getString((String) path));
      }
    }
  }

  public static void generateDefaults() throws IOException {
    Defaults.put("prefix", "<gradient:#FFAA00:#FF55FF><bold>AuroraChat > <reset>");
    Defaults.put("message-similarity-violation",
        "{PREFIX} <gradient:#FFAA00:#FF55FF>You're sending too many similar messages!");

    for (String path : Defaults.keySet()) {
      if (!lang.contains(path) || lang.getString(path) == null) {
        lang.set(path, Defaults.get(path));
        lang.save(lang.getName());
      }
    }
  }

  public static Component getLangComponent(String path) {

    if (lang.contains(path)) {
      pathString = lang.getString(path);
      for (CharSequence placeholder : placeholders.keySet()) {
        if (pathString.contains(placeholder)) {
          pathString = pathString.replace(placeholder,
              placeholders.get(placeholder).replace("{", "").replace("}", ""));
        }
      }
      return deserializeComponent.deserialize(pathString);
    }
    return null;
  }
}
