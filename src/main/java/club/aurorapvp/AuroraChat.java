package club.aurorapvp;

import club.aurorapvp.config.ConfigHandler;
import club.aurorapvp.config.LangHandler;
import club.aurorapvp.listeners.CommandListener;
import club.aurorapvp.listeners.EventListener;
import club.aurorapvp.modules.SimilarMessageBlocker;
import club.aurorapvp.modules.TimedMessages;
import java.io.File;
import java.util.List;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommandYamlParser;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class AuroraChat extends JavaPlugin {
  public static Plugin plugin;
  public static File DataFolder;
  public static PlainTextComponentSerializer serializeComponent;
  public static MiniMessage deserializeComponent;
  public static String prefix;
  public static YamlConfiguration lang;
  public static FileConfiguration config;

  @Override
  public void onEnable() {
    //Register Listeners
    getServer().getPluginManager().registerEvents(new EventListener(), this);
    List<Command> commandList = PluginCommandYamlParser.parse(plugin);
    for (Command command : commandList) {
      getCommand(command.getName()).setExecutor(new CommandListener());
    }

    // Setup configs
    saveDefaultConfig();
    saveResource("lang.yml", false);
    LangHandler.setup();
    SimilarMessageBlocker.setup();

    // Setup Serializers
    serializeComponent = PlainTextComponentSerializer.plainText();
    deserializeComponent = MiniMessage.miniMessage();

    // Load some modules
    TimedMessages.setup();

    // Setup variables
    plugin = Bukkit.getPluginManager().getPlugin("AuroraChat");
    DataFolder = Bukkit.getServer().getPluginManager().getPlugin("AuroraChat").getDataFolder();
    lang = YamlConfiguration.loadConfiguration(new File(DataFolder, "lang.yml"));
    config = YamlConfiguration.loadConfiguration(new File(DataFolder, "config.yml"));
    prefix = lang.getString("prefix");

    plugin.getLogger().info("AuroraChat loaded");
  }

  @Override
  public void onDisable() {
    plugin.getLogger().info("AuroraChat Unloaded");
  }
}