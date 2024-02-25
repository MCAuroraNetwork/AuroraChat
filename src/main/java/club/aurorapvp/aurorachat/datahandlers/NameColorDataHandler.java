package club.aurorapvp.aurorachat.datahandlers;

import club.aurorapvp.aurorachat.AuroraChat;
import club.aurorapvp.aurorachat.modules.PlayerColorName;
import java.util.Objects;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class NameColorDataHandler {

  private final PersistentDataContainer container;
  private final NamespacedKey key = new NamespacedKey(AuroraChat.getInstance(), "name-color");
  private final PlayerColorName nameColor;

  public NameColorDataHandler(PlayerColorName nameColor) {
    this.nameColor = nameColor;
    this.container = nameColor.getPlayer().getPersistentDataContainer();
  }

  public String getColorName() {
    return container.get(key, PersistentDataType.STRING);
  }

  public void save() {
    container.set(key, PersistentDataType.STRING,
        Objects.requireNonNull(NamedTextColor.NAMES.key(nameColor.getColor())));
  }

  public boolean exists() {
    return container.get(key, PersistentDataType.STRING) != null;
  }
}