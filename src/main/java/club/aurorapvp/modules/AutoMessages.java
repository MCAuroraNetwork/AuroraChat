package club.aurorapvp.modules;

import club.aurorapvp.AuroraChat;
import club.aurorapvp.config.Config;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
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

    Bukkit.getPluginManager().registerEvents(this, plugin);

    boolean enabled = Config.get().getBoolean("messages.enabled");

    this.setEnabled(enabled);

    AuroraChat.INSTANCE.getLogger().info(
        "AutoMessages module loaded in " + (System.currentTimeMillis() - startTime) +
            "ms. Enabled: " + enabled);
  }

  private void loadMessages() {
    ConfigurationSection messages = Config.get().getConfigurationSection("messages");

    assert messages != null;
    ConfigurationSection autoMessages = messages.getConfigurationSection("auto-messages");
    ConfigurationSection joinMessages = messages.getConfigurationSection("join-messages");
    ConfigurationSection firstJoinMessages =
        messages.getConfigurationSection("first-join-messages");

    assert autoMessages != null;
    assert joinMessages != null;
    assert firstJoinMessages != null;
    Set<String> autoKeys = autoMessages.getKeys(false);
    Set<String> joinKeys = joinMessages.getKeys(false);
    Set<String> firstJoinKeys = firstJoinMessages.getKeys(false);

    this.autoMessages = new String[autoKeys.size()];
    this.joinMessages = new String[joinKeys.size()];
    this.firstJoinMessages = new String[firstJoinKeys.size()];

    int i = 0;
    for (String key : autoKeys) {
      this.autoMessages[i] = autoMessages.getString(key);
      i++;
    }

    i = 0;
    for (String key : joinKeys) {
      this.joinMessages[i] = joinMessages.getString(key);
      i++;
    }

    i = 0;
    for (String key : firstJoinKeys) {
      this.firstJoinMessages[i] = firstJoinMessages.getString(key);
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
