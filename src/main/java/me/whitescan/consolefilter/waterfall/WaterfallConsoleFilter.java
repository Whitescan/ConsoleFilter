package me.whitescan.consolefilter.waterfall;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import me.whitescan.consolefilter.LogFilter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

/**
 * 
 * @author Whitescan
 *
 */
public class WaterfallConsoleFilter extends Plugin {

	// Startup

	private File configFile;
	private Configuration config;

	// Config

	private TextComponent noPermissionMessage;
	private TextComponent configReloadedMessage;

	private int keepLogs;

	private List<String> filter;

	@Override
	public void onEnable() {

		loadConfigs();

		registerCommands();

		((Logger) LogManager.getRootLogger()).addFilter(new LogFilter(filter));

		getLogger().info("Loaded successfully! Console will now be filtered.");

		if (keepLogs > 0)
			cleanupLogs();

	}

	public void loadConfigs() {

		try {

			if (!getDataFolder().exists()) {
				getLogger().warning("Plugin datafolder does not exist, creating it...");
				getDataFolder().mkdir();
			}

			this.configFile = new File(getDataFolder(), "config.yml");

			if (!configFile.exists()) {
				getLogger().warning("Config file not found, creating default...");
				InputStream in = getResourceAsStream("config.yml");
				Files.copy(in, configFile.toPath());
			}

			this.config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
			saveConfig();

			String noPermissionText = ChatColor.translateAlternateColorCodes('&', getConfig().getString("config.no-permission-message"));
			String configReloadedText = ChatColor.translateAlternateColorCodes('&',
					getConfig().getString("config.config-reloaded-message"));
			this.noPermissionMessage = new TextComponent(noPermissionText);
			this.configReloadedMessage = new TextComponent(configReloadedText);

			this.keepLogs = getConfig().getInt("config.keep-logs", 7) * 24 * 60 * 60 * 1000;

			this.filter = getConfig().getStringList("config.filter");

		} catch (IOException e) {
			getLogger().severe("Failed to load configurations! Your config may be broken. "
					+ "Remember that some characters like tabs are not allowed in .yml files! "
					+ "You can try fixing your current config or delete it to generate the default config.");
			e.printStackTrace();
		}

	}

	private void registerCommands() {
		getProxy().getPluginManager().registerCommand(this, new WaterfallReloadConsoleFilterCommand(this));
	}

	private void cleanupLogs() {

		File logDir = new File("logs");

		int deletedFiles = 0;

		if (logDir.exists()) {

			for (File file : logDir.listFiles()) {

				long diff = new Date().getTime() - file.lastModified();

				if (diff > keepLogs) {
					file.delete();
					deletedFiles++;
				}

			}

			if (deletedFiles > 0) {
				getLogger().info("Log cleanup completed. Deleted " + deletedFiles + " logs old than the configured amount of days.");
				return;
			}

		}

		getLogger().info("No logs have been purged...");

	}

	@Override
	public void onDisable() {
		getLogger().info("Disabled!");
	}

	private void saveConfig() {
		try {
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Configuration getConfig() {
		return config;
	}

	public TextComponent getNoPermissionMessage() {
		return noPermissionMessage;
	}

	public TextComponent getConfigReloadedMessage() {
		return configReloadedMessage;
	}

	public List<String> getFilter() {
		return filter;
	}

}
