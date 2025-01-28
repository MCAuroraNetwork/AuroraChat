package club.aurorapvp.aurorachat;

import club.aurorapvp.aurorachat.commands.CommandManager;
import club.aurorapvp.aurorachat.config.Config;
import club.aurorapvp.aurorachat.config.Lang;
import club.aurorapvp.aurorachat.events.EventManager;
import club.aurorapvp.aurorachat.modules.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class AuroraChat extends JavaPlugin {

  private static AuroraChat INSTANCE;
  private Config config;
  private Lang lang;
  private boolean placeholderApiInstalled = false;

  private MongoClient mongoClient;
  private MongoDatabase mongoDatabase;

  public static AuroraChat getInstance() {
    return INSTANCE;
  }

  public Lang getLang() {
    return lang;
  }

  public boolean isPlaceholderApiInstalled() {
    return placeholderApiInstalled;
  }

  public @NotNull YamlConfiguration getConfig() {
    return config.getYaml();
  }

  public MongoDatabase getDatabase() {
    return mongoDatabase;
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
    NameTag.init();
    TeamManager.init();
    AutoMessages.reload();
    ChatCooldown.reload();
    SimilarMessageBlocker.reload();

    // Check depends
    if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
      placeholderApiInstalled = true;
    }

    String connectionString =
        this.getConfig().getString("mongodb.address", "mongodb://localhost:27017");
    String databaseName = this.getConfig().getString("mongodb.database-name", "aurora_chat");

    mongoClient = MongoClients.create(connectionString);
    mongoDatabase = mongoClient.getDatabase(databaseName);

    getLogger().info("AuroraChat enabled in " + (System.currentTimeMillis() - startTime) + "ms");
  }

  @Override
  public void onDisable() {
    long startTime = System.currentTimeMillis();

    if (mongoClient != null) {
      mongoClient.close();
      getLogger().info("MongoDB connection closed.");
    }

    getLogger().info("AuroraChat disabled in " + (System.currentTimeMillis() - startTime) + "ms");
  }

  public void reloadConfig() {
    config.reload();
  }
}
