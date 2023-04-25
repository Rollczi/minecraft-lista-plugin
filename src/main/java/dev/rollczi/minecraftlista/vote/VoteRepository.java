package dev.rollczi.minecraftlista.vote;

import java.util.concurrent.CompletableFuture;

interface VoteRepository {

    CompletableFuture<Boolean> hasValidateVote(String playerName);

    CompletableFuture<Void> markVoteAsInvalidate(String playerName);

}
