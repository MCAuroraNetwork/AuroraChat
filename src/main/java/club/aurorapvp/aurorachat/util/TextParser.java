package club.aurorapvp.aurorachat.util;

import club.aurorapvp.aurorachat.AuroraChat;
import club.aurorapvp.aurorachat.modules.ChatFormatter;
import club.aurorapvp.aurorachat.modules.NameColor;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;

public class TextParser {

  public static Component parseWithPlaceholders(String input, Player player) {
    MiniMessage miniMessage =
        MiniMessage.builder()
            .tags(
                TagResolver.builder()
                    .resolvers(StandardTags.defaults(), placeholderTag(player))
                    .build())
            .build();
    return miniMessage.deserialize(input);
  }

  public static TagResolver placeholderTag(final Player player) {
    return TagResolver.resolver(
        "placeholder",
        (argumentQueue, context) -> {
          final String placeholder =
              argumentQueue.popOr("placeholder tag requires an argument").value();
          switch (placeholder) {
            case "displayname" -> {
              return Tag.selfClosingInserting(player.displayName());
            }
            case "coloredname" -> {
              return Tag.selfClosingInserting(NameColor.getNameColor(player).getDisplayName());
            }
            case "prefix" -> {
              String prefix = ChatFormatter.chat.getPlayerPrefix(player);

              return Tag.selfClosingInserting(MiniMessage.miniMessage().deserialize(prefix));
            }
            case "suffix" -> {
              String suffix = ChatFormatter.chat.getPlayerSuffix(player);

              return Tag.selfClosingInserting(MiniMessage.miniMessage().deserialize(suffix));
            }
            default -> {
              if (!AuroraChat.getInstance().isPlaceholderApiInstalled()) {
                return Tag.selfClosingInserting(Component.text(placeholder));
              }

              final String parsedPlaceholder =
                  PlaceholderAPI.setPlaceholders(player, '%' + placeholder + '%');

              if (parsedPlaceholder.contains(LegacyComponentSerializer.SECTION_CHAR + "")) {
                Component componentPlaceholder =
                    LegacyComponentSerializer.legacySection().deserialize(parsedPlaceholder);
                return Tag.selfClosingInserting(componentPlaceholder);
              }

              return Tag.selfClosingInserting(
                  MiniMessage.miniMessage().deserialize(parsedPlaceholder));
            }
          }
        });
  }
}
