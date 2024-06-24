package dev.whitescan.consolefilter.paper;

import dev.whitescan.consolefilter.share.ConsoleFilterShare;
import lombok.AllArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Reload command for Paper.
 *
 * @author Whitescan
 * @since 1.0.0
 */
@AllArgsConstructor
public class PaperReloadConsoleFilterCommand implements CommandExecutor, TabExecutor {

    private final PaperConsoleFilter consoleFilter;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

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
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return new ArrayList<String>();
    }

}
