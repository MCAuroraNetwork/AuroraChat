package club.aurorapvp.aurorachat.commands;

import club.aurorapvp.aurorachat.AuroraChat;
import club.aurorapvp.aurorachat.modules.IgnoredPlayers;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Syntax;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class IgnoreCommand extends BaseCommand {

  @Default
  @CommandCompletion("@players")
  @Syntax("[player]")
  @Description("Marks a player to be ignored")
  @SuppressWarnings("unused")
  public void ignore(Player player, String playerName) {
    Player recipient = Bukkit.getPlayer(playerName);

    if (recipient == null) {
      player.sendMessage(AuroraChat.getInstance().getLang().getComponent("unknown-recipient"));
      return;
    }

    if (IgnoredPlayers.isIgnoredPlayer(player, recipient)) {
      IgnoredPlayers.removeIgnoredPlayer(player, recipient);
    } else {
      IgnoredPlayers.addIgnoredPlayer(player, recipient);
    }
  }
}
