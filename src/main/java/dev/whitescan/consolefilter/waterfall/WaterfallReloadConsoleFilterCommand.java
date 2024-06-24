package dev.whitescan.consolefilter.waterfall;

import dev.whitescan.consolefilter.share.ConsoleFilterShare;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;

/**
 * Reload command for Waterfall.
 *
 * @author Whitescan
 * @since 1.0.0
 */
public class WaterfallReloadConsoleFilterCommand extends Command implements TabExecutor {

    private final WaterfallConsoleFilter consoleFilter;

    public WaterfallReloadConsoleFilterCommand(WaterfallConsoleFilter consoleFilter) {
        super("breloadconsolefilter", ConsoleFilterShare.COMMAND_RELOAD, new String[]{});
        this.consoleFilter = consoleFilter;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (!sender.hasPermission(ConsoleFilterShare.COMMAND_RELOAD)) {
            sender.sendMessage(consoleFilter.getNoPermissionMessage());
            return;
        }

        consoleFilter.loadConfigs();
        sender.sendMessage(consoleFilter.getConfigReloadedMessage());

    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        return new ArrayList<String>();
    }

}
