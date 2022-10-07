package club.aurorapvp;

import static club.aurorapvp.config.ConfigHandler.generateDefaults;

import club.aurorapvp.config.ConfigHandler;
import club.aurorapvp.listeners.EventListener;
import club.aurorapvp.modules.SimilarMessages;
import java.io.File;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class AuroraChat extends JavaPlugin {
  public static Plugin plugin;
  public static File DataFolder;
  public static PlainTextComponentSerializer serializeComponent;

  @Override
  public void onEnable() {
    //Register Listeners
    getServer().getPluginManager().registerEvents(new EventListener(), this);

    // Setup variables
    plugin = Bukkit.getPluginManager().getPlugin("AuroraChat");
    DataFolder = Bukkit.getServer().getPluginManager().getPlugin("AuroraChat").getDataFolder();
    serializeComponent = PlainTextComponentSerializer.plainText();

    // Setup configs
    saveDefaultConfig();
    ConfigHandler.setup();
    generateDefaults();
    SimilarMessages.setup();

    plugin.getLogger().info("AuroraChat loaded");
  }

  @Override
  public void onDisable() {
    plugin.getLogger().info("AuroraChat Unloaded");
  }
}