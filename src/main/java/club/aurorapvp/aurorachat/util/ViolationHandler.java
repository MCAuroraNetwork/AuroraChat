package club.aurorapvp.aurorachat.util;

import java.util.*;

import org.bukkit.entity.Player;

public class ViolationHandler {

  private final Map<UUID, Integer> violations = new HashMap<>();
  private final long expirationDelay;
  protected final int maxViolations;

  public ViolationHandler(long expirationDelay, int maxViolations) {
    this.expirationDelay = expirationDelay;
    this.maxViolations = maxViolations;
  }

  public boolean addViolation(Player player) {
    violations.put(player.getUniqueId(), violations.getOrDefault(player.getUniqueId(), 0) + 1);

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

    return violations.get(player.getUniqueId()) >= maxViolations;
  }

  private void removeViolation(Player player) {
    violations.put(player.getUniqueId(), violations.get(player.getUniqueId()) - 1);
  }
}
