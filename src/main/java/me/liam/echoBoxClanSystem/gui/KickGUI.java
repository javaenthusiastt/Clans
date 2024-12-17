package me.liam.echoBoxClanSystem.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.UUID;

public class KickGUI {

    private final Plugin plugin;

    private static final int SIZE = 9;

    public KickGUI(Plugin plugin) {
        this.plugin = plugin;
    }

    public void openVerificationMenu(Player player, String targetPlayerName, UUID targetUUID) {
        Inventory verificationMenu = Bukkit.createInventory(null, SIZE, ChatColor.translateAlternateColorCodes('&', "&cConfirm Kick"));

        ItemStack confirmItem = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
        ItemMeta confirmMeta = confirmItem.getItemMeta();
        if (confirmMeta != null) {
            confirmMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a&lCONFIRM"));
            confirmMeta.setLore(List.of(ChatColor.translateAlternateColorCodes('&', " &7Player:&c " + targetPlayerName)));
            addGlow(confirmMeta);
            confirmMeta.getPersistentDataContainer().set(
                    new NamespacedKey(plugin, "targetUUID"),
                    PersistentDataType.STRING,
                    targetUUID.toString()
            );
            confirmItem.setItemMeta(confirmMeta);
        }

        ItemStack cancelItem = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta cancelMeta = cancelItem.getItemMeta();
        if (cancelMeta != null) {
            cancelMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&C&LCANCEL"));
            addGlow(cancelMeta);
            cancelItem.setItemMeta(cancelMeta);
        }

        verificationMenu.setItem(3, confirmItem);
        verificationMenu.setItem(5, cancelItem);

        player.openInventory(verificationMenu);
    }

    private void addGlow(ItemMeta meta) {
        meta.addEnchant(org.bukkit.enchantments.Enchantment.DURABILITY, 1, true);
        meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
    }
}
