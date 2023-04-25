package dev.rollczi.minecraftlista.award;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

interface AwardRepository {

    CompletableFuture<Collection<Award>> getAwards();

}
