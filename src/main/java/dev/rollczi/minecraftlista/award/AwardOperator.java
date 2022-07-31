package dev.rollczi.minecraftlista.award;

import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

public interface AwardOperator {

    CompletableFuture<Boolean> applyAward(Award award, Player player);

}
