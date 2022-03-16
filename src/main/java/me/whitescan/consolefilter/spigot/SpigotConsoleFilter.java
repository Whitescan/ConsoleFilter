package me.whitescan.consolefilter.spigot;

import java.io.File;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.bukkit.plugin.java.JavaPlugin;

import me.whitescan.consolefilter.LogFilter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

/**
 *
 * @author Whitescan
 *
 */
public class SpigotConsoleFilter extends JavaPlugin {

	// Config

	private TextComponent noPermissionMessage;
	private TextComponent configReloadedMessage;

	private List<String> filter;

	@Override
	public void onEnable() {

		loadConfigs();

		registerCommands();

		((Logger) LogManager.getRootLogger()).addFilter(new LogFilter(getFilter()));

		getLogger().info("Loaded successfully! Console will now be filtered.");

	}

	public void loadConfigs() {

		File file = new File(getDataFolder(), "config.yml");

		if (!file.exists()) {
			getLogger().warning("No config file found! Creating default...");
			saveDefaultConfig();
		}

		String noPermissionText = ChatColor.translateAlternateColorCodes('&', getConfig().getString("config.no-permission-message"));
		String configReloadedText = ChatColor.translateAlternateColorCodes('&', getConfig().getString("config.config-reloaded-message"));
		this.noPermissionMessage = new TextComponent(noPermissionText);
		this.configReloadedMessage = new TextComponent(configReloadedText);

		this.filter = getConfig().getStringList("config.filter");

	}

	private void registerCommands() {
		getCommand("reloadconsolefilter").setExecutor(new SpigotReloadConsoleFilterCommand(this));
	}

	@Override
	public void onDisable() {
		getLogger().info("Disabled!");
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
