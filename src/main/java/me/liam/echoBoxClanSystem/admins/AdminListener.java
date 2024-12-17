package me.liam.echoBoxClanSystem.admins;
import me.liam.echoBoxClanSystem.configs.Config;
import me.liam.echoBoxClanSystem.configs.ReloadConfigClass;
import me.liam.echoBoxClanSystem.helpers.Prefixes;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class AdminListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || !clickedItem.hasItemMeta()) {
            return;
        }

        String inventoryTitle = event.getView().getTitle();
        String titleString = Config.getAdminGuiName();
        MiniMessage miniMessage = MiniMessage.miniMessage();
        Component expectedTitleComponent = miniMessage.deserialize(titleString);
        String expectedTitle = LegacyComponentSerializer.legacySection().serialize(expectedTitleComponent);

        if (!inventoryTitle.equalsIgnoreCase(expectedTitle)) {
            return;
        }

        event.setCancelled(true);

        if (clickedItem.getType().equals(Material.NETHER_STAR)) {
            ReloadConfigClass.reload(player, Prefixes.getPrefix());
            player.closeInventory();
        }
    }
}
