package club.aurorapvp.aurorachat.datahandlers;

import club.aurorapvp.aurorachat.AuroraChat;
import club.aurorapvp.aurorachat.modules.NameColor;
import java.util.List;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class NameColorDataHandler {

  private final PersistentDataContainer container;
  private final NamespacedKey key = new NamespacedKey(AuroraChat.getInstance(), "name-colors");
  private final NameColor color;

  public NameColorDataHandler(NameColor color) {
    this.color = color;
    this.container = color.getPlayer().getPersistentDataContainer();
  }

  public List<String> getColorCodes() {
    String colorNames = container.get(key, PersistentDataType.STRING);

    if (colorNames == null) {
      return List.of("WHITE");
    }

    return List.of(colorNames.split(","));
  }

  public void save() {
    StringBuilder sb = new StringBuilder();

    for (TextColor color : color.getColors()) {
      sb.append(color.asHexString()).append(",");
    }

    container.set(key, PersistentDataType.STRING, sb.toString());
  }

  public boolean exists() {
    return container.get(key, PersistentDataType.STRING) != null;
  }
}