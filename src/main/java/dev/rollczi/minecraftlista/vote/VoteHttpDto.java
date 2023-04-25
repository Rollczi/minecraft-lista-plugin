package dev.rollczi.minecraftlista.vote;

class VoteHttpDto {

    private final long id;
    private final String playerName;

    VoteHttpDto(long id, String playerName) {
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
