package club.aurorapvp.aurorachat.modules;

import club.aurorapvp.aurorachat.AuroraChat;
import club.aurorapvp.aurorachat.data.NameColorDataHandler;
import club.aurorapvp.aurorachat.util.ComponentUtil;
import java.util.*;
import java.util.logging.Level;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;

public class NameColor {

  private static final Map<Player, NameColor> PLAYER_NAME_COLORS = new HashMap<>();
  private final Player player;
  private Component displayName;
  private final NameColorDataHandler data;
  private final List<TextColor> colors = new ArrayList<>();
  public GradientBuilder builder;
  private boolean isBuildingGradient = false;

  public NameColor(Player player) {
    this.player = player;
    this.data = new NameColorDataHandler(this);

    if (data.exists()) {
      this.reload();
    } else {
      colors.add(NamedTextColor.WHITE);

      displayName =
          ComponentUtil.createGradient(
              PlainTextComponentSerializer.plainText().serialize(player.displayName()), colors);
    }

    PLAYER_NAME_COLORS.put(player, this);
  }

  public static void remove(Player player) {
    PLAYER_NAME_COLORS.remove(player);
  }

  public List<TextColor> getColors() {
    return colors;
  }

  public Player getPlayer() {
    return player;
  }

  public Component getDisplayName() {
    return displayName;
  }

  public boolean isBuildingGradient() {
    return isBuildingGradient;
  }

  private void setBuildingGradient(boolean building) {
    this.isBuildingGradient = building;
  }

  public void setColor(String colorName) {
    colors.clear();

    colors.add(NamedTextColor.NAMES.value(colorName));

    this.updateDisplayName();
  }

  public void setDefaultColor(String colorName) {
    colors.clear();

    colors.add(NamedTextColor.NAMES.value(colorName));

    data.save();

    this.updateDisplayName();
  }

  public void setColor(TextColor color) {
    colors.clear();

    colors.add(color);

    this.updateDisplayName();
  }

  public void setDefaultColor(TextColor color) {
    colors.clear();

    colors.add(color);

    data.save();

    this.updateDisplayName();
  }

  public void setGradient(List<TextColor> colors) {
    this.colors.clear();

    this.colors.addAll(colors);

    this.updateDisplayName();
  }

  public void setDefaultGradient(List<TextColor> colors) {
    this.colors.clear();

    this.colors.addAll(colors);

    data.save();

    this.updateDisplayName();
  }

  public void updateDisplayName() {
    displayName =
        ComponentUtil.createGradient(
            PlainTextComponentSerializer.plainText().serialize(player.displayName()), colors);
  }

  public void reload() {
    Collection<String> colorNames = data.getColorCodes();

    colors.clear();

    for (String hexCode : colorNames) {
      TextColor color = TextColor.fromHexString(hexCode);

      if (color != null) {
        AuroraChat.getInstance().getLogger().log(Level.INFO, "not null: " + hexCode);

        colors.add(color);

        continue;
      }

      AuroraChat.getInstance().getLogger().log(Level.INFO, "null: " + hexCode);
    }

    if (colors.isEmpty()) {
      colors.add(NamedTextColor.WHITE);

      this.updateDisplayName();
    }

    for (TextColor color : colors) {
      if (!Objects.equals(color, NamedTextColor.WHITE)
          && !player.hasPermission("moneyprinter.namecolor")) {
        colors.clear();
        colors.add(NamedTextColor.WHITE);

        data.save();
      }
    }

    this.updateDisplayName();
  }

  public static NameColor getNameColor(Player player) {
    return PLAYER_NAME_COLORS.get(player);
  }

  public class GradientBuilder {

    private final List<TextColor> colors = new ArrayList<>();

    public GradientBuilder() {
      setBuildingGradient(true);

      builder = this;
    }

    public GradientBuilder addColor(TextColor color) {
      colors.add(color);
      return this;
    }

    public List<TextColor> build() {
      setBuildingGradient(false);

      return colors;
    }
  }
}
