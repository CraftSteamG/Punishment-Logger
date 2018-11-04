package xyz.pokepalooza.craftsteamg.discord.punishmentlogger.Listeners;

import io.github.nucleuspowered.nucleus.api.events.NucleusMuteEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.Root;
import xyz.pokepalooza.craftsteamg.discord.punishmentlogger.Config;
import xyz.pokepalooza.craftsteamg.discord.punishmentlogger.PunishmentLogger;

import java.awt.*;
import java.time.Instant;

public class MuteListener {

    @Listener
    public void onMuteEvent(NucleusMuteEvent.Muted event, @Root CommandSource source){
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(event.getTargetUser().getName() + " has been muted!")
                .setDescription("Reason:" + event.getReason().toPlain())
                .addField("Muter", source.getName(), true)
                .setTimestamp(Instant.now())
                .setColor(Color.RED);
        if(event.getDuration().isPresent()) eb.addField("Duration", String.valueOf(event.getDuration().get().toMinutes() + " Minutes"), true);
        PunishmentLogger.jda.getTextChannelById(Config.getChannelID()).sendMessage(eb.build()).queue();
    }

    @Listener
    public void onUnmuteEvent(NucleusMuteEvent.Unmuted event, @Root CommandSource source){
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(event.getTargetUser().getName() + " has been unmuted!")
                .setColor(Color.GREEN)
                .setTimestamp(Instant.now())
                .addField("Original Muter", source.getName(), true);
        PunishmentLogger.jda.getTextChannelById(Config.getChannelID()).sendMessage(eb.build()).queue();
    }

}
