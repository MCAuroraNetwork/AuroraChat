package club.aurorapvp.aurorachat.modules;

import club.aurorapvp.aurorachat.datahandlers.NameColorDataHandler;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

public class PlayerColorName {

  private static final Map<Player, PlayerColorName> PLAYER_NAME_COLORS = new HashMap<>();
  private final Player player;
  private final NameColorDataHandler data;
  private NamedTextColor color;

  public PlayerColorName(Player player) {
    this.player = player;
    this.data = new NameColorDataHandler(this);

    if (data.exists()) {
      this.reload();
    } else {
      color = NamedTextColor.WHITE;
    }

    PLAYER_NAME_COLORS.put(player, this);
  }

  public static void remove(Player player) {
    PLAYER_NAME_COLORS.remove(player);
  }

  public NamedTextColor getColor() {
    return color;
  }

  public Player getPlayer() {
    return player;
  }

  public void setColor(String colorName) {
    this.color = NamedTextColor.NAMES.value(colorName);

    data.save();
  }

  public void reload() {
    this.color = NamedTextColor.NAMES.value(data.getColorName());

    if (!Objects.equals(color, NamedTextColor.WHITE) && !player.hasPermission("moneyprinter.namecolor")) {
      color = NamedTextColor.WHITE;

      data.save();
    }
  }

  public static NamedTextColor getColor(Player player) {
    return PLAYER_NAME_COLORS.get(player).getColor();
  }

  public static PlayerColorName getNameColor(Player player) {
    return PLAYER_NAME_COLORS.get(player);
  }
}

