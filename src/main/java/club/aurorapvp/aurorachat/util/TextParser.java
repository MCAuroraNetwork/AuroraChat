package club.aurorapvp.aurorachat.util;

import club.aurorapvp.aurorachat.AuroraChat;
import club.aurorapvp.aurorachat.modules.displayname.ChatFormatter;
import club.aurorapvp.aurorachat.modules.displayname.NameColor;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;

import java.util.List;

public class TextParser {

  public static Component parseWithPlaceholders(String input, Player player) {
    return parseWithPlaceholders(input, player, null);
  }

  public static Component parseWithPlaceholders(String input, Player player, List<TextColor> displayNameColors) {
    MiniMessage miniMessage = MiniMessage.builder()
            .tags(TagResolver.builder()
                    .resolvers(StandardTags.defaults(), placeholderTag(player, displayNameColors))
                    .build())
            .build();
    return miniMessage.deserialize(input);
  }

  public static TagResolver placeholderTag(final Player player, final List<TextColor> displayNameColors) {
    return TagResolver.resolver("placeholder", (argumentQueue, context) -> {
      final String placeholder = argumentQueue.popOr("placeholder tag requires an argument").value();
      switch (placeholder) {
        case "displayname":
          return Tag.selfClosingInserting(player.displayName());
        case "coloredname":
          Component coloredName;
          if (displayNameColors != null && !displayNameColors.isEmpty()) {
            coloredName = ComponentUtil.createGradient(player.getName(), displayNameColors);
          } else {
            coloredName = NameColor.getNameColor(player).getDisplayName();
          }
          return Tag.selfClosingInserting(coloredName);
        case "prefix":
          String prefix = ChatFormatter.chat.getPlayerPrefix(player);
          return Tag.selfClosingInserting(MiniMessage.miniMessage().deserialize(prefix));
        case "suffix":
          String suffix = ChatFormatter.chat.getPlayerSuffix(player);
          return Tag.selfClosingInserting(MiniMessage.miniMessage().deserialize(suffix));
        case "health":
          int health = (int) (player.getHealth() + player.getAbsorptionAmount());
          return Tag.selfClosingInserting(Component.text(health));
        case "ping":
          int ping = player.getPing();
          return Tag.selfClosingInserting(Component.text(ping));
        default:
          if (!AuroraChat.getInstance().isPlaceholderApiInstalled()) {
            return Tag.selfClosingInserting(Component.text(placeholder));
          }
          final String parsedPlaceholder = PlaceholderAPI.setPlaceholders(player, '%' + placeholder + '%');
          if (parsedPlaceholder.contains(LegacyComponentSerializer.SECTION_CHAR + "")) {
            Component componentPlaceholder = LegacyComponentSerializer.legacySection().deserialize(parsedPlaceholder);
            return Tag.selfClosingInserting(componentPlaceholder);
          }
          return Tag.selfClosingInserting(MiniMessage.miniMessage().deserialize(parsedPlaceholder));
      }
    });
  }
}