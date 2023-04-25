package dev.rollczi.minecraftlista.vote;

import java.util.concurrent.CompletableFuture;

public class VoteFacade {

    private final VoteRepository voteRepository;

    private VoteFacade(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    public CompletableFuture<Boolean> hasValidateVote(String playerName) {
        return voteRepository.hasValidateVote(playerName);
    }

    public CompletableFuture<Void> markVoteAsInvalidate(String playerName) {
        return voteRepository.markVoteAsInvalidate(playerName);
    }

    public static VoteFacade configure(VoteHttpSettings voteHttpSettings) {
        VoteRepository voteRepository = VoteHttpRepositoryImpl.create(voteHttpSettings);

        return new VoteFacade(voteRepository);
    }

}
