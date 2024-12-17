package me.liam.echoBoxClanSystem.ranks;
import me.liam.echoBoxClanSystem.configs.Config;
import me.liam.echoBoxClanSystem.data.Clan;
import me.liam.echoBoxClanSystem.data.DataManager;
import me.liam.echoBoxClanSystem.helpers.Messages;
import me.liam.echoBoxClanSystem.helpers.Prefixes;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import java.util.Map;
import java.util.UUID;

public class RankListener implements Listener {

    private final DataManager dataManager;
    private final Plugin plugin;

    public RankListener(DataManager dataManager) {
        this.dataManager = dataManager;
        this.plugin = dataManager.getPlugin();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;

        }
        if (!event.getView().getTitle().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', "&a&lCLAN RANKS"))) {
            return;
        }

        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem == null || clickedItem.getItemMeta() == null) {
            return;
        }

        event.setCancelled(true);

        ItemMeta itemMeta = clickedItem.getItemMeta();
        PersistentDataContainer dataContainer = itemMeta.getPersistentDataContainer();
        String targetUUIDString = dataContainer.get(new NamespacedKey(plugin, "targetUUID"), PersistentDataType.STRING);
        if (targetUUIDString == null) {
            return;
        }

        UUID targetUUID;

        try {
            targetUUID = UUID.fromString(targetUUIDString);
        } catch (IllegalArgumentException e) {
            Messages.sendError(player);
            return;
        }

        Player targetPlayer = Bukkit.getPlayer(targetUUID);
        if (targetPlayer == null) {
            player.closeInventory();
            Messages.sendNoPlayerFound(player);
            return;
        }

        Clan clan = dataManager.getClans().get(getPlayerClanName(targetPlayer));

        if (clan == null) {
            Messages.sendNoClanFound(player);
            return;
        }

        if (clickedItem.getType() == Material.GREEN_STAINED_GLASS_PANE) {
            if (promotePlayer(clan, targetUUID)) {
                player.closeInventory();
                String newRank = clan.getPlayerRank(targetUUID);

                if(Config.isBroadcastEnabled()){
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                            Prefixes.getPrefix() + "&a"+targetPlayer.getName()+" &7got &apromoted &7in their clan."));
                }

                targetPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        Prefixes.getPrefix() + "&7You have been &apromoted&7 to &a" + newRank + "&7 in your clan."));
                dataManager.saveClans();
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        Prefixes.getPrefix() + "&7The player is already at the &ahighest&7 rank or max limit reached."));
                player.closeInventory();
            }
        } else if (clickedItem.getType() == Material.RED_STAINED_GLASS_PANE) {
            if (demotePlayer(clan, targetUUID)) {
                player.closeInventory();
                String newRank = clan.getPlayerRank(targetUUID);

                if(Config.isBroadcastEnabled()){
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                            Prefixes.getPrefix() + "&a"+targetPlayer.getName()+"&7 got &cdemoted &7in their clan."));
                }

                targetPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        Prefixes.getPrefix() + "&7You have been &cdemoted&7 to &c" + newRank + "&7 in your clan."));
                dataManager.saveClans();
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        Prefixes.getPrefix() + "&7The player is already at the &clowest&7 rank."));
                player.closeInventory();
            }
        }
    }

    private boolean promotePlayer(Clan clan, UUID playerUUID) {
        if (clan.isMember(playerUUID)) {
            if (clan.isCoOwner(playerUUID)) {
                return false;
            } else if (clan.isAdmin(playerUUID)) {
                if (!clan.getCoOwners().isEmpty()) {
                    return false;
                }
                clan.addCoOwner(playerUUID);
                clan.removeAdmin(playerUUID);
                return true;
            } else {
                if (clan.getAdmins().size() >= Clan.MAX_ADMIN_COUNT) {
                    return false;
                }
                clan.addAdmin(playerUUID);
                return true;
            }
        }
        return false;
    }

    private boolean demotePlayer(Clan clan, UUID playerUUID) {
        if (clan.isCoOwner(playerUUID)) {
            clan.removeCoOwner(playerUUID);
            clan.addAdmin(playerUUID);
            return true;
        } else if (clan.isAdmin(playerUUID)) {
            clan.removeAdmin(playerUUID);
            clan.addMember(playerUUID);
            return true;
        }
        return false;
    }

    private String getPlayerClanName(Player player) {
        return dataManager.getClans().entrySet().stream()
                .filter(entry -> entry.getValue().getMembers().contains(player.getUniqueId()))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }
}


