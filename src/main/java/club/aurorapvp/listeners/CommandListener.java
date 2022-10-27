package club.aurorapvp.listeners;

import static club.aurorapvp.modules.HelpCommand.helpCmd;
import static club.aurorapvp.modules.PlayerChatCommands.messageCmd;
import static club.aurorapvp.modules.PlayerChatCommands.replyCmd;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class CommandListener implements CommandExecutor {

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                           @NotNull String label, String[] args) {

    switch (command.getName()) {
      case "help" -> helpCmd();
      case "msg" -> messageCmd(Bukkit.getPlayer(args[0]), sender, args);
      case "reply" -> replyCmd(sender, args);
    }

    return true;
  }
}
