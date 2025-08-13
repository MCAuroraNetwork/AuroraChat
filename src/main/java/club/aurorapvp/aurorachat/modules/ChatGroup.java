package club.aurorapvp.aurorachat.modules;

import club.aurorapvp.aurorachat.AuroraChat;
import github.scarsz.discordsrv.api.events.GameChatMessagePreProcessEvent;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class ChatGroup {
  public static Map<UUID, ChatGroup> chatGroups = new HashMap<>();
  private final Set<Player> fixedAllowedPlayers = new HashSet<>();
  private final Set<Player> disallowedPlayers = new HashSet<>();
  private ChatTarget dynamicAllowedGroup = null;
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
    ChatGroup senderGroup = getChatGroup(sender);
    Set<Player> allowedRecipients = new HashSet<>();

    for (Player receiver : Bukkit.getOnlinePlayers()) {
      if (receiver.equals(sender)) {
        allowedRecipients.add(receiver);
        continue;
      }

      ChatGroup receiverGroup = getChatGroup(receiver);

      boolean receiverAllowsSender = false;
      if (receiverGroup.getMode() == ChatMode.ALL
          && !receiverGroup.getDisallowedPlayers().contains(sender)) {
        receiverAllowsSender = true;
      } else if (receiverGroup.getMode() != ChatMode.DISABLED
          && receiverGroup.getAllowedPlayers().contains(sender)
          && !receiverGroup.getDisallowedPlayers().contains(sender)) {
        receiverAllowsSender = true;
      }

      boolean senderAllowsReceiver = true;
      if (senderGroup.getMode() == ChatMode.SELECT) {
        senderAllowsReceiver = senderGroup.getAllowedPlayers().contains(receiver);
      }

      if (receiverAllowsSender && senderAllowsReceiver) {
        allowedRecipients.add(receiver);
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
    fixedAllowedPlayers.add(player);
  }

  public void addDisallowedPlayer(Player player) {
    disallowedPlayers.add(player);
  }

  public void removeAllowedPlayer(Player player) {
    fixedAllowedPlayers.remove(player);
  }

  public void removeDisallowedPlayer(Player player) {
    disallowedPlayers.remove(player);
  }

  public void addAllowedPlayers(Collection<Player> players) {
    fixedAllowedPlayers.addAll(players);
  }

  public void addDisallowedPlayers(Collection<Player> players) {
    disallowedPlayers.addAll(players);
  }

  public void removeAllowedPlayers(Collection<Player> players) {
    fixedAllowedPlayers.removeAll(players);
  }

  public void removeDisallowedPlayers(Collection<Player> players) {
    disallowedPlayers.removeAll(players);
  }

  public void setAllowedPlayers(Collection<Player> players) {
    fixedAllowedPlayers.clear();
    fixedAllowedPlayers.addAll(players);
  }

  public void setDisallowedPlayers(Collection<Player> players) {
    disallowedPlayers.clear();
    disallowedPlayers.addAll(players);
  }

  public Set<Player> getAllowedPlayers() {
    if (dynamicAllowedGroup != null) {
      return dynamicAllowedGroup.getChatMembers();
    } else {
      return fixedAllowedPlayers;
    }
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

  public ChatTarget getAllowedGroup() {
    return dynamicAllowedGroup;
  }

  public void setChatTarget(ChatTarget target) {
    this.dynamicAllowedGroup = target;
    this.mode = (target == null) ? ChatMode.ALL : ChatMode.SELECT;
  }

  public enum ChatMode {
    ALL,
    SELECT,
    DISABLED
  }

  public interface ChatTarget {
    Set<Player> getChatMembers();
  }
}
