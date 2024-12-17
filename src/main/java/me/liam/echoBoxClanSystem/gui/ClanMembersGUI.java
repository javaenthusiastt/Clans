package me.liam.echoBoxClanSystem.gui;

import me.liam.echoBoxClanSystem.Main;
import me.liam.echoBoxClanSystem.colors.Adventure;
import me.liam.echoBoxClanSystem.configs.Config;
import me.liam.echoBoxClanSystem.data.Clan;
import me.liam.echoBoxClanSystem.helpers.Prefixes;
import me.liam.echoBoxClanSystem.helpers.Displays;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;

public class ClanMembersGUI {

    public void openClanMembersMenu(Player player, Clan clan) {
        int size = Math.min(((clan.getMembers().size() + clan.getAdmins().size() + clan.getCoOwners().size() + 8) / 9) * 9, 54);

        String configFetcher = Adventure.parseToLegacy(Objects.requireNonNull(Main.getInstance().getConfig().getString("clan-members-gui-name")));
        Inventory membersMenu = Bukkit.createInventory(null, size, configFetcher);

        UUID ownerUUID = clan.getOwner();
        Set<UUID> allMembers = new HashSet<>();

        if (ownerUUID != null) {
            allMembers.add(ownerUUID);
        }

        allMembers.addAll(clan.getCoOwners());
        allMembers.addAll(clan.getAdmins());
        allMembers.addAll(clan.getMembers());

        if (ownerUUID != null && allMembers.contains(ownerUUID)) {
            ItemStack headItem = createHeadItem(ownerUUID, clan);
            membersMenu.setItem(0, headItem);
            allMembers.remove(ownerUUID);
        }

        int slot = (ownerUUID != null) ? 1 : 0;
        for (UUID memberUUID : allMembers) {
            ItemStack headItem = createHeadItem(memberUUID, clan);
            membersMenu.setItem(slot++, headItem);
        }

        ItemStack glassPane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta glassMeta = glassPane.getItemMeta();
        if (glassMeta != null) {
            glassMeta.setDisplayName(" ");
            glassPane.setItemMeta(glassMeta);
        }

        for (int i = slot; i < size; i++) {
            membersMenu.setItem(i, glassPane);
        }

        player.openInventory(membersMenu);
    }

    private ItemStack createHeadItem(UUID playerUUID, Clan clan) {
        ItemStack headItem = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta meta = headItem.getItemMeta();

        if (meta instanceof SkullMeta skullMeta) {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerUUID);
            String playerName = offlinePlayer.getName();

            if (playerName == null || playerName.isEmpty()) {
                skullMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7Unknown Player &8(&7Member&8)"));
                List<String> lore = new ArrayList<>();
                lore.add("");
                lore.add(Adventure.parseToLegacy(Main.getInstance().getConfig().getString("KICK_PLAYER")));
                lore.add(ChatColor.translateAlternateColorCodes('&', Adventure.parseToLegacy(Config.getStringSTATUS() + Config.getStringOFFLINE())));
                skullMeta.setLore(lore);
            } else {
                skullMeta.setOwningPlayer(offlinePlayer);

                String rankDisplay = Displays.getRankDisplay(clan, playerUUID);

                if (rankDisplay.isEmpty()) {
                    rankDisplay = "7";
                }

                skullMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7" + playerName + " &8(" + rankDisplay + "&8)"));

                List<String> lore = new ArrayList<>();
                if (!playerUUID.equals(clan.getOwner())) {
                    lore.add("");
                    lore.add(Adventure.parseToLegacy(Main.getInstance().getConfig().getString("gui.member-gui-messages.PROMOTE_DEMOTE", "NULL")));
                    lore.add(Adventure.parseToLegacy(Main.getInstance().getConfig().getString("gui.member-gui-messages.KICK_PLAYER", "NULL")));
                }

                lore.add("");

                if (offlinePlayer.isOnline()) {
                    lore.add(ChatColor.translateAlternateColorCodes('&', Adventure.parseToLegacy(Config.getStringSTATUS() + Config.getStringONLINE())));
                } else {
                    long lastPlayed = offlinePlayer.getLastPlayed();
                    if (lastPlayed > 0) {
                        long timeSinceLastOnline = System.currentTimeMillis() - lastPlayed;

                        int days = (int) (timeSinceLastOnline / (1000 * 60 * 60 * 24));
                        int hours = (int) ((timeSinceLastOnline / (1000 * 60 * 60)) % 24);
                        int minutes = (int) ((timeSinceLastOnline / (1000 * 60)) % 60);

                        lore.add(ChatColor.translateAlternateColorCodes('&', Adventure.parseToLegacy(Config.getStringSTATUS() + Config.getStringOFFLINE())));
                        lore.add(ChatColor.translateAlternateColorCodes('&', Adventure.parseToLegacy(Config.getStringLAST_ONLINE(days, hours, minutes))));
                    } else {
                        lore.add(ChatColor.translateAlternateColorCodes('&', Adventure.parseToLegacy(Config.getStringSTATUS() + Config.getStringOFFLINE())));
                        lore.add(ChatColor.translateAlternateColorCodes('&', Adventure.parseToLegacy(Config.getStringLAST_ONLINE(null, null, null) + Config.getStringNODATA())));
                    }
                }
                lore.add(Adventure.parseToLegacy(Objects.requireNonNull(Main.getInstance().getConfig().getString("gui.member-gui-messages.POINTS_EARNED")).replace("{player_points}", clan.getPlayerPointsAsString(offlinePlayer.getUniqueId()))));
                skullMeta.setLore(lore);
            }
            headItem.setItemMeta(skullMeta);
        }
        return headItem;
    }
}
