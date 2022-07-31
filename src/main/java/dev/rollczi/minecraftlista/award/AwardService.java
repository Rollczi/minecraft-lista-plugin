package dev.rollczi.minecraftlista.award;

import dev.rollczi.minecraftlista.util.RandomUtil;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class AwardService {

    private final PickupAwardResolver pickupAwardResolver;
    private final AwardRepository awardRepository;
    private final AwardOperator awardOperator;

    public AwardService(PickupAwardResolver pickupAwardResolver, AwardRepository awardRepository, AwardOperator awardOperator) {
        this.pickupAwardResolver = pickupAwardResolver;
        this.awardRepository = awardRepository;
        this.awardOperator = awardOperator;
    }

    public CompletableFuture<Boolean> applyAward(Player player) {
        return pickupAwardResolver.canPickup(player.getName()).thenApplyAsync(canPickup -> {
            if (!canPickup) {
                return false;
            }

            Collection<Award> awards = this.await(awardRepository.getAwards());
            Award random = RandomUtil.getRandom(awards);

            boolean success = this.await(awardOperator.applyAward(random, player));

            if (success) {
                this.await(pickupAwardResolver.markAsPickedUp(player.getName()));
            }

            return success;
        });
    }

    private <T> T await(CompletableFuture<T> future) {
        try {
            return future.get(15, TimeUnit.SECONDS);
        }
        catch (ExecutionException exception) {
            throw new AwardException(exception.getCause());
        }
        catch (InterruptedException | TimeoutException exception) {
            throw new AwardException(exception);
        }
    }

}
