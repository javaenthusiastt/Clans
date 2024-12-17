package me.liam.echoBoxClanSystem;
import me.liam.echoBoxClanSystem.admins.AdminCommand;
import me.liam.echoBoxClanSystem.admins.AdminListener;
import me.liam.echoBoxClanSystem.admins.AdminCompleter;
import me.liam.echoBoxClanSystem.api.PlaceholderSize;
import me.liam.echoBoxClanSystem.clanchest.ClanChestCommand;
import me.liam.echoBoxClanSystem.clanchest.ClanChestListener;
import me.liam.echoBoxClanSystem.commands.*;
import me.liam.echoBoxClanSystem.commands.clanchat.ClanChatCommand;
import me.liam.echoBoxClanSystem.commands.clanchat.ClanChatListener;
import me.liam.echoBoxClanSystem.commands.clanchat.ClanChatTabCompleter;
import me.liam.echoBoxClanSystem.configs.Config;
import me.liam.echoBoxClanSystem.data.DataManager;
import me.liam.echoBoxClanSystem.data.Keys;
import me.liam.echoBoxClanSystem.discord.DiscordAPI;
import me.liam.echoBoxClanSystem.gui.ColorGUI;
import me.liam.echoBoxClanSystem.gui.ClanGUI;
import me.liam.echoBoxClanSystem.handling.CooldownManager;
import me.liam.echoBoxClanSystem.helpers.Prefixes;
import me.liam.echoBoxClanSystem.listeners.*;
import me.liam.echoBoxClanSystem.api.PlaceholderName;
import me.liam.echoBoxClanSystem.ranks.RankListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class Main extends JavaPlugin {

    private DataManager dataManager;
    private static Main instance;
    private final CooldownManager cooldownManager = new CooldownManager();
    private DiscordAPI discordAPI;

    @Override
    public void onEnable() {
        try {
            instance = this;
            Log("Clans is enabling...");
            Log("Checking for keys...");
            if(Keys.isValidKey(Main.getInstance().getConfig().getString("key"))) {
                Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "[EchoBoxClanSystem] Running Clans "+this.getDescription().getVersion()+" on version " +getServer().getBukkitVersion()+ " (UN-LICENSED VERSION)");
            }else{
                Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[EchoBoxClanSystem] Running Clans "+this.getDescription().getVersion()+" on version " +getServer().getBukkitVersion()+ " (LICENSED VERSION)");
            }
            Config.initialize(this);
            data();
            register();
            eula();
            Log("Starting Eula Check...");
            Log("Clans is now on!");
            if(Config.isDiscordEnabled()){
                discordAPI = new DiscordAPI();
            }
        } catch (Exception e) {
            Log("Error during plugin enable: " + e.getMessage());
        }
    }

    @Override
    public void onDisable() {
        Log("Disabling Clans...");
        if (dataManager != null) {
            dataManager.reloadData();
            dataManager.saveClans();
            Log("Clans data saved.");
        }

        Log("Disabled clans.");

        if(Config.isDiscordEnabled()){
            if (discordAPI != null && discordAPI.getJDA() != null) {
                discordAPI.getJDA().shutdown();
            }
        }

    }

    private void data() {
        try {
            Log("Initializing data...");
            dataManager = new DataManager(this);
            dataManager.setup();
            Log("Fetching data...");
            Log("Data initialization completed.");
        } catch (Exception e) {
            Log("Error initializing data: " + e.getMessage());
        }
    }

    private void register() {
        try {
            Log("Registering commands, listeners, and PlaceholderAPI Interigation if enabled in Config.YML...");

            if(Config.isPlaceholderAPIEnabled()){
                if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                    new PlaceholderName(dataManager).register();
                    new PlaceholderSize(dataManager).register();
                } else {
                    getLogger().warning("PlaceholderAPI not found. Please download it.");
                }
            }

            ClanChatCommand clanChatCommand = new ClanChatCommand(dataManager);

            Log("Registering commands...");
            Objects.requireNonNull(getCommand("claninfo")).setExecutor(new ClanInfoCommand(dataManager));
            Objects.requireNonNull(getCommand("clan")).setExecutor(new ClanCommand(new ClanGUI(dataManager), dataManager));
            Objects.requireNonNull(getCommand("clanchat")).setExecutor(clanChatCommand);
            Objects.requireNonNull(getCommand("clanchat")).setTabCompleter(new ClanChatTabCompleter());
            Objects.requireNonNull(getCommand("claninvite")).setExecutor(new ClanInviteCommand(dataManager));
            Objects.requireNonNull(getCommand("clanaccept")).setExecutor(new ClanAcceptCommand(dataManager));
            Objects.requireNonNull(getCommand("clanchest")).setExecutor(new ClanChestCommand(dataManager, this));

            Log("Registering listeners...");
            ClanDeathListeners clanDeathListeners = new ClanDeathListeners(dataManager);
            AdminCommand adminCommand = new AdminCommand(dataManager, clanDeathListeners);
            AdminListener adminListener = new AdminListener();

            Objects.requireNonNull(getCommand("clanadmin")).setExecutor(adminCommand);

            getServer().getPluginManager().registerEvents(adminListener, this);
            Objects.requireNonNull(getCommand("clanadmin")).setTabCompleter(new AdminCompleter());

            ColorGUI colorGUI = new ColorGUI(dataManager, this);

            getServer().getPluginManager().registerEvents(clanDeathListeners, this);
            getServer().getPluginManager().registerEvents(new ClanListenerGUI(this, colorGUI, new ClanGUI(dataManager), adminCommand), this);
            getServer().getPluginManager().registerEvents(new ClanLeaderboardListenerGUI(), this);
            getServer().getPluginManager().registerEvents(new ClanMembersListenerGUI(dataManager, this), this);
            getServer().getPluginManager().registerEvents(new ClanChestListener(dataManager), this);
            getServer().getPluginManager().registerEvents(new RankListener(dataManager), this);
            getServer().getPluginManager().registerEvents(new ClanChatListener(dataManager, clanChatCommand), this);
            Log("Success.");

        } catch (Exception e) {
            getLogger().severe("Error during registration: " + e.getMessage());
        }
    }

    private void Log(String message) {
        if (Config.isDebugEnabled()) {
            getLogger().info("[Clans Debug] "+ this.getDescription().getVersion()+"] "+ message);
        }
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public CooldownManager getCooldownManager() {
        return cooldownManager;
    }

    public static Main getInstance() {
        return instance;
    }

    public void eula(){
        new BukkitRunnable()
        {
            @Override
            public void run() {
                if (!Config.isEulaChecked()) {
                    for(Player player : Bukkit.getOnlinePlayers()){
                        if(player.isOp()){
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + "&cClans plugin is shutting down."));
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + "&cEULA is set to false in config.yml!"));
                        }
                    }
                    getLogger().severe("========================================");
                    getLogger().severe(" Clans plugin has been disabled.        ");
                    getLogger().severe(" Reason: EULA not accepted.             ");
                    getLogger().severe(" Please set 'eula: true' in the config. ");
                    getLogger().severe("========================================");
                    getServer().getPluginManager().disablePlugin(Main.getInstance());
                }
                dataManager.reloadData();
            }
        }.runTaskLater(this, 120L);
    }
}
