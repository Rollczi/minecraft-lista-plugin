package dev.rollczi.minecraftlista.request;

class Vote {

    private final long id;
    private final String playerName;

    Vote(long id, String playerName) {
        this.id = id;
        this.playerName = playerName;
    }

    long getId() {
        return id;
    }

    String getPlayerName() {
        return playerName;
    }

}
