package me.liam.echoBoxClanSystem.data;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DataManager {

    private final Map<String, Clan> clans = new HashMap<>();
    private final Plugin plugin;
    private File dataFile;
    private FileConfiguration dataConfig;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public DataManager(Plugin plugin) {
        this.plugin = plugin;
    }

    public Map<String, Clan> getClans() {
        return clans;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public void setup() {
        File dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists() && !dataFolder.mkdirs()) {
            plugin.getLogger().warning("Failed to create data folder.");
            return;
        }

        File chestsFolder = new File(dataFolder, "clanchests");
        if (!chestsFolder.exists() && !chestsFolder.mkdirs()) {
            plugin.getLogger().warning("Failed to create clanchests folder.");
        }


        dataFile = new File(dataFolder, "data.yml");
        if (!dataFile.exists()) {
            try {
                if (dataFile.createNewFile()) {
                    plugin.getLogger().info("Created a new empty data.yml file.");
                }
            } catch (IOException e) {
                plugin.getLogger().severe("Error creating data.yml file: " + e.getMessage());
            }
        }

        dataConfig = YamlConfiguration.loadConfiguration(dataFile);

        ensureConfigSections();

        loadClans();
    }

    public void resetClanPoints() {
        if (!dataConfig.contains("clans")) {
            plugin.getLogger().info("No clans found to reset in data.yml.");
            return;
        }

        ConfigurationSection clansSection = dataConfig.getConfigurationSection("clans");

        assert clansSection != null;
        for (String clanName : clansSection.getKeys(false)) {
            dataConfig.set("clans." + clanName + ".points", 1);

            ConfigurationSection playerPointsSection = clansSection.getConfigurationSection(clanName + ".playerPoints");
            if (playerPointsSection != null) {
                for (String playerUUID : playerPointsSection.getKeys(false)) {
                    playerPointsSection.set(playerUUID, 0);
                }
            }
        }

        try {
            dataConfig.save(dataFile);
            plugin.getLogger().info("Successfully reset all clan points to 0.");
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save data.yml after resetting clan points: " + e.getMessage());
        }

        for (Map.Entry<String, Clan> entry : clans.entrySet()) {
            Clan clan = entry.getValue();
            clan.setPoints(1);

            Map<UUID, Integer> playerPointsMap = clan.getPlayerPoints();
            if (playerPointsMap != null) {
                playerPointsMap.replaceAll((uuid, oldValue) -> 1);
            }
        }
    }

    public void ensureConfigSections() {
        if (dataConfig.contains("clans")) {
            for (String key : Objects.requireNonNull(dataConfig.getConfigurationSection("clans")).getKeys(false)) {
                if (!dataConfig.contains("clans." + key + ".admins")) {
                    dataConfig.set("clans." + key + ".admins", new ArrayList<>());
                }
                if (!dataConfig.contains("clans." + key + ".coOwners")) {
                    dataConfig.set("clans." + key + ".coOwners", new ArrayList<>());
                }
                if (!dataConfig.contains("clans." + key + ".points")) {
                    dataConfig.set("clans." + key + ".points", 0);
                }
                if (!dataConfig.contains("clans." + key + ".playerPoints")) {
                    dataConfig.set("clans." + key + ".playerPoints", new HashMap<>());
                }
                if (!dataConfig.contains("clans." + key + ".color")) {
                    dataConfig.set("clans." + key + ".color", ChatColor.WHITE.name());
                }
                if (!dataConfig.contains("clans." + key + ".friendlyFire")) {
                    dataConfig.set("clans." + key + ".friendlyFire", true);
                }
            }
            if (!dataConfig.contains("playerPoints")) {
                dataConfig.set("playerPoints", new HashMap<>());
            }

            try {
                dataConfig.save(dataFile);
            } catch (IOException e) {
                plugin.getLogger().severe("Failed to save data.yml after ensuring sections: " + e.getMessage());
            }
        }
    }

    public boolean isFriendlyFireEnabled(String clanName) {
        Clan clan = clans.get(clanName);
        return clan != null && clan.isFriendlyFireEnabled();
    }

    public void renameClan(String oldName, String newName) {
        if (clans.containsKey(newName) || !clans.containsKey(oldName)) {
            return;
        }

        Clan clan = clans.remove(oldName);
        if (clan != null) {

            clans.put(newName, clan);

            File chestsFolder = new File(plugin.getDataFolder(), "clanchests");
            File oldChestFile = new File(chestsFolder, oldName + "_chest.yml");
            File newChestFile = new File(chestsFolder, newName + "_chest.yml");

            if (oldChestFile.exists()) {
                if (oldChestFile.renameTo(newChestFile)) {
                    plugin.getLogger().info("Renamed chest file from " + oldName + "_chest.yml to " + newName + "_chest.yml.");
                } else {
                    plugin.getLogger().warning("Failed to rename chest file for clan: " + oldName);
                }
            }

            dataConfig.set("clans." + newName + ".owner", clan.getOwner().toString());

            List<String> memberStrings = new ArrayList<>();
            for (UUID member : clan.getMembers()) {
                memberStrings.add(member.toString());
            }
            dataConfig.set("clans." + newName + ".members", memberStrings);

            List<String> adminStrings = new ArrayList<>();
            for (UUID admin : clan.getAdmins()) {
                adminStrings.add(admin.toString());
            }
            dataConfig.set("clans." + newName + ".admins", adminStrings);

            List<String> coOwnerStrings = new ArrayList<>();
            for (UUID coOwner : clan.getCoOwners()) {
                coOwnerStrings.add(coOwner.toString());
            }
            dataConfig.set("clans." + newName + ".coOwners", coOwnerStrings);

            dataConfig.set("clans." + newName + ".clanDate", sdf.format(clan.getClanDate()));
            dataConfig.set("clans." + newName + ".points", clan.getPoints());
            dataConfig.set("clans." + newName + ".friendlyFire", clan.isFriendlyFireEnabled());

            if (clan.getLogo() != null) {
                dataConfig.set("clans." + newName + ".logo", clan.getLogo().serialize());
            } else {
                dataConfig.set("clans." + newName + ".logo", null);
            }

            dataConfig.set("clans." + newName + ".playerPoints", convertPointsToMap(clan.getPlayerPoints()));

            dataConfig.set("clans." + oldName, null);

            try {
                dataConfig.save(dataFile);
                plugin.getLogger().info("Clan renamed from " + oldName + " to " + newName);
            } catch (IOException e) {
                plugin.getLogger().severe("Error saving data.yml after renaming clan: " + e.getMessage());
            }
        }
    }

    public void saveClans() {
        if (dataConfig.contains("clans")) {
            dataConfig.set("clans", null);
        }

        for (Map.Entry<String, Clan> entry : clans.entrySet()) {
            String clanName = entry.getKey();
            Clan clan = entry.getValue();

            dataConfig.set("clans." + clanName + ".owner", clan.getOwner().toString());

            List<String> memberStrings = new ArrayList<>(new HashSet<>(convertUUIDListToStringList(clan.getMembers())));
            dataConfig.set("clans." + clanName + ".members", memberStrings);

            List<String> adminStrings = new ArrayList<>(new HashSet<>(convertUUIDListToStringList(clan.getAdmins())));
            dataConfig.set("clans." + clanName + ".admins", adminStrings);

            List<String> coOwnerStrings = new ArrayList<>(new HashSet<>(convertUUIDListToStringList(clan.getCoOwners())));
            dataConfig.set("clans." + clanName + ".coOwners", coOwnerStrings);

            dataConfig.set("clans." + clanName + ".clanDate", sdf.format(clan.getClanDate()));
            dataConfig.set("clans." + clanName + ".points", clan.getPoints());

            ItemStack logo = clan.getLogo();
            if (logo != null) {
                logo.setAmount(1);
                dataConfig.set("clans." + clanName + ".logo", logo.serialize());
            } else {
                dataConfig.set("clans." + clanName + ".logo", null);
            }

            dataConfig.set("clans." + clanName + ".color", clan.getColor().name());
            dataConfig.set("clans." + clanName + ".playerPoints", convertPointsToMap(clan.getPlayerPoints()));
            dataConfig.set("clans." + clanName + ".friendlyFire", clan.isFriendlyFireEnabled());
        }

        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save data to data.yml: " + e.getMessage());
        }
    }

    private List<String> convertUUIDListToStringList(List<UUID> uuidList) {
        List<String> stringList = new ArrayList<>();
        for (UUID uuid : uuidList) {
            stringList.add(uuid.toString());
        }
        return stringList;
    }

    public boolean deleteClan(String clanName, UUID playerUUID) {
        Clan clan = clans.get(clanName);
        if (clan != null && clan.getOwner().equals(playerUUID)) {
            plugin.getLogger().info("Deleting clan with the name of: " + clanName);
            clans.remove(clanName);
            saveClans();
            return true;
        }
        return false;
    }

    private void loadClans() {
        if (dataConfig.contains("clans")) {
            ConfigurationSection clansSection = dataConfig.getConfigurationSection("clans");

            assert clansSection != null;
            for (String key : clansSection.getKeys(false)) {
                try {
                    UUID owner = UUID.fromString(Objects.requireNonNull(dataConfig.getString("clans." + key + ".owner")));
                    List<String> memberStrings = dataConfig.getStringList("clans." + key + ".members");
                    List<UUID> members = new ArrayList<>();
                    for (String memberString : memberStrings) {
                        members.add(UUID.fromString(memberString));
                    }

                    List<String> adminStrings = dataConfig.getStringList("clans." + key + ".admins");
                    List<UUID> admins = new ArrayList<>();
                    for (String adminString : adminStrings) {
                        admins.add(UUID.fromString(adminString));
                    }

                    List<String> coOwnerStrings = dataConfig.getStringList("clans." + key + ".coOwners");
                    List<UUID> coOwners = new ArrayList<>();
                    for (String coOwnerString : coOwnerStrings) {
                        coOwners.add(UUID.fromString(coOwnerString));
                    }

                    Date clanDate;
                    try {
                        clanDate = sdf.parse(dataConfig.getString("clans." + key + ".clanDate"));
                    } catch (ParseException e) {
                        clanDate = new Date();
                    }

                    int points = dataConfig.getInt("clans." + key + ".points", 0);

                    Clan clan = new Clan(owner, members, admins, coOwners, clanDate);
                    clan.setPoints(points);

                    Object logoObj = dataConfig.get("clans." + key + ".logo");
                    ItemStack logo = null;
                    if (logoObj instanceof Map) {
                        try {
                            logo = ItemStack.deserialize((Map<String, Object>) logoObj);
                            logo.setAmount(1);
                            ItemMeta meta = logo.getItemMeta();
                            if (meta != null) {
                                meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                                meta.addItemFlags(ItemFlag.HIDE_ARMOR_TRIM);
                                meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
                                logo.setItemMeta(meta);
                            }
                        } catch (Exception e) {
                            plugin.getLogger().warning("Error with logo loading.");
                        }
                    } else if (logoObj instanceof MemorySection) {
                        try {
                            Map<String, Object> logoMap = ((MemorySection) logoObj).getValues(false);
                            logo = ItemStack.deserialize(logoMap);

                            ItemMeta meta = logo.getItemMeta();
                            if (meta != null) {
                                meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                                meta.addItemFlags(ItemFlag.HIDE_ARMOR_TRIM);
                                meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
                                logo.setItemMeta(meta);
                            }
                        } catch (Exception e) {
                            plugin.getLogger().warning("Error with logo loading.");
                        }
                    }
                    clan.setLogo(logo);

                    String colorString = dataConfig.getString("clans." + key + ".color", "WHITE");
                    ChatColor color;
                    try {
                        color = ChatColor.valueOf(colorString.toUpperCase());
                    } catch (IllegalArgumentException e) {
                        color = ChatColor.WHITE;
                    }
                    clan.setColor(color);

                    clan.setPlayerPoints(convertMapToPoints(dataConfig.getConfigurationSection("clans." + key + ".playerPoints")));

                    boolean friendlyFire = dataConfig.getBoolean("clans." + key + ".friendlyFire", true);
                    clan.setFriendlyFire(friendlyFire);

                    clans.put(key, clan);
                } catch (Exception e) {
                    plugin.getLogger().severe("Failed to load clan: " + key + " due to: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } else {
            plugin.getLogger().info("No clans found in data.yml.");
        }
    }

    private Map<UUID, Integer> convertMapToPoints(ConfigurationSection section) {
        Map<UUID, Integer> points = new HashMap<>();
        if (section != null) {
            for (String key : section.getKeys(false)) {
                UUID uuid = UUID.fromString(key);
                int value = section.getInt(key);
                points.put(uuid, value);
            }
        }
        return points;
    }

    private Map<String, Integer> convertPointsToMap(Map<UUID, Integer> points) {
        Map<String, Integer> map = new HashMap<>();
        for (Map.Entry<UUID, Integer> entry : points.entrySet()) {
            map.put(entry.getKey().toString(), entry.getValue());
        }
        return map;
    }

    public List<String> getPlayerInvitations(UUID playerUUID) {
        List<String> invitations = new ArrayList<>();
        for (Map.Entry<String, Clan> entry : clans.entrySet()) {
            Clan clan = entry.getValue();
            if (clan.hasInvitation(playerUUID)) {
                invitations.add(entry.getKey());
            }
        }
        return invitations;
    }

    public void saveInventory(String clanName, ItemStack[] contents) {
        File chestsFolder = new File(plugin.getDataFolder(), "clanchests");
        if (!chestsFolder.exists() && !chestsFolder.mkdirs()) {
            plugin.getLogger().warning("Failed to create clanchests folder.");
            return;
        }

        File file = new File(chestsFolder, clanName + "_chest.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        for (int i = 0; i < contents.length; i++) {
            config.set("chest." + i, contents[i]);
        }

        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save chest for clan " + clanName + ": " + e.getMessage());
        }
    }

    public ItemStack[] loadInventory(String clanName) {
        File chestsFolder = new File(plugin.getDataFolder(), "clanchests");
        if (!chestsFolder.exists()) {
            plugin.getLogger().warning("Clanchests folder does not exist.");
            return null;
        }

        File file = new File(chestsFolder, clanName + "_chest.yml");
        if (!file.exists()) {
            plugin.getLogger().info("Chest file for clan " + clanName + " does not exist.");
            return null;
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        ItemStack[] contents = new ItemStack[27];
        for (int i = 0; i < contents.length; i++) {
            contents[i] = config.getItemStack("chest." + i);
        }
        return contents;
    }

    public String getPlayerClanName(Player player) {
        UUID playerUUID = player.getUniqueId();
        return this.getClans().entrySet().stream()
                .filter(entry -> entry.getValue().getMembers().contains(playerUUID))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }

    public void deleteClanChestFile(String clanName) {
        File chestsFolder = new File(plugin.getDataFolder(), "clanchests");
        if (!chestsFolder.exists()) {
            plugin.getLogger().warning("Clanchests folder does not exist.");
            return;
        }

        File file = new File(chestsFolder, clanName + "_chest.yml");
        if (file.exists()) {
            if (file.delete()) {
                plugin.getLogger().info("Deleted chest file for clan: " + clanName);
            } else {
                plugin.getLogger().warning("Failed to delete chest file for clan: " + clanName);
            }

        }
    }

    public void reloadData() {
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
        clans.clear();
        loadClans();
    }
}