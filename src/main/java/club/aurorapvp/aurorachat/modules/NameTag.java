package club.aurorapvp.aurorachat.modules;

import club.aurorapvp.aurorachat.AuroraChat;
import club.aurorapvp.aurorachat.util.ComponentUtil;
import club.aurorapvp.aurorachat.util.ExtendedTextColor;
import club.aurorapvp.aurorachat.util.TextParser;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.purpurmc.purpur.event.entity.EntityTeleportHinderedEvent;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NameTag {

  public static final NamespacedKey KEY = new NamespacedKey(AuroraChat.getInstance(), "nametag");
  protected boolean hide = false;
  private final DisplayContent content;
  private final UUID uuid;
  private final DisplayName displayName;
  private boolean visibleForOwner = false;
  private Component cachedParsed = null;
  private String cachedRawText = null;
  private TextDisplay textDisplay;
  private Set<String> currentPlaceholders = new HashSet<>();
  private Map<String, String> lastPlaceholderValues = new HashMap<>();
  private static final HashMap<UUID, NameTag> NAMETAGS = new HashMap<>();
  private static long time = Long.MIN_VALUE;
  private int nameColorFrameIndex = 0;

  public NameTag(UUID uuid, DisplayContent content) {
    this.uuid = uuid;
    this.content = content;
    this.displayName = DisplayName.getDisplayName(uuid);
  }

  public static void init() {
    reloadNameTags();
    Bukkit.getScheduler().runTaskTimer(AuroraChat.getInstance(), () -> NAMETAGS.values().forEach(NameTag::update), 0, 1);

    Bukkit.getScheduler().runTaskTimer(AuroraChat.getInstance(), () -> {
      Bukkit.getWorlds().forEach(world -> world.getEntities().forEach(entity -> {
        if (!(entity instanceof TextDisplay textDisplay) || !textDisplay.getPersistentDataContainer().has(NameTag.KEY)) {
          return;
        }
        for (NameTag nametag : NAMETAGS.values()) {
          if (nametag.getEntity().equals(textDisplay)) {
            return;
          }
        }
        textDisplay.remove();
      }));
    }, 100, 100);

    Bukkit.getScheduler().runTaskTimerAsynchronously(AuroraChat.getInstance(), () -> {
      time++;
      NAMETAGS.values().forEach(nameTag -> {
        DisplayContent content = nameTag.getContent();
        if (content != null && content.getRefreshRate() > 0) {
          int tickInterval = Math.max(20 / content.getRefreshRate(), 1);
          if (time % tickInterval == 0) {
            content.advanceFrame();
          }
        }
        DisplayName displayName = nameTag.getDisplayName();
        if (displayName.getRefreshRate() > 0) {
          int tickInterval = Math.max(20 / displayName.getRefreshRate(), 1);
          if (time % tickInterval == 0) {
            nameTag.advanceNameColorFrame();
          }
        }
      });
    }, 1, 1);
  }

  public static void onPlayerJoin(PlayerJoinEvent event) {
    UUID joinerUuid = event.getPlayer().getUniqueId();
    DisplayContent displayContent = DisplayContent.createDisplayContent();
    if (displayContent == null) {
      return;
    }
    NAMETAGS.put(joinerUuid, new NameTag(joinerUuid, displayContent));
  }

  public static void onPlayerTeleport(PlayerTeleportEvent event) {
    NameTag nametag = NAMETAGS.get(event.getPlayer().getUniqueId());
    if (nametag == null) {
      return;
    }
    nametag.remove();
  }

  public static void onPlayerTeleportHindered(EntityTeleportHinderedEvent event) {
    if (!(event.getEntity() instanceof Player player) || event.getReason() != EntityTeleportHinderedEvent.Reason.IS_VEHICLE) {
      return;
    }
    NameTag nametag = NAMETAGS.get(player.getUniqueId());
    if (nametag == null) {
      return;
    }
    for (Entity entity : player.getPassengers()) {
      player.removePassenger(entity);
    }
    nametag.remove();
    event.setShouldRetry(true);
  }

  public static void reloadNameTags() {
    NAMETAGS.values().forEach(NameTag::remove);
    NAMETAGS.clear();
    for (Player player : Bukkit.getOnlinePlayers()) {
      NAMETAGS.put(player.getUniqueId(), new NameTag(player.getUniqueId(), DisplayContent.createDisplayContent()));
    }
  }

  public DisplayContent getContent() {
    return content;
  }

  public TextDisplay getEntity() {
    return textDisplay;
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
    if (hide || content == null) {
      remove();
      return;
    }

    Player player = Bukkit.getPlayer(uuid);
    if (player == null || player.isDead() || player.isSneaking() || player.getGameMode() == GameMode.SPECTATOR || player.hasPotionEffect(PotionEffectType.INVISIBILITY) || content.getCurrentFrame().text() == null) {
      remove();
      return;
    }

    if (textDisplay == null || textDisplay.isDead()) {
      cachedRawText = processText(content.getCurrentFrame().text());
      cachedParsed = parseText(cachedRawText, player);
      currentPlaceholders = extractPlaceholders(cachedRawText);
      updatePlaceholderValues(player);

      textDisplay = (TextDisplay) player.getWorld().spawnEntity(player.getEyeLocation(), EntityType.TEXT_DISPLAY, CreatureSpawnEvent.SpawnReason.CUSTOM, entity -> {
        TextDisplay td = (TextDisplay) entity;
        td.getPersistentDataContainer().set(KEY, PersistentDataType.STRING, player.getName());
        td.setInvulnerable(true);
        td.setPersistent(false);
        td.setAlignment(TextDisplay.TextAlignment.CENTER);
        td.setBillboard(content.getBillboard());
        td.setSeeThrough(content.getSeeThrough());
        td.setViewRange(content.getViewRange());
        td.setInterpolationDuration(content.getInterpolationDuration());
        td.setInterpolationDelay(content.getInterpolationDelay());
        td.setShadowRadius(0);
        td.setShadowed(content.getCurrentFrame().shadowed());
        td.setTextOpacity(content.getCurrentFrame().textOpacity());
        Color bg = content.getCurrentFrame().backgroundColor();
        if (bg != null) td.setBackgroundColor(bg);
        td.text(cachedParsed);
        td.setTransformation(new Transformation(content.getCurrentFrame().offset(), new AxisAngle4f(0, 0, 0, 0), content.getCurrentFrame().scale(), new AxisAngle4f(0, 0, 0, 0)));
      });

      if (!visibleForOwner) player.hideEntity(AuroraChat.getInstance(), textDisplay);
    }

    if (!player.getPassengers().contains(textDisplay)) player.addPassenger(textDisplay);

    String currentRawText = processText(content.getCurrentFrame().text());
    if (!Objects.equals(currentRawText, cachedRawText)) {
      cachedRawText = currentRawText;
      currentPlaceholders = extractPlaceholders(cachedRawText);
      updatePlaceholderValues(player);
      cachedParsed = parseText(cachedRawText, player);
      textDisplay.text(cachedParsed);
    } else {
      boolean needsUpdate = false;
      for (String placeholder : currentPlaceholders) {
        Component currentComponent = getPlaceholderComponent(placeholder, player);
        String currentSerialized = MiniMessage.miniMessage().serialize(currentComponent);
        String lastSerialized = lastPlaceholderValues.get(placeholder);
        if (!Objects.equals(currentSerialized, lastSerialized)) {
          needsUpdate = true;
          break;
        }
      }
      if (needsUpdate) {
        updatePlaceholderValues(player);
        cachedParsed = parseText(cachedRawText, player);
        textDisplay.text(cachedParsed);
      }
    }

    textDisplay.setBillboard(content.getBillboard());
    textDisplay.setShadowed(content.getCurrentFrame().shadowed());
    textDisplay.setTextOpacity(content.getCurrentFrame().textOpacity());
    textDisplay.setTransformation(new Transformation(content.getCurrentFrame().offset(), new AxisAngle4f(0, 0, 0, 0), content.getCurrentFrame().scale(), new AxisAngle4f(0, 0, 0, 0)));

    Color bg = content.getCurrentFrame().backgroundColor();
    if (bg == null) {
      textDisplay.setDefaultBackground(true);
    } else {
      textDisplay.setBackgroundColor(bg);
    }

    setVisibleForOwner(visibleForOwner);
  }

  protected void remove() {
    if (textDisplay == null || textDisplay.isDead()) {
      return;
    }
    textDisplay.remove();
  }

  private String processText(String text) {
    String processedText = text;
    if (!displayName.isPrefixEnabled()) {
      processedText = processedText.replace("<placeholder:prefix>", "");
    }
    if (!displayName.isSuffixEnabled()) {
      processedText = processedText.replace("<placeholder:suffix>", "");
    }
    return processedText;
  }

  private Component parseText(String text, Player player) {
    if (player == null || !player.isOnline()) {
      return Component.empty();
    }
    List<List<TextColor>> frameColors = displayName.getFrameColors();
    List<TextColor> colors;
    if (frameColors == null || frameColors.isEmpty() || nameColorFrameIndex >= frameColors.size()) {
      colors = List.of(ExtendedTextColor.WHITE); // Default to white if null, empty, or out of bounds
    } else {
      colors = frameColors.get(nameColorFrameIndex);
      if (colors == null || colors.isEmpty()) {
        colors = List.of(ExtendedTextColor.WHITE); // Default to white if frame is null or empty
      }
    }
    return TextParser.parseWithPlaceholders(text, player, colors);
  }

  private Set<String> extractPlaceholders(String rawText) {
    Set<String> placeholders = new HashSet<>();
    Pattern pattern = Pattern.compile("<placeholder:([^>]+)>");
    Matcher matcher = pattern.matcher(rawText);
    while (matcher.find()) {
      placeholders.add(matcher.group(1));
    }
    return placeholders;
  }

  private Component getPlaceholderComponent(String placeholder, Player player) {
    switch (placeholder) {
      case "displayname":
        return player.displayName();
      case "coloredname":
        List<List<TextColor>> frameColors = displayName.getFrameColors();
        List<TextColor> colors;
        if (frameColors == null || frameColors.isEmpty() || nameColorFrameIndex >= frameColors.size()) {
          colors = List.of(ExtendedTextColor.WHITE); // Default to white if null, empty, or out of bounds
        } else {
          colors = frameColors.get(nameColorFrameIndex);
          if (colors == null || colors.isEmpty()) {
            colors = List.of(ExtendedTextColor.WHITE); // Default to white if frame is null or empty
          }
        }
        return ComponentUtil.createGradient(player.getName(), colors);
      case "prefix":
        String prefix = ChatFormatter.chat.getPlayerPrefix(player);
        return MiniMessage.miniMessage().deserialize(prefix);
      case "suffix":
        String suffix = ChatFormatter.chat.getPlayerSuffix(player);
        return MiniMessage.miniMessage().deserialize(suffix);
      case "health":
        int health = (int) (player.getHealth() + player.getAbsorptionAmount());
        return Component.text(health);
      case "ping":
        int ping = player.getPing();
        return Component.text(ping);
      default:
        if (AuroraChat.getInstance().isPlaceholderApiInstalled()) {
          String parsed = PlaceholderAPI.setPlaceholders(player, '%' + placeholder + '%');
          if (parsed.contains(LegacyComponentSerializer.SECTION_CHAR + "")) {
            return LegacyComponentSerializer.legacySection().deserialize(parsed);
          } else {
            return MiniMessage.miniMessage().deserialize(parsed);
          }
        } else {
          return Component.text(placeholder);
        }
    }
  }

  private void updatePlaceholderValues(Player player) {
    lastPlaceholderValues.clear();
    for (String placeholder : currentPlaceholders) {
      Component component = getPlaceholderComponent(placeholder, player);
      String serialized = MiniMessage.miniMessage().serialize(component);
      lastPlaceholderValues.put(placeholder, serialized);
    }
  }

  public Player getPlayer() {
    return Bukkit.getPlayer(uuid);
  }

  public DisplayName getDisplayName() {
    return displayName;
  }

  public void setFrameDisplayNameColors(List<List<TextColor>> colors) {
    displayName.setFrameColors(colors);
    nameColorFrameIndex = 0;
  }

  public void setRefreshRate(int rate) {
    content.setRefreshRate(Math.max(rate, 1));
    displayName.setRefreshRate(Math.max(rate, 1));
  }

  public static HashMap<UUID, NameTag> getNameTags() {
    return NAMETAGS;
  }

  public void advanceNameColorFrame() {
    List<List<TextColor>> frameColors = displayName.getFrameColors();
    if (frameColors == null || frameColors.isEmpty()) {
      nameColorFrameIndex = 0; // Stay at 0 if null or empty
    } else {
      nameColorFrameIndex = (nameColorFrameIndex + 1) % frameColors.size();
    }
  }
}