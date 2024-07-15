package club.aurorapvp.aurorachat.events.listeners;

import club.aurorapvp.aurorachat.events.custom.NameColorUpdateEvent;
import club.aurorapvp.aurorachat.modules.AutoMessages;
import club.aurorapvp.aurorachat.modules.ChatCooldown;
import club.aurorapvp.aurorachat.modules.NameColor;
import club.aurorapvp.aurorachat.modules.NameColor.ChatFormatter;
import club.aurorapvp.aurorachat.modules.NameTag;
import club.aurorapvp.aurorachat.modules.SimilarMessageBlocker;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class PlayerEventListener implements Listener {

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    AutoMessages.sendJoinMessages(event);
    new NameTag(event.getPlayer(), new NameColor(event.getPlayer()));
    ChatFormatter.onJoin(event);
  }

  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent event) {
    ChatFormatter.onQuit(event);
    NameColor.remove(event.getPlayer());
    NameTag.remove(event.getPlayer());
  }

  @EventHandler
  public void onPlayerChat(AsyncChatEvent event) {
    ChatCooldown.onChat(event);
    SimilarMessageBlocker.onChat(event);
    ChatFormatter.onChat(event);
  }

  @EventHandler
  public void onNameColorUpdate(NameColorUpdateEvent event) {
    NameTag.getNameTag(event.getPlayer()).update();
  }

  @EventHandler
  public void onMove(PlayerMoveEvent event) {
    NameTag.getNameTag(event.getPlayer()).onMove(event);
  }

  @EventHandler
  public void onSneak(PlayerToggleSneakEvent event) {
    NameTag.getNameTag(event.getPlayer()).onSneak(event);
  }
}
