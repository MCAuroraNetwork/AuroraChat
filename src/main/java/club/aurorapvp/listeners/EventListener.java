package club.aurorapvp.listeners;

import static club.aurorapvp.AuroraChat.config;
import static club.aurorapvp.config.LangHandler.getLangComponent;
import static club.aurorapvp.modules.AutoMessages.sendJoinMessages;
import static club.aurorapvp.modules.SimilarMessageBlocker.violationChecker;

import io.papermc.paper.event.player.AsyncChatEvent;
import java.util.HashMap;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class EventListener extends YamlConfiguration implements Listener {
  public static HashMap<Component, UUID> messageContent = new HashMap<>();
  public static HashMap<Component, Long> messageTime = new HashMap<>();
  public static Player p;
  public static Component message;

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {

    p = event.getPlayer();

    sendJoinMessages();

  }

  @EventHandler
  public void onPlayerChat(AsyncChatEvent event) {
    if (config.getBoolean("antispam.enable")) {
      p = event.getPlayer();
      message = event.originalMessage();

      if (violationChecker()) {
        event.setCancelled(true);
        p.sendMessage(getLangComponent("antispam.message-similarity"));
      }
      messageContent.put(event.originalMessage(), p.getUniqueId());
      messageTime.put(event.originalMessage(), System.currentTimeMillis());
    }
  }
}