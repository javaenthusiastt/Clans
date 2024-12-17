package me.liam.echoBoxClanSystem.ranks;

import me.liam.echoBoxClanSystem.data.Clan;
import me.liam.echoBoxClanSystem.data.DataManager;
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RankGUI {

    private final Plugin plugin;

    public RankGUI(Plugin plugin) {
        this.plugin = plugin;
    }

    public void openRankMenu(Player player, Clan clan, String targetPlayerName, UUID targetUUID) {
        int SIZE = 9;
        Inventory rankMenu = Bukkit.createInventory(null, SIZE, ChatColor.translateAlternateColorCodes('&', ChatColor.translateAlternateColorCodes('&', "&a&lCLAN RANKS")));

        ItemStack promoteItem = createRankItem(
                Material.GREEN_STAINED_GLASS_PANE,
                "&a&l✔ PROMOTE ✔",
                targetPlayerName,
                clan.getPlayerRank(targetUUID),
                "&7ᴄʟɪᴄᴋ ᴛᴏ ᴘʀᴏᴍᴏᴛᴇ",
                "&7ꜰʀᴏᴍ ᴛʜᴇɪʀ ᴄᴜʀʀᴇɴᴛ ʀᴀɴᴋ."
        );
        setItemUUID(promoteItem, targetUUID);

        ItemStack demoteItem = createRankItem(
                Material.RED_STAINED_GLASS_PANE,
                "&c&l✖ DEMOTE ✖",
                targetPlayerName,
                clan.getPlayerRank(targetUUID),
                "&7ᴄʟɪᴄᴋ ᴛᴏ ᴅᴇᴍᴏᴛᴇ",
                "&7ꜰʀᴏᴍ ᴛʜᴇɪʀ ᴄᴜʀʀᴇɴᴛ ʀᴀɴᴋ."
        );
        setItemUUID(demoteItem, targetUUID);

        rankMenu.setItem(3, promoteItem);
        rankMenu.setItem(5, demoteItem);

        player.openInventory(rankMenu);
    }

    private ItemStack createRankItem(Material material, String displayName, String playerName, String currentRank, String... loreLines) {
        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta != null) {
            itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));

            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.translateAlternateColorCodes('&', "&7&m-------------------"));
            lore.add("");
            lore.add(ChatColor.translateAlternateColorCodes('&', "&7ᴘʟᴀʏᴇʀ: &b" + playerName));
            lore.add(ChatColor.translateAlternateColorCodes('&', "&7ᴄᴜʀʀᴇɴᴛ ʀᴀɴᴋ: " + currentRank));
            lore.add("");
            for (String line : loreLines) {
                lore.add(ChatColor.translateAlternateColorCodes('&', line));
            }
            lore.add("");
            lore.add(ChatColor.translateAlternateColorCodes('&', "&7&m-------------------"));

            itemMeta.setLore(lore);
            addGlow(itemMeta);
            item.setItemMeta(itemMeta);
        }
        return item;
    }

    private void setItemUUID(ItemStack item, UUID uuid) {
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta != null) {
            itemMeta.getPersistentDataContainer().set(
                    new NamespacedKey(plugin, "targetUUID"),
                    PersistentDataType.STRING,
                    uuid.toString()
            );
            item.setItemMeta(itemMeta);
        }
    }

    private void addGlow(ItemMeta meta) {
        meta.addEnchant(org.bukkit.enchantments.Enchantment.DURABILITY, 1, true);
        meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
    }
}


