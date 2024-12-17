package me.liam.echoBoxClanSystem.commands;
import me.liam.echoBoxClanSystem.Main;
import me.liam.echoBoxClanSystem.colors.Adventure;
import me.liam.echoBoxClanSystem.configs.Config;
import me.liam.echoBoxClanSystem.data.Clan;
import me.liam.echoBoxClanSystem.data.DataManager;
import me.liam.echoBoxClanSystem.helpers.Messages;
import me.liam.echoBoxClanSystem.helpers.Prefixes;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ClanInfoCommand implements CommandExecutor {

    private final DataManager dataManager;

    public ClanInfoCommand(DataManager dataManager) {
        this.dataManager = dataManager;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            Messages.sendInvalidExecutor(sender);
            return true;
        }

        if (args.length != 1) {
            Messages.sendTooLittleArguments(player);
            return true;
        }

        try {
            String arg = args[0];
            Clan clan = null;
            String clanName = null;

            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(arg);
            if (offlinePlayer.hasPlayedBefore()) {
                clanName = getPlayerClanName(offlinePlayer);
                if (clanName != null) {
                    clan = findClanByNameIgnoreCase(clanName);
                }
            }

            if (clan == null) {
                clanName = arg;
                clan = findClanByNameIgnoreCase(clanName);
            }

            if (clan == null) {
                Messages.sendNoClanFound(player);
                return true;
            }

            UUID ownerUUID = clan.getOwner();
            List<UUID> membersUUID = clan.getMembers();
            List<UUID> coOwnersUUID = clan.getCoOwners();
            List<UUID> adminsUUID = clan.getAdmins();

            String ownerName = getPlayerName(ownerUUID);
            if (ownerName == null) {
                ownerName = "Unknown";
            }

            String clanDate = new SimpleDateFormat("yyyy-MM-dd").format(clan.getClanDate());
            int clanPoints = clan.getPoints();

            StringBuilder memberListBuilder = new StringBuilder();
            String memberFormat = Main.getInstance().getConfig().getString("messages.clan_information_display_playerformat.messages.member-format");

            assert memberFormat != null;
            memberListBuilder.append(Adventure.parseToLegacy(memberFormat
                    .replace("{player_name}", ownerName)
                    .replace("{rank}", Config.getOwnerPrefix())
                    .replace("{points}", String.valueOf(clan.getPlayerPoints(ownerUUID))))
            ).append("\n");

            for (UUID coOwnerUUID : coOwnersUUID) {
                if (!coOwnerUUID.equals(ownerUUID)) {
                    String coOwnerName = getPlayerName(coOwnerUUID);
                    memberListBuilder.append(Adventure.parseToLegacy(memberFormat
                            .replace("{player_name}", coOwnerName)
                            .replace("{rank}", Config.getCoOwnerPrefix())
                            .replace("{points}", String.valueOf(clan.getPlayerPoints(coOwnerUUID))))
                    ).append("\n");
                }
            }

            for (UUID adminUUID : adminsUUID) {
                if (!adminUUID.equals(ownerUUID) && !coOwnersUUID.contains(adminUUID)) {
                    String adminName = getPlayerName(adminUUID);
                    memberListBuilder.append(Adventure.parseToLegacy(memberFormat
                            .replace("{player_name}", adminName)
                            .replace("{rank}", Config.getAdminPrefix())
                            .replace("{points}", String.valueOf(clan.getPlayerPoints(adminUUID))))
                    ).append("\n");
                }
            }

            for (UUID memberUUID : membersUUID) {
                if (!memberUUID.equals(ownerUUID) && !coOwnersUUID.contains(memberUUID) && !adminsUUID.contains(memberUUID)) {
                    String memberName = getPlayerName(memberUUID);
                    memberListBuilder.append(Adventure.parseToLegacy(memberFormat
                            .replace("{player_name}", memberName)
                            .replace("{rank}", Config.getMemberPrefix())
                            .replace("{points}", String.valueOf(clan.getPlayerPoints(memberUUID))))
                    ).append("\n");
                }
            }

            String memberList = memberListBuilder.toString();

            for (String line : Main.getInstance().getConfig().getStringList("messages.clan_information_display")) {
                line = Adventure.parseToLegacy(line);
                line = line.replace("{get_clan}", clanName.substring(0, 1).toUpperCase() + clanName.substring(1).toLowerCase())
                        .replace("{get_owner}", ownerName)
                        .replace("{get_date}", clanDate)
                        .replace("{get_points}", String.valueOf(clanPoints))
                        .replace("{fetchmembers}", memberList);

                player.sendMessage(Prefixes.getPrefix() + line);
            }

        } catch (Exception e) {
            Messages.sendError(player);
        }

        return true;
    }




    private String getPlayerName(UUID uuid) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
        return offlinePlayer.getName() != null ? offlinePlayer.getName() : "Unknown";
    }

    private Clan findClanByNameIgnoreCase(String name) {
        for (Map.Entry<String, Clan> entry : dataManager.getClans().entrySet()) {
            if (entry.getKey().equalsIgnoreCase(name)) {
                return entry.getValue();
            }
        }
        return null;
    }

    private String getPlayerClanName(OfflinePlayer player) {
        UUID playerUUID = player.getUniqueId();
        return dataManager.getClans().entrySet().stream()
                .filter(entry -> entry.getValue() != null && entry.getValue().getMembers().contains(playerUUID))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }


    private String letters(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }
}

