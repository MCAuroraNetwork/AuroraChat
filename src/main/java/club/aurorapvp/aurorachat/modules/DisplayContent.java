package club.aurorapvp.aurorachat.modules;

import club.aurorapvp.aurorachat.AuroraChat;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Display;
import org.joml.Vector3f;

public class DisplayContent {

  private final List<DisplayFrame> frames = new ArrayList<>();
  private int refreshRate, interpolationDuration, interpolationDelay, currentFrame, viewRange;
  private Display.Billboard billboard = Display.Billboard.HORIZONTAL;
  private boolean seeThrough = false;

  public void setRefreshRate(int refreshRate) {
    this.refreshRate = refreshRate;
  }

  public int getRefreshRate() {
    return refreshRate;
  }

  public void setBillboard(Display.Billboard billboard) {
    this.billboard = billboard;
  }

  public Display.Billboard getBillboard() {
    return billboard;
  }

  public void setSeeThrough(boolean seeThrough) {
    this.seeThrough = seeThrough;
  }

  public boolean getSeeThrough() {
    return seeThrough;
  }

  public void setInterpolationDelay(int interpolationDelay) {
    this.interpolationDelay = interpolationDelay;
  }

  public void setInterpolationDuration(int interpolationDuration) {
    this.interpolationDuration = interpolationDuration;
  }

  public int getInterpolationDelay() {
    return interpolationDelay;
  }

  public int getInterpolationDuration() {
    return interpolationDuration;
  }

  public void setViewRange(int viewRange) {
    this.viewRange = viewRange;
  }

  public int getViewRange() {
    return viewRange;
  }

  public void addFrame(DisplayFrame frame) {
    frames.add(frame);
  }

  public DisplayFrame getCurrentFrame() {
    if (frames.isEmpty()) {
      return null;
    }

    return frames.get(currentFrame);
  }

  public void advanceFrame() {
    if (frames.isEmpty()) {
      return;
    }

    currentFrame = currentFrame + 1 >= frames.size() ? 0 : currentFrame + 1;
  }

  public static DisplayContent createDisplayContent() {
    ConfigurationSection section =
        AuroraChat.getInstance().getConfig().getConfigurationSection("nametag.default");

    assert section != null;
    ConfigurationSection framesSection = section.getConfigurationSection("frames");

    if (framesSection == null) {
      AuroraChat.getInstance().getLogger().severe("No frames section found");
      return null;
    }

    DisplayContent displayContent = new DisplayContent();

    displayContent.setRefreshRate(section.getInt("refresh-rate", 0));
    displayContent.setSeeThrough(section.getBoolean("see-through", false));
    displayContent.setInterpolationDelay(
        section.getInt("interpolation-delay", displayContent.getRefreshRate()));
    displayContent.setInterpolationDuration(
        section.getInt("interpolation-duration", displayContent.getRefreshRate()));
    displayContent.setViewRange(
        section.getInt(
            "view-range",
            Bukkit.spigot()
                .getSpigotConfig()
                .getInt("world-settings.default.entity-tracking-range.players", 48)));

    Display.Billboard billboard = Display.Billboard.HORIZONTAL;
    try {
      billboard =
          Display.Billboard.valueOf(section.getString("billboard", "horizontal").toUpperCase());
    } catch (IllegalArgumentException e) {
      AuroraChat.getInstance().getLogger().warning("Invalid billboard type");
    }
    displayContent.setBillboard(billboard);

    framesSection
        .getKeys(false)
        .forEach(
            frameName -> {
              ConfigurationSection frameSection = framesSection.getConfigurationSection(frameName);

              if (frameSection == null) {
                return;
              }

              String text = frameSection.getString("text", null);
              String backgroundColor = frameSection.getString("background");
              float scaleX = (float) frameSection.getDouble("scale-x", 1);
              float scaleY = (float) frameSection.getDouble("scale-y", 0.2);
              float scaleZ = (float) frameSection.getDouble("scale-z", 1);
              float offsetX = (float) frameSection.getDouble("offset-x", 0);
              float offsetY = (float) frameSection.getDouble("offset-y", 0);
              float offsetZ = (float) frameSection.getDouble("offset-z", 0);
              boolean shadowed = frameSection.getBoolean("shadowed", false);
              byte textOpacity =
                  (byte) Math.min(Math.max(frameSection.getInt("text-opacity", 255), 0), 255);
              displayContent.addFrame(
                  new DisplayFrame(
                      text,
                      colorFromHex(backgroundColor),
                      new Vector3f(scaleX, scaleY, scaleZ),
                      new Vector3f(offsetX, offsetY, offsetZ),
                      shadowed,
                      textOpacity));
            });

    return displayContent;
  }

  private static Color colorFromHex(String hex) {
    if (hex == null) {
      return null;
    }

    hex = hex.substring(1);

    int r, g, b, a;

    return switch (hex.length()) {
      case 3 -> {
        r = Integer.parseInt(String.valueOf(hex.charAt(0) + hex.charAt(0)), 16);
        g = Integer.parseInt(String.valueOf(hex.charAt(1) + hex.charAt(1)), 16);
        b = Integer.parseInt(String.valueOf(hex.charAt(2) + hex.charAt(2)), 16);
        yield Color.fromRGB(r, g, b);
      }
      case 6 -> {
        r = Integer.parseInt(hex.substring(0, 2), 16);
        g = Integer.parseInt(hex.substring(2, 4), 16);
        b = Integer.parseInt(hex.substring(4, 6), 16);
        yield Color.fromRGB(r, g, b);
      }
      case 8 -> {
        r = Integer.parseInt(hex.substring(0, 2), 16);
        g = Integer.parseInt(hex.substring(2, 4), 16);
        b = Integer.parseInt(hex.substring(4, 6), 16);
        a = Integer.parseInt(hex.substring(6, 8), 16);
        yield Color.fromARGB(a, r, g, b);
      }
      default -> {
        AuroraChat.getInstance()
            .getLogger()
            .warning("Invalid hex color: " + hex + " (invalid length)");
        yield null;
      }
    };
  }
}
