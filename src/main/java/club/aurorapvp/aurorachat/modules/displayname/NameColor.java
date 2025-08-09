package club.aurorapvp.aurorachat.modules.displayname;

import club.aurorapvp.aurorachat.util.ComponentUtil;
import club.aurorapvp.aurorachat.util.ExtendedTextColor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class NameColor {

  private static final ConcurrentHashMap<UUID, NameColor> PLAYER_NAME_COLORS = new ConcurrentHashMap<>();
  private final Player player;
  private Component displayName;
  private final DisplayName displayNameManager;
  private final List<TextColor> colors = new ArrayList<>();
  public GradientBuilder builder;
  private boolean isBuildingGradient = false;

  private NameColor(Player player) {
    this.player = player;
    this.displayNameManager = DisplayName.getDisplayName(player.getUniqueId());

    List<List<TextColor>> frameColors = displayNameManager.getFrameColors();
    if (!frameColors.isEmpty()) {
      colors.addAll(frameColors.getFirst());
    } else {
      colors.add(ExtendedTextColor.WHITE);
    }
  }

  public static void remove(Player player) {
    PLAYER_NAME_COLORS.remove(player.getUniqueId());
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
    colors.add(ExtendedTextColor.NAMES.value(colorName));
    this.updateDisplayName();
  }

  public void setDefaultColor(String colorName) {
    colors.clear();
    colors.add(ExtendedTextColor.NAMES.value(colorName));
    List<List<TextColor>> frameColors = displayNameManager.getFrameColors();
    if (frameColors.isEmpty()) {
      frameColors.add(new ArrayList<>(colors));
    } else {
      frameColors.set(0, new ArrayList<>(colors));
    }
    displayNameManager.setFrameColors(frameColors);
    displayNameManager.save();
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
    List<List<TextColor>> frameColors = displayNameManager.getFrameColors();
    if (frameColors.isEmpty()) {
      frameColors.add(new ArrayList<>(colors));
    } else {
      frameColors.set(0, new ArrayList<>(colors));
    }
    displayNameManager.setFrameColors(frameColors);
    displayNameManager.save();
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
    List<List<TextColor>> frameColors = displayNameManager.getFrameColors();
    if (frameColors.isEmpty()) {
      frameColors.add(new ArrayList<>(colors));
    } else {
      frameColors.set(0, new ArrayList<>(colors));
    }
    displayNameManager.setFrameColors(frameColors);
    displayNameManager.save();
    this.updateDisplayName();
  }

  public void updateDisplayName() {
    displayName = ComponentUtil.createGradient(
            PlainTextComponentSerializer.plainText().serialize(player.displayName()), colors);
  }

  public void reload() {
    List<List<TextColor>> frameColors = displayNameManager.getFrameColors();
    colors.clear();
    if (!frameColors.isEmpty()) {
      colors.addAll(frameColors.get(0)); // Use first frame for NameColor
    }

    if (colors.isEmpty()) {
      colors.add(ExtendedTextColor.WHITE);
      this.updateDisplayName();
    }

    for (TextColor color : colors) {
      if (!Objects.equals(color, ExtendedTextColor.WHITE) && !player.hasPermission("moneyprinter.namecolor")) {
        colors.clear();
        colors.add(ExtendedTextColor.WHITE);
        List<List<TextColor>> newFrameColors = new ArrayList<>();
        newFrameColors.add(new ArrayList<>(colors));
        displayNameManager.setFrameColors(newFrameColors);
        displayNameManager.save();
      }
    }

    this.updateDisplayName();
  }

  public static NameColor getNameColor(Player player) {
    return PLAYER_NAME_COLORS.computeIfAbsent(player.getUniqueId(), k -> new NameColor(player));
  }

  public DisplayName getDisplayNameManager() {
    return displayNameManager;
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