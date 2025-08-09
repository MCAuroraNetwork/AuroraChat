package club.aurorapvp.aurorachat.commands;

import club.aurorapvp.aurorachat.AuroraChat;
import club.aurorapvp.aurorachat.modules.AutoMessages;
import club.aurorapvp.aurorachat.modules.moderation.ChatCooldown;
import club.aurorapvp.aurorachat.modules.moderation.SimilarMessageBlocker;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CatchUnknown;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import org.bukkit.entity.Player;

@CommandAlias("aurorachat")
@CommandPermission("aurorachat.command.aurorachat")
public class PluginCommands extends BaseCommand {

  @Subcommand("reload")
  @CommandPermission("aurorachat.command.aurorachat.reload")
  @Description("Reloads all plugin configs")
  @SuppressWarnings("unused")
  public void onReload(Player player) {
    long startTime = System.currentTimeMillis();

    AuroraChat.getInstance().getLang().reload();
    AuroraChat.getInstance().reloadConfig();
    AutoMessages.reload();
    ChatCooldown.reload();
    SimilarMessageBlocker.reload();

    player.sendMessage(AuroraChat.getInstance().getLang().getComponent("reloaded"));

    AuroraChat.getInstance()
        .getLogger()
        .info("AuroraChat reloaded in " + (System.currentTimeMillis() - startTime) + "ms");
  }

  @Subcommand("help")
  @CommandPermission("aurorachat.command.aurorachat.help")
  @Description("Explains all plugin commands")
  @CatchUnknown
  @SuppressWarnings("unused")
  public void onHelp(Player player) {
    player.sendMessage(AuroraChat.getInstance().getLang().getComponent("help-command"));
  }
}
