package me.liam.echoBoxClanSystem.gui;

import me.liam.echoBoxClanSystem.configs.Config;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class DecorationGUI {

    public static final ItemStack[] RAINBOW_GLASS = {
            createItem(Material.RED_STAINED_GLASS_PANE, "&c"+ Config.getBackgroundDecorationText()),
            createItem(Material.ORANGE_STAINED_GLASS_PANE, "&6"+Config.getBackgroundDecorationText()),
            createItem(Material.YELLOW_STAINED_GLASS_PANE, "&e"+Config.getBackgroundDecorationText()),
            createItem(Material.LIME_STAINED_GLASS_PANE, "&a"+Config.getBackgroundDecorationText()),
            createItem(Material.LIGHT_BLUE_STAINED_GLASS_PANE, "&b"+Config.getBackgroundDecorationText()),
            createItem(Material.BLUE_STAINED_GLASS_PANE, "&1"+Config.getBackgroundDecorationText()),
            createItem(Material.PURPLE_STAINED_GLASS_PANE, "&5"+Config.getBackgroundDecorationText()),
            createItem(Material.MAGENTA_STAINED_GLASS_PANE, "&d"+Config.getBackgroundDecorationText())
    };

    public static void filling(Inventory inventory) {
        ItemStack grayGlass = createItem(Material.GRAY_STAINED_GLASS_PANE, " ");
        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, grayGlass);
            }
        }
    }

    public static void greenfilling(Inventory inventory) {
        ItemStack grayGlass = createItem(Material.GREEN_STAINED_GLASS_PANE, " ");
        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, grayGlass);
            }
        }
    }

    public static void magentafilling(Inventory inventory) {
        ItemStack grayGlass = createItem(Material.MAGENTA_STAINED_GLASS_PANE, " ");
        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, grayGlass);
            }
        }
    }

    public static final int[] STYLED_FILLING = {
            0, 1, 2, 3, 5, 6, 7, 8,
            9, 17
    };

    public static final int[] DECORATION_SHINING_INTS = {
            0, 1, 2, 3, 4, 5, 6, 7, 8,
            9, 17, 18, 26, 27, 35,
            36, 44, 45, 46, 47, 48,
            49, 50, 51, 52, 53,
            10, 16, 37, 43
    };

    public static ItemStack createItem(Material material, String displayName, String... loreLines) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
            if (loreLines != null && loreLines.length > 0) {
                List<String> lore = new ArrayList<>();
                for (String line : loreLines) {
                    lore.add(ChatColor.translateAlternateColorCodes('&', line));
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
}
