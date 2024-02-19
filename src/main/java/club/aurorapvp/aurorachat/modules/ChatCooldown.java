package club.aurorapvp.aurorachat.modules;

import club.aurorapvp.aurorachat.AuroraChat;
import io.papermc.paper.event.player.AsyncChatEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class ChatCooldown {

  private static final Map<Player, Long> timeOfLastMessage = new HashMap<>();
  private static final Map<Player, Integer> violations = new HashMap<>();
  private static final Map<Player, Timer> nextViolationClear = new HashMap<>();
  private static long violationExpiryDelay;
  protected static int maxViolations;

  public static void reload() {
    violationExpiryDelay =
        AuroraChat.getInstance().getConfig().getLong("antispam.cooldown.violations-expire") * 1000;
    maxViolations = AuroraChat.getInstance().getConfig().getInt("antispam.cooldown.max-violations");

    AuroraChat.getInstance().getLogger().log(Level.INFO, "ChatCooldown module reloaded");
  }

  public static void onPlayerChat(AsyncChatEvent event) {
    if (!AuroraChat.getInstance().getConfig().getBoolean("antispam.cooldown.enable")) {
      return;
    }

    if (ChatCooldown.onCooldown(event.getPlayer())) {
      ChatCooldown.addViolation(event.getPlayer(), event);
    }

    timeOfLastMessage.put(event.getPlayer(), System.currentTimeMillis());
  }

  private static long getExpirationDelay() {
    return violationExpiryDelay;
  }

  private static void addViolation(Player p, Cancellable event) {
    violations.put(p, violations.getOrDefault(p, 0) + 1);

    Timer timer = nextViolationClear.get(p);

    if (timer != null) {
      timer.cancel();
    }

    timer = new Timer();
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        clearViolations(p);
      }
    }, ChatCooldown.getExpirationDelay());

    nextViolationClear.put(p, timer);

    if (violations.get(p) >= maxViolations) {
      punish(p, event);
    }
  }

  private static void clearViolations(Player p) {
    violations.put(p, 0);

    Timer timer = nextViolationClear.remove(p);
    if (timer != null) {
      timer.cancel();
    }
  }

  public static boolean onCooldown(Player p) {
    if (timeOfLastMessage.containsKey(p)) {
      return (timeOfLastMessage.get(p) - System.currentTimeMillis()) <=
          AuroraChat.getInstance().getConfig().getLong("antispam.cooldown.time") * 1000;
    }
    return false;
  }

  private static void punish(Player p, Cancellable event) {
    p.sendMessage(AuroraChat.getInstance().getLang().getComponent("cooldown-violation"));

    event.setCancelled(true);
  }
}
