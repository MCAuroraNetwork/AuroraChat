package club.aurorapvp.aurorachat.commands;

import club.aurorapvp.aurorachat.AuroraChat;
import co.aikar.commands.BukkitCommandCompletionContext;
import co.aikar.commands.CommandCompletions;
import co.aikar.commands.PaperCommandManager;
import net.kyori.adventure.text.format.NamedTextColor;

public class CommandManager {

  public static PaperCommandManager MANAGER = new PaperCommandManager(AuroraChat.getInstance());

  public static void init() {
    MANAGER.registerCommand(new HelpCommand());
    MANAGER.registerCommand(new MessageCommand());
    MANAGER.registerCommand(new ReplyCommand());
    MANAGER.registerCommand(new PluginCommands());
    MANAGER.registerCommand(new NameColorCommand());
    MANAGER.registerCommand(new IgnoreCommand());

    CommandCompletions<BukkitCommandCompletionContext> commandCompletions =
        MANAGER.getCommandCompletions();

    commandCompletions.registerCompletion(
        "colorNames",
        c -> NamedTextColor.NAMES.keys());
  }
}
