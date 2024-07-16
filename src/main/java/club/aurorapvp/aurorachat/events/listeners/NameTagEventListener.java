package club.aurorapvp.aurorachat.events.listeners;

import club.aurorapvp.aurorachat.modules.NameTagManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class NameTagEventListener implements Listener {

  @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
  public void onPlayerJoin(PlayerJoinEvent event) {
    NameTagManager.onPlayerJoin(event);
  }

  @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
  public void onPlayerTeleport(PlayerTeleportEvent event) {
    NameTagManager.onPlayerTeleport(event);
  }

  @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
  public void onPlayerMove(PlayerMoveEvent event) {
    NameTagManager.onPlayerMove(event);
  }
}
