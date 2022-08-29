package dev.rollczi.minecraftlista.award;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import dev.rollczi.minecraftlista.util.RandomUtil;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class AwardService {

    private final PickupAwardResolver pickupAwardResolver;
    private final AwardRepository awardRepository;
    private final AwardOperator awardOperator;
    private final Cache<UUID, Boolean> coolDown;

    public AwardService(PickupAwardResolver pickupAwardResolver, AwardRepository awardRepository, AwardSettings settings, AwardOperator awardOperator) {
        this.pickupAwardResolver = pickupAwardResolver;
        this.awardRepository = awardRepository;
        this.awardOperator = awardOperator;
        this.coolDown = CacheBuilder.newBuilder()
                .expireAfterWrite(settings.coolDown())
                .build();
    }

    public CompletableFuture<Result> applyAward(Player player) {
        Boolean isCoolDown = this.coolDown.getIfPresent(player.getUniqueId());

        if (isCoolDown != null) {
            return CompletableFuture.completedFuture(Result.COOL_DOWN);
        }

        this.coolDown.put(player.getUniqueId(), true);

        return pickupAwardResolver.canPickup(player.getName()).thenApplyAsync(canPickup -> {
            if (!canPickup) {
                return Result.FAILURE;
            }

            Collection<Award> awards = this.await(awardRepository.getAwards());
            Award random = RandomUtil.getRandom(awards);

            boolean success = this.await(awardOperator.applyAward(random, player));

            if (success) {
                this.await(pickupAwardResolver.markAsPickedUp(player.getName()));
            }

            return success ? Result.SUCCESS : Result.FAILURE;
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

    public enum Result {
        SUCCESS,
        FAILURE,
        COOL_DOWN,
    }

}
