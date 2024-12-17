package me.liam.echoBoxClanSystem.data;

import me.liam.echoBoxClanSystem.configs.Config;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class Clan {
    private UUID owner;
    private final List<UUID> members;
    private final List<UUID> admins;
    private final List<UUID> coOwners;
    private final Date clanDate;
    private int points;
    private ItemStack logo;
    private ChatColor color;
    private boolean friendlyFire;
    private Map<UUID, Integer> playerPoints;
    private final Set<UUID> invitedPlayers;
    public static final int MAX_CLAN_MEMBERS = Config.getMaxClanMembers();
    public static final int MAX_ADMIN_COUNT = Config.getMaxClanAdmins();
    public static final int MAX_COOWNER_COUNT = Config.getMaxClanCoOwners();
    public static final int MAX_CLAN_INVITES = Config.getMaxInvitesAtOnce();
    public Clan(UUID owner, List<UUID> members, List<UUID> admins, List<UUID> coOwners, Date clanDate) {
        this.owner = owner;
        this.coOwners = coOwners != null ? new ArrayList<>(coOwners) : new ArrayList<>();
        this.admins = admins != null ? new ArrayList<>(admins) : new ArrayList<>();
        this.members = members != null ? new ArrayList<>(members) : new ArrayList<>();
        this.logo = new ItemStack(Material.BARRIER);
        this.invitedPlayers = new HashSet<>();
        this.clanDate = clanDate != null ? clanDate : new Date();
        this.playerPoints = new HashMap<>();
        this.color = ChatColor.WHITE;
        this.friendlyFire = true;
    }
    public boolean isFriendlyFireEnabled() {
        return friendlyFire;
    }
    public String friendlyFireStringVersion() {
        if(friendlyFire){
            return "Activated";
        }else{
            return "Disabled";
        }
    }
    public void setFriendlyFire(boolean friendlyFire) {
        this.friendlyFire = friendlyFire;
    }
    public String getPlayerRank(UUID playerUUID) {
        if (owner.equals(playerUUID)) {
            return Config.getOwnerPrefix();
        } else if (admins.contains(playerUUID)) {
            return Config.getAdminPrefix();
        } else if (coOwners.contains(playerUUID)) {
            return Config.getCoOwnerPrefix();
        } else if (members.contains(playerUUID)) {
            return Config.getMemberPrefix();
        } else {
            return Config.getNoRankPrefix();
        }
    }
    public Map<UUID, Integer> getPlayerPoints() {
        return playerPoints;
    }
    public String getPlayerPointsAsString(UUID playerUUID) {
        Integer points = playerPoints.get(playerUUID);
        return points != null ? "" + points : "0";
    }
    public void addPlayerPoints(UUID playerUUID, int points) {
        playerPoints.put(playerUUID, playerPoints.getOrDefault(playerUUID, 0) + points);
    }
    public void setPlayerPoints(UUID playerUUID, int points) {
        playerPoints.put(playerUUID, points);
    }
    public int getPlayerPoints(UUID playerUUID) {
        return playerPoints.getOrDefault(playerUUID, 0);
    }
    public void setPlayerPoints(Map<UUID, Integer> playerPoints) {
        this.playerPoints = playerPoints;
    }
    public ChatColor getColor() {
        return color;
    }
    public String getColorString() {
        String colorCode = color.toString();
        return switch (colorCode) {
            case "§4" -> "Red";
            case "§1" -> "Dark Blue";
            case "§2" -> "Green";
            case "§3" -> "Aqua";
            case "§5" -> "Purple";
            case "§6" -> "Gold";
            case "§7" -> "Gray";
            case "§8" -> "Dark Gray";
            case "§9" -> "Blue";
            case "§f" -> "White";
            case "§e" -> "Yellow";
            case "§d" -> "Light Purple";
            case "§0" -> "Black";
            case "§a" -> "Light Green";
            case "§b" -> "Light Blue";
            case "§c" -> "Light Red";
            default -> "Unknown";
        };
    }
    public Color getJavaColor() {
        String colorCode = color.toString();
        return switch (colorCode) {
            case "§4", "§c" -> Color.RED;
            case "§1", "§9" -> Color.BLUE;
            case "§2" -> Color.GREEN;
            case "§3" -> Color.CYAN;
            case "§5" -> new Color(128, 0, 128);
            case "§6" -> Color.ORANGE;
            case "§7" -> Color.GRAY;
            case "§8" -> new Color(169, 169, 169);
            case "§f" -> Color.WHITE;
            case "§e" -> Color.YELLOW;
            case "§d" -> new Color(255, 0, 255);
            case "§a" -> new Color(144, 238, 144);
            case "§b" -> new Color(173, 216, 230);
            default -> Color.BLACK;
        };
    }
    public void setColor(ChatColor color) {
        this.color = color;
    }
    public void setOwner(UUID owner) {
        this.owner = owner;
    }
    public List<UUID> getMembers() {
        return members;
    }
    public List<UUID> getAdmins() {
        return admins;
    }
    public List<UUID> getCoOwners() {
        return coOwners;
    }
    public UUID getOwner() {
        return owner;
    }
    public Date getClanDate() {
        return clanDate;
    }
    public String formatClanDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(clanDate);
    }
    public ItemStack getLogo() {
        return logo;
    }
    public void setLogo(ItemStack logo) {
        this.logo = logo;
    }
    public int getPoints() {
        return points;
    }
    public void setPoints(int points) {
        this.points = points;
    }
    public void addPoints(int amount) {
        this.points += amount;
    }
    public void subtractPoints(int amount) {
        this.points -= amount;
    }
    public boolean isCoOwner(UUID playerUUID) {
        return coOwners.contains(playerUUID);
    }
    public void addCoOwner(UUID playerUUID) {
        if(coOwners.size() < MAX_COOWNER_COUNT) {
            coOwners.add(playerUUID);
        }
    }
    public boolean isAdmin(UUID playerUUID) {
        return admins.contains(playerUUID);
    }
    public void addAdmin(UUID playerUUID) {
        if (admins.size() < MAX_ADMIN_COUNT) {
            admins.add(playerUUID);
        }
    }
    public boolean isMember(UUID playerUUID) {
        return members.contains(playerUUID);
    }
    public boolean addMember(UUID playerUUID) {
        members.add(playerUUID);
        invitedPlayers.remove(playerUUID);
        return true;
    }
    public boolean removeMember(UUID playerUUID) {
        return members.remove(playerUUID);
    }
    public void removeCoOwner(UUID playerUUID){
        coOwners.remove(playerUUID);
    }
    public void removeAdmin(UUID playerUUID){
        admins.remove(playerUUID);
    }
    public boolean invitePlayer(UUID playerUUID) {
        if (!members.contains(playerUUID) && !invitedPlayers.contains(playerUUID)) {
            invitedPlayers.add(playerUUID);
            return true;
        }
        return false;
    }
    public boolean hasInvitation(UUID playerUUID) {
        return invitedPlayers.contains(playerUUID);
    }
    public void removeInvitation(UUID playerUUID) {
        invitedPlayers.remove(playerUUID);
    }
}
