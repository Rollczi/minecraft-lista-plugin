package dev.rollczi.minecraftlista.request;

import dev.rollczi.minecraftlista.award.AwardException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;

class RequestConfirmVote {

    private static final String RECEIVE_AWARD_URL = "https://minecraft-lista.pl/api/confirm/%s/";

    private final OkHttpClient client;
    private final RequestSettings settings;

    RequestConfirmVote(OkHttpClient client, RequestSettings settings) {
        this.client = client;
        this.settings = settings;
    }

    boolean confirm(Vote vote) {
        Request request = new Request.Builder()
                .url(String.format(RECEIVE_AWARD_URL, vote.getId()))
                .header("User-Agent", "MinecraftListaPlugin/1.0.0")
                .header("Authorization", settings.apiKey())
                .post(RequestBody.create("", MediaType.get("application/json")))
                .build();

        try (Response response = client.newCall(request).execute()) {
            return this.handleResponse(response);
        } catch (IOException exception) {
            throw new AwardException(exception);
        }
    }

    private boolean handleResponse(Response response) throws IOException {
        if (!response.isSuccessful()) {
            throw ResponseUtil.getException(response);
        }

        ResponseBody responseBody = response.body();

        if (responseBody == null) {
            throw new IOException("Response body is null");
        }

        String info = responseBody.string();

        return info.equals("OK");
    }

}
