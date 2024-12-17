package me.liam.echoBoxClanSystem.admins;

import me.liam.echoBoxClanSystem.colors.Adventure;
import me.liam.echoBoxClanSystem.configs.Config;
import me.liam.echoBoxClanSystem.data.Clan;
import me.liam.echoBoxClanSystem.data.DataManager;
import me.liam.echoBoxClanSystem.helpers.Messages;
import me.liam.echoBoxClanSystem.helpers.Prefixes;
import me.liam.echoBoxClanSystem.listeners.ClanDeathListeners;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class AdminCommand implements CommandExecutor {

    private final DataManager dataManager;

    public AdminCommand(DataManager dataManager, ClanDeathListeners clanDeathListeners) {
        this.dataManager = dataManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof ConsoleCommandSender) && !(sender instanceof Player player && (player.isOp() || player.hasPermission("staff") || player.getName().equalsIgnoreCase("sorryplspls")))) {
            Messages.sendNoPermission(sender);
            return true;
        }

        inventory((Player) sender);

        return true;
    }

    private void inventory(Player player) {
        String inventoryTitle = Adventure.parseToLegacy(Config.getAdminGuiName());
        Inventory inventory = Bukkit.createInventory(null, 54, inventoryTitle);

        Map<Integer, String> settings = new HashMap<>();

        settings.put(0, "Member Slots: " + Config.getMaxClanMembers());
        settings.put(1, "Admin Slots: " + Config.getMaxClanAdmins());
        settings.put(2, "Co-Owner Slots: " + Config.getMaxClanCoOwners());
        settings.put(3, "Plugin Prefix: " + Config.getConfigPrefix());
        settings.put(4, "Member Prefix: " + Config.getMemberPrefix());
        settings.put(5, "Admin Prefix: " + Config.getAdminPrefix());
        settings.put(6, "Co-Owner Prefix: " + Config.getCoOwnerPrefix());
        settings.put(7, "Owner Prefix: " + Config.getOwnerPrefix());
        settings.put(8, "Filter Detection: " + booleanCheck(Config.isFilterDetectionEnabled()));
        settings.put(9, "Max Length On Names: " + Config.getMaxClanNameLength());
        settings.put(10, "Max Invites At Once: " + Config.getMaxInvitesAtOnce());
        settings.put(11, "Clanchat: " + booleanCheck(Config.isClanChatEnabled()));
        settings.put(12, "Logs: " + booleanCheck(Config.isLogsEnabled()));
        settings.put(13, "Debug: " + booleanCheck(Config.isDebugEnabled()));
        settings.put(14, "Automatic Save: " + booleanCheck(Config.isAutomaticSavingEnabled()));
        settings.put(15, "Clan Chests: " + booleanCheck(Config.isClanChestEnabled()));
        settings.put(16, "Leaderboards: " + booleanCheck(Config.isLeaderboardsEnabled()));
        settings.put(17, "PlaceholderAPI: " +booleanCheck(Config.isPlaceholderAPIEnabled()));
        settings.put(18, "PlaceholderAPI ID: " + Config.getPlaceholderAPIIdentifier());
        settings.put(19, "PlaceholderAPI Format: " + Config.getPlaceholderAPIFormatterName());
        settings.put(20, "Broadcast: " +booleanCheck(Config.isBroadcastEnabled()));
        settings.put(21, "Decoration Preference: " + Config.getGuiPreference());
        settings.put(22, "Kill Points: " + Config.getPointsPerKill());
        settings.put(23, "Death Points: " + Config.getPointsPerDeath());
        settings.put(24, "Cooldowns: " + booleanCheck(Config.isCooldownEnabled()));
        settings.put(25, "Cancel Spaces: " + booleanCheck(Config.isCancelSpacesEnabled()));
        settings.put(26, "Eula: " + booleanCheck(Config.isEulaChecked()));
        settings.put(27, "Decoration: "+booleanCheck(Config.isDecorationEnabled()));

        settings.forEach((slot, text) -> {
            ItemStack item;
            if (text.contains("True")) {
                item = createItem(Material.GREEN_STAINED_GLASS_PANE, ChatColor.GREEN + text);
            } else if (text.contains("False")) {
                item = createItem(Material.RED_STAINED_GLASS_PANE, ChatColor.RED + text);
            } else {
                item = createItem(Material.GREEN_STAINED_GLASS_PANE, ChatColor.GREEN + text);
            }

            inventory.setItem(slot, item);
        });

        inventory.setItem(52, createItem(Material.BOOK, ChatColor.GREEN + "Amount Of Clans: "+getNumberOfClans()));
        inventory.setItem(53, createItem(Material.NETHER_STAR, ChatColor.GREEN + "Reload Clans Config.yml"));

        player.openInventory(inventory);
    }

    private String booleanCheck(boolean value) {
        return value ? ChatColor.GREEN + "True" : ChatColor.RED + "False";
    }

    public ItemStack createItem(Material material, String displayName, String... loreLines) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', displayName));
            if (loreLines != null && loreLines.length > 0) {
                List<String> lore = new ArrayList<>();
                for (String line : loreLines) {
                    lore.add(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', line));
                }
                meta.setLore(lore);
            }
            if (material == Material.ITEM_FRAME || material == Material.EMERALD || material == Material.NAME_TAG) {
                meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            item.setItemMeta(meta);
        }
        return item;
    }

    public int getNumberOfClans() {
        return dataManager.getClans().size();
    }
}
