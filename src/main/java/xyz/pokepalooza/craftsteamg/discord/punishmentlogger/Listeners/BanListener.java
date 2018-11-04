package xyz.pokepalooza.craftsteamg.discord.punishmentlogger.Listeners;

import net.dv8tion.jda.core.EmbedBuilder;
import org.spongepowered.api.Server;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.user.BanUserEvent;
import org.spongepowered.api.event.user.PardonUserEvent;
import org.spongepowered.api.text.Text;
import xyz.pokepalooza.craftsteamg.discord.punishmentlogger.Config;
import xyz.pokepalooza.craftsteamg.discord.punishmentlogger.PunishmentLogger;

import java.awt.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Optional;

public class BanListener {

    @Listener
    public void onBanEvent(BanUserEvent event) {
       if(event.getBan().getBanCommandSource().isPresent()) {
           CommandSource source = event.getBan().getBanCommandSource().get();
           if (source instanceof Player || source instanceof Server) {
               if (source.hasPermission("cmdlogger.ban")) {
                   String banner = source.getName();
                   String target = event.getTargetUser().getName();
                   EmbedBuilder eb = new EmbedBuilder();
                   eb.setTitle(target + " was banned!")
                           .setColor(Color.red)
                           .addField("Banned By", banner, false)
                           .setTimestamp(Instant.now());

                   if(event.getBan().getReason().isPresent()){
                       if(!event.getBan().getReason().get().toPlain().isEmpty()) {
                           eb.setDescription("Reason: " + event.getBan().getReason().get().toPlain());
                       }
                   }

                   if(event.getBan().getExpirationDate().isPresent()){
                       String expirationDate = new SimpleDateFormat("yyyy-MM-dd").format(Date.from(event.getBan().getExpirationDate().get()));
                       eb.addField("Expires", expirationDate, false);
                   } else {
                       eb.addField("Is Indefinite", "Yes", false);
                   }

                   PunishmentLogger.jda.getTextChannelById(Config.getChannelID()).sendMessage(eb.build()).queue();
               }
           }
       }
    }

    @Listener
    public void onUnbanEvent(PardonUserEvent event){
        String name = event.getTargetUser().getName();
        Optional<CommandSource> source = event.getBan().getBanCommandSource();
        EmbedBuilder eb = new EmbedBuilder();
        if(source.isPresent()) {
            eb.setTitle(name + " was unbanned!")
                    .setColor(Color.GREEN)
                    .setTimestamp(Instant.now())
                    .addField("Banner", source.get().getName(), true)
                    .addField("Original Reason", event.getBan().getReason().orElse(Text.of("No Reason Given.")).toPlain(), true);
            PunishmentLogger.jda.getTextChannelById(Config.getChannelID()).sendMessage(eb.build()).queue();
        }
    }
}
