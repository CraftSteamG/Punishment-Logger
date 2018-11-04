package xyz.pokepalooza.craftsteamg.discord.punishmentlogger.Listeners;

import io.github.nucleuspowered.nucleus.api.events.NucleusJailEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.Root;
import xyz.pokepalooza.craftsteamg.discord.punishmentlogger.Config;
import xyz.pokepalooza.craftsteamg.discord.punishmentlogger.PunishmentLogger;

import java.awt.*;
import java.time.Instant;

public class JailListener {

    @Listener
    public void onJailEvent(NucleusJailEvent.Jailed event, @Root CommandSource source) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(event.getTargetUser().getName() + " has been jailed!")
                .setDescription("Reason:" + event.getReason().toPlain())
                .setColor(Color.RED)
                .addField("Jailer", source.getName(), true)
                .setTimestamp(Instant.now())
                .addField("Jail", event.getJailName(), true);
        if (event.getDuration().isPresent()) {
            eb.addField("Duration", String.valueOf(event.getDuration().get().toMinutes() + "Minutes"), true);
        }
        PunishmentLogger.jda.getTextChannelById(Config.getChannelID()).sendMessage(eb.build()).queue();
    }

    @Listener
    public void onUnjailEvent(NucleusJailEvent.Unjailed event, @Root CommandSource source) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(event.getTargetUser().getName() + " has been unjailed")
                .setColor(Color.green)
                .setTimestamp(Instant.now());
        PunishmentLogger.jda.getTextChannelById(Config.getChannelID()).sendMessage(eb.build()).queue();
    }
}
