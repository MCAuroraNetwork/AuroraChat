package club.aurorapvp.aurorachat;

import club.aurorapvp.aurorachat.commands.CommandManager;
import club.aurorapvp.aurorachat.config.Config;
import club.aurorapvp.aurorachat.config.Lang;
import club.aurorapvp.aurorachat.events.EventManager;
import club.aurorapvp.aurorachat.modules.AutoMessages;
import club.aurorapvp.aurorachat.modules.ChatCooldown;
import club.aurorapvp.aurorachat.modules.SimilarMessageBlocker;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class AuroraChat extends JavaPlugin {

  private static AuroraChat INSTANCE;
  private Config config;
  private Lang lang;

  public static AuroraChat getInstance() {
    return INSTANCE;
  }

  public Lang getLang() {
    return lang;
  }

  public @NotNull YamlConfiguration getConfig() {
    return config.getYaml();
  }

  @Override
  public void onEnable() {
    long startTime = System.currentTimeMillis();

    INSTANCE = this;

    // Setup configs
    config = new Config();
    lang = new Lang();

    // Initialize classes
    EventManager.init();
    CommandManager.init();
    AutoMessages.reload();
    ChatCooldown.reload();
    SimilarMessageBlocker.reload();

    getLogger().info(
        "AuroraChat loaded in " + (System.currentTimeMillis() - startTime) +
            "ms");
  }

  @Override
  public void onDisable() {
    getLogger().info("AuroraChat Unloaded");
  }

  public void reloadConfig() {
    config.reload();
  }
}