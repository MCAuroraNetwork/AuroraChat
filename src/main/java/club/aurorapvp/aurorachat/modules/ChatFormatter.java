package club.aurorapvp.aurorachat.modules;

import club.aurorapvp.aurorachat.util.ComponentUtil;
import club.aurorapvp.aurorachat.util.ExtendedTextColor;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

public class ChatFormatter {

  public static final Chat chat =
          Objects.requireNonNull(Bukkit.getServer().getServicesManager().getRegistration(Chat.class))
                  .getProvider();

  // TODO use lang
  public static void onJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    String prefix = chat.getPlayerPrefix(player);
    String suffix = chat.getPlayerSuffix(player);
    DisplayName displayNameManager = DisplayName.getDisplayName(player.getUniqueId());
    List<List<TextColor>> frameColors = displayNameManager.getFrameColors();
    int currentFrameIndex = displayNameManager.getCurrentFrameIndex();
    List<TextColor> currentColors = frameColors.isEmpty() ? List.of(ExtendedTextColor.WHITE) : frameColors.get(currentFrameIndex);
    String playerName = PlainTextComponentSerializer.plainText().serialize(player.displayName());
    Component currentDisplayName = ComponentUtil.createGradient(playerName, currentColors);

    Component formattedDisplayName =
            MiniMessage.miniMessage()
                    .deserialize(prefix)
                    .append(currentDisplayName)
                    .append(MiniMessage.miniMessage().deserialize(suffix));

    Component formattedJoinMessage =
            formattedDisplayName.append(MiniMessage.miniMessage().deserialize("<yellow> has joined the game."));

    event.joinMessage(formattedJoinMessage);
  }

  // TODO use lang
  public static void onQuit(PlayerQuitEvent event) {
    Player player = event.getPlayer();
    String prefix = chat.getPlayerPrefix(player);
    String suffix = chat.getPlayerSuffix(player);
    DisplayName displayNameManager = DisplayName.getDisplayName(player.getUniqueId());
    List<List<TextColor>> frameColors = displayNameManager.getFrameColors();
    int currentFrameIndex = displayNameManager.getCurrentFrameIndex();
    List<TextColor> currentColors = frameColors.isEmpty() ? List.of(ExtendedTextColor.WHITE) : frameColors.get(currentFrameIndex);
    String playerName = PlainTextComponentSerializer.plainText().serialize(player.displayName());
    Component currentDisplayName = ComponentUtil.createGradient(playerName, currentColors);

    Component formattedDisplayName =
            MiniMessage.miniMessage()
                    .deserialize(prefix)
                    .append(currentDisplayName)
                    .append(MiniMessage.miniMessage().deserialize(suffix));

    Component formattedQuitMessage =
            formattedDisplayName.append(MiniMessage.miniMessage().deserialize("<yellow> has left the game."));

    event.quitMessage(formattedQuitMessage);
  }

  // TODO use lang
  public static void onChat(AsyncChatEvent event) {
    Player player = event.getPlayer();
    String prefix = chat.getPlayerPrefix(player);
    String suffix = chat.getPlayerSuffix(player);
    DisplayName displayNameManager = DisplayName.getDisplayName(player.getUniqueId());
    List<List<TextColor>> frameColors = displayNameManager.getFrameColors();
    int currentFrameIndex = displayNameManager.getCurrentFrameIndex();
    List<TextColor> currentColors = frameColors.isEmpty() ? List.of(ExtendedTextColor.WHITE) : frameColors.get(currentFrameIndex);
    String playerName = PlainTextComponentSerializer.plainText().serialize(player.displayName());
    Component currentDisplayName = ComponentUtil.createGradient(playerName, currentColors);

    if (event.getPlayer().hasPermission("aurorachat.greentext")) {
      String message = PlainTextComponentSerializer.plainText().serialize(event.message());
      if (message.startsWith(">")) {
        event.message(event.message().color(ExtendedTextColor.GREEN));
      }
    }

    event.renderer((source, sourceDisplayName, message, viewer) -> MiniMessage.miniMessage()
            .deserialize(prefix)
            .append(MiniMessage.miniMessage().deserialize("<"))
            .append(currentDisplayName)
            .append(MiniMessage.miniMessage().deserialize(">"))
            .append(MiniMessage.miniMessage().deserialize(suffix))
            .append(MiniMessage.miniMessage().deserialize(" "))
            .append(message));
  }
}