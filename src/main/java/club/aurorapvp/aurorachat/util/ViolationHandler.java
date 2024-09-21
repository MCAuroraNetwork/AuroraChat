package club.aurorapvp.aurorachat.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import org.bukkit.entity.Player;

public class ViolationHandler {

  private final Map<Player, Integer> violations = new HashMap<>();
  private final long expirationDelay;
  protected final int maxViolations;

  public ViolationHandler(long expirationDelay, int maxViolations) {
    this.expirationDelay = expirationDelay;
    this.maxViolations = maxViolations;
  }

  public boolean addViolation(Player player) {
    violations.put(player, violations.getOrDefault(player, 0) + 1);

    new Timer()
        .schedule(
            new TimerTask() {
              @Override
              public void run() {
                removeViolation(player);

                this.cancel();
              }
            },
            expirationDelay);

    return violations.get(player) >= maxViolations;
  }

  private void removeViolation(Player player) {
    violations.put(player, violations.get(player) - 1);
  }
}
