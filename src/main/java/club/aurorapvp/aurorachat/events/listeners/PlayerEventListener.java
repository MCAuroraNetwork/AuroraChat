package club.aurorapvp.aurorachat.events.listeners;

import club.aurorapvp.aurorachat.modules.*;
import github.scarsz.discordsrv.api.events.GameChatMessagePreProcessEvent;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerEventListener implements Listener {

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    AutoMessages.sendJoinMessages(event);
    ChatFormatter.onJoin(event);
    IgnoredPlayers.onJoin(event);
  }

  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent event) {
    ChatFormatter.onQuit(event);
    NameColor.remove(event.getPlayer());
  }

  @EventHandler
  public void handleGeneralChat(AsyncChatEvent event) {
    ChatCooldown.onChat(event);
    SimilarMessageBlocker.onChat(event);
    ChatFormatter.onChat(event);
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void checkDisabled(AsyncChatEvent event) {
    ChatGroup.checkDisabled(event);
  }

  @EventHandler
  public void onDiscordChat(GameChatMessagePreProcessEvent event) {
    ChatGroup.onDiscordChat(event);
  }

  @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
  public void handleGroupChat(AsyncChatEvent event) {
    ChatGroup.onChat(event);
  }
}
