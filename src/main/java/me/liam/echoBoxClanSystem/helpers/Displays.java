package me.liam.echoBoxClanSystem.helpers;

import me.liam.echoBoxClanSystem.configs.Config;
import me.liam.echoBoxClanSystem.data.Clan;
import org.bukkit.ChatColor;

import java.util.UUID;

public class Displays {


    public static String getRankDisplay(Clan clan, UUID playerUUID) {
        if (clan.getOwner().equals(playerUUID)) {
            return Config.getOwnerPrefix();
        } else if (clan.getCoOwners().contains(playerUUID)) {
            return Config.getCoOwnerPrefix();
        } else if (clan.getAdmins().contains(playerUUID)) {
            return Config.getAdminPrefix();
        } else if (clan.getMembers().contains(playerUUID)) {
            return Config.getMemberPrefix();
        } else {
            return "7";
        }
    }

    public static String getNameColor(Clan clan, UUID playerUUID) {
        if (clan.getOwner().equals(playerUUID)) {
            return ChatColor.GOLD + "";
        } else if (clan.getCoOwners().contains(playerUUID)) {
            return ChatColor.LIGHT_PURPLE + "";
        } else if (clan.getAdmins().contains(playerUUID)) {
            return ChatColor.DARK_RED + "";
        } else if (clan.getMembers().contains(playerUUID)) {
            return ChatColor.GREEN + "";
        } else {
            return ChatColor.GRAY + "";
        }
    }
}
