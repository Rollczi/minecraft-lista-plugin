package dev.rollczi.minecraftlista.vote;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

class VoteHttpRequestReceived {

    private static final Type RESPONSE_TYPE = new TypeToken<ArrayList<VoteHttpDto>>(){}.getType();
    private static final String RECEIVE_AWARD_URL = "https://minecraft-lista.pl/api/receive/%s";

    private final Gson gson = new Gson();
    private final OkHttpClient client;
    private final VoteHttpSettings voteHttpSettings;

    VoteHttpRequestReceived(OkHttpClient client, VoteHttpSettings voteHttpSettings) {
        this.client = client;
        this.voteHttpSettings = voteHttpSettings;
    }

    List<VoteHttpDto> receive() {
        Request request = new Request.Builder()
                .url(String.format(RECEIVE_AWARD_URL, voteHttpSettings.serverId()))
                .header("User-Agent", "MinecraftListaPlugin/1.1.0")
                .header("Authorization", voteHttpSettings.apiKey())
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            return this.handleResponse(response);
        }
        catch (IOException exception) {
            throw new VoteException(exception);
        }
    }

    private List<VoteHttpDto> handleResponse(Response response) {
        try {
            if (!response.isSuccessful()) {
                throw VoteException.newException(response);
            }

            ResponseBody responseBody = response.body();

            if (responseBody == null) {
                throw new IOException("Response body is null");
            }

            return gson.fromJson(responseBody.string(), RESPONSE_TYPE);
        }
        catch (IOException | JsonParseException exception) {
            throw new VoteException(exception);
        }
    }
}
