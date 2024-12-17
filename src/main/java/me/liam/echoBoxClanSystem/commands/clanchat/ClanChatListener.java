package me.liam.echoBoxClanSystem.commands.clanchat;

import me.liam.echoBoxClanSystem.data.Clan;
import me.liam.echoBoxClanSystem.data.DataManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class ClanChatListener implements Listener {

    private final DataManager dataManager;
    private final ClanChatCommand clanChatCommand;

    public ClanChatListener(DataManager dataManager, ClanChatCommand clanChatCommand) {
        this.dataManager = dataManager;
        this.clanChatCommand = clanChatCommand;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        if (clanChatCommand.isInClanChatMode(playerUUID)) {
            event.setCancelled(true);

            String clanName = clanChatCommand.getPlayerClanName(player);
            if (clanName != null) {
                Clan clan = dataManager.getClans().get(clanName);
                if (clan != null) {
                    clanChatCommand.sendClanMessage(clan, player, event.getMessage(), clanName);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        UUID playerUUID = event.getPlayer().getUniqueId();

        if (clanChatCommand.isInClanChatMode(playerUUID)) {
            clanChatCommand.clanChatToggled.put(playerUUID, false);
        }
    }
}
