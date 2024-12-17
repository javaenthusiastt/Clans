package me.liam.echoBoxClanSystem.commands.clanchat;

import me.liam.echoBoxClanSystem.Main;
import me.liam.echoBoxClanSystem.configs.Config;
import me.liam.echoBoxClanSystem.data.Clan;
import me.liam.echoBoxClanSystem.data.DataManager;
import me.liam.echoBoxClanSystem.helpers.Messages;
import me.liam.echoBoxClanSystem.helpers.Displays;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ClanChatCommand implements CommandExecutor, Listener

{

    private final DataManager dataManager;
    public final Map<UUID, Boolean> clanChatToggled = new HashMap<>();

    public ClanChatCommand(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            Messages.sendInvalidExecutor(sender);
            return true;
        }

        if(!Config.isClanChatEnabled()){
            Messages.sendConfigDisabledFeature(player);
            return true;
        }

        UUID playerUUID = player.getUniqueId();
        String clanName = getPlayerClanName(player);

        if (clanName == null) {
            Messages.sendClanNeeded(player);
            return true;
        }

        if (args.length == 0) {
            if (clanChatToggled.getOrDefault(playerUUID, false)) {
                clanChatToggled.put(playerUUID, false);
                Messages.sendClanChatDisabled(player);
            } else {
                clanChatToggled.put(playerUUID, true);
                Messages.sendClanChatEnabled(player);
            }
            return true;
        }

        String message = String.join(" ", args);
        Clan clan = dataManager.getClans().get(clanName);
        if (clan != null) {
            sendClanMessage(clan, player, message, clanName);
        } else {
            Messages.sendError(player);
        }
        return true;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        if (isInClanChatMode(playerUUID)) {
            event.setCancelled(true);

            String clanName = getPlayerClanName(player);
            if (clanName != null) {
                Clan clan = dataManager.getClans().get(clanName);
                if (clan != null) {
                    sendClanMessage(clan, player, event.getMessage(), clanName);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerQuitInClanChat(PlayerQuitEvent event) {
        UUID playerUUID = event.getPlayer().getUniqueId();

        if (isInClanChatMode(playerUUID)) {
            clanChatToggled.put(playerUUID, false);
        }
    }

    public boolean isInClanChatMode(UUID playerUUID) {
        return clanChatToggled.getOrDefault(playerUUID, false);
    }

    public String getPlayerClanName(Player player) {
        return dataManager.getClans().entrySet().stream()
                .filter(entry -> entry.getValue().getMembers().contains(player.getUniqueId()))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }

    public void sendClanMessage(Clan clan, Player sender, String message, String clanName) {
        try {
            String clanchatFormat = Main.getInstance().getConfig().getString("clanchat-format");

            if (clanchatFormat == null) {
                Main.getInstance().getLogger().severe("Clan-Chat format is missing in the config.yml file...");
                return;
            }

            UUID senderUUID = sender.getUniqueId();
            String rankDisplay = Displays.getRankDisplay(clan, senderUUID);
            String nameColor = Displays.getNameColor(clan, senderUUID);
            String clanRank = getClanRank(clanName);

            String clanColor = clan.getColor() != null ? clan.getColor().toString() : "&f";
            String formattedClanName = clanName.substring(0, 1).toUpperCase() + clanName.substring(1).toLowerCase();
            String formattedMessage = ChatColor.translateAlternateColorCodes('&',
                    clanchatFormat
                            .replace("{c}", clanColor)
                            .replace("{clanname}", formattedClanName)
                            .replace("{clanrank}", clanRank)
                            .replace("{leaderboard_place}", rankDisplay)
                            .replace("{color}", nameColor)
                            .replace("{player}", sender.getName())
                            .replace("{message}", message)
            );

            for (UUID memberUUID : clan.getMembers()) {
                Player member = Main.getInstance().getServer().getPlayer(memberUUID);
                if (member != null) {
                    member.sendMessage(formattedMessage);
                }
            }
        } catch (Exception e) {
            Main.getInstance().getLogger().severe("Error sending clan message: " + e.getMessage());
        }
    }

    private String getClanRank(String clanName) {
        List<Map.Entry<String, Clan>> sortedClans = new ArrayList<>(dataManager.getClans().entrySet());

        sortedClans.sort((a, b) -> {
            Clan clanA = a.getValue();
            Clan clanB = b.getValue();

            if (clanA == null && clanB == null) return 0;
            if (clanA == null) return 1;
            if (clanB == null) return -1;

            return Integer.compare(clanB.getPoints(), clanA.getPoints());
        });

        String[] rankColors = { ChatColor.RED + "", ChatColor.GOLD + "", ChatColor.LIGHT_PURPLE + "" };

        for (int i = 0; i < sortedClans.size(); i++) {
            if (sortedClans.get(i).getKey().equals(clanName)) {
                if (i < 3) {
                    return " " + rankColors[i] + "(#" + (i + 1) + ")";
                }
            }
        }
        return "";
    }


}
