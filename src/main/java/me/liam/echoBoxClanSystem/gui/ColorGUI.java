package me.liam.echoBoxClanSystem.gui;
import me.liam.echoBoxClanSystem.data.Clan;
import me.liam.echoBoxClanSystem.data.DataManager;
import me.liam.echoBoxClanSystem.helpers.Messages;
import me.liam.echoBoxClanSystem.helpers.Prefixes;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import java.util.*;

public class ColorGUI implements Listener {

    private final DataManager dataManager;
    private final Plugin plugin;
    private final Map<Material, ChatColor> dyeToChatColor = new HashMap<>();
    private final int colorCost = 5000;

    public ColorGUI(DataManager dataManager, Plugin plugin) {
        this.dataManager = dataManager;
        this.plugin = plugin;

        Bukkit.getPluginManager().registerEvents(this, plugin);

        dyeToChatColor.put(Material.RED_DYE, ChatColor.RED);
        dyeToChatColor.put(Material.BLUE_DYE, ChatColor.BLUE);
        dyeToChatColor.put(Material.GREEN_DYE, ChatColor.GREEN);
        dyeToChatColor.put(Material.YELLOW_DYE, ChatColor.YELLOW);
        dyeToChatColor.put(Material.BLACK_DYE, ChatColor.BLACK);
        dyeToChatColor.put(Material.WHITE_DYE, ChatColor.WHITE);
        dyeToChatColor.put(Material.PURPLE_DYE, ChatColor.LIGHT_PURPLE);
        dyeToChatColor.put(Material.ORANGE_DYE, ChatColor.GOLD);
        dyeToChatColor.put(Material.LIGHT_BLUE_DYE, ChatColor.AQUA);
    }

    public void openColorSelectionGUI(Player player) {
        Inventory colorGUI = Bukkit.createInventory(null, 27, ChatColor.GREEN + "Select Clan Color");

        List<Map.Entry<String, Clan>> sortedClans = new ArrayList<>(dataManager.getClans().entrySet());
        sortedClans.sort((a, b) -> Integer.compare(b.getValue().getPoints(), a.getValue().getPoints()));

        Clan playerClan = dataManager.getClans().get(dataManager.getPlayerClanName(player));
        int clanRank = getClanRank(playerClan, sortedClans);

        int[] middleRowSlots = {9, 10, 11, 12, 13, 14, 15, 16, 17};
        int slotIndex = 0;

        for (Material dye : dyeToChatColor.keySet()) {
            if (slotIndex >= middleRowSlots.length) break;

            ItemStack dyeItem = new ItemStack(dye);
            ItemMeta meta = dyeItem.getItemMeta();
            if (meta != null) {
                ChatColor color = dyeToChatColor.get(dye);
                String colorName = color.name();
                boolean isRestricted = isRestrictedColor(color, clanRank);

                meta.setDisplayName(color + "" + ChatColor.BOLD + "Color: " + colorName);
                List<String> lore = new ArrayList<>();
                lore.add(ChatColor.GRAY + "Cost: " + ChatColor.GOLD + colorCost + " Points");
                if (isRestricted) {
                    lore.add(ChatColor.RED + "Restricted to Top 3 Clans");
                } else {
                    lore.add(ChatColor.GREEN + "Available to all clans");
                }
                meta.setLore(lore);
                dyeItem.setItemMeta(meta);
            }

            colorGUI.setItem(middleRowSlots[slotIndex], dyeItem);
            slotIndex++;
        }
        fillEmptySlotsWithDarkGlass(colorGUI);
        player.openInventory(colorGUI);
    }

    private void fillEmptySlotsWithDarkGlass(Inventory inventory) {
        for (int i = 0; i < 27; i++) {
            if (inventory.getItem(i) == null || Objects.requireNonNull(inventory.getItem(i)).getType() == Material.AIR) {
                inventory.setItem(i, createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
            }
        }
    }

    private boolean isRestrictedColor(ChatColor color, int clanRank) {
        if (color == ChatColor.RED && clanRank != 1) return true;
        if (color == ChatColor.YELLOW && clanRank != 2) return true;
        if (color == ChatColor.GREEN && clanRank != 3) return true;
        return false;
    }

    private int getClanRank(Clan playerClan, List<Map.Entry<String, Clan>> sortedClans) {
        for (int i = 0; i < sortedClans.size(); i++) {
            if (sortedClans.get(i).getValue().equals(playerClan)) {
                return i + 1;
            }
        }
        return -1;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player))
            return;

        Player player = (Player) event.getWhoClicked();
        Inventory inventory = event.getClickedInventory();

        if (inventory != null && event.getView().getTitle().equals(ChatColor.GREEN + "Select Clan Color")) {
            event.setCancelled(true);

            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem != null && dyeToChatColor.containsKey(clickedItem.getType())) {
                ChatColor selectedColor = dyeToChatColor.get(clickedItem.getType());
                Clan clan = dataManager.getClans().get(dataManager.getPlayerClanName(player));

                if (clan != null) {
                    int clanRank = getClanRank(clan, new ArrayList<>(dataManager.getClans().entrySet()));
                    boolean isRestricted = isRestrictedColor(selectedColor, clanRank);

                    if (isRestricted) {
                        int requiredRank = getRequiredRankForColor(selectedColor);
                        Messages.sendClanColorFailedNOTHIGHRANKED(player, String.valueOf(requiredRank));
                        player.closeInventory();
                        return;
                    }

                    if (clan.getPoints() >= colorCost) {
                        clan.setPoints(clan.getPoints() - colorCost);
                        clan.setColor(selectedColor);
                        dataManager.saveClans();

                        Messages.sendClanColorSet(player, selectedColor, selectedColor.name());
                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() +
                                "&cYour clan doesn't have enough points to buy this color."));
                    }
                }
                player.closeInventory();
            }
        }
    }

    private int getRequiredRankForColor(ChatColor color) {
        if (color == ChatColor.RED) return 1;
        if (color == ChatColor.YELLOW) return 2;
        if (color == ChatColor.GREEN) return 3;
        return -1;
    }

    private ItemStack createItem(Material material, String displayName, String... loreLines) {
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
            item.setItemMeta(meta);
        }
        return item;
    }
}
