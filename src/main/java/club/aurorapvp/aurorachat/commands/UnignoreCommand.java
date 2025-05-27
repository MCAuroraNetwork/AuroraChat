package club.aurorapvp.aurorachat.commands;

import club.aurorapvp.aurorachat.AuroraChat;
import club.aurorapvp.aurorachat.modules.IgnoredPlayers;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@CommandAlias("unignore")
public class UnignoreCommand extends BaseCommand {

  @Default
  @CommandCompletion("@players")
  @Syntax("[player]")
  @Description("Marks a player to be unignored")
  @SuppressWarnings("unused")
  public void ignore(Player player, String playerName) {
    Player recipient = Bukkit.getPlayer(playerName);

    if (recipient == null) {
      player.sendMessage(AuroraChat.getInstance().getLang().getComponent("unknown-recipient"));
      return;
    }

    if (!IgnoredPlayers.isIgnoredPlayer(player, recipient)) {
      player.sendMessage(AuroraChat.getInstance().getLang().getComponent("player-not-ignored"));

      return;
    }

    IgnoredPlayers.removeIgnoredPlayer(player, recipient);
  }
}
