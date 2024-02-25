package club.aurorapvp.aurorachat.events.listeners;

import club.aurorapvp.aurorachat.modules.AutoMessages;
import club.aurorapvp.aurorachat.modules.ChatCooldown;
import club.aurorapvp.aurorachat.modules.ChatFormatter;
import club.aurorapvp.aurorachat.modules.PlayerColorName;
import club.aurorapvp.aurorachat.modules.SimilarMessageBlocker;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerEventListener implements Listener {

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    AutoMessages.sendJoinMessages(event);
    new PlayerColorName(event.getPlayer());
  }

  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent event) {
    PlayerColorName.remove(event.getPlayer());
  }

  @EventHandler
  public void onPlayerChat(AsyncChatEvent event) {
    ChatCooldown.onChat(event);
    SimilarMessageBlocker.onChat(event);
    ChatFormatter.onChat(event);
  }
}
