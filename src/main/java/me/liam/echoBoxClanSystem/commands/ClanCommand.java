package me.liam.echoBoxClanSystem.commands;

import me.liam.echoBoxClanSystem.configs.Config;
import me.liam.echoBoxClanSystem.configs.ReloadConfigClass;
import me.liam.echoBoxClanSystem.data.DataManager;
import me.liam.echoBoxClanSystem.data.Keys;
import me.liam.echoBoxClanSystem.gui.ClanGUI;
import me.liam.echoBoxClanSystem.helpers.Messages;
import me.liam.echoBoxClanSystem.helpers.Prefixes;
import me.liam.echoBoxClanSystem.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class ClanCommand implements CommandExecutor {

    private final ClanGUI clanGUI;
    private final DataManager dataManager;

    public ClanCommand(ClanGUI clanGUI, DataManager dataManager) {
        this.clanGUI = clanGUI;
        this.dataManager = dataManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (!(sender instanceof Player player)) {
            Messages.sendInvalidExecutor(sender);
            return true;
        }

        if (strings.length == 0) {
            clanGUI.openClanMenu(player);
            return true;
        } else if (strings[0].equalsIgnoreCase("reload") && strings.length == 1) {
            if (!(player.isOp())) {
                Messages.sendNoPermission(player);
                return true;
            }
            if (Config.isReloadConfigCmdEnabled()) {
                ReloadConfigClass.reload(player, Prefixes.getPrefix());
                Messages.sendDataRefreshed(player);
                dataManager.reloadData();
            } else {
                Messages.sendConfigDisabledFeature(player);
            }
        }
        return false;
    }
}
