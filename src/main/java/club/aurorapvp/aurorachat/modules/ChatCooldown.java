package club.aurorapvp.aurorachat.modules;

import club.aurorapvp.aurorachat.config.Config;
import club.aurorapvp.aurorachat.config.Lang;
import io.papermc.paper.event.player.AsyncChatEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;

public class ChatCooldown {
  private static final Map<Player, Long> timeOfLastMessage = new HashMap<>();
  private static final Map<Player, Integer> violations = new HashMap<>();
  private static final Map<Player, Timer> nextViolationClear = new HashMap<>();
  private static long violationExpiryDelay;
  protected static int maxViolations;

  public static void init() {
    violationExpiryDelay = Config.get().getLong("antispam.cooldown.violations-expire") * 1000;
    maxViolations = Config.get().getInt("antispam.cooldown.max-violations");
  }

  public static void onPlayerChat(AsyncChatEvent event) {
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
          Config.get().getLong("antispam.cooldown.time") * 1000;
    }
    return false;
  }

  private static void punish(Player p, Cancellable event) {
    p.sendMessage(Lang.getComponent("cooldown-violation"));

    event.setCancelled(true);
  }
}
