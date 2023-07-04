package club.aurorapvp.aurorachat.events.listeners;

import club.aurorapvp.aurorachat.modules.AutoMessages;
import club.aurorapvp.aurorachat.modules.ChatCooldown;
import club.aurorapvp.aurorachat.modules.SimilarMessageBlocker;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerEventListener implements Listener {
  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    AutoMessages.sendJoinMessages(event);
  }

  @EventHandler
  public void onPlayerChat(AsyncChatEvent event) {
    ChatCooldown.onPlayerChat(event);
    SimilarMessageBlocker.onAsyncChat(event);
  }
}
