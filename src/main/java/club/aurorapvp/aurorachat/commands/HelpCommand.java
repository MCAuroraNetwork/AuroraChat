package club.aurorapvp.aurorachat.commands;

import club.aurorapvp.aurorachat.AuroraChat;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CatchUnknown;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import org.bukkit.entity.Player;

@CommandAlias("help")
@CommandPermission("aurorachat.command.help")
public class HelpCommand extends BaseCommand {

  @CatchUnknown
  @Default
  @SuppressWarnings("unused")
  public void onHelp(Player player) {
    player.sendMessage(AuroraChat.getInstance().getLang().getComponent("help"));
  }
}
