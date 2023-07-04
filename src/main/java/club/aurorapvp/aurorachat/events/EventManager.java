package club.aurorapvp.aurorachat.events;

import club.aurorapvp.aurorachat.AuroraChat;
import club.aurorapvp.aurorachat.events.listeners.PlayerEventListener;
import org.bukkit.Bukkit;

public class EventManager {
  public static void init() {
    Bukkit.getPluginManager().registerEvents(new PlayerEventListener(), AuroraChat.INSTANCE);
  }
}
