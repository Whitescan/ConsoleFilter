package dev.whitescan.consolefilter.paper;

import dev.whitescan.consolefilter.share.ConsoleFilterShare;
import dev.whitescan.consolefilter.share.LogFilter;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;

/**
 * ConsoleFilter Plugin for Paper.
 *
 * @author Whitescan
 * @since 1.0.0
 */
public class PaperConsoleFilter extends JavaPlugin {

    // Config

    @Getter
    private TextComponent noPermissionMessage;

    @Getter
    private TextComponent configReloadedMessage;

    private long keepLogs;

    @Getter
    private List<String> filter;

    @Override
    public void onEnable() {

        loadConfigs();

        getCommand("reloadconsolefilter").setExecutor(new PaperReloadConsoleFilterCommand(this));

        ((Logger) LogManager.getRootLogger()).addFilter(new LogFilter(getFilter()));

        getLogger().info("Loaded successfully! Console will now be filtered.");

        if (keepLogs > 0)
            ConsoleFilterShare.cleanupLogs(getLogger(), keepLogs);

    }

    public void loadConfigs() {

        File file = new File(getDataFolder(), "config.yml");

        if (!file.exists()) {
            getLogger().warning("No config file found! Creating default...");
            saveDefaultConfig();
        }

        String noPermissionText = ChatColor.translateAlternateColorCodes('&', getConfig().getString("config.no-permission-message", "&cYou lack the permission &7consolefilter.reload"));
        String configReloadedText = ChatColor.translateAlternateColorCodes('&', getConfig().getString("config.config-reloaded-message", "&cConsoleFilter &aConfig reloaded!"));
        this.noPermissionMessage = new TextComponent(noPermissionText);
        this.configReloadedMessage = new TextComponent(configReloadedText);

        this.keepLogs = getConfig().getInt("config.keep-logs", 7) * 24 * 60 * 60 * 1000;

        this.filter = getConfig().getStringList("config.filter");

    }

    @Override
    public void onDisable() {
        getLogger().info("Disabled!");
    }

}
