package xyz.pokepalooza.craftsteamg.discord.punishmentlogger.Listeners;

import net.dv8tion.jda.core.EmbedBuilder;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.living.humanoid.player.KickPlayerEvent;
import org.spongepowered.api.event.filter.cause.Root;
import xyz.pokepalooza.craftsteamg.discord.punishmentlogger.Config;
import xyz.pokepalooza.craftsteamg.discord.punishmentlogger.PunishmentLogger;

import java.awt.*;
import java.time.Instant;

public class KickListener {

    @Listener
    public void onKickEvent(KickPlayerEvent event, @Root CommandSource source){
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(event.getTargetEntity().getName() + " was kicked!")
                .addField("Kicker", source.getName(), true)
                .setColor(Color.MAGENTA)
                .setTimestamp(Instant.now())
                .setDescription("Reason:" + event.getMessage().toPlain());
        PunishmentLogger.jda.getTextChannelById(Config.getChannelID()).sendMessage(eb.build()).queue();
    }
}
