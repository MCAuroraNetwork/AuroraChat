package club.aurorapvp.aurorachat.modules;

import club.aurorapvp.aurorachat.AuroraChat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class MessageCommands {

  private static final Map<UUID, UUID> lastSender = new HashMap<>();

  public static void sendMessage(Player sender, Player recipient, String message) {
    sender.sendMessage(
        AuroraChat.getInstance().getLang()
            .formatComponent("message-format", "You", recipient.getName(), message));
    recipient.sendMessage(AuroraChat.getInstance().getLang()
        .formatComponent("message-format", sender.getName(), "You", message));

    lastSender.put(recipient.getUniqueId(), sender.getUniqueId());
  }

  public static void replyToMessage(Player sender, String message) {
    sendMessage(sender, (Player) Bukkit.getOfflinePlayer(lastSender.get(sender.getUniqueId())), message);
  }
}