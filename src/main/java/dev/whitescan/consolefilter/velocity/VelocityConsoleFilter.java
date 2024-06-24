package dev.whitescan.consolefilter.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.whitescan.consolefilter.share.ConsoleFilterShare;
import dev.whitescan.consolefilter.share.LogFilter;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Logger;

/**
 * ConsoleFilter Plugin for Waterfall.
 *
 * @author Whitescan
 * @since 1.0.0
 */

@Plugin(id = "consolefilter", name = "ConsoleFilter", version = "1.2.0",
        url = "https://github.com/Whitescan/ConsoleFilter", description = "A lightweight plugin to filter and hide console messages and remove old log files.", authors = {"Whitescan"})
public class VelocityConsoleFilter {

    private final Logger logger;

    // Config

    @Getter
    private String noPermissionMessage;

    @Getter
    private String configReloadedMessage;

    private long keepLogs;

    @Getter
    private List<String> filter;

    @Inject
    public VelocityConsoleFilter(ProxyServer proxy, Logger logger) {
        this.logger = logger;

        loadConfigs();

        CommandManager cm = proxy.getCommandManager();
        CommandMeta reloadCommandMeta = cm.metaBuilder("vreloadconsolefilter").aliases("breloadconsolefilter").plugin(this).build();
        proxy.getCommandManager().register(reloadCommandMeta, new VelocityReloadConsoleFilterCommand(this));

        ((org.apache.logging.log4j.core.Logger) LogManager.getRootLogger()).addFilter(new LogFilter(getFilter()));

        logger.info("Loaded successfully! Console will now be filtered.");

        if (keepLogs > 0)
            ConsoleFilterShare.cleanupLogs(logger, keepLogs);

    }

    @SneakyThrows
    public void loadConfigs() {

        final File configFile = new File("plugins/ConsoleFilter/config.yml");

        if (!configFile.getParentFile().exists())
            configFile.mkdirs();

        if (!configFile.exists()) {
            logger.warning("Config file not found, creating default...");
            InputStream in = VelocityConsoleFilter.class.getResourceAsStream("config.yml");
            Files.copy(in, configFile.toPath());
        }

        final YamlConfigurationLoader loader = YamlConfigurationLoader.builder().path(Path.of("config.yml")).build();

        CommentedConfigurationNode root;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException("An error occurred while loading configuration: " + configFile.getAbsolutePath());
        }

        final ConfigurationNode config = root.node("config");

        this.noPermissionMessage = config.node("no-permission-message").getString("&cYou lack the permission &7consolefilter.reload").replace("&", "§");
        this.configReloadedMessage = config.node("config-reloaded-message").getString("&cConsoleFilter &aConfig reloaded!").replace("&", "§");

        this.keepLogs = config.node("keep-logs").getLong(7) * 24 * 60 * 60 * 1000;

        this.filter = config.node("filter").getList(String.class);

    }

}
