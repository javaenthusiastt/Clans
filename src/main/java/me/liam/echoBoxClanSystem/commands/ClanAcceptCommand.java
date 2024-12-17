package me.liam.echoBoxClanSystem.commands;

import me.liam.echoBoxClanSystem.data.Clan;
import me.liam.echoBoxClanSystem.data.DataManager;
import me.liam.echoBoxClanSystem.helpers.Messages;
import me.liam.echoBoxClanSystem.helpers.Prefixes;
import me.liam.echoBoxClanSystem.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ClanAcceptCommand implements CommandExecutor {

    private final DataManager dataManager;

    public ClanAcceptCommand(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            Messages.sendInvalidExecutor(sender);
            return true;
        }

        UUID playerUUID = player.getUniqueId();

        if (args.length != 1) {
            Messages.sendTooLittleArguments(player);
            return true;
        }

        String clanNameInput = args[0].toLowerCase();
        Clan clan = findClanByName(clanNameInput);

        if (clan == null) {
            Messages.sendNoClanFound(player);
            return true;
        }

        if (!clan.hasInvitation(playerUUID)) {
            Messages.sendNoPendingInvites(player);
            return true;
        }

        String playerClan = getPlayerClanName(player);
        if (playerClan != null) {
            Messages.sendAlreadyInClan(player);
            return true;
        }

        if (clan.getMembers().size() >= Clan.MAX_CLAN_MEMBERS) {
            Messages.sendClanMaxed(player);
            return true;
        }

        boolean added = clan.addMember(playerUUID);
        if (added) {
            clan.removeInvitation(playerUUID);
            dataManager.saveClans();
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
            Messages.sendJoinedClan(player, clanNameInput);
        } else {
            Messages.sendError(player);
        }

        return true;
    }

    private Clan findClanByName(String clanName) {
        return dataManager.getClans().entrySet().stream()
                .filter(entry -> entry.getKey().toLowerCase().equals(clanName))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);
    }

    private String getPlayerClanName(Player player) {
        return dataManager.getClans().entrySet().stream()
                .filter(entry -> entry.getValue().getMembers().contains(player.getUniqueId()))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }
}

