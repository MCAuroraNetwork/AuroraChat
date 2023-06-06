package club.aurorapvp;

import club.aurorapvp.config.Config;
import club.aurorapvp.config.Lang;
import org.bukkit.plugin.java.JavaPlugin;

public final class AuroraChat extends JavaPlugin {
  public static JavaPlugin INSTANCE;

  @Override
  public void onEnable() {
    long startTime = System.currentTimeMillis();

    INSTANCE = this;

    // Initialize classes
    Config.init();
    Lang.init();
    ChatModules.init();
    Commands.init();

    getLogger().info(
        "AuroraChat loaded in " + (System.currentTimeMillis() - startTime) +
            "ms");
  }

  @Override
  public void onDisable() {
    getLogger().info("AuroraChat Unloaded");
  }
}