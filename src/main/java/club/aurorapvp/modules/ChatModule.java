package club.aurorapvp.modules;

import club.aurorapvp.AuroraChat;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class ChatModule implements Listener {
  private boolean enabled = false;
  private final String name;

  protected ChatModule(String name) {
    this.name = name;
  }

  public abstract void init(JavaPlugin plugin);

  protected void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public String getName() {
    return name;
  }

  public void enableModule() {
    this.init(AuroraChat.INSTANCE);
  }
}