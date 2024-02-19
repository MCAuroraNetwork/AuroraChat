package club.aurorapvp.aurorachat.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CatchUnknown;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;

@CommandAlias("help")
public class HelpCommand extends BaseCommand {

  @CatchUnknown
  @Default
  public void helpCommand() {

  }
}
