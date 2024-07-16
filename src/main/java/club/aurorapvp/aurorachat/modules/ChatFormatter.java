package club.aurorapvp.aurorachat.modules;

import io.papermc.paper.event.player.AsyncChatEvent;
import java.util.Objects;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ChatFormatter {

  public static final Chat chat =
      Objects.requireNonNull(Bukkit.getServer().getServicesManager().getRegistration(Chat.class))
          .getProvider();

  // TODO use lang
  public static void onJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    String prefix = chat.getPlayerPrefix(player);
    String suffix = chat.getPlayerSuffix(player);
    Component displayName = NameColor.getNameColor(player).getDisplayName();

    Component formattedJoinMessage =
        MiniMessage.miniMessage()
            .deserialize(prefix + "<reset> ")
            .append(displayName)
            .append(MiniMessage.miniMessage().deserialize(suffix + "<reset> "))
            .append(MiniMessage.miniMessage().deserialize("<reset><yellow> has joined the game."));

    event.joinMessage(formattedJoinMessage);
  }

  // TODO use lang
  public static void onQuit(PlayerQuitEvent event) {
    Player player = event.getPlayer();
    String prefix = chat.getPlayerPrefix(player);
    String suffix = chat.getPlayerSuffix(player);
    Component displayName = NameColor.getNameColor(player).getDisplayName();

    Component formattedQuitMessage =
        MiniMessage.miniMessage()
            .deserialize(prefix + "<reset> ")
            .append(displayName)
            .append(MiniMessage.miniMessage().deserialize(suffix + "<reset> "))
            .append(MiniMessage.miniMessage().deserialize("<yellow> has left the game."));

    event.quitMessage(formattedQuitMessage);
  }

  // TODO use lang
  public static void onChat(AsyncChatEvent event) {
    Player player = event.getPlayer();
    String prefix = chat.getPlayerPrefix(player);
    String suffix = chat.getPlayerSuffix(player);
    Component displayName = NameColor.getNameColor(player).getDisplayName();

    Component formattedMessage =
        MiniMessage.miniMessage()
            .deserialize(prefix + " <reset><")
            .append(displayName)
            .append(MiniMessage.miniMessage().deserialize(suffix + "> <reset>"))
            .append(event.message());

    event.renderer((source, sourceDisplayName, formatted, isNameMentioned) -> formattedMessage);
  }
}
