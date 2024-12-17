package me.liam.echoBoxClanSystem.clanchest;

import me.liam.echoBoxClanSystem.data.DataManager;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


public class ClanChestListener implements Listener {

    private final DataManager dataManager;

    public ClanChestListener(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();
        String inventoryTitle = event.getView().getTitle();
        if (inventoryTitle.endsWith(" Clan Chest")) {
            String clanName = inventoryTitle.replace(" Clan Chest", "");
            dataManager.saveInventory(clanName, inventory.getContents());
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        String inventoryTitle = event.getView().getTitle();
        if(inventoryTitle.endsWith(" Clan Chest")) {
            Player player = (Player) event.getWhoClicked();
            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem != null) {
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
            }
        }
    }
}
