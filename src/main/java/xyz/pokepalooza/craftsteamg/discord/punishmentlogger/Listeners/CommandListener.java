package xyz.pokepalooza.craftsteamg.discord.punishmentlogger.Listeners;

import net.dv8tion.jda.core.EmbedBuilder;
import org.spongepowered.api.Server;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.command.SendCommandEvent;
import org.spongepowered.api.event.filter.cause.Root;
import xyz.pokepalooza.craftsteamg.discord.punishmentlogger.Config;
import xyz.pokepalooza.craftsteamg.discord.punishmentlogger.PunishmentLogger;

import java.awt.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class CommandListener {


    public static List<String> excludedCommands = new ArrayList<String>();

    public CommandListener(){
        excludedCommands.add("ban");
        excludedCommands.add("tempban");
        excludedCommands.add("pardon");
        excludedCommands.add("unban");
        excludedCommands.add("kick");
        excludedCommands.add("jail");
        excludedCommands.add("mute");
    }

    @Listener
    public void onCommandEvent(SendCommandEvent event, @Root CommandSource source){
        String bareCommand = event.getCommand();
        String argsCommand = event.getArguments();
        if(source instanceof Player || source instanceof Server) {
            if(Config.getCommandList() != null) {
                for (String s : Config.getCommandList()) {
                    if(bareCommand.trim().equalsIgnoreCase(s) && source.hasPermission("cmdlogger." + s.toLowerCase())){
                        if(!excludedCommands.contains(bareCommand.toLowerCase().trim())) {
                            EmbedBuilder eb = new EmbedBuilder();

                            eb.setTitle("Command Logger")
                                    .addField("Sender", source.getName(), true)
                                    .addField("Command", bareCommand + " " + argsCommand, true)
                                    .setColor(Color.RED)
                                    .setTimestamp(Instant.now());
                            PunishmentLogger.jda.getTextChannelById(Config.getChannelID()).sendMessage(eb.build()).queue();

                            break;
                        }
                    }
                }
            }
        }
    }
}
