package dev.whitescan.consolefilter.velocity;

import com.velocitypowered.api.command.SimpleCommand;
import dev.whitescan.consolefilter.share.ConsoleFilterShare;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Reload command for Velocity.
 *
 * @author Whitescan
 * @since 1.0.0
 */
@AllArgsConstructor
public class VelocityReloadConsoleFilterCommand implements SimpleCommand {

    private final VelocityConsoleFilter velocityConsoleFilter;

    @Override
    public void execute(Invocation invocation) {
        velocityConsoleFilter.loadConfigs();
        invocation.source().sendRichMessage(velocityConsoleFilter.getConfigReloadedMessage());
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        return List.of();
    }

    @Override
    public CompletableFuture<List<String>> suggestAsync(Invocation invocation) {
        return CompletableFuture.completedFuture(List.of());
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission(ConsoleFilterShare.COMMAND_RELOAD);
    }

}