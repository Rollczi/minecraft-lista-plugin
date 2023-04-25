package dev.rollczi.minecraftlista.vote;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import okhttp3.OkHttpClient;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

class VoteHttpRepositoryImpl implements VoteRepository {

    private static final Logger LOGGER = Logger.getLogger(VoteHttpRepositoryImpl.class.getName());

    private final ExecutorService executorService = Executors.newFixedThreadPool(4, new ThreadFactoryBuilder()
            .setNameFormat("VoteHttpRepositoryImpl-%d")
            .setUncaughtExceptionHandler((thread, throwable) -> LOGGER.log(Level.SEVERE, "Uncaught exception in thread " + thread.getName(), throwable))
            .build()
    );

    private final VoteHttpRequestReceived requestReceivedVote;
    private final VoteHttpRequestConfirm requestConfirmVote;

    private VoteHttpRepositoryImpl(VoteHttpRequestReceived requestReceivedVote, VoteHttpRequestConfirm requestConfirmVote) {
        this.requestReceivedVote = requestReceivedVote;
        this.requestConfirmVote = requestConfirmVote;
    }

    @Override
    public CompletableFuture<Boolean> hasValidateVote(String playerName) {
        return CompletableFuture.supplyAsync(() -> this.findVote(playerName).isPresent(), executorService);
    }

    @Override
    public CompletableFuture<Void> markVoteAsInvalidate(String playerName) {
        return CompletableFuture.runAsync(() -> {
            Optional<VoteHttpDto> vote = this.findVote(playerName);

            if (!vote.isPresent()) {
                return;
            }

            requestConfirmVote.confirm(vote.get());
        }, executorService);
    }

    private Optional<VoteHttpDto> findVote(String playerName) {
        List<VoteHttpDto> received = requestReceivedVote.receive();

        for (VoteHttpDto vote : received) {
            if (vote.getPlayerName().equals(playerName)) {
                return Optional.of(vote);
            }
        }

        return Optional.empty();
    }

    public static VoteHttpRepositoryImpl create(VoteHttpSettings settings) {
        OkHttpClient okHttpClient = new OkHttpClient();
        VoteHttpRequestReceived requestReceivedVote = new VoteHttpRequestReceived(okHttpClient, settings);
        VoteHttpRequestConfirm requestConfirmVote = new VoteHttpRequestConfirm(okHttpClient, settings);

        return new VoteHttpRepositoryImpl(requestReceivedVote, requestConfirmVote);
    }

}
