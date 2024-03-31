package club.aurorapvp.aurorachat.modules;

import club.aurorapvp.aurorachat.datahandlers.NameColorDataHandler;
import club.aurorapvp.aurorachat.util.ComponentUtil;
import io.papermc.paper.event.player.AsyncChatEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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

    data.save();
  }

  public void setGradient(List<TextColor> colors) {
    this.colors.clear();

    this.colors.addAll(colors);

    data.save();
  }

  public void reload() {
    Collection<String> colorNames = data.getColorCodes();

    for (String hexCode : colorNames) {
      colors.add(TextColor.fromHexString(hexCode));
    }

    for (TextColor color : colors) {
      if (!Objects.equals(color, NamedTextColor.WHITE) && !player.hasPermission(
          "moneyprinter.namecolor")) {
        colors.clear();
        colors.add(NamedTextColor.WHITE);

        data.save();
      }
    }

    displayName = ComponentUtil.createGradient(
        PlainTextComponentSerializer.plainText().serialize(player.displayName()), colors);
  }

  public static NameColor getNameColor(Player player) {
    return PLAYER_NAME_COLORS.get(player);
  }

  public static class ChatFormatter {

    private static final Chat chat = Objects.requireNonNull(
        Bukkit.getServer().getServicesManager().getRegistration(Chat.class)).getProvider();

    public static void onJoin(PlayerJoinEvent event) {
      Player player = event.getPlayer();
      Component displayName = NameColor.getNameColor(player).getDisplayName();

      Component formattedJoinMessage = displayName.append(
          MiniMessage.miniMessage().deserialize("<reset><yellow> has joined the game."));

      event.joinMessage(formattedJoinMessage);
    }

    public static void onQuit(PlayerQuitEvent event) {
      Player player = event.getPlayer();
      Component displayName = NameColor.getNameColor(player).getDisplayName();

      Component formattedQuitMessage = displayName.append(
          MiniMessage.miniMessage().deserialize("<reset><yellow> has left the game."));

      event.quitMessage(formattedQuitMessage);
    }

    public static void onChat(AsyncChatEvent event) {
      Player player = event.getPlayer();
      String prefix = chat.getPlayerPrefix(player);
      String suffix = chat.getPlayerSuffix(player);
      Component displayName = NameColor.getNameColor(player).getDisplayName();

      Component formattedMessage = MiniMessage.miniMessage().deserialize(prefix + " <reset><")
          .append(displayName).append(MiniMessage.miniMessage().deserialize(suffix + "> <reset>"))
          .append(event.message());

      event.renderer((source, sourceDisplayName, formatted, isNameMentioned) -> formattedMessage);
    }
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

