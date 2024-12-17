package me.liam.echoBoxClanSystem.listeners;

import me.liam.echoBoxClanSystem.colors.Adventure;
import me.liam.echoBoxClanSystem.data.Clan;
import me.liam.echoBoxClanSystem.data.DataManager;
import me.liam.echoBoxClanSystem.gui.KickGUI;
import me.liam.echoBoxClanSystem.helpers.Messages;
import me.liam.echoBoxClanSystem.ranks.RankGUI;
import me.liam.echoBoxClanSystem.handling.CooldownManager;
import me.liam.echoBoxClanSystem.helpers.Prefixes;
import me.liam.echoBoxClanSystem.Main;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class ClanMembersListenerGUI implements Listener {

    private final DataManager dataManager;
    private final Plugin plugin;

    public CooldownManager cooldownManager = Main.getInstance().getCooldownManager();

    private RankGUI rankGUI;

    public ClanMembersListenerGUI(DataManager dataManager, Main main) {
        this.dataManager = dataManager;
        this.plugin = dataManager.getPlugin();
        this.rankGUI = new RankGUI(plugin);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        String configFetcher = Adventure.parseToLegacy(Objects.requireNonNull(Main.getInstance().getConfig().getString("clan-members-gui-name")));

        if (event.getView().getType() == InventoryType.CHEST) {
            if (event.getView().getTitle().equalsIgnoreCase(configFetcher)) {
                event.setCancelled(true);
                ItemStack clickedItem = event.getCurrentItem();

                if (clickedItem == null || !(clickedItem.getItemMeta() instanceof SkullMeta skullMeta)) {
                    return;
                }

                OfflinePlayer clickedPlayer = skullMeta.getOwningPlayer();

                if (clickedPlayer == null) {
                    Messages.sendNoClanFound(player);
                    return;
                }

                UUID clickedUUID = clickedPlayer.getUniqueId();

                Clan clan = dataManager.getClans().get(getPlayerClanName(player));
                if (clan == null) {
                    Messages.sendNoClanFound(player);
                    return;
                }

                if (clan.getOwner().equals(clickedUUID)) {
                    return;
                }

                if (event.isRightClick()) {
                    if (!clan.getOwner().equals(player.getUniqueId()) && !clan.getCoOwners().contains(player.getUniqueId()) && !clan.getAdmins().contains(player.getUniqueId())) {
                        Messages.sendHigherRank(player);
                        return;
                    }

                    if (clickedUUID.equals(player.getUniqueId())) {
                        Messages.sendError(player);
                        player.closeInventory();
                        return;
                    }

                    KickGUI verificationGUI = new KickGUI(plugin);
                    verificationGUI.openVerificationMenu(player, clickedPlayer.getName(), clickedUUID);
                    player.playSound(player, Sound.ENTITY_ITEM_BREAK, 1f, 1f);
                } else if (event.isLeftClick()) {

                    if (!clan.getOwner().equals(player.getUniqueId()) && !clan.getCoOwners().contains(player.getUniqueId())) {
                        Messages.sendHigherRank(player);
                        return;
                    }

                    player.closeInventory();
                    rankGUI.openRankMenu(player, clan, clickedPlayer.getName(), clickedUUID);
                    player.playSound(player, Sound.ENTITY_VILLAGER_YES, 1f, 1.6f);
                }

            } else if (event.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&', "&cConfirm Kick"))) {

                event.setCancelled(true);
                ItemStack clickedItem = event.getCurrentItem();

                if (clickedItem == null || clickedItem.getItemMeta() == null) {
                    return;
                }

                Material clickedType = clickedItem.getType();
                if (clickedType == Material.RED_STAINED_GLASS_PANE) {
                    player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                    player.closeInventory();
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + "&7Kick cancelled."));
                } else if (clickedType == Material.GREEN_STAINED_GLASS_PANE) {
                    ItemMeta itemMeta = clickedItem.getItemMeta();
                    PersistentDataContainer dataContainer = itemMeta.getPersistentDataContainer();
                    String targetUUIDString = dataContainer.get(new NamespacedKey(plugin, "targetUUID"), PersistentDataType.STRING);
                    if (targetUUIDString == null) {
                        return;
                    }
                    UUID targetUUID = UUID.fromString(targetUUIDString);
                    Clan clan = dataManager.getClans().get(getPlayerClanName(player));

                    if (clan == null) {
                        Messages.sendNoClanFound(player);
                        return;
                    }

                    OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(targetUUID);
                    if (clan.removeMember(targetUUID)) {
                        if(clan.getAdmins().contains(targetUUID)){
                            clan.removeAdmin(targetUUID);
                        }
                        if(clan.getCoOwners().contains(targetUUID)){
                            clan.removeCoOwner(targetUUID);
                        }
                        dataManager.saveClans();
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.5f, 1.0f);
                        Messages.sendKickSuccesful(player, targetPlayer.getName());
                        Player onlineTarget = Bukkit.getPlayer(targetUUID);
                        if (onlineTarget != null) {
                            onlineTarget.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                            Messages.sendKickNotify(onlineTarget, getPlayerClanName(onlineTarget));
                        }
                    } else {
                        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                        Messages.sendNoPlayerFound(player);
                    }

                    player.closeInventory();
                }
            }
        }
    }

    private String getPlayerClanName(Player player) {
        return dataManager.getClans().entrySet().stream()
                .filter(entry -> entry.getValue().getMembers().contains(player.getUniqueId()))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }
}