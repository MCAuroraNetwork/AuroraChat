package club.aurorapvp.aurorachat.events.listeners;

import club.aurorapvp.aurorachat.modules.NameTag;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.purpurmc.purpur.event.entity.EntityTeleportHinderedEvent;

public class NameTagEventListener implements Listener {

  @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
  public void onPlayerJoin(PlayerJoinEvent event) {
    NameTag.onPlayerJoin(event);
  }

  @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
  public void onPlayerTeleport(PlayerTeleportEvent event) {
    NameTag.onPlayerTeleport(event);
  }

  @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
  public void onTeleportHindered(EntityTeleportHinderedEvent event) {
    NameTag.onPlayerTeleportHindered(event);
  }
}
