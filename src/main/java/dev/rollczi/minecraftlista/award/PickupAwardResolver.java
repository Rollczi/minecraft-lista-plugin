package dev.rollczi.minecraftlista.award;

import java.util.concurrent.CompletableFuture;

public interface PickupAwardResolver {

    CompletableFuture<Boolean> canPickup(String playerName);

    CompletableFuture<Void> markAsPickedUp(String playerName);

}
