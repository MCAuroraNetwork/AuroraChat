package club.aurorapvp.aurorachat.commands;

import club.aurorapvp.aurorachat.AuroraChat;
import club.aurorapvp.aurorachat.modules.AutoMessages;
import club.aurorapvp.aurorachat.modules.ChatCooldown;
import club.aurorapvp.aurorachat.modules.SimilarMessageBlocker;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import org.bukkit.entity.Player;

@CommandAlias("aurorachat")
public class PluginCommands extends BaseCommand {

  @Subcommand("reload")
  @CommandPermission("aurorachat.command.reload")
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
}
