package club.aurorapvp.aurorachat.modules;

import club.aurorapvp.aurorachat.AuroraChat;
import github.scarsz.discordsrv.api.events.GameChatMessagePreProcessEvent;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.entity.Player;

import java.util.*;

public class ChatGroup {
  public static Map<UUID, ChatGroup> chatGroups = new HashMap<>();
  private final Set<Player> allowedPlayers = new HashSet<>();
  private final Set<Player> disallowedPlayers = new HashSet<>();
  private final Player player;
  private ChatMode mode;

  public ChatGroup(Player player) {
    this.player = player;
    this.mode = ChatMode.ALL;
  }

  public static void checkDisabled(AsyncChatEvent event) {
    ChatGroup chatGroup = chatGroups.get(event.getPlayer().getUniqueId());

    if (chatGroup == null || chatGroup.getMode() != ChatMode.DISABLED) {
      return;
    }

    event.getPlayer().sendMessage(AuroraChat.getInstance().getLang().getComponent("chat-disabled"));

    event.setCancelled(true);
  }

  public static void onDiscordChat(GameChatMessagePreProcessEvent event) {
    ChatGroup chatGroup = ChatGroup.getChatGroup(event.getPlayer());

    if (chatGroup.getMode() != ChatMode.ALL) {
      event.setCancelled(true);
    }
  }

  public static void onChat(AsyncChatEvent event) {
    Player sender = event.getPlayer();

    Set<Player> allowedRecipients = new HashSet<>();

    ChatGroup chatGroup = chatGroups.get(sender.getUniqueId());

    if (chatGroup != null && chatGroup.getMode() == ChatMode.SELECT) {
      event.viewers().retainAll(chatGroup.getAllowedPlayers());

      return;
    }

    for (ChatGroup group : chatGroups.values()) {
      if (group.getPlayer().equals(sender)) {
        allowedRecipients.add(group.getPlayer());

        continue;
      }

      if (group.getMode() == ChatMode.ALL && !group.getDisallowedPlayers().contains(sender)) {
        allowedRecipients.add(group.getPlayer());
      } else if (group.getMode() != ChatMode.DISABLED
          && group.getAllowedPlayers().contains(sender)
          && !group.getDisallowedPlayers().contains(sender)) {
        allowedRecipients.add(group.getPlayer());
      }
    }

    event.viewers().retainAll(allowedRecipients);
  }

  public static ChatGroup getChatGroup(Player player) {
    return chatGroups.computeIfAbsent(player.getUniqueId(), k -> new ChatGroup(player));
  }

  public Player getPlayer() {
    return player;
  }

  public void addAllowedPlayer(Player player) {
    allowedPlayers.add(player);
  }

  public void addDisallowedPlayer(Player player) {
    disallowedPlayers.add(player);
  }

  public void removeAllowedPlayer(Player player) {
    allowedPlayers.remove(player);
  }

  public void removeDisallowedPlayer(Player player) {
    disallowedPlayers.remove(player);
  }

  public void addAllowedPlayers(Collection<Player> players) {
    allowedPlayers.addAll(players);
  }

  public void addDisallowedPlayers(Collection<Player> players) {
    disallowedPlayers.addAll(players);
  }

  public void removeAllowedPlayers(Collection<Player> players) {
    allowedPlayers.removeAll(players);
  }

  public void removeDisallowedPlayers(Collection<Player> players) {
    disallowedPlayers.removeAll(players);
  }

  public void setAllowedPlayers(Collection<Player> players) {
    allowedPlayers.clear();

    allowedPlayers.addAll(players);
  }

  public void setDisallowedPlayers(Collection<Player> players) {
    disallowedPlayers.clear();

    disallowedPlayers.addAll(players);
  }

  public Set<Player> getAllowedPlayers() {
    return allowedPlayers;
  }

  public Set<Player> getDisallowedPlayers() {
    return disallowedPlayers;
  }

  public void setMode(ChatMode mode) {
    this.mode = mode;
  }

  public ChatMode getMode() {
    return mode;
  }

  public enum ChatMode {
    ALL,
    SELECT,
    DISABLED
  }
}
