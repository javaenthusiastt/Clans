package me.liam.echoBoxClanSystem.listeners;

import me.liam.echoBoxClanSystem.admins.AdminCommand;
import me.liam.echoBoxClanSystem.configs.Config;
import me.liam.echoBoxClanSystem.data.Clan;
import me.liam.echoBoxClanSystem.data.DataManager;
import me.liam.echoBoxClanSystem.gui.ColorGUI;
import me.liam.echoBoxClanSystem.gui.ClanGUI;
import me.liam.echoBoxClanSystem.gui.ClanMembersGUI;
import me.liam.echoBoxClanSystem.handling.CooldownManager;
import me.liam.echoBoxClanSystem.helpers.Messages;
import me.liam.echoBoxClanSystem.helpers.Prefixes;
import me.liam.echoBoxClanSystem.Main;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.*;
import java.util.regex.Pattern;

public class ClanListenerGUI implements Listener {

    // CLEAR THIS CLASS UP, JESUS CHRIST
    // CLEAR THIS CLASS UP, JESUS CHRIST
    // CLEAR THIS CLASS UP, JESUS CHRIST
    // CLEAR THIS CLASS UP, JESUS CHRIST
    // CLEAR THIS CLASS UP, JESUS CHRIST
    // CLEAR THIS CLASS UP, JESUS CHRIST
    // CLEAR THIS CLASS UP, JESUS CHRIST
    // CLEAR THIS CLASS UP, JESUS CHRIST
    // CLEAR THIS CLASS UP, JESUS CHRIST
    // CLEAR THIS CLASS UP, JESUS CHRIST
    // CLEAR THIS CLASS UP, JESUS CHRIST
    // CLEAR THIS CLASS UP, JESUS CHRIST
    

    private final DataManager dataManager;
    private final ColorGUI changeColorGUI;
    private final ClanMembersGUI clanMembersGUI;
    private final List<UUID> waitingForClanName = new ArrayList<>();
    private final Set<UUID> clanDeletionConfirmation = new HashSet<>();
    private final Map<UUID, String> pendingClanDeletions = new HashMap<>();
    private final Map<UUID, String> awaitingOwnershipTransfer = new HashMap<>();
    private final Map<UUID, UUID> pendingOwnershipTransfers = new HashMap<>();
    private final ClanGUI clanGUI;

    private final AdminCommand adminCommand;

    public CooldownManager cooldownManager = Main.getInstance().getCooldownManager();

    private final List<UUID> waitingForClanRename = new ArrayList<>();

    private static final Pattern SPECIAL_CHAR_PATTERN = Pattern.compile("[!\"#¤%&/()*+,\\-.:;<=>?@\\[\\]^_`{|}~]");

    private static final Set<String> DISALLOWED_WORDS = Set.of("kys", "retard", "faggot", "sex", "dick", "moron", "penis", "vagina", "fuck", "hell",
            "selfharm", "cancer", "nigger", "nigga", "pedo", "pedophiles", "racist", "butt", "butthole", "sexy", "pills", "cock", "glock", "sexslave", "slave",
            "tits", "suicide", "gay", "homo", "Niggos", "Nword", "Niggas", "BigFatDick", "Diddy");

    public ClanListenerGUI(Main plugin, ColorGUI changeColorGUI, ClanGUI clanGUI, AdminCommand adminCommand) {
        this.dataManager = plugin.getDataManager();
        this.changeColorGUI = changeColorGUI;
        this.clanGUI = clanGUI;
        this.adminCommand = adminCommand;
        this.clanMembersGUI = new ClanMembersGUI();
    }

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
        String titleString = Config.getClanGuiName();
        MiniMessage miniMessage = MiniMessage.miniMessage();
        Component expectedTitleComponent = miniMessage.deserialize(titleString);
        String expectedTitle = LegacyComponentSerializer.legacySection().serialize(expectedTitleComponent);
        if (inventoryTitle.equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', expectedTitle))) {
            event.setCancelled(true);

            if(event.isShiftClick()){
                return;
            }

            if(clickedItem.getType() == Material.PLAYER_HEAD){
                player.closeInventory();
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + "&bYou thought i was serious?..."));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + "&7Enjoy some cat sounds though! <3"));
                player.playSound(player, Sound.ENTITY_CAT_PURR, 1f, 1f);
            }

            if (clickedItem.getType() == Config.getItemStack("gui.adminstration", Material.BLAZE_POWDER).getType()) {
                clanGUI.openClanAdministrationMenuDEFAULT(player);
                player.playSound(player, Sound.UI_BUTTON_CLICK, 1f, 1f);
            } else if (clickedItem.getType() == Config.getItemStack("gui.management", Material.CHEST).getType()) {
                clanGUI.openClanManagementMenuDEFAULT(player);
                player.playSound(player, Sound.UI_BUTTON_CLICK, 1f, 1f);
            } else if (clickedItem.getType() == Config.getItemStack("gui.information", Material.PAPER).getType()) {
                clanGUI.openClanInformationMenuDEFAULT(player);
                player.playSound(player, Sound.UI_BUTTON_CLICK, 1f, 1f);
            } else if (clickedItem.getType() == Config.getItemStack("gui.customization", Material.BLAZE_ROD).getType()) {
                clanGUI.openClanCustomizationMenuDEFAULT(player);
                player.playSound(player, Sound.UI_BUTTON_CLICK, 1f, 1f);
            } else if (clickedItem.getType() == Config.getItemStack("gui.communication", Material.BOOK).getType()) {
                clanGUI.openClanCommunicationMenuDEFAULT(player);
                player.playSound(player, Sound.UI_BUTTON_CLICK, 1f, 1f);
            }
        }
        else if (inventoryTitle.equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', "&8Clan Administration"))) {
            event.setCancelled(true);

            if (event.isShiftClick()) {
                return;
            }
            if (clickedItem.getType() == Config.getItemStack("gui.CLAN_CREATION", Material.EMERALD).getType()) {
                player.closeInventory();
                handleCreateClan(player);
                player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
            } else if (clickedItem.getType() == Config.getItemStack("gui.CLAN_DELETE", Material.SKELETON_SKULL).getType()) {
                player.closeInventory();
                handleDeleteClan(player);
                player.playSound(player, Sound.UI_BUTTON_CLICK, 1f, 1f);
            } else if (clickedItem.getType() == Config.getItemStack("gui.CLAN_RENAME", Material.NAME_TAG).getType()) {
                player.closeInventory();
                handleClanRename(player);
                player.playSound(player, Sound.UI_BUTTON_CLICK, 1f, 1f);
            } else if (clickedItem.getType() == Config.getItemStack("gui.CLAN_LEAVE", Material.FEATHER).getType()) {
                player.closeInventory();
                handleLeaveClan(player);
                player.playSound(player, Sound.UI_BUTTON_CLICK, 1f, 1f);
            } else if (clickedItem.getType() == Config.getItemStack("gui.CLAN_TRANSFER", Material.STICK).getType()) {
                player.closeInventory();
                handleTransferOwnership(player);
                player.playSound(player, Sound.UI_BUTTON_CLICK, 1f, 1f);
            }
        }
        else if (inventoryTitle.equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', "&8Clan Information"))) {
            event.setCancelled(true);
            if(event.isShiftClick()){
                return;
            }
            if (clickedItem.getType() == Config.getItemStack("gui.CLAN_LEADERBOARDS", Material.ITEM_FRAME).getType()) {
                if(!(Config.isLeaderboardsEnabled())){
                    Messages.sendConfigDisabledFeature(player);
                    player.closeInventory();
                    return;
                }
                player.playSound(player, Sound.UI_BUTTON_CLICK, 1f, 1f);
                displayClanLeaderboard(player);
            } else if (clickedItem.getType() == Config.getItemStack("gui.CLAN_INFORMATION", Material.PAPER).getType()) {
                player.closeInventory();
                player.playSound(player, Sound.UI_BUTTON_CLICK, 1f, 1f);
                player.performCommand("claninfo "+player.getName());
            } else if (clickedItem.getType() == Config.getItemStack("gui.CLAN_LOGO", Material.BLACK_BANNER).getType()) {
                player.playSound(player, Sound.UI_BUTTON_CLICK, 1f, 1f);
                player.closeInventory();
                handleSetClanLogo(player);
            }
        }
        else if (inventoryTitle.equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', "&8Clan Management"))) {
            event.setCancelled(true);

            if (event.isShiftClick()) {
                return;
            }

            Material clickedType = clickedItem.getType();

            if (clickedType == Config.getItemStack("gui.CLAN_MEMBERS", Material.PLAYER_HEAD).getType()) {
                player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);
                handleViewClanMembers(player);
            } else if (clickedType == Config.getItemStack("gui.CLAN_CHEST", Material.CHEST).getType()) {
                player.playSound(player, Sound.UI_BUTTON_CLICK, 1f, 1f);
                player.closeInventory();
                player.performCommand("clanchest");
            } else if (clickedType == Config.getItemStack("gui.CLAN_POINTS", Material.NETHER_STAR).getType()) {
                player.playSound(player, Sound.UI_BUTTON_CLICK, 1f, 1f);
                player.closeInventory();
                handleClanPoints(player);
            } else if (clickedType == Config.getItemStack("gui.CLAN_ANNOUNCEMENT", Material.EMERALD).getType()) {
                player.playSound(player, Sound.UI_BUTTON_CLICK, 1f, 1f);
                player.closeInventory();
                handleClanAnnouncement(player);
            }
        }
        else if (inventoryTitle.equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', "&8Clan Communication"))) {
            event.setCancelled(true);


            if(event.isShiftClick()){
                return;
            }

            if (clickedItem.getType() == Config.getItemStack("gui.CLAN_INVITE", Material.BELL).getType()) {
                player.playSound(player, Sound.UI_BUTTON_CLICK, 1f, 1f);
                player.closeInventory();
                player.performCommand("claninvite");
            } else if (clickedItem.getType() == Config.getItemStack("gui.CLAN_SEE_INVITES", Material.ECHO_SHARD).getType()) {
                player.playSound(player, Sound.UI_BUTTON_CLICK, 1f, 1f);
                player.closeInventory();
                handleGetClanInvites(player);
            } else if (clickedItem.getType() == Config.getItemStack("gui.CLAN_CHAT", Material.BOOK).getType()) {
                player.playSound(player, Sound.UI_BUTTON_CLICK, 1f, 1f);
                player.closeInventory();
                player.performCommand("clanchat");
            }
        }

        else if (inventoryTitle.equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', "&8Clan Customization"))) {
            event.setCancelled(true);


            if(event.isShiftClick()){
                return;
            }

            if (clickedItem.getType() == Config.getItemStack("gui.CLAN_RANKS", Material.GOLDEN_SWORD).getType()) {
                player.playSound(player, Sound.UI_BUTTON_CLICK, 1f, 1f);
                player.closeInventory();
            } else if (clickedItem.getType() == Config.getItemStack("gui.CLAN_SETTINGS", Material.EMERALD).getType()) {
                player.playSound(player, Sound.UI_BUTTON_CLICK, 1f, 1f);
                player.closeInventory();
            } else if (clickedItem.getType() == Config.getItemStack("gui.CLAN_COLOR", Material.BLACK_DYE).getType()) {
                player.playSound(player, Sound.UI_BUTTON_CLICK, 1f, 1f);
                handleClanChangeColor(player);
            }
        }
    }


    private void handleClanChangeColor(Player player){
        String playerClanName = getPlayerClanName(player);
        if (playerClanName == null) {
            Messages.sendClanNeeded(player);
            return;
        }
        Clan clan = dataManager.getClans().get(playerClanName);
        if (clan == null) {
            Messages.sendNoClanFound(player);
            return;
        }
        if (!clan.getOwner().equals(player.getUniqueId()) && !clan.getCoOwners().contains(player.getUniqueId()) && !clan.getAdmins().contains(player.getUniqueId())) {
            Messages.sendHigherRank(player);
            return;
        }

        int points = clan.getPoints();

        if(!(points > 2500)){
            Messages.sendClanNeedMorePoints(player);
            return;
        }

        changeColorGUI.openColorSelectionGUI(player);
    }

    private void handleClanAnnouncement(Player player) {
        String playerClanName = getPlayerClanName(player);
        if (playerClanName == null) {
            Messages.sendClanNeeded(player);
            return;
        }
        Clan clan = dataManager.getClans().get(playerClanName);
        if (clan == null) {
            Messages.sendNoClanFound(player);
            return;
        }
        if (!clan.getOwner().equals(player.getUniqueId()) && !clan.getCoOwners().contains(player.getUniqueId()) && !clan.getAdmins().contains(player.getUniqueId())) {
            Messages.sendHigherRank(player);
            return;
        }

        if(cooldownManager.isOnCooldown(player.getUniqueId(), "ClanAnnouncement")){
            Messages.sendYouAreOnCooldownCLANANNOUNCEMENT(player);
            return;
        }

        ChatColor clanColor = clan.getColor() != null ? clan.getColor() : ChatColor.WHITE;
        String formattedClanName = playerClanName.substring(0, 1).toUpperCase() + playerClanName.substring(1).toLowerCase();

        Messages.sendAnnouncement(clanColor+formattedClanName, player.getName());

        if(Config.isCooldownEnabled()){
            cooldownManager.setCooldown(player.getUniqueId(), Duration.ofMinutes(45), "ClanAnnouncement");
        }
    }


    private void handleClanRename(Player player) {

        if(cooldownManager.isOnCooldown(player.getUniqueId(), "ClanRename")){
            Messages.sendYouAreOnCooldownRENAMES(player);
            return;
        }

        String playerClan = getPlayerClanName(player);
        if (playerClan == null) {
            Messages.sendClanNeeded(player);
            return;
        }

        Clan clan = dataManager.getClans().get(playerClan);
        if (clan == null) {
            Messages.sendNoClanFound(player);
            return;
        }

        if (!clan.getOwner().equals(player.getUniqueId())) {
            Messages.sendYouAreNotTheOwnerOfClan(player);
            return;
        }


        Messages.sendWaitingForRename(player);
        waitingForClanRename.add(player.getUniqueId());
    }

    private void handleGetClanInvites(Player player) {
        UUID playerUUID = player.getUniqueId();
        List<String> invitations = dataManager.getPlayerInvitations(playerUUID);
        if(invitations.isEmpty()){
            Messages.sendNoPendingInvites(player);
        }else{
            Messages.sendPendingInvites(player, invitations.size());
        }
    }

    private void handleViewClanMembers(Player player) {
        String playerClanName = getPlayerClanName(player);
        if (playerClanName == null) {
            Messages.sendClanNeeded(player);
            return;
        }

        Clan clan = dataManager.getClans().get(playerClanName);
        if (clan == null) {
            Messages.sendNoClanFound(player);
            return;
        }

        clanMembersGUI.openClanMembersMenu(player, clan);
    }

    public void handleCreateClan(Player player) {
        if (dataManager.getClans().values().stream().anyMatch(clan -> clan.getMembers().contains(player.getUniqueId()))) {
            Messages.sendYouAreAlreadyInAClan(player);
            return;
        }

        if(cooldownManager.isOnCooldown(player.getUniqueId(), "ClanCreate")){
            Messages.sendYouAreOnCooldownCREATIONS(player);
            return;
        }


        Messages.sendWaitingForName(player);
        waitingForClanName.add(player.getUniqueId());
    }

    public void handleDeleteClan(Player player) {
        String playerClanName = getPlayerClanName(player);
        if (playerClanName == null) {
            Messages.sendClanNeeded(player);
            return;
        }
        Clan clan = dataManager.getClans().get(playerClanName);
        if (clan == null) {
            Messages.sendNoClanFound(player);
            return;
        }
        if (!clan.getOwner().equals(player.getUniqueId())) {
            Messages.sendYouAreNotTheOwnerOfClan(player);
            return;
        }

        Messages.sendConfirmDeletion(player, playerClanName);
        clanDeletionConfirmation.add(player.getUniqueId());
        pendingClanDeletions.put(player.getUniqueId(), playerClanName);
    }

    public String getPlayerClanName(Player player) {
        UUID playerUUID = player.getUniqueId();
        return dataManager.getClans().entrySet().stream()
                .filter(entry -> entry.getValue().getMembers().contains(playerUUID))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }

    private void handleClanPoints(Player player) {
        String playerClanName = getPlayerClanName(player);
        if (playerClanName == null) {
            Messages.sendClanNeeded(player);
            return;
        }
        Clan clan = dataManager.getClans().get(playerClanName);
        if (clan == null) {
            Messages.sendNoClanFound(player);
            return;
        }
        Messages.sendPlayerClanPoints(player, clan.getPoints());
    }

    public void displayClanLeaderboard(Player player) {
        Inventory leaderboardInventory = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + "Top 10 Clans"));

        List<Map.Entry<String, Clan>> sortedClans = new ArrayList<>(dataManager.getClans().entrySet());
        sortedClans.sort((a, b) -> Integer.compare(b.getValue().getPoints(), a.getValue().getPoints()));

        for (int i = 0; i < 10; i++) {
            ItemStack item;
            ItemMeta meta;

            if (i < sortedClans.size()) {
                Map.Entry<String, Clan> entry = sortedClans.get(i);
                String clanName = entry.getKey();
                Clan clan = entry.getValue();
                int clanPoints = clan.getPoints();
                UUID founderUUID = clan.getOwner();
                String founderName = Bukkit.getOfflinePlayer(founderUUID).getName();
                if (founderName == null) {
                    founderName = "Unknown Player (Clan Founder)";
                }

                ItemStack logo = clan.getLogo();
                item = (logo != null && logo.getType() != Material.AIR) ? logo.clone() : new ItemStack(Material.BARRIER);

                meta = item.getItemMeta();
                if (meta != null) {
                    ChatColor clanColor = clan.getColor() != null ? clan.getColor() : ChatColor.GRAY;

                    String formattedClanName = clanName.substring(0, 1).toUpperCase() + clanName.substring(1).toLowerCase();

                    meta.setDisplayName(clanColor + "" + ChatColor.BOLD + (i + 1) + ". " + clanColor + formattedClanName);
                    List<String> lore = Arrays.asList(
                            ChatColor.translateAlternateColorCodes('&', clanColor + "&7Points: "+clanColor + clanPoints),
                            ChatColor.translateAlternateColorCodes('&', clanColor + "&7Clan Founder: "+clanColor + founderName)
                    );
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                }
            } else {
                item = new ItemStack(Material.NAME_TAG);
                meta = item.getItemMeta();
                if (meta != null) {
                    meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7" + (i + 1) + ". &d(Free Spot)"));
                    meta.setLore(Collections.singletonList(ChatColor.translateAlternateColorCodes('&', "&7No clan data")));
                    item.setItemMeta(meta);
                }
            }

            int slotIndex = switch (i) {
                case 0 -> 4;
                case 1 -> 5;
                case 2 -> 3;
                default -> i + 7;
            };
            leaderboardInventory.setItem(slotIndex, item);
        }

        for (int i = 0; i < leaderboardInventory.getSize(); i++) {
            if (leaderboardInventory.getItem(i) == null) {
                ItemStack emptyItem = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
                ItemMeta emptyMeta = emptyItem.getItemMeta();
                if (emptyMeta != null) {
                    emptyMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7 "));
                    emptyItem.setItemMeta(emptyMeta);
                }
                leaderboardInventory.setItem(i, emptyItem);
            }
        }
        player.openInventory(leaderboardInventory);
    }


    public void handleLeaveClan(Player player) {
        String clanName = getPlayerClanName(player);
        if (clanName != null) {
            Clan clan = dataManager.getClans().get(clanName);
            if (clan != null) {
                UUID playerUUID = player.getUniqueId();

                if (clan.getOwner().equals(playerUUID)) {

                    Messages.sendCantLeaveWhileOwner(player);

                } else {
                    if (clan.getCoOwners().contains(playerUUID)) {
                        clan.getCoOwners().remove(playerUUID);
                    }

                    if (clan.getAdmins().contains(playerUUID)) {
                        clan.getAdmins().remove(playerUUID);
                    }

                    boolean removed = clan.removeMember(playerUUID);
                    if (removed) {
                        dataManager.saveClans();
                        Messages.sendLeftClan(player, clanName);
                        for (UUID memberUUID : clan.getMembers()) {
                            Player member = dataManager.getPlugin().getServer().getPlayer(memberUUID);
                            if (member != null) {
                                Messages.sendNotifyLeftClan(member, player.getName());
                            }
                        }
                    } else {
                        Messages.sendFailedToLeaveClan(player);
                    }
                }
            } else {
                Messages.sendNoClanFound(player);
            }
        } else {
            Messages.sendClanNeeded(player);
        }
    }

    public void handleTransferOwnership(Player player) {
        String playerClanName = getPlayerClanName(player);
        if (playerClanName == null) {
            Messages.sendClanNeeded(player);
            return;
        }

        Clan clan = dataManager.getClans().get(playerClanName);
        if (clan == null) {
            Messages.sendNoClanFound(player);
            return;
        }

        if (!clan.getOwner().equals(player.getUniqueId())) {
            Messages.sendYouAreNotTheOwnerOfClan(player);
            return;
        }

        List<UUID> membersUUID = clan.getMembers();

        Messages.sendProcessingTransfer(player);

        for (UUID memberUUID : membersUUID) {
            if (!memberUUID.equals(player.getUniqueId())) {
                String memberName = Bukkit.getOfflinePlayer(memberUUID).getName();
                if (memberName == null) {
                    memberName = "Unknown Player";
                }
            }
        }
        awaitingOwnershipTransfer.put(player.getUniqueId(), playerClanName);
    }

    public void handleSetClanLogo(Player player) {
        String playerClanName = getPlayerClanName(player);
        if (playerClanName == null) {
            Messages.sendClanNeeded(player);
            return;
        }
        Clan clan = dataManager.getClans().get(playerClanName);
        if (clan == null) {
            Messages.sendNoClanFound(player);
            return;
        }

        if (!clan.getOwner().equals(player.getUniqueId()) && !clan.getCoOwners().contains(player.getUniqueId())) {
            Messages.sendHigherRank(player);
            return;
        }

        ItemStack itemInHand = player.getInventory().getItemInMainHand();

        if (itemInHand.getType() == Material.AIR) {
            Messages.sendHowToSetLogo(player);
            player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1f, 1f);
            return;
        }

        ItemStack logo = itemInHand.clone();
        logo.setAmount(1);
        clan.setLogo(logo);
        dataManager.saveClans();
        Messages.sendLogoSet(player);
        player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        String message = event.getMessage().trim();

        if (waitingForClanName.contains(playerUUID)) {
            event.setCancelled(true);

            if (message.isEmpty()) {
                waitingForClanName.remove(playerUUID);
                Messages.sendClanNameEmpty(player);
                return;
            }
            if (message.length() > Config.getMaxClanNameLength()) {
                waitingForClanName.remove(playerUUID);
                Messages.sendClanNameTooLong(player);
                return;
            }

            if (SPECIAL_CHAR_PATTERN.matcher(message).find()) {
                waitingForClanName.remove(playerUUID);
                Messages.sendClanNameContainSpecialCharacters(player);
                return;
            }

            if (message.contains(" ")) {
                if(Config.isCancelSpacesEnabled()){
                    waitingForClanName.remove(playerUUID);
                    Messages.SendClanNameContainSpaces(player);
                    return;
                }
            }

            if(Config.isFilterDetectionEnabled()){
                for (String word : DISALLOWED_WORDS) {
                    if (message.toLowerCase().contains(word)) {
                        waitingForClanName.remove(playerUUID);
                        Messages.sendInappropiateLanguage(player);
                        return;
                    }
                }
            }

            String clanNameLowerCase = message.toLowerCase();

            for (String existingClanName : dataManager.getClans().keySet()) {
                if (existingClanName.toLowerCase().equals(clanNameLowerCase)) {
                    waitingForClanName.remove(playerUUID);
                    Messages.sendClanAlreadyExists(player);
                    return;
                }
            }

            List<UUID> admins = new ArrayList<>();
            List<UUID> coOwners = new ArrayList<>();
            UUID owner = player.getUniqueId();

            Date getDate = new Date();
            Clan newClan = new Clan(owner, List.of(owner), admins, coOwners, getDate);
            newClan.addPoints(1);
            newClan.setPlayerPoints(playerUUID, 1);
            dataManager.getClans().put(message, newClan);

            dataManager.saveClans();

            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
            player.playEffect(player.getLocation(), Effect.DRAGON_BREATH, null);

            Messages.sendEarnedPoint(player, "1");
            Messages.sendMadeClan(player, message);

            waitingForClanName.remove(playerUUID);
            if(Config.isCooldownEnabled()){
                cooldownManager.setCooldown(playerUUID, Duration.ofMinutes(45), "ClanCreate");
            }

        } else if (clanDeletionConfirmation.contains(playerUUID)) {
            event.setCancelled(true);
            String clanName = pendingClanDeletions.get(playerUUID);

            if (message.equals(clanName)) {
                if (dataManager.deleteClan(clanName, playerUUID)) {
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                    Messages.sendDeletedClan(player, clanName);
                    dataManager.saveClans();
                    dataManager.deleteClanChestFile(clanName);
                } else {
                    Messages.sendError(player);
                }
                clanDeletionConfirmation.remove(playerUUID);
                pendingClanDeletions.remove(playerUUID);
            } else {
                Messages.sendFailedToDeleteClan(player);
                clanDeletionConfirmation.remove(playerUUID);
                pendingClanDeletions.remove(playerUUID);
            }
        } else if (awaitingOwnershipTransfer.containsKey(playerUUID)) {
            event.setCancelled(true);
            String playerClanName = awaitingOwnershipTransfer.get(playerUUID);
            Clan clan = dataManager.getClans().get(playerClanName);

            if (clan != null) {
                Player newOwner = Bukkit.getPlayer(message);
                if (newOwner != null) {
                    if (newOwner.getUniqueId().equals(clan.getOwner())) {
                        Messages.sendTransferFailedToThemself(player);
                        awaitingOwnershipTransfer.remove(playerUUID);
                        return;
                    }
                    if (clan.getMembers().contains(newOwner.getUniqueId())) {
                        pendingOwnershipTransfers.put(newOwner.getUniqueId(), playerUUID);
                        Messages.sendTransferOffered(newOwner, playerClanName);
                        Messages.sendOwnershipRequestSent(player, newOwner.getName());
                    } else {
                        Messages.sendTransferFailedNotFound(player);
                    }
                } else {
                    Messages.sendTransferFailedNotFound(player);
                }
                awaitingOwnershipTransfer.remove(playerUUID);
            } else {
                Messages.sendError(player);
            }
        } else if (pendingOwnershipTransfers.containsKey(playerUUID)) {
            event.setCancelled(true);
            UUID currentOwnerUUID = pendingOwnershipTransfers.get(playerUUID);
            Player currentOwner = Bukkit.getPlayer(currentOwnerUUID);

            if (currentOwner == null) {
                Messages.sendTransferFailedNewOwnerLeft(player);
                pendingOwnershipTransfers.remove(playerUUID);
                return;
            }

            if (message.equalsIgnoreCase("Accept")) {
                String playerClanName = getPlayerClanName(currentOwner);
                Clan clan = dataManager.getClans().get(playerClanName);

                if (clan != null) {
                    clan.setOwner(playerUUID);
                    dataManager.saveClans();
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                    currentOwner.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                    Messages.sendAcceptedOwnership(player, playerClanName);
                    Messages.sendOwnershipTransferred(currentOwner, playerClanName);
                } else {
                    Messages.sendError(player);
                    Messages.sendError(currentOwner);
                }
            } else if (message.equalsIgnoreCase("Deny")) {
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                currentOwner.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                Messages.sendDeniedOwnership(player);
                Messages.sendNotifyDeniedOwnership(currentOwner);
            } else {
                Messages.sendError(player);
                return;
            }
            pendingOwnershipTransfers.remove(playerUUID);
        } else if (waitingForClanRename.contains(playerUUID)){
            event.setCancelled(true);
            String playerClanName = getPlayerClanName(player);
            Clan clan = dataManager.getClans().get(playerClanName);
            
            if (message.isEmpty()) {
                Messages.sendError(player);
                return;
            }

            if (message.length() > Config.getMaxClanNameLength()) {
                Messages.sendClanNameTooLong(player);
                return;
            }

            if (SPECIAL_CHAR_PATTERN.matcher(message).find()) {
                waitingForClanName.remove(playerUUID);
                Messages.sendClanNameContainSpecialCharacters(player);
                return;
            }

            if (message.contains(" ")) {
                if(Config.isCancelSpacesEnabled()){
                    Messages.SendClanNameContainSpaces(player);
                    waitingForClanName.remove(playerUUID);
                    return;
                }
            }

            if(Config.isFilterDetectionEnabled()){
                for (String word : DISALLOWED_WORDS) {
                    if (message.toLowerCase().contains(word)) {
                        waitingForClanName.remove(playerUUID);
                        Messages.sendInappropiateLanguage(player);
                        return;
                    }
                }
            }

            if(Config.isCooldownEnabled()){
                cooldownManager.setCooldown(playerUUID, Duration.ofMinutes(45), "ClanRename");
            }
            dataManager.renameClan(playerClanName, message);
            dataManager.getClans().put(message, clan);
            dataManager.getClans().remove(playerClanName);
            dataManager.saveClans();

            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
            Messages.sendRenamedClan(player, message);
            waitingForClanRename.remove(playerUUID);
        }
    }

    @EventHandler
    public void onPlayerQuit(@NotNull PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        waitingForClanName.remove(playerUUID);
        clanDeletionConfirmation.remove(playerUUID);
        pendingClanDeletions.remove(playerUUID);
        awaitingOwnershipTransfer.remove(playerUUID);
        pendingOwnershipTransfers.remove(playerUUID);
        waitingForClanRename.remove(playerUUID);
    }
}
