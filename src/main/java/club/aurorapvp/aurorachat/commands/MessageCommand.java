package club.aurorapvp.aurorachat.commands;

import club.aurorapvp.aurorachat.AuroraChat;
import club.aurorapvp.aurorachat.modules.MessageCommands;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Syntax;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@CommandAlias("message|msg|tell")
public class MessageCommand extends BaseCommand {

  @Default
  @CommandCompletion("@players")
  @Syntax("[player] [message]")
  @Description("Sends a message to another player")
  @SuppressWarnings("unused")
  public void messageCommand(Player player, String playerName, String message) {
    Player recipient = Bukkit.getPlayer(playerName);

    if (recipient == null) {
      player.sendMessage(AuroraChat.getInstance().getLang().getComponent("unknown-recipient"));
      return;
    }

    MessageCommands.sendMessage(player, recipient, message);
  }
}
