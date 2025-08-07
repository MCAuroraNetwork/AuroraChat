package club.aurorapvp.aurorachat.commands;

import club.aurorapvp.aurorachat.AuroraChat;
import club.aurorapvp.aurorachat.util.ExtendedTextColor;
import co.aikar.commands.BukkitCommandCompletionContext;
import co.aikar.commands.CommandCompletions;
import co.aikar.commands.PaperCommandManager;

public class CommandManager {

  public static PaperCommandManager MANAGER = new PaperCommandManager(AuroraChat.getInstance());

  public static void init() {
    MANAGER.registerCommand(new HelpCommand());
    MANAGER.registerCommand(new MessageCommand());
    MANAGER.registerCommand(new ReplyCommand());
    MANAGER.registerCommand(new PluginCommands());
    MANAGER.registerCommand(new NameColorCommand());
    MANAGER.registerCommand(new IgnoreCommand());
    MANAGER.registerCommand(new UnignoreCommand());

    CommandCompletions<BukkitCommandCompletionContext> commandCompletions =
        MANAGER.getCommandCompletions();

    commandCompletions.registerCompletion(
        "colorNames",
        c -> ExtendedTextColor.NAMES.keys());
  }
}
