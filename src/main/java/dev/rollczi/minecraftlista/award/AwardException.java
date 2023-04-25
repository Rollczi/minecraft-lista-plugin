package dev.rollczi.minecraftlista.award;

class AwardException extends RuntimeException {

    public AwardException() {
    }

    public AwardException(String message) {
        super(message);
    }

    public AwardException(String message, Throwable cause) {
        super(message, cause);
    }

    public AwardException(Throwable cause) {
        super(cause);
    }

}
