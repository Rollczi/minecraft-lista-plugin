package dev.rollczi.minecraftlista.vote;

import okhttp3.Response;

class VoteException extends RuntimeException {

    public VoteException() {
    }

    public VoteException(String message) {
        super(message);
    }

    public VoteException(String message, Throwable cause) {
        super(message, cause);
    }

    public VoteException(Throwable cause) {
        super(cause);
    }

    static VoteException newException(Response response) {
        if (response.code() == 404) {
            return new VoteException("Server not found!");
        }

        if (response.code() == 403) {
            return new VoteException("Invalid API key!");
        }

        return new VoteException("Unexpected code " + response);
    }

}
