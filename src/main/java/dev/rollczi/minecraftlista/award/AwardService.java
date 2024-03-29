package dev.rollczi.minecraftlista.award;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import dev.rollczi.minecraftlista.vote.VoteFacade;
import dev.rollczi.minecraftlista.util.RandomUtil;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

class AwardService {

    private static final Logger LOGGER = Logger.getLogger(AwardService.class.getName());
    private final ExecutorService executorService = Executors.newFixedThreadPool(8, new ThreadFactoryBuilder()
            .setNameFormat("AwardService-%d")
            .setUncaughtExceptionHandler((thread, throwable) -> LOGGER.log(Level.SEVERE, "Uncaught exception in thread " + thread.getName(), throwable))
            .build()
    );

    private final VoteFacade voteFacade;
    private final AwardRepository awardRepository;
    private final AwardOperator awardOperator;
    private final Cache<UUID, Boolean> coolDown;

    public AwardService(VoteFacade voteFacade, AwardRepository awardRepository, AwardSettings settings, AwardOperator awardOperator) {
        this.voteFacade = voteFacade;
        this.awardRepository = awardRepository;
        this.awardOperator = awardOperator;
        this.coolDown = CacheBuilder.newBuilder()
                .expireAfterWrite(settings.coolDown().toMillis(), TimeUnit.MILLISECONDS)
                .build();
    }

    CompletableFuture<Result> applyAward(Player player) {
        Boolean isCoolDown = this.coolDown.getIfPresent(player.getUniqueId());

        if (isCoolDown != null) {
            return CompletableFuture.completedFuture(Result.COOL_DOWN);
        }

        this.coolDown.put(player.getUniqueId(), true);

        return CompletableFuture.supplyAsync(() -> {
            Boolean canPickup = this.await(voteFacade.hasValidateVote(player.getName()));

            if (!canPickup) {
                return Result.FAILURE;
            }

            Collection<Award> awards = this.await(awardRepository.getAwards());
            Award random = RandomUtil.getRandom(awards);

            boolean success = this.await(awardOperator.applyAward(random, player));

            if (success) {
                this.await(voteFacade.markVoteAsInvalidate(player.getName()));
            }

            return success ? Result.SUCCESS : Result.FAILURE;
        }, executorService);
    }

    private <T> T await(CompletableFuture<T> future) {
        try {
            return future.get(10, TimeUnit.SECONDS);
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
