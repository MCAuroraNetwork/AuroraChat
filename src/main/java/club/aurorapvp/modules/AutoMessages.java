package club.aurorapvp.modules;

import club.aurorapvp.AuroraChat;
import club.aurorapvp.config.Config;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class AutoMessages extends ChatModule {
  private String[] autoMessages;
  private String[] joinMessages;
  private String[] firstJoinMessages;

  public AutoMessages() {
    super("AutoMessages");
  }

  public void init(JavaPlugin plugin) {
    long startTime = System.currentTimeMillis();

    this.loadMessages();
    Bukkit.getPluginManager().registerEvents(this, plugin);

    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    final Runnable sendMessages = () -> {
      for (Player p : Bukkit.getOnlinePlayers()) {
        for (String message : autoMessages) {
          p.sendMessage(Component.text(message));
        }
      }
    };

    executor.scheduleAtFixedRate(sendMessages, 0,
        Config.get().getLong("messages.auto-messages.interval"), TimeUnit.SECONDS);

    boolean enabled = Config.get().getBoolean("messages.enabled");

    this.setEnabled(enabled);

    AuroraChat.INSTANCE.getLogger().info(
        "AutoMessages module loaded in " + (System.currentTimeMillis() - startTime) +
            "ms. Enabled: " + enabled);
  }

  private void loadMessages() {
    Object[] paths =
        Objects.requireNonNull(Config.get().getConfigurationSection("messages.auto-messages"))
            .getKeys(false).toArray();
    autoMessages = new String[paths.length];

    int i = 0;
    for (Object path : paths) {
      autoMessages[i] = Config.get().getString((String) path);
      i++;
    }

    paths = Objects.requireNonNull(Config.get().getConfigurationSection("messages.join-messages"))
        .getKeys(false).toArray();
    joinMessages = new String[paths.length];

    i = 0;
    for (Object path : paths) {
      joinMessages[i] = Config.get().getString((String) path);
      i++;
    }

    paths =
        Objects.requireNonNull(Config.get().getConfigurationSection("messages.first-join-messages"))
            .getKeys(false)
            .toArray();
    firstJoinMessages = new String[paths.length];

    i = 0;
    for (Object path : paths) {
      firstJoinMessages[i] = Config.get().getString((String) path);
      i++;
    }
  }

  @EventHandler
  public void sendJoinMessages(PlayerJoinEvent event) {
    Player p = event.getPlayer();

    for (String message : joinMessages) {
      p.sendMessage(Component.text(message));
    }

    if (!p.hasPlayedBefore()) {
      return;
    }

    for (String message : firstJoinMessages) {
      p.sendMessage(Component.text(message));
    }
  }
}
