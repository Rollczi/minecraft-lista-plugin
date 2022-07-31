package dev.rollczi.minecraftlista.request;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import dev.rollczi.minecraftlista.award.PickupAwardResolver;
import okhttp3.OkHttpClient;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HttpAwardResolver implements PickupAwardResolver {

    private static final Logger LOGGER = Logger.getLogger(HttpAwardResolver.class.getName());

    private final ExecutorService executorService = Executors.newFixedThreadPool(4, new ThreadFactoryBuilder()
            .setNameFormat("http-award-resolver-%d")
            .setUncaughtExceptionHandler((thread, throwable) -> LOGGER.log(Level.SEVERE, "Uncaught exception in thread " + thread.getName(), throwable))
            .build()
    );

    private final RequestReceivedVote requestReceivedVote;
    private final RequestConfirmVote requestConfirmVote;

    private HttpAwardResolver(RequestReceivedVote requestReceivedVote, RequestConfirmVote requestConfirmVote) {
        this.requestReceivedVote = requestReceivedVote;
        this.requestConfirmVote = requestConfirmVote;
    }

    @Override
    public CompletableFuture<Boolean> canPickup(String playerName) {
        return CompletableFuture.supplyAsync(() -> this.findVote(playerName).isPresent(), executorService);
    }

    @Override
    public CompletableFuture<Void> markAsPickedUp(String playerName) {
        return CompletableFuture.runAsync(() -> {
            Optional<Vote> vote = this.findVote(playerName);

            if (!vote.isPresent()) {
                return;
            }

            requestConfirmVote.confirm(vote.get());
        }, executorService);
    }

    private Optional<Vote> findVote(String playerName) {
        List<Vote> received = requestReceivedVote.receive();

        for (Vote vote : received) {
            if (vote.getPlayerName().equals(playerName)) {
                return Optional.of(vote);
            }
        }

        return Optional.empty();
    }

    public static HttpAwardResolver create(RequestSettings settings) {
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestReceivedVote requestReceivedVote = new RequestReceivedVote(okHttpClient, settings);
        RequestConfirmVote requestConfirmVote = new RequestConfirmVote(okHttpClient, settings);

        return new HttpAwardResolver(requestReceivedVote, requestConfirmVote);
    }

}
