package club.aurorapvp.aurorachat.commands;

import club.aurorapvp.aurorachat.AuroraChat;
import club.aurorapvp.aurorachat.modules.NameColor;
import club.aurorapvp.aurorachat.modules.NameColor.GradientBuilder;
import club.aurorapvp.aurorachat.util.StringUtil;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Name;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import java.util.Objects;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
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
    NameColor color = NameColor.getNameColor(player);

    if (Objects.equals(colorName, "done") && color.isBuildingGradient()) {
      color.setGradient(color.builder.build());

      player.sendMessage(AuroraChat.getInstance().getLang().getComponent("gradient-set"));
      return;
    }
    
    if (!NamedTextColor.NAMES.keys().contains(colorName)) {
      player.sendMessage(AuroraChat.getInstance().getLang().getComponent("unknown-color"));
      return;
    }

    if (color.isBuildingGradient()) {
      color.builder.addColor(NamedTextColor.NAMES.value(colorName));

      player.sendMessage(AuroraChat.getInstance().getLang().getComponent("color-added"));
      return;
    }

    color.setColor(colorName);

    player.sendMessage(AuroraChat.getInstance().getLang().formatComponent("name-color-changed",
        StringUtil.convertToNormalTypedLanguage(colorName)));
  }


  @Subcommand("gradient")
  @CommandPermission("aurorachat.command.namecolor.gradient")
  @CommandCompletion("@colorNames")
  @Syntax("[color]")
  @Description("Changes the color of your name")
  @SuppressWarnings("unused")
  public void onSetGradient(Player player) {
    NameColor.getNameColor(player).new GradientBuilder();

    player.sendMessage(AuroraChat.getInstance().getLang().formatComponent("set-gradient"));
  }
}
