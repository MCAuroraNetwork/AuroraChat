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

  public MessageCommands() {
    super("MessageCommands");
  }

  public void init(JavaPlugin plugin) {
    long startTime = System.currentTimeMillis();

    // TODO enable checking
    Commands.MANAGER.registerCommand(new MessageCommand(this));
    Commands.MANAGER.registerCommand(new ReplyCommand(this));

    boolean enabled = Config.get().getBoolean("message-commands.enable");

    this.setEnabled(enabled);

    AuroraChat.INSTANCE.getLogger().info(
        "MessageCommands module loaded in " + (System.currentTimeMillis() - startTime) +
            "ms. Enabled: " + enabled);
  }

  public void sendMessage(Player sender, Player recipient, String message) {
    sender.sendMessage(Lang.formatComponent("message-format", "You", recipient.getName(), message));
    recipient.sendMessage(Lang.formatComponent("message-format", sender.getName(), "You", message));

    lastSender.put(recipient, sender);
  }

  public void replyToMessage(Player sender, String message) {
    sendMessage(sender, lastSender.get(sender), message);
  }
}
