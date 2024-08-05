package club.aurorapvp.aurorachat.events;

import club.aurorapvp.aurorachat.AuroraChat;
import club.aurorapvp.aurorachat.events.listeners.NameTagEventListener;
import club.aurorapvp.aurorachat.events.listeners.PlayerEventListener;
import java.util.logging.Level;
import org.bukkit.Bukkit;

public class EventManager {

  public static void init() {
    Bukkit.getPluginManager().registerEvents(new PlayerEventListener(), AuroraChat.getInstance());

    try {
      Class.forName("org.purpurmc.purpur.event.entity.EntityTeleportHinderedEvent");

      Bukkit.getPluginManager()
          .registerEvents(new NameTagEventListener(), AuroraChat.getInstance());
    } catch (ClassNotFoundException e) {
      AuroraChat.getInstance().getLogger().log(Level.INFO, "It is recommended to use Purpur!");
    }
  }
}
