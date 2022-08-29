package dev.rollczi.minecraftlista.award;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BukkitAwardOperatorImpl implements AwardOperator {

    private final BukkitScheduler bukkitScheduler;
    private final Server server;
    private final Plugin plugin;

    public BukkitAwardOperatorImpl(Server server, Plugin plugin) {
        this.bukkitScheduler = server.getScheduler();
        this.server = server;
        this.plugin = plugin;
    }

    @Override
    public CompletableFuture<Boolean> applyAward(Award award, Player player) {
        CompletableFuture<Boolean> completableFuture = new CompletableFuture<>();

        this.bukkitScheduler.runTask(plugin, () -> {
            if (!player.isOnline()) {
                completableFuture.complete(false);
                return;
            }

            List<String> commands = award.getCommands(player);

            for (String command : commands) {
                server.dispatchCommand(server.getConsoleSender(), command);
            }

            completableFuture.complete(true);
        });

        return completableFuture;
    }

}
