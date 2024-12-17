package me.liam.echoBoxClanSystem.discord;
import me.liam.echoBoxClanSystem.Main;
import me.liam.echoBoxClanSystem.helpers.DiscordHelper;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.requests.GatewayIntent;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DiscordAPI extends ListenerAdapter {

    private final String botChannelID;
    private final net.dv8tion.jda.api.JDA jda;
    private final Map<String, Long> cooldowns = new HashMap<>();

    public DiscordAPI() {
        String botToken = Main.getInstance().getConfig().getString("discord-bot-token");
        this.botChannelID = Main.getInstance().getConfig().getString("discord-channel-id");

        this.jda = JDABuilder.createDefault(botToken)
                .addEventListeners(this)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .build();

        registerSlashCommands(getJDA());
    }

    public void registerSlashCommands(JDA jda) {
        jda.upsertCommand("clantop", "View the Top 10 Clans").queue();
        jda.upsertCommand("claninfo", "View clan information of a player")
                .addOption(OptionType.STRING, "player", "The player's name", true)
                .queue();
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String commandName = event.getName();
        String userId = event.getUser().getId();
        long currentTime = System.currentTimeMillis();

        if (!commandName.equals("clantop") && !commandName.equals("claninfo")) {
            return;
        }

        if (!event.getChannel().getId().equals(botChannelID)) {
            String correctChannelMention = "<#" + botChannelID + ">";
            event.reply("You can only track clan stats at " + correctChannelMention + ".")
                    .setEphemeral(true)
                    .queue();
            return;
        }

        if (cooldowns.containsKey(userId)) {
            long lastUsed = cooldowns.get(userId);
            long COOLDOWN_TIME = 10000;
            if (currentTime - lastUsed < COOLDOWN_TIME) {
                long secondsRemaining = (COOLDOWN_TIME - (currentTime - lastUsed)) / 1000;
                event.reply("Please wait " + secondsRemaining + " seconds before using this again.").setEphemeral(true).queue();
                return;
            }
        }

        cooldowns.put(userId, currentTime);

        switch (commandName) {
            case "clantop" -> {
                MessageEmbed top10Embed = DiscordHelper.fetchTop10Embed();
                event.replyEmbeds(top10Embed).queue();
            }
            case "claninfo" -> {
                String playerName = Objects.requireNonNull(event.getOption("player")).getAsString();
                MessageEmbed clanInfoEmbed = DiscordHelper.fetchClanInfo(playerName);

                event.replyEmbeds(clanInfoEmbed).queue();
            }
        }
    }

    public net.dv8tion.jda.api.JDA getJDA() {
        return jda;
    }
}