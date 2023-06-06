package club.aurorapvp;

import club.aurorapvp.commands.HelpCommand;
import co.aikar.commands.PaperCommandManager;

public class Commands {
  public static PaperCommandManager MANAGER = new PaperCommandManager(AuroraChat.INSTANCE);

  public static void init() {
    MANAGER.registerCommand(new HelpCommand());
  }
}
