package dev.whitescan.consolefilter.waterfall;

import dev.whitescan.consolefilter.share.ConsoleFilterShare;
import dev.whitescan.consolefilter.share.LogFilter;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;

/**
 * ConsoleFilter Plugin for Waterfall.
 *
 * @author Whitescan
 * @since 1.0.0
 */
public class WaterfallConsoleFilter extends Plugin {

    // Config

    @Getter
    private TextComponent noPermissionMessage;
    @Getter
    private TextComponent configReloadedMessage;

    private int keepLogs;

    @Getter
    private List<String> filter;

    @Override
    public void onEnable() {

        loadConfigs();

        getProxy().getPluginManager().registerCommand(this, new WaterfallReloadConsoleFilterCommand(this));

        ((Logger) LogManager.getRootLogger()).addFilter(new LogFilter(filter));

        getLogger().info("Loaded successfully! Console will now be filtered.");

        if (keepLogs > 0)
            ConsoleFilterShare.cleanupLogs(getLogger(), keepLogs);

    }

    public void loadConfigs() {

        try {

            if (!getDataFolder().exists()) {
                getLogger().warning("Plugin data folder does not exist, creating it...");
                getDataFolder().mkdir();
            }

            File configFile = new File(getDataFolder(), "config.yml");

            if (!configFile.exists()) {
                getLogger().warning("Config file not found, creating default...");
                InputStream in = getResourceAsStream("config.yml");
                Files.copy(in, configFile.toPath());
            }

            final Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
            try {
                ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, configFile);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String noPermissionText = ChatColor.translateAlternateColorCodes('&', config.getString("config.no-permission-message", "&cYou lack the permission &7consolefilter.reload"));
            String configReloadedText = ChatColor.translateAlternateColorCodes('&',
                    config.getString("config.config-reloaded-message", "&cConsoleFilter &aConfig reloaded!"));
            this.noPermissionMessage = new TextComponent(noPermissionText);
            this.configReloadedMessage = new TextComponent(configReloadedText);

            this.keepLogs = config.getInt("config.keep-logs", 7) * 24 * 60 * 60 * 1000;

            this.filter = config.getStringList("config.filter");

        } catch (IOException e) {
            getLogger().severe("Failed to load configurations! Your config may be broken. "
                    + "Remember that some characters like tabs are not allowed in .yml files! "
                    + "You can try fixing your current config or delete it to generate the default config.");
            e.printStackTrace();
        }

    }

    @Override
    public void onDisable() {
        getLogger().info("Disabled!");
    }

}
