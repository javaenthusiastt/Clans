package me.liam.echoBoxClanSystem.api;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.liam.echoBoxClanSystem.configs.Config;
import me.liam.echoBoxClanSystem.data.DataManager;
import me.liam.echoBoxClanSystem.data.Clan;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class PlaceholderName extends PlaceholderExpansion {

    private final DataManager dataManager;

    public PlaceholderName(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public @NotNull String getIdentifier() {
        return Config.getPlaceholderAPIIdentifier();
    }

    @Override
    public @NotNull String getAuthor() {
        return "Sorryplspls";
    }

    @Override
    public @NotNull String getVersion() {
        return "2.0.0";
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String identifier) {
        if (player == null) {
            return null;
        }

        if (identifier.equals(Config.getPlaceholderAPIFormatterName())) {
            String formattedClanName = format(player);
            return formattedClanName != null ? formattedClanName : "No Clan";
        }

        return null;
    }

    /**
     * uwu.
     *
     * @param player the player
     * @return format clan
     */


    public String format(Player player) {
        UUID playerUUID = player.getUniqueId();

        String playerClanName = dataManager.getClans().entrySet().stream()
                .filter(entry -> entry.getValue().getMembers().contains(playerUUID))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);

        if (playerClanName == null) {
            return ChatColor.translateAlternateColorCodes('&', "&fNo Clan");
        }

        List<Map.Entry<String, Clan>> sortedClans = new ArrayList<>(dataManager.getClans().entrySet());
        sortedClans.sort((a, b) -> Integer.compare(b.getValue().getPoints(), a.getValue().getPoints()));

        Clan playerClan = dataManager.getClans().get(playerClanName);
        ChatColor clanColor = playerClan.getColor() != null ? playerClan.getColor() : ChatColor.WHITE;

        String formattedClanName = playerClanName.substring(0, 1).toUpperCase() + playerClanName.substring(1).toLowerCase();

        for (int i = 0; i < sortedClans.size(); i++) {
            Map.Entry<String, Clan> entry = sortedClans.get(i);
            if (entry.getKey().equals(playerClanName)) {
                String rank = i < 3 ? " (#" + (i + 1) + ")" : "";
                return clanColor + "" + ChatColor.BOLD + formattedClanName + " Clan" + ChatColor.RESET + clanColor + rank;
            }
        }

        return clanColor + "" + ChatColor.BOLD + formattedClanName + " Clan";
    }
}
