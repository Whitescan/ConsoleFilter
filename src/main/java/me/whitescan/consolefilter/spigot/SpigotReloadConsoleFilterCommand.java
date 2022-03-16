package me.whitescan.consolefilter.spigot;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import me.whitescan.consolefilter.ConsoleFilterShare;

/**
 *
 * @author Whitescan
 *
 */
public class SpigotReloadConsoleFilterCommand implements CommandExecutor, TabExecutor {

	private SpigotConsoleFilter consoleFilter;

	public SpigotReloadConsoleFilterCommand(SpigotConsoleFilter consoleFilter) {
		this.consoleFilter = consoleFilter;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (!sender.hasPermission(ConsoleFilterShare.COMMAND_RELOAD)) {
			sender.spigot().sendMessage(consoleFilter.getNoPermissionMessage());
			return true;
		}

		consoleFilter.reloadConfig();
		consoleFilter.loadConfigs();
		sender.spigot().sendMessage(consoleFilter.getConfigReloadedMessage());
		return true;

	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		return new ArrayList<String>();
	}
}
