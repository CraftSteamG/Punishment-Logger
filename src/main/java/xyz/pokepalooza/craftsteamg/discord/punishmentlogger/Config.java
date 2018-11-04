package xyz.pokepalooza.craftsteamg.discord.punishmentlogger;

import com.mcsimonflash.sponge.teslalibs.configuration.ConfigHolder;
import com.mcsimonflash.sponge.teslalibs.configuration.ConfigurationException;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.util.TypeTokens;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Config {
    private static final Path directory = Sponge.getConfigManager().getPluginConfig(PunishmentLogger.instance).getDirectory();
    private static final Path configuration = directory.resolve("configuration");
    private static ConfigHolder config, storage;


    public static void read() {
        try {
            ConfigHolder root = ConfigHolder.of(HoconConfigurationLoader.builder().setPath(directory.resolve("configuration")).build());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void load(){
        try{
            PunishmentLogger.instance.getLogger().info("Loading Config...");
            Files.createDirectories(directory);
            Files.createDirectories(configuration);
            config = loadConfig(configuration, "configuration.conf", false);
            loadConfiguration();
            saveAll();
        }catch (IOException | ConfigurationException e){
            PunishmentLogger.instance.getLogger().error("Configuration loading has halted.");
        }
    }

    private static ConfigHolder loadConfig(Path dir, String name, boolean asset) throws IOException {
        Path path = dir.resolve(name);
        try {
            if (asset) {
                Sponge.getAssetManager().getAsset(PunishmentLogger.instance, name).get().copyToFile(path);
            } else if (Files.notExists(path)) {
                Files.createFile(path);
            }
            return ConfigHolder.of(HoconConfigurationLoader.builder().setDefaultOptions(ConfigurationOptions.defaults().setShouldCopyDefaults(true)).setPath(path).build());
        } catch (IOException e) {
            PunishmentLogger.instance.getLogger().error("&cAn unexpected error occurred initializing " + name + ": " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public static String getBotToken(){
        return config.getNode("bot", "constants", "token").getString();
    }
    public static String getChannelID() {return config.getNode("discord", "constants", "logChannelID").getString();}
    public static List<String> getCommandList(){
        try{
            return config.getNode("commands").getList(TypeTokens.STRING_TOKEN);
        }catch (ObjectMappingException e){
            PunishmentLogger.instance.getLogger().error("Invalid Config in COMMAND LIST");
            return null;
        }
    }


    private static void saveAll(){
        config.save();
    }

    private static void loadConfiguration(){
        config.getNode("bot", "constants", "token").getString("Token Here");
        config.getNode("discord", "constants", "logChannelID").getString("Channel ID Here");
        try {
            config.getNode("commands").getList(TypeTokens.STRING_TOKEN);
        }catch (ObjectMappingException e){
            PunishmentLogger.instance.getLogger().error("Error Loading Config.");
        }
    }
}
