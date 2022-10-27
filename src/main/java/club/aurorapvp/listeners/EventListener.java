package club.aurorapvp.listeners;

import static club.aurorapvp.AuroraChat.config;
import static club.aurorapvp.config.LangHandler.getLangComponent;
import static club.aurorapvp.modules.AutoMessages.sendJoinMessages;
import static club.aurorapvp.modules.ChatCooldown.checkCooldown;
import static club.aurorapvp.modules.SimilarMessageBlocker.violationChecker;

import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class EventListener extends YamlConfiguration implements Listener {
  public static Player p;

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {

    p = event.getPlayer();

    sendJoinMessages();

  }

  @EventHandler
  public void onPlayerChat(AsyncChatEvent event) {
    if (config.getBoolean("antispam.similarity-detection.enable") &&
        violationChecker(p, event.originalMessage())) {
      event.setCancelled(true);
      p.sendMessage(getLangComponent("message-similarity-violation"));
    }
    if (config.getBoolean("antispam.cooldown.enable") && checkCooldown(p)) {
      event.setCancelled(true);
      p.sendMessage(getLangComponent("cooldown-violation"));
    }
  }
}