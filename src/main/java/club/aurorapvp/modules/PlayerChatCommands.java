package club.aurorapvp.modules;

import static club.aurorapvp.AuroraChat.deserializeComponent;
import static club.aurorapvp.config.LangHandler.getLangComponent;

import java.util.HashMap;
import net.kyori.adventure.text.Component;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerChatCommands {
  public static HashMap<Player, Player> lastRecieved = new HashMap<>();

  public static void messageCmd(String displayName, CommandSender sender, String[] args) {

    if (Bukkit.getPlayerExact(displayName).isOnline()) {
      Player p = Bukkit.getPlayer(displayName);
      String messageContent = StringUtils.join(args, " ").trim();

      p.sendMessage(deserializeComponent.deserialize(displayName + "->" + messageContent));
      sender.sendMessage(deserializeComponent.deserialize(displayName + "->" + messageContent));

      lastRecieved.put(Bukkit.getPlayer(sender.getName()), p);
    } else {
      sender.sendMessage(getLangComponent("player-not-online"));
    }
  }

  public static void replyCmd(CommandSender sender, String[] args) {

    Player p = Bukkit.getPlayer(sender.getName());
    Player receiver = lastRecieved.get(p);

    if (receiver.isOnline()) {
      String messageContent = StringUtils.join(args, " ").trim();

      p.sendMessage(deserializeComponent.deserialize(sender.getName() + "->" + messageContent));
      receiver.sendMessage(deserializeComponent.deserialize(sender.getName() + "->" + messageContent));

      lastRecieved.put(receiver, p);
    } else {
      sender.sendMessage(getLangComponent("player-not-online"));
    }
  }
}
