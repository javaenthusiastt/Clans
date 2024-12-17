package me.liam.echoBoxClanSystem.clanchest;

import me.liam.echoBoxClanSystem.configs.Config;
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
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ClanChestCommand implements CommandExecutor {

    private final DataManager dataManager;
    private final Plugin plugin;
    private final Map<String, Inventory> clanChests = new HashMap<>();

    public ClanChestCommand(DataManager dataManager, Plugin plugin) {
        this.dataManager = dataManager;
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            return true;
        }

        if(!Config.isClanChestEnabled()){
            Messages.sendConfigDisabledFeature(player);
            return true;
        }

        String clanName = getPlayerClanName(player);
        if (clanName == null) {
            Messages.sendClanNeeded(player);
            return true;
        }

        Clan clan = dataManager.getClans().get(clanName);
        if (clan == null) {
            Messages.sendNoClanFound(player);
            return true;
        }

        openChest(player, clanName);
        return true;
    }

    private String getPlayerClanName(Player player) {
        UUID playerUUID = player.getUniqueId();
        return dataManager.getClans().entrySet().stream()
                .filter(entry -> entry.getValue().getMembers().contains(playerUUID))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }

    private void openChest(Player player, String clanName) {
        Inventory chest = clanChests.computeIfAbsent(clanName, k -> {
            Inventory savedInventory = loadClanChest(clanName);
            return savedInventory != null ? savedInventory : Bukkit.createInventory(null, 27, clanName + " Clan Chest");
        });
        player.openInventory(chest);
    }

    private Inventory loadClanChest(String clanName) {
        ItemStack[] items = dataManager.loadInventory(clanName);
        if (items != null) {
            Inventory chest = Bukkit.createInventory(null, 27, clanName + " Clan Chest");
            chest.setContents(items);
            return chest;
        }
        return null;
    }
}

