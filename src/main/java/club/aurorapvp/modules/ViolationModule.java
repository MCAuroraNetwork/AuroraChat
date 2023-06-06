package club.aurorapvp.modules;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public abstract class ViolationModule extends ChatModule {
  private final Map<Player, Integer> violations = new HashMap<>();
  private final Map<Player, Timer> nextViolationClear = new HashMap<>();
  private final long violationExpiryDelay;
  protected final int maxViolations;

  protected ViolationModule(String name, long violationExpiryDelay, int maxViolations) {
    super(name);
    this.violationExpiryDelay = violationExpiryDelay;
    this.maxViolations = maxViolations;
  }

  protected abstract void punish(Player p, Cancellable event);

  protected void addViolation(Player p, Cancellable event) {
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
    }, this.getExpirationDelay());

    nextViolationClear.put(p, timer);

    if (violations.get(p) >= maxViolations) {
      punish(p, event);
    }
  }

  protected int getViolations(Player p) {
    return violations.get(p);
  }

  protected void clearViolations(Player p) {
    violations.put(p, 0);

    Timer timer = nextViolationClear.remove(p);
    if (timer != null) {
      timer.cancel();
    }
  }

  protected long getExpirationDelay() {
    return violationExpiryDelay;
  }
}

