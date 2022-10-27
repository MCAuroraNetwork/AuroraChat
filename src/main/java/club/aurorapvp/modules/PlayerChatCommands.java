package club.aurorapvp.modules;

import static club.aurorapvp.AuroraChat.deserializeComponent;
import static club.aurorapvp.config.LangHandler.getLangComponent;

import java.util.HashMap;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerChatCommands {
  public static HashMap<String, String> lastRecieved = new HashMap<>();

  public static void messageCmd(Player p, CommandSender sender, String[] args) {

    if (p != null && p.isOnline()) {
      String messageContent = StringUtils.join(args, " ").replaceFirst(p.getName(), "");

      p.sendMessage(deserializeComponent.deserialize(
          "<gradient:#FFAA00:#FF55FF><bold>" + sender.getName() + " -> " + p.getName() +
              "<reset> <italic><#AAAAAA>" +
              messageContent));
      sender.sendMessage(deserializeComponent.deserialize(
          "<gradient:#FFAA00:#FF55FF><bold>" + sender.getName() + " -> " + p.getName() +
              "<reset> <italic><#AAAAAA>" +
              messageContent));

      lastRecieved.put(sender.getName(), p.getName());
    } else {
      sender.sendMessage(getLangComponent("player-not-online"));
    }
  }

  public static void replyCmd(CommandSender sender, String[] args) {

    Player p = Bukkit.getPlayer(sender.getName());
    Player receiver = Bukkit.getPlayer(lastRecieved.get(p.getName()));

    if (receiver != null && receiver.isOnline()) {
      String messageContent = StringUtils.join(args, " ").replaceFirst(receiver.getName(), "");

      p.sendMessage(deserializeComponent.deserialize(
          "<gradient:#FFAA00:#FF55FF><bold>" + sender.getName() + " -> " + receiver.getName() +
              "<reset> <italic><#AAAAAA>" +
              messageContent));
      receiver.sendMessage(deserializeComponent.deserialize(
          "<gradient:#FFAA00:#FF55FF><bold>" + sender.getName() + " -> " + receiver.getName() +
              "<reset> <italic><#AAAAAA>" +
              messageContent));

      lastRecieved.put(receiver.getName(), p.getName());
    } else {
      sender.sendMessage(getLangComponent("player-not-online"));
    }
  }
}
