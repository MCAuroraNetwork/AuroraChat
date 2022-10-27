package club.aurorapvp.listeners;

import static club.aurorapvp.modules.HelpCommand.helpCmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class CommandListener implements CommandExecutor {

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                           @NotNull String label, String[] args) {

    if (command.getName().equals("aurorakits")) {
      helpCmd();
    }

    return true;
  }
}
