package me.liam.echoBoxClanSystem.listeners;

import me.liam.echoBoxClanSystem.Main;
import me.liam.echoBoxClanSystem.data.Clan;
import me.liam.echoBoxClanSystem.data.DataManager;
import me.liam.echoBoxClanSystem.helpers.Messages;
import me.liam.echoBoxClanSystem.helpers.Prefixes;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Map;
import java.util.UUID;

public class ClanDeathListeners implements Listener {

    private final DataManager dataManager;

    public ClanDeathListeners(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player victim) || !(event.getDamager() instanceof Player damager)) return;

        if (!victim.getWorld().getName().equalsIgnoreCase(Main.getInstance().getConfig().getString("world_friendlyfire"))) return;

        String victimClanName = getClanName(victim);
        String damagerClanName = getClanName(damager);

        Clan victimClan = dataManager.getClans().get(victimClanName);
        Clan damagerClan = dataManager.getClans().get(damagerClanName);

        if (victimClanName != null && victimClanName.equals(damagerClanName)) {
            if(!damagerClan.isFriendlyFireEnabled() && !victimClan.isFriendlyFireEnabled()){
                event.setCancelled(true);
                Messages.sendPlayerFriendlyFire(damager);
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();

        if (killer == null) return;

        String victimClanName = getClanName(victim);
        String killerClanName = getClanName(killer);

        if (victimClanName != null && killerClanName != null && !victimClanName.equals(killerClanName)) {
            Clan victimClan = dataManager.getClans().get(victimClanName);
            Clan killerClan = dataManager.getClans().get(killerClanName);

            if (victimClan != null && killerClan != null) {

                int baseKillPoints = 5;
                int baseDeathPoints = 1;

                killerClan.addPoints(baseKillPoints);
                victimClan.subtractPoints(baseDeathPoints);

                if (victimClan.getPoints() < 0) {
                    victimClan.setPoints(0);
                }

                killerClan.addPlayerPoints(killer.getUniqueId(), baseKillPoints);

                dataManager.saveClans();

                String killerPointsMessage = "&a&l+ &a" + baseKillPoints + " &a"+killer.getName()+" &7killed &c"+victim.getName();
                String victimPointsMessage = "&c- " + baseDeathPoints + " &c("+victim.getName()+"&c)";

                notifyClan(victimClan, victimPointsMessage);
                notifyClan(killerClan, killerPointsMessage);
            }
        }
    }

    private String getClanName(Player player) {
        return dataManager.getClans().entrySet().stream()
                .filter(entry -> entry.getValue().getMembers().contains(player.getUniqueId()))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }

    private void notifyClan(Clan clan, String message) {
        for (UUID memberUUID : clan.getMembers()) {
            Player member = Bukkit.getPlayer(memberUUID);
            if (member != null) {
                String fullMessage = Prefixes.getPrefix() + message;
                member.sendMessage(ChatColor.translateAlternateColorCodes('&', fullMessage));
            }
        }
    }
}
