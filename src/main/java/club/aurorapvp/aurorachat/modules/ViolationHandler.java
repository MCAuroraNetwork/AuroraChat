package club.aurorapvp.aurorachat.modules;

import club.aurorapvp.aurorachat.AuroraChat;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class ViolationHandler {

  private final Map<Player, Integer> violations = new HashMap<>();
  private final Map<Player, Timer> nextViolationClear = new HashMap<>();
  private final long expirationDelay;
  protected final int maxViolations;

  public ViolationHandler(long expirationDelay, int maxViolations) {
    this.expirationDelay = expirationDelay;
    this.maxViolations = maxViolations;
  }

  public void addViolation(Player p, Cancellable event) {
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
    }, expirationDelay);

    nextViolationClear.put(p, timer);

    if (violations.get(p) >= maxViolations) {
      p.sendMessage(AuroraChat.getInstance().getLang().getComponent("cooldown-violation"));

      event.setCancelled(true);
    }
  }

  private void clearViolations(Player p) {
    violations.put(p, 0);

    Timer timer = nextViolationClear.remove(p);
    if (timer != null) {
      timer.cancel();
    }
  }
}
