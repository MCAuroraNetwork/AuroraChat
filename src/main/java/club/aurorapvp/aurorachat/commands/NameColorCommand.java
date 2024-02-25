package club.aurorapvp.aurorachat.commands;

import club.aurorapvp.aurorachat.AuroraChat;
import club.aurorapvp.aurorachat.modules.PlayerColorName;
import club.aurorapvp.aurorachat.util.StringUtil;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Syntax;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

@CommandAlias("namecolor")
@CommandPermission("aurorachat.command.namecolor")
public class NameColorCommand extends BaseCommand {

  @Default
  @CommandPermission("aurorachat.command.namecolor")
  @CommandCompletion("@colorNames")
  @Syntax("[color]")
  @Description("Changes the color of your name")
  @SuppressWarnings("unused")
  public void onColorChange(Player player, String colorName) {
    if (!NamedTextColor.NAMES.keys().contains(colorName)) {
      player.sendMessage(AuroraChat.getInstance().getLang().getComponent("unknown-color"));
      return;
    }

    PlayerColorName.getNameColor(player).setColor(colorName);

    player.sendMessage(AuroraChat.getInstance().getLang().formatComponent("name-color-changed",
        StringUtil.convertToNormalTypedLanguage(colorName)));
  }
}
