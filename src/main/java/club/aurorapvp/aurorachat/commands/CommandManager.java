package club.aurorapvp.aurorachat.commands;

import club.aurorapvp.aurorachat.AuroraChat;
import co.aikar.commands.PaperCommandManager;

public class CommandManager {

  public static PaperCommandManager MANAGER = new PaperCommandManager(AuroraChat.getInstance());

  public static void init() {
    MANAGER.registerCommand(new HelpCommand());
    CommandManager.MANAGER.registerCommand(new MessageCommand());
    CommandManager.MANAGER.registerCommand(new ReplyCommand());
  }
}
