package club.aurorapvp.aurorachat.modules;

import club.aurorapvp.aurorachat.AuroraChat;
import club.aurorapvp.aurorachat.util.TextParser;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;

public class NameTag {

  public static final NamespacedKey KEY = new NamespacedKey(AuroraChat.getInstance(), "nametag");
  protected boolean hide = false;
  private final DisplayContent content;
  private final UUID uuid;
  private boolean visibleForOwner = false;

  private TextDisplay textDisplay;

  public NameTag(UUID uuid, DisplayContent content) {
    this.uuid = uuid;
    this.content = content;
  }

  public DisplayContent getContent() {
    return content;
  }

  protected TextDisplay getEntity() {
    return textDisplay;
  }

  private void createDisplayEntity() {
    if ((textDisplay != null && !textDisplay.isDead())
        || this.content == null
        || content.getCurrentFrame().text() == null) {
      return;
    }

    Player player = Bukkit.getPlayer(uuid);

    if (player == null) {
      return;
    }

    this.textDisplay =
        (TextDisplay)
            player
                .getWorld()
                .spawnEntity(
                    player.getEyeLocation(),
                    EntityType.TEXT_DISPLAY,
                    CreatureSpawnEvent.SpawnReason.CUSTOM,
                    entity -> {
                      TextDisplay textDisplay = (TextDisplay) entity;
                      textDisplay
                          .getPersistentDataContainer()
                          .set(KEY, PersistentDataType.STRING, player.getName());
                      textDisplay.setInvulnerable(true);
                      textDisplay.setPersistent(false);
                      textDisplay.setAlignment(TextDisplay.TextAlignment.CENTER);
                      textDisplay.setBillboard(this.content.getBillboard());
                      textDisplay.setSeeThrough(this.content.getSeeThrough());
                      textDisplay.setViewRange(content.getViewRange());
                      textDisplay.setShadowRadius(0);
                      textDisplay.setInterpolationDuration(content.getInterpolationDuration());
                      textDisplay.setInterpolationDelay(content.getInterpolationDelay());
                      textDisplay.setShadowed(content.getCurrentFrame().shadowed());
                      textDisplay.setTextOpacity(content.getCurrentFrame().textOpacity());

                      Color backgroundColor = this.content.getCurrentFrame().backgroundColor();
                      if (backgroundColor != null) textDisplay.setBackgroundColor(backgroundColor);

                      textDisplay.text(parseText(this.content.getCurrentFrame().text(), player));

                      textDisplay.setTransformation(
                          new Transformation(
                              content.getCurrentFrame().offset(),
                              new AxisAngle4f(0, 0, 0, 0),
                              content.getCurrentFrame().scale(),
                              new AxisAngle4f(0, 0, 0, 0)));
                    });

    if (!this.visibleForOwner) {
      player.hideEntity(AuroraChat.getInstance(), textDisplay);
    }
    player.addPassenger(textDisplay);
  }

  public void setVisibleForOwner(boolean visible) {
    this.visibleForOwner = visible;

    if (textDisplay == null || textDisplay.isDead()) {
      return;
    }

    Player player = Bukkit.getPlayer(uuid);

    if (player == null) {
      return;
    }

    if (visible) {
      player.showEntity(AuroraChat.getInstance(), textDisplay);
    } else {
      player.hideEntity(AuroraChat.getInstance(), textDisplay);
    }
  }

  public void update() {
    if (hide) {
      remove();
      return;
    }

    Player player = Bukkit.getPlayer(uuid);

    if (player == null
        || player.isDead()
        || content.getCurrentFrame().text() == null
        || player.isSneaking()
        || player.getGameMode() == GameMode.SPECTATOR
        || player.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
      remove();
      return;
    }

    createDisplayEntity();

    if (textDisplay == null || textDisplay.isDead()) {
      return;
    }

    if (!player.getPassengers().contains(textDisplay)) {
      player.addPassenger(textDisplay);
    }

    textDisplay.text(parseText(this.content.getCurrentFrame().text(), player));

    textDisplay.setBillboard(this.content.getBillboard());
    textDisplay.setShadowed(content.getCurrentFrame().shadowed());
    textDisplay.setTextOpacity(content.getCurrentFrame().textOpacity());
    textDisplay.setTransformation(
        new Transformation(
            content.getCurrentFrame().offset(),
            new AxisAngle4f(0, 0, 0, 0),
            content.getCurrentFrame().scale(),
            new AxisAngle4f(0, 0, 0, 0)));

    Color backgroundColor = this.content.getCurrentFrame().backgroundColor();

    if (backgroundColor == null) {
      textDisplay.setDefaultBackground(true);
    } else {
      textDisplay.setBackgroundColor(backgroundColor);
    }

    setVisibleForOwner(this.visibleForOwner);
  }

  protected void remove() {
    if (textDisplay == null || textDisplay.isDead()) {
      return;
    }

    textDisplay.remove();
  }

  private Component parseText(String text, Player player) {
    if (player == null || !player.isOnline()) {
      return Component.empty();
    }

    return TextParser.parseWithPlaceholders(text, player);
  }
}
