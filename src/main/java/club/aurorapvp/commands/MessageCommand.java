package club.aurorapvp.commands;

import club.aurorapvp.ChatModules;
import club.aurorapvp.modules.MessageCommands;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CatchUnknown;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@CommandAlias("message|msg|tell")
public class MessageCommand extends BaseCommand {
  private final MessageCommands module;

  public MessageCommand(MessageCommands module) {
    this.module = module;
  }

  @Default
  @CommandCompletion("@players")
  @Syntax("[player] [message]")
  @Description("Sends a message to another player")
  public void messageCommand(Player p, String player, String message) {
    Player recipient = Bukkit.getPlayer(player);

    assert recipient != null;
    module.sendMessage(p, recipient, message);
  }

  @CatchUnknown
  @Subcommand("help")
  public void onUnknown() {

  }
}
