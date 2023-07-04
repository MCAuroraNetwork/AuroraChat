package club.aurorapvp.aurorachat;

import club.aurorapvp.aurorachat.commands.CommandManager;
import club.aurorapvp.aurorachat.config.Config;
import club.aurorapvp.aurorachat.config.Lang;
import club.aurorapvp.aurorachat.modules.AutoMessages;
import club.aurorapvp.aurorachat.modules.ChatCooldown;
import club.aurorapvp.aurorachat.modules.SimilarMessageBlocker;
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
    AutoMessages.init();
    ChatCooldown.init();
    SimilarMessageBlocker.init();
    CommandManager.init();

    getLogger().info(
        "AuroraChat loaded in " + (System.currentTimeMillis() - startTime) +
            "ms");
  }

  @Override
  public void onDisable() {
    getLogger().info("AuroraChat Unloaded");
  }
}