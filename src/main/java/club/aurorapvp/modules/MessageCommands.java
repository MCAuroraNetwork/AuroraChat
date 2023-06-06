package club.aurorapvp.modules;

import club.aurorapvp.AuroraChat;
import club.aurorapvp.Commands;
import club.aurorapvp.commands.MessageCommand;
import club.aurorapvp.commands.ReplyCommand;
import club.aurorapvp.config.Config;
import club.aurorapvp.config.Lang;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class MessageCommands extends ChatModule {
  private final Map<Player, Player> lastSender = new HashMap<>();

  protected MessageCommands(String name) {
    super(name);
  }

  public void init(JavaPlugin plugin) {
    long startTime = System.currentTimeMillis();

    Commands.MANAGER.registerCommand(new MessageCommand());
    Commands.MANAGER.registerCommand(new ReplyCommand());

    boolean enabled = Config.get().getBoolean("messages.enabled");

    this.setEnabled(enabled);

    AuroraChat.INSTANCE.getLogger().info(
        "AutoMessages module loaded in " + (System.currentTimeMillis() - startTime) +
            "ms. Enabled: " + enabled);
  }

  public void sendMessage(Player sender, Player reciever, String message) {
    sender.sendMessage(Lang.formatComponent("message-format", "You", reciever.getName(), message));
    sender.sendMessage(Lang.formatComponent("message-format", sender.getName(), sender.getName(), message));

    lastSender.put(reciever, sender);
  }

  public void replyToMessage(Player sender, String message) {
    sendMessage(sender, lastSender.get(sender), message);
  }
}
