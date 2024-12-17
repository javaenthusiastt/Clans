package me.liam.echoBoxClanSystem.gui;
import me.liam.echoBoxClanSystem.Main;
import me.liam.echoBoxClanSystem.colors.Adventure;
import me.liam.echoBoxClanSystem.configs.Config;
import me.liam.echoBoxClanSystem.data.DataManager;
import me.liam.echoBoxClanSystem.helpers.Licensed;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.ApiStatus;

import java.util.*;
public class ClanGUI {

    private static final int SIZE = 54;

    private final DataManager dataManager;

    private Inventory inventory;

    private final Map<Player, Integer> rainbowTasks = new HashMap<>();
    private final Map<Player, Integer> shiningTasks = new HashMap<>();

    public ClanGUI(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public void openClanMenu(Player player) {
        String inventoryTitle = Adventure.parseToLegacy(Config.getClanGuiName());

        Inventory playerInventory = Bukkit.createInventory(null, SIZE, inventoryTitle);

        player.playSound(player, Sound.BLOCK_ENDER_CHEST_OPEN, 1f, 1f);

        if(Config.getGuiPreference().equalsIgnoreCase("DEFAULT")){
            MainMenuDEFAULT(playerInventory, player);
            player.openInventory(playerInventory);
        }else if (Config.getGuiPreference().equalsIgnoreCase("STYLED")){
            MainMenuSTYLED(playerInventory, player);
            player.openInventory(playerInventory);
        }

        if(Config.isDecorationEnabled() && Config.getDecorationType().equalsIgnoreCase("Rainbow")){
            decorationRAINBOW(player, playerInventory);
        }else if (Config.isDecorationEnabled() && Config.getDecorationType().equalsIgnoreCase("Shiny")){
            decorationSHINY(player, playerInventory);
        }
    }

    private void MainMenuSTYLED(Inventory inventory, Player player){
        for (int slot : DecorationGUI.STYLED_FILLING) {
            inventory.setItem(slot, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        }
        inventory.setItem(Config.getSlot("gui.adminstration"), createAdministrationItemDEFAULT());
        inventory.setItem(Config.getSlot("gui.management"), createManagementItemDEFAULT());
        inventory.setItem(Config.getSlot("gui.information"), createInformationItemDEFAULT());
        inventory.setItem(Config.getSlot("gui.communication"), createCommunicationDEFAULTItem());
        inventory.setItem(Config.getSlot("gui.customization"), createCustomizationItemDEFAULT());

        if(Licensed.isNotLicensed()){
            inventory.setItem(53, credits());
        }
    }

    private void MainMenuDEFAULT(Inventory inventory, Player player) {
        inventory.setItem(Config.getSlot("gui.adminstration"), createAdministrationItemDEFAULT());
        inventory.setItem(Config.getSlot("gui.management"), createManagementItemDEFAULT());
        inventory.setItem(Config.getSlot("gui.information"), createInformationItemDEFAULT());
        inventory.setItem(Config.getSlot("gui.communication"), createCommunicationDEFAULTItem());
        inventory.setItem(Config.getSlot("gui.customization"), createCustomizationItemDEFAULT());

        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
            }
        }

        if(Licensed.isNotLicensed()){
            inventory.setItem(53, credits());
        }

    }

    public void openClanManagementMenuDEFAULT(Player player) {
        this.inventory = Bukkit.createInventory(null, 36, ChatColor.translateAlternateColorCodes('&', "&8Clan Management"));
        setupManagementMenuItemsDEFAULT();
        player.openInventory(inventory);
    }

    private void setupManagementMenuItemsDEFAULT() {
        inventory.setItem(Config.getSlot("gui.CLAN_MEMBERS"), Config.getItemStack("gui.CLAN_MEMBERS", Material.PLAYER_HEAD));
        inventory.setItem(Config.getSlot("gui.CLAN_CHEST"), Config.getItemStack("gui.CLAN_CHEST", Material.CHEST));
        inventory.setItem(Config.getSlot("gui.CLAN_POINTS"), Config.getItemStack("gui.CLAN_POINTS", Material.NETHER_STAR));
        inventory.setItem(Config.getSlot("gui.CLAN_ANNOUNCEMENT"), Config.getItemStack("gui.CLAN_ANNOUNCEMENT", Material.EMERALD));
        DecorationGUI.magentafilling(inventory);
    }

    public void setupAdministrationMenuItemsDEFAULT(Player player) {
        String pClan = dataManager.getPlayerClanName(player);
        if (pClan == null)
            inventory.setItem(Config.getSlot("gui.CLAN_CREATION"), Config.getItemStack("gui.CLAN_CREATION", Material.EMERALD));

        inventory.setItem(Config.getSlot("gui.CLAN_DELETE"), Config.getItemStack("gui.CLAN_DELETE", Material.SKELETON_SKULL));
        inventory.setItem(Config.getSlot("gui.CLAN_LEAVE"), Config.getItemStack("gui.CLAN_LEAVE", Material.FEATHER));
        inventory.setItem(Config.getSlot("gui.CLAN_TRANSFER"), Config.getItemStack("gui.CLAN_TRANSFER", Material.STICK));
        inventory.setItem(Config.getSlot("gui.CLAN_RENAME"), Config.getItemStack("gui.CLAN_RENAME", Material.NAME_TAG));
        DecorationGUI.greenfilling(inventory);
    }

    private void setupCommunicationMenuItemsDEFAULT() {
        inventory.setItem(Config.getSlot("gui.CLAN_CHAT"), Config.getItemStack("gui.CLAN_CHAT", Material.BOOK));
        inventory.setItem(Config.getSlot("gui.CLAN_INVITE"), Config.getItemStack("gui.CLAN_INVITE", Material.BELL));
        inventory.setItem(Config.getSlot("gui.CLAN_SEE_INVITES"), Config.getItemStack("gui.CLAN_SEE_INVITES", Material.ECHO_SHARD));
        DecorationGUI.magentafilling(inventory);
    }


    private void setupCustomizationMenuItemsDEFAULT() {
        inventory.setItem(Config.getSlot("gui.CLAN_COLOR"), Config.getItemStack("gui.CLAN_COLOR", Material.BLACK_DYE));
        inventory.setItem(Config.getSlot("gui.CLAN_SETTINGS"), Config.getItemStack("gui.CLAN_SETTINGS", Material.EMERALD));
        inventory.setItem(Config.getSlot("gui.CLAN_RANKS"), Config.getItemStack("gui.CLAN_RANKS", Material.GOLDEN_SWORD));
        DecorationGUI.magentafilling(inventory);
    }

    private void setupInformationMenuItemsDEFAULT() {
        inventory.setItem(Config.getSlot("gui.CLAN_LEADERBOARDS"), Config.getItemStack("gui.CLAN_LEADERBOARDS", Material.ITEM_FRAME));
        inventory.setItem(Config.getSlot("gui.CLAN_INFORMATION"), Config.getItemStack("gui.CLAN_INFORMATION", Material.PAPER));
        inventory.setItem(Config.getSlot("gui.CLAN_LOGO"), Config.getItemStack("gui.CLAN_LOGO", Material.BLACK_BANNER));
        DecorationGUI.magentafilling(inventory);
    }







    public void openClanInformationMenuDEFAULT(Player player) {
        this.inventory = Bukkit.createInventory(null, 36, ChatColor.translateAlternateColorCodes('&', "&8Clan Information"));
        setupInformationMenuItemsDEFAULT();
        player.openInventory(inventory);
    }
    public void openClanCommunicationMenuDEFAULT(Player player) {
        this.inventory = Bukkit.createInventory(null, 36, ChatColor.translateAlternateColorCodes('&', "&8Clan Communication"));
        setupCommunicationMenuItemsDEFAULT();
        player.openInventory(inventory);
    }
    public void openClanCustomizationMenuDEFAULT(Player player) {
        this.inventory = Bukkit.createInventory(null, 36, ChatColor.translateAlternateColorCodes('&', "&8Clan Customization"));
        setupCustomizationMenuItemsDEFAULT();
        player.openInventory(inventory);
    }
    public void openClanAdministrationMenuDEFAULT(Player player) {
        this.inventory = Bukkit.createInventory(null, 36, ChatColor.translateAlternateColorCodes('&', "&8Clan Administration"));
        setupAdministrationMenuItemsDEFAULT(player);
        player.openInventory(inventory);
    }
    public ItemStack createAdministrationItemDEFAULT() {
        return Config.getItemStack("gui.adminstration", Material.BLAZE_POWDER);
    }
    public ItemStack createManagementItemDEFAULT() {
        return Config.getItemStack("gui.management", Material.CHEST);
    }
    public ItemStack createInformationItemDEFAULT() {
        return Config.getItemStack("gui.information", Material.PAPER);
    }
    public ItemStack createCommunicationDEFAULTItem() {
        return Config.getItemStack("gui.communication", Material.BOOK);
    }
    public ItemStack createCustomizationItemDEFAULT() {
        return Config.getItemStack("gui.customization", Material.BLAZE_ROD);
    }
    public ItemStack credits(){
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();

        OfflinePlayer me = Bukkit.getOfflinePlayer("sorryplspls");
        assert skullMeta != null;
        skullMeta.setOwningPlayer(me);
        skullMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&aClan Developement Information"));
        List<String> loreLines = new ArrayList<>();
        loreLines.add(ChatColor.translateAlternateColorCodes('&', "&8Information"));
        loreLines.add("");
        loreLines.add(ChatColor.translateAlternateColorCodes('&', "&8● &7Dev: &fsorryplspls"));
        loreLines.add(ChatColor.translateAlternateColorCodes('&', "&8● &7Active API Session: &f"+Main.getInstance().getDescription().getAPIVersion()));
        loreLines.add("");
        loreLines.add(ChatColor.translateAlternateColorCodes('&', "&cBugs?"));
        loreLines.add(ChatColor.translateAlternateColorCodes('&', "  &8● &7Contact: &fl.x60 <- &2(Discord)"));
        loreLines.add(ChatColor.translateAlternateColorCodes('&', "  &8● &7Source Code: &fhttps://github.com/javaenthusiastt"));
        loreLines.add("");
        loreLines.add(ChatColor.translateAlternateColorCodes('&', "&a&lLeft Click for 10 Clan Points!"));
        skullMeta.setLore(loreLines);
        skull.setItemMeta(skullMeta);

        return skull;
    }
    public ItemStack createItem(Material material, String displayName, String... loreLines) {
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

    public void decorationSHINY(Player player, Inventory inventory) {
        if (shiningTasks.containsKey(player)) {
            return;
        }

        int taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(
                Objects.requireNonNull(Main.getInstance()),
                new Runnable() {
                    private int colorIndex = 0;
                    private final Material[] christmasColors = {
                            Material.RED_STAINED_GLASS_PANE,
                            Material.GREEN_STAINED_GLASS_PANE,
                            Material.WHITE_STAINED_GLASS_PANE
                    };

                    @Override
                    public void run() {
                        if (inventory == null || !player.getOpenInventory().getTopInventory().equals(inventory)) {
                            stopSHINY(player);
                            return;
                        }

                        for (int slot : DecorationGUI.DECORATION_SHINING_INTS) {
                            if (slot < inventory.getSize()) {
                                Material glassMaterial = christmasColors[colorIndex];
                                inventory.setItem(slot, createItem(glassMaterial, Config.getBackgroundDecorationText()));
                            }
                        }

                        colorIndex = (colorIndex + 1) % christmasColors.length;
                        player.updateInventory();
                    }
                },
                0L, 30L
        );

        shiningTasks.put(player, taskID);
    }

    public void decorationRAINBOW(Player player, Inventory inventory) {
        if (rainbowTasks.containsKey(player)) {
            return;
        }

        int taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(
                Objects.requireNonNull(Main.getInstance()),
                new Runnable() {
                    private int colorIndex = 0;

                    @Override
                    public void run() {
                        if (inventory == null || !player.getOpenInventory().getTopInventory().equals(inventory)) {
                            stopRAINBOW(player);
                            return;
                        }

                        for (int i = 0; i < inventory.getSize(); i++) {
                            ItemStack item = inventory.getItem(i);

                            if (item != null && item.getType().toString().contains("STAINED_GLASS_PANE")) {
                                inventory.setItem(i, DecorationGUI.RAINBOW_GLASS[colorIndex]);
                            }
                        }
                        colorIndex = (colorIndex + 1) % DecorationGUI.RAINBOW_GLASS.length;
                        player.updateInventory();
                    }
                },
                0L, 20L
        );

        rainbowTasks.put(player, taskID);
    }
    public void stopSHINY(Player player) {
        if (shiningTasks.containsKey(player)) {
            Bukkit.getScheduler().cancelTask(shiningTasks.get(player));
            shiningTasks.remove(player);
        }
    }
    public void stopRAINBOW(Player player) {
        Integer taskID = rainbowTasks.remove(player);
        if (taskID != null) {
            Bukkit.getScheduler().cancelTask(taskID);
        }
    }
}
