package dev.rollczi.minecraftlista.request;

import dev.rollczi.minecraftlista.award.AwardException;
import okhttp3.Response;

final class ResponseUtil {

    private ResponseUtil() {}

    static AwardException getException(Response response) {
        if (response.code() == 404) {
            return new AwardException("Server is not found!");
        }

        if (response.code() == 403) {
            return new AwardException("Invalid API key!");
        }

        return new AwardException("Unexpected code " + response);
    }

}
