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

  public boolean addViolation(Player player) {
    violations.put(player, violations.getOrDefault(player, 0) + 1);

    Timer timer = nextViolationClear.get(player);

    if (timer != null) {
      timer.cancel();
    }

    timer = new Timer();
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        clearViolations(player);
      }
    }, expirationDelay);

    nextViolationClear.put(player, timer);

    return violations.get(player) >= maxViolations;
  }

  private void clearViolations(Player p) {
    violations.put(p, 0);

    Timer timer = nextViolationClear.remove(p);
    if (timer != null) {
      timer.cancel();
    }
  }
}
