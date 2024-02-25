package club.aurorapvp.aurorachat.modules;

import io.papermc.paper.event.player.AsyncChatEvent;
import java.util.Objects;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;

public class ChatFormatter {

  private static final Chat chat = Objects.requireNonNull(
      Bukkit.getServer().getServicesManager().getRegistration(Chat.class)).getProvider();

  public static void onChat(AsyncChatEvent event) {
    String prefix = chat.getPlayerPrefix(event.getPlayer());
    String suffix = chat.getPlayerSuffix(event.getPlayer());
    NamedTextColor color = PlayerColorName.getColor(event.getPlayer());
    Component displayName = event.getPlayer().displayName();

    Component formattedMessage = MiniMessage.miniMessage().deserialize(prefix + " <reset><")
        .append(displayName.color(color))
        .append(MiniMessage.miniMessage().deserialize(suffix + "> <reset>"))
        .append(event.message());

    event.renderer((source, sourceDisplayName, formatted, isNameMentioned) -> formattedMessage);
  }
}