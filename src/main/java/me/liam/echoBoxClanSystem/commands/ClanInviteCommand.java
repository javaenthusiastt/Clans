package me.liam.echoBoxClanSystem.commands;

import me.liam.echoBoxClanSystem.data.Clan;
import me.liam.echoBoxClanSystem.data.DataManager;
import me.liam.echoBoxClanSystem.helpers.Messages;
import me.liam.echoBoxClanSystem.helpers.Prefixes;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.util.Map;
import java.util.UUID;

public class ClanInviteCommand implements CommandExecutor {

    private final DataManager dataManager;

    public ClanInviteCommand(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public String getPlayerClanName(Player player) {
        UUID playerUUID = player.getUniqueId();
        return dataManager.getClans().entrySet().stream()
                .filter(entry -> entry.getValue().getMembers().contains(playerUUID))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(sender instanceof Player player)) {
            Messages.sendInvalidExecutor(sender);
            return true;
        }

        String playerClan = getPlayerClanName(player);

        if (playerClan == null) {
            Messages.sendClanNeeded(player);
            return true;
        }

        if (strings.length != 1) {
            Messages.sendTooLittleArguments(player);
            return true;
        }

        Clan clan = dataManager.getClans().get(playerClan);
        if (clan == null) {
            Messages.sendNoClanFound(player);
            return true;
        }

        if (!clan.getOwner().equals(player.getUniqueId())
                && !clan.getCoOwners().contains(player.getUniqueId())
                && !clan.getAdmins().contains(player.getUniqueId())) {
            Messages.sendHigherRank(player);
            return true;
        }

        String target = strings[0];

        Player targetPlayer = Bukkit.getPlayer(target);
        if (targetPlayer == null) {
            Messages.sendNoPlayerFound(player);
            return true;
        }

        UUID targetUUID = targetPlayer.getUniqueId();

        String targetClan = getPlayerClanName(targetPlayer);
        if (targetClan != null) {
            Messages.sendPlayerIsAlreadyInClan(player);
            return true;
        }

        if (targetUUID.equals(player.getUniqueId())) {
            Messages.sendMentionedYourselfCantUse(player);
            return true;
        }

        if (clan.getMembers().contains(targetUUID)) {
            Messages.sendAlreadyMember(player);
            return true;
        }

        if (clan.hasInvitation(targetUUID)) {
            Messages.sendPlayerIsAlreadyInvited(player);
            return true;
        }

        if (dataManager.getPlayerInvitations(targetUUID).size() >= Clan.MAX_CLAN_INVITES) {
            Messages.sendPlayerHasMaxedInvitations(player);
            return true;
        }

        if (clan.getMembers().size() >= Clan.MAX_CLAN_MEMBERS) {
            Messages.sendClanMaxed(player);
            return true;
        }

        if (clan.invitePlayer(targetUUID)) {
            Messages.sendPlayerInvitedToClan(player, targetPlayer.getName());
            Messages.sendYouWereInvited(targetPlayer, playerClan);
            Messages.sendYouWereInvitedHowToAccept(targetPlayer, playerClan);
            dataManager.saveClans();
        } else {
            Messages.sendError(player);
        }
        return true;
    }
}

