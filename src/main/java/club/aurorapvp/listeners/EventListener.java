package club.aurorapvp.listeners;

import static club.aurorapvp.modules.SimilarMessages.SimilarMessagesModule;

import club.aurorapvp.config.ConfigHandler;
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
  public static HashMap<Component, Long> messageTime = new HashMap<Component, Long>();
  public static Player p;
  public static Component message;

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    p = event.getPlayer();
    ConfigHandler.setup();

    for (Object path : ConfigHandler.get().getConfigurationSection("joinMessages")
        .getKeys(false).toArray()) {
      p.sendMessage(Component.text(ConfigHandler.get().getString("joinMessages." + path)));
    }

    if (!p.hasPlayedBefore()) {
      for (Object path : ConfigHandler.get().getConfigurationSection("firstJoinMessages")
          .getKeys(false).toArray()) {
        p.sendMessage(
            Component.text(ConfigHandler.get().getString("firstJoinMessages." + path)));
      }
    }
  }

  @EventHandler
  public void onPlayerChat(AsyncChatEvent event) {
    if (ConfigHandler.get().getBoolean("antispam.enable")) {
      p = event.getPlayer();
      message = event.originalMessage();

      if (SimilarMessagesModule()) {
        event.setCancelled(true);
      }
      messageContent.put(event.originalMessage(), p.getUniqueId());
      messageTime.put(event.originalMessage(), System.currentTimeMillis());
    }
  }
}