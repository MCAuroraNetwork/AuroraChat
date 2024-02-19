package club.aurorapvp.aurorachat.modules;

import club.aurorapvp.aurorachat.AuroraChat;
import io.papermc.paper.event.player.AsyncChatEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import org.bukkit.entity.Player;

public class ChatCooldown {

  private static final Map<Player, Long> timeOfLastMessage = new HashMap<>();
  private static ViolationHandler violationHandler;

  public static void reload() {
    violationHandler = new ViolationHandler(
        AuroraChat.getInstance().getConfig().getLong("antispam.cooldown.violations-expire") * 1000,
        AuroraChat.getInstance().getConfig().getInt("antispam.cooldown.max-violations"));

    AuroraChat.getInstance().getLogger().log(Level.INFO, "ChatCooldown module reloaded");
  }

  public static void onPlayerChat(AsyncChatEvent event) {
    if (!AuroraChat.getInstance().getConfig().getBoolean("antispam.cooldown.enable")) {
      return;
    }

    if (ChatCooldown.onCooldown(event.getPlayer())) {
      violationHandler.addViolation(event.getPlayer(), event);
    }

    timeOfLastMessage.put(event.getPlayer(), System.currentTimeMillis());
  }

  public static boolean onCooldown(Player p) {
    if (timeOfLastMessage.containsKey(p)) {
      return (timeOfLastMessage.get(p) - System.currentTimeMillis()) <=
          AuroraChat.getInstance().getConfig().getLong("antispam.cooldown.time") * 1000;
    }
    return false;
  }
}
