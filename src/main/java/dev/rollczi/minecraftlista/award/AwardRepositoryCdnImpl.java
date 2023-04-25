package dev.rollczi.minecraftlista.award;

import dev.rollczi.minecraftlista.config.ReloadableConfig;
import net.dzikoysk.cdn.entity.Contextual;
import net.dzikoysk.cdn.entity.Description;
import net.dzikoysk.cdn.source.Resource;
import net.dzikoysk.cdn.source.Source;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

class AwardRepositoryCdnImpl implements AwardRepository, ReloadableConfig {

    @Description("# Lista nagród (nagroda jest losowana z listy)")
    List<CommandAward> awards = Arrays.asList(
            new CommandAward(),
            new CommandAward(Collections.singletonList("minecraft:give {player} minecraft:diamond_sword 1"))
    );

    @Override
    public CompletableFuture<Collection<Award>> getAwards() {
        return CompletableFuture.completedFuture(Collections.unmodifiableList(awards));
    }

    @Override
    public Resource resource(File folder) {
        return Source.of(folder, "awards.yml");
    }

    @Contextual
    static class CommandAward implements Award {

        @Description("# Komendy do wykonania dla jednej nagrody")
        List<String> commands = Arrays.asList("minecraft:give {player} minecraft:golden_apple 16", "minecraft:give {player} minecraft:beacon 1");

        CommandAward() {}

        CommandAward(List<String> commands) {
            this.commands = commands;
        }

        @Override
        public List<String> getCommands(Player player) {
            return commands.stream()
                    .map(command -> command.replace("{player}", player.getName()))
                    .collect(Collectors.toList());
        }

    }

}