package dev.rollczi.minecraftlista.award;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.List;
import java.util.concurrent.CompletableFuture;

class BukkitAwardOperatorImpl implements AwardOperator {

    private final BukkitScheduler bukkitScheduler;
    private final Server server;
    private final Plugin plugin;

    BukkitAwardOperatorImpl(Plugin plugin) {
        this.bukkitScheduler = Bukkit.getScheduler();
        this.server = Bukkit.getServer();
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
