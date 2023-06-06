package club.aurorapvp.modules;

import club.aurorapvp.AuroraChat;
import club.aurorapvp.config.Config;
import club.aurorapvp.config.Lang;
import io.papermc.paper.event.player.AsyncChatEvent;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;

public class ChatCooldown extends ViolationModule {
  private static final Map<Player, Long> timeOfLastMessage = new HashMap<>();

  public void init(JavaPlugin plugin) {
    long startTime = System.currentTimeMillis();

    boolean enabled = Config.get().getBoolean("antispam.cooldown.enable");

    this.setEnabled(enabled);

    AuroraChat.INSTANCE.getLogger().info(
        "ChatCooldown module loaded in " + (System.currentTimeMillis() - startTime) +
            "ms. Enabled: " + enabled);
  }

  protected ChatCooldown() {
    super("ChatCooldown", Config.get().getLong("antispam.cooldown.violations-expire") * 1000,
        Config.get().getInt("antispam.cooldown.max-violations"));
  }

  @EventHandler
  public void onPlayerChat(AsyncChatEvent event) {
    if (ChatCooldown.onCooldown(event.getPlayer())) {
      this.addViolation(event.getPlayer(), event);
    }

    timeOfLastMessage.put(event.getPlayer(), System.currentTimeMillis());
  }

  public static boolean onCooldown(Player p) {
    return (timeOfLastMessage.get(p) - System.currentTimeMillis()) <=
        Config.get().getLong("antispam.cooldown.time") * 1000;
  }

  protected void punish(Player p, Cancellable event) {
    p.sendMessage(Lang.getComponent("cooldown-violation"));

    event.setCancelled(true);
  }
}
