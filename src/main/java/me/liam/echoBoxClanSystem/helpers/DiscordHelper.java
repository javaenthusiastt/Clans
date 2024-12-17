package me.liam.echoBoxClanSystem.helpers;

import me.liam.echoBoxClanSystem.Main;
import me.liam.echoBoxClanSystem.data.Clan;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DiscordHelper {


    /**
     * Returns a formatted string of the top 10 clans by points.
     *
     * @return A string representing the top 10 clans.
     */


    public static MessageEmbed fetchTop10Embed() {
        List<Map.Entry<String, Clan>> sortedClans = Main.getInstance().getDataManager().getClans().entrySet().stream()
                .sorted((entry1, entry2) -> Integer.compare(entry2.getValue().getPoints(), entry1.getValue().getPoints()))
                .toList();

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(Main.getInstance().getConfig().getString("discord-top-10-header-text"));
        embedBuilder.setColor(Color.RED);
        embedBuilder.setFooter("Generated by Clans Plugin ● Top 10 Clans", "https://images.creativefabrica.com/products/previews/2023/10/27/XHTAI876A/2XMiRhukZa6S9QgDvlZQE2CFe5y-mobile.jpg");
        if(Licensed.isNotLicensed()){
            embedBuilder.setDescription("⚠️ This server is running an unlicensed version of the Clans plugin ⚠️");
            embedBuilder.addField(":octagonal_sign: Unlock all clan features with a key! :octagonal_sign:", "", true);

        }

        int rank = 1;
        for (int i = 0; i < 10; i++) {
            String rankText = String.format("%d", rank);
            String clanText;
            int points = 0;

            if (i < sortedClans.size()) {
                Map.Entry<String, Clan> entry = sortedClans.get(i);
                String clanName = entry.getKey();
                UUID ownerUUID = entry.getValue().getOwner();

                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(ownerUUID);
                String founderName = offlinePlayer.getName();

                if (founderName == null) {
                    founderName = "Unknown";
                }

                clanText = clanName + " ● Owner: " + founderName;
                points = entry.getValue().getPoints();
            } else {
                clanText = "N/A";
            }

            String formattedPoints = String.format("%,d", points);
            embedBuilder.addField(rankText + ". " + clanText, "Points: " + formattedPoints, false);
            rank++;
        }

        return embedBuilder.build();
    }

    public static MessageEmbed fetchClanInfo(String playerName) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        if(Licensed.isNotLicensed()){
            embedBuilder.addField(":octagonal_sign: Unlock all clan features with a key! :octagonal_sign:", "", true);
            embedBuilder.setDescription("⚠️ This server is running an unlicensed version of the Clans plugin ⚠️");
        }

        for (Map.Entry<String, Clan> entry : Main.getInstance().getDataManager().getClans().entrySet()) {
            Clan clan = entry.getValue();
            embedBuilder.setColor(clan.getJavaColor());
            UUID ownerUUID = clan.getOwner();
            OfflinePlayer owner = Bukkit.getOfflinePlayer(ownerUUID);

            if (owner.getName() != null && owner.getName().equalsIgnoreCase(playerName)) {
                embedBuilder.setTitle("Clan Information: " + entry.getKey());
                embedBuilder.addField("Player Lookup", playerName, false);
                embedBuilder.addField("Clan Owner", owner.getName(), true);
                embedBuilder.addField("Points", String.format("%,d", clan.getPoints()), true);
                embedBuilder.addField("Rank", "Owner", true);
                embedBuilder.addField("Friendly Fire", clan.friendlyFireStringVersion(), true);
                embedBuilder.addField("Clan Color", clan.getColorString(), false);
                embedBuilder.addField("Clan Date", clan.formatClanDate(), false);


                StringBuilder membersList = new StringBuilder();
                for (UUID memberUUID : clan.getMembers()) {
                    OfflinePlayer member = Bukkit.getOfflinePlayer(memberUUID);
                    if (member.getName() != null) {
                        membersList.append(member.getName()).append("\n");
                    }
                }
                embedBuilder.addField("Members", !membersList.isEmpty() ? membersList.toString() : "No members", false);
                embedBuilder.setFooter("Generated by Clans Plugin ● Clan Information", "https://images.creativefabrica.com/products/previews/2023/10/27/XHTAI876A/2XMiRhukZa6S9QgDvlZQE2CFe5y-mobile.jpg");
                return embedBuilder.build();
            }

            for (UUID memberUUID : clan.getMembers()) {
                OfflinePlayer member = Bukkit.getOfflinePlayer(memberUUID);
                if (member.getName() != null && member.getName().equalsIgnoreCase(playerName)) {
                    embedBuilder.setTitle("Clan Information: " + entry.getKey());
                    embedBuilder.addField("Player Lookup", playerName, false);
                    embedBuilder.addField("Clan Owner", owner.getName() != null ? owner.getName() : "Unknown", true);
                    embedBuilder.addField("Points", String.format("%,d", clan.getPoints()), true);
                    embedBuilder.addField("Rank", "Member", true);
                    embedBuilder.addField("Friendly Fire", clan.friendlyFireStringVersion(), true);
                    embedBuilder.addField("Clan Color", clan.getColorString(), false);
                    embedBuilder.addField("Clan Date", clan.formatClanDate(), false);

                    StringBuilder membersList = new StringBuilder();
                    for (UUID uuid : clan.getMembers()) {
                        OfflinePlayer otherMember = Bukkit.getOfflinePlayer(uuid);
                        if (otherMember.getName() != null) {
                            membersList.append(otherMember.getName()).append("\n");
                        }
                    }
                    embedBuilder.addField("Members", !membersList.isEmpty() ? membersList.toString() : "No members", false);
                    embedBuilder.setFooter("Generated by Clans Plugin ● Clan Information", "https://images.creativefabrica.com/products/previews/2023/10/27/XHTAI876A/2XMiRhukZa6S9QgDvlZQE2CFe5y-mobile.jpg");
                    return embedBuilder.build();
                }
            }
        }

        embedBuilder.setTitle("Clan Information");
        embedBuilder.setDescription("Player not found or not in a clan!");
        embedBuilder.setColor(Color.RED);
        embedBuilder.setFooter("Generated by Clans Plugin ● Clan Information", "https://images.creativefabrica.com/products/previews/2023/10/27/XHTAI876A/2XMiRhukZa6S9QgDvlZQE2CFe5y-mobile.jpg");
        return embedBuilder.build();
    }
}