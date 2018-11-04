package xyz.pokepalooza.craftsteamg.discord.punishmentlogger;

import com.google.inject.Inject;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import xyz.pokepalooza.craftsteamg.discord.punishmentlogger.Listeners.BanListener;
import xyz.pokepalooza.craftsteamg.discord.punishmentlogger.Listeners.CommandListener;
import xyz.pokepalooza.craftsteamg.discord.punishmentlogger.Listeners.JailListener;
import xyz.pokepalooza.craftsteamg.discord.punishmentlogger.Listeners.KickListener;

import javax.security.auth.login.LoginException;

@Plugin(
        id = "punishmentlogger",
        name = "PunishmentLogger",
        description = "Punishment Logger for Discord",
        authors = {
                "CraftSteamG"
        }
)
public class PunishmentLogger {

    @Inject
    private Logger logger;

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
    }


    @Inject
    public PunishmentLogger(PluginContainer container) {
        PunishmentLogger.instance = this;
        PunishmentLogger.container = container;
        logger = container.getLogger();
    }

    @Listener
    public void onPreInit(GamePreInitializationEvent event){
        Config.load();
        if(!Config.getBotToken().isEmpty()) {
            jda = createClient(Config.getBotToken());
        }
        Sponge.getEventManager().registerListeners(this, new CommandListener());
        Sponge.getEventManager().registerListeners(this, new BanListener());
        Sponge.getEventManager().registerListeners(this, new KickListener());
        Sponge.getEventManager().registerListeners(this, new JailListener());
    }

    public Logger getLogger() {
        return logger;
    }

    @Listener
    public void onReload(GameReloadEvent event){
        Config.load();
    }

    public static JDA jda;
    public static PunishmentLogger instance;
    public static PluginContainer container;


    public static JDA createClient(String token){
        JDABuilder builder = new JDABuilder();

        try{
            return builder.setToken(token)
                    .build();
        }catch (LoginException e){
            return null;
        }
    }

}
